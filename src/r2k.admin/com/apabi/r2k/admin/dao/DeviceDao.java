package com.apabi.r2k.admin.dao;

import java.util.List;
import java.util.Map;

import com.apabi.r2k.admin.model.Device;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

public interface DeviceDao {

	/**
	 * 保存设备信息
	 * @param device
	 */
	public boolean saveDevice(Device device) throws Exception; 
	/**
	 * 通过id获取设备信息
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public Device getDeviceById(int deviceId, String orgId) throws Exception;
	/**
	 * 更新设备
	 * @param device
	 * @return
	 * @throws Exception
	 */
	public boolean updateDevice(Device device)throws Exception; 
	/**
	 * 删除设备
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public boolean deleteDevice(int deviceId, String orgId)throws Exception; 
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(String statementName, PageRequest<?> pageRequest, String countQuery) throws Exception;
	/**
	 * 批量添加设备列表
	 * @param list
	 */
	public void addDeviceList(List<Device> list) throws Exception;
	/**
	 * 验证设备id唯一性
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public Device getDeviceByDevId(String deviceId, String orgId)throws Exception;
	/**
	 * 验证设备名称唯一性
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public Device getDeviceByDevName(String deviceName, String orgId)throws Exception;
	/**
	 * 通过OrgId获取设备列表
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public List<Device> getDeviceListByOrgId(String orgId)  throws Exception;
	//通过设备id和机构id获取
	public Device getDeviceByDeviceId(String deviceId,String orgId) throws Exception;
	//获取数量
	public int getDeviceCount(Map<String,Object> map) throws Exception;
	//获取升级版本为该版本的所有设备
	public List<Device> getDeviceByToVersion(String toVersion, String deviceType) throws Exception;
}
