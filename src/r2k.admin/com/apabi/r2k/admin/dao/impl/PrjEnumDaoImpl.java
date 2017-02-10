package com.apabi.r2k.admin.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.PrjEnumDao;
import com.apabi.r2k.admin.model.PrjEnum;
import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("prjEnumDao")
public class PrjEnumDaoImpl extends BaseDaoImpl<PrjEnum, Serializable> implements PrjEnumDao {

	private Logger log = LoggerFactory.getLogger(PrjEnumDaoImpl.class);
	
	//入库
	public int save(PrjEnum authEnum) throws Exception {
		return baseDao.insert(getStatement("insert"), authEnum);
	}
	
	//更新
	public int update(PrjEnum authEnum) throws Exception {
		return baseDao.update(getStatement("update"), authEnum);
	}
	
	//根据ID删除
	public boolean deleteById(Integer id) throws Exception {
		boolean flag = false;
		int num = baseDao.delete(getStatement("delete"), id);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	
	//根据ID批量删除
	public boolean batchDeleteByIds(List<java.lang.Integer> ids) throws Exception {
		boolean flag = false;
		int num = baseDao.delete(getStatement("batchDelete"), ids);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	
	//根据ID查询
	public PrjEnum getById(Integer id) throws Exception {
		return baseDao.selectOne(getStatement("getById"), id);
	}
	
	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return basePageQuery(statementName, pageRequest, countQuery);
	}

	//通过ENUM_TYPE获取全部AUTH_ENUM列表
	public List<PrjEnum> queryAllByType(String enumType) throws Exception {
		Map param = new HashMap<String, Object>();
		param.put("enumType", enumType);
		return baseDao.selectList(getStatement("queryAllByType"), param);
	} 
	
	//通过enumCode获取对象
	public PrjEnum getByEnumCode(String enumCode) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("enumCode", enumCode);
		return baseDao.selectOne(getStatement("getByEnumCode"),map);
	}

	@Override
	public List<PrjEnum> getAllEnum() throws Exception {
		return baseDao.selectList(getStatement("getAll"));
	}

	@Override
	public List<PrjEnum> getOrgPrjEnums(Map params) throws Exception {
		return baseDao.selectList(getStatement("getOrgPrjEnums"), params);
	}
	
	public List<PrjEnum> getAuthresEnumsBydef(Map params) throws Exception {
		return baseDao.selectList(getStatement("getAuthresEnumsBydef"), params);
	}
	public List<PrjEnum> getEnumByInEnumValues(Map params) throws Exception {
		return baseDao.selectList(getStatement("getEnumByInEnumValues"), params);
	}
	//获取机构权限列表
	public List<PrjEnum> getEnumByDevtype(Map params) throws Exception {
		return baseDao.selectList(getStatement("getEnumByDevtype"), params);
	}

	@Override
	public List<PrjEnum> getEnumByTypeAndValues(String enumType, List<String> enumValues) throws Exception {
		Map params = new HashMap();
		params.put("enumType", enumType);
		params.put("enumValues", enumValues);
		return baseDao.selectList(getStatement("getEnumsByTypeAndValues"), params);
	}
}
