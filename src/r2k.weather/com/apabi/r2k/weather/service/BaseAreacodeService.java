package com.apabi.r2k.weather.service;

import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.weather.model.BaseAreacode;

public interface BaseAreacodeService {

	/**
	 * 入库
	 */
	public int save(BaseAreacode baseAreacode) throws Exception;
	
	/**
	 * 批量保存
	 */
	public void batchSaveAreaCode(List<BaseAreacode> codelist) throws Exception;
	
	/**
	 * 更新
	 */
	public int update(BaseAreacode baseAreacode) throws Exception;
	
	/**
	 * 根据ID删除
	 */
	public int deleteById(java.lang.Integer id) throws Exception;
	
	/**
	 * 根据ID查询
	 */
	public BaseAreacode getById(java.lang.Integer id) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception; 
	
	/**
	 * 根据父id查询BaseAreacode列表
	 */
	public List<BaseAreacode> findCodeListByPid(int pid) throws Exception;
	/**
	 * 根据地区名查询BaseAreacode列表
	 */
	public BaseAreacode findCodeSetByAreaName(String province, String city, String district) throws Exception;
	/**
	 * 根据地区名查询BaseAreacode列表
	 */
	public BaseAreacode findCodeSetByAreaCode(String areaCode) throws Exception;
	
	/*####################################获取天气相关方法#####################################*/
	/**
	 * 通过经纬度获取天气
	 * @param lng 纬度
	 * @param lat 经度
	 * @param orgId 机构id
	 * @return 天气json字符串
	 */
	public String getWeatherByGeocoding(String orgId, String lng, String lat) throws Exception;
	/**
	 * 通过地区名获取天气
	 * @param province 省
	 * @param city 市
	 * @param district 区县
	 * @return 天气json字符串
	 */
	public String getWeatherByAreaName(String province, String city, String district) throws Exception;
	/**
	 * 通过地区码获取天气信息
	 * @param areaCode 地区编码
	 * @return
	 */
	public String getWeatherByAreaCode(String areaCode) throws Exception;
	/**
	 * 通过经纬度获取地区名
	 * @param lng 纬度
	 * @param lat 经度
	 * @return 地区json字符串
	 */
	public String getAreaNameByGeocoding(String lng, String lat, String orgId) throws Exception;
	
}
