package com.apabi.r2k.common.email;

import org.springframework.beans.factory.annotation.Value;

public abstract class Mail {
	
	protected String to;
	
	@Value("${from}")
	protected String from;
	
	protected String subject;
	
	@Value("${failureTime}")
	protected Long failureTime;
	
	@Value("${mail.personal}")
	protected String personal ;

	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	public String getTo() {
		return to;
	}
	public String getFrom() {
		return from;
	}
	public String getSubject() {
		return subject;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Long getFailureTime() {
		return failureTime;
	}
	public void setFailureTime(Long failureTime) {
		this.failureTime = failureTime;
	}
}
