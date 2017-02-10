package com.apabi.r2k.weather.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpUtils;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.service.AuthOrgService;
import com.apabi.r2k.weather.dao.BaseAreacodeDao;
import com.apabi.r2k.weather.model.BaseAreacode;
import com.apabi.r2k.weather.model.WeatherInfo;
import com.apabi.r2k.weather.service.BaseAreacodeService;

@Service("baseAreacodeService")
@Scope("prototype")
public class BaseAreacodeServiceImpl implements BaseAreacodeService {

	private Logger log = LoggerFactory.getLogger(BaseAreacodeServiceImpl.class);
	
	public static final String PAGE_QUERY_STATEMENT = ".pageSelect";
	
	@Resource(name="baseAreacodeDao")
	private BaseAreacodeDao baseAreacodeDao;
	@Resource(name="authOrgService")
	private AuthOrgService authOrgService;
	
	public void setBaseAreacodeDao(BaseAreacodeDao baseAreacodeDao){
		this.baseAreacodeDao = baseAreacodeDao;
	}
	public BaseAreacodeDao getBaseAreacodeDao(){
		return baseAreacodeDao;
	}
	
	//入库
	@Transactional(propagation = Propagation.REQUIRED)
	public int save(BaseAreacode baseAreacode) throws Exception {
		return baseAreacodeDao.save(baseAreacode);
	}
	//批量保存
	@Transactional(propagation = Propagation.REQUIRED)
	public void batchSaveAreaCode(List<BaseAreacode> codelist) throws Exception {
		baseAreacodeDao.batchSaveAreaCode(codelist);
	}
	
	//更新
	@Transactional(propagation = Propagation.REQUIRED)
	public int update(BaseAreacode baseAreacode) throws Exception {
		return baseAreacodeDao.update(baseAreacode);
	}
	
	//根据ID删除
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteById(java.lang.Integer id) throws Exception {
		return baseAreacodeDao.deleteById(id);
	}
	
	//根据ID查询
	public BaseAreacode getById(java.lang.Integer id) throws Exception {
		return baseAreacodeDao.getById(id);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception {
		return baseAreacodeDao.pageQuery(PAGE_QUERY_STATEMENT, pageRequest, countQuery);
	}
	
	//根据父id查询BaseAreacode列表
	public List<BaseAreacode> findCodeListByPid(int pid) throws Exception{
		return this.baseAreacodeDao.findCodeListByPid(pid);
	}
	
	//根据地区名查询BaseAreacode列表
	public BaseAreacode findCodeSetByAreaName(String province, String city, String district) throws Exception{
		Map<String, String> param = new HashMap<String, String>();
		param.put("province", province);
		param.put("city", city);
		param.put("district", district);
		List<BaseAreacode> list = baseAreacodeDao.findCodeListByAreaName(param);
		BaseAreacode codeSet = BaseAreacode.initBaseAreacode(list);
		return codeSet;
	}
	
	//根据地区名查询BaseAreacode列表
	public BaseAreacode findCodeSetByAreaCode(String areaCode) throws Exception{
		BaseAreacode codeSet = null;
		String province = areaCode.substring(0, BaseAreacode.INDEX_PROVINCE);
		String city = areaCode.substring(BaseAreacode.INDEX_PROVINCE, BaseAreacode.INDEX_CITY);
		String district = areaCode.substring(BaseAreacode.INDEX_CITY);
		int provinceCode = Integer.parseInt(province);
		Map<String, String> param = new HashMap<String, String>();
		param.put("province", province);
		if(provinceCode < BaseAreacode.CUT_POINT_CITY){
			param.put("city", district);
			param.put("district", city);
		} else {
			param.put("city", city);
			param.put("district", district);
			
		}
		List<BaseAreacode> list = baseAreacodeDao.findCodeListByAreaCode(param);
		if(list != null && list.size() > 0){
			codeSet = BaseAreacode.initBaseAreacode(list);
		}
		return codeSet;
	}
	//############################################################################################
	//错误码errorCode：1获取天气失败且本地没有对应xml信息	2地区名区域码转区域对象失败 	
	
	// 通过经纬度获取天气
	public String getWeatherByGeocoding(String orgId, String lng, String lat) throws Exception{
		String info = "";
		String areaNameStr = getAreaNameByGeocoding(lng, lat, orgId);
		JSONObject jsonObject = JSONObject.fromObject(areaNameStr);
		String province = jsonObject.get(BaseAreacode.CODETYPE_PROVINCE).toString();
		String city = jsonObject.get(BaseAreacode.CODETYPE_CITY).toString();
		String district = jsonObject.get(BaseAreacode.CODETYPE_DISTRICT).toString();
		info = getWeatherByAreaName(province, city, district);
		return info;
	}
	
	//通过地区名获取天气
	public String getWeatherByAreaName(String provinceName, String cityName, String districtName) throws Exception{
		String info = "";
		BaseAreacode codeSet = findCodeSetByAreaName(provinceName, cityName, districtName);
		if(codeSet != null){
			BaseAreacode province = codeSet.getProvince();
			BaseAreacode city = codeSet.getCity();
			BaseAreacode district = codeSet.getDistrict();
			if(province != null && city != null && district != null){
				info = getWeatherInfo(province, city, district);
			} else {
				info = "{'status':'0','errorCode':'2'}";
			}
		}
		return info;
	}
	
	//通过地区名获取天气
	public String getWeatherByAreaCode(String areaCode) throws Exception{
		String info = "";
		BaseAreacode codeSet = findCodeSetByAreaCode(areaCode);
		if(codeSet != null){
			BaseAreacode province = codeSet.getProvince();
			BaseAreacode city = codeSet.getCity();
			BaseAreacode district = codeSet.getDistrict();
			if(province != null && city != null && district != null){
				info = getWeatherInfo(province, city, district);
			}
		}
		return info;
	}
	
	//通过BaseAreacode对象列表获取天气信息
	public String getWeatherInfo(BaseAreacode province, BaseAreacode city, BaseAreacode district) throws Exception{
//		System.out.println("线程名："+Thread.currentThread().getName()+"，开始时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		String info = "";
		Element root = null, record = null;
		String provinceCode = null, cityCode = null, districtCode = null;
		WeatherInfo weatherInfo = null;
		try {
			//1、检查本地xml是否有天气信息
			String path = PropertiesUtil.getRootPath() + "/"+PropertiesUtil.getValue("base.r2kfile")+ "/" 
					+ PropertiesUtil.getValue("path.weather") + "/weather.xml";
			SAXReader reader = new SAXReader();  
			File xmlFile = new File(path);
			Document document = reader.read(xmlFile);  
			root = document.getRootElement();
			provinceCode = province.getAreaCode();
			cityCode = city.getAreaCode();
			districtCode = district.getAreaCode();
			record = getWeatherInfoFromXML(root, provinceCode, cityCode, districtCode);
			String areaCode = BaseAreacode.getAreaCode(provinceCode, cityCode, districtCode);
			weatherInfo = null;
			//本地xml存在天气信息
			if(record != null){
				String time = record.attributeValue(WeatherInfo.KEY_TIME);
				//判断本地天气是否需要更新
				if(StringUtils.isNotBlank(time)){
					Date localDate = GlobalConstant.sdf.parse(time);
					long localVal = localDate.getTime();
					Date now = new Date();
					long nowVal = now.getTime();
					long day = 24*60*60*1000;
					//更新当天的整体天气信息
					if(nowVal > (localVal + day)){
						weatherInfo = getDayWeather(new WeatherInfo(), areaCode);
						createWeatherInfoXMLNode(root, province, city, district, weatherInfo);
						updateWeatherInfoXMLFile(xmlFile, document);
					}
					//更新实时天气信息
					String interval = PropertiesUtil.get("weather.interval");
					weatherInfo = getWeatherInfoFromXMLElement(record);
					if(nowVal > (localVal + 60*1000*Integer.parseInt(interval)) ){
						weatherInfo = getTraceWeather(weatherInfo, areaCode);
						createWeatherInfoXMLNode(root, province, city, district, weatherInfo);
						updateWeatherInfoXMLFile(xmlFile, document);
					}
				} else {
					weatherInfo = new WeatherInfo();
					weatherInfo = getTraceWeather(weatherInfo, areaCode);
					weatherInfo = getDayWeather(weatherInfo, areaCode);
					createWeatherInfoXMLNode(root, province, city, district, weatherInfo);
					updateWeatherInfoXMLFile(xmlFile, document);
				}
			}//本地xml不存在天气信息
			else {
				weatherInfo = new WeatherInfo();
				weatherInfo = getTraceWeather(weatherInfo, areaCode);
				weatherInfo = getDayWeather(weatherInfo, areaCode);
				createWeatherInfoXMLNode(root, province, city, district, weatherInfo);
				updateWeatherInfoXMLFile(xmlFile, document);
			}
			if(weatherInfo != null){
				JSONObject jsonObj = JSONObject.fromObject(weatherInfo);
				jsonObj.put("status", "1");
				info = jsonObj.toString();
			}
		} catch (Exception e) {
			if(record == null){
				record = getWeatherInfoFromXML(root, provinceCode, cityCode, districtCode);
			}
			if(record != null){
				weatherInfo = getWeatherInfoFromXMLElement(record);
				if(weatherInfo != null){
					JSONObject jsonObj = JSONObject.fromObject(weatherInfo);
					jsonObj.put("status", "1");
					info = jsonObj.toString();
				}
			} else {
				info = "{\"status\" :\"1\", \"errorCode\":\"1\"}"; 
			}
			log.error(e.getMessage(),e);
		}
		log.info("返回天气信息："+info);
//		System.out.println("线程名："+Thread.currentThread().getName()+"，结束时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		return info;
	}
	
	//通过天气码获取当前天气信息
	public WeatherInfo getTraceWeather(WeatherInfo weatherInfo, String areaCode) throws Exception{
		//获取实时天气
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String traceurl = PropertiesUtil.get("weather.url.trace") + "/" + areaCode + ".html";
		String traceJson = getWeatherFromWeb(traceurl);
		JSONObject traceObj = JSONObject.fromObject(traceJson);
		JSONObject infoObj = traceObj.getJSONObject("weatherinfo");
		if(StringUtils.isBlank(weatherInfo.getCity())){
			weatherInfo.setCity(infoObj.get(WeatherInfo.KEY_CITY).toString());
		}
		if(StringUtils.isBlank(weatherInfo.getCityId())){
			weatherInfo.setCityId(infoObj.get(WeatherInfo.KEY_CITYID).toString());
		}
		weatherInfo.setTemp(infoObj.get(WeatherInfo.KEY_TEMP).toString());
		weatherInfo.setWd(infoObj.get(WeatherInfo.KEY_WD).toString());
		weatherInfo.setWs(infoObj.get(WeatherInfo.KEY_WS).toString());
		weatherInfo.setSd(infoObj.get(WeatherInfo.KEY_SD).toString());
		weatherInfo.setWse(infoObj.get(WeatherInfo.KEY_WSE).toString());
		weatherInfo.setTime(date + " " + infoObj.get(WeatherInfo.KEY_TIME).toString() + ":00");
		weatherInfo.setNjd(infoObj.get(WeatherInfo.KEY_NJD).toString());
		weatherInfo.setQy(infoObj.get(WeatherInfo.KEY_QY).toString());
		
        log.info("请求实时天气信息的URL："+traceurl+"\n返回值：" + traceJson);
		return weatherInfo;
	}
	//通过天气码获取当天天气信息
	public WeatherInfo getDayWeather(WeatherInfo weatherInfo, String areaCode) throws Exception{
		//获取实时天气
		String traceurl = PropertiesUtil.get("weather.url.day") + "/" + areaCode + ".html";
		String dayJson = getWeatherFromWeb(traceurl);
		JSONObject dayObj = JSONObject.fromObject(dayJson);
		JSONObject infoObj = dayObj.getJSONObject("weatherinfo");
		if(StringUtils.isBlank(weatherInfo.getCity())){
			weatherInfo.setCity(infoObj.get(WeatherInfo.KEY_CITY).toString());
		}
		if(StringUtils.isBlank(weatherInfo.getCityId())){
			weatherInfo.setCityId(infoObj.get(WeatherInfo.KEY_CITYID).toString());
		}
		weatherInfo.setTempMax(infoObj.get(WeatherInfo.KEY_TEMPMAX).toString());
		weatherInfo.setTempMin(infoObj.get(WeatherInfo.KEY_TEMPMIN).toString());
		weatherInfo.setWeather(infoObj.get(WeatherInfo.KEY_WEATHER).toString());
		
		log.info("请求当天整体天气信息的URL："+traceurl+"\n返回值：" + dayJson);
		return weatherInfo;
	}
	
	
	//从气象局获取天气信息(json)
	public String getWeatherFromWeb(String url) throws Exception{
		HttpEntity entity = HttpUtils.httpGet(url);
		InputStream is = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));   
        StringBuilder strb = new StringBuilder();   
        String line = null;   
        while ((line = reader.readLine()) != null) {   
        	strb.append(line);   
        }  
		return strb.toString();
	}
	//################################辅助方法#####################################
	/**
	 * 通过经纬度获取地区名
	 * @param lng 纬度
	 * @param lat 经度
	 * @return 返回地区json字符串(status为1成功，0失败)：{'province':'','city':'','district':'','status':'0/1'}
	 */
	public String getAreaNameByGeocoding(String lng, String lat, String orgId) throws Exception{
		String json = "";
		//请求地址
		String url = PropertiesUtil.get("area.url") + "/?" +
				"ak=" + PropertiesUtil.get("area.ak")
				+"&location="+lat+","+lng
				+"&output="+PropertiesUtil.get("area.output"); 
		StringBuilder strb;
		String province = null, city = null, district = null;
		try {
			HttpEntity entity = HttpUtils.httpGet(url);
			InputStream is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));   
			strb = new StringBuilder();   
			String line = null;   
			while ((line = reader.readLine()) != null) {   
				strb.append(line);   
			}
			String result = strb.toString();
			JSONObject jsonObject=JSONObject.fromObject(result);
			JSONObject addressJson=(JSONObject)((JSONObject)jsonObject.get("result")).get("addressComponent");
			//省
			Object provinceObj = addressJson.get("province");
			if(provinceObj != null){
				province = provinceObj.toString();
			}
			//市
			Object cityObj = addressJson.get("city");
			if(cityObj != null){
				city = cityObj.toString();
				city = getCityName(city);
			}
			//县
			Object districtObj = addressJson.get("district");
			if(districtObj != null){
				district = districtObj.toString();
				district = getDistrictName(district);
			}
			if(provinceObj == null || cityObj == null || districtObj == null){
				BaseAreacode codeSet = getBaseAreacodeByOrgId(orgId);
				BaseAreacode codeProvince = codeSet.getProvince();
				BaseAreacode codeCity = codeSet.getProvince();
				BaseAreacode codeDistrict = codeSet.getProvince();
				province = codeProvince.getAreaName();
				city = codeCity.getAreaName();
				district = codeDistrict.getAreaName();
				json = "{'province':'"+province+"','city':'"+city+"','district':'"+district+"'}";
			} else {
				json = "{'province':'"+province+"','city':'"+city+"','district':'"+district+"'}";
			}
		} catch (Exception e) {
			//通过orgId获取天气区域码
			BaseAreacode codeSet = getBaseAreacodeByOrgId(orgId);
			BaseAreacode codeProvince = codeSet.getProvince();
			BaseAreacode codeCity = codeSet.getProvince();
			BaseAreacode codeDistrict = codeSet.getProvince();
			province = codeProvince.getAreaName();
			city = codeCity.getAreaName();
			district = codeDistrict.getAreaName();
			json = "{'province':'"+province+"','city':'"+city+"','district':'"+district+"'}";
			log.error(e.getMessage(), e);
		}
        log.info("请求路径为：" + url + "\n返回值：" + json);
		return json;
	}
	//获取城市名
	private String getCityName(String cityName){
		//巴州：巴音郭楞蒙古自治州/克州：克孜勒苏柯尔克孜自治州/博州：博尔塔拉蒙古自治州
		String[] citySuffix = new String[]{"市", "地区", "盟", 
			"朝鲜族治州自治州"," 土家族苗族自治州","藏族羌族自治州","布依族苗族自治州",
			"苗族侗族自治州","傣族景颇族自治州","哈尼族彝族自治州","壮族苗族自治州",
			"蒙古族藏族自治州",
			"回族自治州", "哈萨克自治州", "蒙古自治州","藏族自治州"," 彝族自治州",
			"傣族自治州","白族自治州","僳僳族自治州"
		};
		
		if(cityName.equals("巴音郭楞蒙古自治州")){
			cityName = "巴州";
		} else if(cityName.equals("克孜勒苏柯尔克孜自治州")){
			cityName = "克州";
		} else if(cityName.equals("博尔塔拉蒙古自治州")){
			cityName = "博州";
		}
		for (String cs : citySuffix) {
			if(cityName.endsWith(cs)){
				cityName = cityName.replace(cs, "");
				break;
			}
		}
		return cityName;
	}
	//获取区县名
	private String getDistrictName(String districtName){
		//喀左：喀喇沁左翼蒙古族自治县/塔什库尔干塔吉克自治县/察布查尔锡伯自治县/ 
		String[] districtSuffix = new String[]{"市", "区", "县", "林区",
			"彝族回族苗族自治县","哈尼族彝族傣族自治县","拉祜族佤族布朗族傣族自治县",
			"傣族拉祜族佤族自治县","彝族哈尼族拉祜族自治县","苗族瑶族傣族自治县",
			"保安族东乡族撒拉族自治县",
			
			"满族蒙古族自治县","苗族侗族自治县","壮族瑶族自治县","黎族苗族自治县",
			"土家族苗族自治县","布依族苗族自治县","苗族布依族自治县","仡佬族苗族自治县",
			"彝族苗族自治县","回族彝族自治县","彝族傣族自治县","傣族佤族自治县",
			"哈尼族彝族自治县","傣族彝族自治县","彝族回族自治县","白族普米族自治县",
			"独龙族怒族自治县","傈僳族自治县","回族土族自治县",
			
			"满族自治县","回族自治县","蒙古族自治县","朝鲜族自治县","畲族自治县",
			"土家族自治县","侗族自治县","瑶族自治县","毛南族自治县","仫佬族自治县",
			"各族自治县","黎族自治县","羌族自治县","藏族自治县","水族自治县",
			"佤族自治县","纳西族自治县","拉祜族自治县","哈尼族自治县","彝族自治县",
			"苗族自治县","哈萨克族自治县","裕固族自治县","东乡族自治县","土族自治县",
			"撒拉族自治县","塔吉克自治县","锡伯自治县"
		};
		if(districtName.equals("喀喇沁左翼蒙古族自治县")){
			districtName = "喀左";
		}
		for (String cs : districtSuffix) {
			if(districtName.endsWith(cs)){
				districtName = districtName.replace(cs, "");
				break;
			}
		}
		return districtName;
	}
	//从天气xml中读取天气信息
	private Element getWeatherInfoFromXML(Element root, String provinceCode, String cityCode, String districtCode) throws Exception{
		Element record = null;
		List<Node> provinceNodes = root.selectNodes("//Province[@code='"+provinceCode+"']");
		if(provinceNodes != null && provinceNodes.size() > 0){
			Node provinceNode = provinceNodes.get(0);
			List<Node> cityNodes = provinceNode.selectNodes("//City[@code='"+cityCode+"']");
			if(cityNodes != null && cityNodes.size() > 0){
				Node cityNode = cityNodes.get(0);
				String areaCode = BaseAreacode.getAreaCode(provinceCode, cityCode, districtCode);
				List<Element> districtNodes = cityNode.selectNodes("//WeatherInfo[@cityid='"+areaCode+"']");
				if(districtNodes != null && districtNodes.size() > 0){
					record = districtNodes.get(0);
				}
			}
		}
		return record;
	}
	//从天气xml节点中读取天气信息
	private WeatherInfo getWeatherInfoFromXMLElement(Element record) throws Exception{
		WeatherInfo weatherInfo = new WeatherInfo();
		String city = record.attributeValue(WeatherInfo.KEY_CITY);
		if(city != null){
			weatherInfo.setCity(city);
		}
		String cityId = record.attributeValue(WeatherInfo.KEY_CITYID);
		if(cityId != null){
			weatherInfo.setCityId(cityId);
		}
		String tempMax = record.attributeValue(WeatherInfo.KEY_TEMPMAX);
		if(tempMax != null){
			weatherInfo.setTempMax(tempMax);
		}
		String tempMin = record.attributeValue(WeatherInfo.KEY_TEMPMIN);
		if(tempMin != null){
			weatherInfo.setTempMin(tempMin);
		}
		String weather = record.attributeValue(WeatherInfo.KEY_WEATHER);
		if(weather != null){
			weatherInfo.setWeather(weather);
		}
		String temp = record.attributeValue(WeatherInfo.KEY_TEMP);
		if(temp != null){
			weatherInfo.setTemp(temp);
		}
		String wd = record.attributeValue(WeatherInfo.KEY_WD);
		if(wd != null){
			weatherInfo.setWd(wd);
		}
		String ws = record.attributeValue(WeatherInfo.KEY_WS);
		if(ws != null){
			weatherInfo.setWs(ws);
		}
		String sd = record.attributeValue(WeatherInfo.KEY_SD);
		if(sd != null){
			weatherInfo.setSd(sd);
		}
		String wse = record.attributeValue(WeatherInfo.KEY_WSE);
		if(wse != null){
			weatherInfo.setWse(wse);
		}
		String time = record.attributeValue(WeatherInfo.KEY_TIME);
		if(time != null){
			weatherInfo.setTime(time);
		}
		String njd = record.attributeValue(WeatherInfo.KEY_NJD);
		if(njd != null){
			weatherInfo.setNjd(njd);
		}
		String qy = record.attributeValue(WeatherInfo.KEY_QY);
		if(qy != null){
			weatherInfo.setQy(qy);
		}
		return weatherInfo;
	}
	
	//创建本地xml中地区天气节点
	public synchronized void updateWeatherInfoXMLFile(File xmlFile, Document document) throws Exception{
		 /** 将document中的内容写入文件中 */ 
		OutputFormat format = OutputFormat.createPrettyPrint(); 
        format.setEncoding( "UTF-8"); 
        XMLWriter writer = new XMLWriter(new FileOutputStream(xmlFile),format); 
        writer.write(document); 
        writer.close(); 
//        Thread.currentThread().sleep(10000);
	}
	public void createWeatherInfoXMLNode(Element root, BaseAreacode province, BaseAreacode city, BaseAreacode district, WeatherInfo weatherInfo) throws Exception{
		String provinceCode = province.getAreaCode();
		List<Element> provinceNodes = root.selectNodes("//Province[@code='"+provinceCode+"']");
		Element provinceNode = null;
		if(provinceNodes != null && provinceNodes.size() > 0){
			provinceNode = provinceNodes.get(0);
		} else {
			provinceNode = root.addElement("Province");
			provinceNode.addAttribute("code", province.getAreaCode()).addAttribute("name", province.getAreaName());
		}
		String cityCode = city.getAreaCode();
		List<Element> cityNodes = provinceNode.selectNodes("//Province[@code='"+provinceCode+"']//City[@code='"+cityCode+"']");
		Element cityNode = null;
		if(cityNodes != null && cityNodes.size() > 0){
			cityNode = cityNodes.get(0);
		} else {
			cityNode = provinceNode.addElement("City");
			cityNode.addAttribute("code", city.getAreaCode()).addAttribute("name", city.getAreaName());
		}
		String districtCode = district.getAreaCode();
		String areaCode = BaseAreacode.getAreaCode(provinceCode, cityCode, districtCode);
		List<Element> districtNodes = cityNode.selectNodes("//Province[@code='"+provinceCode+"']//City[@code='"+cityCode+"']//WeatherInfo[@cityid='"+areaCode+"']");
		Element newElement = null;
		if(districtNodes != null && districtNodes.size() > 0){
			newElement = districtNodes.get(0);
		} else {
			newElement = cityNode.addElement("WeatherInfo");
		}
		String cityName = weatherInfo.getCity();
		if(cityName != null){
			newElement.addAttribute(WeatherInfo.KEY_CITY, cityName);
		}
		String cityId = weatherInfo.getCityId();
		if(cityId != null){
			newElement.addAttribute(WeatherInfo.KEY_CITYID, cityId);
		}
		String tempMax = weatherInfo.getTempMax();
		if(tempMax != null){
			newElement.addAttribute(WeatherInfo.KEY_TEMPMAX, tempMax);
		}
		String tempMin = weatherInfo.getTempMin();
		if(tempMin != null){
			newElement.addAttribute(WeatherInfo.KEY_TEMPMIN, tempMin);
		}
		String weather = weatherInfo.getWeather();
		if(weather != null){
			newElement.addAttribute(WeatherInfo.KEY_WEATHER, weather);
		}
		String temp = weatherInfo.getTemp();
		if(temp != null){
			newElement.addAttribute(WeatherInfo.KEY_TEMP, temp);
		}
		String wd = weatherInfo.getWd();
		if(wd != null){
			newElement.addAttribute(WeatherInfo.KEY_WD, wd);
		}
		String ws = weatherInfo.getWs();
		if(ws != null){
			newElement.addAttribute(WeatherInfo.KEY_WS, ws);
		}
		String sd = weatherInfo.getSd();
		if(sd != null){
			newElement.addAttribute(WeatherInfo.KEY_SD, sd);
		}
		String wse = weatherInfo.getWse();
		if(wse != null){
			newElement.addAttribute(WeatherInfo.KEY_WSE, wse);
		}
		String time = weatherInfo.getTime();
		if(time != null){
			newElement.addAttribute(WeatherInfo.KEY_TIME, time);
		}
		String njd = weatherInfo.getNjd();
		if(njd != null){
			newElement.addAttribute(WeatherInfo.KEY_NJD, njd);
		}
		String qy = weatherInfo.getQy();
		if(qy != null){
			newElement.addAttribute(WeatherInfo.KEY_QY, qy);
		}
	}
	//通过orgId获取机构地区对象集合
	public BaseAreacode getBaseAreacodeByOrgId(String orgId) throws Exception{
		AuthOrg org = authOrgService.getOrgObject(orgId);
		Map<String, String> param = new HashMap<String, String>();
		param.put("areaCode", org.getAreaCode());
		List<BaseAreacode> list = baseAreacodeDao.findCodeListByAreaCode(param);
		return BaseAreacode.initBaseAreacode(list);
	}
}
