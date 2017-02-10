package com.apabi.r2k.admin.service;

import java.io.File;
import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.Column;
import com.apabi.r2k.admin.model.Picture;

public interface ColumnService {
	void addColumn(Column column) throws Exception;
	void deleteColumnById(int id) throws Exception;
	void updateColumn(Column column) throws Exception;
	Column getColumnObject(int id) throws Exception;
	List<Column> getColumnListByPid(int parentId) throws Exception;
	List<Column> getOrderList() throws Exception;
	Column getColumnById(int id) throws Exception;
	//通过parentId查询非停用信息列表
	List<Column> getByPidInUsed(int parentId, String orgId) throws Exception;
	//通过parentId查询非停用信息总数
	int getCountByPidInUsed(int parentId, String orgId) throws Exception ;
	
	//分页查询某一设备的栏目列表
	public Page<?> devicePageQuery(PageRequest<?> pageRequest) throws Exception;
	//分页查询机构栏目
	public Page<?> orgPageQuery(PageRequest<?> pageRequest) throws Exception;
	
	public List<Column> getByPid(int parentId) throws Exception;
	//获取设备的所有一级栏目
	public List<Column> getColumnByDevice(String deviceId) throws Exception;
	
	//获取某一引用栏目
	public Column getDeviceQuote(int quoteId,String deviceId,int parentId,String orgId) throws Exception;
	//通过parentId获取某一设备的栏目
	public List<Column> getByPidDevice(int parentId,String orgId,String deviceId) throws Exception;
	//获取所有对该栏目的引用
	public List<Column> getColumnByQuoteId(int quoteId) throws Exception;
	//查看某一机构的欢迎页
	public Column getOrgWelcome(String orgId,String deviceType) throws Exception;
	//查询某一设备的欢迎页
	public Column getDeviceWelcome(String orgId,String deviceId) throws Exception;
	//通过deviceId修改设备专题状态和link
	public void updateQuoteColumn(int id, int status, String link) throws Exception;
	
	/**
	 * 保存图集
	 */
	public void savePics(Column column,File thumbnail, List<Picture> pictures) throws Exception;
	
	/**
	 * 更新图集
	 */
	public void updatePics(Column column,File thumbnail, List<Integer> deletePics, List<Picture> pictures,List<Picture> newPictures) throws Exception;
	
	/**
	 * 根据id获取图集及子图片
	 */
	public Column findPics(int id) throws Exception;
	
	/**
	 * 添加图片
	 */
	public void savePic(Column column, File image) throws Exception;
	
	/**
	 * 更新图片
	 */
	public void updatePic(Column column, File newImage) throws Exception;
	
	public void addOrgQuoted(String orgid, String devType, int[] quoteIds, String setNo, String homeName) throws Exception;
	
	public void addDevQuoted(String orgid, String deviceId, int[] quoteIds, String setNo, String homeName) throws Exception;
	
	public List<Column> findOrgPublishCols(String orgid, String deviceType) throws Exception;
	
	public List<Column> findDevPublishCols(String orgid, String deviceId) throws Exception;
	
	public List<Column> findOrgAllColumns(String orgid, String deviceType) throws Exception;
	
	public List<Column> findDevAllColumns(String orgid, String deviceId) throws Exception;
	
	public void saveArtCol(Column column, File image) throws Exception;
	
	public void saveArt(Column column, File thumbnail, List<Picture> pictures) throws Exception;
	
	public void removeOrgColumn(String orgid, String devType, int id) throws Exception;
	
	public void removeDevColumn(String orgid, String deviceId, int id) throws Exception;
	
	public List<Column> getOrgChildColumns(String orgId, String devType, int parentId) throws Exception;
	
	public List<Column> getDevChildColumns(String orgId, String deviceId, int parentId) throws Exception;
	
	public Column hasWelcome(List<Column> columns) throws Exception;
	
	/**
	 * 根据id获取图集及子图片
	 */
	public Column findArt(int id) throws Exception;
	
	public Column getPreviewCol(String orgId, int id, String devType, String deviceId) throws Exception;
	
	public void savePicsCol(Column column, File image) throws Exception;
	
	public void updatePicsCol(Column column, File newImage, int templateId) throws Exception;
	
	public void updateArtCol(Column column, File newImage, int templateId) throws Exception;
	
	/**
	 * 保存欢迎页
	 * @param column
	 * @param image
	 * @throws Exception
	 */
	public void saveWelcome(Column column, File image) throws Exception;
	
	/**
	 * 更新欢迎页
	 * @param column
	 * @param newImage
	 * @throws Exception
	 */
	public void updateWelcome(Column column, File newImage) throws Exception;
	
	/**
	 * 引用页分页查询
	 */
	public Page quotePageQuery(PageRequest pageRequest) throws Exception;
	
	/**
	 * 更新文章
	 */
	public void updateArt(Column column,File thumbnail, List<Integer> deletePics, List<Picture> pictures, List<Picture> newPictures, int templateId) throws Exception;
	
	//删除设备资讯内容
	public int deleteDevColumns(String orgId, String deviceId) throws Exception;
}
