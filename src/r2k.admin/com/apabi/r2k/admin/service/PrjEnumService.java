package com.apabi.r2k.admin.service;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.PrjEnum;

public interface PrjEnumService {

	/**
	 * 入库
	 */
	public boolean save(PrjEnum authEnum) throws Exception;
	
	/**
	 * 更新
	 */
	public boolean update(PrjEnum authEnum) throws Exception;
	
	/**
	 * 根据ID删除
	 */
	public boolean deleteById(java.lang.Integer id) throws Exception;
	
	/**
	 * 批量删除
	 */
	public boolean batchDelete(List<java.lang.Integer> ids) throws Exception;
	
	/**
	 * 根据ID查询
	 */
	public PrjEnum getById(java.lang.Integer id) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception; 
	
	/**
	 * 通过ENUM_TYPE获取全部AUTH_ENUM列表
	 * @return
	 */
	public List<PrjEnum> queryAllByType(String enumType) throws Exception;
	//通过enumCode获取对象
	public PrjEnum getByEnumCode(String enumCode) throws Exception;
	//获取所有prjenum
	public List<PrjEnum> getAllEnum() throws Exception;
	//根据enumcode从prjEnum列表中找到对应的prjEnum
	public PrjEnum getPrjEnumFromList(List<PrjEnum> prjEnums, int enumId);
	//获取机构已引用或未引用的内置菜单
	public List<PrjEnum> getOrgPrjEnums(Map params) throws Exception;
	public List<PrjEnum> getAuthresEnumsBydef(String deviceType) throws Exception;
	public List<PrjEnum> getEnumByInEnumValues(String enumType, List<String> enumValues,String devDef) throws Exception;
	//获取机构权限列表
	public List<PrjEnum> getEnumByDevtype(String enumType, String devDef) throws Exception;
	//根据enumType和enumValue获取所有的prjenum，并转成map
	public Map<String, PrjEnum> getEnumMapByEnumValues(String enumType, List<String> enumValues) throws Exception;
}
