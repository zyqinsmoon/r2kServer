package com.apabi.r2k.msg.service.impl.client;

import com.apabi.r2k.msg.service.MsgProducer;
import com.apabi.r2k.msg.service.impl.MsgRequest;
import com.apabi.r2k.msg.service.impl.MsgResponse;

/**
 * 心跳消息生产类
 * @author liuyingyu
 *
 */
public class HeartbeatMsgProducer  implements MsgProducer {

	
	@Override
	public void produceMsg(MsgRequest msgRequest, MsgResponse msgResponse)
			throws Exception {
		msgResponse.addMessage("userid", "heartbeat");
		
	}


}
