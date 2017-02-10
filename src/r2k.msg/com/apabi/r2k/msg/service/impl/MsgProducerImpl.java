package com.apabi.r2k.msg.service.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.apabi.r2k.msg.service.MsgProducer;

public class MsgProducerImpl implements MsgProducer{
	private Properties msgProp ;
	public Properties getMsgProp() {
		return msgProp;
	}
	public void setMsgProp(Properties msgProp) {
		this.msgProp = msgProp;
	}


	@Override
	public void produceMsg(MsgRequest msgRequest,MsgResponse msgResponse) throws Exception {
		Set<Map.Entry<Object,Object>> sets = msgProp.entrySet();
		String serverType = msgRequest.getServerType();
		for (Iterator iterator = sets.iterator(); iterator.hasNext();) {
			Map.Entry  map = (Map.Entry) iterator.next();
			Object obj = map.getValue();
		/*	Method met = obj.getClass().getMethod("getBeantypes");
			Properties prop = (Properties) met.invoke(obj);
			for (Entry<Object, Object> entry : prop.entrySet()) {
				String propKey = (String)entry.getKey();
				System.out.println("propKey:["+propKey+"]serverType:["+serverType+"]");
				if(propKey.indexOf(serverType)>-1){
					System.out.println("执行"+map.getKey());*/
					MsgProducer msgProducer = (MsgProducer) obj;
					msgProducer.produceMsg(msgRequest, msgResponse);
			//	}
			//}
		}
		
	}

	
}
