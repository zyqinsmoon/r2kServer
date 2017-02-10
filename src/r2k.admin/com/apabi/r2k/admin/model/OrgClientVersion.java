package com.apabi.r2k.admin.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class OrgClientVersion implements Comparable<OrgClientVersion>{
	private int id;
	private String orgId;
	private String devType;
	private String version;
	private Date lastDate;
	private Integer versionCode;
	
	//通过版本号设置versionCode
	public static int getVersionCodeByVerion(String version){
		String[] vers = version.split("\\.");
		int Major_Version_Number = Integer.parseInt(vers[0]) * 1000*1000;
		int Minor_Version_Number = Integer.parseInt(vers[1]) * 1000;
		int Revision_Number = Integer.parseInt(vers[2]);
		return Major_Version_Number + Minor_Version_Number + Revision_Number;
	}
	public int setVersionCodeByVerion(String version){
		this.versionCode = getVersionCodeByVerion(version);
		return this.versionCode;
	}
	
	/**
	 * 机构版本对比方法
	 * @return 1大于，0等于，-1小于，-2错误
	 */
	public int compareTo(OrgClientVersion o) {
		if(o != null && StringUtils.isNotBlank(o.getVersion())){
			if(o.getVersionCode() == null){
				o.setVersionCodeByVerion(o.getVersion());
			}
			if(this.versionCode == null){
				this.setVersionCodeByVerion(this.getVersion());
			}
			int versionCode = o.getVersionCode();
			if(this.versionCode > versionCode){
				return 1;
			} else if(this.versionCode == versionCode){
				return 0;
			} else if(this.versionCode < versionCode){
				return -1;
			}
		}
		return -2;
	}
	
	
	// getter and setter
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public String getDevType() {
		return devType;
	}
	public void setDevType(String devType) {
		this.devType = devType;
	}
	public Integer getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
}
