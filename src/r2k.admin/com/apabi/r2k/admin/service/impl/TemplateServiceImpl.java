package com.apabi.r2k.admin.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.tools.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.TemplateDao;
import com.apabi.r2k.admin.model.InfoTemplate;
import com.apabi.r2k.admin.service.TemplateService;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.TempTypeEnum;
import com.apabi.r2k.common.utils.ZipUtils;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.utils.SessionUtils;

@Service("templateService")
public class TemplateServiceImpl implements TemplateService {

	private Logger log = LoggerFactory.getLogger(TemplateServiceImpl.class);
	
	public static final String PAGE_QUERY_STATEMENT = "pageSelect";
	
	/** 模板套id前缀：setno_ */
	public static final String RESULT_TYPE_SETNO = "setno_";
	/** 模板套名前缀 ：setname_*/
	public static final String RESULT_TYPE_SETNAME = "setname_";

	@Resource(name="templateDao")
	private TemplateDao templateDao;
	
	public void setTemplateDao(TemplateDao templateDao){
		this.templateDao = templateDao;
	}
	
	public TemplateDao getTemplateDao(){
		return templateDao;
	}
	
	//入库
	@Transactional(propagation = Propagation.REQUIRED)
	public int save(InfoTemplate template) throws Exception {
		return templateDao.saveTemplate(template);
	}
	
	//批量保存
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean batchSave(List<InfoTemplate> templist) throws Exception {
		boolean flag = true;
		List<InfoTemplate> failList = templateDao.batchSaveTemplate(templist);
		if (failList != null && failList.size() > 0) {
			flag = false;
		}
		return flag;
	}
	
	//更新
	@Transactional(propagation = Propagation.REQUIRED)
	public int update(InfoTemplate template) throws Exception {
		template.setLastDate(new Date());
		return templateDao.updateTemplate(template);
	}
	//批量更新
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean batchUpdate(List<InfoTemplate> templist) throws Exception {
		boolean flag = true;
		List<InfoTemplate> failList = templateDao.batchUpdateTemplate(templist);
		if (failList != null && failList.size() > 0) {
			flag = false;
		}
		return flag;
	}
	
	//根据ID删除
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteById(java.lang.Integer id) throws Exception {
		return templateDao.deleteById(id);
	}
	//按模板套号删除模板
	public int deleteBySetNo(String orgId, String setNo) throws Exception {
		return templateDao.deleteBySetNo(orgId, setNo);
	}
	//批量删除
	@Transactional(propagation = Propagation.REQUIRED)
	public int batchDelete(List<java.lang.Integer> ids) throws Exception {
		return templateDao.batchDeleteTemplate(ids);
	}
	
	//根据ID查询
	public InfoTemplate getById(java.lang.Integer id) throws Exception {
		return templateDao.getTemplateById(id);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery) throws Exception {
		return templateDao.pageQuery(PAGE_QUERY_STATEMENT, pageRequest, countQuery);
	} 
	
	//根据模板套号返回模板列表
	public List<InfoTemplate> findTemplateListBySetNo(String orgId, String setNo) throws Exception {
		return templateDao.findTemplateListBySetNo(orgId, setNo);
	}

	//根据机构id和模板套名解压模板zip文件
//	@Transactional(propagation = Propagation.REQUIRED)
//	public boolean createOrgTemplate(String orgId, String setNo, String devTpye, String filename) throws Exception {
//		boolean result = true;
//		List<InfoTemplate> list = templateDao.findTemplateListBySetNo(orgId, setNo);
//		if(list != null && list.size() > 0){
//			result = false;
//		} else {
//			//解压zip文件
//			String root = PropertiesUtil.getRootPath() + GlobalConstant.SLASH + GlobalConstant.PROJECT_FILE_PATH + GlobalConstant.SLASH;
//			String pubpath = root + GlobalConstant.TEMPLATE_PUBLIC_PATH + GlobalConstant.SLASH + setNo + ".zip";
//			String orgpath = root + GlobalConstant.TEMPLATE_ORG_PATH + GlobalConstant.SLASH + orgId + GlobalConstant.SLASH + setNo;
//			ZipUtils.unzip(pubpath, orgpath);
//			
//			//解析zip文件中的xml文件
//			Map<String, String> zipmap = new HashMap<String, String>();
//			zipmap = parseZipData(new File(pubpath), filename, setNo);
//			
//			//保存
//			List<InfoTemplate> templist = new ArrayList<InfoTemplate>();
//			if(zipmap != null){
//				for (String key : zipmap.keySet()) {
//					InfoTemplate template = new InfoTemplate();
//					Date now = new Date();
//					template.setCrtDate(now);
//					template.setLastDate(now);
//					template.setType(key.substring(0,key.lastIndexOf(".")));
//					template.setDeviceType(devTpye);
//					String pageSize = zipmap.get(key);
//					if(!"".equals(pageSize)){
//						template.setPageListNum(Integer.parseInt(pageSize));
//					}
//					String path = GlobalConstant.TEMPLATE_ORG_PATH + GlobalConstant.SLASH + orgId + GlobalConstant.SLASH + setNo + GlobalConstant.SLASH + key;
//					template.setPath(path);
//					template.setSetNo(setNo);
//					template.setSetName(filename);
//					template.setScope(InfoTemplate.SCOPE_TYPE_ORG);
//					template.setOrgId(orgId);
//					templist.add(template);
//				}
//				List<InfoTemplate> failList = templateDao.batchSaveTemplate(templist);
//				if (failList != null && failList.size() > 0) {
//					result = false;
//				}
//			}
//		}
//		return result;
//	}

	/**
	 * 获取zip中的模板名称和xml中的信息
	 * @param zip	status=1成功、-1没有home页、-2文件名命名错误、-3存在子页缺子页对应的默认模板、-4子页数字匹配错误
	 * @param filename
	 */
	public Map<String, String> parseZipData(File zip, String filename, String setNo) throws Exception{
		Map<String, String> zipmap = new HashMap<String, String>();
		ZipFile zipFile = new ZipFile(zip.getAbsoluteFile());
		org.apache.tools.zip.ZipEntry entry = null; // 文件条目
		Enumeration entries = zipFile.getEntries();
		
		zipmap.put(RESULT_TYPE_SETNO + setNo, "");
		if(filename != null){
			zipmap.put(RESULT_TYPE_SETNAME + filename.substring(0, filename.indexOf(".")), "");
		}
		Map<String, TempTypeEnum> tempmap = TempTypeEnum.getAllTempTypeMap();
		List<String> childlist = new ArrayList<String>();
		//判断上传zip各类型模板是否包含默认模板
		while (entries.hasMoreElements()) {
			entry = (org.apache.tools.zip.ZipEntry) entries.nextElement();
			if (entry.isDirectory()) {
				continue;
			} else {
				String zipname = entry.getName();
				String typeKey = null;
				if (zipname.endsWith("ftl")) {
					int splitIndex = zipname.indexOf(".");
					String childname = zipname.substring(0, splitIndex);
					typeKey = childname;
					String[] childfile = childname.split("-");
					if(!tempmap.containsKey(childfile[0])){
						zipmap.put("status", "-2");
						zipFile.close();
						return zipmap;
					}
					if(childfile.length > 1){
						typeKey = childfile[0];
						java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("[0-9]*");
				        java.util.regex.Matcher match=pattern.matcher(childfile[1]);
				        if(match.matches()==false){
				        	zipmap.put("status", "-4");
							zipFile.close();
							return zipmap;
				        }
						childlist.add(childfile[0] + ".ftl");
					}
					zipmap.put(zipname, tempmap.get(typeKey).getValue());
				}
			}
		}
		if(!zipmap.containsKey("home.ftl")){
			zipmap.put("status", "-1");
			zipFile.close();
			return zipmap;
		} else {
			for (int i = 0, len = childlist.size(); i < len; i++) {
				if(!zipmap.containsKey(childlist.get(i))){
					zipmap.put("status", "-3");
					zipFile.close();
					return zipmap;
				}
			}
		}
		zipmap.put("status", "1");
		zipFile.close();
		return zipmap;
	}

	/**
	 * 判断数据库中是否已存在当前选中的默认类型模板
	 * @param templist	数据库设备类型默认模板
	 * @param deviceType 更改设备类型默认模板
	 * @param setNo
	 * @return
	 */
//	private boolean isHaveDefaultType(List<InfoTemplate> templist, String deviceType, String setNo){
//		boolean flag = true;
//		String[] devTypes = deviceType.split("#");
//		if(devTypes != null && devTypes.length > 0){
//			for (InfoTemplate temp : templist) {
//				String infoSetNo = temp.getSetNo();
//				String defaultType = temp.getDefaultType();
//				for (String dtype : devTypes) {
//					if(defaultType.indexOf(dtype) >= 0){
//						if(!infoSetNo.equals(setNo)){
//							flag = false;
//						}
//						break;
//					}
//				}
//			}
//		} else {
//			flag = false;
//		}
//		return flag;
//	}
	
	/**
	 * 判断新增设备类型和数据库中的设备类型是否相等
	 * @param oldType
	 * @param newType
	 * @return true 相等 ，false 不等
	 */
	private boolean isTypeEqual(String oldType, String newType){
		boolean flag = false;
		String[] oldTypes = oldType.split("#");
		String[] newTypes = newType.split("#");
		if(oldTypes.length == newTypes.length){
			Map<String, String> oldMap = new HashMap<String, String>();
			for (String old : oldTypes) {
				oldMap.put(old, "");
			}
			for (String nType : newTypes) {
				if(!oldMap.containsKey(nType)){
					return false;
				}
			}
		}
		return flag;
	}

	//获取机构发布时所用首页模板
	@Override
	public InfoTemplate findOrgHomeTemplate(String orgid, String deviceType) throws Exception {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("orgid", orgid);
		parameters.put("deviceType", deviceType);
		return templateDao.findPublishHomeTemplate(parameters);
	}

	//获取设备发布时所用首页模板
	@Override
	public InfoTemplate findDevHomeTemplate(String orgid, String deviceId) throws Exception {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("orgid", orgid);
		parameters.put("deviceId", deviceId);
		return templateDao.findPublishHomeTemplate(parameters);
	}

	@Override
	public List<InfoTemplate> findBySetNo(String setNo) throws Exception {
		return templateDao.findBySetNo(setNo);
	}

	@Override
	public Map<String, InfoTemplate> getDefaultTemplate(List<InfoTemplate> infoTemplates) throws Exception {
		Map<String, InfoTemplate> templateMap = new HashMap<String, InfoTemplate>();
		for(InfoTemplate temp : infoTemplates){
			if(TempTypeEnum.isDefaultTemp(temp.getName())){
				templateMap.put(temp.getType(), temp);
			}
		}
		return templateMap;
	}

	@Override
	public InfoTemplate findOrgUsedTemplate(String orgId, String deviceType)
			throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgId);
		params.put("deviceType", deviceType);
		return templateDao.findUsedTemplate(params);
	}

	@Override
	public InfoTemplate findDevUsedTemplate(String orgId, String deviceId)
			throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgId);
		params.put("deviceId", deviceId);
		return templateDao.findUsedTemplate(params);
	}

	@Override
	public List<InfoTemplate> getAllTemplates(String orgId, String deviceType) throws Exception {
		return templateDao.findOrgTemplateByDevType(orgId, deviceType);
	}

	@Override
	public List<InfoTemplate> getColAllTemplates(String setNo, String templateType) throws Exception {
		return templateDao.findColAllTemplates(setNo, templateType);
	}

	@Override
	public Map<String, List<InfoTemplate>> getTemplateMap(List<InfoTemplate> infoTemplates) throws Exception {
		Map<String, List<InfoTemplate>> templateMap = new HashMap<String, List<InfoTemplate>>();
		for(InfoTemplate template : infoTemplates){
			String type = template.getType();
			List<InfoTemplate> list = templateMap.get(type);
			if(list == null){
				list = new ArrayList<InfoTemplate>();
				templateMap.put(type, list);
			}
			list.add(template);
		}
		return templateMap;
	}

	//根据设备类型查询机构下模板
	public List<InfoTemplate> findOrgTemplateByDevType(String orgId, String deviceType) throws Exception {
		return templateDao.findOrgAllTemplateByDevType(orgId, deviceType);
	}

	@Override
	public InfoTemplate findDefaultTemplate(String orgId, String deviceType) throws Exception {
		List<InfoTemplate> infoTemplates = templateDao.findDefaultTemplates(orgId, deviceType);
		int size = infoTemplates.size();
		if(size == 0){
			return null;
		}
		if(size == 1){
			return infoTemplates.get(0);
		}
		InfoTemplate defaultTemp = null;
		InfoTemplate orgDefaultTemp = null;
		InfoTemplate devDefaultTemp = null;
		InfoTemplate devTemp = null;
		InfoTemplate orgTemp = null;
		for(InfoTemplate template : infoTemplates){
			if(TempTypeEnum.isDefaultTemp(template.getName())){
				String tempOrg = template.getTemplateSet().getOrgId();
				String tempDefaultType = template.getTemplateSet().getDefaultType();
				if(tempDefaultType.contains(deviceType) && tempOrg.equals("apabi")){
					orgDefaultTemp = template;
				}
				if(tempDefaultType.contains(deviceType) && tempOrg.equals(orgId)){
					devDefaultTemp = template;
					break;
				}
				if(tempOrg.equals(orgId) && devTemp == null){
					devTemp = template;
				}
				if(tempOrg.equals("apabi") && orgTemp == null){
					orgTemp = template;
				}
			}
		}
		if(devDefaultTemp != null){
			defaultTemp = devDefaultTemp;
		}else if(orgDefaultTemp != null){
			defaultTemp = orgDefaultTemp;
		}else if(devTemp != null){
			defaultTemp = devTemp;
		}else{
			defaultTemp = orgTemp;
		}
		return defaultTemp;
	}
	//返回整套模板的map(如：默认套、-1套、-2套)
	public Map<String, InfoTemplate> getTemplateSet(String orgId, String setNo, String homeName) throws Exception{
		Map<String, InfoTemplate> tempSetMap = new HashMap<String, InfoTemplate>();
		List<InfoTemplate> templist = templateDao.findTemplateListBySetNo(null, setNo);
		String[] homesplit = homeName.split("-");
		int homelen = homesplit.length;
		if(homelen == 1){
			for (InfoTemplate temp : templist) {
				boolean defaultFlag = TempTypeEnum.isDefaultTemp(temp.getName());
				if(defaultFlag){
					tempSetMap.put(temp.getType(), temp);
				}
			}
		}else if(homelen > 1){
			String suffix = homesplit[1];
			for (InfoTemplate temp : templist) {
				String tempname = temp.getName();
				if(tempname.endsWith(suffix)){
					tempSetMap.put(temp.getType(), temp);
				}
			}
			for (InfoTemplate temp : templist) {
				if(!tempSetMap.containsKey(temp.getType())){
					tempSetMap.put(temp.getType(), temp);
				}
			}
		}
		return tempSetMap;
	}
}