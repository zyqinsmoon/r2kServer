package com.apabi.r2k.menu.model;

import org.apache.commons.lang3.StringUtils;

import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.PropertiesUtil;

public class Menu {
	private int id;
	private String title;
	private String link;	//链接地址
	private String background;	//背景图片地址
	private String normal;	//按钮图片地址
	private String iconBackground;	//选中时图片
	private String logo;	//置灰时图片
	private String orgId;
	private String deviceId;
	private int status;	//状态
	private String deviceType;	//类型 
	private String menuType;	//菜单类型
	private String description;		// 菜单描述
	private int hide;			//是否隐藏
	private String homePage;		//是否为主页
	private int sort;			//菜单顺序
	
	public static final int SATAUS_UNPUBLISH = 0;
	public static final int STATUS_PUBLISH = 1;
	
	public static final int TYPE_ORG = 1;
	public static final int TYPE_DEVICE = 2;
	public static final int TYPE_IPAD = 3;
	
	public static final int MENU_LINK = 1;//浏览器打开
	public static final int MENU_INTENT = 2;//apk内部程序打开
	
	public static final String RELATIVE_ROOT = PropertiesUtil.get("r2k.menu");
	
	public static final int MAX_DESC_WORDS = 1000;	//描述最多接受字数
	
	public static final String MENU_ALL = "0";		//全部内置菜单标识
	public static final String MENU_CALL = "1";		//已引用内置菜单标识
	
	public static final String MENU_TYPE_CUSTOM = "link";		//自定义菜单
	
	public static final int IS_NOT_HIDE = 1;	//显示菜单
	public static final int IS_HIDE = 0;		//菜单隐藏
	
	public static final String IS_NOT_HOME_PAGE = "0"; //非主页
	public static final String IS_HOME_PAGE = "1";	//主页
	
	public static final String XML_IMG_ICONBACKGROUND = "Iconbackground";	//菜单图标背景图片名称
	public static final String XML_IMG_BACKGROUND = "Background";	//背景图片名称
	public static final String XML_IMG_NORMAL = "Normal";			//normal图片名称
	public static final String XML_IMG_LOGO = "Logo";				//logo图名称
	public static final String XML_IMG_BUTTON_LOGO = "buttonlogo";	//菜单logo图名称
	public static final String XML_IMG_CENTER_LOGO = "Centerlogo";	//导航转盘logo图名称
	public static final String XML_TITLE = "Title";	//
	public static final String XML_LINK = "Link";	//
	public static final String XML_DESCRITTION = "Description";	//
	public static final String XML_ICONS = "Icons";	//
	public static final String XML_MENU = "Menu";	//
	public static final String XML_MENUS = "Menus";	//
	
	public Menu(){}
	
	public Menu(String orgId, String deviceId, String type, int callId) {
		super();
		this.orgId = orgId;
		this.deviceId = deviceId;
		this.deviceType = type;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getBackground() {
		return background;
	}
	
	public String getBackgroundPath(){
		return "/" + GlobalConstant.PROJECT_FILE_PATH + "/" + PropertiesUtil.get("path.menu.res") + "/" + background;
	}
	
	public void setBackground(String background) {
		this.background = background;
	}
	
	public String getNormal() {
		return normal;
	}
	
	public String getNormalPath(){
		return "/" + GlobalConstant.PROJECT_FILE_PATH + "/" + PropertiesUtil.get("path.menu.res") + "/" + normal;
	}
	
	public void setNormal(String normal) {
		this.normal = normal;
	}
	
	public String getIconBackground() {
		return iconBackground;
	}
	
	public String getIconBackgroundPath(){
		return "/" + GlobalConstant.PROJECT_FILE_PATH + "/" + PropertiesUtil.get("path.menu.res") + "/" + iconBackground;
	}
	
	public void setIconBackground(String iconBackground) {
		this.iconBackground = iconBackground;
	}
	
	public String getLogo() {
		return logo;
	}
	
	public String getLogoPath(){
		return "/" + GlobalConstant.PROJECT_FILE_PATH + "/" + PropertiesUtil.get("path.menu.res") + "/" + logo;
	}
	
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public int getHide() {
		return hide;
	}

	public void setHide(int hide) {
		this.hide = hide;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getHomePageFormatBoolean(){
	String isHomePage = "false";
		if(this.homePage.equals(IS_HOME_PAGE)){
			isHomePage = "true";
	
		}
		return isHomePage;
	}
	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public boolean equals(Object obj) {
		Menu menu = (Menu)obj;
		if(menu.getMenuType().equals(this.menuType) && menu.getDeviceType() == this.deviceType && menu.getOrgId().equals(this.orgId)){
			String deviceid = menu.getDeviceId();
			if(StringUtils.isNotBlank(deviceid)){
				if(deviceid.equals(this.deviceId)){
					return true;
				}
			}else{
				if(StringUtils.isBlank(this.deviceId)){
					return true;
				}
			}
		}
		return false;
	}
}
