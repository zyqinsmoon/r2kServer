package com.apabi.r2k.admin.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.InfoTemplate;

public interface TemplateService {

	/**
	 * 入库
	 */
	public int save(InfoTemplate template) throws Exception;
	/**
	 * 批量保存
	 */
	public boolean batchSave(List<InfoTemplate> templist) throws Exception;
	
	/**
	 * 更新
	 */
	public int update(InfoTemplate template) throws Exception;
	/**
	 * 批量更新
	 */
	public boolean batchUpdate(List<InfoTemplate> templist) throws Exception;
	
	/**
	 * 根据ID删除
	 */
	public int deleteById(java.lang.Integer id) throws Exception;
	/**
	 * 按模板套号删除模板
	 * @param setNo
	 * @return
	 */
	public int deleteBySetNo(String orgId, String setNo) throws Exception;
	
	/**
	 * 批量删除
	 */
	public int batchDelete(List<java.lang.Integer> ids) throws Exception;
	
	/**
	 * 根据模板ID查询
	 */
	public InfoTemplate getById(java.lang.Integer id) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception; 

	/**
	 * 根据模板套号返回模板列表
	 * @param orgId
	 * @param setNo
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplate> findTemplateListBySetNo(String orgId, String setNo) throws Exception;

	/**
	 * 获取zip中的模板名称和xml中的信息
	 * @param zip 上传文件
	 * @param filename 上传文件原文件名
	 * @param setNo 
	 * @return
	 */
	public Map<String, String> parseZipData(File zip, String filename, String setNo) throws Exception;
	
	/**
	 * 获取机构发布首页模板
	 * @param orgid
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public InfoTemplate findOrgHomeTemplate(String orgid, String deviceType) throws Exception;
	
	/**
	 * 获取设备发布首页模板
	 * @param orgid
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public InfoTemplate findDevHomeTemplate(String orgid, String deviceId) throws Exception;
	
	/**
	 * 根据设备类型查询机构下模板
	 * @param orgId
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplate> findOrgTemplateByDevType(String orgId, String deviceType) throws Exception;
	
	/**
	 * 只根据套号获取模板
	 * @param setNo
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplate> findBySetNo(String setNo) throws Exception;
	
	/**
	 * 从模板列表中找到每种类型模板的默认模板
	 * @param infoTemplates
	 * @return
	 * @throws Exception
	 */
	public Map<String, InfoTemplate> getDefaultTemplate(List<InfoTemplate> infoTemplates) throws Exception;
	
	/**
	 * 获取机构在用模板套信息
	 * @param orgId
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public InfoTemplate findOrgUsedTemplate(String orgId, String deviceType) throws Exception;
	
	/**
	 * 获取设备在用模板套信息
	 * @param orgId
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public InfoTemplate findDevUsedTemplate(String orgId, String deviceId) throws Exception;
	
	/**
	 * 获取机构可选模板
	 * @param orgId
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplate> getAllTemplates(String orgId, String deviceType) throws Exception;
	
	/**
	 * 获取套中指定类型的模板
	 * @param orgId
	 * @param setNo
	 * @param templateType
	 * @return
	 * @throws Exception
	 */
	public List<InfoTemplate> getColAllTemplates(String setNo, String templateType) throws Exception;
	
	/**
	 * 将模板列表转成map，结构为：{模板类型1:[模板1,模板2,模板3]}
	 * @param infoTemplates
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<InfoTemplate>> getTemplateMap(List<InfoTemplate> infoTemplates) throws Exception;
	
	public InfoTemplate findDefaultTemplate(String orgId, String deviceType) throws Exception;
	
	/**
	 * 返回整套模板的map(如：默认套、-1套、-2套)
	 * @param orgId
	 * @param setNo
	 * @param homeName 主页模板名称
	 * @return
	 * @throws Exception
	 */
	public Map<String, InfoTemplate> getTemplateSet(String orgId, String setNo, String homeName) throws Exception;
}
