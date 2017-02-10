package com.apabi.r2k.menu.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.PrjConfig;
import com.apabi.r2k.admin.model.PrjEnum;
import com.apabi.r2k.admin.service.PrjConfigService;
import com.apabi.r2k.admin.service.PrjEnumService;
import com.apabi.r2k.common.utils.CollectionUtils;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.FileUtils;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.ZipUtils;
import com.apabi.r2k.menu.dao.MenuDao;
import com.apabi.r2k.menu.model.Menu;
import com.apabi.r2k.menu.service.MenuService;
import com.apabi.r2k.security.model.AuthOrg;

@Service("menuService")
public class MenuServiceImpl implements MenuService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private PrjEnumService prjEnumService;
	@Autowired
	private PrjConfigService prjConfigService;
	

	
	public Page<?> orgPageQuery(PageRequest<?> pageRequest,String orgId) throws Exception{
		Map<String,Object> params = (Map) pageRequest.getFilters();
		params.put("type", DevTypeEnum.ORG.getName());
		params.put("orgId", orgId);
		return menuDao.pageQuery(pageRequest);
	}
	
	public Page<?> orgPageQuery(PageRequest<?> pageRequest,String orgId,String deviceType) throws Exception{
		Map<String,Object> params = (Map) pageRequest.getFilters();
		params.put("deviceType", deviceType);
		params.put("orgId", orgId);
		return menuDao.pageQuery(pageRequest);
	}
	
	//获取设备菜单页面
	public Page<?> devicePageQuery(PageRequest<?> pageRequest) throws Exception{
		Map params = (Map) pageRequest.getFilters();
		params.put("type", DevTypeEnum.AndroidLarge.getName());
		return menuDao.pageQuery(pageRequest);
	}
	
	//保存菜单
	public void saveMenu(Menu menu) throws Exception{
		menuDao.saveMenu(menu);
	}
	
	//更新菜单
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateMenu(Menu menu) throws Exception{
		menuDao.updateMenu(menu);
	}
	
	public Menu getById(int id) throws Exception{
		return menuDao.getMenuById(id);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteById(String orgId, String devType, String deviceId, int id) throws Exception{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgId", orgId);
		params.put("deviceType", devType);
		params.put("deviceId", deviceId);
		params.put("id", id);
		int result = menuDao.deleteById(params);
		//如果数据库删除成功则删除文件
		if(result > 0){
			String devDir = StringUtils.isNotBlank(deviceId) ? deviceId : devType;
			String path = PropertiesUtil.getRootPath() + "/" + PropertiesUtil.get("base.r2kfile") + "/" + PropertiesUtil.get("path.menu.res") + "/" + orgId + "/" + devDir + "/" +id;
			File dir = new File(path);
			FileUtils.deleteDirectory(dir);
		}
	}
	
	private void zipFile(ZipOutputStream out,File file,String filename) throws IOException{
		out.putNextEntry(new ZipEntry(filename));
        final FileInputStream in = new FileInputStream(file);
        int data = in.read(); // 为了防止出现乱码 
        while (data != -1) { // 此处按字节流读取和写入 
        	out.write(data); 
           	data = in.read(); 
        } 
	}
	

	
	
	private void apiZipFile(ZipOutputStream out,File file,String filename) throws IOException{
		out.putNextEntry(new ZipEntry(filename));
        FileUtils.copyFile(file, out);
        
	}
	
	//根据用户类型进行默认菜单分配，有ipad、iphone、anroand phone带user菜单，触摸屏带导航菜单
    private List<String> addDeviceTypeDefMenu(List<String> authRes,String deviceType){
    	if(DevTypeEnum.isHaveUser(deviceType)){
    		authRes.add(0,PrjEnum.ENUM_VALUE_USER);
    	}else{
    		authRes.add(0,PrjEnum.ENUM_VALUE_NAVIGATION);
    	}
    	
    	return authRes;
    }
	/**
	 * 按照设备类型生成默认菜单1、找到本机构已经授权的先关资源2、根据授权找到相应的菜单项3、根据菜单项打包图片、生成xml
	 * @param orgid
	 * @param deviceType
	 * @param out
	 * @throws Exception 
	 */
	public void initDefMenusForDeviceType(AuthOrg authOrg,String deviceType,String filepath) throws Exception{
		List<String> authResList = addDeviceTypeDefMenu(authOrg.getAllAuthRes(), deviceType);
		List<PrjEnum> prjEnums = prjEnumService.getEnumByInEnumValues("AUTH_RES",authResList,deviceType);
		String root = PropertiesUtil.getRootPath();
		//String root = "D:/Workspace/.metadata/.me_tcat/webapps";
		String r2kpath = PropertiesUtil.get("base.r2k");
		String menu = PropertiesUtil.get("path.menu");
		//String dirPath =root+ "/"+PropertiesUtil.get("base.r2kfile")+"/"+PropertiesUtil.get("path.menu.pub")+"/"+deviceType+"/"+PropertiesUtil.get("file.menu.pub.name");
		
		 File file = new File(filepath);
		 if(!file.getParentFile().exists()){
		    	file.getParentFile().mkdirs();
		    }
	     if(!file.exists()){
	    	file.createNewFile();
	     }
		//将文件打包
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
		//生成包含菜单信息的xml
		Document doc = DocumentHelper.createDocument();
		Element r2k = doc.addElement("R2k","http://www.w3.org/2001/XMLSchema-instance");
		Element menus = r2k.addElement(Menu.XML_MENUS);
		//ipad后缀要加@2x
		String suffix = ".png";
		String iosimgformat = "";
		if(deviceType.startsWith("iP")){//ios 
			 iosimgformat = PropertiesUtil.get("ios.img.format");
			suffix = iosimgformat +suffix;
		}
		if(!DevTypeEnum.isHaveUser(deviceType)){
			String buttonlogoName =  Menu.XML_IMG_BUTTON_LOGO+suffix;	
			menus.addAttribute(Menu.XML_IMG_BUTTON_LOGO,buttonlogoName);
			apiZipFile(zos,new File(root+"/"+r2kpath+"/"+menu+"/"+deviceType +"/"+ Menu.XML_IMG_BUTTON_LOGO+suffix),buttonlogoName);
		}
		
		for(PrjEnum prjEnum : prjEnums){
			String s = "false";
				//获取url地址
				String enumValue = prjEnum.getEnumValue();
				String dir = root+"/"+r2kpath+"/"+menu+"/"+deviceType+"/"+enumValue;
				//生成xml文件，同时打包图片
				Element menuEle = menus.addElement(Menu.XML_MENU);
				if(enumValue.equals("navigation")){
					s ="true";
					addImgfileElement(menuEle, Menu.XML_IMG_CENTER_LOGO, suffix,  null);
					apiZipFile(zos,new File(root+"/"+r2kpath+"/"+menu+"/"+deviceType +"/"+ Menu.XML_IMG_CENTER_LOGO+suffix),Menu.XML_IMG_CENTER_LOGO+suffix);

					addImgfileElement(menuEle, Menu.XML_IMG_BACKGROUND, ".jpg",  enumValue);
				}
				menuEle.addAttribute("startpage",s);
				addTextElement(menuEle, Menu.XML_TITLE, prjEnum.getEnumName());
				
				//startpage=true
				Element link = menuEle.addElement(Menu.XML_LINK);
				link.addAttribute("type", prjEnum.getEnumValue());
				//相对路径需要拼成完整的url
				String interfaceUrl = prjEnum.getInterfaceUrl();
				interfaceUrl = PropertiesUtil.get("server.url") + interfaceUrl;
				link.setText(interfaceUrl);
				
				Element icons = menuEle.addElement(Menu.XML_ICONS);
				addImgfileElement(icons, Menu.XML_IMG_NORMAL, suffix, enumValue);
				addTextElement(menuEle, Menu.XML_DESCRITTION, prjEnum.getEnumDesc());
				
				ZipUtils.zipIncludeDirByR2k(zos,new File(dir),"",ZipUtils.Filter.TRUE_FILTER,deviceType,iosimgformat);
		}
		
		menus.addAttribute("count", String.valueOf(prjEnums.size()));
		//路径
		//if(ApiUtils.isHaveUser(deviceType)){
		
		zos.putNextEntry(new ZipEntry(menu+".xml"));
		OutputFormat of = new OutputFormat();
		of.setEncoding("UTF-8");
		XMLWriter writer = new XMLWriter(zos,of);
		writer.write(doc);
		writer.close();
		zos.close();
		
		
	}
	
	public boolean checkHasHomePage(List<Menu> menuList) throws Exception{
		boolean hasHomePage = false;
		for(Menu menu : menuList){
			if(menu.getHomePage().equals(Menu.IS_HOME_PAGE)){
				hasHomePage = true;
				break;
			}
		}
		return hasHomePage;
	}
	
	/**
	 * 生成菜单。适用于机构下按照设备类型或设备上
	 * @param authOrg
	 * @param deviceType
	 * @param deviceId 可以为null
	 * @throws Exception
	 */
	public void makeMenuZip(AuthOrg authOrg,String deviceType,String deviceId, List<Menu> menuList) throws Exception{
		String orgId= authOrg.getOrgId();
		String root = PropertiesUtil.getRootPath();
		String r2kfilepath = PropertiesUtil.get("base.r2kfile");
		String menupath = PropertiesUtil.get("path.menu");
		String pub = PropertiesUtil.get("path.pub");
		String menures = PropertiesUtil.get("path.menu.res");
		String deviceurl = "";
		//菜单发布目录r2kFile/menu/pub/orgid/deviceid/menu.zip
		String suffix = ".png";
		String iosimgformat = "";
	    Map<String,PrjConfig> pc = null ;
	  //iPad,iPhone
		if(deviceType.startsWith("iP")){
			iosimgformat = PropertiesUtil.get("ios.img.format");
			suffix = iosimgformat+suffix;
		}
		if(!DevTypeEnum.isHaveUser(deviceType)){
			List<String> configKeys = new ArrayList<String>();
			configKeys.add(PrjConfig.R2k_NAV_BUTTON_LOGO);
			configKeys.add(PrjConfig.R2K_NAV_CENTER_LOGO);
			if(StringUtils.isNotBlank(deviceId)){
				deviceurl = deviceId;
				 pc = prjConfigService.getPrjConfigMapByDevId(orgId, deviceType, deviceId, PrjConfig.CONFIG_ENABLE, configKeys);
			}else{
				deviceurl = deviceType;
				 pc = prjConfigService.getPrjConfigMapByDevType(orgId, deviceType, PrjConfig.CONFIG_ENABLE, configKeys);
			}	
		}else{
			deviceurl = deviceType;
		}
		
		
		//文件发布后路径
		String pubPath = root+"/"+r2kfilepath+"/"+menupath+"/"+pub+"/"+orgId+"/"+deviceurl+"/"+menupath+".zip";
     	File file = FileUtils.delAndCrtFile(pubPath);
		//将文件打包
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
		//生成包含菜单信息的xml
		Document doc = DocumentHelper.createDocument();
		Element r2k = doc.addElement("R2k","http://www.w3.org/2001/XMLSchema-instance");
		Element menus = r2k.addElement(Menu.XML_MENUS);
		menus.addAttribute("count", String.valueOf(menuList.size()));
		if(!DevTypeEnum.isHaveUser(deviceType)){
			String buttonlogoName =  Menu.XML_IMG_BUTTON_LOGO+suffix;	
			menus.addAttribute(Menu.XML_IMG_BUTTON_LOGO,buttonlogoName);
			//copy buttonlogo
			apiZipFile(zos,new File(root+"/"+r2kfilepath+"/"+menures+"/"+pc.get(PrjConfig.R2k_NAV_BUTTON_LOGO).getConfigValue()),buttonlogoName);
		}
		
        //List<Menu> menuList = this.getMenus(orgId, deviceId, deviceType, Menu.IS_NOT_HIDE);
		for(Menu menu : menuList){
			//获取url地址
			String menuId = String.valueOf(menu.getId());
			//#菜单文件上传目录r2kFile/menu/res/orgid/devicetype或者deviceid/菜单id(内置菜单就是ENUM_VALUE)/文件
			String dir =root+"/"+r2kfilepath+"/"+menures+"/"+orgId+"/"+deviceurl+"/"+menuId;;
			
			Element menuEle = menus.addElement(Menu.XML_MENU);
			menuEle.addAttribute("startpage",menu.getHomePageFormatBoolean());
			addTextElement(menuEle,Menu.XML_TITLE,menu.getTitle());
			
			Element link = menuEle.addElement(Menu.XML_LINK);
			String menuType = menu.getMenuType();
			link.addAttribute("type",menuType);
			//相对路径需要拼成完整的url
			String linkurl = menu.getLink();
			if(!menu.getMenuType().equals(Menu.MENU_TYPE_CUSTOM)){
				linkurl = PropertiesUtil.get("server.url") +linkurl;
			}
			link.setText(linkurl);
			Element icons = menuEle.addElement(Menu.XML_ICONS);
			addImgfileElement(icons,Menu.XML_IMG_NORMAL,suffix,menuId);
			if(StringUtils.isNotBlank(menu.getIconBackground())){
				addImgfileElement(icons,Menu.XML_IMG_ICONBACKGROUND,suffix,menuId);
			}
			
			if(menuType.equals("navigation")){
				addImgfileElement(menuEle,Menu.XML_IMG_CENTER_LOGO,suffix,null);
				apiZipFile(zos,new File(root+"/"+r2kfilepath+"/"+menures+"/"+pc.get(PrjConfig.R2K_NAV_CENTER_LOGO).getConfigValue()),Menu.XML_IMG_CENTER_LOGO+suffix);
				String backgroundimgsuffix = menu.getBackground().substring(menu.getBackground().lastIndexOf("."), menu.getBackground().length());
				addImgfileElement(menuEle,Menu.XML_IMG_BACKGROUND,backgroundimgsuffix,menuId);
			}
			
			//增加菜单描述
			addTextElement(menuEle,Menu.XML_DESCRITTION,menu.getDescription());
			//拷贝菜单图片
			ZipUtils.zipIncludeDirByR2k(zos,new File(dir),"",ZipUtils.Filter.TRUE_FILTER,deviceType,iosimgformat);
		}
		
		
		zos.putNextEntry(new ZipEntry("menu.xml"));
		OutputFormat of = new OutputFormat();
		of.setEncoding("UTF-8");
		XMLWriter writer = new XMLWriter(zos,of);
		writer.write(doc);
		writer.close();
		zos.close();
		//更新菜单状态
		if(StringUtils.isNotBlank(deviceId)){
			this.updateUnpublish(orgId, deviceType, deviceId);
		}else{
			this.updateUnpublish(orgId, deviceType);
		}
		
	}
	
//**********************
	private void copyMenuImg(Document doc,String basePath){
		
	}
	public void makeMenuZip(File zipFile,Document doc) throws FileNotFoundException{
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
		try {
			//copy文件
			
			zos.putNextEntry(new ZipEntry("menu.xml"));
			OutputFormat of = new OutputFormat();
			of.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(zos,of);
			writer.write(doc);
			writer.close();
			zos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(zos!=null){
				try {
					zos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//从menu 中获取需要copy的文件路径
	public void getImgurlByMenulist( String orgId ,String deviceType,String deviceId,List<Menu> menuList) throws Exception{
		String root = PropertiesUtil.getRootPath();
		String r2kfilepath = PropertiesUtil.get("base.r2kfile");
		String menures = PropertiesUtil.get("path.menu.res");
		List<String> imgurlList = new ArrayList<String>();
		Map<String,PrjConfig> pc = getPrjConfigByMenu(orgId, deviceType, deviceId);
		for(Menu menu:menuList){
			if(menu.getMenuType().equals("navigation")){
				String centerlogourl = root+"/"+r2kfilepath+"/"+menures+"/"+pc.get(PrjConfig.R2K_NAV_CENTER_LOGO).getConfigValue();
				imgurlList.add(centerlogourl);
				
			}else{
				String url = root+"/"+r2kfilepath+"/"+menures+"/"+orgId+"/"+menu.getId();
			   imgurlList.add(url);
			}
		}
		String buttonlogourl  =root+"/"+r2kfilepath+"/"+menures+"/"+pc.get(PrjConfig.R2k_NAV_BUTTON_LOGO).getConfigValue(); 
		imgurlList.add(buttonlogourl);
	}
	
	//获取配置项,如果deviceid为空将按设备类型查询
	private  Map<String,PrjConfig> getPrjConfigByMenu(String orgId,String deviceType,String deviceId) throws Exception{
		List<String> configKeys = new ArrayList<String>();
		configKeys.add(PrjConfig.R2k_NAV_BUTTON_LOGO);
		configKeys.add(PrjConfig.R2K_NAV_CENTER_LOGO);
		 Map<String,PrjConfig> pc = null ;
		if(StringUtils.isNotBlank(deviceId)){
			 pc = prjConfigService.getPrjConfigMapByDevId(orgId, deviceType, deviceId, PrjConfig.CONFIG_ENABLE, configKeys);
		}else{
			 pc = prjConfigService.getPrjConfigMapByDevType(orgId, deviceType, PrjConfig.CONFIG_ENABLE, configKeys);
		}
		return pc;
	}
	
	//生成菜单xml
	private Document makeMenuXml(String deviceType, List<Menu> menuList,String suffix) throws Exception{ 
		Document doc = DocumentHelper.createDocument();
		Element r2k = doc.addElement("R2k","http://www.w3.org/2001/XMLSchema-instance");
		Element menus = r2k.addElement(Menu.XML_MENUS);
		menus.addAttribute("count", String.valueOf(menuList.size()));
		
		if(deviceType.startsWith("iP")){//IOS设备特殊处理
			String iosimgformat = PropertiesUtil.get("ios.img.format");
			suffix = iosimgformat+suffix;
		}
		if(!DevTypeEnum.isHaveUser(deviceType)){//触摸屏设备特殊处理
		    String buttonlogoName =  Menu.XML_IMG_BUTTON_LOGO+suffix;	
			menus.addAttribute(Menu.XML_IMG_BUTTON_LOGO,buttonlogoName);
		}
		for(Menu menu : menuList){
			String menuId = String.valueOf(menu.getId());
			Element menuEle = menus.addElement(Menu.XML_MENU);
			menuEle.addAttribute("startpage",menu.getHomePageFormatBoolean());
			addTextElement(menuEle,Menu.XML_TITLE,menu.getTitle());
			
			Element link = menuEle.addElement(Menu.XML_LINK);
			String menuType = menu.getMenuType();
			link.addAttribute("type",menuType);
			//相对路径需要拼成完整的url
			String linkurl = menu.getLink();
			if(!linkurl.startsWith("http://")&&!linkurl.startsWith("https://")){
				linkurl = PropertiesUtil.get("server.url") +linkurl;
			}
			link.setText(linkurl);
			Element icons = menuEle.addElement(Menu.XML_ICONS);
			addImgfileElement(icons,Menu.XML_IMG_NORMAL,suffix,menuId);
			if(StringUtils.isNotBlank(menu.getIconBackground())){
				addImgfileElement(icons,Menu.XML_IMG_ICONBACKGROUND,suffix,menuId);
			}
			//带有系统默认导航页设备特殊处理
			if(menuType.equals("navigation")){
				addImgfileElement(menuEle,Menu.XML_IMG_CENTER_LOGO,suffix,null);
				String backgroundimgsuffix = menu.getBackground().substring(menu.getBackground().lastIndexOf("."), menu.getBackground().length());
				addImgfileElement(menuEle,Menu.XML_IMG_BACKGROUND,backgroundimgsuffix,menuId);
			}
			
			//增加菜单描述
			addTextElement(menuEle,Menu.XML_DESCRITTION,menu.getDescription());
			
		}
		return doc;
	}
	//********************************
	
	//添加图片类型子节点
	private void addImgfileElement(Element menuEle,String eleName,String suffix,String parentPath){
		String backgroundName =eleName + suffix;
		Element child = menuEle.addElement(eleName);
		//child.addAttribute("type", "img");
		if(StringUtils.isNotBlank(parentPath)){
			child.addText(parentPath+"/"+backgroundName);
		}else{
			child.addText(backgroundName);
		}
		
	}

	//添加文字节点
	private void addTextElement(Element menuEle,String eleName,String text){
		if(StringUtils.isNotBlank(text)){
			Element descEle = menuEle.addElement(eleName);
			descEle.addText(text);
			}
	}
	/**
	 * 添加机构默认菜单
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Menu> addDefaultMenus(AuthOrg authOrg, String deviceType, String deviceId) throws Exception {
		//获取默认菜单列表
		List<String> authResList = addDeviceTypeDefMenu(authOrg.getAllAuthRes(), deviceType);
		List<PrjEnum> prjEnums = prjEnumService.getEnumByInEnumValues("AUTH_RES",authResList,deviceType);
		List<Menu> menus = new ArrayList<Menu>();
		List<PrjConfig> configs = new ArrayList<PrjConfig>();
		String orgId = authOrg.getOrgId();
		//创建默认菜单
		for(PrjEnum prjEnum : prjEnums){
			Menu menu = null;
			if(prjEnum.getEnumValue().equals(PrjEnum.ENUM_VALUE_NAVIGATION)){
				menu = createNavMenu(prjEnum, orgId, deviceType, deviceId);
				PrjConfig logoConf = createOrgLogoConfig(orgId, deviceType,deviceId);
				if(logoConf != null){
					configs.add(logoConf);
				}
				PrjConfig buttonConf = createOrgButtonLogoConfig(orgId, deviceType,deviceId);
				if(buttonConf != null){
					configs.add(buttonConf);
				}
				prjConfigService.batchSave(configs);
			}else{
				menu = createNormalSystemMenu(prjEnum, orgId, deviceType, deviceId);
			}
			menus.add(menu);
		}
		return menus;
	}

	/**
	 * 创建导航页菜单
	 */
	private Menu createNavMenu(PrjEnum prjEnum, String orgId, String devType, String deviceId) throws Exception{
		Menu menu = createMenuByPrjEnum(prjEnum, orgId, devType, deviceId);
		menu.setHomePage(Menu.IS_HOME_PAGE);
		saveMenu(menu);
		if(menu.getId() > 0){
			int mid = menu.getId();
			String devDir = StringUtils.isNotBlank(deviceId) ? deviceId : devType;
			String srcPath = getR2kMenuResPath(devType+"/"+prjEnum.getEnumValue());
			String destPath = getR2kFileMenuResPath(orgId+"/"+devDir+"/"+mid);
			File destDir = new File(destPath);
			File backgroundFile = new File(srcPath+"/"+Menu.XML_IMG_BACKGROUND+FileUtils.EXT_JPG);
			FileUtils.copyFileToDirectory(backgroundFile, destDir);
			menu.setBackground(orgId+"/"+devDir+"/"+mid+"/"+Menu.XML_IMG_BACKGROUND+FileUtils.EXT_JPG);
			File normalFile = new File(srcPath+"/"+Menu.XML_IMG_NORMAL+FileUtils.EXT_PNG);
			FileUtils.copyFileToDirectory(normalFile, destDir);
			menu.setNormal(orgId+"/"+devDir+"/"+mid+"/"+Menu.XML_IMG_NORMAL+FileUtils.EXT_PNG);
			updateMenu(menu);
		}
		return menu;
	}
	
	/*
	 * 创建机构导航页logo配置项
	 */
	private PrjConfig createOrgLogoConfig(String orgId, String devType, String deviceId) throws Exception{
		return createConfig(orgId, devType, deviceId, PrjConfig.R2K_NAV_CENTER_LOGO, Menu.XML_IMG_CENTER_LOGO+FileUtils.EXT_PNG);
	}
	
	private PrjConfig createOrgButtonLogoConfig(String orgId, String devType,String deviceId) throws Exception{
		return createConfig(orgId, devType, deviceId, PrjConfig.R2k_NAV_BUTTON_LOGO, Menu.XML_IMG_BUTTON_LOGO+FileUtils.EXT_PNG);
	}
	
	private PrjConfig createConfig(String orgId, String devType,String deviceId,String configKey, String imgName) throws Exception{
		String devDir = StringUtils.isNotBlank(deviceId) ? deviceId : devType;
		//拼接设备类型devType下的导航页logo所在目录绝对路径
		String srcPath = getR2kMenuResPath(devType+"/"+imgName);
		File srcImg = new File(srcPath);
		if(!srcImg.exists()){
			return null;
		}
		//拼接导航logo图需要拷贝的路径
		String destPath = orgId+"/"+devDir+"/"+imgName;
		File destFile = new File(getR2kFileMenuResPath(destPath));
		FileUtils.copyFile(srcImg, destFile);
		PrjConfig prjConfig = new PrjConfig(orgId, devType, deviceId, configKey, destPath, PrjConfig.CONFIG_ENABLE);
		return prjConfig;
	}
	
	/**
	 * 创建机构非导航页以外的默认菜单
	 */
	private Menu createNormalSystemMenu(PrjEnum prjEnum, String orgId, String devType, String deviceId) throws Exception{
		String devDir = StringUtils.isNotBlank(deviceId) ? deviceId : devType;
		Menu menu = createMenuByPrjEnum(prjEnum, orgId, devType, deviceId);
		saveMenu(menu);
		if(menu.getId() > 0){
			int mid = menu.getId();
			String srcPath = getR2kMenuResPath(devType+ "/" +prjEnum.getEnumValue());
			String destPath = getR2kFileMenuResPath(orgId+ "/" +devDir+ "/" +mid);
			File destDir = new File(destPath);
			File srcDir = new File(srcPath);
			File[] imgs = srcDir.listFiles();
			for(File img : imgs){
				String imgName = img.getName();
				if(imgName.startsWith(Menu.XML_IMG_BACKGROUND)){
					FileUtils.copyFileToDirectory(img, destDir);
					menu.setBackground(orgId+ "/" +devDir+ "/" +mid+ "/" +imgName);
				}else if(imgName.startsWith(Menu.XML_IMG_ICONBACKGROUND)){
					FileUtils.copyFileToDirectory(img, destDir);
					menu.setIconBackground(orgId+ "/" +devDir+ "/" +mid+ "/" +imgName);
				}else if(imgName.startsWith(Menu.XML_IMG_NORMAL)){
					FileUtils.copyFileToDirectory(img, destDir);
					menu.setNormal(orgId+ "/" +devDir+ "/" +mid+ "/" +imgName);
				}else if(imgName.startsWith(Menu.XML_IMG_LOGO)){
					FileUtils.copyFileToDirectory(img, destDir);
					menu.setLogo(orgId+ "/" +devDir+ "/" +mid+ "/" +imgName);
				}
			}
			updateMenu(menu);
		}
		return menu;
	}
	
	private Menu createMenuByPrjEnum(PrjEnum prjEnum, String orgId, String devType, String deviceId){
		Menu menu = new Menu();
		menu.setTitle(prjEnum.getEnumName());
		menu.setOrgId(orgId);
		menu.setDeviceType(devType);
		menu.setDeviceId(deviceId);
		menu.setLink(prjEnum.getInterfaceUrl());
		menu.setMenuType(prjEnum.getEnumValue());
		menu.setStatus(Menu.SATAUS_UNPUBLISH);
		menu.setHide(Menu.IS_NOT_HIDE);
		menu.setHomePage(Menu.IS_NOT_HOME_PAGE);
		menu.setDescription(prjEnum.getEnumDesc());
		menu.setSort(1);
		return menu;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateNavigation(Menu menu, File logo, File background, File buttonLogo, File iconBackground, File normal) throws Exception {
		List<String> configKeys = new ArrayList<String>();
		configKeys.add(PrjConfig.R2K_NAV_CENTER_LOGO);
		configKeys.add(PrjConfig.R2k_NAV_BUTTON_LOGO);
		List<PrjConfig> prjConfigs = null;
		String orgId = menu.getOrgId();
		String devType = menu.getDeviceType();
		if(StringUtils.isNotBlank(menu.getDeviceId())){
			prjConfigs = prjConfigService.getPrjConfigListByDevId(orgId, devType, menu.getDeviceId(), PrjConfig.CONFIG_ENABLE, configKeys);
		}else{

			prjConfigs = prjConfigService.getPrjConfigListByDevType(orgId, devType, PrjConfig.CONFIG_ENABLE, configKeys);
		}
		return updateNav(menu, logo, background, buttonLogo, iconBackground, normal, prjConfigs);
	}
	
	
	private int updateNav(Menu menu, File logo, File background, File buttonLogo, File iconBackground, File normal, List<PrjConfig> prjConfigs) throws Exception{
		PrjConfig logoConf = null;
		PrjConfig buttonConf = null;
		for(PrjConfig conf : prjConfigs){
			if(conf.getConfigKey().equals(PrjConfig.R2K_NAV_CENTER_LOGO)){
				logoConf = conf;
			}
			if(conf.getConfigKey().equals(PrjConfig.R2k_NAV_BUTTON_LOGO)){
				buttonConf = conf;
			}
		}
		String orgId = menu.getOrgId();
		String devType = menu.getDeviceType();
		String deviceId = menu.getDeviceId();
		//如果center logo配置项不存在，则先创建
		if(logoConf == null){
			logoConf = createOrgLogoConfig(orgId, devType, deviceId);
			prjConfigService.save(logoConf);
		}
		//如果button log配置项不存在，则先创建
		if(buttonConf == null){
			buttonConf = createOrgButtonLogoConfig(orgId, devType, deviceId);
			prjConfigService.save(buttonConf);
		}
		String devDir = StringUtils.isNotBlank(deviceId) ? deviceId : devType;
		String baseMenuPath = menu.getOrgId() + "/" + devDir;
		String baseOrgMenuPath = baseMenuPath + "/" + menu.getId();
		if(logo != null){
			String oldLogoPath = getR2kFileMenuResPath(logoConf.getConfigValue());
			File oldLogo = new File(oldLogoPath);
			FileUtils.deleteAndCopyFile(logo, oldLogo);
			logoConf.setLastDate(new Date());
		}
		if(background != null){
			String baseBackgroundPath = baseOrgMenuPath + "/" + Menu.XML_IMG_BACKGROUND + FileUtils.EXT_JPG;
			String backgroundPath = getR2kFileMenuResPath(baseBackgroundPath);
			File backgroundFile = new File(backgroundPath);
			FileUtils.deleteAndCopyFile(background, backgroundFile);
			menu.setBackground(baseBackgroundPath);
		}
		if(buttonLogo != null){
			String btnLogoPath = getR2kFileMenuResPath(buttonConf.getConfigValue());
			File btnLogoFile = new File(btnLogoPath);
			FileUtils.deleteAndCopyFile(buttonLogo, btnLogoFile);
			buttonConf.setLastDate(new Date());
		}
		if(iconBackground != null){
			String baseIconBackgroundPath = baseOrgMenuPath + "/" + Menu.XML_IMG_ICONBACKGROUND + FileUtils.EXT_PNG;
			String iconBackgroundPath = getR2kFileMenuResPath(baseIconBackgroundPath);
			File iconBackgroundFile = new File(iconBackgroundPath);
			FileUtils.deleteAndCopyFile(iconBackground, iconBackgroundFile);
			menu.setIconBackground(baseIconBackgroundPath);
		}
		if(normal != null){
			String baseNormalPath = baseOrgMenuPath + "/" + Menu.XML_IMG_NORMAL + FileUtils.EXT_PNG;
			String normalPath = getR2kFileMenuResPath(baseNormalPath);
			File normalFile = new File(normalPath);
			FileUtils.deleteAndCopyFile(normal, normalFile);
			menu.setNormal(baseNormalPath);
		}
		return menuDao.updateMenu(menu);
		
	}
	
	public List<Menu> getMenus(String orgId,String deviceId ,String deviceType,int hide) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orgId", orgId);
		param.put("deviceId", deviceId);
		param.put("deviceType", deviceType);
		param.put("hide", hide);
		return menuDao.getMenus(param);
		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateSort(Menu menu) throws Exception {
		return menuDao.updateSort(menu);
	}

	@Override
	public Menu findHomePage(String orgId, String deviceType, String deviceId) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgId", orgId);
		params.put("deviceType", deviceType);
		params.put("deviceId", deviceId);
		return menuDao.findHomePage(params);
	}

	//参数路径path前需有"/"
	private String getR2kMenuResPath(String path) throws Exception{
		return PropertiesUtil.getRootPath() + "/" + GlobalConstant.R2K_PATH + "/" + PropertiesUtil.get("path.menu") + "/" + path;
	}
	
	//参数路径path前需有"/"
	private String getR2kFileMenuResPath(String path) throws Exception{
		return PropertiesUtil.getRootPath() + "/" + GlobalConstant.PROJECT_FILE_PATH + "/" + PropertiesUtil.get("path.menu.res") + "/" + path;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateStatus(String orgId, String devType, String deviceId, int oldStatus, int newStatus, int hide) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgId", orgId);
		params.put("deviceType", devType);
		params.put("deviceId", deviceId);
		params.put("status", oldStatus);
		params.put("newStatus", newStatus);
		params.put("hide", hide);
		return menuDao.updateStatus(params);
	}

	@Override
	public Page pageQuery(PageRequest pageRequest) throws Exception {
		return menuDao.pageQuery(pageRequest);
	}

	//更新设备未发布且未隐藏的菜单的状态为已发布
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateUnpublish(String orgId, String devType, String deviceId) throws Exception {
		return updateStatus(orgId, devType, deviceId, Menu.SATAUS_UNPUBLISH, Menu.STATUS_PUBLISH, Menu.IS_NOT_HIDE);
	}

	//更新设备类型上未发布且未隐藏的菜单的状态为已发布
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateUnpublish(String orgId, String devType) throws Exception {
		return updateStatus(orgId, devType, null, Menu.SATAUS_UNPUBLISH, Menu.STATUS_PUBLISH, Menu.IS_NOT_HIDE);
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void modifyMenus(AuthOrg authOrg, List<String> addMenus, List<String> delMenus) throws Exception {
		String orgId = authOrg.getOrgId();
		List<Menu> hasMenuDevs = menuDao.findAddMenuDevs(orgId);
		//如果没有添加过菜单则不执行后续操作
		if(CollectionUtils.isEmpty(hasMenuDevs)){
			return;
		}
		//如果需要删除的菜单列表不为空，则删除取消授权的菜单及文件
		if(CollectionUtils.isNotEmpty(delMenus)){
			deleteMenuAndFile(orgId, hasMenuDevs, delMenus);
		}
		//如果需要增加的菜单列表不为空，则添加加菜单授权及文件
		if(CollectionUtils.isNotEmpty(addMenus)){
			addAuthMenus(orgId, hasMenuDevs, addMenus);
		}
		//重新发布菜单
		republish(authOrg, hasMenuDevs);
	}
	
	//删除取消授权的菜单及文件
	private void deleteMenuAndFile(String orgId, List<Menu> hasMenuDevs, List<String> delMenus) throws Exception{
		//删除取消授权的菜单
		List<Menu> needDelMenus = menuDao.findMenuByTypes(orgId, delMenus);
		int delResult = menuDao.deleteByMenuTypes(orgId, delMenus);
		if(delResult == 0){
			return;
		}
		String basePath = PropertiesUtil.getRootPath() + "/" + PropertiesUtil.get("base.r2kfile") + "/" + PropertiesUtil.get("path.menu.res");
		//删除菜单文件
		for(Menu menu : hasMenuDevs){
			String devType = menu.getDeviceType();
			String deviceId = menu.getDeviceId();
			String devDir = StringUtils.isNotBlank(deviceId) ? deviceId : devType;
			String id = null;
			//删除当前设备类型或设备下所有需要删除的菜单的文件
			for(Menu tmpMenu : needDelMenus){
				if(StringUtils.equals(devType, tmpMenu.getDeviceType()) && StringUtils.equals(deviceId, tmpMenu.getDeviceId())){
					id = String.valueOf(tmpMenu.getId());
					File menuDir = new File(basePath + "/" + orgId + "/" + devDir + "/" + id);
					FileUtils.deleteDirectory(menuDir);
				}
			}
		}
	}
	
	//增加授权菜单及拷贝文件
	private void addAuthMenus(String orgId, List<Menu> hasMenuDevs, List<String> addMenus) throws Exception{
		//获取增加授权的默认菜单
		Map<String, PrjEnum> prjEnums = prjEnumService.getEnumMapByEnumValues("AUTH_RES", addMenus);
		List<Menu> menus = new ArrayList<Menu>();
		for(Menu menuDev : hasMenuDevs){
			String devType = menuDev.getDeviceType();
			for(String menuType : addMenus){
				PrjEnum prjEnum = prjEnums.get(menuType);
				//如果默认菜单适合当前的设备类型则创建菜单
				if(prjEnum.getDevDef().contains(devType)){
					Menu menu = createMenuByPrjEnum(prjEnum, orgId, devType, menuDev.getDeviceId());
					menus.add(menu);
				}
			}
		}
		menuDao.batchSaveMenu(menus);
		//源资源文件根目录{tomcat}/r2k/menu
		String baseSrcPath = PropertiesUtil.getRootPath() + "/" + PropertiesUtil.get("base.r2k") + "/" + PropertiesUtil.get("path.menu");
		//目标资源文件根目录{tomcat}/r2kFile/menu/res
		String baseDestPath = PropertiesUtil.getRootPath() + "/" + PropertiesUtil.get("base.r2kfile") + "/" + PropertiesUtil.get("path.menu.res");
		for(Menu m : menus){
			//如果菜单id大于0，则认为保存成功，可以拷贝图片
			int mid = m.getId();
			if(mid > 0){
				String devType = m.getDeviceType();
				String devDir = StringUtils.isNotBlank(m.getDeviceId()) ? m.getDeviceId() : devType;
				String menuType = m.getMenuType();
				//数据库记录菜单资源文件相对根目录/orgId/[deviceId|deviceType]/menuId
				String menuResPath = orgId+ "/" +devDir+ "/" +mid;
				//源资源文件存放目录{tomcat}/r2k/menu/{navigation|picture|...}
				String srcDirPath = baseSrcPath + "/" + devType + "/" + menuType;
				File srcDir = new File(srcDirPath);
				//目标文件存放目录{tomcat}/r2kFile/menu/res/orgId/[deviceId|deviceType]/menuId
				File destDir = new File(baseDestPath+"/"+orgId+"/"+devDir+"/"+mid);
				File[] resFiles = srcDir.listFiles();
				//遍历源资源文件目录中的所有文件，根据已有文件拷贝资源文件并设置菜单对应的文件路径
				for(File resFile : resFiles){
					String imgName = resFile.getName();
					if(imgName.startsWith(Menu.XML_IMG_BACKGROUND)){
						//拷贝背景图，并设置menu背景图路径
						m.setBackground(menuResPath+ "/" +imgName);
					}else if(imgName.startsWith(Menu.XML_IMG_ICONBACKGROUND)){
						//拷贝菜单背景图，并设置menu菜单背景图路径
						m.setIconBackground(menuResPath+ "/" +imgName);
					}else if(imgName.startsWith(Menu.XML_IMG_NORMAL)){
						//拷贝菜单图标，并设置menu菜单图标路径
						m.setNormal(menuResPath+ "/" +imgName);
					}else if(imgName.startsWith(Menu.XML_IMG_LOGO)){
						//拷贝logo图，并设置menu logo图路径
						m.setLogo(menuResPath+ "/" +imgName);
					}
					FileUtils.copyFileToDirectory(resFile, destDir);
				}
			}
		}
		//拷贝完资源文件后更新菜单记录
		menuDao.batchUpdateMenu(menus);
	}
	
	//重新发布
	private void republish(AuthOrg authOrg, List<Menu> hasMenuDevs) throws Exception{
		String orgId = authOrg.getOrgId();
		String menuPath = PropertiesUtil.getRootPath()+"/"+PropertiesUtil.get("base.r2kfile")+"/"+PropertiesUtil.get("path.menu.pub")+"/"+orgId;
		//先删除原来所有已发布的菜单再重新发布
		FileUtils.deleteDirectory(new File(menuPath));
		for(Menu menu : hasMenuDevs){
			String devType = menu.getDeviceType();
			String deviceId = menu.getDeviceId();
			List<Menu> menuList = getPublishedMenu(orgId, menu.getDeviceType(), menu.getDeviceId());
			if(CollectionUtils.isEmpty(menuList)){
				continue;
			}
			menuList = vilidMenuHome(menuList);
			makeMenuZip(authOrg, devType, deviceId, menuList);
		}
	}
	
	//获取机构已发布的菜单
	private List<Menu> getPublishedMenu(String orgId, String devType, String deviceId) throws Exception{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("orgId", orgId);
		params.put("deviceType", devType);
		params.put("deviceId", deviceId);
		params.put("hide", Menu.IS_NOT_HIDE);
		params.put("status", Menu.STATUS_PUBLISH);
		return menuDao.getMenus(params);
	}
	
	
	
	//判断是否存在主页，不存在默认给第一个菜单为主页
	public List<Menu> vilidMenuHome(List<Menu> menuList){
		boolean flag = false;
		for(Menu menu:menuList){
			if(menu.getHomePage().equals(Menu.IS_HOME_PAGE)){
				flag = true;
			}
		}
		if(!flag){
			menuList.get(0).setHomePage(Menu.IS_HOME_PAGE);
		}
		return menuList;
	}

	@Override
	public void clearPublish(String orgId, String devType, String deviceId) throws Exception {
		int result = updateStatus(orgId, devType, deviceId, Menu.STATUS_PUBLISH, Menu.SATAUS_UNPUBLISH, Menu.IS_NOT_HIDE);
		if(result > 0){
			String menuPath = PropertiesUtil.getRootPath()+"/"+PropertiesUtil.get("base.r2kfile")+"/"+PropertiesUtil.get("path.menu.pub")+"/"+orgId+"/"+deviceId;
			File menuDir = new File(menuPath);
			FileUtils.deleteDirectory(menuDir);
		}
	}
	
	//删除设备菜单，清空菜单文件
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteDevMenu(String orgId, String devType, String deviceId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", orgId);
		params.put("deviceType", devType);
		params.put("deviceId", deviceId);
		int result = menuDao.deleteDevMenu(params);
		if(result > 0){
			//删除发布数据
			String menuPath = PropertiesUtil.getRootPath()+"/"+PropertiesUtil.get("base.r2kfile")+"/"+PropertiesUtil.get("path.menu.pub")+"/"+orgId+"/"+deviceId;
			File menuDir = new File(menuPath);
			FileUtils.deleteDirectory(menuDir);
			//删除资源文件
			String menuResPath = PropertiesUtil.getRootPath()+"/"+PropertiesUtil.get("base.r2kfile")+"/"+PropertiesUtil.get("path.menu.res")+"/"+orgId+"/"+deviceId;
			File menuResDir = new File(menuResPath);
			FileUtils.deleteDirectory(menuResDir);
		}
		return result;
	}
	
	public static void main(String[] args) {
		String linkurl = "";
		if(!linkurl.startsWith("http://")&&!linkurl.startsWith("https://")){
			linkurl = PropertiesUtil.get("server.url") +linkurl;
		}
	}
}
	

