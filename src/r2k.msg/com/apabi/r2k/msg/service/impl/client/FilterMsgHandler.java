package com.apabi.r2k.msg.service.impl.client;

import java.io.InputStream;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpUtils;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.XmlUtil;
import com.apabi.r2k.msg.model.FilterMsg;
import com.apabi.r2k.msg.model.MsgResults;
import com.apabi.r2k.msg.model.ReplyHandleMsg;
import com.apabi.r2k.msg.service.MsgHandler;
import com.apabi.r2k.msg.service.impl.MsgRequest;
import com.apabi.r2k.msg.service.impl.MsgResponse;

public class FilterMsgHandler implements MsgHandler {

	private Logger log = LoggerFactory.getLogger(FilterMsgHandler.class);
	
	@Override
	public void handlerMsg(MsgRequest msgRequest, MsgResponse msgResponse) {
		FilterMsg msg = (FilterMsg) msgRequest.getMsg();		//获取报纸消息
		if(msg == null){
			return;
		}
		try {
			log.info("handlerMsg:[msgid#"+msg.getId()+"]:开始处理filter消息");
			Document filterDoc = XmlUtil.getDocumentFromString(msg.getFilter());
//			log.info("handlerMsg:获取本地已缓存报纸"+GlobalConstant.URL_PAPER_GET+"&from=1&to=10000");
//			Document paperDoc = XmlUtil.getDataFromSolr(GlobalConstant.URL_PAPER_GET+"&from=1&to=10000");
//			String filterXml = modifyFilterXml(filterDoc, paperDoc);
			String filterXml = getImportXml(filterDoc);
			System.out.println(filterXml);
			InputStream in = HttpUtils.httpPostByStream(GlobalConstant.URL_FILTER_PAPER_CREATE, filterXml, GlobalConstant.HTTP_DATA_TYPE_XML);;
			String resultCode = getResultCode(in);
			log.info("handlerMsg:filter创建状态:"+resultCode);
			createReplyMsg(msg, resultCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//将solr查询的filter xml转换成solr入库需要的xml
	@SuppressWarnings("unchecked")
	private String getImportXml(Document filterDoc){
		//获取所有filter节点
		List<Element> filters = XmlUtil.getNodes(filterDoc, "//Filter");
		String orgid = PropertiesUtil.get("client.orgid");
		Document importDoc = DocumentHelper.createDocument();
		Element root = importDoc.addElement("R2k");
		Element recommands = root.addElement("PaperRecommends");
		recommands.addAttribute("id", orgid);
		for(Element filter : filters){
			//获取报纸id
			String paperid = filter.elementText("Paper");
			//获取报纸可读范围开始时间
			String startDate = translateDateString(filter.elementText("StartDate"));
			//获取报纸可读范围结束时间
			String endDate = translateDateString(filter.elementText("EndDate"));
			//获取报纸位置
			String position = filter.elementText("position");
			createFilterElement(recommands, paperid, startDate, endDate, position);
		}
		return importDoc.asXML();
	}

	//转换filter xml
	private String modifyFilterXml(Document filterDoc, Document paperDoc){
		List<Element> filters = XmlUtil.getNodes(filterDoc, "//Filter");
		String defaultStartDate = "1970-01-01 00:00:00.000";
		String defaultEndDate = "9999-12-31 00:00:00.000";
		if(filters.size() > 0){
			Element firstFilter = filters.get(0);
			defaultStartDate = translateDateString(firstFilter.elementText("StartDate"));
			defaultEndDate = translateDateString(firstFilter.elementText("EndDate"));
		}
		String orgid = PropertiesUtil.get("client.orgid");
		Document newFilterDoc = DocumentHelper.createDocument();
		Element root = newFilterDoc.addElement("R2k");
		Element recommands = root.addElement("PaperRecommends");
		recommands.addAttribute("id", orgid);
		List<Element> papers = XmlUtil.getNodes(paperDoc, "//Paper");
		for(Element paper : papers){
			String paperid = paper.attributeValue("id");
			Element filter = (Element) XmlUtil.getSingleNodes(filterDoc, "//Filter[@id='"+orgid+"-"+paperid+"']");
			String startDate = defaultStartDate;
			String endDate = defaultEndDate;
			String position = "0";
			if(filter != null){
				startDate = translateDateString(filter.elementText("StartDate"));
				endDate = translateDateString(filter.elementText("EndDate"));
				position = filter.elementText("position");
			}
			createFilterElement(recommands, paperid, startDate, endDate, position);
		}
		return newFilterDoc.asXML();
	}
	
	//创建报纸推荐节点
	private Element createFilterElement(Element recommands, String paperid, String startDate, String endDate, String position){
		Element recommand = recommands.addElement("PaperRecommend");
		Element paper = recommand.addElement("Paper");
		paper.setText(paperid);
		Element eStartDate = recommand.addElement("StartDate");
		eStartDate.setText(startDate);
		Element eEndDate = recommand.addElement("EndDate");
		eEndDate.setText(endDate);
		Element ePosition = recommand.addElement("Position");
		ePosition.setText(position);
		return recommand;
	}
	
	//solr查询出来的时间格式为:yyyy-MM-ddThh:mm:ssZ,需要转换为yyyy-MM-dd hh:mm:ss入库
	private String translateDateString(String dateString){
		return dateString.replace("T", " ").replace("Z", "");
	}
	
	private String getResultCode(InputStream in) throws Exception{
		Document doc = XmlUtil.getDocumentFromInputStream(in);
		Attribute code = (Attribute) XmlUtil.getSingleNodes(doc, "//Error/@code");
		return code.getValue();
	}
	
	private void createReplyMsg(FilterMsg msg, String result){
		if(msg == null){
			return;
		}
		int msgid = msg.getId();
		int status = result.equals(GlobalConstant.RESULT_STATUS_SUCCESS) ? GlobalConstant.SYNC_MESSAGE_STATUS_SUCCESS : GlobalConstant.SYNC_MESSAGE_STATUS_FAIL;
		ReplyHandleMsg replyMsg = new ReplyHandleMsg();
		replyMsg.setId(msgid);
		replyMsg.setStatus(status);
		MsgResults.addMsg(replyMsg);
	}
	
	public void test(){
		try {
			Document filterDoc = XmlUtil.getDocumentFromString("<?xml version=\"1.0\" encoding=\"UTF-8\"?><R2k xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><Filters><Filter id=\"tiyan-n.D110000renmrb\"><StartDate>2014-06-01T00:00:00Z</StartDate><EndDate>2015-06-01T00:00:00Z</EndDate><Paper>n.D110000renmrb</Paper><position>21</position></Filter></Filters></R2k>");
			System.out.println(GlobalConstant.URL_PAPER_GET+"&org=tiyan");
			Document paperDoc = XmlUtil.getDataFromSolr(GlobalConstant.URL_PAPER_GET+"&org=tiyan");
			System.out.println(paperDoc.asXML());
			String filterXml = modifyFilterXml(filterDoc, paperDoc);
			System.out.println(filterXml);
			InputStream in = HttpUtils.httpPostByStream(GlobalConstant.URL_FILTER_PAPER_CREATE, filterXml, GlobalConstant.HTTP_DATA_TYPE_XML);;
			String resultCode = getResultCode(in);
			log.info("handlerMsg:filter创建状态:"+resultCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		FilterMsgHandler fmh = new FilterMsgHandler();
		fmh.test();
	}
}
