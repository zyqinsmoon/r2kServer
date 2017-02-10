package com.apabi.r2k.msg.model;

/**
 * 报纸处理结果返回消息
 * @author l.wen
 */
public class ReplyHandleMsg {

	private int id;				//消息id
	private int status;			//消息处理状态
	
	public ReplyHandleMsg(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
