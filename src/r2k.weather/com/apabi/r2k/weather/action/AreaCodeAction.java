package com.apabi.r2k.weather.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.apabi.r2k.weather.service.BaseAreacodeService;

@Controller("areaCodeAction")
@Scope("prototype")
public class AreaCodeAction {
	@Resource(name="baseAreacodeService")
	private BaseAreacodeService baseAreacodeService;
	
	String code;
	public String createData(){
		try {
			//test1 = province = 北京市, city = 北京市, district = 海淀区{'province':'北京市','city':'北京','district':'海淀'}
//			String province = "北京市"; 
//			String city = "北京";
//			String district = "海淀";
//			String place = "{'province':'北京市','city':'北京','district':'海淀'}";
//			String lng = "116.322987";
//			String lat = "39.983424";
//			String code = "101010200";
			
//			//test2 = province = 黑龙江省, city = 哈尔滨市, district = 南岗区
//			String province = "黑龙江省"; 
//			String city = "哈尔滨市";
//			String district = "五常市";
//			String lng = "126.657717";
//			String lat = "45.773225";
//			String code = "";
			
			//test3 = {'province':'黑龙江省','city':'哈尔滨市','district':'五常市'}
//			String province = "黑龙江省"; 
//			String city = "哈尔滨";
//			String district = "五常";
			String lng = "127.174119";
			String lat = "44.937836";
//			String code = "101050112";
			
			//test4 = {'province':'新疆维吾尔自治区','city':'吐鲁番地','district':'托克逊'}	昌吉87.305237,44.032573
//			String place = "{'province':'新疆维吾尔自治区','city':'吐鲁番地','district':'托克逊'}";
//			String province = "新疆维吾尔自治区"; 
//			String city = "吐鲁番地区";
//			String district = "托克逊县";
//			String lng = "87.6817700000";
//			String lat = "43.2707910000";
//			String code = "101130502";
			
			
			String orgId = "tiyan";
//			baseAreacodeService.getWeatherByGeocoding(orgId, lng, lat);
			baseAreacodeService.getWeatherByAreaCode(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
