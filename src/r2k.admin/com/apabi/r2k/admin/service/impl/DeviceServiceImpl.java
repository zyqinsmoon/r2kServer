package com.apabi.r2k.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.DeviceDao;
import com.apabi.r2k.admin.model.ClientVersion;
import com.apabi.r2k.admin.model.Device;
import com.apabi.r2k.admin.model.OrgClientVersion;
import com.apabi.r2k.admin.model.PrjConfig;
import com.apabi.r2k.admin.service.ClientVersionService;
import com.apabi.r2k.admin.service.ColumnService;
import com.apabi.r2k.admin.service.DeviceService;
import com.apabi.r2k.admin.service.OrgClientVersionService;
import com.apabi.r2k.admin.service.PrjConfigService;
import com.apabi.r2k.admin.service.ReleaseRecordService;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.menu.service.MenuService;
import com.apabi.r2k.paper.model.SyncMessage;
import com.apabi.r2k.paper.service.SyncMessageService;
import com.apabi.r2k.security.app.AppLogin;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.service.AuthOrgService;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {
	@Resource(name="deviceDao")
	private DeviceDao deviceDao;
	@Resource
	private AuthOrgService authOrgService;
	@Resource
	private AppLogin appLogin;
	@Resource
	private ClientVersionService clientVersionService;
	@Resource
	private OrgClientVersionService orgClientVersionService;
	@Resource
	private MenuService menuService;
	@Resource
	private ColumnService columnService;
	@Resource
	private ReleaseRecordService releaseRecordService;
	@Resource(name="syncMessageService")
	private SyncMessageService syncMessageService;
	@Resource
	private PrjConfigService prjConfigService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	public static final String PAGE_QUERY_STATEMENT = "pageSelect";
	
	//保存设备
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean save(Device device) throws Exception {
		return this.deviceDao.saveDevice(device);
	}
	//通过id获取设备信息
	public Device getDeviceById(int deviceId, String orgId) throws Exception{
		return this.deviceDao.getDeviceById(deviceId, orgId);
	}
	//更新设备
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean update(Device device) throws Exception{
		return this.deviceDao.updateDevice(device);
	}
	//删除设备
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean delete(int deviceId, String orgId, boolean isDeleteAll) throws Exception{
		Device dev = deviceDao.getDeviceById(deviceId, orgId);
		String devId = dev.getDeviceId();
		String devType = dev.getDeviceType();
		boolean flag = this.deviceDao.deleteDevice(deviceId, orgId);
		if(flag && isDeleteAll){
			prjConfigService.deleteByDevId(orgId, devId);
			menuService.deleteDevMenu(orgId, devType, devId);
			columnService.deleteDevColumns(orgId, devId);
			releaseRecordService.deleteDevReleaseRecord(orgId, devId);
			syncMessageService.deleteDevMsgs(orgId, devType, devId);
		}
		return flag;
	}
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception {
		Page page = deviceDao.pageQuery(PAGE_QUERY_STATEMENT, pageRequest, countQuery);
		List<Device> list = page.getResult();
		for(Device device : list){
//			Date lastDate = device.getLastDate();
			int heartbeat = device.getHeartbeat();
			Date lastHeartTime = device.getLastHeartTime();
			if(lastHeartTime != null){
				boolean flag = Device.checkOnLine(lastHeartTime, heartbeat);
				if(flag){
					device.setIsOnline(Device.STATUS_ONLINE);
				} else {
					device.setIsOnline(Device.STATUS_OFFLINE);
				}
			} else {
				device.setIsOnline(Device.STATUS_OFFLINE);
			}
//			//15秒钟容错时间
//			Date date = DateUtils.addMinutes(lastDate, heartbeat + 1/4);
//			if(date.getTime() > new Date().getTime()){
//				device.setIsOnline(Device.STATUS_ONLINE);
//			}else{
//				device.setIsOnline(Device.STATUS_OFFLINE);
//			}
//			int deviceTypeId = device.getDeviceTypeId();
//			DevTypeEnum devTypeEnum = DevTypeEnum.findId(deviceTypeId);
//			device.setDeviceTypeName(devTypeEnum.getName());
		}
		return page;
	}
	//批量添加设备列表
	@Transactional(propagation=Propagation.REQUIRED)
	public void addDeviceList(List<Device> list) throws Exception{
		deviceDao.addDeviceList(list);
	}
	// 验证设备id唯一性
	public Device checkDeviceId(String deviceId, String orgId)  throws Exception{
		return this.deviceDao.getDeviceByDevId(deviceId, orgId);
	}
	// 验证设备id唯一性
	public Device checkDeviceName(String deviceName, String orgId)  throws Exception{
		return this.deviceDao.getDeviceByDevName(deviceName, orgId);
	}
	//通过OrgId获取设备列表
	public List<Device> getDeviceListByOrgId(String orgId)  throws Exception{
		return this.deviceDao.getDeviceListByOrgId(orgId);
	}
	//通过设备ID和机构ID
	@Transactional(propagation=Propagation.REQUIRED)
	public Device getDeviceByDeviceId(String deviceId,String orgId) throws Exception{
		return deviceDao.getDeviceByDeviceId(deviceId,orgId);
	}
	
	//注册设备是否已经达到上限
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean isLimit(String orgId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId", orgId);
		int count = deviceDao.getDeviceCount(map);
		AuthOrg org = authOrgService.getOrgObject(orgId);
		int deviceNum = org.getDeviceNum();
		if(count >= deviceNum){
			return true;
		}else{
			return false;
		}
    }
	
	//设备注册，防止重复注册
	@Transactional(propagation=Propagation.REQUIRED)
	public synchronized String register(String name,String orgId,String deviceId,String userAgent,String version) throws Exception{
		String xml;
		int interval = PropertiesUtil.getInt("heartbeat.interval");
		DevTypeEnum devTypeEnum = DevTypeEnum.findName(userAgent);
//		String devType = devTypeEnum.getName();
		String devTypeName = devTypeEnum.getName();
		Device deviceNameExit = checkDeviceName(name,orgId);
		Device deviceIdExit = checkDeviceId(deviceId,orgId);
		if(deviceNameExit == null){
			if(deviceIdExit == null){
				//新设备注册
    			if(isLimit(orgId)){
    				xml = "<Error Code=\"-1004\" Message=\"已经达到注册上限，无法再注册\"/>";
    			}else{
    				Device device = new Device();
					device.setDeviceName(name);
                	device.setDeviceId(deviceId);
                	device.setOrgId(orgId);
                	device.setHeartbeat(interval);
                	device.setLastDate(new Date());
//        			device.setDeviceTypeId(devType);
        			device.setDeviceType(devTypeName);
        			device.setCurVersion(version);
        			save(device);
        			//如果该机构第一次注册设备，初始化机构版本
        			OrgClientVersion orgClientVersion = orgClientVersionService.getOrgClientVersion(orgId, devTypeName);
        			if(orgClientVersion == null){
        				orgClientVersion = new OrgClientVersion();
        				orgClientVersion.setOrgId(orgId);
        				orgClientVersion.setVersion(version);
        				orgClientVersion.setVersionCodeByVerion(version);
        				orgClientVersion.setLastDate(new Date());
        				orgClientVersion.setDevType(devTypeName);
        				orgClientVersionService.save(orgClientVersion);
        			}
        			
        			log.info(name + "设备注册成功");
        			appLogin.androidLargeLogin(orgId, deviceId, userAgent,version);
        			xml = getXml(orgId,version, devTypeName, deviceId);
    			}
			}else{
				//设备再次注册，更新设备信息，更新客户端版本号
				if(StringUtils.isBlank(deviceIdExit.getDeviceName())){
					deviceIdExit.setDeviceName(name);
				}
    			deviceIdExit.setLastDate(new Date());
    			deviceIdExit.setCurVersion(version);
    			update(deviceIdExit);
    			log.info(name + "设备更新成功");
    			appLogin.androidLargeLogin(orgId, deviceId, userAgent,version);
    			String toVersion = deviceIdExit.getToVersion();
    			if(StringUtils.isNotBlank(toVersion)){
    				version = toVersion;
    			}
    			xml = getXml(orgId,version, devTypeName, deviceId);
			}
		}else{
			if(deviceIdExit != null && deviceIdExit.getId() == deviceNameExit.getId()){
				//设备相同，名称相同，不更新名称
    			deviceIdExit.setLastDate(new Date());
    			deviceIdExit.setCurVersion(version);
    			update(deviceIdExit);
    			String toVersion = deviceIdExit.getToVersion();
    			if(StringUtils.isNotBlank(toVersion)){
    				version = toVersion;
    			}
    			xml = getXml(orgId,version, devTypeName, deviceId);
			}else{
				xml = "<Error Code=\"-1004\" Message=\"设备名称与其他设备重名，请更换名称\"/>";
			}
		}
		return xml;
	}
	private String getXml(String orgId,String version, String devType, String deviceid) throws Exception{
//    	int interval = Integer.getInteger(PropertiesUtil.getValue("heartbeat.interval"),5);
		String baseUrl = PropertiesUtil.getValue("server.url");
//    	String menuUrl = "<![CDATA["+PropertiesUtil.getValue("r2k.api.menu") + "?orgid="+orgId+"&deviceid="+deviceid+"]]>";
		String menuUrl = getMenuUrlByVersion(orgId, deviceid, version);
		ClientVersion clientVersion = clientVersionService.getClientVersion(version, devType);
		String softLink = "";
		if(clientVersion != null){
			softLink = baseUrl + "/" + GlobalConstant.PROJECT_FILE_PATH + "/" + clientVersion.getPath();
		}
//		DevTypeEnum devTypeEnum = DevTypeEnum.findId(devType);
		String confXml = getConfXml(orgId, devType, deviceid);
		log.info("设备注册：[版本号：" + version + ",地址：" + softLink + "]");
//    	builder.append("<R2k><Heartbeat>").append(interval).append("</Heartbeat><Orgid>").append(orgId).append("</Orgid><Baseurl>").append(baseUrl).append("</Baseurl><Menuurl>")
//    	.append(menuUrl).append("</Menuurl><Softlink>").append(softLink).append("</Softlink><Version>").append(version).append("</Version></R2k>");
		StringBuilder builder = new StringBuilder("<R2k>");
		builder.append("<Orgid>"+orgId+"</Orgid>");
		builder.append("<Menuurl>"+menuUrl+"</Menuurl>");
		builder.append("<Softlink>"+softLink+"</Softlink>");
		builder.append("<Version>"+version+"</Version>");
		builder.append("<Heartbeat>"+PropertiesUtil.get("heartbeat.interval")+"</Heartbeat>");
		builder.append(confXml);
		builder.append("</R2k>");
    	return builder.toString();
    }
	
	/**
	 * 获取心跳响应xml
	 */
	@Override
	public String getHeartbeatXml(String orgId, String devType, String deviceId) throws Exception {
		String confXml = getConfXml(orgId, devType, deviceId);
		StringBuilder builder = new StringBuilder("<R2k>");
		builder.append(confXml);
		builder.append("</R2k>");
		return builder.toString();
	}
	
	//生成设备配置xml
	private String getConfXml(String orgId, String devType, String deviceId) throws Exception{
		List<SyncMessage> syncMessages = syncMessageService.getDeviceMsgs(orgId, devType, deviceId);
		StringBuilder xmlBuilder = new StringBuilder("<Conf>");
		for(SyncMessage message : syncMessages){
			if(message.getType().equals(SyncMessage.TYPE_CONFIG)){
				List<String> configKeys = new ArrayList<String>();
				configKeys.add(PrjConfig.DEVICE_START_TIME);
				configKeys.add(PrjConfig.DEVICE_END_TIME);
				configKeys.add(PrjConfig.DEVICE_HOTSPOT);
				Map<String, PrjConfig> prjConfigs = prjConfigService.getPrjConfigMapByDevId(orgId, devType, deviceId, PrjConfig.CONFIG_ENABLE, configKeys);
				//添加自动开关机配置
				xmlBuilder.append("<Autotime>");
				if(prjConfigs.containsKey(PrjConfig.DEVICE_START_TIME)&&prjConfigs.containsKey(PrjConfig.DEVICE_END_TIME)){
					xmlBuilder.append("<Auto>true</Auto>");
					PrjConfig startConfig = prjConfigs.get(PrjConfig.DEVICE_START_TIME);
					String startTime = startConfig.getConfigValue();
					PrjConfig endConfig = prjConfigs.get(PrjConfig.DEVICE_END_TIME);
					String endTime = endConfig.getConfigValue();
					xmlBuilder.append("<Time>"+startTime+"#"+endTime+"</Time>");
				}else{
					xmlBuilder.append("<Auto>false</Auto>");
				}
				xmlBuilder.append("</Autotime>");
				//添加wifi开启配置
				xmlBuilder.append("<Autohot>");
				String wifi = "false";
				if(prjConfigs.containsKey(PrjConfig.DEVICE_HOTSPOT)){
					PrjConfig hotspotConfig = prjConfigs.get(PrjConfig.DEVICE_HOTSPOT);
					if(hotspotConfig.getConfigValue().equals(GlobalConstant.WIFI_OPEN)){
						wifi = "true";
					}
				}
				xmlBuilder.append("<Auto>"+wifi+"</Auto>");
				xmlBuilder.append("</Autohot>");
				xmlBuilder.append("<Heartbeat>"+PropertiesUtil.get("heartbeat.interval")+"</Heartbeat>");
				//添加设备名称配置
				Device device = deviceDao.getDeviceByDevId(deviceId, orgId);
				xmlBuilder.append("<Devicename>"+device.getDeviceName()+"</Devicename>");
				message.setLastSendDate(new Date());
				message.setStatus(SyncMessage.STATUS_DEAL);
			}
		}
		//添加心跳时间配置
		xmlBuilder.append("</Conf>");
		syncMessageService.batchUpdateMsgs(syncMessages);
		return xmlBuilder.toString();
	}
	
	//根据版本号获取菜单地址
	private String getMenuUrlByVersion(String orgId, String deviceid,String version) throws Exception{
		if(version.compareTo("1.0.0") <= 0){
			return PropertiesUtil.getValue("server.url");		//版本号小于或等于1.0.0时菜单地址为基地址
		}else{
			return "<![CDATA["+PropertiesUtil.get("url.r2k.api")+"/"+PropertiesUtil.get("path.menu") + "?orgid="+orgId+"&deviceid="+deviceid+"]]>";
		}
	}
	
	public List<Device> getByToVersion(String toVersion, String deviceType) throws Exception{
		return deviceDao.getDeviceByToVersion(toVersion,deviceType);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public String registerSlave(String orgId,String deviceId, String name) throws Exception{
		int interval = PropertiesUtil.getInt("heartbeat.interval");
		
		Device device = deviceDao.getDeviceByDeviceId(deviceId, orgId);
		if(device == null){
			device = new Device();
			device.setDeviceId(deviceId);
	    	device.setOrgId(orgId);
	    	device.setHeartbeat(interval);
	    	device.setLastDate(new Date());
	    	device.setDeviceType(GlobalConstant.USER_AGENT_SLAVE);
	    	device.setDeviceName(name);
	    	device.setDeviceTypeId(DevTypeEnum.Slave.getId());
			save(device);
		}else{
			device.setHeartbeat(interval);
			device.setDeviceName(name);
			device.setLastHeartTime(new Date());
			update(device);
		}
		StringBuilder builder = new StringBuilder();
    	builder.append("<R2k><Heartbeat>").append(interval).append("</Heartbeat>").append("</R2k>");
		//authTokenService.addScreenRole(orgId, deviceId);
		return builder.toString();
	}
	
	//子节点分页查询
	public Page<?> pageSlave(PageRequest<?> pageRequest,String orgId) throws Exception {
		Map param = (Map)pageRequest.getFilters();
		param.put("deviceType", "slave");
		param.put("orgId", orgId);
		Page page = deviceDao.pageQuery(PAGE_QUERY_STATEMENT, pageRequest, null);
		List<Device> list = page.getResult();
		for(Device device : list){
			Date lastDate = device.getLastDate();
			int heartbeat = device.getHeartbeat();
			//15秒钟容错时间
			Date date = DateUtils.addMinutes(lastDate, heartbeat + 1/4);
			if(date.getTime() > new Date().getTime()){
				device.setIsOnline(Device.STATUS_ONLINE);
			}else{
				device.setIsOnline(Device.STATUS_OFFLINE);
			}
		}
		return page;
	}
}
