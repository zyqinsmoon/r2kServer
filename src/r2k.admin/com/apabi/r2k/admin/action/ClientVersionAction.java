package com.apabi.r2k.admin.action;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.ClientVersion;
import com.apabi.r2k.admin.model.Device;
import com.apabi.r2k.admin.model.OrgClientVersion;
import com.apabi.r2k.admin.service.ClientVersionService;
import com.apabi.r2k.admin.service.DeviceService;
import com.apabi.r2k.admin.service.HomePageService;
import com.apabi.r2k.admin.service.OrgClientVersionService;
import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.QRCodeUtil;

@Controller("clientVersionAction")
@Scope("prototype")
public class ClientVersionAction{
	
	private Logger log = LoggerFactory.getLogger(ClientVersionAction.class);
	
	@Resource(name="clientVersionService")
	private ClientVersionService clientVersionService;
	@Resource
	private OrgClientVersionService orgClientVersionService;
	@Resource
	private DeviceService deviceService;
	@Resource
	private HomePageService homePageService;

	private Page page;
	private String devType;
	private File client;
	private String clientFileName;
	private String description;
	private String version;
	private int id;
	private ClientVersion clientVersion;
	private int[] checked;	//调用列表
	private String orgId;
	private Map<String,String> select; //下拉菜单列表
	private int flag;				//json返回状态：0重复，1不重复
	private String publish;	//是否需要发布
	private Map<String, DevTypeEnum> deviceTypeMap;
	
	public String index() {
		try {
			PageRequest pageRequest = new PageRequest();
			HttpServletRequest request = ServletActionContext.getRequest();
			PageRequestFactory.bindPageRequest(pageRequest, request);
			page = clientVersionService.pageQuery(pageRequest, devType);
			//设置下拉菜单
			select = DevTypeEnum.getVersionTypeMap();
//			setSelect(DevTypeEnum.toMap());
//			deviceTypeMap = DevTypeEnum.getAllTypeMap();		//设备类型列表
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "index";
	}
	
	public String checkVersion(){
		try {
			ClientVersion clientVersion = clientVersionService.getClientVersion(version, devType);
			if(clientVersion == null){
				flag = 1;
			}else{
				flag = 0;
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
		return "version";
	}
	
	public String savePage(){
		select = DevTypeEnum.getVersionTypeMap();
//		setSelect(DevTypeEnum.toMap());
//		deviceTypeMap = DevTypeEnum.getAllTypeMap();		//设备类型列表
		return "save";
	}
	
	public String save(){
		ClientVersion clientVersion = new ClientVersion();
		clientVersion.setVersion(version);
		clientVersion.setDescription(description);
		clientVersion.setDevType(devType);
		clientVersion.setCrtDate(new Date());
		clientVersion.setVersionCodeByVerion(version);
	
		try {
			String root = PropertiesUtil.getRootPath() + "/" + PropertiesUtil.get("base.r2kfile") + "/";
			String relativePath = PropertiesUtil.get("path.soft")+ "/" + PropertiesUtil.get("path.version");
			if(DevTypeEnum.iPad.getName().equals(devType)){
				relativePath += "/" + PropertiesUtil.get("path.iPad") ;
			} else if(DevTypeEnum.iPhone.getName().equals(devType)){
				relativePath += "/" + PropertiesUtil.get("path.iPhone");
			} else {
				relativePath += "/" + PropertiesUtil.get("path.android");
			}
			String versionPath = relativePath + "/"+ version + "/" + version + clientFileName.substring(clientFileName.lastIndexOf("."));
			File destFile = new File(root + versionPath);
			destFile.getParentFile().mkdirs();
			FileUtils.copyFile(client, destFile);
			clientVersion.setPath(versionPath);
			//生成二维码
			String qrcode = relativePath+ "/" + version + "/" + version + ".jpg";
			String url = PropertiesUtil.getValue("server.url") + relativePath+ "/";
			QRCodeUtil.encode(url, new File(root + qrcode));
			clientVersion.setQrcode(qrcode);
			clientVersionService.save(clientVersion);
			log.info("版本上传成功：版本号为" + version + "#设备类型为" + devType + "#二维码路径为" + qrcode);
			
			//发布
			if(StringUtils.isNotBlank(publish)){
				homePageService.publish();
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "indexPage";
	}
	
	public String updatePage(){
		try {
			setClientVersion(clientVersionService.getById(id));
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "update";
	}
	
	public String update(){
		try {
			ClientVersion clientVersion = clientVersionService.getById(id);
			//更新文件
			if(client != null){
				File destFile = new File(PropertiesUtil.getRootPath() + "/"+PropertiesUtil.getValue("base.r2kfile")+ "/" + clientVersion.getPath());
				FileUtils.copyFile(client, destFile);
			}
			
			clientVersion.setDescription(description);
			clientVersionService.update(clientVersion);
			log.info("版本更新成功：版本号为" + clientVersion.getVersion() + "#设备类型为" + clientVersion.getDevType());
			
			//发布
			if(StringUtils.isNotBlank(publish)){
				homePageService.publish();
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "indexPage";
	}
	
	public String risePage(){
		try {
			PageRequest pageRequest = new PageRequest();
			HttpServletRequest request = ServletActionContext.getRequest();
			PageRequestFactory.bindPageRequest(pageRequest, request);
			page = orgClientVersionService.downVersionPage(pageRequest, version, devType);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
		return "rise";
	}
	
	public String updateVersion(){
		try {
			for(int id : checked){
				OrgClientVersion orgClientVersion = orgClientVersionService.getVersionById(id);
				orgClientVersion.setVersion(version);
				orgClientVersion.setVersionCodeByVerion(version);
				orgClientVersion.setLastDate(new Date());
				orgClientVersionService.update(orgClientVersion);
				//更新每一个版本的信息
				String orgId = orgClientVersion.getOrgId();
				List<Device> list = deviceService.getDeviceListByOrgId(orgId);
				for(Device device : list){
					device.setToVersion(version);
					deviceService.update(device);
				}
			}
			log.info("版本升级成功：版本号为" + version);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "indexPage";
	}
	
	public String rollbackPage(){
		try {
			PageRequest pageRequest = new PageRequest();
			HttpServletRequest request = ServletActionContext.getRequest();
			PageRequestFactory.bindPageRequest(pageRequest, request);
			page = orgClientVersionService.upVersionPage(pageRequest, version, devType);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "rollback";
	}
	
	public String device(){
		try {
			PageRequest pageRequest = new PageRequest();
			HttpServletRequest request = ServletActionContext.getRequest();
			PageRequestFactory.bindPageRequest(pageRequest, request);
			Map param = (Map) pageRequest.getFilters();
			param.put("orgId", orgId);
			page = deviceService.pageQuery(pageRequest, null);
			deviceTypeMap = DevTypeEnum.getAllTypeMap();		//设备类型列表
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "device";
	}
	
	public String delete(){
		try {
			if(id > 0){
				clientVersionService.deleteById(id);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public File getClient() {
		return client;
	}

	public void setClient(File client) {
		this.client = client;
	}

	public String getClientFileName() {
		return clientFileName;
	}

	public void setClientFileName(String clientFileName) {
		this.clientFileName = clientFileName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setClientVersion(ClientVersion clientVersion) {
		this.clientVersion = clientVersion;
	}

	public ClientVersion getClientVersion() {
		return clientVersion;
	}

	public void setChecked(int[] checked) {
		this.checked = checked;
	}

	public int[] getChecked() {
		return checked;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgId() {
		return orgId;
	}
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public String getPublish() {
		return publish;
	}
	public Map<String, DevTypeEnum> getDeviceTypeMap() {
		return deviceTypeMap;
	}
	public void setDeviceTypeMap(Map<String, DevTypeEnum> deviceTypeMap) {
		this.deviceTypeMap = deviceTypeMap;
	}
	public String getDevType() {
		return devType;
	}
	public void setDevType(String devType) {
		this.devType = devType;
	}

	public Map<String, String> getSelect() {
		return select;
	}

	public void setSelect(Map<String, String> select) {
		this.select = select;
	}
	
	
}
