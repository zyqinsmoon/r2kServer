package com.apabi.r2k.admin.utils;

import java.util.HashMap;
import java.util.Map;

import com.apabi.r2k.common.utils.GlobalConstant;

public enum ColumnType {

	Article(GlobalConstant.TYPE_ARTICLE, GlobalConstant.TEMPLATE_ARTICLE),
	Video(GlobalConstant.TYPE_VIDEO, GlobalConstant.TEMPLATE_VIDEO),
	Pictures(GlobalConstant.TYPE_PICTURES, GlobalConstant.TEMPLATE_PICTURE_LIST),
	ArticleColumns(GlobalConstant.TYPE_ARTICLE_COLUMNS, GlobalConstant.TEMPLATE_ARTICLE_LIST),
	PictureColumns(GlobalConstant.TYPE_PICTURE_COLUMNS, GlobalConstant.TEMPLATE_PICTURE_GROUP),
	VideoColumns(GlobalConstant.TYPE_VIDEO_COLUMNS, GlobalConstant.TEMPLATE_VIDEO_LIST);
	
	private String columnType;
	private String templateType;
	
	private ColumnType(String columnType, String templateType) {
		this.columnType = columnType;
		this.templateType = templateType;
	}

	//获取内容对应的模板类型
	public static String getTemplateType(String colType){
		ColumnType[] columnTypes = ColumnType.values();
		ColumnType columnType = null;
		for(ColumnType ct : columnTypes){
			if(ct.getColumnType().equals(colType)){
				columnType = ct;
				break;
			}
		}
		if(columnType == null){
			return null;
		}
		return columnType.getTemplateType();
	}
	
	//获取内容类型/模板类型map
	public static Map<String, String> getColumnTemplateMap(){
		ColumnType[] columnTypes = ColumnType.values();
		Map<String, String> map = new HashMap<String, String>();
		for(ColumnType ct : columnTypes){
			map.put(ct.getColumnType(), ct.getTemplateType());
		}
		return map;
	}
	
	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
}
