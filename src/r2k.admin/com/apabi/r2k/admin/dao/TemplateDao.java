package com.apabi.r2k.admin.dao;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.InfoTemplate;

public interface TemplateDao {
	/**
	 * 入库
	 */
	public int saveTemplate(InfoTemplate template) throws Exception;
	/**
	 * 批量保存
	 * @param templist
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplate> batchSaveTemplate(List<InfoTemplate> templist) throws Exception;
	
	/**
	 * 更新
	 */
	public int updateTemplate(InfoTemplate template) throws Exception;
	/**
	 * 批量更新
	 * @param templist
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplate> batchUpdateTemplate(List<InfoTemplate> templist) throws Exception;
	
	/**
	 * 根据ID删除
	 */
	public int deleteById(java.lang.Integer id) throws Exception;
	/**
	 * 根据模板套号删除模板
	 * @param setNo
	 * @return
	 * @throws Exception
	 */
	public int deleteBySetNo(String orgId, String setNo) throws Exception;
	/**
	 * 根据ID批量删除
	 */
	public int batchDeleteTemplate(List<java.lang.Integer> ids) throws Exception;
	
	/**
	 * 根据ID查询
	 */
	public InfoTemplate getTemplateById(java.lang.Integer id) throws Exception;
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception; 
	/**
	 * 根据模板套号返回模板列表
	 * @param orgId
	 * @param setNo
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplate> findTemplateListBySetNo(String orgId, String setNo) throws Exception;
	/**
	 * 根据设备类型查询机构下模板
	 * @param orgId
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplate> findOrgTemplateByDevType(String orgId, String deviceType) throws Exception;
	/**
	 * 根据设备类型查询机构和默认模板
	 * @param orgId
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplate> findOrgAllTemplateByDevType(String orgId, String deviceType) throws Exception;
	
	/**
	 * 获取发布所用首页模板
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public InfoTemplate findPublishHomeTemplate(Map parameters) throws Exception;
	
	/**
	 * 只根据模板套号查询模板信息
	 * @param setNo
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplate> findBySetNo(String setNo) throws Exception;
	
	/**
	 * 获取机构在用模板套信息
	 */
	public InfoTemplate findUsedTemplate(Map parameters) throws Exception;
	
	/**
	 * 获取栏目可用模板
	 * @param setNo
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplate> findColAllTemplates(String setNo, String type) throws Exception;
	
	public List<InfoTemplate> findDefaultTemplates(String orgId, String deviceType) throws Exception;
}
