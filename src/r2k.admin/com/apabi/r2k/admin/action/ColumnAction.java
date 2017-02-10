package com.apabi.r2k.admin.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.Column;
import com.apabi.r2k.admin.model.InfoTemplate;
import com.apabi.r2k.admin.model.InfoTemplateSet;
import com.apabi.r2k.admin.model.Picture;
import com.apabi.r2k.admin.service.ColumnService;
import com.apabi.r2k.admin.service.DeviceService;
import com.apabi.r2k.admin.service.FreeMarkerService;
import com.apabi.r2k.admin.service.ReleaseRecordService;
import com.apabi.r2k.admin.service.TemplateService;
import com.apabi.r2k.admin.service.TemplateSetService;
import com.apabi.r2k.admin.utils.ColumnType;
import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.ImageUtils;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.TempTypeEnum;
import com.apabi.r2k.security.utils.SessionUtils;
import com.opensymphony.xwork2.ActionContext;

import freemarker.ext.dom.NodeModel;

@Controller("columnAction")
@Scope("prototype")
public class ColumnAction {
	private Logger log = LoggerFactory.getLogger(ColumnAction.class);
	@Resource
	private ColumnService columnService;
	
	@Resource(name="freeMarkerService")
	private FreeMarkerService freeMarkerService;
	@Autowired
	private DeviceService deviceService;
	@Resource(name="releaseRecordService")
	private ReleaseRecordService releaseRecordService;
	@Resource(name="templateService")
	private TemplateService templateService;
	@Resource(name="templateSetService")
	private TemplateSetService templateSetService;
	
	private List<Column> columnList;
	private Column column;
	private int id;
	private int status;		//0未发布 1发布
	private String newImage;
	private String oldImage;

	private int parentId;
	private String title;
	private String summary;
	private int sort;
	private String orgId;
	private String type;
	private File image;
	private String content;
	private String deviceId;
	private int quoteId;
	private boolean addWelcome; //是否允许添加欢迎页
	private String deviceName;//设备名称
	private String devType;	//设备类型
	private List<Integer> delIds;	//删除的id
	private String templateNo;
	private String fileInfos;
	private List<Picture> pictures;
	private List<Picture> newPictures;
	private InfoTemplate defaultTemplate;
	private InfoTemplateSet templateSet;
	private List<InfoTemplate> infoTemplates;
	private List<InfoTemplateSet> templateSetList;
	private Map<String, List<InfoTemplate>> defaultTemplates;
	private Map<String, String> colTempTypeMap;		//资讯内容类型对应模板类型map
	private String setNo;
	private String templateName;
	private String homeName;
	private int templateId;
	private File file;

	private int flag;				//json返回状态：：-1错误，0失败，1成功
	private String msg;				// 返回信息

	private String templetPath; 	// 模板存放的父路径
	private String publishPath; 	// 模板发布的父路径
	private List<Column> collist;	// 首页模板栏目列表
	private List<Column> artlist;	// 首页模板文章列表
	private int pageCount;			// 模板显示的页数
	private int colPageSize; 		// 模板栏目每页显示的个数
	private int artPageSize; 		// 模板文章每页显示的个数
	private Column welColumn;		//欢迎页对象
	private List<String> errorMsgs;	//发布错误消息

	private final int DEFAULT_PARENT_ID = 0;
	private Page page;
	private String actionName;		//重定向action名称
	private String devTypeName;
	private NodeModel doc;

	public String upload(){
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			String root = PropertiesUtil.getRootPath();
			//资讯文件临时存放路径：/r2k/info/res/upload
			String path = "/" + PropertiesUtil.get("base.r2kfile")+"/" + PropertiesUtil.get("path.info.res.upload") + "/" + java.util.UUID.randomUUID().toString() + ".jpg";
			File destFile = new File(root + path);
			destFile.getParentFile().mkdirs();
			//从配置文件中读取的限制值为kb，转换为字节
			long sizeLimit = PropertiesUtil.getLong("max.filesize.publish")*1024;
			//如果大于sizeLimit设置的限制值，则压缩图片
			if(file.length() > sizeLimit){
//				double scale = (double)sizeLimit/(double)file.length();
				int scale = (int) Math.ceil((double)file.length()/(double)sizeLimit);
				ImageUtils.scale(file, destFile, scale);
			}else{
				FileUtils.copyFile(file, destFile);
			}
			response.getWriter().print(path);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
	public String sort(){
		 try {
			 //更新排序
			 Column column = columnService.getColumnById(id);
				if(sort != column.getSort()){
					column.setSort(sort);
					//排序更改后，变成未发布状态
					column.setStatus(Column.STATUS_UNPUBLISH);
					columnService.updateColumn(column);
					flag = 1;
				}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "sort";
	}
	
	public String saveWelcome(){
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "saveWelcome";
	}
	
	public String updateWelcome(){
		try {
			column = columnService.getColumnById(id);
			column.setLink("/"+PropertiesUtil.get("base.r2kfile")+column.getImage());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "updateWelcome";
	}
	
	public String saveCol(){
		try {
			setParentId(id);
			infoTemplates = templateService.getColAllTemplates(setNo, TempTypeEnum.ARTICLELIST.getName());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "saveCol";
	}
	
	public String updateCol(){
		try {
//			setColumn(columnService.getColumnById(id));
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column = columnService.getColumnById(id);
				column.setLink(PropertiesUtil.get("r2k.r2kFile")+column.getImage());
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "updateCol";
	}
	
	public String saveArt(){
		try {
			setParentId(id);
			infoTemplates = templateService.getColAllTemplates(setNo, TempTypeEnum.ARTICLE.getName());
			String suffix = "";
			if(StringUtils.isNotBlank(templateName)){
				suffix = templateName.substring(TempTypeEnum.ARTICLELIST.getName().length());
			}
			String tempName = TempTypeEnum.ARTICLE.getName()+suffix;
			for(InfoTemplate temp : infoTemplates){
				if(temp.getName().equals(tempName)){
					defaultTemplate = temp;
					break;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "saveArt";
	}
	
	public String updateArt(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column = columnService.findArt(id);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "updateArt";
	}
	
	public String device() {
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				log.info("分页查询开始");
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				Map params = (Map) pageRequest.getFilters();
				params.put("parentId", DEFAULT_PARENT_ID);
				params.put("deviceId", deviceId);
				params.put("orgId", orgId);
				page = columnService.devicePageQuery(pageRequest);
//				//决定页面是否能够添加欢迎页
//				Column column = columnService.getDeviceWelcome(orgId,deviceId);
				checkHasWelcome(deviceId, pageRequest);
				
//				if(column != null){
//					addWelcome = false;
//				}else{
//					addWelcome = true;
//				}
				defaultTemplate = templateService.findDevUsedTemplate(orgId, deviceId);
				if(defaultTemplate == null){
					defaultTemplate = templateService.findDefaultTemplate(orgId, devType);
					if(defaultTemplate != null){
						releaseRecordService.addDevHomeTemplate(orgId, deviceId, defaultTemplate.getId());
					}
				}
				if(defaultTemplate != null){
					//查整套在用模板
//					List<InfoTemplate> tempList = templateService.findBySetNo(defaultTemplate.getSetNo());
					InfoTemplateSet tempset = templateSetService.getTemplateSetBySetNo(defaultTemplate.getOrgId(), defaultTemplate.getSetNo());
					defaultTemplates = templateService.getTemplateMap(tempset.getTemplates());
				}
				//查全部可用模板
//				infoTemplates = templateService.findOrgTemplateByDevType(orgId, devType);
				templateSetList = templateSetService.getTemplateSetByDevType(orgId, devType);
				colTempTypeMap = ColumnType.getColumnTemplateMap();
//				Device dev = deviceService.checkDeviceId(deviceId, orgId);
//				if(dev != null){
//					devType = dev.getDeviceTypeName();
//				}
//				ReleaseRecord releaseRecord = releaseRecordService.findDeviceRecord(orgId, deviceId);
//				if(releaseRecord != null){
//					templateNo = releaseRecord.getSetNo();
//				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "device";
	}
	
	public String org() {
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				devType = DevTypeEnum.ORG.getName();
				log.info("分页查询开始");
				showColumns(orgId, devType);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "org";
	}
	
	//横屏内容管理页面
	public String showLarge(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				devType = DevTypeEnum.AndroidLarge.getName();
				log.info("showLarge:分页查询开始");
				showColumns(orgId, devType);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "org";
	}
	
	//竖屏内容管理
	public String showPortrait(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				devType = DevTypeEnum.AndroidPortrait.getName();
				log.info("showPortrait:分页查询开始");
				showColumns(orgId, devType);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "org";
	}
	
	//Android Pad内容管理
	public String showAndroidPad(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				devType = DevTypeEnum.AndroidPad.getName();
				log.info("showAndroidPad:分页查询开始");
				showColumns(orgId, devType);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "org";
	}
	
	//Android Phone内容管理
	public String showAndroidPhone(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				devType = DevTypeEnum.AndroidPhone.getName();
				log.info("showAndroidPhone:分页查询开始");
				showColumns(orgId, devType);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "org";
	}
	
	//ipad内容管理
	public String showIPad(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				devType = DevTypeEnum.iPad.getName();
				log.info("showIPad:分页查询开始");
				showColumns(orgId, devType);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "org";
	}
	
	//iphone内容管理
	public String showIPhone(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				devType = DevTypeEnum.iPhone.getName();
				log.info("showIPhone:分页查询开始");
				showColumns(orgId, devType);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "org";
	}
	
	public void showColumns(String orgid, String deviceType) throws Exception{
		setActionName();
		devTypeName = DevTypeEnum.findName(deviceType).getValue();
		HttpServletRequest req = ServletActionContext.getRequest();
		PageRequest pageRequest= new PageRequest();
		PageRequestFactory.bindPageRequest(pageRequest,req);
		Map<String, Object> params = (Map<String, Object>) pageRequest.getFilters();
		params.put("orgId", orgid);
		params.put("deviceType", deviceType);
		page = columnService.orgPageQuery(pageRequest);
		//决定页面是否能够添加欢迎页
		checkHasWelcome(deviceType, pageRequest);
		
		if(deviceType.equals(DevTypeEnum.ORG.getName())){
			return;
		}
		defaultTemplate = templateService.findOrgUsedTemplate(orgid, deviceType);
		if(defaultTemplate == null){
			defaultTemplate = templateService.findDefaultTemplate(orgid, deviceType);
			if(defaultTemplate != null){
				releaseRecordService.addOrgHomeTemplate(orgid, deviceType, defaultTemplate.getId());
			}
		}
		if(defaultTemplate != null){
			//查整套在用模板
//			List<InfoTemplate> tempList = templateService.findBySetNo(defaultTemplate.getSetNo());
			InfoTemplateSet tempset = templateSetService.getTemplateSetBySetNo(defaultTemplate.getOrgId(), defaultTemplate.getSetNo());
			defaultTemplates = templateService.getTemplateMap(tempset.getTemplates());
		}
		//查全部可用模板
//		infoTemplates = templateService.findOrgTemplateByDevType(orgId, devType);
		templateSetList = templateSetService.getTemplateSetByDevType(orgid, devType);
		colTempTypeMap = ColumnType.getColumnTemplateMap();
	}
	
	//session中添加是否有欢迎页标记
	private void checkHasWelcome(String key, PageRequest pageRequest) throws Exception{
		Map<String, Object> session = ActionContext.getContext().getSession();
		addWelcome = true;
		if(pageRequest.getPageNumber() == 1){
			Column col = columnService.hasWelcome(page.getResult());
			if(col != null){
				session.put(key, col);
				addWelcome = false;
			}
		}else{
			Column col = (Column) session.get(key);
			if(col != null){
				addWelcome = false;
			}
		}
	}
	
	//移除session中是否有欢迎页的标记
	private void removeHasWelcome(String key){
		Map session = ActionContext.getContext().getSession();
		session.remove(key);
	}
	
	public String getChilds(){
		try {
			log.info("getChilds:[parentId#"+parentId+"]:获取所有子节点");
			columnList = columnService.getByPid(parentId);
		} catch (Exception e) {
			log.error("getChilds:[parentId#"+parentId+"]:"+e.getMessage());
			log.error(e.getMessage(),e);
		}
		return "getchilds";
	}
	
	public String getOrgChilds(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgId = SessionUtils.getOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				log.info("getOrgChilds:[parentId#"+parentId+"]:获取所有子节点");
				columnList = columnService.getOrgChildColumns(orgId, devType, parentId);
			}
		} catch (Exception e) {
			log.error("getOrgChilds:[parentId#"+parentId+"]:"+e.getMessage());
			log.error(e.getMessage(),e);
		}
		return "getchilds";
	}
	
	public String getDevChilds(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgId = SessionUtils.getOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				log.info("getDevChilds:[parentId#"+parentId+"]:获取所有子节点");
				columnList = columnService.getDevChildColumns(orgId, deviceId, parentId);
			}
		} catch (Exception e) {
			log.error("getDevChilds:[parentId#"+parentId+"]:"+e.getMessage());
			log.error(e.getMessage(),e);
		}
		return "getchilds";
	}
	
	public String articleModify()
	{
		try {
		setColumn(columnService.getColumnById(id));
		//columnService.updateColumn(column);
		return "articleModify";
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
			return "error";
		}
	}
	
	private String updateImage(int id,String path){
		String root = getRootPath();
		if(Column.TYPE_COLUMN.equals(type)){
			// 入库图片
			if (image != null) {
				try {
					if(StringUtils.isBlank(path)){
						path = getImagePath(id,null);
					}
					FileUtils.copyFile(image, new File(root + path));
				} catch (IOException e) {
					log.error(e.getMessage(),e);
				}
			}
		}else if(Column.TYPE_ARTICLE.equals(type)){
			StringBuilder imageData = new StringBuilder();
			imageData.append(oldImage);
			// 入库图片
			if(StringUtils.isNotBlank(newImage)){
				String[] srcPaths = newImage.split(";");
				for(String srcPath : srcPaths){
					if(StringUtils.isNotBlank(srcPath)){
						String fileName = srcPath.substring(srcPath.lastIndexOf("/") + 1);
						String imagePath = getImagePath(id, fileName);
						File destFile = new File(root + imagePath);
						destFile.getParentFile().mkdirs();
						File srcFile = new File(root + srcPath);
						try {
							FileUtils.copyFile(srcFile, destFile);
						} catch (IOException e) {
							log.error(e.getMessage(),e);
						}
						imageData.append(imagePath).append(";");
					}
				}
			}
			path = imageData.toString();
		}
		return path;
	}

	public String update() {
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				Column column = columnService.getColumnById(id);
				column.setTitle(title);
				column.setSummary(summary);
				column.setContent(content);
				//入库图片
				String imagePath = updateImage(id,column.getImage());
				column.setImage(imagePath);
				if(Column.TYPE_ARTICLE.equals(type)&&StringUtils.isNotBlank(imagePath)){
					String thumbnail = saveThumbnail(imagePath, id);
					column.setThumbnail(thumbnail);
				}
				//栏目和文章更改后，变成未发布状态
				column.setStatus(Column.STATUS_UNPUBLISH);
				columnService.updateColumn(column);
			}
		} catch (Exception e) {
			setMsg("更新失败");
			log.error(e.getMessage(),e);
		}
		if(StringUtils.isNotBlank(deviceId)&&!deviceId.equals("null")){
			return "devicert";
		}else{
			return "orgrt";
		}
	}
	
	private String getImagePath(int id,String fileName){
		String root = "/r2kFile/upload/image/";
		if(StringUtils.isBlank(fileName)){
			fileName = "title.jpg";
		}
		String path = null;
		if(StringUtils.isNotBlank(deviceId)){
			path = root + orgId + "/" + deviceId + "/" + id + "/" + fileName;
		}else{
			path = root + orgId + "/" + id + "/" + fileName;
		}
		return path;
	}
	
	private String getRootPath(){
		// 计算目录位置
		String dir = System.getProperty("user.dir");
		dir = dir.substring(0, dir.lastIndexOf("bin"));
		String root = dir + "webapps";
		return root;
	}
	//保存图片
	private String saveImage(int id){
		String root = getRootPath();
		String path = null;
		if (Column.TYPE_COLUMN.equals(type)) {
			try {
				// 入库图片
				if (image != null) {
					path = getImagePath(id,null);
					File destFile = new File(root + path);
					destFile.getParentFile().mkdirs();
					FileUtils.copyFile(image, destFile);
				}
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		} else if (Column.TYPE_ARTICLE.equals(type)) {
			StringBuilder imageData = new StringBuilder();
			try {
				// 入库图片
				if(StringUtils.isNotBlank(newImage)){
					String[] srcPaths = newImage.split(";");
					for(String srcPath : srcPaths){
						if(StringUtils.isNotBlank(srcPath)){
							String fileName = srcPath.substring(srcPath.lastIndexOf("/") + 1);
							String imagePath = getImagePath(id,fileName);
							File destFile = new File(root + imagePath);
							destFile.getParentFile().mkdirs();
							File srcFile = new File(root + srcPath);
							FileUtils.copyFile(srcFile, destFile);
							imageData.append(imagePath).append(";");
						}
					}
					path = imageData.toString();
				}
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		return path;
	}
	
	//加工缩略图
	private String saveThumbnail(String imagePaths,int id){
		StringBuilder thumbnailData = new StringBuilder();
		String root = getRootPath();
		String[] srcPaths = imagePaths.split(";");
		for(String srcPath : srcPaths){
			String fileName = srcPath.substring(srcPath.lastIndexOf("/") + 1);
			// 生成缩略图
			String thumbnailName = "small_" + fileName;
			String thumbnailPath = getImagePath(id, thumbnailName);
			// 按像素压缩图片
			ImageUtils.scale2(root + srcPath, root + thumbnailPath, 150, 200, true);
			thumbnailData.append(thumbnailPath).append(";");
		}
		return thumbnailData.toString();
	}
	
	
	
	public String save() {
		HttpServletRequest req = ServletActionContext.getRequest();
		orgId = SessionUtils.getAuthOrgId(req);
		if(orgId != null){
			Column column = new Column();
			column.setParentId(parentId);
			column.setStatus(Column.STATUS_UNPUBLISH);
			column.setType(type);
			column.setTitle(title);
			if(type.equals(Column.TYPE_WELCOME)){
				column.setSort(0);
			}else{
				column.setSort(1);
			}
			
			column.setSummary(summary);
			column.setContent(content);
			column.setOrgId(orgId);
			if(StringUtils.isNotBlank(deviceId)&&!deviceId.equals("null")){
				column.setDeviceId(deviceId);
			}else{
				column.setDeviceType(devType);
			}
			column.setCrtDate(new Date());
			try {
				columnService.addColumn(column);
				int id = column.getId();
				//增加链接地址
//			if(Column.TYPE_COLUMN.equals(type)){
//				String link;
//				if(StringUtils.isNotBlank(deviceId)){
//					link = "/r2kFile/publish/" + orgId + "/" + deviceId + "/" + id + ".html";
//				}else{
//					link = "/r2kFile/publish/" + orgId + "/" + id + ".html";
//				}
//				column.setLink(link);
//			}
				//存储图片
				String imagePath = saveImage(id);
				column.setImage(imagePath);
				if(Column.TYPE_ARTICLE.equals(type)&&StringUtils.isNotBlank(imagePath)){
					column.setThumbnail(saveThumbnail(imagePath, id));
				}
				columnService.updateColumn(column);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		if(StringUtils.isNotBlank(deviceId)&&!deviceId.equals("null")){
			return "devicert";
		}else{
			return "orgrt";
		}
	}

	/**
	 * 保存欢迎页
	 * @return
	 */
	public String saveWelcome1(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column.setOrgId(orgId);
				column.setSort(0);
				column.setCrtDate(new Date());
				columnService.saveWelcome(column, image);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		return getResultName("orgrt","devicert");
	}
	
	/**
	 * 更新欢迎页
	 * @return
	 */
	public String updateWelcome1(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column.setOrgId(orgId);
				columnService.updateWelcome(column, image);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		return getResultName("orgrt","devicert");
	}
	
	//添加顶级文章栏目
	public String toSaveTopCol(){
		try {
			setParentId(id);
			if(StringUtils.isNotBlank(setNo)){
				infoTemplates = templateService.getColAllTemplates(setNo, TempTypeEnum.ARTICLELIST.getName());
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "saveCol";
	}
	
	//添加子文章栏目
	public String toSaveCol(){
		try {
			setParentId(id);
			if(templateId != 0){
				defaultTemplate = templateService.getById(templateId);
				infoTemplates = new ArrayList<InfoTemplate>();
				infoTemplates.add(defaultTemplate);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "saveCol";
	}
	
	/**
	 * 保存文章栏目
	 * @return
	 */
	public String saveArtCol(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column.setOrgId(orgId);
				column.setSort(1);
				column.setCrtDate(new Date());
				columnService.saveArtCol(column, image);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		return getResultName("orgrt","devicert");
	}
	
	/**
	 * 跳转修改顶级文章栏目页面
	 */
	public String toUpdateTopArtCol(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column = columnService.getColumnById(id);
				if(column != null){
					column.setLink("/"+PropertiesUtil.get("base.r2kfile")+column.getImage());
					if(StringUtils.isNotBlank(setNo)){
						String tempType = ColumnType.getTemplateType(column.getType());
						infoTemplates = templateService.getColAllTemplates(setNo, tempType);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "updateCol";
	}
	
	/**
	 * 跳转修改文章栏目页面
	 */
	public String toUpdateArtCol(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column = columnService.getColumnById(id);
				if(column != null){
					column.setLink("/"+PropertiesUtil.get("base.r2kfile")+column.getImage());
					if(column.getInfoTemplate() != null){
						infoTemplates = new ArrayList<InfoTemplate>();
						infoTemplates.add(column.getInfoTemplate());
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "updateCol";
	}
	
	/**
	 * 更新文章栏目
	 * @return
	 */
	public String updateArtCol(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column.setOrgId(orgId);
				columnService.updateArtCol(column, image, templateId);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		return getResultName("orgrt","devicert");
	}
	
	public String toSaveTopArt(){
		try {
			setParentId(id);
			if(StringUtils.isNotBlank(setNo)){
				infoTemplates = templateService.getColAllTemplates(setNo, TempTypeEnum.ARTICLE.getName());
//				String tempName = TempTypeEnum.ARTICLE.getName();
//				for(InfoTemplate temp : infoTemplates){
//					if(temp.getName().equals(tempName)){
//						defaultTemplate = temp;
//						break;
//					}
//				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "saveArt";
	}
	
	public String toSaveArt(){
		try {
			setParentId(id);
			if(StringUtils.isNotBlank(setNo)){
				infoTemplates = templateService.getColAllTemplates(setNo, TempTypeEnum.ARTICLE.getName());
				String suffix = "";
				if(StringUtils.isNotBlank(templateName)){
					suffix = templateName.substring(TempTypeEnum.ARTICLELIST.getName().length());
				}
				String tempName = TempTypeEnum.ARTICLE.getName()+suffix;
				for(InfoTemplate temp : infoTemplates){
					if(temp.getName().equals(TempTypeEnum.ARTICLE.getName())){
						defaultTemplate = temp;
					}
					if(temp.getName().equals(tempName)){
						defaultTemplate = temp;
						break;
					}
				}
				infoTemplates = null;
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "saveArt";
	}
	
	/**
	 * 保存文章
	 * @return
	 */
	public String saveArt1(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column.setSort(1);
				column.setCrtDate(new Date());
				column.setOrgId(orgId);
				if(pictures == null){
					pictures = new ArrayList<Picture>();
				}
				columnService.saveArt(column,image, pictures);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		return getResultName("orgrt","devicert");
	}
	
	public String toUpdateTopArt(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column = columnService.findArt(id);
				if(column != null){
					if(StringUtils.isNotBlank(setNo)){
						String tempType = ColumnType.getTemplateType(column.getType());
						infoTemplates = templateService.getColAllTemplates(setNo, tempType);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "updateArt";
	}
	
	public String updateArt1(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column.setOrgId(orgId);
				if(pictures == null){
					pictures = new ArrayList<Picture>();
				}
				if(newPictures == null){
					newPictures = new ArrayList<Picture>();
				}
				columnService.updateArt(column, image, delIds, pictures, newPictures, templateId);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		return getResultName("orgrt","devicert");
	}
	
	/**
	 * 跳转保存顶级图集列表页
	 * @return
	 */
	public String toSaveTopPicsCol(){
		try {
			setParentId(id);
			if(StringUtils.isNotBlank(setNo)){
				infoTemplates = templateService.getColAllTemplates(setNo, TempTypeEnum.PICTUREGROUP.getName());
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "toSavePicsCol";
	}
	
	/**
	 * 跳转保存图集列表页
	 * @return
	 */
	public String toSavePicsCol(){
		try {
			setParentId(id);
			if(templateId != 0){
				defaultTemplate = templateService.getById(templateId);
				infoTemplates = new ArrayList<InfoTemplate>();
				infoTemplates.add(defaultTemplate);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "toSavePicsCol";
	}
	
	public String toSaveTopPics(){
		try {
			setParentId(id);
			if(StringUtils.isNotBlank(setNo)){
				infoTemplates = templateService.getColAllTemplates(setNo, TempTypeEnum.PICTURELIST.getName());
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "toSavePics";
	}
	
	public String savePicsCol(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column.setSort(1);
				column.setCrtDate(new Date());
				column.setOrgId(orgId);
				columnService.savePicsCol(column, image);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		return getResultName("orgrt","devicert");
	}
	
	/**
	 * 跳转修改顶级图集列表页面
	 */
	public String toUpdateTopPicsCol(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column = columnService.getColumnById(id);
				if(column != null){
					column.setLink("/"+PropertiesUtil.get("base.r2kfile")+column.getThumbnail());
					if(StringUtils.isNotBlank(setNo)){
						String tempType = ColumnType.getTemplateType(column.getType());
						infoTemplates = templateService.getColAllTemplates(setNo, tempType);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "toUpdatePicsCol";
	}
	
	/**
	 * 修改图集列表
	 */
	public String updatePicsCol(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column.setOrgId(orgId);
				columnService.updatePicsCol(column, image, templateId);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		return getResultName("orgrt","devicert");
	}
	
	/**
	 * 跳转创建新图集页面
	 */
	public String toSavePics(){
		try {
			setParentId(id);
			if(StringUtils.isNotBlank(setNo)){
				infoTemplates = templateService.getColAllTemplates(setNo, TempTypeEnum.PICTURELIST.getName());
				String suffix = "";
				if(StringUtils.isNotBlank(templateName)){
					suffix = templateName.substring(TempTypeEnum.PICTUREGROUP.getName().length());
				}
				String tempName = TempTypeEnum.PICTURELIST.getName()+suffix;
				for(InfoTemplate temp : infoTemplates){
					if(temp.getName().equals(TempTypeEnum.PICTURELIST.getName())){
						defaultTemplate = temp;
					}
					if(temp.getName().equals(tempName)){
						defaultTemplate = temp;
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "toSavePics";
	}
	
	/**
	 * 保存图集
	 */
	public String savePics(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column.setSort(1);
				column.setCrtDate(new Date());
				column.setOrgId(orgId);
				if(pictures == null){
					pictures = new ArrayList<Picture>();
				}
				columnService.savePics(column,image, pictures);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		return getResultName("orgrt","devicert");
	}
	
	/**
	 * 跳转修改顶级图集页面
	 */
	public String toUpdateTopPics(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column = columnService.findPics(id);
				if(column != null){
					column.setLink("/"+PropertiesUtil.get("base.r2kfile")+column.getThumbnail());
					if(StringUtils.isNotBlank(setNo)){
						String tempType = ColumnType.getTemplateType(column.getType());
						infoTemplates = templateService.getColAllTemplates(setNo, tempType);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "toUpdatePics";
	}
	
	/**
	 * 跳转修改图集页面
	 */
	public String toUpdatePics(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column = columnService.findPics(id);
				if(column != null){
					column.setLink("/"+PropertiesUtil.get("base.r2kfile")+column.getThumbnail());
					if(column.getInfoTemplate() != null){
						infoTemplates = new ArrayList<InfoTemplate>();
						infoTemplates.add(column.getInfoTemplate());
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "toUpdatePics";
	}
	
	/**
	 * 修改图集
	 */
	public String updatePics(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column.setOrgId(orgId);
				if(pictures == null){
					pictures = new ArrayList<Picture>();
				}
				if(newPictures == null){
					newPictures = new ArrayList<Picture>();
				}
				columnService.updatePics(column,image, delIds, pictures, newPictures);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		return getResultName("orgrt","devicert");
	}
	
	public String toSavePic(){
		setParentId(id);
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "toSavePic";
	}
	
	public String savePic(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column.setOrgId(orgId);
				column.setSort(1);
				column.setCrtDate(new Date());
				columnService.savePic(column, image);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		return getResultName("orgrt","devicert");
	}
	
	/**
	 * 跳转修改图片页面
	 */
	public String toUpdatePic(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				setParentId(id);
				column = columnService.getColumnById(id);
				if(column != null){
					column.setLink("/"+PropertiesUtil.get("base.r2kfile")+column.getImage());
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		if(StringUtils.isBlank(deviceId)){
			devTypeName = DevTypeEnum.findName(devType).getValue();
		}
		return "toUpdatePic";
	}
	
	/**
	 * 修改图片
	 */
	public String updatePic(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				column.setOrgId(orgId);
				columnService.updatePic(column, image);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		return getResultName("orgrt","devicert");
	}
	
	private String getResultName(String orgrt, String devrt){
		String result = orgrt;
		if(StringUtils.isNotBlank(deviceId)){
			result=devrt;
		}
		return result;
	}
	
	// 预览页面
	public String preview() {
		String result = null;
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				String baseDir = "/" + PropertiesUtil.get("base.r2kfile") + "/" + PropertiesUtil.get("path.info.pub") + "/" + orgId + "/" + devType;
				Column col = columnService.getPreviewCol(orgId, id, devType, deviceId);
				if(col != null){
					freeMarkerService.preview(col, baseDir);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}
	// 按设备预览页面
	public String previewByDevice() {
		String result = null;
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				String baseDir = "/" + PropertiesUtil.get("base.r2kfile") + "/" + PropertiesUtil.get("path.info.pub") + "/" + orgId + "/" + deviceId;
				Column col = columnService.getPreviewCol(orgId, id, devType, deviceId);
				if(col != null){
					freeMarkerService.preview(col, baseDir);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}
	
//	private String columnToXml(Column col,String baseDir) throws Exception{
//		String columnXml = "";
//		List<Column> previewCols = new ArrayList<Column>();
//		previewCols.add(col);
//		columnXml = ServerModelTrandsform.objectToXml(previewCols);
//		Document doc = XmlUtil.getDocumentFromString(columnXml);
//		Element baseDirTag = doc.getRootElement().addElement("BaseDir");
//		baseDirTag.addText(baseDir);
//		return doc.asXML();
//	}
//	
//	private void preview(Column col, String baseDir) throws Exception{
//		InfoTemplate temp = col.getInfoTemplate();
//		if(temp != null){
//			String tempPath = "/" + PropertiesUtil.get("base.r2kfile") + temp.getPath();
//			String xml = columnToXml(col, baseDir);
////			System.out.println(xml);
//			Map<String, NodeModel> modelMap = new HashMap<String, NodeModel>();
//			modelMap.put("doc", NodeModel.parse(new InputSource(new StringReader(xml))));
//			ServletActionContext.getResponse().getOutputStream();
//			Writer writer = new OutputStreamWriter(ServletActionContext.getResponse().getOutputStream(),"UTF-8");
//			Template template = freeMarkerConfigurer.getConfiguration().getTemplate(tempPath);
//			template.process(modelMap, writer);
//		}
//	}
	//发布页面
	public String publish(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				errorMsgs = freeMarkerService.deviceTypePublish(orgId, devType);
				if(errorMsgs.size() > 0){
					flag = 0;
				}else{
					
					this.flag = 1;
				}
			}
		} catch (Exception e) {
			this.flag = 0;
			log.error(e.getMessage(),e);
		}
		setActionName();
		return "publish";
	}
	//按设备id发布页面
	public String publishByDevice(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
//				flag = publishDeviceUtil(orgId, deviceId);
				errorMsgs = freeMarkerService.devicePublish(orgId, deviceId);
				if(errorMsgs.size() > 0){
					flag = 0;
				}else{
					
					this.flag = 1;
				}
				if(flag == 0){
					return "error";
				}
			}
		} catch (Exception e) {
			flag = 0;
			log.error(e.getMessage(),e);
		}
		return "publishByDevice";
	}
	
	//一键发布
	public String publishAll(){
		publish();
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
//				List<Device> dlist = this.deviceService.getDeviceListByOrgId(orgId);
//				for(int i = 0, len = dlist.size(); i < len; i++){
//					flag = this.publishDeviceUtil(orgId, dlist.get(i).getDeviceId());
//					if(flag == 0){
//						break;
//					}
//				}
				freeMarkerService.onekeyPublish(orgId);
			}
		} catch (Exception e) {
			flag = 0;
			log.error(e.getMessage(),e);
		}
		return "publishAll";
	}
	
	/**
	 * 设备发布
	 * @param orgId
	 * @param deviceId
	 * @return
	 */
//	private int publishDeviceUtil(String orgId, String deviceId) throws Exception{
//		if(deviceId != null){
//			columnList = this.columnService.getByPidDevice(PARENT_ID_HOME, orgId, deviceId);
//			if(columnList != null && columnList.size() > 0){
//				//创建二级页面
//				for(int i = 0, len = columnList.size(); i < len; i++){
//					Column col = columnList.get(i);
//					if(col != null){
//						List<Column> subList = new ArrayList<Column>();
//						String type = col.getType();
//						if(type.equals("10")||type.equals("11")||type.equals("12")){
//							subList = columnService.getByPidInUsed(col.getQuoteId(), orgId);
//						}else{
//							subList = this.columnService.getByPidDevice(col.getId(), orgId,deviceId);
//						}
//						if(subList != null && subList.size() > 0){
//							String title = col.getTitle() != null ? col.getTitle() : ""; 
//							this.freeMarkerService.publishSubPageUtil("subpage.ftl", col.getId()+".html", col.getOrgId()+"/"+deviceId, subList, title);
//							for(Column cc : subList){
//								if(cc != null && cc.getStatus() != this.STATUS_UNUSED && cc.getStatus() != this.STATUS_PUBLISH){
//									//更新二级页面的发布状态
//									this.columnService.updateQuoteColumn(cc.getId(), this.STATUS_PUBLISH, null);
//								}
//							}
//							//更新父节点的链接地址
//							String linkPath = PropertiesUtil.getValue("publish.path") + col.getOrgId()+"/"+deviceId+"/"+col.getId()+".html";
//							this.columnService.updateQuoteColumn(col.getId(), this.STATUS_PUBLISH, linkPath);
//						}
//					}
//				}
//				//创建主页
//				createHomeHtml(columnList, orgId+"/"+deviceId);
//			}else{
//				//数据不存在删除目录
//				String dir = System.getProperty("user.dir");
//	        	String root = dir.substring(0, dir.lastIndexOf("bin")) + "webapps";
//				String linkPath = root + PropertiesUtil.getValue("publish.path") + orgId+"/"+deviceId;   
//				FileUtils.deleteDirectory(new File(linkPath));   
//			}
//			this.flag = 1;
//		}else{
//			this.flag = 0;
//		}
//		return flag;
//	}
	
	//查看首页
	public String showHome(){
		try {
			
			String root = PropertiesUtil.getValue("r2k.url");
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
//			String rootpath = root.substring(0,root.lastIndexOf("/"));
//			String publishUrl = PropertiesUtil.getValue("publish.path");
//			String publishIndex = PropertiesUtil.getValue("publish.index");
//			String publishWelcome = PropertiesUtil.getValue("publish.welcome");
//			String publishFilePath = null;
			if(StringUtils.isNotBlank(orgId)){
				String devDir = StringUtils.isNotBlank(deviceId)?deviceId:devType;
				publishPath = PropertiesUtil.get("url.base.r2kfile") + "/" + PropertiesUtil.get("path.info.pub") + "/" + orgId + "/" + devDir + "/info.html";
//				if(StringUtils.isNotBlank(deviceId)){
//					this.publishPath = rootpath + publishUrl + orgId + "/" + deviceId + publishIndex;
//					publishFilePath = PropertiesUtil.getRootPath() + publishUrl + orgId + "/" + deviceId + publishIndex;
//				}else{
//					this.publishPath = rootpath + publishUrl + orgId + publishIndex;;
//					publishFilePath = PropertiesUtil.getRootPath() + publishUrl + orgId + publishIndex;
//				}
//				File indexFile = new File(publishFilePath);
//				if(!(indexFile.exists() && indexFile.isFile())){
//					publishPath = rootpath + publishUrl + publishWelcome;
//				}
			}else{
				return "error";
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "show_home";
	}

	// 创建主页html
//	public String createHomeHtml(List<Column> columnList, String pubpath) throws Exception{
//		String result = null;
//		// 数据转化：将父节点为0的columnList按顺序转化为collist和artlist
//		collist = new ArrayList<Column>();
//		artlist = new ArrayList<Column>();
//		this.column = null;
//		for (int i = 0, len = columnList.size(); i < len; i++) {
//			Column column = columnList.get(i);
//			if (column != null) {
//				// 类型：0为栏目，1为文章，2为欢迎页
//				String type = column.getType();
//				if (Column.TYPE_COLUMN.equals(type)||Column.TYPE_QUOTE_COLUMN.equals(type)) {
//					collist.add(column);
//				} else if (Column.TYPE_ARTICLE.equals(type)||Column.TYPE_QUOTE_ARTICLE.equals(type)) {
//					artlist.add(column);
//				}else if (Column.TYPE_WELCOME.equals(type)||Column.TYPE_QUOTE_WELCOME.equals(type)) {
//					this.column = column;
//				}
//				//更新发布状态
//				if(column.getStatus() != this.STATUS_UNUSED && column.getStatus() != this.STATUS_PUBLISH){
//					this.columnService.updateQuoteColumn(column.getId(), this.STATUS_PUBLISH, null);
//				}
//			}
//		}
//		result = this.freeMarkerService.publishUtil("index.ftl", "index.html", pubpath, collist, artlist, this.column);
//		return result;
//	}

	/**
	 * 预览公用方法
	 * @param fileName	模板文件名(无后缀：默认后缀为.ftl)
	 * @param outSuffix 发布文件名后缀
	 * @param ftlPath	模板路径
	 * @param columnList数据
	 * @return
	 * @throws Exception
	 */
//	private String previewUtil(String fileName, String outSuffix, String ftlPath, String pubPath,
//			List<Column> columnList, String modelColArtCount, String type, int id) throws Exception {
//		// 填充数据
//		List<Column> columnlist = new ArrayList<Column>();
//		List<Column> articlelist = new ArrayList<Column>();
//		for (Column column : columnList) {
//			if (column != null) {
//				// 类型：0为栏目，1为文章
//				String nodetype = column.getType();
//				if (Column.TYPE_COLUMN.equals(nodetype) || Column.TYPE_QUOTE_COLUMN.equals(nodetype)) {
//					columnlist.add(column);
//				} else if (Column.TYPE_ARTICLE.equals(nodetype) || Column.TYPE_QUOTE_ARTICLE.equals(nodetype)) {
//					articlelist.add(column);
//				} else if (Column.TYPE_WELCOME.equals(nodetype) || Column.TYPE_QUOTE_WELCOME.equals(nodetype)) {
//					this.welColumn = column;
//				}
//			}
//		}
//		String serverPath = PropertiesUtil.getValue("r2k.url");
//		String basePath = serverPath+"/pubtemplet/templet/"+ftlPath+"/";
//
//		// 计算页数
//		String[] strs = PropertiesUtil.getValue(modelColArtCount).split("-");
//		int colCount = Integer.parseInt(strs[0]);	//获取模板中栏目的个数
//		int artCount = Integer.parseInt(strs[1]);	//获取模板中文章的个数
//		int collen = columnlist.size();
//		int artlen = articlelist.size();
//		// 预览对象所在页
//		int pageIndex = 1;
//		if (type != null) {
//			// 获取预览对象在集合中的下标
//			int index = 0;
//			if (Column.TYPE_COLUMN.equals(type) || Column.TYPE_QUOTE_COLUMN.equals(type)) {
//				for (int i = 0; i < collen; i++) {
//					Column cc = columnlist.get(i);
//					if (cc != null && cc.getId() == id) {
//						index = i + 1;
//						break;
//					}
//				}
//				// 获取预览对象所在页
//				if (index != 0) {
//					pageIndex = (index % colCount) == 0 ? (index / colCount): (index / colCount) + 1;
//				}
//				this.flag = 0;
//			} else if (Column.TYPE_ARTICLE.equals(type) || Column.TYPE_QUOTE_ARTICLE.equals(type)) {
//				for (int i = 0; i < artlen; i++) {
//					Column cc = articlelist.get(i);
//					if (cc != null && cc.getId() == id) {
//						index = i + 1;
//						break;
//					}
//				}
//				// 获取预览对象所在页
//				if (index != 0) {
//					pageIndex = (index % artCount) == 0 ? (index / artCount) : (index / artCount) + 1;
//				}
//				this.flag = 0;
//			} else if (Column.TYPE_WELCOME.equals(type) || Column.TYPE_QUOTE_WELCOME.equals(type)) {
//				this.flag = 1;		//用于标示欢迎页预览
//			}
//		}
//		this.collist = new ArrayList<Column>();
//		int end = (colCount * pageIndex >= collen) ? collen : (colCount * pageIndex);
//		for (int i = colCount * (pageIndex - 1); i < end; i++) {
//			collist.add(columnlist.get(i));
//		}
//		this.artlist = new ArrayList<Column>();
//		end = (artCount * pageIndex >= artlen) ? artlen : (artCount * pageIndex);
//		for (int i = artCount * (pageIndex - 1); i < end; i++) {
//			artlist.add(articlelist.get(i));
//		}
//		this.templetPath = basePath;
//		this.publishPath = serverPath + "/pubtemplet/publish/"+pubPath+"/";
//		this.pageCount = 1;
//		this.colPageSize = colCount;
//		this.artPageSize = artCount;
//		if(this.flag != 1){
//			if(this.collist != null || this.artlist != null){
//				this.welColumn = null;
//			}
//		}
//		return fileName + "_" + this.CREATE_TYPE_FTL;
//	}

	// getter and setter

	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}

	public List<Column> getColumnList() {
		return columnList;
	}

	public String deleteColumn() {
		try {
			this.columnService.deleteColumnById(id);
			return null;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return "error";
		}
	}
	
	public String deleteOrgColumn(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgid = SessionUtils.getOrgId(req);
			if(StringUtils.isNotBlank(orgid)){
				columnService.removeOrgColumn(orgid, devType, id);
				if(type.equals(Column.TYPE_WELCOME)){
					removeHasWelcome(devType);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}

	public String deleteDevColumn(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgid = SessionUtils.getOrgId(req);
			if(StringUtils.isNotBlank(orgid)){
				columnService.removeDevColumn(orgid, deviceId, id);
				if(type.equals(Column.TYPE_WELCOME)){
					removeHasWelcome(deviceId);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}

	//查看当前机构是否生成首页
	public String checkPublish(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			String publishUrl = PropertiesUtil.getValue("publish.path");
			String publishIndex = PropertiesUtil.getValue("publish.index");
			String path = null;
			if(deviceId != null){
				path = PropertiesUtil.getRootPath()+"/"+publishUrl+orgId+"/"+deviceId+publishIndex;
			}else{
				path = PropertiesUtil.getRootPath()+"/"+publishUrl+orgId+"/"+publishIndex;
			}
			File indexFile = new File(path);
			if(indexFile.exists() && indexFile.isFile()){
				flag = 1;
			}else{
				flag = 0;
			}
		} catch (Exception e) {
			flag = -1;
			log.error(e.getMessage(),e);
		}
		return "ispublish";
	}
	
	private void setActionName(){
		if(StringUtils.isNotBlank(deviceId)){
			actionName = "device";
		} else if(devType.equals(DevTypeEnum.ORG.getName())){
			actionName = "org";
		} else if(devType.equals(DevTypeEnum.AndroidLarge.getName())){
			actionName = "showLarge";
		} else if(devType.equals(DevTypeEnum.AndroidPortrait.getName())){
			actionName = "showPortrait";
		} else if(devType.equals(DevTypeEnum.AndroidPad.getName())){
			actionName = "showAndroidPad";
		} else if(devType.equals(DevTypeEnum.AndroidPhone.getName())){
			actionName = "showAndroidPhone";
		} else if(devType.equals(DevTypeEnum.iPad.getName())){
			actionName = "showIPad";
		} else if(devType.equals(DevTypeEnum.iPhone.getName())){
			actionName = "showIPhone";
		}
	}
	
	public ColumnService getColumnService() {
		return columnService;
	}

	public void setColumnService(ColumnService columnService) {
		this.columnService = columnService;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public FreeMarkerService getFreeMarkerService() {
		return freeMarkerService;
	}

	public void setFreeMarkerService(FreeMarkerService freeMarkerService) {
		this.freeMarkerService = freeMarkerService;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTempletPath() {
		return templetPath;
	}

	public void setTempletPath(String templetPath) {
		this.templetPath = templetPath;
	}

	public List<Column> getCollist() {
		return collist;
	}

	public void setCollist(List<Column> collist) {
		this.collist = collist;
	}

	public List<Column> getArtlist() {
		return artlist;
	}

	public void setArtlist(List<Column> artlist) {
		this.artlist = artlist;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getColPageSize() {
		return colPageSize;
	}

	public void setColPageSize(int colPageSize) {
		this.colPageSize = colPageSize;
	}

	public int getArtPageSize() {
		return artPageSize;
	}

	public void setArtPageSize(int artPageSize) {
		this.artPageSize = artPageSize;
	}

	public String getPublishPath() {
		return publishPath;
	}

	public void setPublishPath(String publishPath) {
		this.publishPath = publishPath;
	}

	public Column getWelColumn() {
		return welColumn;
	}

	public void setWelColumn(Column welColumn) {
		this.welColumn = welColumn;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getNewImage() {
		return newImage;
	}

	public void setNewImage(String newImage) {
		this.newImage = newImage;
	}

	public void setOldImage(String oldImage) {
		this.oldImage = oldImage;
	}

	public String getOldImage() {
		return oldImage;
	}
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public void setImage(File image) {
		this.image = image;
	}
	public File getImage() {
		return image;
	}
	public DeviceService getDeviceService() {
		return deviceService;
	}
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	public int getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(int quoteId) {
		this.quoteId = quoteId;
	}

	public void setAddWelcome(boolean addWelcome) {
		this.addWelcome = addWelcome;
	}

	public boolean isAddWelcome() {
		return addWelcome;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public List<Integer> getDelIds() {
		return delIds;
	}

	public void setDelIds(List<Integer> delIds) {
		this.delIds = delIds;
	}
	public String getTemplateNo() {
		return templateNo;
	}

	public void setTemplateNo(String templateNo) {
		this.templateNo = templateNo;
	}

	public String getFileInfos() {
		return fileInfos;
	}

	public void setFileInfos(String fileInfos) {
		this.fileInfos = fileInfos;
	}

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	public InfoTemplate getDefaultTemplate() {
		return defaultTemplate;
	}

	public void setDefaultTemplate(InfoTemplate defaultTemplate) {
		this.defaultTemplate = defaultTemplate;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public List<InfoTemplate> getInfoTemplates() {
		return infoTemplates;
	}

	public void setInfoTemplates(List<InfoTemplate> infoTemplates) {
		this.infoTemplates = infoTemplates;
	}

	public String getSetNo() {
		return setNo;
	}

	public void setSetNo(String setNo) {
		this.setNo = setNo;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public NodeModel getDoc() {
		return doc;
	}

	public void setDoc(NodeModel doc) {
		this.doc = doc;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Map<String, List<InfoTemplate>> getDefaultTemplates() {
		return defaultTemplates;
	}

	public void setDefaultTemplates(Map<String, List<InfoTemplate>> defaultTemplates) {
		this.defaultTemplates = defaultTemplates;
	}

	public Map<String, String> getColTempTypeMap() {
		return colTempTypeMap;
	}

	public void setColTempTypeMap(Map<String, String> colTempTypeMap) {
		this.colTempTypeMap = colTempTypeMap;
	}

	public List<String> getErrorMsgs() {
		return errorMsgs;
	}

	public void setErrorMsgs(List<String> errorMsgs) {
		this.errorMsgs = errorMsgs;
	}

	public String getHomeName() {
		return homeName;
	}

	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}

	public List<Picture> getNewPictures() {
		return newPictures;
	}

	public void setNewPictures(List<Picture> newPictures) {
		this.newPictures = newPictures;
	}

	public InfoTemplateSet getTemplateSet() {
		return templateSet;
	}

	public void setTemplateSet(InfoTemplateSet templateSet) {
		this.templateSet = templateSet;
	}

	public TemplateSetService getTemplateSetService() {
		return templateSetService;
	}

	public void setTemplateSetService(TemplateSetService templateSetService) {
		this.templateSetService = templateSetService;
	}

	public List<InfoTemplateSet> getTemplateSetList() {
		return templateSetList;
	}

	public void setTemplateSetList(List<InfoTemplateSet> templateSetList) {
		this.templateSetList = templateSetList;
	}

	public String getDevTypeName() {
		return devTypeName;
	}

	public void setDevTypeName(String devTypeName) {
		this.devTypeName = devTypeName;
	}

}
