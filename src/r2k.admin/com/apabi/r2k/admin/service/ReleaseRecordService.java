package com.apabi.r2k.admin.service;

import java.util.List;
import java.util.Map;

import com.apabi.r2k.admin.model.Column;
import com.apabi.r2k.admin.model.ReleaseRecord;

public interface ReleaseRecordService {

public int saveReleaseRecord(ReleaseRecord record) throws Exception;
	
	public void batchSaveRecords(List<ReleaseRecord> records) throws Exception;
	
	public int updateStatus(Map parameters) throws Exception;
	
	public int updateReleaseRecord(ReleaseRecord record) throws Exception;
	
	public void batchUpdateRecords(List<ReleaseRecord> records) throws Exception;
	
	public List<ReleaseRecord> findReleaseRecords(Map parameters) throws Exception;
	
	public ReleaseRecord findDevTypeRecord(String orgid, String devType) throws Exception;
	
	public ReleaseRecord findDeviceRecord(String orgid, String deviceid) throws Exception;
	
	public void selectOrgTemplate(String orgid, String devType, String setNo, String homeName) throws Exception;
	
	public void selectDeviceTemplate(String orgid, String deviceId, String setNo, String homeName) throws Exception;
	
	public void selectOrgColTemplate(String orgid, String devType, String setNo, int templateId, int colId) throws Exception;
	
	public void selectDevColTemplate(String orgid, String deviceId, String setNo, int templateId, int colId) throws Exception;
	
	public void deleteAllByColids(List ids) throws Exception;
	
	public int addDevHomeTemplate(String orgId, String deviceId, int templateId) throws Exception;
	
	public int addOrgHomeTemplate(String orgId, String deviceType, int templateId) throws Exception;
	
	public int saveOrgReleaseRecord(String orgId, String deviceType, int columnId, int templateId) throws Exception;
	
	public int saveDevReleaseRecord(String orgId, String deviceId, int columnId, int templateId) throws Exception;
	
	/**
	 * 添加机构内容及其子内容发布信息
	 * @param column
	 * @throws Exception
	 */
	public void addOrgColumnsRecord(Column column) throws Exception;
	
	/**
	 * 添加设备内容及其子内容发布信息
	 * @param column
	 * @throws Exception
	 */
	public void addDevColumnsRecord(Column column) throws Exception;
	
	/**
	 * 创建机构发布记录
	 */
	public ReleaseRecord createOrgReleaseRecord(String orgid, String deviceType, Integer columnId, int templateId);
	
	/**
	 * 创建设备发布记录
	 */
	public ReleaseRecord createDevReleaseRecord(String orgid, String deviceId, Integer columnId, int templateId);
	
	/**
	 * 根据内容id、机构id、设备类型和设备id更新单条发布记录的模板id
	 */
	public int updateSingleRecord(String orgid, String deviceId, String devType, int columnId, int templateId) throws Exception;
	
	/**
	 * 删除设备发布信息
	 */
	public int deleteDevReleaseRecord(String orgId, String deviceId) throws Exception;
}
