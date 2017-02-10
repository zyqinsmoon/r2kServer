package com.apabi.r2k.msg.service.impl.server;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apabi.r2k.msg.model.ReplyHandleMsg;
import com.apabi.r2k.msg.service.MsgHandler;
import com.apabi.r2k.msg.service.impl.MsgRequest;
import com.apabi.r2k.msg.service.impl.MsgResponse;
import com.apabi.r2k.paper.model.SyncMessage;
import com.apabi.r2k.paper.service.SyncMessageService;

/**
 * 处理客户端报纸处理结果消息类
 * @author liuyingyu
 *
 */
public class PaperReplyMsgHandler implements MsgHandler{

	private Logger log = LoggerFactory.getLogger(PaperReplyMsgHandler.class);
	
	@Resource
	private SyncMessageService syncMessageService;
	
	@Override
	public void handlerMsg(MsgRequest msgRequest, MsgResponse msgResponse) {
		ReplyHandleMsg replyMsg = (ReplyHandleMsg) msgRequest.getMsg();
		int msgid = replyMsg.getId();
		int status = replyMsg.getStatus();
		try {
			SyncMessage syncMessage = new SyncMessage();
			syncMessage.setId(msgid);
			syncMessage.setStatus(status);
			log.info("handlerMsg:[id#"+msgid+",status#"+status+"]:消息更新");
			syncMessageService.update(syncMessage);
		} catch (Exception e) {
			log.error("handlerMsg:[id#"+msgid+",status#"+status+"]:消息更新异常");
			e.printStackTrace();
		}
	}

}
