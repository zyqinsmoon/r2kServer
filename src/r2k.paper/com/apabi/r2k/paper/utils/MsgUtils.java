package com.apabi.r2k.paper.utils;

import java.util.Date;

import com.apabi.r2k.common.base.ServerModelTrandsform;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.paper.model.MsgBody;
import com.apabi.r2k.paper.model.SyncMessage;

public class MsgUtils {

	//创建报纸消息体xml
	public static String createPaperMsgBody(String paperId, String periodId){
		MsgBody msgBody = new MsgBody();
		msgBody.setType(SyncMessage.TYPE_PAPER);
		msgBody.setPaper(paperId);
		msgBody.setPeriod(periodId);
		return ServerModelTrandsform.objToXml(msgBody);
	}
	
	//创建filter消息体xml
	public static String createFilterMsgBody(){
		MsgBody msgBody = new MsgBody();
		msgBody.setType(SyncMessage.TYPE_FILTER);
		return ServerModelTrandsform.objToXml(msgBody);
	}
	
	public static String createMsgBody(String msgType){
		MsgBody msgBody = new MsgBody();
		msgBody.setType(msgType);
		return ServerModelTrandsform.objToXml(msgBody);
	}
	
	//创建缓存同步消息
	public static SyncMessage createDevMsg(String msgBody, String orgId,String deviceType,String deviceId, double score,String msgType){
		return createSyncMsg(msgBody, orgId, null,deviceType,deviceId, score, msgType);
	}
	
	//创建用户同步消息
	public static SyncMessage createUserMsg(String msgBody,String orgId, String userId, double score,String msgType){
		return createSyncMsg(msgBody, orgId, userId, null, null, score, msgType);
	}
	
	
	//创建同步消息
	public static SyncMessage createSyncMsg(String msgBody, String orgId, String userId,String deviceType, String deviceId, double score,String msgType){
		Date now = new Date();
		int expiredDays = PropertiesUtil.getInt("r2k.msg.expired");
		Date expiredDate = new Date(System.currentTimeMillis() + expiredDays * 24 * 60 * 60 * 1000);
		SyncMessage syncMessage = new SyncMessage();
		syncMessage.setOrgId(orgId);
		syncMessage.setUserId(userId);
		syncMessage.setCrtDate(now);
		syncMessage.setIsTop(0);
		syncMessage.setExpiredDate(expiredDate);
		syncMessage.setStatus(GlobalConstant.SYNC_MESSAGE_STATUS_UNSEND);
		syncMessage.setTimeouts(0);
		syncMessage.setScore(score);
		syncMessage.setMsgBody(msgBody);
		syncMessage.setType(msgType);
		syncMessage.setDeviceType(deviceType);
		syncMessage.setDeviceId(deviceId);
		return syncMessage;
	}
}
