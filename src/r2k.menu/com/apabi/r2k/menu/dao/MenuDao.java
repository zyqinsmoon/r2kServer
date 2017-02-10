package com.apabi.r2k.menu.dao;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.menu.model.Menu;

public interface MenuDao {
	//获取菜单页面
	public Page<?> pageQuery(PageRequest<?> pageRequest) throws Exception;
	//保存菜单
	public void saveMenu(Menu menu) throws Exception;
	//更新菜单
	public int updateMenu(Menu menu) throws Exception;
	//根据唯一标识查询
	public Menu getMenuById(int id) throws Exception;
	
	public int deleteById(Map params) throws Exception;
	//根据特定条件查询菜单列表
	public List<Menu> getMenuList(Object... args) throws Exception;
	//删除默认菜单
	public void deleteDefaultMenu(int callId,String orgId) throws Exception;
	//批量删除默认菜单
	//删除默认菜单
	public void deleteDefaultMenu(int[] callIds,String orgId) throws Exception;
	//获取引用菜单
	public List<Menu> getCallMenus(Map params) throws Exception;
	/**
	 * 批量保存菜单
	 * @param menus
	 * @throws Exception
	 */
	public void batchSaveMenu(List<Menu> menus) throws Exception; 
	
	/**
	 * 根据条件获取菜单
	 */
	public  List<Menu> getMenus(Map param) throws Exception;

	
	/**
	 * 调整顺序
	 */
	public int updateSort(Menu menu) throws Exception;
	
	/**
	 * 获取主页
	 */
	public Menu findHomePage(Map params) throws Exception;
	
	public int updateStatus(Map params) throws Exception;
	
	public List<Menu> findAddMenuDevs(String orgId) throws Exception;
	
	/**
	 * 根据机构id和菜单类型批量删除
	 */
	public int deleteByMenuTypes(String orgId, List<String> menuTypes) throws Exception;
	
	/**
	 * 批量更新菜单
	 */
	public void batchUpdateMenu(List<Menu> menus) throws Exception;
	
	/**
	 * 根据菜单类型查询菜单
	 * @param orgId
	 * @param menuTypes
	 * @return
	 * @throws Exception
	 */
	public List<Menu> findMenuByTypes(String orgId, List<String> menuTypes) throws Exception;
	
	/**
	 * 删除设备菜单
	 */
	public int deleteDevMenu(Map<String, Object> params) throws Exception;
}
