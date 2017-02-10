package com.apabi.r2k.admin.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Subscribe")
public class Subscribe {

	@XStreamAlias("type")
	@XStreamAsAttribute
	private String type;
	@XStreamAlias("Resources")
	private List resources;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List getResources() {
		return resources;
	}

	public void setResources(List resources) {
		this.resources = resources;
	}
}
