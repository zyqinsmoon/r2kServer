package com.apabi.r2k.msg.service.impl;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHeaders;
import org.apache.http.protocol.HTTP;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import com.apabi.r2k.common.utils.ClientModelTrandsform;
import com.apabi.r2k.common.utils.HttpUtils;
import com.apabi.r2k.msg.model.R2kMessage;
import com.apabi.r2k.msg.service.MsgConnector;
import com.apabi.r2k.msg.service.MsgHandler;
import com.apabi.r2k.msg.service.MsgProducer;


@Service("msgConnector")
public class MsgConnectorImpl implements MsgConnector{
	@Resource
	private MsgProducer msgProducer;
	@Resource
	private MsgHandler msgHandler;
	//服务端消息处理
	public void serverHandler(HttpServletRequest request,HttpServletResponse response) throws Exception{
	
		MsgResponse msgResponse = new MsgResponse(response);
		msgResponse  = this.msgHandler(request, msgResponse);
		MsgRequest msgRequest = new MsgRequest(request);
		//获取消息
	    this.msgProducer(msgRequest, msgResponse);
	}
	

	//客户端消息处理
	@Override
	public void clientHandler(HttpServletRequest request,HttpServletResponse response) throws Exception {
		MsgRequest msgRequest = new MsgRequest(request);
		MsgResponse msgResponse = new MsgResponse(response);
		this.msgProducer(msgRequest, msgResponse);
		this.msgHandler(request, msgResponse);
	    
	}
	
	
	//客户端消息处理
	public void clientHttpHandler(String httpurl) throws Exception {
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		URL url = new URL(httpurl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(HttpUtils.DEFAULT_TIMEOUT);
		conn.setReadTimeout(HttpUtils.DEFAULT_TIMEOUT);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		MsgRequest msgRequest = new MsgRequest(request);
		MsgResponse msgResponse = new MsgResponse(response);
		msgResponse.setConn(conn);
		this.msgHttpProducer(msgRequest, msgResponse);
		this.msgHttpHandler(conn, msgResponse);
	}
	
	
	//获取消息并发送消息
	private void msgProducer(MsgRequest msgRequest,MsgResponse msgResponse) throws Exception{
	    msgProducer.produceMsg(msgRequest, msgResponse);
	    if(msgResponse.getMessage()!=null&&msgResponse.getMessage().size()>0){
	    	OutputStreamWriter writer = new OutputStreamWriter(msgResponse.getOutputStream(), HTTP.UTF_8);
	    	writer.write(ClientModelTrandsform.objToXml(msgResponse.pushMessage()));
	 		writer.close();
	    }
	   
	    
	}
	//获取消息并发送消息
	private void msgHttpProducer(MsgRequest msgRequest,MsgResponse msgResponse) throws Exception{
	    msgProducer.produceMsg(msgRequest, msgResponse);
	    if(msgResponse.getMessage()!=null&&msgResponse.getMessage().size()>0){
	    	msgResponse.getConn().addRequestProperty(HttpHeaders.CONTENT_TYPE, "text/xml");
	    	OutputStreamWriter writer = new OutputStreamWriter(msgResponse.getConn().getOutputStream(), HTTP.UTF_8);
	    	writer.write(ClientModelTrandsform.objToXml(msgResponse.pushMessage()));
	    	writer.flush();
	 		writer.close();
	    }
	}
	
	//client处理消息
	private MsgResponse msgHttpHandler(HttpURLConnection conn,MsgResponse msgResponse) throws Exception{
		InputStream in = conn.getInputStream();
		if(null!=in&&in.available()>0){
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(in);
			//消息列表
			List<R2kMessage> messageList = (List<R2kMessage>)ClientModelTrandsform.xmlToObj(doc.asXML());
			for (int i = 0; i < messageList.size(); i++) {
				HttpServletRequest request = null;
				MsgRequest msgRequest = new MsgRequest(request);
				R2kMessage message = messageList.get(i);
				msgRequest.setMsg(message.getMsg());
				msgRequest.setMsgType(message.getMsgType());
				//单个处理消息
				msgHandler.handlerMsg(msgRequest,msgResponse);
			}
		}
		in.close();
		return msgResponse;
	}
	
	
	//处理消息
	private MsgResponse msgHandler(HttpServletRequest request,MsgResponse msgResponse) throws Exception{
		InputStream in = request.getInputStream();
		if(null!=in&&in.available()>0){
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(in);
			//消息列表
			List<R2kMessage> messageList = (List<R2kMessage>)ClientModelTrandsform.xmlToObj(doc.asXML());
			for (int i = 0; i < messageList.size(); i++) {
				MsgRequest msgRequest = new MsgRequest(request);
				R2kMessage message = messageList.get(i);
				msgRequest.setMsg(message.getMsg());
				msgRequest.setMsgType(message.getMsgType());
				//单个处理消息
				msgHandler.handlerMsg(msgRequest,msgResponse);
			}
		}
		in.close();
		return msgResponse;
	}
	
	
	
}
