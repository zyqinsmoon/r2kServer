package com.apabi.r2k.admin.model;

import java.util.Date;

import com.apabi.r2k.common.utils.GlobalConstant;

public class Device {
	
	//alias
	public static final String TABLE_ALIAS = "Device";
	public static final String ALIAS_ID = "主键ID";
	public static final String ALIAS_DEVICE_ID = "设备ID";
	public static final String ALIAS_DEVICE_NAME = "设备名称";
	public static final String ALIAS_ORG_ID = "机构ID";
	public static final String ALIAS_GROUP_NAME = "设备组名称";
	public static final String ALIAS_LAST_DATE = "最后在线时间";
	public static final String ALIAS_IS_ONLINE = "是否在线";
	public static final String ALIAS_MAKER_ID = "模板id";
	public static final String ALIAS_MAC = "MAC";
	public static final String ALIAS_IPV4 = "IPV4";
	
	public static final String ALIAS_DEVICETYPE_ID = "设备类型ID";
	public static final String ALIAS_DEVICETYPE_NAME = "设备类型名称";
	public static final String ALIAS_DESC = "描述";
	
	private int id;					//id
	private String orgId;			//机构ID
	private String deviceId;		//设备id
	private String deviceName;		//设备名称
	private String groupName;		//设备组名称
	private Date lastDate;			//最后更新时间	
	private String isOnline;		//是否在线：0不在线 1 在线	
	private int makerId;			//模板id
	private String mac;				//mac
	private int heartbeat;			//心跳间隔
	private int deviceTypeId;		//设备类型ID
	private String deviceType;	//设备类型名称
	private String curVersion;
	private String toVersion;
	private String ipv4;
	private Date lastHeartTime;		//最后心跳时间
	
	public static final String STATUS_ONLINE = "在线";
	public static final String STATUS_OFFLINE = "离线";
	
	/**
	 * 检查设备是否在线
	 * 规则：验证时长为：二倍心跳间隔
	 * @param lastHeartTime
	 * @param heartbeat
	 * @return true 在线， false 离线
	 */
	public static boolean checkOnLine(Date lastHeartTime, int heartbeat){
		boolean flag = false;
		long interval = Long.valueOf(GlobalConstant.HEARTBEAT_TIMEOUT);
		long checkInterval = interval * 60 * 1000L; 
		long nowlong = new Date().getTime();
		long lastTime = lastHeartTime.getTime();
		if( (lastTime + checkInterval) > nowlong){
			flag = true;
		}
		return flag;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	public int getMakerId() {
		return makerId;
	}
	public void setMakerId(int makerId) {
		this.makerId = makerId;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public void setHeartbeat(int heartbeat) {
		this.heartbeat = heartbeat;
	}
	public int getHeartbeat() {
		return heartbeat;
	}
	public void setDeviceTypeId(int deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	public int getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setCurVersion(String curVersion) {
		this.curVersion = curVersion;
	}
	public String getCurVersion() {
		return curVersion;
	}
	public void setToVersion(String toVersion) {
		this.toVersion = toVersion;
	}
	public String getToVersion() {
		return toVersion;
	}

	public String getIpv4() {
		return ipv4;
	}
	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}
	public Date getLastHeartTime() {
		return lastHeartTime;
	}
	public void setLastHeartTime(Date lastHeartTime) {
		this.lastHeartTime = lastHeartTime;
	}
}
