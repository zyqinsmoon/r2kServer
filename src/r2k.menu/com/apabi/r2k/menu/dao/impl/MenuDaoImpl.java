package com.apabi.r2k.menu.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.BaseDaoImpl;
import com.apabi.r2k.menu.dao.MenuDao;
import com.apabi.r2k.menu.model.Menu;

@Repository("menuDao")
public class MenuDaoImpl extends BaseDaoImpl<Menu, Integer> implements MenuDao {
	
	//获取菜单页面
	public Page<?> pageQuery(PageRequest<?> pageRequest) throws Exception{
		return basePageQuery(getStatement("pageSelect"), pageRequest,getStatement("pageSelectCount"));
	}
	
	public void saveMenu(Menu menu) throws Exception{
		save(menu);
	}
	
	public int updateMenu(Menu menu) throws Exception{
		return update(menu);
	}
	
	public Menu getMenuById(int id) throws Exception{
		return getById(id);
	}
	
	public int deleteById(Map params) throws Exception{
		return baseDao.delete(getStatement("delete"), params);
	}
	
	public List<Menu> getMenuList(Object... args) throws Exception{
		return findBy(args);
	}
	
	//删除默认菜单
	public void deleteDefaultMenu(int callId,String orgId) throws Exception{
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("orgId", orgId);
		map.put("callId", callId);
		baseDao.delete(getStatement("deleteDefaultMenu"), map);
	}

	@Override
	public List<Menu> getCallMenus(Map params) throws Exception {
		return baseDao.selectList(getStatement("getCallMenus"), params);
	}


	@Override
	public void deleteDefaultMenu(int[] callIds, String orgId) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("orgId", orgId);
		map.put("callIds", callIds);
		baseDao.delete(getStatement("deleteDefaultMenus"), map);
	}

	@Override
	public void batchSaveMenu(List<Menu> menus) throws Exception {
		batchSave(menus);
	}
	
	public  List<Menu> getMenus(Map param) throws Exception{
		return baseDao.selectList("getMenus", param);
	}

	@Override
	public int updateSort(Menu menu) throws Exception {
		return baseDao.update(getStatement("updateSort"), menu);
	}

	@Override
	public Menu findHomePage(Map params) throws Exception {
		return baseDao.selectOne(getStatement("findHomePage"), params);
	}

	@Override
	public int updateStatus(Map params) throws Exception {
		return baseDao.update(getStatement("updateStatus"),params);
	}

	@Override
	public List<Menu> findAddMenuDevs(String orgId) throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgId);
		return baseDao.selectList("findAddMenuDev", params);
	}

	/**
	 * 根据机构id和菜单类型批量删除
	 */
	@Override
	public int deleteByMenuTypes(String orgId, List<String> menuTypes) throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgId);
		params.put("menuTypes", menuTypes);
		return baseDao.delete(getStatement("delByMenuTypes"), params);
	}

	@Override
	public void batchUpdateMenu(List<Menu> menus) throws Exception {
		batchUpdate(getStatement("update"), menus);
	}

	@Override
	public List<Menu> findMenuByTypes(String orgId, List<String> menuTypes) throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgId);
		params.put("menuTypes", menuTypes);
		return baseDao.selectList(getStatement("findMenuByTypes"), params);
	}

	@Override
	public int deleteDevMenu(Map<String, Object> params) throws Exception {
		return baseDao.delete(getStatement("deleteDevMenu"), params);
	}
}
