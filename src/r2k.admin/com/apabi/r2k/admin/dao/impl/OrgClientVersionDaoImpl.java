package com.apabi.r2k.admin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.OrgClientVersionDao;
import com.apabi.r2k.admin.model.OrgClientVersion;
import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("orgClientVersionDao")
public class OrgClientVersionDaoImpl extends BaseDaoImpl<OrgClientVersion, Integer> implements OrgClientVersionDao {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	//入库
	public void saveVersion(OrgClientVersion orgClientVersion) throws Exception {
		save(orgClientVersion);
	}
	
	//更新
	public void updateVersion(OrgClientVersion orgClientVersion) throws Exception {
		update(orgClientVersion);
	}
	
	//根据ID查询
	public OrgClientVersion getVersionById(int id) throws Exception {
		return getById(id);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest) throws Exception {
		return basePageQuery(getStatement("pageSelect"), pageRequest,getStatement("count"));
	}
	
	public Page<?> downVersionPage(PageRequest<?> pageRequest) throws Exception{
		return basePageQuery(getStatement("downVersionPage"), pageRequest,getStatement("downVersionCount"));
	}
	
	public Page<?> upVersionPage(PageRequest<?> pageRequest) throws Exception{
		return basePageQuery(getStatement("upVersionPage"), pageRequest,getStatement("upVersionCount"));
	}
	
	public OrgClientVersion getOrgClientVersion(String orgId, String devType) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId", orgId);
		String[] deviceTypeList = devType.split("#");
		map.put("deviceTypeList", deviceTypeList);
		List<OrgClientVersion> list = baseDao.selectList(getStatement("get"),map);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public void deleteByVersion(String version, String devType) throws Exception{
		String[] deviceTypeList = devType.split("#");
		deleteBy("version",version,"deviceTypeList",deviceTypeList);
	}
}
