package com.apabi.r2k.admin.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.Device;
import com.apabi.r2k.admin.service.DeviceService;
import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.common.utils.CookieUtil;
import com.apabi.r2k.common.utils.DateUtil;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.FileUtils;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.HttpParamUtils;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.ResponseUtils;
import com.apabi.r2k.paper.service.SyncMessageService;
import com.apabi.r2k.security.service.AuthOrgService;
import com.apabi.r2k.security.utils.SecurityUtil;
import com.apabi.r2k.security.utils.SessionUtils;

@Controller("deviceAction")
@Scope("prototype")
public class DeviceAction implements ServletRequestAware,ServletResponseAware{

	private Logger log = LoggerFactory.getLogger(DeviceAction.class);
	@Resource
	private DeviceService deviceService;
	@Resource
	private AuthOrgService authOrgService;
	@Resource(name="syncMessageService")
	private SyncMessageService syncMessageService;
	
	private Device device;
	private List<Device> devlist;
	private Page page;
	private int id;							//id
	private String deviceId;				// 设备id
	private String deviceName;				// 设备名称
	private String queryIdOrName;			// 查询字符串
	
	private File devfile;					//上传文件
	private String devfileFileName;			//上传文件名
	private InputStream inputStream;		//数据流
	private String status;					//返回状态：1成功，0失败，-1错误
	private String msg;						//返回信息
	private String orgId;
	private int templateId;
	private Map<String, DevTypeEnum> deviceTypeMap;
	private int isDeleteAll;			//1 删除全部,0只删除设备信息

	private HttpServletRequest request;
	private HttpServletResponse response;
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String show(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				Map param = (Map) pageRequest.getFilters();
				param.put("orgId", orgId);
				//只展示触摸屏数据
				List<String> deviceTypeList = new ArrayList<String>();
				deviceTypeList.add(DevTypeEnum.AndroidLarge.getName());
				deviceTypeList.add(DevTypeEnum.AndroidPortrait.getName());
				deviceTypeList.add(DevTypeEnum.Slave.getName());
				param.put("deviceTypeList", deviceTypeList);

				page = this.deviceService.pageQuery(pageRequest, null);
				deviceTypeMap = DevTypeEnum.getAllTypeMap();		//设备类型列表
			}else{
				return "error";
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return "error";
		}
		return "list";
	}
	//删除设备
	public String delete(){
		String result = null;
		try{
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				//isDeleteAll等于1时删除全部
				boolean bAll = (isDeleteAll==1 ? true : false); 
				boolean flag = this.deviceService.delete(id, orgId, bAll);
				result = flag ? "update" : "error";
			}else{
				result = "error";
			}
		}catch(Exception e){
			log.error("delete:[]:[result#" + result + "]:删除设备错误");
			log.error(e.getMessage(),e);
			result = "error";
		}
		return result;
	}
    
    public void register(){
    	String xml = "";
    	try {
    		String name = HttpParamUtils.getName(request);
    		String deviceId = HttpParamUtils.getDeviceId(request);
    		String orgId = HttpParamUtils.getOrgid(request);
    		//获取版本号
    		String version = HttpParamUtils.getSoftVersion(request);

    		if(StringUtils.isBlank(deviceId)){
    			xml = "<Error Code=\"-1004\" Message=\"设备id为空\"/> ";
    			ResponseUtils.responseOutXml(response,xml);
    			return;
    		}
    		if(StringUtils.isBlank(name)){
    			xml = "<Error Code=\"-1004\" Message=\"设备名称为空\"/> ";
    			ResponseUtils.responseOutXml(response,xml);
    			return;
    		}
    		if(StringUtils.isNotBlank(orgId)){
    			//机构ID都为小写
        		orgId = orgId.toLowerCase();
    			/*xml = "<Error Code=\"-1004\" Message=\"机构id为空\"/> ";
    			ResponseUtils.responseOutXml(response,xml);
    			return;*/
    		}
    		
    		String userAgent = HttpParamUtils.getUserAgent(request);
    		log.info("设备注册,设备ID：" + deviceId + "#设备类型：" + userAgent + "#设备名称：" + name + "#设备版本：" + version);
    		
    		if(userAgent.equalsIgnoreCase(GlobalConstant.USER_AGENT_SLAVE)){
    			//子节点注册
    			xml = deviceService.registerSlave(orgId, deviceId, name);
    			SessionUtils.setCurrentUser(request, SecurityUtil.getSecurityUser());
    		}else{
    			//触摸屏等设备注册
    			if(StringUtils.isNotBlank(orgId)){
        			if(authOrgService.checkOrgId(orgId).equals("1")){
        				xml = "<Error Code=\"-1004\" Message=\"注册的机构不存在\"/> ";
        			}else{
        				xml = deviceService.register(name,orgId,deviceId,userAgent,version);
        				SessionUtils.setCurrentUser(request, SecurityUtil.getSecurityUser());
        			}
        		}else{
        			orgId = PropertiesUtil.getValue("org.id");
        			xml = deviceService.register(name,orgId,deviceId,userAgent,version);
        			SessionUtils.setCurrentUser(request, SecurityUtil.getSecurityUser());
        		}
    		}
		} catch (Exception e) {
			xml = "<Error Code=\"-1004\" Message=\"操作失败\"/> ";
			log.error(e.getMessage(),e);
		}
		try {
			CookieUtil.saveCookie("JSESSIONID",request.getSession().getId(),(HttpServletResponse) response);
			ResponseUtils.responseOutXml(response,xml);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}

    }
    //将流转化为字符串
    private String inputStream2String(InputStream is) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null){
          buffer.append(line);
        }
        return buffer.toString();
    }
    
    public String heartbeat(){
    	try {
    		SAXReader reader = new SAXReader();
    		InputStream in = request.getInputStream();
    		String str = inputStream2String(in);
    		Document doc = reader.read(new ByteArrayInputStream(str.getBytes()));
			String orgId = doc.selectSingleNode("//R2k/Orgid").getText();
			String deviceId = doc.selectSingleNode("//R2k/Deviceid").getText();
			String cpu = doc.selectSingleNode("//R2k/Cpu").getText();
			String memory = doc.selectSingleNode("//R2k/Memory").getText();
			String ipv4 = doc.selectSingleNode("//R2k/Ipv4").getText();
			log.info("接收心跳请求，orgId:" + orgId + "设备Id：" + deviceId);
			String userAgent = HttpParamUtils.getUserAgent(request);
			//更新最后活跃时间
			Device device = deviceService.getDeviceByDeviceId(deviceId, orgId);
			if(device != null){
				Date now = new Date();
				device.setLastDate(now);
				device.setLastHeartTime(now);
				device.setIpv4(ipv4);
				deviceService.update(device);
				//记录日志
				String rootPath = PropertiesUtil.getRootPath();
				String filename = rootPath + "/r2kFile/heartbeatLog/" + orgId + "/" + deviceId.replace(":", "") + ".txt";
				File file = new File(filename);
				if(!file.exists()){
					FileUtils.createFile(filename);
				}
				FileWriter writer = new FileWriter(file,true);
				StringBuilder builder = new StringBuilder();
				builder.append(DateUtil.currentDate("yyyy-MM-dd HH:mm:ss")).append(" 接收心跳请求,").append("CPU使用率为").append(cpu)
				.append(",内存使用率为").append(memory).append("\r\n");
				writer.write(builder.toString());
				writer.close();
				String xml = deviceService.getHeartbeatXml(orgId, device.getDeviceType(), deviceId);
				ResponseUtils.responseOutXml(response, xml);
			}
		} catch (Exception e) {
			log.error("接收心跳失败", e);
			log.error(e.getMessage(),e);
		}
    	
    	return null;
    }

    
    public String slave(){
    	//展示子节点信息
    	try {
    		HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				page = deviceService.pageSlave(pageRequest,orgId);
			}else{
				return "error";
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return "error";
		}
    	return "slave";
    }
	
    //更新设备名称
    public String updateDeviceName(){
    	try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				Device checkDevice = deviceService.checkDeviceName(deviceName, orgId);
				if(checkDevice != null){
					this.status = GlobalConstant.STATUS_FAILURE;
				}else {
					checkDevice = deviceService.getDeviceById(id, orgId);
					checkDevice.setDeviceName(deviceName);
					checkDevice.setLastDate(new Date());
					deviceService.update(checkDevice);
					this.status = GlobalConstant.STATUS_SUCCESS;
					syncMessageService.saveConfigMsg(orgId, checkDevice.getDeviceType(), checkDevice.getDeviceId());
				}
			}
		} catch (Exception e) {
			this.status = GlobalConstant.STATUS_ERROR;
			log.error(e.getMessage(),e);
		}
    	return "updateDeviceName";
    }
    
	// getters and setters
	
	public DeviceService getDeviceService() {
		return deviceService;
	}
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	
	public List<Device> getDevlist() {
		return devlist;
	}
	public void setDevlist(List<Device> devlist) {
		this.devlist = devlist;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public File getDevfile() {
		return devfile;
	}
	public void setDevfile(File devfile) {
		this.devfile = devfile;
	}
	public String getDevfileFileName() {
		return devfileFileName;
	}
	public void setDevfileFileName(String devfileFileName) {
		this.devfileFileName = devfileFileName;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuery() {
		return queryIdOrName;
	}
	public void setQuery(String query) {
		this.queryIdOrName = query;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	public Map<String, DevTypeEnum> getDeviceTypeMap() {
		return deviceTypeMap;
	}
	public void setDeviceTypeMap(Map<String, DevTypeEnum> deviceTypeMap) {
		this.deviceTypeMap = deviceTypeMap;
	}
	public int getIsDeleteAll() {
		return isDeleteAll;
	}
	public void setIsDeleteAll(int isDeleteAll) {
		this.isDeleteAll = isDeleteAll;
	}
}
