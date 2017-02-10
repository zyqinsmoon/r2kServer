package com.apabi.r2k.common.utils;

import java.util.HashMap;
import java.util.Map;

public enum TempTypeEnum {

	HOME	(0, GlobalConstant.TEMPLATE_HOME,	"主页模板"),
	COLUMN	(1, GlobalConstant.TEMPLATE_COLUMN,	"栏目模板"),
	ARTICLE	(2, GlobalConstant.TEMPLATE_ARTICLE,"文章模板"),
	PICTURELIST	(6, GlobalConstant.TEMPLATE_PICTURE_LIST,"图集模板"),
	VIDEO	(4, GlobalConstant.TEMPLATE_VIDEO,	"视频模板"),
	LIST	(5, GlobalConstant.TEMPLATE_LIST,	"列表模板"),
	PICTUREGROUP (9, GlobalConstant.TEMPLATE_PICTURE_GROUP, "图集列表模板"),
	VIDEOLIST	(7, GlobalConstant.TEMPLATE_VIDEO_LIST, "视频列表模板"),
	ARTICLELIST	(8, GlobalConstant.TEMPLATE_ARTICLE_LIST, "文章列表模板"),
	PICTURE (3, GlobalConstant.TEMPLATE_PICTURE, "图片模板");
	
	
	private int id;
	private String name;
	private String value;
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	private TempTypeEnum(int id,String name,String value){
		this.id = id;
		this.name = name;
		this.value = value;
	}
	/**
	 * 获取所有模板类型map
	 */
	public static Map<String, TempTypeEnum> getAllTempTypeMap(){
		Map<String, TempTypeEnum> tempTypeEnumMap = new HashMap<String, TempTypeEnum>();
		TempTypeEnum[] tempTypeEnums = TempTypeEnum.values();
		for(TempTypeEnum tType : tempTypeEnums){
			tempTypeEnumMap.put(tType.getName(), tType);
		}
		return tempTypeEnumMap;
	}
	/**
	 * 是否为默认模板
	 * @param tempname 模板名(不包含扩展名)
	 * @return true 默认,false 非默认
	 * @throws Exception
	 */
	public static boolean isDefaultTemp(String tempname) throws Exception{
		boolean flag = false;
		Map<String, TempTypeEnum> tempTypeEnumMap = TempTypeEnum.getAllTempTypeMap();
		if(tempTypeEnumMap.containsKey(tempname)){
			flag = true;
		}
		return flag;
	}
}
