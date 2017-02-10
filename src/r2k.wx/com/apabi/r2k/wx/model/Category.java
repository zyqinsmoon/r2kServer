package com.apabi.r2k.wx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("CatItem")
public class Category {
	@XStreamAsAttribute
	private String Name;

    @XStreamAsAttribute
	private String Code;

    @XStreamAsAttribute
	private String ParentCode;

    @XStreamAsAttribute
	private String ResourceNumber;

    @XStreamAsAttribute
	private String HasChild;
	
    public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getParentCode() {
		return ParentCode;
	}

	public void setParentCode(String parentCode) {
		ParentCode = parentCode;
	}

	public String getResourceNumber() {
		return ResourceNumber;
	}

	public void setResourceNumber(String resourceNumber) {
		ResourceNumber = resourceNumber;
	}

	public String getHasChild() {
		return HasChild;
	}

	public void setHasChild(String hasChild) {
		HasChild = hasChild;
	}

	
}
