package com.apabi.r2k.msg.service.impl.server;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;

import com.apabi.r2k.common.base.ServerModelTrandsform;
import com.apabi.r2k.common.utils.DateUtil;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.XmlUtil;
import com.apabi.r2k.msg.model.FilterMsg;
import com.apabi.r2k.msg.model.PaperMsg;
import com.apabi.r2k.msg.service.MsgProducer;
import com.apabi.r2k.msg.service.impl.MsgRequest;
import com.apabi.r2k.msg.service.impl.MsgResponse;
import com.apabi.r2k.paper.model.MsgBody;
import com.apabi.r2k.paper.model.SyncMessage;
import com.apabi.r2k.paper.service.SyncMessageService;

/**
 * solr消息生产器
 * @author l.wen
 *
 */
public class SolrMsgProducer  implements MsgProducer{

	private String beantypes;
	@Resource(name="syncMessageService")
	private SyncMessageService syncMessageService;
	
	public void produceMsg(MsgRequest msgRequest, MsgResponse msgResponse) {
		String orgid = msgRequest.getOrgId();
		try {
			List<SyncMessage> syncMessages = getMsgFromQueue(orgid);
			createMsgs(syncMessages, msgResponse);
			updateMsgs(syncMessages);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//从队列中获取消息
	public List<SyncMessage> getMsgFromQueue(String orgid) throws Exception{
		int limit = PropertiesUtil.getInt("r2k.msg.count");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", orgid);
		params.put("deviceId", orgid);
		params.put("deviceType", GlobalConstant.USER_AGENT_SLAVE);
		params.put("limit", limit);
		Date crtDate = DateUtil.getDateBeforeMinutes(new Date(), PropertiesUtil.getInt("r2k.msg.time.differ"));
		params.put("crtDate", crtDate);
		return syncMessageService.getCacheMsgs(params);
	}
	
	//创建消息
	public void createMsgs(List<SyncMessage> syncMessages, MsgResponse msgResponse) throws Exception{
		if(CollectionUtils.isNotEmpty(syncMessages)){
			for(SyncMessage syncMessage : syncMessages){
				Object msg = null;
				String msgType = syncMessage.getType();
				//推送报纸消息
				if(msgType.equals(GlobalConstant.MESSAGE_BODY_TYPE_PAPER)){
					msg = createPaperMsg(syncMessage);
					if(msg != null){
						msgResponse.addMessage(msg, GlobalConstant.MESSAGE_TYPE_SOLRPAPER);
					}
				}
				//推送filter消息
				if(msgType.equals(GlobalConstant.MESSAGE_BODY_TYPE_FILTER)){
					msg = createFilterMsg(syncMessage);
					if(msg != null){
						msgResponse.addMessage(msg, GlobalConstant.MESSAGE_TYPE_SOLRFILTER);
					}
				}
			}
		}
	}
	
	//创建报纸消息
	public PaperMsg createPaperMsg(SyncMessage syncMessage){
		PaperMsg paperMsg = null;
		if(syncMessage == null){
			return null;
		}
		int msgid = syncMessage.getId();
		String periodId = null;
		//获取消息体，从消息体中取期次id
		MsgBody msgBody = (MsgBody) ServerModelTrandsform.xmlToObj(syncMessage.getMsgBody());
		periodId = msgBody.getPeriod();
		if(StringUtils.isBlank(periodId)){
			return null;
		}
		//拼接获取报纸期次id的url
		String url = GlobalConstant.SERVERURL + PropertiesUtil.get("r2k.msg.paper.url") + periodId;
		paperMsg = new PaperMsg();
		paperMsg.setId(msgid);
		paperMsg.setUrl(url);
		return paperMsg;
	}
	
	//创建filter消息
	public FilterMsg createFilterMsg(SyncMessage syncMessage) throws Exception{
		FilterMsg filterMsg = null;
		if(syncMessage != null){
			int msgid = syncMessage.getId();
			//获取filter xml
			String url = GlobalConstant.URL_PAPER_QUERY + GlobalConstant.FILTER_QUERY + "&org="+syncMessage.getOrgId();
			Document doc = XmlUtil.getDataFromSolr(url);
			String filterXml = doc.asXML();
			filterMsg = new FilterMsg();
			filterMsg.setId(msgid);
			filterMsg.setFilter(filterXml);
		}
		return filterMsg;
	}
	
	public void updateMsgs(List<SyncMessage> syncMessages) throws Exception{
		Date lastSendDate = new Date(); 
		for(SyncMessage message : syncMessages){
			message.setLastSendDate(lastSendDate);
			message.setTimeouts(message.getTimeouts()+1);
			if(message.getTimeouts() >= PropertiesUtil.getInt("r2k.msg.timeouts")){
				message.setStatus(GlobalConstant.SYNC_MESSAGE_STATUS_INVALID);
			}else{
				message.setStatus(GlobalConstant.SYNC_MESSAGE_STATUS_DEALING);
			}
		}
		syncMessageService.batchUpdateMsgs(syncMessages);
	}
	
	public String getBeantypes() {
		return beantypes;
	}

	public void setBeantypes(String beantypes) {
		this.beantypes = beantypes;
	}

	public SyncMessageService getSyncMessageService() {
		return syncMessageService;
	}

	public void setSyncMessageService(SyncMessageService syncMessageService) {
		this.syncMessageService = syncMessageService;
	}
}
