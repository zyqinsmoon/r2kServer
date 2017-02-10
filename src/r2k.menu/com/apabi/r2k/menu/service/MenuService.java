package com.apabi.r2k.menu.service;

import java.io.File;
import java.util.List;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.menu.model.Menu;
import com.apabi.r2k.security.model.AuthOrg;

public interface MenuService {
	//获取机构菜单页面
	public Page<?> orgPageQuery(PageRequest<?> pageRequest,String orgId) throws Exception;
	//获取指定设备类型的机构菜单页面
	public Page<?> orgPageQuery(PageRequest<?> pageRequest,String orgId,String deviceType) throws Exception;
	//保存菜单
	public void saveMenu(Menu menu) throws Exception;
	//更新菜单
	public void updateMenu(Menu menu) throws Exception;
	//根据唯一标识查询
	public Menu getById(int id) throws Exception;
	//删除
	public void deleteById(String orgId, String devType, String deviceId, int id) throws Exception;

	public void initDefMenusForDeviceType(AuthOrg authOrg,String deviceType,String filepath)throws Exception;
	/**
	 * 添加机构默认的菜单
	 */
	public List<Menu> addDefaultMenus(AuthOrg authOrg, String deviceType, String deviceId) throws Exception;
	
	public int updateNavigation(Menu menu, File logo, File background, File buttonLogo, File iconBackground, File normal) throws Exception;

	public List<Menu> getMenus(String orgId,String deviceId ,String deviceType,int hide) throws Exception;

	
	/**
	 * 调整顺序
	 */
	public int updateSort(Menu menu) throws Exception;
	
	/**
	 * 获取机构指定设备类型的主页
	 */
	public Menu findHomePage(String orgId, String deviceType, String deviceId) throws Exception;
	
	/**
	 * 检查是否设置了主页
	 * @param menuList
	 * @return
	 * @throws Exception
	 */
	public boolean checkHasHomePage(List<Menu> menuList) throws Exception;
	/**
	 * 生成菜单。适用于机构下按照设备类型或设备上
	 * @param authOrg
	 * @param deviceType
	 * @param deviceId 可以为null
	 * @throws Exception
	 */
	public void makeMenuZip(AuthOrg authOrg,String deviceType,String deviceId,List<Menu> menuList) throws Exception;

	public int updateStatus(String orgId, String devType, String deviceId, int oldStatus, int newStatus, int hide) throws Exception;
	
	public Page pageQuery(PageRequest pageRequest) throws Exception;
	
	public int updateUnpublish(String orgId, String devType, String deviceId) throws Exception;
	
	public int updateUnpublish(String orgId, String devType) throws Exception;
	
	/**
	 * 修改机构授权后，对机构下所有设备类型和设备增减菜单并重新发布
	 * @param authOrg
	 * @param addMenus
	 * @param delMenus
	 * @throws Exception
	 */
	public void modifyMenus(AuthOrg authOrg, List<String> addMenus, List<String> delMenus) throws Exception;
	
	/**
	 * 清除发布
	 * @param orgId
	 * @param devType
	 * @param deviceId
	 * @throws Exception
	 */
	public void clearPublish(String orgId, String devType, String deviceId) throws Exception;
	
	/**
	 * 删除设备菜单
	 */
	public int deleteDevMenu(String orgId, String devType, String deviceId) throws Exception;
}
