package com.apabi.r2k.admin.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.xml.sax.InputSource;

import com.apabi.r2k.admin.model.Column;
import com.apabi.r2k.admin.model.Device;
import com.apabi.r2k.admin.model.InfoTemplate;
import com.apabi.r2k.admin.model.ReleaseRecord;
import com.apabi.r2k.admin.service.ColumnService;
import com.apabi.r2k.admin.service.DeviceService;
import com.apabi.r2k.admin.service.FreeMarkerService;
import com.apabi.r2k.admin.service.ReleaseRecordService;
import com.apabi.r2k.admin.service.TemplateService;
import com.apabi.r2k.admin.utils.ColumnComparator;
import com.apabi.r2k.admin.utils.ColumnUtil;
import com.apabi.r2k.common.base.ServerModelTrandsform;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.ObjectUtil;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.XmlUtil;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.dom.NodeModel;
import freemarker.template.Template;
@Scope("prototype")
@Service("freeMarkerService")
public class FreemarkerServiceImpl implements FreeMarkerService, InitializingBean {

	private Logger log = LoggerFactory.getLogger(FreemarkerServiceImpl.class);
	
	@Resource(name="freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;
	private String ftlPath = "test1";
	
	@Resource(name="templateService")
	private TemplateService templateService;
	@Resource(name="releaseRecordService")
	private ReleaseRecordService recordService;
	@Resource(name="columnService")
	private ColumnService columnService;
	@Resource(name="deviceService")
	private DeviceService deviceService;
	
	public final static String SUFFIX_PUB_NEW = ".new";		//新发布文件夹后缀
	
	//资讯发布错误消息
	public final static String ERROR_MSG_NO_HOMETEMPLATE ="首页模板不存在";
	public final static String ERROR_MSG_NO_COLUMNS = "不存在可以发布的资讯内容";
	public final static String ERROR_MSG_FAIL = "发布失败";
	public final static String ERROR_MSG_SYSTEM = "系统异常";
	//生成主页
	public void createHtml(String ftlname, String outFile, Map<String, Object> rootMap, String modelName) throws Exception{
		Writer out = null;
        try {  
        	String dir = System.getProperty("user.dir");
        	String root = dir.substring(0, dir.lastIndexOf("bin")) + "webapps";
        	String htmlPath = root + "/r2kFile/publish/" + modelName + "/";
        	File pubPath = new File(htmlPath);
        	if(!pubPath.exists()){
        		pubPath.mkdirs();
        	}
            out = new OutputStreamWriter(new FileOutputStream(htmlPath + outFile),"UTF-8");  
            
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(ftlname);  
            template.process(rootMap, out);  
        } finally {  
            if (null != out)  
                try {  
                    out.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
        }  
	}
	
	//发布公用方法
	public String publishUtil(String fileName, String outFile, String pubPath, List<Column> columnlist, List<Column> articlelist, Column welColumn) throws Exception{
		setFtlPath(pubPath);
		String serverPath = PropertiesUtil.getValue("r2k.url");
		String basePath = serverPath + "/pubtemplet/templet/" + ftlPath+"/";
		//计算页数
		String[] strs = PropertiesUtil.getValue("home.col.art.count").split("-");
		int colCount = Integer.parseInt(strs[0]);
		int artCount = Integer.parseInt(strs[1]);
		int collen = columnlist.size();
		int artlen = articlelist.size();
		int pageCount = 1;
		int colpage = (collen % colCount) == 0 ? (collen / colCount) : (collen / colCount)+1;
		int artpage = (artlen % artCount) == 0 ? (artlen / artCount) : (artlen / artCount)+1;
		
		if(colpage > artpage){
			pageCount = colpage;
		}else{
			pageCount = artpage;
		}
		
		if(pageCount == 0){
			pageCount = 1;
		}
		Map<String, Object> rootMap = new HashMap<String, Object>(); 
		rootMap.put("collist", columnlist);  
        rootMap.put("artlist", articlelist); 
        rootMap.put("welColumn", welColumn); 
        rootMap.put("templetPath", basePath);
        rootMap.put("pageCount", pageCount);
        rootMap.put("colPageSize", colCount);  
        rootMap.put("artPageSize", artCount); 
        rootMap.put("publishPath", PropertiesUtil.getValue("publish.path")+pubPath+"/");
        rootMap.put("flag", 0);		//用于标示欢迎页预览
		
        createHtml(ftlPath+"/"+fileName, outFile, rootMap, pubPath);
		return outFile;
	}
	
	//发布二级页面公用方法
	public String publishSubPageUtil(String fileName, String outName, String pubPath, List<Column> columnlist, String title) throws Exception{
		setFtlPath(pubPath);
		String templetPath = "/pubtemplet/templet/"+ftlPath+"/";
		String basePath = PropertiesUtil.getValue("r2k.url")+templetPath;
		//计算页数
		int pageCount = columnlist.size();
		Map<String, Object> rootMap = new HashMap<String, Object>(); 
		rootMap.put("collist", columnlist);  
        rootMap.put("templetPath", basePath);
        rootMap.put("pageCount", pageCount);
        rootMap.put("title", title);
        rootMap.put("colPageSize", columnlist.size());
        rootMap.put("publishPath", PropertiesUtil.getValue("publish.path")+pubPath+"/");
		
        createHtml(ftlPath+"/"+fileName, outName, rootMap, pubPath);
		return fileName;
	}
	
	//getter and setter

	public FreeMarkerConfigurer getFreeMarkerConfigurer() {
		return freeMarkerConfigurer;
	}
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.freeMarkerConfigurer = freeMarkerConfigurer;
	}

	/**
	 * 临时方法：修改ftlpath，模板生成相关方法重构后该方法失效
	 */
	private void setFtlPath(String pubPath) throws Exception{
		String orgId = pubPath;
		int inx = pubPath.indexOf("/");
		if(inx != -1){
			orgId = pubPath.substring(0, inx);
		}
		String templetPath = "/r2k/pubtemplet/templet/"+orgId+"/";
		String basePath = PropertiesUtil.getRootPath()+templetPath;
		if(new File(basePath).exists()){
			ftlPath = orgId;
		}
	}
	
/*###################################################新资讯发布#########################################################*/
	
	/**
	 * 预览
	 */
	@Override
	public void preview(Column col, String baseDir) throws Exception {
		InfoTemplate temp = col.getInfoTemplate();
		if(temp != null){
			String tempPath = "/" + PropertiesUtil.get("base.r2kfile") + temp.getPath();
			String xml = columnToXml(col, baseDir);
//			System.out.println(xml);
			Map<String, NodeModel> modelMap = new HashMap<String, NodeModel>();
			modelMap.put("doc", NodeModel.parse(new InputSource(new StringReader(xml))));
			ServletActionContext.getResponse().getOutputStream();
			Writer writer = new OutputStreamWriter(ServletActionContext.getResponse().getOutputStream(),"UTF-8");
			Template template = freeMarkerConfigurer.getConfiguration().getTemplate(tempPath);
			template.process(modelMap, writer);
		}
	}
	
	private String columnToXml(Column col,String baseDir) throws Exception{
		String columnXml = "";
		List<Column> previewCols = new ArrayList<Column>();
		previewCols.add(col);
		columnXml = ServerModelTrandsform.objectToXml(previewCols);
		Document doc = XmlUtil.getDocumentFromString(columnXml);
		Element baseDirTag = doc.getRootElement().addElement("BaseDir");
		baseDirTag.addText(baseDir);
		return doc.asXML();
	}
	
	/**
	 * 一键发布
	 */
	public void onekeyPublish(String orgid) throws Exception{
		orgPublish(orgid);
		List<Device> devices = deviceService.getDeviceListByOrgId(orgid);
		for(Device device : devices){
			devicePublish(orgid, device.getDeviceId());
		}
	}
	
	/**
	 * 机构发布
	 */
	public void orgPublish(String orgid) throws Exception{
		List<DevTypeEnum> deviceTypes = DevTypeEnum.getClientTypes();
		for(DevTypeEnum dt : deviceTypes){
			log.info("orgPublish:[orgid#"+orgid+",devType#"+dt.getName()+"]:开始发布");
			deviceTypePublish(orgid, dt.getName());
		}
	}
	
	/**
	 * 设备发布
	 */
	public List<String> devicePublish(String orgid, String deviceid) throws Exception{
		List<String> errorMsgs = new ArrayList<String>();
		String errorMsg;
		try {
			String baseDir = "/" + PropertiesUtil.get("base.r2kfile") + "/" + PropertiesUtil.get("path.info.pub") + "/" + orgid + "/" + deviceid + SUFFIX_PUB_NEW;
			InfoTemplate indexTemplate = templateService.findDevHomeTemplate(orgid, deviceid);	//获取首页模板
			if(indexTemplate == null){
				errorMsg = ERROR_MSG_NO_HOMETEMPLATE;
				errorMsgs.add(errorMsg);
				return errorMsgs;
			}
			List<Column> allColumns = columnService.findDevPublishCols(orgid, deviceid);
			if(CollectionUtils.isEmpty(allColumns)){
				completePublish(baseDir);
				return errorMsgs;
			}
			removeDir(baseDir);
			copyDir(indexTemplate, baseDir);
			List<Column> indexColumns = findParentAndChilds(allColumns, 0);				//首页发布需要的数据
			publishIndex(orgid, deviceid, indexColumns, indexTemplate, baseDir, errorMsgs);
			for(Column column : indexColumns){
				int nextPid = column.getId();
				if(column.getQuoteId() > 0){
					nextPid = column.getQuoteId();
				}
				List<Column> childColumns = findChilds(allColumns, nextPid);		//所有子内容
				column.setColumns(childColumns);
				column.setChilds(childColumns.size());
				publishDevColumn(orgid, deviceid, column, allColumns, baseDir, errorMsgs);
			}
			if(errorMsgs.size() > 0){
				FileUtils.deleteDirectory(new File(baseDir));
			}else{
				updateDevRecordStatus(orgid, deviceid);
				completePublish(baseDir);
			}
		} catch (Exception e) {
			errorMsg = ERROR_MSG_SYSTEM;
			errorMsgs.add(errorMsg);
			e.printStackTrace();
		}
		return errorMsgs;
	}

	//按设备类型发布
	@Override
	public List<String> deviceTypePublish(String orgid, String devType) throws Exception{
		List<String> errorMsgs = new ArrayList<String>();
		String errorMsg;
		try {
			String baseDir = "/" + PropertiesUtil.get("base.r2kfile") + "/" + PropertiesUtil.get("path.info.pub") + "/" + orgid + "/" + devType + SUFFIX_PUB_NEW;
			InfoTemplate indexTemplate = templateService.findOrgHomeTemplate(orgid, devType);	//获取首页模板
			if(indexTemplate == null){
				errorMsg = ERROR_MSG_NO_HOMETEMPLATE;
				errorMsgs.add(errorMsg);
				return errorMsgs;
			}
			List<Column> allColumns = columnService.findOrgPublishCols(orgid, devType);		//获取所有内容
			if(CollectionUtils.isEmpty(allColumns)){
				completePublish(baseDir);
				return errorMsgs;
			}
			removeDir(baseDir);
			copyDir(indexTemplate, baseDir);
			List<Column> indexColumns = findParentAndChilds(allColumns, 0);				//首页发布需要的数据
			publishIndex(orgid, devType, indexColumns, indexTemplate, baseDir, errorMsgs);
			for(Column column : indexColumns){
				int nextPid = column.getId();
				if(column.getQuoteId() > 0){
					nextPid = column.getQuoteId();
				}
				List<Column> childColumns = findChilds(allColumns, nextPid);		//所有子内容
				column.setColumns(childColumns);
				column.setChilds(childColumns.size());
				publishOrgColumn(orgid, devType, column, allColumns, baseDir, errorMsgs);
			}
			if(errorMsgs.size() > 0){
				FileUtils.deleteDirectory(new File(baseDir));
			}else{
				updateOrgRecordStatus(orgid, devType);
				completePublish(baseDir);
			}
		} catch (Exception e) {
			errorMsg = ERROR_MSG_SYSTEM;
			errorMsgs.add(errorMsg);
			e.printStackTrace();
		}
		return errorMsgs;
	}
	
	private void removeDir(String baseDir) throws Exception{
		FileUtils.deleteDirectory(new File(PropertiesUtil.getRootPath()+baseDir));
	}
	
	private void copyDir(InfoTemplate indexInfoTemplate, String destPath) throws Exception{
		String path = PropertiesUtil.getRootPath() + "/" + PropertiesUtil.get("base.r2kfile") + indexInfoTemplate.getPath();
		File sourceFile = new File(path);
		File dir = sourceFile.getParentFile();
		File destDir = new File(PropertiesUtil.getRootPath()+destPath);
		if(!destDir.exists()){
			destDir.mkdirs();
		}
		File[] files = dir.listFiles();
		for(File file : files){
			if(file.isDirectory()){
				String dirName = file.getName();
				File destFile = new File(destDir.getAbsolutePath()+"/"+dirName);
				destFile.mkdirs();
				FileUtils.copyDirectory(file, destFile);
			}
		}
	}
	
	//获取父内容及子内容
	private List<Column> findParentAndChilds(List<Column> columns, int parentId) throws Exception{
		Comparator<Column> comparator = new ColumnComparator();
		List<Column> newColumns = new ArrayList<Column>();
		//获取父内容
		for(Column col : columns){
			if(col.getParentId() == parentId){
				int nxtPid = col.getId();
				if(col.getQuoteId() > 0){
					nxtPid = col.getQuoteId();
				}
				List<Column> childs = findChilds(columns, nxtPid);	//获取子内容
				col.setColumns(childs);
				ObjectUtil.resetNullValue(col);
				newColumns.add(col);
			}
		}
		Collections.sort(newColumns, comparator);
		return newColumns;
	}
	
	//只获取子内容
	private List<Column> findChilds(List<Column> columns, int parentId) throws Exception{
		Comparator<Column> comparator = new ColumnComparator();
		List<Column> newColumns = new ArrayList<Column>();
		for(Column col : columns){
			if(col.getParentId() == parentId){
				ObjectUtil.resetNullValue(col);
				newColumns.add(col);
			}
		}
		Collections.sort(newColumns, comparator);
		return newColumns;
	}
	
	//发布首页
	private void publishIndex(String orgid, String dev, List<Column> columns, InfoTemplate infoTemplate,String baseDir, List<String> errorMsgs) throws Exception{
		try {
			String r2kFile = PropertiesUtil.get("base.r2kfile");
			String ftlPath = "/"+r2kFile + infoTemplate.getPath();		//模板路径
			String outFilePath = getOutputPath(orgid, dev+SUFFIX_PUB_NEW, "info");		//拼接首页生成路径
			publishTemplate(columns, ftlPath, outFilePath, baseDir);
		} catch (Exception e) {
			String errorMsg = ERROR_MSG_FAIL+":[首页模板:"+infoTemplate.getName()+"]";
			errorMsgs.add(errorMsg);
			e.printStackTrace();
		}
	}
	
	//按设备类型发布内容
	private void publishOrgColumn(String orgid, String devType, Column column, List<Column> allColumns , String baseDir, List<String> errorMsgs) throws Exception{
		InfoTemplate infoTemplate = column.getInfoTemplate();
		if(infoTemplate == null){
			return;
		}
		List<Column> columns = new ArrayList<Column>();
		columns.add(column);
		String r2kFile = "/" + PropertiesUtil.get("base.r2kfile");
		String ftlPath = r2kFile + infoTemplate.getPath();		//模板路径
		String outFilePath = ColumnUtil.getOutputPath(orgid, devType+SUFFIX_PUB_NEW, infoTemplate.getType(), column);
		try {
			log.debug("publishColumn:[内容id#"+column.getId()+",模板路径#"+ftlPath+",页面路径#"+outFilePath+"]");
			publishTemplate(columns, ftlPath, outFilePath, baseDir);
		} catch (Exception e) {
			String errorMsg = ERROR_MSG_FAIL+":[资讯:"+column.getTitle()+",模板:"+infoTemplate.getName()+"]";
			errorMsgs.add(errorMsg);
			e.printStackTrace();
		}
		List<Column> childColumns = column.getColumns();		//所有子内容
		if(CollectionUtils.isNotEmpty(childColumns)){
			for(Column col : childColumns){
				int nextPid = col.getId();
				if(col.getQuoteId() > 0){
					nextPid = col.getQuoteId();
				}
				List<Column> newChildColumns = findChilds(allColumns, nextPid);		//所有子内容
				col.setColumns(newChildColumns);
				publishOrgColumn(orgid, devType, col, allColumns, baseDir, errorMsgs);
			}
		}
	}
	
	//发布设备内容
	private void publishDevColumn(String orgid, String deviceid, Column column, List<Column> allColumns, String baseDir, List<String> errorMsgs) throws Exception{
		InfoTemplate infoTemplate = column.getInfoTemplate();
		if(infoTemplate == null){
			return;
		}
		List<Column> columns = new ArrayList<Column>();
		columns.add(column);
		String r2kFile = "/" + PropertiesUtil.get("base.r2kfile");
		String ftlPath = r2kFile + infoTemplate.getPath();		//模板路径
		String outFilePath = ColumnUtil.getOutputPath(orgid, deviceid+SUFFIX_PUB_NEW, infoTemplate.getType(), column);
		log.debug("publishColumn:[内容id#"+column.getId()+",模板路径#"+ftlPath+",页面路径#"+outFilePath+"]");
		try {
			publishTemplate(columns, ftlPath, outFilePath, baseDir);
		} catch (Exception e) {
			String errorMsg = ERROR_MSG_FAIL+":[资讯:"+column.getTitle()+",模板:"+infoTemplate.getName()+"]";
			errorMsgs.add(errorMsg);
			e.printStackTrace();
		}
		List<Column> childColumns = column.getColumns();		//所有子内容
		if(CollectionUtils.isNotEmpty(childColumns)){
			for(Column col : childColumns){
				int nextPid = col.getId();
				if(col.getQuoteId() > 0){
					nextPid = col.getQuoteId();
				}
				List<Column> newChildColumns = findChilds(allColumns, nextPid);		//所有子内容
				col.setColumns(newChildColumns);
				publishDevColumn(orgid, deviceid, col,allColumns, baseDir, errorMsgs);
			}
		}
	}
	
	//发布模板
	private void publishTemplate(List<Column> columns,String ftlPath, String outFilePath,String baseDir) throws Exception{
		String columnXml = ServerModelTrandsform.objectToXml(columns);
		columnXml = addBaseDirTag(columnXml, baseDir);
//		log.info(columnXml);
		String htmlPath = PropertiesUtil.getRootPath() + outFilePath;
		File htmlFile = new File(htmlPath);
		File dirFile = htmlFile.getParentFile();
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}
		InputSource is = new InputSource(new StringReader(columnXml));
		Map<String, NodeModel> modelMap = new HashMap<String, NodeModel>();
		modelMap.put("doc", NodeModel.parse(is));
		createHtml(modelMap, ftlPath, htmlFile);
	}
	
	private String addBaseDirTag(String sourceXml, String baseDir) throws Exception{
		Document document = XmlUtil.getDocumentFromString(sourceXml);
		Element e = document.getRootElement().addElement("BaseDir");
		e.addText(baseDir.substring(0, baseDir.indexOf(SUFFIX_PUB_NEW)));
		return document.asXML();
	}
	
	//生成html页面
	private void createHtml(Map<String, NodeModel> modelMap, String ftlPath, File htmlFile) throws Exception{
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(htmlFile),"UTF-8");
			Template template = freeMarkerConfigurer.getConfiguration().getTemplate(ftlPath);
			template.process(modelMap, writer);
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e.getCause());
		} finally{
			if(writer != null){
				writer.close();
			}
		}
	}
	
	//获取输出路径
	private String getOutputPath(String orgid, String devDir, String tempType){
		String pubDir = "/" + PropertiesUtil.get("base.r2kfile")+"/" + PropertiesUtil.get("path.info.pub")+"/";
		String htmlName = tempType + ".html";
		StringBuilder outFilePath = new StringBuilder(pubDir + orgid + "/" + devDir + "/");
		outFilePath.append(htmlName);
		return outFilePath.toString();
	}
	
	private void updateOrgRecordStatus(String orgid, String devType) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("devType", devType);
		params.put("orgid", orgid);
		updateStatus(params);
	}

	private void updateDevRecordStatus(String orgid, String deviceid) throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceid", deviceid);
		params.put("orgid", orgid);
		updateStatus(params);
	}
	
	private void updateStatus(Map<String, Object> params) throws Exception{
		Date now = new Date();
		params.put("status", ReleaseRecord.STATUS_PUBLISHED);
		params.put("releaseDate", now);
		params.put("lastDate", now);
		recordService.updateStatus(params);
	}
	
	/*
	 *完成发布：
	 *1 删除上次发布文件夹
	 *2 把新发布的.new文件夹去除.new后缀 
	 */
	private void completePublish(String baseDir) throws Exception{
		File newPubDir = new File(PropertiesUtil.getRootPath()+baseDir);
		File parentDir = newPubDir.getParentFile();
		String newPubName = newPubDir.getName();
		String pubName = newPubName.substring(0, newPubName.indexOf(SUFFIX_PUB_NEW));
		File lastPubDir = new File(parentDir, pubName);
		FileUtils.deleteDirectory(lastPubDir);	//删除上次发布的文件夹
		newPubDir.renameTo(lastPubDir);
		FileUtils.deleteDirectory(new File(PropertiesUtil.getRootPath()+baseDir));
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		TemplateLoader loader = new FileTemplateLoader(new File(PropertiesUtil.getRootPath()));
		freeMarkerConfigurer.getConfiguration().setTemplateLoader(loader);
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(new File("D:/1111").getName());
//			FileUtils.copyDirectory(new File("D:/Test/xmltest"), new File("D:/Test/test"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
