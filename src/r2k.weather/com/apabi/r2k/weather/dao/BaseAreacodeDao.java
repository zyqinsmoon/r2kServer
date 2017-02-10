package com.apabi.r2k.weather.dao;

import com.apabi.r2k.weather.model.BaseAreacode;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

public interface BaseAreacodeDao {
	
	/**
	 * 入库
	 */
	public int save(BaseAreacode baseAreacode) throws Exception;
	
	/**
	 * 批量保存
	 */
	public List<BaseAreacode> batchSaveAreaCode(List<BaseAreacode> codelist) throws Exception;
	
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
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception; 
	/**
	 * 根据父id查询BaseAreacode列表
	 */
	public List<BaseAreacode> findCodeListByPid(int pid) throws Exception;
	/**
	 * 根据地区名查询BaseAreacode列表
	 */
	public List<BaseAreacode> findCodeListByAreaName(Map<String, String> param) throws Exception;
	/**
	 * 根据地区名查询BaseAreacode列表
	 */
	public List<BaseAreacode> findCodeListByAreaCode(Map<String, String> param) throws Exception;
}
