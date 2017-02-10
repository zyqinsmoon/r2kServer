package com.apabi.r2k.admin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.ClientVersionDao;
import com.apabi.r2k.admin.model.ClientVersion;
import com.apabi.r2k.common.base.BaseDaoImpl;

@Repository("clientVersionDao")
public class ClientVersionDaoImpl extends BaseDaoImpl<ClientVersion, Integer> implements ClientVersionDao {

	private Logger log = LoggerFactory.getLogger(ClientVersionDaoImpl.class);
	
	//入库
	public void saveVersion(ClientVersion clientVersion) throws Exception {
		save(clientVersion);
	}
	
	//更新
	public void updateVersion(ClientVersion clientVersion) throws Exception {
		update(clientVersion);
	}
	
	//根据ID查询
	public ClientVersion getVersionById(int id) throws Exception {
		return getById(id);
	}
	
	//分页查询
	public Page<?> pageQuery(PageRequest<?> pageRequest) throws Exception {
		return basePageQuery(getStatement("pageSelect"), pageRequest,getStatement("count"));
	}
	
	public ClientVersion getClientVersion(String version, String devType) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("version", version);
		String[] deviceTypeList = devType.split("#");
		map.put("deviceTypeList", deviceTypeList);
		List<ClientVersion> list = baseDao.selectList(getStatement("get"),map);
		if(list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public ClientVersion getLatestVersion(String devType) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String[] deviceTypeList = devType.split("#");
		map.put("deviceTypeList", deviceTypeList);
		return baseDao.selectOne(getStatement("getLatestVersion"),map);
	}
	
	public void deleteById(int id) throws Exception{
		deleteBy("id",id);
	}

	@Override
	public List<ClientVersion> getAllTypeLatestVersion() throws Exception {
		return baseDao.selectList(getStatement("getAllTypeLatestVersion"));
	}
}
