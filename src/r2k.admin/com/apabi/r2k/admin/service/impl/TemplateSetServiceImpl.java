package com.apabi.r2k.admin.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.TemplateSetDao;
import com.apabi.r2k.admin.model.InfoTemplate;
import com.apabi.r2k.admin.model.InfoTemplateSet;
import com.apabi.r2k.admin.service.TemplateService;
import com.apabi.r2k.admin.service.TemplateSetService;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.TempTypeEnum;
import com.apabi.r2k.common.utils.ZipUtils;

@Service("templateSetService")
public class TemplateSetServiceImpl implements TemplateSetService {

	@Resource(name="templateSetDao")
	private TemplateSetDao templateSetDao;
	@Resource(name="templateService")
	private TemplateService templateService;
	public void setTemplateDao(TemplateSetDao templateSetDao){
		this.templateSetDao = templateSetDao;
	}
	public TemplateSetDao getTemplateDao(){
		return templateSetDao;
	}
	
	//入库
	@Transactional(propagation = Propagation.REQUIRED)
	public int save(InfoTemplateSet templateSet) throws Exception {
		return templateSetDao.saveTemplateSet(templateSet);
	}

	//批量保存
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean batchSave(InfoTemplateSet templateSet, String tempListLenStr) throws Exception {
		String root = PropertiesUtil.getRootPath() + GlobalConstant.SLASH + GlobalConstant.PROJECT_FILE_PATH;
		
		List<InfoTemplate> templist = new ArrayList<InfoTemplate>();
		String[] modellist = tempListLenStr.split("#");
		String tempPath = null;
		if(modellist.length > 0){
			String setNo = templateSet.getSetNo();
			int scope = templateSet.getScope();
			//解压zip文件
			tempPath = root + GlobalConstant.SLASH + GlobalConstant.TEMPLATE_TEMP_PATH + GlobalConstant.SLASH + setNo + ".zip";
			String relPath = "";
			if(scope == InfoTemplateSet.SCOPE_TYPE_ALL){
				relPath = GlobalConstant.SLASH + GlobalConstant.TEMPLATE_PUBLIC_PATH + GlobalConstant.SLASH + setNo;
			} else if(scope == InfoTemplateSet.SCOPE_TYPE_ORG){
				relPath = GlobalConstant.SLASH + GlobalConstant.TEMPLATE_ORG_PATH + GlobalConstant.SLASH + templateSet.getOrgId() + GlobalConstant.SLASH + setNo;
			}
			String pubpath = root + relPath;
			File tempfile = new File(tempPath); 
			ZipUtils.unzip(tempfile, pubpath);
			//刪除zip文件
			tempfile.delete();
			
			//数据入库
			Date now = new Date();
			templateSet.setCrtDate(now);
			templateSet.setLastDate(now);
			for (String model : modellist) {
				String type = model.substring(0,model.indexOf("."));
				InfoTemplate tempObj = new InfoTemplate();
				tempObj.setName(type);
				int index = type.indexOf("-");
				if(index >= 0){
					tempObj.setType(type.substring(0,index));
					int setIndex = Integer.parseInt(type.substring(index));
				} else {
					tempObj.setType(type);
				}
				tempObj.setPath(relPath + GlobalConstant.SLASH + model);
				tempObj.setSetNo(setNo);
				tempObj.setOrgId(templateSet.getOrgId());
				tempObj.setCrtDate(now);
				tempObj.setLastDate(now);
				templist.add(tempObj);
			}
			
		}
		int count = this.save(templateSet);
		boolean tempsetFlag = count > 0 ? true : false;
		boolean tempFlag = templateService.batchSave(templist);
		return (tempsetFlag && tempFlag);
	}

	//批量保存
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean batchSave(List<InfoTemplateSet> tempsetlist)	throws Exception {
		boolean flag = true;
		List<InfoTemplateSet> failList = templateSetDao.batchSaveTemplateSet(tempsetlist);
		if (failList != null && failList.size() > 0) {
			flag = false;
		}
		return flag;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int update(InfoTemplateSet templateSet) throws Exception {
		return templateSetDao.updateTemplateSet(templateSet);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean batchUpdate(List<InfoTemplateSet> tempsetlist) throws Exception {
		boolean flag = true;
		List<InfoTemplateSet> failList = templateSetDao.batchUpdateTemplateSet(tempsetlist);
		if (failList != null && failList.size() > 0) {
			flag = false;
		}
		return flag;
	}

	@Override
	public int deleteById(Integer id) throws Exception {
		return templateSetDao.deleteById(id);
	}

	@Override
	public InfoTemplateSet getTemplateSetById(Integer id) throws Exception {
		return templateSetDao.getTemplateSetById(id);
	}

	public InfoTemplateSet getTemplateSetBySetNo(String orgId, String setNo) throws Exception{
		return templateSetDao.getTemplateSetBySetNo(orgId, setNo);
	}
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest, String countQuery)	throws Exception {
		return templateSetDao.pageQuery(GlobalConstant.PAGE_QUERY_STATEMENT, pageRequest, countQuery);
	}
	//查找默认设备类型模板列表 true:不存在默认模板,false:存在默认模板
	public boolean checkDefaultType(int scope, String defaultType, String orgId) throws Exception{
		boolean flag = true;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("scope", ""+scope);
		String[] defTypeList = defaultType.split("#");
		params.put("defTypeList", defTypeList);
		params.put("orgId", orgId);
		List<InfoTemplateSet> tempsetlist = templateSetDao.checkDefaultType(params);
		if(tempsetlist != null && tempsetlist.size() > 0){
			flag = false;
		}
		return flag;
	}
	public List<InfoTemplateSet> getTemplateSetByDevType(String orgId, String deviceType) throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("orgId", orgId);
		params.put("deviceType", ""+deviceType);
		return templateSetDao.getTemplateSetByDevType(params);
	}
}
