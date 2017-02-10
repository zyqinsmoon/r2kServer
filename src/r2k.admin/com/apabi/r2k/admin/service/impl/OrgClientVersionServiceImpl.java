package com.apabi.r2k.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.OrgClientVersionDao;
import com.apabi.r2k.admin.model.OrgClientVersion;
import com.apabi.r2k.admin.service.OrgClientVersionService;

@Service("orgClientVersionService")
public class OrgClientVersionServiceImpl implements OrgClientVersionService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	private OrgClientVersionDao orgClientVersionDao;
	
	//入库
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(OrgClientVersion orgClientVersion) throws Exception {
		orgClientVersionDao.saveVersion(orgClientVersion);
	}
	
	//更新
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(OrgClientVersion orgClientVersion) throws Exception {
		orgClientVersionDao.updateVersion(orgClientVersion);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest) throws Exception {
		
		return null;
	}
	
	//获取机构版本为低版本的列表
	public Page<?> downVersionPage(PageRequest<?> pageRequest, String version, String devType) throws Exception{
		Map params = (Map) pageRequest.getFilters();
		String[] deviceTypeList = devType.split("#");
		params.put("deviceTypeList", deviceTypeList);
		int versionCode = OrgClientVersion.getVersionCodeByVerion(version);
		params.put("versionCode", versionCode);
		Page page = orgClientVersionDao.downVersionPage(pageRequest);
		List<OrgClientVersion> list = page.getResult();
		List<OrgClientVersion> needlist = new ArrayList<OrgClientVersion>();
		OrgClientVersion ver = new OrgClientVersion();
		ver.setVersion(version);
		ver.setVersionCodeByVerion(version);
		for (OrgClientVersion client : list) {
			int result = client.compareTo(ver);
			if(result < 0){
				needlist.add(client);
			}
		}
		page.setResult(needlist);
		return page;
	}
	
	//获取机构版本为高版本的列表
	public Page<?> upVersionPage(PageRequest<?> pageRequest, String version, String devType) throws Exception{
		Map params = (Map) pageRequest.getFilters();
		String[] deviceTypeList = devType.split("#");
		params.put("deviceTypeList", deviceTypeList);
		int versionCode = OrgClientVersion.getVersionCodeByVerion(version);
		params.put("versionCode", versionCode);
		Page page = orgClientVersionDao.upVersionPage(pageRequest);
		List<OrgClientVersion> list = page.getResult();
		List<OrgClientVersion> needlist = new ArrayList<OrgClientVersion>();
		OrgClientVersion ver = new OrgClientVersion();
		ver.setVersion(version);
		ver.setVersionCodeByVerion(version);
		for (OrgClientVersion client : list) {
			int result = client.compareTo(ver);
			if(result > 0){
				needlist.add(client);
			}
		}
		page.setResult(needlist);
		return page;
	}
	
	public OrgClientVersion getVersionById(int id) throws Exception{
		return orgClientVersionDao.getVersionById(id);
	}
	
	public OrgClientVersion getOrgClientVersion(String orgId, String devType) throws Exception{
		return orgClientVersionDao.getOrgClientVersion(orgId,devType);
	}
	
	public void deleteByVersion(String version, String devType) throws Exception{
		orgClientVersionDao.deleteByVersion(version,devType);
	}
}
