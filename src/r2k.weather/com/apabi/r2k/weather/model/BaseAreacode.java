package com.apabi.r2k.weather.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAreacode {
	
	//alias
	public static final String TABLE_ALIAS = "BaseAreacode";
	public static final String ALIAS_AREA_NAME = "areaName";
	public static final String ALIAS_AREA_CODE = "areaCode";
	public static final String ALIAS_PID = "pid";
	
	
	private int id;
	private java.lang.String areaName;
	private java.lang.String areaCode;
	private int pid;

	private BaseAreacode province;
	private BaseAreacode city;
	private BaseAreacode district;
	
	/** 顶级元素父id：0 */
	public static final int PID_TOP = 0;
	public static final int PID_BEIJING_CITY = 1;
	public static final int PID_BEIJING_DISTRICT = 35;
	
	/** 地区代码省分割点下标 */
	public static final int INDEX_PROVINCE = 5;
	/** 地区代码市分割点下标 */
	public static final int INDEX_CITY = 7;
	
	/** 直辖市与省分界点代码，注：直辖市代码小于该值 */
	public static final int CUT_POINT_CITY = 10105;
	
	/** 地区代码类型：省(province) */
	public static final String CODETYPE_PROVINCE = "province";
	/** 地区代码类型：市(city) */
	public static final String CODETYPE_CITY = "city";
	/** 地区代码类型：区县 (district)*/
	public static final String CODETYPE_DISTRICT = "district";
	
	
	/** 初始化BaseAreacode对象中的省市区县属性 */
	public static BaseAreacode initBaseAreacode(List<BaseAreacode> codelist){
		BaseAreacode codeSet = new BaseAreacode();
		for (int i = 0, len = codelist.size(); i < len; i++) {
			BaseAreacode code1 = codelist.get(i);
			if(code1.getPid() == PID_TOP){
				codeSet.setProvince(code1);
				break;
			}
		}
		BaseAreacode province = codeSet.getProvince();
		for (int i = 0, len = codelist.size(); i < len; i++) {
			BaseAreacode code2 = codelist.get(i);
			if(code2.getPid() == province.getId()){
				codeSet.setCity(code2);
				break;
			}
		}
		BaseAreacode city = codeSet.getCity();
		for (int i = 0, len = codelist.size(); i < len; i++) {
			BaseAreacode code3 = codelist.get(i);
			if(code3.getPid() == city.getId()){
				codeSet.setDistrict(code3);
				break;
			}
		}
		return codeSet;
	}
	/** 通过部分地区码拼装整体区域码 */
	public static String getAreaCode(String provinceCode, String cityCode, String districtCode){
		String areaCode = null;
		int iProvinceCode = Integer.parseInt(provinceCode);
		if(iProvinceCode < BaseAreacode.CUT_POINT_CITY){
			areaCode = provinceCode + districtCode + cityCode;
		} else {
			areaCode = provinceCode + cityCode + districtCode;
		}
		return areaCode;
	}
	
	/** 将省市县三元素的地区列表转换成地区map，key为province、city和district */
	public static Map<String, BaseAreacode> codeListToMap(List<BaseAreacode> codelist){
		Map<String, BaseAreacode> codemap = new HashMap<String, BaseAreacode>();
		for (int i = 0, len = codelist.size(); i < len; i++) {
			BaseAreacode code1 = codelist.get(i);
			if(code1.getPid() == PID_TOP){
				codemap.put(CODETYPE_PROVINCE, code1);
				break;
			}
		}
		BaseAreacode province = codemap.get(CODETYPE_PROVINCE);
		for (int i = 0, len = codelist.size(); i < len; i++) {
			BaseAreacode code2 = codelist.get(i);
			if(code2.getPid() == province.getId()){
				codemap.put(CODETYPE_CITY, code2);
				break;
			}
		}
		BaseAreacode city = codemap.get(CODETYPE_CITY);
		for (int i = 0, len = codelist.size(); i < len; i++) {
			BaseAreacode code3 = codelist.get(i);
			if(code3.getPid() == city.getId()){
				codemap.put(CODETYPE_DISTRICT, code3);
				break;
			}
		}
		return codemap;
	}
	
	public int getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public void setAreaName(java.lang.String value) {
		this.areaName = value;
	}
	public java.lang.String getAreaName() {
		return this.areaName;
	}
	public void setAreaCode(java.lang.String value) {
		this.areaCode = value;
	}
	public java.lang.String getAreaCode() {
		return this.areaCode;
	}
	public void setPid(java.lang.Integer value) {
		this.pid = value;
	}
	public int getPid() {
		return this.pid;
	}
	public BaseAreacode getProvince() {
		return province;
	}
	public void setProvince(BaseAreacode province) {
		this.province = province;
	}
	public BaseAreacode getCity() {
		return city;
	}
	public void setCity(BaseAreacode city) {
		this.city = city;
	}
	public BaseAreacode getDistrict() {
		return district;
	}
	public void setDistrict(BaseAreacode district) {
		this.district = district;
	}
}

