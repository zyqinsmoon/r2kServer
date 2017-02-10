package com.apabi.r2k.paper.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 消息体
 */
@XStreamAlias("Msg")
public class MsgBody {

	@XStreamAsAttribute
	private String type;		//消息类型:报纸消息:paper filter消息:filter
	@XStreamAlias("Paper")
	private String paper;		//报纸id
	@XStreamAlias("Period")
	private String period;		//期次id

	public MsgBody(){}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPaper() {
		return paper;
	}

	public void setPaper(String paper) {
		this.paper = paper;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
}
