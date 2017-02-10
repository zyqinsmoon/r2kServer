package com.apabi.r2k.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.PrjEnumDao;
import com.apabi.r2k.admin.model.PrjEnum;
import com.apabi.r2k.admin.service.PrjEnumService;

@Service("prjEnumService")
public class PrjEnumServiceImpl implements PrjEnumService {

	private Logger log = LoggerFactory.getLogger(PrjEnumServiceImpl.class);
	
	public static final String PAGE_QUERY_STATEMENT = ".pageSelect";
	
	@Resource(name="prjEnumDao")
	private PrjEnumDao prjEnumDao;
	
	
	//入库
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean save(PrjEnum authEnum) throws Exception {
		boolean flag = false;
		int num = prjEnumDao.save(authEnum);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	
	//更新
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean update(PrjEnum authEnum) throws Exception {
		boolean flag = false;
		int num = prjEnumDao.update(authEnum);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	
	//根据ID删除
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteById(java.lang.Integer id) throws Exception {
		return prjEnumDao.deleteById(id);
	}
	
	//批量删除
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean batchDelete(List<java.lang.Integer> ids) throws Exception {
		return prjEnumDao.batchDeleteByIds(ids);
	}
	
	//根据ID查询
	public PrjEnum getById(java.lang.Integer id) throws Exception {
		return prjEnumDao.getById(id);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception {
		return prjEnumDao.pageQuery(PAGE_QUERY_STATEMENT, pageRequest, countQuery);
	} 
	
	//通过ENUM_TYPE获取全部AUTH_ENUM列表
	public List<PrjEnum> queryAllByType(String enumType) throws Exception{
		return this.prjEnumDao.queryAllByType(enumType);
	}
	
	public PrjEnum getByEnumCode(String enumCode) throws Exception{
		return prjEnumDao.getByEnumCode(enumCode);
	}

	public PrjEnumDao getprjEnumDao() {
		return prjEnumDao;
	}

	public void setprjEnumDao(PrjEnumDao prjEnumDao) {
		this.prjEnumDao = prjEnumDao;
	}

	@Override
	public List<PrjEnum> getAllEnum() throws Exception {
		return this.prjEnumDao.getAllEnum();
	}

	@Override
	public PrjEnum getPrjEnumFromList(List<PrjEnum> prjEnums, int enumId) {
		PrjEnum prjEnum = null;
		for(PrjEnum pe : prjEnums){
			if(pe.getEnumId() == enumId){
				prjEnum = pe;
			}
		}
		return prjEnum;
	}

	@Override
	public List<PrjEnum> getOrgPrjEnums(Map params) throws Exception {
		return prjEnumDao.getOrgPrjEnums(params);
	}
	
	public List<PrjEnum> getAuthresEnumsBydef(String deviceType) throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("devDef", deviceType);
		return prjEnumDao.getAuthresEnumsBydef(params);
	}
	
	public List<PrjEnum> getEnumByInEnumValues(String enumType, List<String> enumValues,String devDef) throws Exception{
		Map params = new HashMap();
		params.put("enumValues", enumValues);
		params.put("enumType", enumType);
		params.put("devDef", devDef);
		return prjEnumDao.getEnumByInEnumValues(params);
	}
	//获取机构权限列表
	public List<PrjEnum> getEnumByDevtype(String enumType, String devDef) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("enumType", enumType);
		params.put("devDef", devDef);
		return prjEnumDao.getEnumByDevtype(params);
	}

	@Override
	public Map<String, PrjEnum> getEnumMapByEnumValues(String enumType, List<String> enumValues) throws Exception {
		List<PrjEnum> prjEnums = prjEnumDao.getEnumByTypeAndValues(enumType, enumValues);
		Map<String, PrjEnum> prjEnumMap = new HashMap<String, PrjEnum>();
		for(PrjEnum prjEnum : prjEnums){
			prjEnumMap.put(prjEnum.getEnumValue(), prjEnum);
		}
		return prjEnumMap;
	}
}
