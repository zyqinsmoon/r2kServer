package com.apabi.r2k.msg.service.impl.client;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.msg.model.MsgResults;
import com.apabi.r2k.msg.service.MsgProducer;
import com.apabi.r2k.msg.service.impl.MsgRequest;
import com.apabi.r2k.msg.service.impl.MsgResponse;

public class PaperReplyMsgProducer implements MsgProducer{

	private Logger log = LoggerFactory.getLogger(PaperReplyMsgProducer.class);
	private String beantypes;
	
	@Override
	public void produceMsg(MsgRequest msgRequest, MsgResponse msgResponse) throws Exception {
		log.info("produceMsg:客户端开始生产报纸处理结果消息");
		List msgs = MsgResults.getMsgs();
		if(CollectionUtils.isNotEmpty(msgs)){
			for(Object obj : msgs){
				msgResponse.addMessage(obj, GlobalConstant.MESSAGE_TYPE_PAPERREPLY);
			}
		}
	}



}
