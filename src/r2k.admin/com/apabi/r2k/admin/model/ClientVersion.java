package com.apabi.r2k.admin.model;

import org.apache.commons.lang3.StringUtils;

public class ClientVersion implements Comparable<ClientVersion>{
	
	private java.lang.Integer id;
	private String version;
	private java.lang.String path;
	private java.lang.String description;
	private java.util.Date crtDate;
	private String devType;
	private String devName;
	private String qrcode;
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
	public int compareTo(ClientVersion o) {
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

	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setPath(java.lang.String value) {
		this.path = value;
	}
	public java.lang.String getPath() {
		return this.path;
	}
	public void setDescription(java.lang.String value) {
		this.description = value;
	}
	public java.lang.String getDescription() {
		return this.description;
	}
	public void setCrtDate(java.util.Date value) {
		this.crtDate = value;
	}
	public java.util.Date getCrtDate() {
		return this.crtDate;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVersion() {
		return version;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getDevName() {
		return devName;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getQrcode() {
		return qrcode;
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

