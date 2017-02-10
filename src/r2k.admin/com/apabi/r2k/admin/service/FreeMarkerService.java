package com.apabi.r2k.admin.service;

import java.util.List;
import java.util.Map;

import com.apabi.r2k.admin.model.Column;

public interface FreeMarkerService {
	
	/** 
     * 通过模板生成文件 
     * @param ftlname 模板文件名(有后缀)
     * @param outFile 生成文件名(有后缀)
     * @param rootMap 数据
     * @param modelName 模块名
     */  
    public void createHtml(String ftlname, String outFile, Map<String, Object> rootMap, String modelName) throws Exception;
    
    /**
	 * 发布公用方法
	 * @param fileName	  模板文件名
	 * @param outFile 	  生成文件名
	 * @param pubPath	  生成文件的动态路径（包含机构id和设备id）
	 * @param columnlist 栏目列表
	 * @param articlelist文章列表
	 * @param welColumn	  欢迎语对象
	 * @return	
	 * @throws Exception
	 */
	public String publishUtil(String fileName, String outFile, String pubPath, List<Column> columnlist, List<Column> articlelist, Column welColumn) throws Exception;
	
	/**
	 * 发布二级页面公用方法
	 * @param fileName	模板文件名
	 * @param outName	生成文件名
	 * @param pubPath	生成文件的动态路径（包含机构id和设备id）
	 * @param columnlist数据
	 * @param title		父节点名称
	 * @return
	 * @throws Exception
	 */
	public String publishSubPageUtil(String fileName, String outName, String pubPath, List<Column> columnlist, String title) throws Exception;
	
	public void onekeyPublish(String orgid) throws Exception;
	
	public void orgPublish(String orgid) throws Exception;
	
	public List<String> devicePublish(String orgid, String deviceid) throws Exception;
	
	public List<String> deviceTypePublish(String orgid, String devType) throws Exception;
	
	/**
	 * 预览
	 * @param col
	 * @param baseDir
	 * @throws Exception
	 */
	public void preview(Column col, String baseDir) throws Exception;
}
