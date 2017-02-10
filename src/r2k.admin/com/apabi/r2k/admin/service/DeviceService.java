package com.apabi.r2k.admin.service;

import java.util.List;

import com.apabi.r2k.admin.model.Device;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

public interface DeviceService {

	/**
	 * 保存设备
	 * @param device
	 * @return
	 * @throws Exception
	 */
	public boolean save(Device device) throws Exception;
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
	public boolean update(Device device) throws Exception;
	/**
	 * 删除设备
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public boolean delete(int deviceId, String orgId, boolean isDeleteAll) throws Exception;
	/**
	 * 分页查询
	 */
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception;
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
	public Device checkDeviceId(String deviceId, String orgId)  throws Exception;
	/**
	 * 验证设备名称唯一性
	 * @param deviceName
	 * @return
	 * @throws Exception
	 */
	public Device checkDeviceName(String deviceName, String orgId)  throws Exception;
	/**
	 * 通过OrgId获取设备列表
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public List<Device> getDeviceListByOrgId(String orgId)  throws Exception;
	//通过设备ID（mac）和机构ID获取
	public Device getDeviceByDeviceId(String deviceId,String orgId) throws Exception;
	//注册设备是否已经达到上限
	public boolean isLimit(String orgId) throws Exception;
	//设备注册，防止重复注册
	public String register(String name,String orgId,String deviceId,String userAgent,String version) throws Exception;
	//获取升级为该版本的设备类表
	public List<Device> getByToVersion(String toVersion, String deviceType) throws Exception;
	//子节点注册
	public String registerSlave(String orgId,String deviceId, String name) throws Exception;
	//子节点分页查询
	public Page<?> pageSlave(PageRequest<?> pageRequest,String orgId) throws Exception;

	/**
	 * 获取心跳响应xml
	 * @param orgId
	 * @param devType
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public String getHeartbeatXml(String orgId, String devType, String deviceId) throws Exception;
}
