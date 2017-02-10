package com.apabi.r2k.common.solr;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Error")
public class SolrResult {
	
	@XStreamAlias("code")
	@XStreamAsAttribute
	private String code;
	@XStreamAlias("message")
	@XStreamAsAttribute
	private String message;
	
	public SolrResult(){}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
