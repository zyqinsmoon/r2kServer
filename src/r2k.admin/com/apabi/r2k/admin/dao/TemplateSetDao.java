package com.apabi.r2k.admin.dao;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.InfoTemplateSet;

public interface TemplateSetDao {
	/**
	 * 入库
	 */
	public int saveTemplateSet(InfoTemplateSet templateSet) throws Exception;
	/**
	 * 批量保存
	 */
	public List<InfoTemplateSet> batchSaveTemplateSet(List<InfoTemplateSet> tempsetlist) throws Exception;
	/**
	 * 更新
	 */
	public int updateTemplateSet(InfoTemplateSet templateSet) throws Exception;
	/**
	 * 批量保存
	 */
	public List<InfoTemplateSet> batchUpdateTemplateSet(List<InfoTemplateSet> tempsetlist) throws Exception;
	/**
	 * 根据ID删除
	 */
	public int deleteById(java.lang.Integer id) throws Exception;
	/**
	 * 根据ID查询
	 */
	public InfoTemplateSet getTemplateSetById(java.lang.Integer id) throws Exception;
	/**
	 * 根据套号查询
	 */
	public InfoTemplateSet getTemplateSetBySetNo(String orgId, String setNo) throws Exception;
	/**
	 * 查找默认设备类型模板列表
	 * @param scope
	 * @param defaultType
	 * @param orgId
	 * @return true:不存在默认模板,false:存在默认模板
	 * @throws Exception
	 */
	public List<InfoTemplateSet> checkDefaultType(Map<String, Object> params) throws Exception;
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception; 
	/**
	 * 按参数查找模板套信息
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplateSet> queryAll(Map<String, String> parameter) throws Exception;
	/**
	 * 按设备类型查找模板套信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplateSet> getTemplateSetByDevType(Map<String, String> params) throws Exception;
}
