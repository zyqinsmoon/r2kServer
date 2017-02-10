package com.apabi.r2k.weather.model;

public class WeatherInfo {
	private String city;	//城市名
	private String cityId;	//城市id
	private String tempMax;	//最高温度
	private String tempMin;	//最低温度
	private String weather;	//天气
	private String temp;	//当前温度
	private String wd;		//风向
	private String ws;		//风速
	private String sd;		//相对湿度
	private String wse;		//风力
	private String time;	//发布时间
	private String njd;		//
	private String qy;		//
//	private String isRadar;	//是否有雷达图
//	private String Radar;	//// 雷达图编号，雷达图的地址在 http://www.weather.com.cn/html/radar/雷达图编号.shtml
	
	public static String KEY_CITY = "city";
	public static String KEY_CITYID = "cityid";
	public static String KEY_TEMPMAX = "temp1";
	public static String KEY_TEMPMIN = "temp2";
	public static String KEY_WEATHER = "weather";
	public static String KEY_TEMP = "temp";
	public static String KEY_WD = "WD";
	public static String KEY_WS = "WS";
	public static String KEY_SD = "SD";
	public static String KEY_WSE = "WSE";
	public static String KEY_TIME = "time";
	public static String KEY_NJD = "njd";
	public static String KEY_QY = "qy";
	
	public static void main(String[] args) {
		WeatherInfo weather = new WeatherInfo();
		System.out.println("["+(weather)+"]");
		System.out.println("["+(weather == null)+"]");
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getTempMax() {
		return tempMax;
	}
	public void setTempMax(String tempMax) {
		this.tempMax = tempMax;
	}
	public String getTempMin() {
		return tempMin;
	}
	public void setTempMin(String tempMin) {
		this.tempMin = tempMin;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getWd() {
		return wd;
	}
	public void setWd(String wd) {
		this.wd = wd;
	}
	public String getWs() {
		return ws;
	}
	public void setWs(String ws) {
		this.ws = ws;
	}
	public String getSd() {
		return sd;
	}
	public void setSd(String sd) {
		this.sd = sd;
	}
	public String getWse() {
		return wse;
	}
	public void setWse(String wse) {
		this.wse = wse;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getNjd() {
		return njd;
	}
	public void setNjd(String njd) {
		this.njd = njd;
	}
	public String getQy() {
		return qy;
	}
	public void setQy(String qy) {
		this.qy = qy;
	}
}
