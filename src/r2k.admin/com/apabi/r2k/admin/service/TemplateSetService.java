package com.apabi.r2k.admin.service;

import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.InfoTemplate;
import com.apabi.r2k.admin.model.InfoTemplateSet;

public interface TemplateSetService {
	/**
	 * 入库
	 */
	public int save(InfoTemplateSet templateSet) throws Exception;
	/**
	 * 批量保存
	 */
	public boolean batchSave(InfoTemplateSet templateSet, String tempListLenStr) throws Exception;
	/**
	 * 批量保存
	 */
	public boolean batchSave(List<InfoTemplateSet> tempsetlist) throws Exception;
	
	/**
	 * 更新
	 */
	public int update(InfoTemplateSet templateSet) throws Exception;
	/**
	 * 批量更新
	 */
	public boolean batchUpdate(List<InfoTemplateSet> tempsetlist) throws Exception;
	/**
	 * 根据ID删除
	 */
	public int deleteById(java.lang.Integer id) throws Exception;
	/**
	 * 根据模板ID查询
	 */
	public InfoTemplateSet getTemplateSetById(java.lang.Integer id) throws Exception;
	/**
	 * 根据模板ID查询
	 */
	public InfoTemplateSet getTemplateSetBySetNo(String orgId, String setNo) throws Exception;
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception; 
	/**
	 * 查找默认设备类型模板列表
	 * @param scope
	 * @param defTypeList
	 * @return true:不存在默认模板,false:存在默认模板
	 * @throws Exception
	 */
	public boolean checkDefaultType(int scope, String defTypeList, String orgId) throws Exception;
	
	/**
	 * 按机构内的设备类型查找模板套信息
	 * @param orgId
	 * @param devType
	 * @return
	 */
	public List<InfoTemplateSet> getTemplateSetByDevType(String orgId, String devType) throws Exception;
}
