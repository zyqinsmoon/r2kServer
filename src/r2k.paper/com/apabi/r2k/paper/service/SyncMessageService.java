package com.apabi.r2k.paper.service;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.paper.model.SyncMessage;

public interface SyncMessageService {

	/**
	 * 入库
	 */
	public int save(SyncMessage syncMessage) throws Exception;
	
	/**
	 * 更新
	 */
	public int update(SyncMessage syncMessage) throws Exception;
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception;
	
	/**
	 * 根据id查询
	 */
	public SyncMessage getById(int id) throws Exception;
	
	/**
	 * 获取消息
	 */
	public List<SyncMessage> getCacheMsgs(Map params) throws Exception;
	
	/**
	 * 报纸消息增加到队列
	 */
	public void savePaperMsg(String paperId, String periodId, String publishDate) throws Exception;
	
	/**
	 * 分发filter消息
	 */
	public void distributeFilterMsg(String orgId) throws Exception;
	
	/**
	 * 批量更新消息
	 */
	public void batchUpdateMsgs(List<SyncMessage> syncMessages) throws Exception;
	
	/**
	 * 分发配置消息
	 */
	public void saveConfigMsg(String orgId, String deviceType, String deviceId) throws Exception;
	
	/**
	 * 删除设备的消息
	 */
	public int deleteDevMsgs(String orgId, String devType, String deviceId) throws Exception;
	
	/**
	 * 获取设备的消息
	 */
	public List<SyncMessage> getDeviceMsgs(String orgId, String devType, String deviceId) throws Exception;
}
