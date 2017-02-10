package com.apabi.r2k.paper.model;

public class SyncMessage {
	
	private java.lang.Integer id;
	private java.lang.String orgId;
	private java.lang.String userId;
	private String deviceType;
	private String deviceId;
	private java.lang.Integer isTop;
	private java.lang.Double score;
	private java.util.Date crtDate;
	private java.util.Date expiredDate;
	private java.util.Date lastSendDate;
	private java.lang.Integer status;
	private java.lang.Integer timeouts;
	private String type;
	private String msgBody;

	//消息类型
	public final static String TYPE_PAPER = "paper";		//报纸
	public final static String TYPE_FILTER = "filter";		//filter
	public final static String TYPE_CONFIG = "config";		//配置
	
	//消息状态
	public final static int STATUS_UNSEND = 0;		//未发送
	public final static int STATUS_DEAL = 1;		//处理中
	public final static int STATUS_SUCCESS = 2;		//处理成功
	public final static int STATUS_FAIL = 3;		//处理失败
	public final static int STATUS_INVALID = 4;		//失效
	
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setOrgId(java.lang.String value) {
		this.orgId = value;
	}
	
	public java.lang.String getOrgId() {
		return this.orgId;
	}
	public void setUserId(java.lang.String value) {
		this.userId = value;
	}
	
	public java.lang.String getUserId() {
		return this.userId;
	}
	public void setIsTop(java.lang.Integer value) {
		this.isTop = value;
	}
	
	public java.lang.Integer getIsTop() {
		return this.isTop;
	}
	public void setScore(java.lang.Double value) {
		this.score = value;
	}
	
	public java.lang.Double getScore() {
		return this.score;
	}
	
	public void setCrtDate(java.util.Date value) {
		this.crtDate = value;
	}
	
	public java.util.Date getCrtDate() {
		return this.crtDate;
	}
	
	public void setExpiredDate(java.util.Date value) {
		this.expiredDate = value;
	}
	
	public java.util.Date getExpiredDate() {
		return this.expiredDate;
	}
	
	public void setLastSendDate(java.util.Date value) {
		this.lastSendDate = value;
	}
	
	public java.util.Date getLastSendDate() {
		return this.lastSendDate;
	}
	public void setStatus(java.lang.Integer value) {
		this.status = value;
	}
	
	public java.lang.Integer getStatus() {
		return this.status;
	}
	public void setTimeouts(java.lang.Integer value) {
		this.timeouts = value;
	}
	
	public java.lang.Integer getTimeouts() {
		return this.timeouts;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
}

