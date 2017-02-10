package com.apabi.r2k.admin.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.ColumnDao;
import com.apabi.r2k.admin.model.Column;
import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.common.utils.GlobalConstant;

@Repository("columnDao")
public class ColumnDaoImpl extends BaseDaoImpl<Column, Serializable> implements ColumnDao {
	
	private Logger log = LoggerFactory.getLogger(ColumnDaoImpl.class);
	
	private String DEVICE_PAGE_QUERY_STATEMENT = "devicePageSelect";
	private String ORG_PAGE_QUERY_STATEMENT = "orgPageSelect";
	private String ORG_PAGE_QUERY_COUNT = "orgPageSelectCount";
	
	
	
	public int insertColumn(Column column) throws Exception{
		return baseDao.insert(getStatement("insert"), column);
	}
	
	public void deleteById(int id) throws Exception{
		baseDao.delete(getStatement("deleteById"),id);
	}
	
	public int updateColumn(Column column) throws Exception{
		return baseDao.update(getStatement("update"),column);
	}
	
	public Column getColumnObject(int id) throws Exception{
		return baseDao.selectOne(getStatement("objectById"), id);
	}	
	public List<Column> getByPid(int parentId) throws Exception{
		return baseDao.selectList(getStatement("getByPid"),parentId);
	}
	
	public List<Column> getOrderList() throws Exception{
		List<Column> list = new ArrayList<Column>();
		getOrderList(0,list);
		return list;
	}
	
	private void getOrderList(int pid,List<Column> list) throws Exception{
		List<Column> highList = getByPid(pid);
		for(Column column : highList){
			list.add(column);
			int id = column.getId();
			getOrderList(id,list);
		}
	}
	//通过parentId查询非停用信息列表
	public List<Column> getByPidInUsed(Map<String,Object> map) throws Exception{
		return baseDao.selectList(getStatement("getByPidInUsed"),map);
	}

	//通过parentId查询非停用信息总数
	public int getCountByPidInUsed(Map<String, Object> map) throws Exception {
		return (Integer)baseDao.selectOne(getStatement("getCountByPidInUsed"),map);
	}
	
	//分页查询设备的栏目
	public Page<?> devicePageQuery(PageRequest<?> pageRequest) throws Exception {
		return basePageQuery(getStatement(DEVICE_PAGE_QUERY_STATEMENT), pageRequest);
	}
	
	public Page<?> orgPageQuery(PageRequest<?> pageRequest) throws Exception{
		return basePageQuery(getStatement(ORG_PAGE_QUERY_STATEMENT), pageRequest, ORG_PAGE_QUERY_COUNT);
	}
	
	public List<Column> getColumnByDevice(String deviceId) throws Exception{
		return baseDao.selectList(getStatement("getColumnByDevice"),deviceId);
	}
	
	public Column getDeviceQuote(int quoteId,String deviceId,int parentId,String orgId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId", orgId);
		map.put("quoteId", quoteId);
		map.put("deviceId", deviceId);
		map.put("parentId", parentId);
		return baseDao.selectOne(getStatement("getColumn"),map);
	}
	
	public List<Column> getByPidDevice(Map<String,Object> map) throws Exception{
		return baseDao.selectList(getStatement("getByPidDevice"),map);
	}
	
	//获取所有对该栏目的引用
	public List<Column> getColumnByQuoteId(int quoteId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("quoteId", quoteId);
		return baseDao.selectList(getStatement("getColumn"),map);
	}
	//查看某一设备的欢迎页
	@Override
	public Column getDeviceWelcome(String orgId,String deviceId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId", orgId);
		map.put("deviceId", deviceId);
		map.put("type", GlobalConstant.TYPE_WELCOME);
		return baseDao.selectOne(getStatement("getDeviceWelcome"),map);
	}
	
	@Override
	public Column getOrgWelcome(String orgId, String deviceType) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId", orgId);
		map.put("deviceType", deviceType);
		map.put("type", GlobalConstant.TYPE_WELCOME);
		return baseDao.selectOne(getStatement("getOrgWelcome"),map);
	}

	@Override
	public List<Column> findHomeColumns(Map parameters) throws Exception {
		return baseDao.selectList(getStatement("findHomeCols"), parameters);
	}

	@Override
	public List<Column> findByParentId(Map parameters) throws Exception {
		return baseDao.selectList(getStatement("findByParentId"), parameters);
	}

	@Override
	public void batchSaveColumns(List<Column> columns) throws Exception {
		batchSave(columns);
	}

	@Override
	public int savePic(Column column) throws Exception {
		return baseDao.insert(getStatement("insertPic"), column);
	}

	@Override
	public int batchDeleteByIds(String orgid, List ids) throws Exception {
		Map params = new HashMap();
		params.put("ids", ids);
		params.put("orgId", orgid);
		return batchDelete(params);
	}

	@Override
	public List<Column> findColumnAndChilds(Map parameter) throws Exception {
		return baseDao.selectList(getStatement("findColumnAndChilds"),parameter);
	}

	@Override
	public void batchUpdateCols(List<Column> columns) throws Exception {
		batchUpdate(getStatement("update"), columns);
	}

	@Override
	public List<Column> getOrgQuoted(Map params) throws Exception {
		return baseDao.selectList(getStatement("getOrgQuoted"),params);
	}
	
	@Override
	public List<Column> getDevQuoted(Map params) throws Exception{
		return baseDao.selectList(getStatement("getDevQuoted"),params);
	}
	@Override
	public List<Column> findByIds(List ids) throws Exception {
		Map params = new HashMap();
		params.put("ids", ids);
		return baseDao.selectList(getStatement("findByIds"), params);
	}

	@Override
	public List<Column> findPublishCols(Map parameters) throws Exception {
		return baseDao.selectList("findPubCols", parameters);
	}
	
	@Override
	public List<Column> findAllColumns(Map parameters) throws Exception{
		return baseDao.selectList(getStatement("getColumn"),parameters);
	}

	@Override
	public int deleteColumnAndQuote(List ids) throws Exception {
		Map params = new HashMap();
		params.put("ids", ids);
		return baseDao.delete(getStatement("delColAndQuote"), params);
	}

	@Override
	public List<Column> getChildsByPid(Map parameters) throws Exception {
		return baseDao.selectList(getStatement("getChildsByPid"), parameters);
	}

	@Override
	public List<Column> findPreviewColumns(int id, String devType, String deviceId) throws Exception {
		Map params = new HashMap();
		params.put("id", id);
		params.put("devType", devType);
		params.put("deviceId", deviceId);
		return baseDao.selectList(getStatement("previewCols"), params);
	}

	@Override
	public Page quotePageQuery(PageRequest pageRequest) throws Exception {
		return basePageQuery(getStatement("quotePageSelect"), pageRequest, getStatement("quotePageSelectCount"));
	}

	/**
	 * 批量删除设备资讯内容
	 */
	@Override
	public int deleteDevColumn(Map<String, Object> params) throws Exception {
		return baseDao.delete(getStatement("deleteDevColumn"), params);
	}

	@Override
	public List<Column> getAllColumns(Map<String, Object> params)
			throws Exception {
		return baseDao.selectList(getStatement("getAllColumns"), params);
	}
}
