package com.apabi.r2k.admin.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.ClientVersionDao;
import com.apabi.r2k.admin.model.ClientVersion;
import com.apabi.r2k.admin.model.Device;
import com.apabi.r2k.admin.service.ClientVersionService;
import com.apabi.r2k.admin.service.DeviceService;
import com.apabi.r2k.admin.service.OrgClientVersionService;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.PropertiesUtil;

@Service("clientVersionService")
public class ClientVersionServiceImpl implements ClientVersionService {

	private Logger log = LoggerFactory.getLogger(ClientVersionServiceImpl.class);
	@Resource
	private ClientVersionDao clientVersionDao;
	@Resource
	private OrgClientVersionService orgClientVersionService;
	@Resource
	private DeviceService deviceService;
	
	//入库
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(ClientVersion clientVersion) throws Exception {
		clientVersionDao.saveVersion(clientVersion);
	}
	
	//更新
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(ClientVersion clientVersion) throws Exception {
		clientVersionDao.updateVersion(clientVersion);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String devType) throws Exception {
		//是否要求设备类型
//		if(devType > 0){
//			Map params = (Map) pageRequest.getFilters();
//			params.put("devType", devType);
//		}
		if(StringUtils.isNotBlank(devType)){
			String[] devTypeList = devType.split("#");
			Map params = (Map) pageRequest.getFilters();
			params.put("deviceTypeList", devTypeList);
		}
		Map<String, String> deviceTypeMap = DevTypeEnum.getVersionTypeMap();
		Page page = clientVersionDao.pageQuery(pageRequest);
		List<ClientVersion> list = page.getResult();
		for(ClientVersion clientVersion : list){
			String type = clientVersion.getDevType();
//			String devName = DevTypeEnum.findName(type).getValue();
			String[] devTypes = type.split("#");
			if(devTypes != null){
				if(devTypes.length == 2){
					if(type.equals("Android-Large#Android-Portrait") || type.equals("Android-Portrait#Android-Large")){
						clientVersion.setDevName(deviceTypeMap.get("Android-Large#Android-Portrait"));
					} else {
						StringBuffer buf = new StringBuffer("");
						for (String str : devTypes) {
							buf.append(deviceTypeMap.get(str) + " ");
						}
						clientVersion.setDevName(buf.toString());
					}
				} else {
					StringBuffer buf = new StringBuffer("");
					for (String str : devTypes) {
						buf.append(deviceTypeMap.get(str) + " ");
					}
					clientVersion.setDevName(buf.toString());
				}
			}
			clientVersion.setQrcode(GlobalConstant.SERVERURL + "/" + GlobalConstant.PROJECT_FILE_PATH + "/" + clientVersion.getQrcode());
		}
		return page;
	}

	public ClientVersion getById(int id) throws Exception {
		return clientVersionDao.getVersionById(id);
	}

	@Override
	public ClientVersion getClientVersion(String version, String devType) throws Exception {
		return clientVersionDao.getClientVersion(version, devType);
	}
	
	public ClientVersion getLatestVersion(String devType) throws Exception{
		ClientVersion clientVersion = clientVersionDao.getLatestVersion(devType);
		if(clientVersion != null){
			//拼接完整访问路径
			String url = PropertiesUtil.getValue("server.url") + "/"+PropertiesUtil.getValue("base.r2kfile");
			clientVersion.setPath(url + "/" + clientVersion.getPath());
			clientVersion.setQrcode(url + "/" + clientVersion.getQrcode());
		}
		return clientVersion;
	}
	
	//返回所有类型最新版本信息
	public List<ClientVersion> getAllTypeLatestVersion() throws Exception{
		List<ClientVersion> needVersions = clientVersionDao.getAllTypeLatestVersion();
//		List<ClientVersion> clients = clientVersionDao.getAllTypeLatestVersion();
//		Map<String, String> devTypes = DevTypeEnum.getVersionTypeMap();
//		Map<String, List<ClientVersion>> typeVersions = new HashMap<String, List<ClientVersion>>();
//		for (String type : devTypes.keySet()) {
//			typeVersions.put(type, new ArrayList<ClientVersion>());
//		}
//		for (ClientVersion ver : clients) {
//			typeVersions.get(ver.getDevType()).add(ver);
//		}
//		for (String key : typeVersions.keySet()) {
//			Collections.sort(typeVersions.get(key));
//		}
//		//各种设备类型的最后版本
//		List<ClientVersion> needVersions = new ArrayList<ClientVersion>();
//		for (String key : typeVersions.keySet()) {
//			List<ClientVersion> templist = typeVersions.get(key);
//			int size = templist.size();
//			if(size > 0){
//				needVersions.add(templist.get(size - 1));
//			}
//		}
		//拼装访问路径
		for (ClientVersion ver : needVersions) {
			if(ver != null){
				//拼接完整访问路径
				String url = PropertiesUtil.getValue("server.url") + "/"+PropertiesUtil.getValue("base.r2kfile");
				ver.setPath(url + "/" + ver.getPath());
				ver.setQrcode(url + "/" + ver.getQrcode());
			}
		}
		return needVersions;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteById(int id) throws Exception{
		ClientVersion clientVersion = getById(id);
		if(clientVersion != null){
			clientVersionDao.deleteById(id);
			String version = clientVersion.getVersion();
			String devType = clientVersion.getDevType();
			//相关机构版本信息删除，即初始化为空
			orgClientVersionService.deleteByVersion(version, devType);
			//删除相应设备的升级信息
			List<Device> list = deviceService.getByToVersion(version, devType);
			for(Device device : list){
				device.setToVersion(null);
				deviceService.update(device);
			}
			//最后删除版本文件
//			File file = new File(PropertiesUtil.getRootPath() +"/"+ PropertiesUtil.getValue("base.r2kfile")+"/" + clientVersion.getPath());
//			FileUtils.deleteQuietly(file);
			int index = clientVersion.getPath().lastIndexOf("/");
			String fileDir = clientVersion.getPath().substring(0, index);
			String path = PropertiesUtil.getRootPath() +"/"+ PropertiesUtil.getValue("base.r2kfile")+"/"+fileDir;
			com.apabi.r2k.common.utils.FileUtils.deleteDir(new File(path));
		}
	}
}
