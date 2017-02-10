package com.apabi.r2k.admin.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.DeviceDao;
import com.apabi.r2k.admin.model.Device;
import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("deviceDao")
public class DeviceDaoImpl extends BaseDaoImpl<Device, Serializable> implements DeviceDao {

	private Logger log = LoggerFactory.getLogger(DeviceDaoImpl.class);

	//保存设备信息
	public boolean saveDevice(Device device) throws Exception{
		boolean flag = false;
		int	num = baseDao.insert(getStatement("insertDevice"), device);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	//通过设备id获取对象
	public Device getDeviceById(int id, String orgId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("orgId", orgId);
		return baseDao.selectOne(getStatement("getDeviceById"), map);
	}
	//更新设备
	public boolean updateDevice(Device device)throws Exception{
		boolean flag = false;
		int	num = baseDao.update(getStatement("updateDeviceById"), device);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	//删除设备
	public boolean deleteDevice(int id, String orgId)throws Exception{
		boolean flag = false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("orgId", orgId);
		int	num = baseDao.delete(getStatement("deleteDeviceById"), map);
		if(num > 0){
			flag = true;
		}
		return flag;
	}
	//分页查询
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception {
		return basePageQuery(getStatement(statementName), pageRequest);
	}
	//批量添加设备列表
	public void addDeviceList(List<Device> list) throws Exception{
		for (Device device : list) {
			baseDao.insert(getStatement("insertDevice"), device);
		}
	}
	//通过设备deviceId获取对象
	public Device getDeviceByDevId(String deviceId, String orgId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceId", deviceId);
		map.put("orgId", orgId);
		return baseDao.selectOne(getStatement("getDeviceByDevId"), map);
	}
	//通过设备deviceId获取对象
	public Device getDeviceByDevName(String deviceName, String orgId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceName", deviceName);
		map.put("orgId", orgId);
		return baseDao.selectOne(getStatement("getDeviceByDevName"), map);
	}
	//通过OrgId获取设备列表
	public List<Device> getDeviceListByOrgId(String orgId)  throws Exception{
		return baseDao.selectList(getStatement("getDeviceListByOrgId"), orgId);
	}
	//通过设备Id和机构Id获取
	public Device getDeviceByDeviceId(String deviceId,String orgId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("deviceId", deviceId);
		map.put("orgId", orgId);
		return baseDao.selectOne(getStatement("getDeviceByDeviceId"), map);
	}
	//获取数量
	public int getDeviceCount(Map<String,Object> map) throws Exception{
		Integer count = baseDao.selectOne(getStatement("count"), map);
		return count;
	}
	
	public List<Device> getDeviceByToVersion(String toVersion,String deviceType) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("toVersion", toVersion);
		String[] deviceTypeList = deviceType.split("#");
		map.put("deviceTypeList", deviceTypeList);
		return baseDao.selectList(getStatement("getDeviceByToVersion"),map);
	}
}
