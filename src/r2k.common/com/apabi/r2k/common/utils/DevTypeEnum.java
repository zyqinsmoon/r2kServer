package com.apabi.r2k.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public enum DevTypeEnum {
	
	ORG(0,"ORG","机构"),
	AndroidLarge(1,GlobalConstant.USER_AGENT_ANDROID_LARGE,"触摸横屏"),
	AndroidPad(2,GlobalConstant.USER_AGENT_ANDROID_PAD,"安卓pad"),
	AndroidPhone(3,GlobalConstant.USER_AGENT_ANDROID_PHONE,"安卓手机"),
	iPad(4,GlobalConstant.USER_AGENT_IPAD,"iPad"),
	iPhone(5,GlobalConstant.USER_AGENT_IPHONE,"iPhone"),
	AndroidPortrait(6,GlobalConstant.USER_AGENT_ANDROID_PORTRAIT,"触摸竖屏"),
	Slave(-1,GlobalConstant.USER_AGENT_SLAVE,"缓存服务器"),
	WeiXin(-2,GlobalConstant.USER_AGENT_WEIXIN,"微信");
	
	//
	//iPad，
	//iPhone，
	//Android-Pad，
	//Android-Landscape(横)，
	//Android-Portrait(竖)，
	//Android-Phone

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
	
	private DevTypeEnum(int id,String name,String value){
		this.id = id;
		this.name = name;
		this.value = value;
	}
	public static DevTypeEnum findId(int id){
		DevTypeEnum[] relations = DevTypeEnum.values();
		for(DevTypeEnum relation : relations){
			if(relation.getId() == id){
				return relation;
			}
		}
		return null;
	}
	
	public static DevTypeEnum findName(String name){
		DevTypeEnum[] relations = DevTypeEnum.values();
		for(DevTypeEnum relation : relations){
			System.out.println(relation.getName());
			if(relation.getName().equals(name)){
				return relation;
			}
		}
		return null;
	}
	
	public static Map<Integer,String> toMap(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		DevTypeEnum[] relations = DevTypeEnum.values();
		for(DevTypeEnum relation : relations){
			map.put(relation.getId(), relation.getValue());
		}
		return map;
	}
	
	/**
	 * 获取所有客户端设备类型
	 */
	public static List<DevTypeEnum> getClientTypes(){
		List<DevTypeEnum> devTypeEnums = new ArrayList<DevTypeEnum>();
		DevTypeEnum[] arrDevTypeEnums = DevTypeEnum.values();
		for(DevTypeEnum dte : arrDevTypeEnums){
			if(dte.getId() > 0){
				devTypeEnums.add(dte);
			}
		}
		return devTypeEnums;
	}
	
	/**
	 * 获取所有客户端设备类型
	 */
	public static Map<String, DevTypeEnum> getClientTypeMap(){
		Map<String, DevTypeEnum> mDevTypeEnums = new HashMap<String, DevTypeEnum>();
		DevTypeEnum[] arrDevTypeEnums = DevTypeEnum.values();
		for(DevTypeEnum dte : arrDevTypeEnums){
			if(dte.getId() > 0){
				mDevTypeEnums.put(dte.getName(), dte);
			}
		}
		return mDevTypeEnums;
	}
	/**
	 * 获取所有客户端设备类型
	 */
	public static Map<String, DevTypeEnum> getAllTypeMap(){
		Map<String, DevTypeEnum> mDevTypeEnums = new HashMap<String, DevTypeEnum>();
		DevTypeEnum[] arrDevTypeEnums = DevTypeEnum.values();
		for(DevTypeEnum dte : arrDevTypeEnums){
			mDevTypeEnums.put(dte.getName(), dte);
		}
		return mDevTypeEnums;
	}
	
	
	/**
	 * 根据设备类型判断是否有用户管理
	 * deviceType
	 * iphone,ipad,android-phone
	 */
	public static boolean isHaveUser(String deviceType ){
		boolean ishavauser = false;
    	if(deviceType.equals(GlobalConstant.USER_AGENT_IPHONE)||deviceType.equals(GlobalConstant.USER_AGENT_ANDROID_PHONE)||deviceType.equals(GlobalConstant.USER_AGENT_IPAD)){
    		ishavauser= true;
    	}

    	return ishavauser;
	}
	
	/**
	 * 根据设备类型判断是否竖屏
	 * deviceType
	 * iphone,ipad,android-phone
	 */
	public static boolean isLarge(String deviceType ){
		boolean isLarge = false;
    	if(deviceType.equals(GlobalConstant.USER_AGENT_ANDROID_LARGE)||deviceType.equals(GlobalConstant.USER_AGENT_ANDROID_PAD)||deviceType.equals(GlobalConstant.USER_AGENT_IPAD)){
    		isLarge= true;
    	}

    	return isLarge;
	}
	/**
	 * 拼装版本需要的设备类型
	 * @return：iPad、iPhone、Android-Large#Android-Portrait
	 */
	public static Map<String, String> getVersionTypeMap(){
		Map<String, String> mDevTypeEnums = new LinkedHashMap<String, String>();
		mDevTypeEnums.put("Android-Large#Android-Portrait", "触摸屏");
		mDevTypeEnums.put(DevTypeEnum.iPad.getName(), DevTypeEnum.iPad.getValue());
		mDevTypeEnums.put(DevTypeEnum.iPhone.getName(), DevTypeEnum.iPhone.getValue());
		return mDevTypeEnums;
	}
}
