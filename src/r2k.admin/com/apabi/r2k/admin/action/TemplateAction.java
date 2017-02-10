package com.apabi.r2k.admin.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.InfoTemplate;
import com.apabi.r2k.admin.model.InfoTemplateSet;
import com.apabi.r2k.admin.service.ReleaseRecordService;
import com.apabi.r2k.admin.service.TemplateService;
import com.apabi.r2k.admin.service.TemplateSetService;
import com.apabi.r2k.admin.utils.ColumnType;
import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.TempTypeEnum;
import com.apabi.r2k.common.utils.ZipUtils;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.utils.SessionUtils;

@Controller("templateAction")
@Scope("prototype")
public class TemplateAction {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Resource
	private TemplateService templateService;
	@Resource
	private TemplateSetService templateSetService;

	@Resource(name="releaseRecordService")
	private ReleaseRecordService releaseRecordService;
	private Page page;
	private InfoTemplate template;
	private InfoTemplateSet templateSet;
	private int id;
	private Map<String, String> filemap;
	private String tempListLenStr;
	private String temptext;
	private List<InfoTemplate> templist;	
	private List<InfoTemplateSet> tempsetlist;	
	private Map<String, DevTypeEnum> deviceTypeMap;
	private Map<String, TempTypeEnum> tempTypeMap;
	private String devType;
	private String deviceId;
	private String deviceName;
	private String setNo;
	private String columnType;
	private int colId;
	private int homeId;
	private String homeName;

	private int status;		//json返回状态：：-1错误，0失败，1成功

	private File zip;
	private String filename; 
	private int flag;
	private String actionName;
	
	public String index() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(request);
			if(orgId != null){
				PageRequest pageRequest = new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest, request);
				Map params = (Map) pageRequest.getFilters();
				params.put("orgId", orgId);
				page = templateSetService.pageQuery(pageRequest, null);
				List list = getDefaultTypeList(page.getResult());
				page.setResult(list);
				deviceTypeMap = DevTypeEnum.getClientTypeMap();		//设备类型列表
				tempTypeMap = TempTypeEnum.getAllTempTypeMap();		//模板类型列表
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "index";
	}

	public String savePage() {
		try {
			deviceTypeMap = DevTypeEnum.getClientTypeMap();		//设备类型列表
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "savePage";
	}

	/**
	 * zip上传方法
	 * @return filemap|key:status|value:1成功,-1不包含默认模板
	 */
	public String uploadZip() {
		try {
			String setNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			filemap = templateService.parseZipData(zip, filename, setNo);
			//复制zip文件到指定文件夹
			if(GlobalConstant.STATUS_SUCCESS.equals(filemap.get("status"))){
				String tempFileName = PropertiesUtil.getRootPath() + GlobalConstant.SLASH + GlobalConstant.PROJECT_FILE_PATH + 
				GlobalConstant.SLASH + GlobalConstant.TEMPLATE_TEMP_PATH + GlobalConstant.SLASH + setNo + ".zip";
				com.apabi.r2k.common.utils.FileUtils.createFile(tempFileName);
				FileUtils.copyFile(zip, new File(tempFileName));
			}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		} catch (Exception e1) {
			log.error(e1.getMessage(),e1);
		}
		return "uploadZip";
	}

	public String save() {
		String setNo = templateSet.getSetNo();
		int scope = templateSet.getScope();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(request);
			if(orgId != null){
				templateSet.setOrgId(orgId);
				templateSetService.batchSave(templateSet, tempListLenStr);
			}
		} catch (Exception e) {
			try {
				//删除机构或公共目录下本次复制的文件
				AuthOrg currentOrg = SessionUtils.getCurrentOrg(ServletActionContext.getRequest());
				String root = PropertiesUtil.getRootPath() + GlobalConstant.SLASH + GlobalConstant.PROJECT_FILE_PATH;
				String tempPath = root + GlobalConstant.TEMPLATE_TEMP_PATH + setNo + ".zip";
				String path = getSetNoPath(currentOrg.getOrgId(), setNo, scope);
				com.apabi.r2k.common.utils.FileUtils.deleteDir(new File(path));
				//删除临时目录下的zip文件
				new File(tempPath).delete();
			} catch (Exception e1) {
				log.error(e1.getMessage(),e1);
			}
			log.error(e.getMessage(),e);
		}
		return "save";
	}

	public String updatePage() {
		BufferedReader br = null;
		try {
			template = templateService.getById(id);
			StringBuffer sf = new StringBuffer("");
			String path = PropertiesUtil.getRootPath()+ GlobalConstant.SLASH + GlobalConstant.PROJECT_FILE_PATH + template.getPath();
			File updatefile = new File(path); 
			if(!updatefile.exists()){
				com.apabi.r2k.common.utils.FileUtils.createFile(path);
			}
			br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(updatefile)	,"utf-8")	);
			String line;
			while ((line = br.readLine()) != null) {
				sf.append(line+ "\r\n");
			}
			temptext = sf.toString();
		} catch (Exception e) {
			try {
				br.close();
			} catch (IOException e1) {
				log.error(e1.getMessage(),e1);
			}
			log.error(e.getMessage(),e);
		} finally{
			try {
				br.close();
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			}
		}
		return "updatePage";
	}

	public String update() {
		File oldfile = null, bakfile = null;
		try {
			int count = templateService.update(template);
			if(count > 0) {
				//原文件备份
				String oldname = PropertiesUtil.getRootPath() + GlobalConstant.SLASH + GlobalConstant.PROJECT_FILE_PATH + template.getPath();
				bakfile = com.apabi.r2k.common.utils.FileUtils.fileBak(new File(oldname), "_bak");
				if(bakfile != null){
					//将页面修改的信息写入文件
					BufferedWriter bufferWritter = new BufferedWriter(
							new OutputStreamWriter(
									new FileOutputStream(oldname), "utf-8"));
					bufferWritter.write(temptext);
					bufferWritter.flush();
					bufferWritter.close();
					//删除备份文件
					bakfile.delete();
				}
			}
		} catch (Exception e) {
			try {
				//异常还原文件
				FileUtils.copyFile(bakfile, oldfile);
				bakfile.delete();
			} catch (IOException e1) {
				log.error(e1.getMessage(),e1);
			}
			log.error(e.getMessage(),e);
		}
		return "update";
	}
	//更新整套信息
	public String updateSet(){
		File dirFileBak = null;
		String bakSurfix = "_bak";
		boolean flag = true;
 		try {
 			if(zip != null){
 				int scope = templateSet.getScope(); 
 				String setNo = templateSet.getSetNo();
 				String orgId = templateSet.getOrgId();
 				String root = PropertiesUtil.getRootPath() + GlobalConstant.SLASH + GlobalConstant.PROJECT_FILE_PATH + GlobalConstant.SLASH;
 				String filedir = root + getSetNoPath(orgId, setNo, scope);
 				//备份原zip文件
 				File oldDirFile = new File(filedir);
 				if(oldDirFile.exists()){
 					dirFileBak = com.apabi.r2k.common.utils.FileUtils.fileBak(oldDirFile, bakSurfix);
 				}
 				//解压zip
 				ZipUtils.unzip(zip.getAbsolutePath(), filedir);
 				//更新数据
 				flag = uploadReplaceZipUpdateData(orgId, setNo, scope);
 				
 				//删除备份
 				if(dirFileBak != null && flag){
 					com.apabi.r2k.common.utils.FileUtils.deleteDir(dirFileBak);
 				}
 			}
 			if(flag){
 				templateSetService.update(templateSet);
 			} else {
 				String oldfilename = dirFileBak.getAbsolutePath();
 				oldfilename = oldfilename.substring(0,oldfilename.lastIndexOf(bakSurfix));
 				//删除上传文件
 				com.apabi.r2k.common.utils.FileUtils.deleteDir(new File(oldfilename));
 				//异常备份文件还原
 				dirFileBak.renameTo(new File(oldfilename));
 			}
		} catch (Exception e) {
			String oldfilename = dirFileBak.getAbsolutePath();
			oldfilename = oldfilename.substring(0,oldfilename.lastIndexOf(bakSurfix));
			//删除上传文件
			com.apabi.r2k.common.utils.FileUtils.deleteDir(new File(oldfilename));
			//异常备份文件还原
			dirFileBak.renameTo(new File(oldfilename));
			log.error(e.getMessage(),e);
		}
		return "updateSet";
	}
	//按模板套号删除模板
	public String delete() {
		File oldFile = null;
		File bakFile = null;
		String bakSurfix = "_bak";
		try {
			int id = templateSet.getId();
			InfoTemplateSet tempset = templateSetService.getTemplateSetById(id);
			String orgId = tempset.getOrgId();
			String setNo = tempset.getSetNo();
			int scope = tempset.getScope();
			List<InfoTemplate> temps = tempset.getTemplates();
			List<Integer> ids = new ArrayList<Integer>();
			for (InfoTemplate t : temps) {
				ids.add(t.getId());
			}
			int listResult = templateService.batchDelete(ids);
			int setResult = templateSetService.deleteById(id);
			if (listResult > 0 && setResult > 0) {
				String relPath = getSetNoPath(orgId, setNo, scope);
				String path = PropertiesUtil.getRootPath() + GlobalConstant.SLASH + GlobalConstant.PROJECT_FILE_PATH + GlobalConstant.SLASH + relPath;
				String bakpath = path+bakSurfix;
				com.apabi.r2k.common.utils.FileUtils.copyDirectiory(path, bakpath);
				oldFile = new File(path);
				bakFile = new File(bakpath);
				com.apabi.r2k.common.utils.FileUtils.deleteDir(oldFile);
				com.apabi.r2k.common.utils.FileUtils.deleteDir(bakFile);
				log.info("deleteBySetNo:[id#" + id + "]:[]:删除模板成功");
			} else {
				log.info("deleteBySetNo:[id#" + id + "]:[]:删除模板失败");
			}
		} catch (Exception e) {
			log.error("deleteBySetNo:[id#" + id + "]:[]:删除模板错误");
			if(bakFile != null){
				String bakFileName = bakFile.getAbsolutePath();
				bakFileName = bakFileName.substring(0,bakFileName.lastIndexOf(bakSurfix));
				if(oldFile != null && oldFile.exists()){
					com.apabi.r2k.common.utils.FileUtils.deleteDir(oldFile);
					bakFile.renameTo(new File(bakFileName));
				}
			}
			log.error(e.getMessage(),e);
		}
		return "deleteBySetNo";
	}

	//通过模板套号查询模板列表
	public String findTempListBySetNo(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(request);
			if(orgId != null){
				templist = templateService.findTemplateListBySetNo(template.getOrgId(), template.getSetNo());
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "templistBySetNo";
	}
	
	//下载整套模板zip文件
	public String downloadZip(){
		OutputStream os = null;
		try {
			this.filename = templateSet.getSetName() + ".zip";
			String setNo = templateSet.getSetNo();
			String tempDir = PropertiesUtil.getRootPath() + GlobalConstant.SLASH + GlobalConstant.PROJECT_FILE_PATH 
				+ GlobalConstant.SLASH + getSetNoPath(templateSet.getOrgId(), setNo, templateSet.getScope()); 
			HttpServletResponse response = ServletActionContext.getResponse();
			// 设置输出的格式
	        response.reset();
	        response.setContentType("bin");
	        response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			os = response.getOutputStream();
			ZipUtils.zipIncludeDir(os, tempDir);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}finally{
			try {
				if(os != null){
					os.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			}
		}
		return null;
	}

	//设备模板列表
	public String deviceTempList(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(request);
			if(orgId != null){
				PageRequest pageRequest = new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest, request);
				Map params = (Map) pageRequest.getFilters();
				params.put("orgId", orgId);
				page = templateService.pageQuery(pageRequest, null);
				deviceTypeMap = DevTypeEnum.getClientTypeMap();		//设备类型列表
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "deviceTempList";
	}
	
	//查找默认设备类型模板列表	return true:不存在默认模板,false:存在默认模板
	public String checkDefaultType(){
		try {
			status = 1;
			HttpServletRequest request = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(request);
			if(orgId != null){
				String defaultType = templateSet.getDefaultType();
				if(defaultType != null && !"".equals(defaultType)){
					int scope = templateSet.getScope();
					boolean flag = templateSetService.checkDefaultType(scope, defaultType, orgId);
					if(!flag){
						status = 0;
					}
				}
			}
		} catch (Exception e) {
			status = -1;
			log.error(e.getMessage(),e);
		}
		return "checkDefaultType";
	}

	//查找机构默认设备类型模板列表	 true:不存在默认模板,false:存在默认模板
	public String checkOrgDefaultType(){
		try {
			status = 1;
			HttpServletRequest request = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(request);
			if(orgId != null){
				String defaultType = templateSet.getDefaultType();
				if(defaultType != null && !"".equals(defaultType)){
					boolean flag = templateSetService.checkDefaultType(templateSet.getScope(), defaultType, orgId);
					if(!flag){
						status = 0;
					}
				}
			}
		} catch (Exception e) {
			status = -1;
			log.error(e.getMessage(),e);
		}
		return "checkOrgDefaultType";
	}
	
	/**
	 * 辅助方法:上传zip,数据更新
	 * @param orgId
	 * @param setNo
	 * @param scope
	 * zipmap=status=1成功、-1没有home页、-2文件名命名错误、-3存在子页缺子页对应的默认模板、-4子页数字匹配错误
	 */
	private boolean uploadReplaceZipUpdateData(String orgId, String setNo, Integer scope) throws Exception{
		boolean flag = true;
		List<InfoTemplate> savelist = new ArrayList<InfoTemplate>();
		List<Integer> deletelist = new ArrayList<Integer>();
		Date now = new Date();
		String filedir = GlobalConstant.SLASH + getSetNoPath(orgId, setNo, scope);
		List<InfoTemplate> templist = templateService.findTemplateListBySetNo(orgId, setNo);
		Map<String, String> zipmap = templateService.parseZipData(zip, filename, setNo);
		//整套上传替换文件,不包含默认模板文件
		if(zipmap.get("status").equals("-1") || zipmap.get("status").equals("-2") || zipmap.get("status").equals("-3") || zipmap.get("status").equals("-4") ){
			return false;
		}
		//key包含扩展名
		for (String key : zipmap.keySet()) {
			boolean saveflag = false;
			//获取模板类型
			String keyType = null;
			if(key.indexOf("setno_") < 0 && key.indexOf("setname_") < 0 && !key.equals("status")){
				keyType = key.substring(0, key.indexOf("."));
			} else {
				continue;
			}
			//判断原数据中是否包含上传的模板类型
			for (InfoTemplate tempInfo : templist) {
				String type = tempInfo.getName();
				if(keyType.equals(type)){
					saveflag = true;
					break;
				} 
			}
			if(!saveflag){
				InfoTemplate tempsave = new InfoTemplate();
				tempsave.setName(keyType);
				int splitIndex = keyType.indexOf("-");
				if(splitIndex >= 0){
					tempsave.setType(keyType.substring(0, splitIndex));
					int setIndex = Integer.parseInt(keyType.substring(splitIndex));
				} else {
					tempsave.setType(keyType);
				}
				String pathsave = filedir + GlobalConstant.SLASH + key;
				tempsave.setPath(pathsave);
				tempsave.setOrgId(orgId);
				tempsave.setSetNo(setNo);
				tempsave.setCrtDate(now);
				tempsave.setLastDate(now);
				savelist.add(tempsave);
			}
		}
		for (InfoTemplate tempNode : templist) {
			String type = tempNode.getName() + ".ftl";
			if(!zipmap.containsKey(type)){
				deletelist.add(tempNode.getId());
			}
		}
		if(savelist.size() > 0){
			templateService.batchSave(savelist);
		}
		if(deletelist.size() > 0){
			templateService.batchDelete(deletelist);
		}
		return flag;
	}
	
	/**
	 * 获取模板文件存储相对路径,路径到套号
	 * @param orgId
	 * @param setNo
	 * @param scope
	 * @return
	 * @throws Exception
	 */
	private String getSetNoPath(String orgId, String setNo, Integer scope) throws Exception{
		String setNoPath = "";
		if(scope == InfoTemplateSet.SCOPE_TYPE_ALL){
			setNoPath = GlobalConstant.TEMPLATE_PUBLIC_PATH + GlobalConstant.SLASH + setNo;
		} else if(scope == InfoTemplateSet.SCOPE_TYPE_ORG){
			setNoPath = GlobalConstant.TEMPLATE_ORG_PATH + GlobalConstant.SLASH + orgId + GlobalConstant.SLASH + setNo;
		}
		return setNoPath;
	}
	
	/**
	 * 机构获取全部整套可用模板
	 */
	public String getOrgAllTemplates(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgid = SessionUtils.getOrgId(req);
			if(orgid != null){
				templist = templateService.findOrgTemplateByDevType(orgid, devType);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "getAllTemplates";
	}
	
	public String selectOrgTemplate(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgid = SessionUtils.getOrgId(req);
			if(StringUtils.isNotBlank(orgid)){
				releaseRecordService.selectOrgTemplate(orgid, devType, setNo, homeName);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setRedirectAction();
		return "org";
	}
	
	public String selectDeviceTemplate(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgid = SessionUtils.getOrgId(req);
			if(StringUtils.isNotBlank(orgid)){
				releaseRecordService.selectDeviceTemplate(orgid, deviceId, setNo, homeName);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "device";
	}
	
	public String getOrgColTemplate(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgid = SessionUtils.getOrgId(req);
			if(StringUtils.isNotBlank(orgid)){
				String tempType = ColumnType.getTemplateType(columnType);
				templist = templateService.getColAllTemplates(setNo, tempType);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "getAllTemplates";
	}
	
	public String selectOrgColTemplate(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgid = SessionUtils.getOrgId(req);
			if(StringUtils.isNotBlank(orgid)){
				releaseRecordService.selectOrgColTemplate(orgid, devType, setNo, id, colId);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setRedirectAction();
		return "org";
	}
	public String selectDevColTemplate(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgid = SessionUtils.getOrgId(req);
			if(StringUtils.isNotBlank(orgid)){
				releaseRecordService.selectDevColTemplate(orgid, deviceId, setNo, id, colId);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setRedirectAction();
		return "device";
	}
	
	public String getHomeTemplates(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgid = SessionUtils.getOrgId(req);
			if(StringUtils.isNotBlank(orgid)){
				templist = templateService.getColAllTemplates(setNo, TempTypeEnum.HOME.getName());
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "getAllTemplates";
	}
	
	private void setRedirectAction(){
		if(StringUtils.isNotBlank(deviceId)){
			actionName = "device";
		} else if(devType.equals(DevTypeEnum.ORG.getName())){
			actionName = "org";
		} else if(devType.equals(DevTypeEnum.AndroidLarge.getName())){
			actionName = "showLarge";
		} else if(devType.equals(DevTypeEnum.AndroidPortrait.getName())){
			actionName = "showPortrait";
		} else if(devType.equals(DevTypeEnum.AndroidPad.getName())){
			actionName = "showAndroidPad";
		} else if(devType.equals(DevTypeEnum.AndroidPhone.getName())){
			actionName = "showAndroidPhone";
		} else if(devType.equals(DevTypeEnum.iPad.getName())){
			actionName = "showIPad";
		} else if(devType.equals(DevTypeEnum.iPhone.getName())){
			actionName = "showIPhone";
		}
	}
	
	//模板默认类型全局机构唯一显示(机构优先于全局)
	private List<InfoTemplateSet> getDefaultTypeList(List<InfoTemplateSet> list){
		Map<String, String> defaultTypeMap = new HashMap<String, String>();
		Map<String,DevTypeEnum> devTypeMap = DevTypeEnum.getClientTypeMap();		//模板类型列表
		for (String key : devTypeMap.keySet()) {
			defaultTypeMap.put(key, "");
		}
		if(list != null && list.size() > 0){
			for (int i = 0, len = list.size(); i < len; i++) {
				InfoTemplateSet infoTemplateSet = list.get(i);
				String defaultType = infoTemplateSet.getDefaultType();
				if(defaultType != null && !"".equals(defaultType)){
					String[] types = defaultType.split("#");
					int scope = infoTemplateSet.getScope();
					//机构
					if(scope == 1){
						if(types.length > 1){
							for (String str : types) {
								list = getOrgDefaultType(str, i, defaultTypeMap, list);
							}
						} else {
							list = getOrgDefaultType(types[0], i, defaultTypeMap, list);
						}
					}//全局 
					else if(scope == 0){
						if(types.length > 1){
							for (String str : types) {
								list = getAllDefaultType(str, i, defaultTypeMap, list);
							}
						} else {
							list = getAllDefaultType(types[0], i, defaultTypeMap, list);
						}
					}
				}
			}
		}
		return list;
	}
	//机构默认类型模板处理
	private List<InfoTemplateSet> getOrgDefaultType(String key, int i, Map<String, String> defaultTypeMap, List<InfoTemplateSet> list){
		if(defaultTypeMap.containsKey(key)){
			String val = defaultTypeMap.get(key);
			if(!"".equals(val)){//选择性替换默认类型
				int index = Integer.parseInt(val);
				String oldDefType = list.get(index).getDefaultType();
				oldDefType = oldDefType.replace(key, "");
				list.get(index).setDefaultType(oldDefType);
			}
			defaultTypeMap.put(key, "" + i);
		}
		return list;
	}
	
	//全局默认类型模板处理
	private List<InfoTemplateSet> getAllDefaultType(String key, int index, Map<String, String> defaultTypeMap, List<InfoTemplateSet> list){
		if(defaultTypeMap.containsKey(key)){
			String val = defaultTypeMap.get(key);
			if(!"".equals(val)){
				String oldDefType = list.get(index).getDefaultType();
				oldDefType = oldDefType.replace(key, "");
				list.get(index).setDefaultType(oldDefType);
			} else {
				defaultTypeMap.put(key, "" + index);
			}
		}
		return list;
	}
	
	// getter and setter
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public void setTemplate(InfoTemplate template) {
		this.template = template;
	}
	public InfoTemplate getTemplate() {
		return template;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public File getZip() {
		return zip;
	}
	public void setZip(File zip) {
		this.zip = zip;
	}
	public Map<String, String> getFilemap() {
		return filemap;
	}
	public void setFilemap(Map<String, String> filemap) {
		this.filemap = filemap;
	}
	public String getTempListLenStr() {
		return tempListLenStr;
	}
	public void setTempListLenStr(String tempListLenStr) {
		this.tempListLenStr = tempListLenStr;
	}
	public String getTemptext() {
		return temptext;
	}
	public void setTemptext(String temptext) {
		this.temptext = temptext;
	}
	public List<InfoTemplate> getTemplist() {
		return templist;
	}
	public void setTemplist(List<InfoTemplate> templist) {
		this.templist = templist;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Map<String, DevTypeEnum> getDeviceTypeMap() {
		return deviceTypeMap;
	}
	public String getDevType() {
		return devType;
	}
	public void setDevType(String devType) {
		this.devType = devType;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getSetNo() {
		return setNo;
	}
	public void setSetNo(String setNo) {
		this.setNo = setNo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public int getColId() {
		return colId;
	}
	public void setColId(int colId) {
		this.colId = colId;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public Map<String, TempTypeEnum> getTempTypeMap() {
		return tempTypeMap;
	}
	public int getHomeId() {
		return homeId;
	}
	public void setHomeId(int homeId) {
		this.homeId = homeId;
	}
	public String getHomeName() {
		return homeName;
	}
	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}
	public InfoTemplateSet getTemplateSet() {
		return templateSet;
	}
	public void setTemplateSet(InfoTemplateSet templateSet) {
		this.templateSet = templateSet;
	}
	public List<InfoTemplateSet> getTempsetlist() {
		return tempsetlist;
	}
	public void setTempsetlist(List<InfoTemplateSet> tempsetlist) {
		this.tempsetlist = tempsetlist;
	}
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
}
