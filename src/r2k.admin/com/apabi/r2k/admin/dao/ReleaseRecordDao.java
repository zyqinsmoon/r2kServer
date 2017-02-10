package com.apabi.r2k.admin.dao;

import java.util.List;
import java.util.Map;

import com.apabi.r2k.admin.model.ReleaseRecord;

public interface ReleaseRecordDao {

	public int saveReleaseRecord(ReleaseRecord record) throws Exception;
	
	public void batchSaveRecords(List<ReleaseRecord> records) throws Exception;
	
	public int updateStatus(Map parameters) throws Exception;
	
	public int updateReleaseRecord(ReleaseRecord record) throws Exception;
	
	public void batchUpdateRecords(List<ReleaseRecord> records) throws Exception;
	
	public List<ReleaseRecord> findReleaseRecords(Map parameters) throws Exception;
	
	public ReleaseRecord findReleaseRecord(Map parameters) throws Exception;
	
	public int deleteByDeviceType(String orgid, String deviceType) throws Exception;
	
	public int deleteByDeviceId(String orgid, String deviceId) throws Exception;
	
	public int deleteAllByColIds(List colIds) throws Exception;
	
	public int deleteByColIds(Map parameters) throws Exception;
	
	/**
	 * 更新单条发布记录
	 */
	public int updateTemplateByColumn(Map<String, Object> params) throws Exception;
}
