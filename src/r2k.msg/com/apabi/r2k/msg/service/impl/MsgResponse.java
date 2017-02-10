package com.apabi.r2k.msg.service.impl;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.apabi.r2k.msg.model.R2kMessage;
import com.apabi.r2k.msg.model.ReplyMsg;


public class MsgResponse {
	//private List<R2kMessage> messageList;
	private HttpServletResponse response;
	private HttpURLConnection conn;
	public HttpURLConnection getConn() {
		return conn;
	}

	public void setConn(HttpURLConnection conn) {
		this.conn = conn;
	}

	public MsgResponse(HttpServletResponse response) {
	    this.response = response;
	}

	
	private List<R2kMessage> getMessageList() {
		return ReplyMsg.replayMsgList;
	}

   public void addMessage(Object msg,String msgType){
	   R2kMessage message = new R2kMessage();
	   message.setMsgType(msgType);
	   message.setMsg(msg);
	   ReplyMsg.replayMsgList.add(message);
   }
	
	public List<R2kMessage> getMessage() {
		return ReplyMsg.replayMsgList;
	}
	public List<R2kMessage> pushMessage() {
		int size = ReplyMsg.replayMsgList.size();
		List<R2kMessage> replyMsg = new ArrayList<R2kMessage>();
		for (int i = 0; i < size; i++) {
			replyMsg.add(ReplyMsg.replayMsgList.remove(0));
		}
		//ReplyMsg.replayMsgList.removeAll(replyMsg);
		return replyMsg;
	}
	
	
	public OutputStream getOutputStream() throws Exception{
		return response.getOutputStream();
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	
}
