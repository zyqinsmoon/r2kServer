package com.apabi.r2k.weather.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.weather.dao.BaseAreacodeDao;
import com.apabi.r2k.weather.model.BaseAreacode;

@Repository("baseAreacodeDao")
public class BaseAreacodeDaoImpl extends BaseDaoImpl<BaseAreacode, Serializable> implements BaseAreacodeDao {

	//入库
	public int save(BaseAreacode baseAreacode) throws Exception {
		return baseDao.insert(getStatement("insert"), baseAreacode);
	}
	
	//批量保存
	public List<BaseAreacode> batchSaveAreaCode(List<BaseAreacode> codelist) throws Exception {
		return batchSave(getStatement("insert"), codelist);
	}
	
	//更新
	public int update(BaseAreacode baseAreacode) throws Exception {
		return baseDao.update(getStatement("update"), baseAreacode);
	}
	
	//根据ID删除
	public int deleteById(java.lang.Integer id) throws Exception {
		return baseDao.delete(getStatement("delete"),id);
	}
	
	//根据ID查询
	public BaseAreacode getById(java.lang.Integer id) throws Exception {
		return baseDao.selectOne(getStatement("getById"), id);
	}
	
	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return basePageQuery(statementName, pageRequest, countQuery);
	} 
	//根据父id查询BaseAreacode列表
	public List<BaseAreacode> findCodeListByPid(int pid) throws Exception{
		return baseDao.selectList(getStatement("findCodeListByPid"), pid);
	}
	//根据地区名查询BaseAreacode列表
	public List<BaseAreacode> findCodeListByAreaName(Map<String, String> param) throws Exception{
		return baseDao.selectList(getStatement("findCodeListByAreaName"), param);
	}
	//根据地区名查询BaseAreacode列表
	public List<BaseAreacode> findCodeListByAreaCode(Map<String, String> param) throws Exception{
		return baseDao.selectList(getStatement("findCodeListByAreaCode"), param);
	}
}
