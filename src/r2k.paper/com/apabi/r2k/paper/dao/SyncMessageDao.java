package com.apabi.r2k.paper.dao;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.paper.model.SyncMessage;

public interface SyncMessageDao {
	
	/**
	 * 入库
	 */
	public int save(SyncMessage syncMessage) throws Exception;
	
	/**
	 * 更新
	 */
	public int update(Map syncMessage) throws Exception;
	
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception; 
	
	/**
	 * 根据id查询
	 */
	public SyncMessage getById(int id) throws Exception;
	
	/**
	 * 查询缓存服务器的信息
	 */
	public List<SyncMessage> getCacheMsgs(Map params) throws Exception;
	
	/**
	 * 批量保存消息
	 */
	public void batchSaveMsgs(List<SyncMessage> syncMessages) throws Exception;
	
	/**
	 * 批量更新
	 */
	public void batchUpdateMsgs(List<SyncMessage> syncMessages) throws Exception;
	
	/**
	 * 根据消息类型更新
	 */
	public int updateByType(Map parameters) throws Exception;
	
	/**
	 * 删除指定用户或者设备的消息
	 */
	public int deleteMsgs(Map<String, Object> params) throws Exception;
	
	/**
	 * 获取设备消息
	 */
	public List<SyncMessage> getDeviceMsgs(Map<String, Object> params) throws Exception;
}
