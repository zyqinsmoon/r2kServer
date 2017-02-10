package com.apabi.r2k.common.base;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.converters.basic.IntConverter;

public class XmlIntConverter extends IntConverter {

	public XmlIntConverter() {
		super();
	}
	
	@Override
	public Object fromString(String str) {
		if(StringUtils.isEmpty(str) || !StringUtils.isNumeric(str)){
			return 0;
		}
		return super.fromString(str);
	}
}
