package com.apabi.r2k.admin.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.dao.ColumnDao;
import com.apabi.r2k.admin.model.Column;
import com.apabi.r2k.admin.model.InfoTemplate;
import com.apabi.r2k.admin.model.Picture;
import com.apabi.r2k.admin.model.ReleaseRecord;
import com.apabi.r2k.admin.service.ColumnService;
import com.apabi.r2k.admin.service.ReleaseRecordService;
import com.apabi.r2k.admin.service.TemplateService;
import com.apabi.r2k.admin.utils.ColumnType;
import com.apabi.r2k.admin.utils.ColumnUtil;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.ImageUtils;
import com.apabi.r2k.common.utils.ObjectUtil;
import com.apabi.r2k.common.utils.PropertiesUtil;

@Service("columnService")
public class ColumnServiceImpl implements ColumnService {
	@Resource
	private ColumnDao columnDao;
	@Resource
	private ReleaseRecordService releaseRecordService;
	@Resource
	private TemplateService templateService;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void addColumn(Column column) throws Exception{
		columnDao.insertColumn(column);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteColumnById(int id) throws Exception {
		columnDao.deleteById(id);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void updateColumn(Column column) throws Exception {
		columnDao.updateColumn(column);
	}

	public Column getColumnObject(int id) throws Exception{
		return columnDao.getColumnObject(id);
	}	
	public List<Column> getColumnListByPid(int parentId) throws Exception{
		return columnDao.getByPid(parentId);
	}
	
	public List<Column> getOrderList() throws Exception{
		return columnDao.getOrderList();
	}
	
	public Column getColumnById(int id) throws Exception{
		return columnDao.getColumnObject(id);
	}
	//通过parentId查询非停用信息列表
	public List<Column> getByPidInUsed(int parentId, String orgId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentId", parentId);
		map.put("orgId", orgId);
		return this.columnDao.getByPidInUsed(map);
	}
	//通过parentId查询非停用信息总数
	public int getCountByPidInUsed(int parentId, String orgId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentId", parentId);
		map.put("orgId", orgId);
		return this.columnDao.getCountByPidInUsed(map);
	}
	
	//分页查询设备栏目列表
	public Page<?> devicePageQuery(PageRequest<?> pageRequest) throws Exception {
		return columnDao.devicePageQuery(pageRequest);
	}

	public List<Column> getByPid(int parentId) throws Exception {
		return columnDao.getByPid(parentId);
	}
	public List<Column> getColumnByDevice(String deviceId) throws Exception{
		return columnDao.getColumnByDevice(deviceId);
	}
	
	//机构栏目列表分页查询
	public Page<?> orgPageQuery(PageRequest<?> pageRequest) throws Exception {
		return columnDao.orgPageQuery(pageRequest);
	}
	
	public Column getDeviceQuote(int quoteId,String deviceId,int parentId,String orgId) throws Exception{
		return columnDao.getDeviceQuote(quoteId,deviceId,parentId,orgId);
	}
	
	public List<Column> getByPidDevice(int parentId,String orgId,String deviceId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("parentId", parentId);
		map.put("orgId", orgId);
		map.put("deviceId", deviceId);
		return columnDao.getByPidDevice(map);
	}
	
	//获取所有对该栏目的引用
	public List<Column> getColumnByQuoteId(int quoteId) throws Exception{
		return columnDao.getColumnByQuoteId(quoteId);
	}
	
	//查看某一机构的欢迎页
	public Column getOrgWelcome(String orgId,String deviceType) throws Exception{
		return columnDao.getOrgWelcome(orgId,deviceType);
	}
	//查询某一设备的欢迎页
	public Column getDeviceWelcome(String orgId,String deviceId) throws Exception{
		return columnDao.getDeviceWelcome(orgId, deviceId);
	}
	//通过deviceId修改设备专题状态和link
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateQuoteColumn(int id, int status, String link) throws Exception{
		Column col = columnDao.getColumnObject(id);
		col.setStatus(status);
		col.setLink(link);
		columnDao.updateColumn(col);
	}
	
	@Override
	public void saveArt(Column column, File thumbnail, List<Picture> pictures) throws Exception{
		saveMutilPicColumn(column, thumbnail, pictures);
		if(StringUtils.equals(column.getDeviceType(), DevTypeEnum.ORG.getName())){
			return;
		}
		if(StringUtils.isNotBlank(column.getDeviceId())){
			releaseRecordService.saveDevReleaseRecord(column.getOrgId(), column.getDeviceId(), column.getId(), column.getInfoTemplate().getId());
		}else{
			releaseRecordService.saveOrgReleaseRecord(column.getOrgId(), column.getDeviceType(), column.getId(), column.getInfoTemplate().getId());
		}
	}
	
	@Override
	public void savePics(Column column,File thumbnail, List<Picture> pictures) throws Exception{
//		if(thumbnail == null && CollectionUtils.isNotEmpty(pictures)){
//			thumbnail = 
//		}
		saveMutilPicColumn(column, thumbnail, pictures);
		if(StringUtils.equals(column.getDeviceType(), DevTypeEnum.ORG.getName())){
			return;
		}
		if(StringUtils.isNotBlank(column.getDeviceId())){
//			releaseRecordService.addDevColumnsRecord(column);
			releaseRecordService.saveDevReleaseRecord(column.getOrgId(), column.getDeviceId(), column.getId(), column.getInfoTemplate().getId());
		}else{
			releaseRecordService.saveOrgReleaseRecord(column.getOrgId(), column.getDeviceType(), column.getId(), column.getInfoTemplate().getId());
		}
	}
	
	//保存多图栏目:图集、文章
	private void saveMutilPicColumn(Column column,File thumbnail, List<Picture> pictures) throws Exception{
		columnDao.insertColumn(column);
		int colid = column.getId();
		if(colid <= 0){
			throw new Exception("保存失败");
		}
		String orgid = column.getOrgId();
		String devType = column.getDeviceType();
		String deviceid = column.getDeviceId();
		String devDir = StringUtils.isBlank(deviceid) ? devType : deviceid;
		String saveDir = getImagePath(orgid, devDir, column.getId());
		if(thumbnail != null){
			String imagePath = saveDir + "/" + colid + ".jpg";
			column.setThumbnail(imagePath);
			File thumbnailPic = new File(PropertiesUtil.getRootPath()+"/"+PropertiesUtil.get("base.r2kfile")+imagePath);
			if(!thumbnailPic.getParentFile().exists()){
				thumbnailPic.getParentFile().mkdirs();
			}
			FileUtils.moveFile(thumbnail, thumbnailPic);
			FileUtils.deleteQuietly(thumbnail);
		}
		columnDao.updateColumn(column);
		List<Column> successCols = new ArrayList<Column>();
		for(Picture picture : pictures){
			Column col = createPicCol(colid, orgid, devType, deviceid, picture.getName(), picture.getDescription());
			int result = savePic(col, picture.getPath());
			if(result > 0){
				successCols.add(col);
			}
		}
		column.setColumns(successCols);
		columnDao.batchUpdateCols(successCols);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	private int savePic(Column column, String tmpPath) throws Exception{
		columnDao.savePic(column);
		int colid = column.getId();
		String saveDir = column.getImage();
		if(colid > 0){
			String fileName = colid+".jpg";
			column.setImage(column.getImage()+"/"+fileName);
			String rootPath = PropertiesUtil.getRootPath();
			File dir = new File(rootPath+"/"+PropertiesUtil.get("base.r2kfile")+saveDir);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File tmpFile = new File(rootPath+tmpPath);
			File img = new File(rootPath+"/"+PropertiesUtil.get("base.r2kfile")+column.getImage());
			img.deleteOnExit();
			FileUtils.moveFile(tmpFile, img);
			FileUtils.deleteQuietly(tmpFile);
		}
		return colid;
	}
	
	private Column createPicCol(int parentId, String orgid, String devType, String deviceid,String title, String content){
		Column column = new Column();
		column.setOrgId(orgid);
		column.setDeviceType(devType);
		column.setDeviceId(deviceid);
		column.setParentId(parentId);
		String devDir = StringUtils.isBlank(deviceid) ? devType : deviceid;
		String imgPath = getImagePath(orgid, devDir, parentId);
		column.setImage(imgPath);
		column.setType(GlobalConstant.TYPE_PICTURE);
		column.setCrtDate(new Date());
		column.setTitle(title);
		column.setContent(content);
		column.setSort(1);
		return column;
	}
	
	private String getImagePath(String orgid,String devDir,int parentId){
//		return PropertiesUtil.get("r2k.info.res.dir")+orgid+"/"+devDir+"/"+parentId+PropertiesUtil.get("r2k.info.pic.dir");
		return "/"+PropertiesUtil.get("path.info.res")+"/"+orgid+"/"+PropertiesUtil.get("path.img");
	}
	
	public void updatePics(Column column,File thumbnail, List<Integer> deletePics, List<Picture> pictures, List<Picture> newPictures) throws Exception{
		int colid = column.getId();
		String orgid = column.getOrgId();
		String devType = column.getDeviceType();
		String deviceid = column.getDeviceId();
		if(thumbnail != null){
			String devDir = StringUtils.isBlank(deviceid) ? devType : deviceid;
			String saveDir = getImagePath(orgid, devDir, column.getId());
			String imagePath = saveDir + "/"+ colid + ".jpg";
			column.setThumbnail(imagePath);
			File thumbnailPic = new File(PropertiesUtil.getRootPath()+"/"+PropertiesUtil.get("base.r2kfile")+imagePath);
			if(!thumbnailPic.getParentFile().exists()){
				thumbnailPic.getParentFile().mkdirs();
			}
			if(thumbnailPic.exists()){
				thumbnailPic.delete();
			}
			FileUtils.moveFile(thumbnail, thumbnailPic);
			FileUtils.deleteQuietly(thumbnail);
		}
		int result = columnDao.updateColumn(column);
		if(result > 0){
			if(CollectionUtils.isNotEmpty(deletePics)){
				columnDao.batchDeleteByIds(orgid,deletePics);
			}
			List<Column> successCols = new ArrayList<Column>();
			for(Picture pic : pictures){
				Column col = createUpdatePic(orgid, deviceid, devType, pic);
				successCols.add(col);
			}
			for(Picture pic : newPictures){
				Column col = createPicCol(colid, orgid, devType, deviceid, pic.getName(), pic.getDescription());
				int line = savePic(col, pic.getPath());
				if(line > 0){
					successCols.add(col);
				}
			}
			columnDao.batchUpdateCols(successCols);
		}
	}

	@Override
	public Column findPics(int id) throws Exception {
		return findColumnAndChilds(id);
	}
	
	private Column findColumnAndChilds(int id) throws Exception{
		Map params = new HashMap();
		params.put("id", id);
		List<Column> columns = columnDao.findColumnAndChilds(params);
		Column column = null;
		for(Column col : columns){
			if(col.getId() == id){
				column = col;
			}
			if(col.getType().equals(GlobalConstant.TYPE_PICTURE)){
				col.setImage("/"+PropertiesUtil.get("base.r2kfile")+col.getImage());
			}
			if(col.getType().equals(GlobalConstant.TYPE_PICTURE_COLUMNS)){
				col.setThumbnail("/"+PropertiesUtil.get("base.r2kfile")+col.getThumbnail());
			}
			ObjectUtil.resetNullValue(col);
		}
		columns.remove(column);
		column.setColumns(columns);
		return column;
	}
	
	public void saveArtCol(Column column, File image) throws Exception{
		saveSinglePicColumn(column, image);
		if(StringUtils.equals(column.getDeviceType(), DevTypeEnum.ORG.getName())){
			return;
		}
		if(StringUtils.isNotBlank(column.getDeviceId())){
			releaseRecordService.saveDevReleaseRecord(column.getOrgId(), column.getDeviceId(), column.getId(), column.getInfoTemplate().getId());
		}else{
			releaseRecordService.saveOrgReleaseRecord(column.getOrgId(), column.getDeviceType(), column.getId(), column.getInfoTemplate().getId());
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void savePic(Column column, File image) throws Exception{
		saveSinglePicColumn(column, image);
	}
	
	//保存单个图片内容：文章栏目、图片
	private void saveSinglePicColumn(Column column, File image) throws Exception{
		int result = columnDao.insertColumn(column);
		if(result <= 0){
			throw new Exception("保存失败");
		}
		if(image != null){
			int colid = column.getId();
			String deviceid = column.getDeviceId();
			String devType = column.getDeviceType();
			String devDir = StringUtils.isBlank(deviceid)?devType:deviceid;
			String saveDir = getImagePath(column.getOrgId(), devDir, column.getParentId());
			String imgPath = saveDir +"/"+ colid + ".jpg";
			String root = PropertiesUtil.getRootPath();
			column.setImage(imgPath);
			File imgDir = new File(root+"/"+PropertiesUtil.get("base.r2kfile")+imgPath);
			if(!imgDir.getParentFile().exists()){
				imgDir.getParentFile().mkdirs();
			}
			saveImage(image, imgDir);
			columnDao.updateColumn(column);
		}
	}
	
	//拷贝图片，如果图片大于配置文件中设置的大小，则压缩后再拷贝
	public void saveImage(File srcImg, File destImg) throws Exception{
		//从配置文件中读取的限制值为kb，转换为字节
		long sizeLimit = PropertiesUtil.getLong("max.filesize.publish")*1024;
		//如果大于sizeLimit设置的限制值，则压缩图片
		if(srcImg.length() > sizeLimit){
			int scale = (int) Math.ceil(srcImg.length()/sizeLimit);
			ImageUtils.scale(srcImg, destImg, scale);
		}else{
			FileUtils.deleteQuietly(destImg);
			FileUtils.copyFile(srcImg, destImg);
		}
		FileUtils.deleteQuietly(srcImg);
	}
	
	@Override
	public void updatePic(Column column, File newImage) throws Exception{
		updateSinglePicColumn(column, newImage);
	}
	
	public void updateArtCol(Column column, File newImage, int templateId) throws Exception{
		int result = updateSinglePicColumn(column, newImage);
		if(result <= 0){
			return;
		}
		if(column.getInfoTemplate() != null && column.getInfoTemplate().getId() != templateId && templateId > 0){
			if(StringUtils.isNotBlank(column.getDeviceId())){
				releaseRecordService.selectDevColTemplate(column.getOrgId(), column.getDeviceId(), column.getInfoTemplate().getSetNo(), templateId, column.getId());
			}else{
				releaseRecordService.selectDevColTemplate(column.getOrgId(), column.getDeviceType(), column.getInfoTemplate().getSetNo(), templateId, column.getId());
			}
		}
	}
	
	@Override
	public void updatePicsCol(Column column, File newImage, int templateId) throws Exception{
		int result = updateThumbnailColumn(column, newImage);
		if(result <= 0){
			return;
		}
		if(column.getInfoTemplate() != null && column.getInfoTemplate().getId() != templateId && templateId > 0){
			if(StringUtils.isNotBlank(column.getDeviceId())){
				releaseRecordService.selectDevColTemplate(column.getOrgId(), column.getDeviceId(), column.getInfoTemplate().getSetNo(), templateId, column.getId());
			}else{
				releaseRecordService.selectDevColTemplate(column.getOrgId(), column.getDeviceType(), column.getInfoTemplate().getSetNo(), templateId, column.getId());
			}
		}
	}
	
	private int updateSinglePicColumn(Column column, File newImage) throws Exception{
		String baseImgPath = "/" + PropertiesUtil.get("path.info.res") + "/" + column.getOrgId() + "/" + PropertiesUtil.get("path.img") + "/" + column.getId() + com.apabi.r2k.common.utils.FileUtils.EXT_JPG;
		if(newImage != null){
			column.setImage(baseImgPath);
		}
		int result = columnDao.updateColumn(column);
		if(result > 0 && newImage != null){
			String rootPath = PropertiesUtil.getRootPath();
			String imgPath = rootPath +"/"+ PropertiesUtil.get("base.r2kfile") + baseImgPath;
			File img = new File(imgPath);
			if(img.exists()){
				img.delete();
			}
			saveImage(newImage, img);
		}
		return result;
	}
	
	//添加机构引用并添加模板
	@Override
	public void addOrgQuoted(String orgid, String devType, int[] quoteIds, String setNo, String homeName) throws Exception {
		//所有机构公用内容
		List<Column> publicColumns = findOrgAllColumns(orgid, DevTypeEnum.ORG.getName());		
		//公用内容转成map
		Map publicMap = ColumnUtil.allColumnToMap(publicColumns);
		//获取默认模板
		Map<String, InfoTemplate> templateMap = templateService.getTemplateSet(orgid, setNo, homeName);
		List<Column> quoteColumns = new ArrayList<Column>();
		List<ReleaseRecord> records = new ArrayList<ReleaseRecord>();
		//创建所有引用内容记录及模板发布记录
		for(int quoteId : quoteIds){
			Column col = (Column) publicMap.get(quoteId);
			//创建引用记录
			Column quoteCol = createOrgQuoteCol(col, orgid, devType);
			quoteColumns.add(quoteCol);
			List<Column> columns = ColumnUtil.normalizeColumn(col);
			//从列表中移除顶级父内容，只保留子内容
			columns.remove(col);
			for (Column c : columns) {
				//创建子内容发布记录
				addToOrgReleaseRecord(c, templateMap, orgid, devType, records);
			}
		}
		columnDao.batchSaveColumns(quoteColumns);
		//创建引用内容发布记录
		for(Column quoteCol : quoteColumns){
			addToOrgReleaseRecord(quoteCol, templateMap, orgid, devType, records);
		}
		releaseRecordService.batchSaveRecords(records);
	}
	
	@Override
	public void addDevQuoted(String orgid, String deviceId, int[] quoteIds, String setNo, String homeName) throws Exception {
		//所有机构公用内容
		List<Column> publicColumns = findOrgAllColumns(orgid, DevTypeEnum.ORG.getName());		
		//公用内容转成map
		Map publicMap = ColumnUtil.allColumnToMap(publicColumns);
		//获取默认模板
		Map<String, InfoTemplate> templateMap = templateService.getTemplateSet(orgid, setNo, homeName);
		List<Column> quoteColumns = new ArrayList<Column>();
		List<ReleaseRecord> records = new ArrayList<ReleaseRecord>();
		//创建所有引用内容记录及模板发布记录
		for(int quoteId : quoteIds){
			Column col = (Column) publicMap.get(quoteId);
			//创建引用记录
			Column quoteCol = createDevQuoteCol(col, orgid, deviceId);
			quoteColumns.add(quoteCol);
			List<Column> columns = ColumnUtil.normalizeColumn(col);
			//从列表中移除顶级父内容，只保留子内容
			columns.remove(col);
			for (Column c : columns) {
				//创建子内容发布记录
				addToDevReleaseRecord(c, templateMap, orgid, deviceId, records);
			}
		}
		columnDao.batchSaveColumns(quoteColumns);
		for(Column quoteCol :quoteColumns){
			//创建引用内容发布记录
			addToDevReleaseRecord(quoteCol, templateMap, orgid, deviceId, records);
		}
		releaseRecordService.batchSaveRecords(records);
	}
	
	//添加机构发布记录
	private void addToOrgReleaseRecord(Column column, Map<String, InfoTemplate> templateMap, String orgId, String devType, List<ReleaseRecord> records){
		String tempType = ColumnType.getTemplateType(column.getType());
		InfoTemplate template = templateMap.get(tempType);
		if(column.getType().equals(Column.TYPE_WELCOME)){
			template = new InfoTemplate();
			template.setId(0);
		}
		if(template == null){
			return;
		}
		ReleaseRecord record = releaseRecordService.createOrgReleaseRecord(orgId, devType, column.getId(), template.getId());
		records.add(record);
	}
	
	//添加设备发布记录
	private void addToDevReleaseRecord(Column column, Map<String, InfoTemplate> templateMap, String orgId, String deviceId, List<ReleaseRecord> records){
		String tempType = ColumnType.getTemplateType(column.getType());
		InfoTemplate template = templateMap.get(tempType);
		if(column.getType().equals(Column.TYPE_WELCOME)){
			template = new InfoTemplate();
			template.setId(0);
		}
		if(template == null){
			return;
		}
		ReleaseRecord record = releaseRecordService.createDevReleaseRecord(orgId, deviceId, column.getId(), template.getId());
		records.add(record);
	}
	
	private List<Column> getOrgQuotedColumns(String orgid, String devType, int parentId) throws Exception{
		Map params = new HashMap();
		params.put("orgId", orgid);
		params.put("devType", devType);
		params.put("parentId", parentId);
		List<Column> quotedCols = columnDao.getOrgQuoted(params);
		return quotedCols;
	}
	
	private List<Column> getDevQuotedColumns(String orgid, String deviceId, int parentId) throws Exception{
		Map params = new HashMap();
		params.put("orgId", orgid);
		params.put("deviceId", deviceId);
		params.put("parentId", parentId);
		List<Column> quotedCols = columnDao.getDevQuoted(params);
		return quotedCols;
	}
	
	private Column getQuotedCol(List<Column> quotedCols, int quoteId){
		for(Column col : quotedCols){
			if(col.getQuoteId() == quoteId){
				return col;
			}
		}
		return null;
	}
	
	private Column createOrgQuoteCol(Column column,String orgId,String devType){
		return createQuoteCol(column, orgId, devType, null);
	}
	
	private Column createDevQuoteCol(Column column,String orgId,String deviceId){
		return createQuoteCol(column, orgId, null, deviceId);
	}
	
	private Column createQuoteCol(Column column,String orgId,String devType,String deviceid){
		Column quoteCol = new Column();
		quoteCol.setDeviceType(devType);
		quoteCol.setQuoteId(column.getId());
		quoteCol.setDeviceId(deviceid);
		quoteCol.setOrgId(orgId);
		quoteCol.setParentId(0);
		quoteCol.setCrtDate(new Date());
		quoteCol.setStatus(Column.STATUS_UNPUBLISH);
		//引用类型
		quoteCol.setType(column.getType());
		quoteCol.setSort(column.getSort());
		return quoteCol;
	}

	@Override
	public List<Column> findOrgPublishCols(String orgid, String deviceType)
			throws Exception {
		Map params = new HashMap();
		params.put("orgid", orgid);
		params.put("deviceType", deviceType);
		List<Column> columns = columnDao.findPublishCols(params);
		modifyPublishColumns(columns,orgid,deviceType);
		return columns;
	}

	@Override
	public List<Column> findDevPublishCols(String orgid, String deviceId)
			throws Exception {
		Map params = new HashMap();
		params.put("orgid", orgid);
		params.put("deviceId", deviceId);
		List<Column> columns = columnDao.findPublishCols(params);
		modifyPublishColumns(columns,orgid,deviceId);
		return columns;
	}
	
	//重新设置发布内容中的数据:图片链接加/r2kFile,添加内容link字段数据
	private void modifyPublishColumns(List<Column> columns,String orgid, String devDir){
		for(Column col : columns){
			if(StringUtils.isNotBlank(col.getThumbnail())){
				col.setThumbnail(resetLink(col.getThumbnail())); //重设缩略图的链接
			}
			if(StringUtils.isNotBlank(col.getImage())){
				col.setImage(resetLink(col.getImage()));
			}
			String tempType = ColumnType.getTemplateType(col.getType());
			col.setLink(getOutputPath(orgid, devDir, tempType, col));
		}
	}
	
	private String resetLink(String link){
		return "/"+PropertiesUtil.get("base.r2kfile") + link;
	}
	
	//获取输出路径
	private String getOutputPath(String orgid, String devDir, String tempType, Column column){
		String pubDir = "/" + PropertiesUtil.get("base.r2kfile") + "/" + PropertiesUtil.get("path.info.pub");
		String htmlName = tempType + ".html";
		StringBuilder outFilePath = new StringBuilder(pubDir + "/" + orgid + "/" + devDir + "/");
		if(column != null){
			int parentid = column.getParentId();
			int selfid = column.getId();
			if(parentid != 0){
				outFilePath.append(parentid+"/");
			}
			outFilePath.append(selfid+"/");
		}
		outFilePath.append(htmlName);
		return outFilePath.toString();
	}

	@Override
	public List<Column> findOrgAllColumns(String orgid, String deviceType)
			throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgid);
		params.put("deviceType", deviceType);
		return columnDao.findAllColumns(params);
	}

	@Override
	public List<Column> findDevAllColumns(String orgid, String deviceId)
			throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgid);
		params.put("deviceId", deviceId);
		return columnDao.findAllColumns(params);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void removeOrgColumn(String orgid, String devType, int id) throws Exception {
		List<Column> columns = findOrgAllColumns(orgid, devType);
		Column column = ColumnUtil.getColumnAndChilds(columns, id);
		if(column == null){
			return;
		}
		List delIds = new ArrayList();
		delIds.add(id);
		if(StringUtils.isNotBlank(column.getImage())){
			removeFile(column.getImage());
		}
		if(column.getQuoteId() > 0){
			List<Column> pubColumns = findOrgAllColumns(orgid, DevTypeEnum.ORG.getName());
			Map publicMap = ColumnUtil.allColumnToMap(pubColumns);
			Column tmpCol = (Column) publicMap.get(column.getQuoteId());
			if(tmpCol != null){
				column.setColumns(tmpCol.getColumns());
			}
		}
		List<Column> normalColumns = ColumnUtil.normalizeColumn(column);
		for(Column col : normalColumns){
			if(column.getQuoteId() > 0){
				continue;
			}
			delIds.add(col.getId());
			if(StringUtils.isNotBlank(col.getImage())){
				removeFile(col.getImage());
			}
		}
		releaseRecordService.deleteAllByColids(delIds);
		columnDao.deleteColumnAndQuote(delIds);
	}
	
	@Override
	public void removeDevColumn(String orgid, String deviceId, int id)
			throws Exception {
		List<Column> columns = findDevAllColumns(orgid, deviceId);
		Column column = ColumnUtil.getColumnAndChilds(columns, id);
		if(column == null){
			return;
		}
		List delIds = new ArrayList();
		delIds.add(column.getId());
		if(StringUtils.isNotBlank(column.getImage())){
			removeFile(column.getImage());
		}
		if(column.getQuoteId() > 0){
			List<Column> pubColumns = findOrgAllColumns(orgid, DevTypeEnum.ORG.getName());
			Map publicMap = ColumnUtil.allColumnToMap(pubColumns);
			Column tmpCol = (Column) publicMap.get(column.getQuoteId());
			if(tmpCol != null){
				column.setColumns(tmpCol.getColumns());
			}
		}
		List<Column> normalColumns = ColumnUtil.normalizeColumn(column);
		normalColumns.remove(column);
		for(Column col : normalColumns){
			if(column.getQuoteId() > 0){
				continue;
			}
			delIds.add(col.getId());
			if(StringUtils.isNotBlank(col.getImage())){
				removeFile(col.getImage());
			}
		}
		releaseRecordService.deleteAllByColids(delIds);
		columnDao.deleteColumnAndQuote(delIds);
	}
	
	public void removeFile(String path) throws Exception{
		String dir = PropertiesUtil.getRootPath()+"/"+PropertiesUtil.get("base.r2kfile")+path;
		FileUtils.deleteQuietly(new File(dir));
	}

	@Override
	public List<Column> getOrgChildColumns(String orgId, String devType,
			int parentId) throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgId);
		params.put("deviceType", devType);
		params.put("parentId", parentId);
		return columnDao.getChildsByPid(params);
	}

	@Override
	public List<Column> getDevChildColumns(String orgId, String deviceId,
			int parentId) throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgId);
		params.put("deviceId", deviceId);
		params.put("parentId", parentId);
		return columnDao.getChildsByPid(params);
	}

	@Override
	public Column hasWelcome(List<Column> columns) throws Exception {
		for (Column column : columns) {
			if(column.getType().equals(GlobalConstant.TYPE_WELCOME)){
				return column;
			}
		}
		return null;
	}

	@Override
	public Column findArt(int id) throws Exception {
		return findColumnAndChilds(id);
	}

	/**
	 * 预览
	 */
	@Override
	public Column getPreviewCol(String orgId, int id, String devType, String deviceId) throws Exception {
		List<Column> columns = columnDao.findPreviewColumns(id, devType, deviceId);	//获取预览的内容及其子内容
		Column parentCol = null;
		Column quoteCol = null;
		for(Column col : columns){
			if(col.getId() == id){
				if(col.getQuoteId() > 0){
					parentCol = col;
					quoteCol = findColumnAndChilds(col.getQuoteId());
					break;
				}
			}
		}
		if(quoteCol != null){
			columns = new ArrayList<Column>();
			parentCol.setTitle(quoteCol.getTitle());
			parentCol.setImage(quoteCol.getImage());
			parentCol.setThumbnail(quoteCol.getThumbnail());
			parentCol.setContent(quoteCol.getContent());
			columns.add(parentCol);
			columns.addAll(quoteCol.getColumns());
		}else{
			for(Column col : columns){
				if(StringUtils.isNotBlank(col.getImage())){
					col.setImage("/"+PropertiesUtil.get("base.r2kfile")+col.getImage());
				}
				if(StringUtils.isNotBlank(col.getThumbnail())){
					col.setThumbnail("/"+PropertiesUtil.get("base.r2kfile")+col.getThumbnail());
				}
				ObjectUtil.resetNullValue(col);
			}
		}
		Column column = ColumnUtil.mergeColumns(columns, id);		//父子内容合并
		return column;
	}

	@Override
	public void savePicsCol(Column column, File image) throws Exception {
		saveThumbnailColumn(column, image);
		if(StringUtils.equals(column.getDeviceType(), DevTypeEnum.ORG.getName())){
			return;
		}
		if(StringUtils.isNotBlank(column.getDeviceId())){
			releaseRecordService.saveDevReleaseRecord(column.getOrgId(), column.getDeviceId(), column.getId(), column.getInfoTemplate().getId());
		}else{
			releaseRecordService.saveOrgReleaseRecord(column.getOrgId(), column.getDeviceType(), column.getId(), column.getInfoTemplate().getId());
		}
	}

	/**
	 * 保存图片只有缩略图的内容(图集列表)
	 */
	private void saveThumbnailColumn(Column column, File image) throws Exception{
		int result = columnDao.insertColumn(column);
		if(result <= 0){
			throw new Exception("保存失败");
		}
		if(image != null){
			int colid = column.getId();
			String deviceid = column.getDeviceId();
			String devType = column.getDeviceType();
			String devDir = StringUtils.isBlank(deviceid)?devType:deviceid;
			String saveDir = getImagePath(column.getOrgId(), devDir, column.getParentId());
			String imgPath = saveDir +"/"+ colid + ".jpg";
			String root = PropertiesUtil.getRootPath();
			column.setThumbnail(imgPath);
			File imgDir = new File(root+"/"+PropertiesUtil.get("base.r2kfile")+imgPath);
			if(!imgDir.getParentFile().exists()){
				imgDir.getParentFile().mkdirs();
			}
			saveImage(image, imgDir);
			columnDao.updateColumn(column);
		}
	}
	
	/**
	 * 更新图片只有缩略图的项目(图集列表)
	 */
	private int updateThumbnailColumn(Column column, File newImage) throws Exception{
		int result = columnDao.updateColumn(column);
		if(result > 0 && newImage != null){
			String rootPath = PropertiesUtil.getRootPath();
			String imgPath = rootPath +"/"+ PropertiesUtil.get("base.r2kfile") + column.getThumbnail();
			File img = new File(imgPath);
			if(img.exists()){
				img.delete();
			}
			saveImage(newImage, img);
		}
		return result;
	}
	
	@Override
	public void saveWelcome(Column column, File image) throws Exception {
		saveSinglePicColumn(column, image);
		if(StringUtils.equals(column.getDeviceType(), DevTypeEnum.ORG.getName())){
			return;
		}
		if(StringUtils.isNotBlank(column.getDeviceId())){
			releaseRecordService.saveDevReleaseRecord(column.getOrgId(), column.getDeviceId(), column.getId(), 0);
		}else{
			releaseRecordService.saveOrgReleaseRecord(column.getOrgId(), column.getDeviceType(), column.getId(), 0);
		}
	}

	@Override
	public void updateWelcome(Column column, File newImage) throws Exception {
		updateSinglePicColumn(column, newImage);
	}

	@Override
	public Page quotePageQuery(PageRequest pageRequest) throws Exception {
		return columnDao.quotePageQuery(pageRequest);
	}

	@Override
	public void updateArt(Column column, File thumbnail,
			List<Integer> deletePics, List<Picture> pictures, List<Picture> newPictures, int templateId)
			throws Exception {
		int colid = column.getId();
		String orgid = column.getOrgId();
		String devType = column.getDeviceType();
		String deviceid = column.getDeviceId();
		if(thumbnail != null){
			String devDir = StringUtils.isBlank(deviceid) ? devType : deviceid;
			String saveDir = getImagePath(orgid, devDir, column.getId());
			String imagePath = saveDir + "/"+ colid + ".jpg";
			column.setThumbnail(imagePath);
			File thumbnailPic = new File(PropertiesUtil.getRootPath()+"/"+PropertiesUtil.get("base.r2kfile")+imagePath);
			if(!thumbnailPic.getParentFile().exists()){
				thumbnailPic.getParentFile().mkdirs();
			}
			if(thumbnailPic.exists()){
				thumbnailPic.delete();
			}
			FileUtils.moveFile(thumbnail, thumbnailPic);
			FileUtils.deleteQuietly(thumbnail);
		}
		int result = columnDao.updateColumn(column);
		if(result > 0){
			if(CollectionUtils.isNotEmpty(deletePics)){
				columnDao.batchDeleteByIds(orgid,deletePics);
			}
			List<Column> successCols = new ArrayList<Column>();
			for(Picture pic : pictures){
				Column col = createUpdatePic(orgid, deviceid, devType, pic);
				successCols.add(col);
			}
			for(Picture pic : newPictures){
				Column col = createPicCol(colid, orgid, devType, deviceid, pic.getName(), pic.getDescription());
				int line = savePic(col, pic.getPath());
				if(line > 0){
					successCols.add(col);
				}
			}
			columnDao.batchUpdateCols(successCols);
			if(column.getInfoTemplate() != null && column.getInfoTemplate().getId() != templateId){
				releaseRecordService.updateSingleRecord(orgid, deviceid, devType, column.getId(), templateId);
			}
		}
		
	}
	
	private Column createUpdatePic(String orgId, String deviceid, String devType, Picture pic){
		Column col = new Column();
		col.setId(pic.getId());
		col.setOrgId(orgId);
		col.setDeviceId(deviceid);
		col.setDeviceType(devType);
		col.setTitle(pic.getName());
		col.setContent(pic.getDescription());
		return col;
	}
	
	/**
	 * 删除设备资讯内容
	 */
	@Override
	public int deleteDevColumns(String orgId, String deviceId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", orgId);
		params.put("deviceId", deviceId);
		List<Column> columns = columnDao.getAllColumns(params);
		int result = columnDao.deleteDevColumn(params);
		if(result > 0){
			//删除资源文件
			String resPath = PropertiesUtil.getRootPath()+"/"+PropertiesUtil.get("base.r2kfile")+"/"+PropertiesUtil.get("path.info.res")+"/"+orgId+"/"+PropertiesUtil.get("path.img");
			for(Column col : columns){
				File resDir = new File(resPath+"/"+col.getId()+com.apabi.r2k.common.utils.FileUtils.EXT_JPG);
				FileUtils.deleteDirectory(resDir);
			}
		}
		return result;
	}
}
