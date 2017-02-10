package com.apabi.r2k.admin.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.apabi.r2k.admin.dao.ReleaseRecordDao;
import com.apabi.r2k.admin.model.ReleaseRecord;
import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("releaseRecordDao")
public class ReleaseRecordDaoImpl extends BaseDaoImpl<ReleaseRecord, Serializable> implements ReleaseRecordDao {

	@Override
	public int saveReleaseRecord(ReleaseRecord record) throws Exception {
		return save(record);
	}

	@Override
	public void batchSaveRecords(List<ReleaseRecord> records) throws Exception {
		batchSave(records);
	}

	@Override
	public int updateStatus(Map parameters) throws Exception{
		return baseDao.update(getStatement("updateStatus"), parameters);
	}
	
	@Override
	public int updateReleaseRecord(ReleaseRecord record) throws Exception {
		return update(record);
	}

	@Override
	public void batchUpdateRecords(List<ReleaseRecord> records)
			throws Exception {
		batchUpdate(getStatement("update"), records);
	}

	@Override
	public List<ReleaseRecord> findReleaseRecords(Map parameters)
			throws Exception {
		return baseDao.selectList(getStatement("findRecords"), parameters);
	}

	@Override
	public ReleaseRecord findReleaseRecord(Map parameters) throws Exception {
		return baseDao.selectOne(getStatement("findRecords"), parameters);
	}

	@Override
	public int deleteByDeviceType(String orgid, String deviceType)
			throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgid);
		params.put("deviceType", deviceType);
		return baseDao.delete(getStatement("delByDevType"), params);
	}

	@Override
	public int deleteByDeviceId(String orgid, String deviceId) throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgid);
		params.put("deviceId", deviceId);
		return baseDao.delete(getStatement("delByDeviceid"), params);
	}

	@Override
	public int deleteAllByColIds(List colIds) throws Exception {
		Map params = new HashMap();
		params.put("colIds", colIds);
		return baseDao.delete(getStatement("delAllByColids"), params);
	}

	@Override
	public int deleteByColIds(Map parameters) throws Exception {
		return baseDao.delete(getStatement("delByColids"), parameters);
	}

	@Override
	public int updateTemplateByColumn(Map<String, Object> params) throws Exception {
		return baseDao.update(getStatement("updateTempId"), params);
	}

}
