package com.apabi.r2k.admin.dao;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.Column;

public interface ColumnDao {
	int insertColumn(Column column) throws Exception;
	void deleteById(int id) throws Exception;
	int updateColumn(Column column) throws Exception;
	Column getColumnObject(int id) throws Exception;
	List<Column> getByPid(int parentId) throws Exception;
	//递归查询栏目文章的结构
	List<Column> getOrderList() throws Exception;
	//通过parentId查询非停用信息列表
	List<Column> getByPidInUsed(Map<String,Object> map) throws Exception;
	//通过parentId查询非停用信息总数
	int getCountByPidInUsed(Map<String,Object> map) throws Exception;
	//分页查询设备栏目列表
	public Page<?> devicePageQuery(PageRequest<?> pageRequest) throws Exception;
	//分页查询机构栏目列表
	public Page<?> orgPageQuery(PageRequest<?> pageRequest) throws Exception;
	//获取某一设备的所有一级栏目
	public List<Column> getColumnByDevice(String deviceId) throws Exception;
	//获取设备引用的一条数据
	public Column getDeviceQuote(int quoteId,String deviceId,int parentId,String orgId) throws Exception;
	//通过parentId获取某一设备的栏目
	public List<Column> getByPidDevice(Map<String,Object> map) throws Exception;
	//获取所有对该栏目的引用
	public List<Column> getColumnByQuoteId(int quoteId) throws Exception;
	//查看某一机构的欢迎页
	public Column getOrgWelcome(String orgId, String deviceType) throws Exception;
	//查询某一设备的欢迎页
	public Column getDeviceWelcome(String orgId,String deviceId) throws Exception;
	
	public List<Column> findHomeColumns(Map parameters) throws Exception;
	
	public List<Column> findByParentId(Map parameters) throws Exception;
	
	public void batchSaveColumns(List<Column> columns) throws Exception;
	
	/**
	 * 保存图片，需要在sql中拼接图片地址
	 */
	public int savePic(Column column) throws Exception;
	
	/**
	 * 按id批量删除
	 */
	public int batchDeleteByIds(String orgid,List ids) throws Exception;
	
	/**
	 * 根据id查询内容及子内容
	 */
	public List<Column> findColumnAndChilds(Map parameter) throws Exception;
	
	public void batchUpdateCols(List<Column> columns) throws Exception;
	
	public List<Column> getOrgQuoted(Map params) throws Exception;
	
	public List<Column> getDevQuoted(Map params) throws Exception;
	
	public List<Column> findByIds(List ids) throws Exception;
	
	public List<Column> findPublishCols(Map parameters) throws Exception;
	
	public List<Column> findAllColumns(Map parameters) throws Exception;
	
	public int deleteColumnAndQuote(List ids) throws Exception;
	
	public List<Column> getChildsByPid(Map parameters) throws Exception;
	
	public List<Column> findPreviewColumns(int id, String devType, String deviceId) throws Exception;
	
	public Page quotePageQuery(PageRequest pageRequest) throws Exception;
	
	/**
	 * 批量删除设备资讯内容
	 */
	public int deleteDevColumn(Map<String, Object> params) throws Exception;
	
	/**
	 * 查询所有资讯内容，不拼接引用
	 */
	public List<Column> getAllColumns(Map<String, Object> params) throws Exception;
}
