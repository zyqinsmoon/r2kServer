package com.apabi.r2k.paper.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.PaperAuth;
import com.apabi.r2k.admin.model.PaperSub;
import com.apabi.r2k.admin.service.PaperAuthService;
import com.apabi.r2k.admin.service.PaperSubService;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.paper.dao.SyncMessageDao;
import com.apabi.r2k.paper.model.SyncMessage;
import com.apabi.r2k.paper.service.SyncMessageService;
import com.apabi.r2k.paper.utils.MsgUtils;

@Service("syncMessageService")
public class SyncMessageServiceImpl implements SyncMessageService {

	private Logger log = LoggerFactory.getLogger(SyncMessageServiceImpl.class);
	
	public static final String PAGE_QUERY_STATEMENT = ".pageSelect";
	
	@Resource(name="syncMessageDao")
	private SyncMessageDao syncMessageDao;
	@Resource(name="paperAuthService")
	private PaperAuthService paperAuthService;
	@Resource(name="paperSubService")
	private PaperSubService paperSubService;
	
	public void setSyncMessageDao(SyncMessageDao syncMessageDao){
		this.syncMessageDao = syncMessageDao;
	}
	
	public SyncMessageDao getSyncMessageDao(){
		return syncMessageDao;
	}
	
	//入库
	@Transactional(propagation = Propagation.REQUIRED)
	public int save(SyncMessage syncMessage) throws Exception {
		return syncMessageDao.save(syncMessage);
	}
	
	//更新
	@Transactional(propagation = Propagation.REQUIRED)
	public int update(SyncMessage syncMessage) throws Exception {
		Map msgMap = new HashMap();
		msgMap.put("id", syncMessage.getId());
		msgMap.put("status", syncMessage.getStatus());
		return syncMessageDao.update(msgMap);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception {
		return syncMessageDao.pageQuery(PAGE_QUERY_STATEMENT, pageRequest, countQuery);
	}

	//根据id查询
	@Override
	public SyncMessage getById(int id) throws Exception {
		return syncMessageDao.getById(id);
	}

	@Override
	public List<SyncMessage> getCacheMsgs(Map params) throws Exception {
		return syncMessageDao.getCacheMsgs(params);
	}

	//保存报纸消息
	@Override
	public void savePaperMsg(String paperId, String periodId, String publishDate) throws Exception {
		if(StringUtils.isNotBlank(paperId) && StringUtils.isNotBlank(periodId)){
//			log.info("savePaperMsg:[paperId#"+paperId+",periodId#"+periodId+"]:创建报纸消息体");
			String msgBody = MsgUtils.createPaperMsgBody(paperId, periodId);
			//获取报纸优先级进行打分
			int priority = 0;
			double score = getPaperMsgScore(0, priority);
			//创建报纸消息
			List<SyncMessage> syncMsgs = new ArrayList<SyncMessage>();
			List<SyncMessage> orgSyncMsgs = createCacheMsg(msgBody, paperId, score, publishDate);
			syncMsgs.addAll(orgSyncMsgs);
			List<SyncMessage> devSyncMsgs = createUserMsg(msgBody, paperId, score, publishDate);
			syncMsgs.addAll(devSyncMsgs);
			log.info("savePaperMsg:[paperId#"+paperId+",periodId#"+periodId+",publishDate#"+publishDate+"]:批量保存消息");
			syncMessageDao.batchSaveMsgs(syncMsgs);
		}
	}

	//打分
	public double getPaperMsgScore(double order, double priority){
		//300*顺序/3^顺序+10/(1/3^优先级+1)
		double score = 300*order/Math.pow(3, order) + 10/(1/Math.pow(3, priority) + 1);
		return score;
	}
	
	//分发filter消息
	@Transactional(propagation = Propagation.REQUIRED)
	public void distributeFilterMsg(String orgId) throws Exception{
		String msgBody = MsgUtils.createFilterMsgBody();
		String devType = GlobalConstant.USER_AGENT_SLAVE;
		String deviceId = orgId;	//缓存设备id为机构id
		SyncMessage msg = MsgUtils.createDevMsg(msgBody, orgId,devType,deviceId, /*临时值*/1, SyncMessage.TYPE_FILTER);
//		setFilterMsgsInvalid(orgId);
		setDevMsgsInvalid(orgId, devType, deviceId, null, SyncMessage.TYPE_FILTER);
		save(msg);
	}
	
	//保存配置消息
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveConfigMsg(String orgId, String deviceType, String deviceId) throws Exception {
		//创建配置消息
		String msgBody = MsgUtils.createMsgBody(SyncMessage.TYPE_CONFIG);	
		SyncMessage msg = MsgUtils.createDevMsg(msgBody, orgId, deviceType, deviceId, /*临时值*/1, SyncMessage.TYPE_CONFIG);
		//将之前所有的配置消息置为失效
		setDevMsgsInvalid(orgId, deviceType, deviceId, null, SyncMessage.TYPE_CONFIG);
		save(msg);
	}
	
	private int setDevMsgsInvalid(String orgId, String deviceType, String deviceId, String userId, String msgType) throws Exception{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("orgId", orgId);
		parameters.put("userId", userId);
		parameters.put("deviceType", deviceType);
		parameters.put("deviceId", deviceId);
		parameters.put("status", GlobalConstant.SYNC_MESSAGE_STATUS_INVALID);
		parameters.put("type", msgType);
		return syncMessageDao.updateByType(parameters);
	}
	
	@Override
	public void batchUpdateMsgs(List<SyncMessage> syncMessages)
			throws Exception {
		syncMessageDao.batchUpdateMsgs(syncMessages);
	}
	
	//删除设备消息
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteDevMsgs(String orgId, String devType, String deviceId) throws Exception {
		Map<String , Object> params = new HashMap<String, Object>();
		params.put("orgId", orgId);
		params.put("devType", devType);
		params.put("deviceId", deviceId);
		return syncMessageDao.deleteMsgs(params);
	}

	//获取设备消息
	@Override
	public List<SyncMessage> getDeviceMsgs(String orgId, String devType, String deviceId) throws Exception {
		Map<String , Object> params = new HashMap<String, Object>();
		params.put("orgId", orgId);
		params.put("deviceType", devType);
		params.put("deviceId", deviceId);
		params.put("status", SyncMessage.STATUS_UNSEND);
		return syncMessageDao.getDeviceMsgs(params);
	}
	
	//创建机构的更新消息
	private List<SyncMessage> createCacheMsg(String msgBody, String paperId, double score, String publishDate) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("paperId", paperId);
		List<SyncMessage> syncMessages = new ArrayList<SyncMessage>();
		String devType = GlobalConstant.USER_AGENT_SLAVE;
//		log.info("createOrgMsg:[paperId#"+paperId+"]:获取该报纸授权数据");
		//报纸授权机构列表
		List<PaperAuth> paperAuths = paperAuthService.findByPaperAndDate(paperId, publishDate);
		if(CollectionUtils.isNotEmpty(paperAuths)){
			for(PaperAuth paperAuth : paperAuths){
				String orgId = paperAuth.getOrgId();
				String deviceId = orgId;
				SyncMessage syncMessage = MsgUtils.createDevMsg(msgBody, orgId, devType, deviceId, score, SyncMessage.TYPE_PAPER);
				syncMessages.add(syncMessage);
			}
		}
		return syncMessages;
	}
	
	//创建用户消息
	private List<SyncMessage> createUserMsg(String msgBody, String paperId, double score, String publishDate) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("paperId", paperId);
		List<SyncMessage> syncMessages = new ArrayList<SyncMessage>();
//		log.info("createUserMsg:[paperId#"+paperId+"]:获取该报纸订阅数据");
		//报纸订阅列表
		List<PaperSub> paperSubs = paperSubService.findByPaperAndDate(paperId, publishDate);
		if(CollectionUtils.isNotEmpty(paperSubs)){
			for(PaperSub paperSub : paperSubs){
				SyncMessage syncMessage = MsgUtils.createUserMsg(msgBody, paperSub.getOrgId(), paperSub.getUserId(), score, SyncMessage.TYPE_PAPER);
				syncMessages.add(syncMessage);
			}
		}
		return syncMessages;
	}
}
