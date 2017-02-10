package com.apabi.r2k.menu.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.PrjConfig;
import com.apabi.r2k.admin.service.PrjConfigService;
import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.FileUtils;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.menu.model.Menu;
import com.apabi.r2k.menu.service.MenuService;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.utils.SessionUtils;

@Controller("menuAction")
@Scope("prototype")
public class MenuAction {
	@Autowired
	private MenuService menuService;
	@Autowired
	private PrjConfigService prjConfigService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private int id;
	private String orgId;
	private Page page;
	private String title;
	private String link;
	private File background;
	private File normal;
	private File iconBackground;
	private File logo;
	private File buttonLogo;
	private File centerLogo;
	private Menu menu;
	private List<Menu> menuList;
	private String devType;
	private int menuType;//菜单类型
	
	private int[] checked;	//调用列表
	private String deviceId;
	private String deviceName;
	private int flag;				//0失败，1成功
	private int sort;
	private String centerLogoPath;
	private String buttonLogoPath;
	private String actionName;
	private String devTypeName;
	
	public void pageQuery(String orgId, String devType, String devId) throws Exception{
		HttpServletRequest req = ServletActionContext.getRequest();
		PageRequest pageRequest= new PageRequest();
		PageRequestFactory.bindPageRequest(pageRequest,req);
		Map params = (Map) pageRequest.getFilters();
		params.put("orgId", orgId);
		params.put("deviceType", devType);
		if(StringUtils.isNotEmpty(devId)){
			params.put("deviceId", devId);
		}
		page = menuService.pageQuery(pageRequest);
		if(pageRequest.getPageNumber() == 1 && CollectionUtils.isEmpty(page.getResult())){
			AuthOrg authOrg = SessionUtils.getCurrentOrg(req);
			List<Menu> menus = menuService.addDefaultMenus(authOrg, devType, devId);
			page.setResult(menus);
		}
	}
	
	/**
	 * 触摸屏菜单管理页
	 */
	public String showAndroidLarge(){
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		try {
			devType = DevTypeEnum.AndroidLarge.getName();
			pageQuery(orgId, devType, null);
			setActionName();
			devTypeName = DevTypeEnum.AndroidLarge.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "org";
	}
	
	/**
	 * 竖屏菜单管理页
	 */
	public String showAndroidPortrait(){
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		try {
			devType = DevTypeEnum.AndroidPortrait.getName();
			pageQuery(orgId, devType, null);
			setActionName();
			devTypeName = DevTypeEnum.AndroidPortrait.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "org";
	}
	
	/**
	 * 安卓phone菜单管理页
	 */
	public String showAndroidPhone(){
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		try {
			devType = DevTypeEnum.AndroidPhone.getName();
			pageQuery(orgId, devType, null);
			setActionName();
			devTypeName = DevTypeEnum.AndroidPhone.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "org";
	}
	
	/**
	 * 安卓pad菜单管理页
	 */
	public String showAndroidPad(){
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		try {
			devType = DevTypeEnum.AndroidPad.getName();
			pageQuery(orgId, devType, null);
			setActionName();
			devTypeName = DevTypeEnum.AndroidPad.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "org";
	}
	
	/**
	 * ipad菜单管理页
	 */
	public String orgIPad(){
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		try {
			devType = DevTypeEnum.iPad.getName();
			pageQuery(orgId, devType, null);
			setActionName();
			devTypeName = DevTypeEnum.iPad.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "org";
	}
	
	/**
	 * iphone菜单管理页
	 */
	public String showIPhone(){
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		try {
			devType = DevTypeEnum.iPhone.getName();
			pageQuery(orgId, devType, null);
			setActionName();
			devTypeName = DevTypeEnum.iPhone.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "org";
	}
	
	public String savePage(){
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "save";
	}
	
	public String save(){
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		menu.setOrgId(orgId);
		menu.setStatus(Menu.SATAUS_UNPUBLISH);
		menu.setMenuType(Menu.MENU_TYPE_CUSTOM);
		
		try {
			menuService.saveMenu(menu);
			String devDir = StringUtils.isNotBlank(deviceId) ? deviceId : devType;
			int id = menu.getId();
			if(iconBackground != null){
				String iconBackgroundPath = getIconBackgroundImgPath(orgId, devDir, id);
				File iconBackgroundFile = new File(getImageSavePath(iconBackgroundPath));
				FileUtils.copyFile(iconBackground, iconBackgroundFile);
				FileUtils.deleteQuietly(iconBackground);
				menu.setIconBackground(iconBackgroundPath);
			}
			if(normal != null){
				String normalPath = getNormalImgPath(orgId, devDir, id);
				File normalFile = new File(getImageSavePath(normalPath));
				FileUtils.copyFile(normal, normalFile);
				FileUtils.deleteQuietly(normal);
				menu.setNormal(normalPath);
			}
			menuService.updateMenu(menu);
			logger.info("菜单" + id + "生成成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setActionName();
		return getResultName();
	}
	
	//拼接图片相对路径
	private String getImagePath(String orgId, String devDir,int menuId, String imgName){
		return "/"+orgId+"/"+devDir+"/"+String.valueOf(menuId)+"/"+imgName;
	}
	
	//拼接background图片路径
	private String getIconBackgroundImgPath(String orgId, String devDir,int menuId){
		return getImagePath(orgId, devDir, menuId, Menu.XML_IMG_ICONBACKGROUND+FileUtils.EXT_PNG);
	}
	
	//拼接normal图片路径
	private String getNormalImgPath(String orgId, String devDir, int menuId){
		return getImagePath(orgId, devDir, menuId, Menu.XML_IMG_NORMAL+FileUtils.EXT_PNG);
	}
	
	//拼接图片保存路径
	private String getImageSavePath(String imgPath) throws Exception{
//		return PropertiesUtil.getRootPath() + PropertiesUtil.get("r2k.menu") + imgPath;
		return PropertiesUtil.getRootPath() + "/" + GlobalConstant.PROJECT_FILE_PATH + "/" + PropertiesUtil.get("path.menu.res") + "/" + imgPath;
	}
	
	public String updatePage(){
		try {
			menu = menuService.getById(id);
			setActionName();
			if(StringUtils.isBlank(deviceId)){
				devTypeName = DevTypeEnum.findName(devType).getValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "update";
	}
	
	
	public String update(){
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		try {
			String devDir = StringUtils.isNotBlank(menu.getDeviceId()) ? menu.getDeviceId() : menu.getDeviceType();
			String baseMenuPath = orgId + "/" + devDir + "/" + menu.getId();
			//菜单修改后，状态变为未发布
			menu.setStatus(Menu.SATAUS_UNPUBLISH);
			//替换图片
			if(iconBackground != null){
				String baseIconBackgroundPath = baseMenuPath + "/" + Menu.XML_IMG_ICONBACKGROUND + FileUtils.EXT_PNG;
				String iconBackgroundPath = getImageSavePath(baseIconBackgroundPath);
				File iconBackgroundImg = new File(iconBackgroundPath);
				FileUtils.deleteQuietly(iconBackgroundImg);
				FileUtils.copyFile(iconBackground, iconBackgroundImg);
				menu.setIconBackground(baseIconBackgroundPath);
			}
			if(normal != null){
				String baseNormalPath = baseMenuPath + "/" + Menu.XML_IMG_NORMAL + FileUtils.EXT_PNG;
				String normalPath = getImageSavePath(baseNormalPath);
				File normalImg = new File(normalPath);
				FileUtils.deleteQuietly(normalImg);
				FileUtils.copyFile(normal, normalImg);
				menu.setNormal(baseNormalPath);
			}
			menuService.updateMenu(menu);
			logger.info("菜单" + id + "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setActionName();
		return getResultName();
	}
	
	private void setActionName(){
		if(devType.equals(DevTypeEnum.AndroidLarge.getName())){
			actionName = "showAndroidLarge";
		} else if(devType.equals(DevTypeEnum.AndroidPortrait.getName())){
			actionName = "showAndroidPortrait";
		} else if(devType.equals(DevTypeEnum.AndroidPad.getName())){
			actionName = "showAndroidPad";
		} else if(devType.equals(DevTypeEnum.AndroidPhone.getName())){
			actionName = "showAndroidPhone";
		} else if(devType.equals(DevTypeEnum.iPad.getName())){
			actionName = "orgIPad";
		} else if(devType.equals(DevTypeEnum.iPhone.getName())){
			actionName = "showIPhone";
		}
	}
	
	private String getResultName(){
		if(StringUtils.isNotBlank(deviceId)){
			return "devicePage";
		}else{
			return "orgPage";
		}
	}
	public String delete(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			menuService.deleteById(orgId, devType, deviceId,id);
			logger.info("菜单" + id + "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String device(){
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		try {
			pageQuery(orgId, devType, deviceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "device";
	}
	
	public String publish(){
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		try {
			AuthOrg authOrg = SessionUtils.getCurrentOrg(req);
			List<Menu> menuList = menuService.getMenus(authOrg.getOrgId(), deviceId, devType, Menu.IS_NOT_HIDE);
			if(menuService.checkHasHomePage(menuList)){
				menuService.makeMenuZip(authOrg, devType, deviceId,menuList);
				setFlag(1);
			}else{
				setFlag(2);
			}
		} catch (Exception e) {
			setFlag(0);
			logger.error("发布失败", e);
		}
		
		return "publish";
	}
	
	/**
	 * 跳转创建导航页页面
	 * @return
	 */
	public String updateNavPage(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			menu = menuService.getById(id);
			List<String> configKeys = new ArrayList<String>();
			configKeys.add(PrjConfig.R2K_NAV_CENTER_LOGO);
			configKeys.add(PrjConfig.R2k_NAV_BUTTON_LOGO);
			Map<String, PrjConfig> prjConfigs = new HashMap<String, PrjConfig>();
			if(StringUtils.isNotBlank(deviceId)){
				prjConfigs = prjConfigService.getPrjConfigMapByDevId(orgId, devType, deviceId, PrjConfig.CONFIG_ENABLE, configKeys);
			}else{
				prjConfigs = prjConfigService.getPrjConfigMapByDevType(orgId, devType, PrjConfig.CONFIG_ENABLE, configKeys);
			}
			PrjConfig logoConfig = prjConfigs.get(PrjConfig.R2K_NAV_CENTER_LOGO);
			if(logoConfig != null){
				centerLogoPath = "/" + PropertiesUtil.get("base.r2kfile") + "/"+ PropertiesUtil.get("path.menu.res") + "/"+ logoConfig.getConfigValue();
			}
			PrjConfig buttonConfig = prjConfigs.get(PrjConfig.R2k_NAV_BUTTON_LOGO);
			if(buttonConfig != null){
				buttonLogoPath = "/" + PropertiesUtil.get("base.r2kfile") + "/"+ PropertiesUtil.get("path.menu.res") + "/"+ buttonConfig.getConfigValue();
			}
			setActionName();
			if(StringUtils.isBlank(deviceId)){
				devTypeName = DevTypeEnum.findName(devType).getValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "nav";
	}
	
	/**
	 * 更新导航页
	 * @return
	 */
	public String updateNav(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				menu.setOrgId(orgId);
				menu.setStatus(Menu.SATAUS_UNPUBLISH);
				menuService.updateNavigation(menu, centerLogo, background, buttonLogo, iconBackground, normal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		setActionName();
		return getResultName();
	}

	public String sort(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				if(sort > 0){
					Menu m = new Menu();
					m.setId(id);
					m.setOrgId(orgId);
					m.setSort(sort);
					m.setStatus(Menu.SATAUS_UNPUBLISH);
					m.setDeviceType(devType);
					m.setDeviceId(deviceId);
					menuService.updateSort(m);
				}
			}
			flag = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "sort";
	}
	
	public String checkHomePage(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				menu = menuService.findHomePage(orgId, devType, deviceId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "homePage";
	}
	
	public String clearPublish(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				menuService.clearPublish(orgId, devType, deviceId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "devicePage";
	}
	
//	private String getMenuListUrl(String devType){
//		return "/"+PropertiesUtil.get(key)
//	}
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public File getBackground() {
		return background;
	}

	public void setBackground(File background) {
		this.background = background;
	}

	public File getNormal() {
		return normal;
	}

	public void setNormal(File normal) {
		this.normal = normal;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Menu getMenu() {
		return menu;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setChecked(int[] checked) {
		this.checked = checked;
	}

	public int[] getChecked() {
		return checked;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getFlag() {
		return flag;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}

	public int getMenuType() {
		return menuType;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public File getIconBackground() {
		return iconBackground;
	}

	public void setIconBackground(File iconBackground) {
		this.iconBackground = iconBackground;
	}

	public File getLogo() {
		return logo;
	}

	public void setLogo(File logo) {
		this.logo = logo;
	}

	public File getButtonLogo() {
		return buttonLogo;
	}

	public void setButtonLogo(File buttonLogo) {
		this.buttonLogo = buttonLogo;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public File getCenterLogo() {
		return centerLogo;
	}

	public void setCenterLogo(File centerLogo) {
		this.centerLogo = centerLogo;
	}

	public String getCenterLogoPath() {
		return centerLogoPath;
	}

	public void setCenterLogoPath(String centerLogoPath) {
		this.centerLogoPath = centerLogoPath;
	}

	public String getButtonLogoPath() {
		return buttonLogoPath;
	}

	public void setButtonLogoPath(String buttonLogoPath) {
		this.buttonLogoPath = buttonLogoPath;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getDevTypeName() {
		return devTypeName;
	}

	public void setDevTypeName(String devTypeName) {
		this.devTypeName = devTypeName;
	}
	
}
