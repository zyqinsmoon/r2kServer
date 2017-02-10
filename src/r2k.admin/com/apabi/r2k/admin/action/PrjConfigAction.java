package com.apabi.r2k.admin.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;

import com.apabi.r2k.admin.model.PrjConfig;
import com.apabi.r2k.admin.service.PrjConfigService;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.paper.service.SyncMessageService;
import com.apabi.r2k.security.utils.SessionUtils;

@Controller("prjConfigAction")
@Scope("prototype")
public class PrjConfigAction{
	
	private Logger log = LoggerFactory.getLogger(PrjConfigAction.class);
	
	@Resource(name="prjConfigService")
	private PrjConfigService prjConfigService;	
	@Resource(name="syncMessageService")
	private SyncMessageService syncMessageService;
	private String redirectAction;
	
	private Page page;
	private PrjConfig prjConfig;
	private List<PrjConfig> configlist;
	private List<Integer> configIds;
	private String deviceStartTime;
	private String deviceEndTime;
	private String deviceHotspot;
	private String deviceType;
	private String deviceId;
	private String deviceName;
	
	
	//触摸屏横屏设置
	public String showByLarge(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				List<String> keys = new ArrayList<String>();
				keys.add(PrjConfig.DEVICE_START_TIME);
				keys.add(PrjConfig.DEVICE_END_TIME);
				keys.add(PrjConfig.DEVICE_HOTSPOT);
				deviceType = DevTypeEnum.AndroidLarge.getName();
				String enable = PrjConfig.CONFIG_ENABLE;
				configlist = prjConfigService.getPrjConfigListByDevType(orgId, deviceType, enable, keys);
				for (PrjConfig config : configlist) {
					if(PrjConfig.DEVICE_START_TIME.equals(config.getConfigKey())){
						String val = config.getConfigValue();
						deviceStartTime = (val != null ? val : "");
					} else if(PrjConfig.DEVICE_END_TIME.equals(config.getConfigKey())){
						String val = config.getConfigValue();
						deviceEndTime = (val != null ? val : "");
					} else if(PrjConfig.DEVICE_HOTSPOT.equals(config.getConfigKey())){
						String val = config.getConfigValue();
						deviceHotspot = (val != null ? val : "");
					}
				}
				redirectAction = "showByLarge";
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "show";
	}
	//触摸屏竖屏设置
	public String showByPortrait(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				List<String> keys = new ArrayList<String>();
				keys.add(PrjConfig.DEVICE_START_TIME);
				keys.add(PrjConfig.DEVICE_END_TIME);
				keys.add(PrjConfig.DEVICE_HOTSPOT);
				deviceType = DevTypeEnum.AndroidPortrait.getName();
				String enable = PrjConfig.CONFIG_ENABLE;
				configlist = prjConfigService.getPrjConfigListByDevType(orgId, deviceType, enable, keys);
				for (PrjConfig config : configlist) {
					if(PrjConfig.DEVICE_START_TIME.equals(config.getConfigKey())){
						String val = config.getConfigValue();
						deviceStartTime = (val != null ? val : "");
					} else if(PrjConfig.DEVICE_END_TIME.equals(config.getConfigKey())){
						String val = config.getConfigValue();
						deviceEndTime = (val != null ? val : "");
					} else if(PrjConfig.DEVICE_HOTSPOT.equals(config.getConfigKey())){
						String val = config.getConfigValue();
						deviceHotspot = (val != null ? val : "");
					}
				}
				redirectAction = "showByPortrait";
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "show";
	}
	//按设备id设置
	public String showByDevId(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				List<String> keys = new ArrayList<String>();
				keys.add(PrjConfig.DEVICE_START_TIME);
				keys.add(PrjConfig.DEVICE_END_TIME);
				keys.add(PrjConfig.DEVICE_HOTSPOT);
				String enable = PrjConfig.CONFIG_ENABLE;
				configlist = prjConfigService.getPrjConfigListByDevId(orgId, deviceType, deviceId, enable, keys);
				for (PrjConfig config : configlist) {
					if(PrjConfig.DEVICE_START_TIME.equals(config.getConfigKey())){
						String val = config.getConfigValue();
						deviceStartTime = (val != null ? val : "");
					} else if(PrjConfig.DEVICE_END_TIME.equals(config.getConfigKey())){
						String val = config.getConfigValue();
						deviceEndTime = (val != null ? val : "");
					} else if(PrjConfig.DEVICE_HOTSPOT.equals(config.getConfigKey())){
						String val = config.getConfigValue();
						deviceHotspot = (val != null ? val : "");
					}
				}
				redirectAction = "showByDevId";
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "show";
	}
	
	//保存
	public String saveByDevType() {
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				String enable = PrjConfig.CONFIG_ENABLE;
				List<PrjConfig> DBConfiglist = getDevConfigList(orgId, deviceType, deviceId, enable);
				boolean flag = true;
				if(DBConfiglist != null && DBConfiglist.size() > 0){
					String key = DBConfiglist.get(0).getConfigKey();
					if(DBConfiglist.size() == 1 && PrjConfig.DEVICE_HOTSPOT.equals(key)){
						Date now = new Date();
						PrjConfig config = DBConfiglist.get(0);
						config.setConfigValue(deviceHotspot);
						config.setLastDate(now);
						int count = prjConfigService.update(config);
						boolean updateFlag = (count > 0 ? true : false); 
						List<PrjConfig> updatelist = new ArrayList<PrjConfig>();
						String remark = null;
						PrjConfig saveConfig = null;
						if(deviceStartTime != null && !"".equals(deviceStartTime)){
							String startConfigName = PrjConfig.configNameMap.get(PrjConfig.DEVICE_START_TIME);
							saveConfig = new PrjConfig(orgId, deviceType, deviceId, PrjConfig.DEVICE_START_TIME, deviceStartTime, enable, now, now, startConfigName, remark);
							updatelist.add(saveConfig);
						}
						if(deviceEndTime != null && !"".equals(deviceEndTime)){
							String endConfigName = PrjConfig.configNameMap.get(PrjConfig.DEVICE_END_TIME);
							saveConfig = new PrjConfig(orgId, deviceType, deviceId, PrjConfig.DEVICE_END_TIME, deviceEndTime, enable, now, now, endConfigName, remark);
							updatelist.add(saveConfig);
						}
						boolean saveFlag = prjConfigService.batchSave(updatelist);
						flag = updateFlag && saveFlag;
					} else {
						flag = devConfigBatchUpdate(DBConfiglist);
					}
				} else {
					flag = devConfigBatchSave(orgId, deviceType, deviceId, enable);
				}
				if(!flag){
					log.error("save:[orgId#"+orgId+",deviceType#"+deviceType+",deviceIdF#"+deviceId+",enable#"+enable+"]:[]:更新设备开/关机或WiFi状态失败");
				}else{
					//如果成功修改配置，分发设备配置消息
					syncMessageService.saveConfigMsg(orgId, deviceType, deviceId);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "saveByDevType";
	}
	public String saveByDevId() {
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				String enable = PrjConfig.CONFIG_ENABLE;
				List<PrjConfig> DBConfiglist = getDevConfigList(orgId, deviceType, deviceId, enable);
				boolean flag = true;
				if(DBConfiglist != null && DBConfiglist.size() > 0){
					String key = DBConfiglist.get(0).getConfigKey();
					if(DBConfiglist.size() == 1 && PrjConfig.DEVICE_HOTSPOT.equals(key)){
						Date now = new Date();
						PrjConfig config = DBConfiglist.get(0);
						config.setConfigValue(deviceHotspot);
						config.setLastDate(now);
						int count = prjConfigService.update(config);
						boolean updateFlag = (count > 0 ? true : false); 
						List<PrjConfig> updatelist = new ArrayList<PrjConfig>();
						String remark = null;
						PrjConfig saveConfig = null;
						if(deviceStartTime != null && !"".equals(deviceStartTime)){
							String startConfigName = PrjConfig.configNameMap.get(PrjConfig.DEVICE_START_TIME);
							saveConfig = new PrjConfig(orgId, deviceType, deviceId, PrjConfig.DEVICE_START_TIME, deviceStartTime, enable, now, now, startConfigName, remark);
							updatelist.add(saveConfig);
						}
						if(deviceEndTime != null && !"".equals(deviceEndTime)){
							String endConfigName = PrjConfig.configNameMap.get(PrjConfig.DEVICE_END_TIME);
							saveConfig = new PrjConfig(orgId, deviceType, deviceId, PrjConfig.DEVICE_END_TIME, deviceEndTime, enable, now, now, endConfigName, remark);
							updatelist.add(saveConfig);
						}
						boolean saveFlag = prjConfigService.batchSave(updatelist);
						flag = updateFlag && saveFlag;
					} else {
						flag = devConfigBatchUpdate(DBConfiglist);
					}
				} else {
					flag = devConfigBatchSave(orgId, deviceType, deviceId, enable);
				}
				if(!flag){
					log.error("save:[orgId#"+orgId+",deviceType#"+deviceType+",deviceIdF#"+deviceId+",enable#"+enable+"]:[]:更新设备开/关机或WiFi状态失败");
				}else{
					//如果成功修改配置，分发设备配置消息
					syncMessageService.saveConfigMsg(orgId, deviceType, deviceId);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "saveByDevId";
	}

	//辅助方法
	//获取配置列表
	private List<PrjConfig> getDevConfigList(String orgId, String deviceType, String deviceId, String enable) throws Exception{
		List<PrjConfig> DBConfiglist = null;
		List<String> keys = new ArrayList<String>();
		keys.add(PrjConfig.DEVICE_START_TIME);
		keys.add(PrjConfig.DEVICE_END_TIME);
		keys.add(PrjConfig.DEVICE_HOTSPOT);
		if(deviceId != null && !"".equals(deviceId)){
			DBConfiglist = prjConfigService.getPrjConfigListByDevId(orgId, deviceType, deviceId, enable, keys);
		} else {
			DBConfiglist = prjConfigService.getPrjConfigListByDevType(orgId, deviceType, enable, keys);
		}
		return DBConfiglist;
	}
	//批量更新
	private boolean devConfigBatchUpdate(List<PrjConfig> DBConfiglist) throws Exception{
		Date now = new Date();
		List<PrjConfig> updatelist = new ArrayList<PrjConfig>();
		for (PrjConfig config : DBConfiglist) {
			if(PrjConfig.DEVICE_START_TIME.equals(config.getConfigKey())){
				config.setConfigValue(deviceStartTime);
				config.setLastDate(now);
				updatelist.add(config);
			} else if(PrjConfig.DEVICE_END_TIME.equals(config.getConfigKey())){
				config.setConfigValue(deviceEndTime);
				config.setLastDate(now);
				updatelist.add(config);
			} else if(PrjConfig.DEVICE_HOTSPOT.equals(config.getConfigKey())){
				config.setConfigValue(deviceHotspot);
				config.setLastDate(now);
				updatelist.add(config);
			}
		}
		return prjConfigService.batchUpdate(updatelist);
	}
	//批量更新
	private boolean devConfigBatchSave(String orgId, String deviceType, String deviceId, String enable) throws Exception{
		List<PrjConfig> updatelist = new ArrayList<PrjConfig>();
		Date now = new Date();
		String remark = null;
		PrjConfig config = null;
		if(deviceStartTime != null && !"".equals(deviceStartTime)){
			String startConfigName = PrjConfig.configNameMap.get(PrjConfig.DEVICE_START_TIME);
			config = new PrjConfig(orgId, deviceType, deviceId, PrjConfig.DEVICE_START_TIME, deviceStartTime, enable, now, now, startConfigName, remark);
			updatelist.add(config);
		}
		if(deviceEndTime != null && !"".equals(deviceEndTime)){
			String endConfigName = PrjConfig.configNameMap.get(PrjConfig.DEVICE_END_TIME);
			config = new PrjConfig(orgId, deviceType, deviceId, PrjConfig.DEVICE_END_TIME, deviceEndTime, enable, now, now, endConfigName, remark);
			updatelist.add(config);
		}
		config = null;
		String hotConfigName = PrjConfig.configNameMap.get(PrjConfig.DEVICE_HOTSPOT);
		config = new PrjConfig(orgId, deviceType, deviceId, PrjConfig.DEVICE_HOTSPOT, deviceHotspot, enable, now, now, hotConfigName, remark);
		updatelist.add(config);
		return prjConfigService.batchSave(updatelist);
	}
	
	//getter and setter
	public void setPrjConfigService(PrjConfigService prjConfigService) {
		this.prjConfigService = prjConfigService;
	}
	public PrjConfigService getPrjConfigService() {
		return prjConfigService;
	}
	public void setPrjConfig(PrjConfig prjConfig) {
		this.prjConfig = prjConfig;
	}
	public PrjConfig getPrjConfig() {
		return prjConfig;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public List<PrjConfig> getConfiglist() {
		return configlist;
	}
	public void setConfiglist(List<PrjConfig> configlist) {
		this.configlist = configlist;
	}
	public String getDeviceStartTime() {
		return deviceStartTime;
	}
	public void setDeviceStartTime(String deviceStartTime) {
		this.deviceStartTime = deviceStartTime;
	}
	public String getDeviceEndTime() {
		return deviceEndTime;
	}
	public void setDeviceEndTime(String deviceEndTime) {
		this.deviceEndTime = deviceEndTime;
	}
	public String getDeviceHotspot() {
		return deviceHotspot;
	}
	public void setDeviceHotspot(String deviceHotspot) {
		this.deviceHotspot = deviceHotspot;
	}
	public List<Integer> getConfigIds() {
		return configIds;
	}
	public void setConfigIds(List<Integer> configIds) {
		this.configIds = configIds;
	}
	public String getRedirectAction() {
		return redirectAction;
	}
	public void setRedirectAction(String redirectAction) {
		this.redirectAction = redirectAction;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
}
