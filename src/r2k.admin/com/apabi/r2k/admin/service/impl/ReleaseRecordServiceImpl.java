package com.apabi.r2k.admin.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.apabi.r2k.admin.dao.ReleaseRecordDao;
import com.apabi.r2k.admin.model.Column;
import com.apabi.r2k.admin.model.InfoTemplate;
import com.apabi.r2k.admin.model.ReleaseRecord;
import com.apabi.r2k.admin.service.ColumnService;
import com.apabi.r2k.admin.service.ReleaseRecordService;
import com.apabi.r2k.admin.service.TemplateService;
import com.apabi.r2k.admin.utils.ColumnType;
import com.apabi.r2k.admin.utils.ColumnUtil;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.FileUtils;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.TempTypeEnum;

@Service("releaseRecordService")
public class ReleaseRecordServiceImpl implements ReleaseRecordService {

	@Resource
	private ReleaseRecordDao releaseRecordDao;
	@Resource
	private TemplateService templateService;
	@Resource
	private ColumnService columnService;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveReleaseRecord(ReleaseRecord record) throws Exception {
		return releaseRecordDao.saveReleaseRecord(record);
	}

	@Override
	public void batchSaveRecords(List<ReleaseRecord> records) throws Exception {
		releaseRecordDao.batchSaveRecords(records);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateStatus(Map parameters) throws Exception{
		return releaseRecordDao.updateStatus(parameters);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateReleaseRecord(ReleaseRecord record) throws Exception {
		return releaseRecordDao.updateReleaseRecord(record);
	}

	@Override
	public void batchUpdateRecords(List<ReleaseRecord> records)
			throws Exception {
		releaseRecordDao.batchUpdateRecords(records);
	}

	@Override
	public List<ReleaseRecord> findReleaseRecords(Map parameters)
			throws Exception {
		return releaseRecordDao.findReleaseRecords(parameters);
	}

	@Override
	public ReleaseRecord findDevTypeRecord(String orgid, String devType)
			throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgid);
		params.put("deviceType", devType);
		return releaseRecordDao.findReleaseRecord(params);
	}

	@Override
	public ReleaseRecord findDeviceRecord(String orgid, String deviceid)
			throws Exception {
		Map params = new HashMap();
		params.put("orgId", orgid);
		params.put("deviceId", deviceid);
		return releaseRecordDao.findReleaseRecord(params);
	}

	/**
	 * 选择机构设备类型模板
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void selectOrgTemplate(String orgid, String devType, String setNo, String homeName) throws Exception {
		//获取机构所有该设备类型下的内容
		List<Column> columns = columnService.findOrgAllColumns(orgid, devType);			
		//所有机构公用内容
		List<Column> publicColumns = columnService.findOrgAllColumns(orgid, DevTypeEnum.ORG.getName());		
		//公用内容转成map
		Map publicMap = ColumnUtil.allColumnToMap(publicColumns);
		//获取整套模板中不同类型模板的默认模板
		Map<String, InfoTemplate> templateMap = templateService.getTemplateSet(orgid, setNo, homeName);
		//删除发布表中原有的发布信息
		releaseRecordDao.deleteByDeviceType(orgid, devType);
		List<ReleaseRecord> releaseRecords = new ArrayList<ReleaseRecord>();
		for(Column col : columns){
			//根据内容类型获取对应的模板类型
			String tempType = ColumnType.getTemplateType(col.getType());
			//获取该模板类型的默认模板
			InfoTemplate template = templateMap.get(tempType);
			if(col.getType().equals(Column.TYPE_WELCOME)){
				template = new InfoTemplate();
				template.setId(0);
			}
			if(template != null){
				ReleaseRecord record = createOrgReleaseRecord(orgid, devType, col.getId(), template.getId());
				releaseRecords.add(record);
			}
			//将引用内容添加到发布表
			if(col.getQuoteId() > 0){
				Column pubCol = (Column) publicMap.get(col.getQuoteId());
				if(pubCol == null){
					continue;
				}
				List<Column> childs = ColumnUtil.normalizeColumn(pubCol);		//将column树转为列表
				childs.remove(pubCol);
				for(Column c : childs){
					tempType = ColumnType.getTemplateType(c.getType());
					template = templateMap.get(tempType);
					if(template != null){
						ReleaseRecord record = createOrgReleaseRecord(orgid, devType, c.getId(), template.getId());
						releaseRecords.add(record);
					}
				}
			}
		}
		InfoTemplate indexTemp = templateMap.get(TempTypeEnum.HOME.getName());
		ReleaseRecord record = createOrgHomeRecord(orgid, devType, indexTemp.getId());
		releaseRecords.add(record);
		releaseRecordDao.batchSaveRecords(releaseRecords);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void selectDeviceTemplate(String orgid, String deviceId, String setNo, String homeName) throws Exception {
		List<Column> columns = columnService.findDevAllColumns(orgid, deviceId);		//获取所有内容
		List<Column> publicColumns = columnService.findOrgAllColumns(orgid, DevTypeEnum.ORG.getName());		//所有公用内容
		//公用内容转成map
		Map publicMap = ColumnUtil.allColumnToMap(publicColumns);
		Map<String, InfoTemplate> templateMap = templateService.getTemplateSet(orgid, setNo, homeName);	//获取所有默认的模板
		releaseRecordDao.deleteByDeviceId(orgid, deviceId);		//删除原来的发布信息
		List<ReleaseRecord> releaseRecords = new ArrayList<ReleaseRecord>();
		for(Column col : columns){
			String tempType = ColumnType.getTemplateType(col.getType());
			InfoTemplate template = templateMap.get(tempType);
			if(col.getType().equals(Column.TYPE_WELCOME)){
				template = new InfoTemplate();
				template.setId(0);
			}
			if(template != null){
				ReleaseRecord record = createDevReleaseRecord(orgid, deviceId, col.getId(), template.getId());
				releaseRecords.add(record);
			}
			//将引用内容添加到发布表
			if(col.getQuoteId() > 0){
				Column pubCol = (Column) publicMap.get(col.getQuoteId());
				if(pubCol == null){
					continue;
				}
				List<Column> childs = ColumnUtil.normalizeColumn(pubCol);		//将column树转为列表
				childs.remove(pubCol);
				for(Column c : childs){
					tempType = ColumnType.getTemplateType(c.getType());
					template = templateMap.get(tempType);
					if(template != null){
						ReleaseRecord record = createDevReleaseRecord(orgid, deviceId, c.getId(), template.getId());
						releaseRecords.add(record);
					}
				}
			}
		}
		InfoTemplate indexTemp = templateMap.get(TempTypeEnum.HOME.getName());
		ReleaseRecord record = createDevHomeRecord(orgid, deviceId, indexTemp.getId());
		releaseRecords.add(record);
		releaseRecordDao.batchSaveRecords(releaseRecords);
	}
	
	private ReleaseRecord createOrgHomeRecord(String orgid, String deviceType, int templateId){
		return createReleaseRecord(orgid, deviceType, null, null, templateId);
	}
	
	private ReleaseRecord createDevHomeRecord(String orgid, String deviceId, int templateId){
		return createReleaseRecord(orgid, null, deviceId, null, templateId);
	}
	
	@Override
	public ReleaseRecord createOrgReleaseRecord(String orgid, String deviceType, Integer columnId, int templateId){
		return createReleaseRecord(orgid, deviceType, null, columnId, templateId);
	}
	
	@Override
	public ReleaseRecord createDevReleaseRecord(String orgid, String deviceId, Integer columnId, int templateId){
		return createReleaseRecord(orgid, null, deviceId, columnId, templateId);
	}
	
	private ReleaseRecord createReleaseRecord(String orgid, String deviceType, String deviceId, Integer columnId, int templateId){
		ReleaseRecord record = new ReleaseRecord();
		record.setOrgId(orgid);
		record.setDeviceType(deviceType);
		record.setDeviceId(deviceId);
		record.setColumnId(columnId);
		record.setTemplateId(templateId);
		record.setStatus(ReleaseRecord.STATUS_UNPUBLISH);
		Date now = new Date();
		record.setCrtDate(now);
		record.setLastDate(now);
		return record;
	}

	@Override
	public void selectOrgColTemplate(String orgid, String devType,
			String setNo, int templateId, int colId) throws Exception {
		List<Column> columns = columnService.findOrgAllColumns(orgid, devType);
		List<Column> publicColumns = columnService.findOrgAllColumns(orgid, DevTypeEnum.ORG.getName());		//所有公用内容
 		//公用内容转成map
 		Map publicMap = ColumnUtil.allColumnToMap(publicColumns);
		List<InfoTemplate> infoTemplates = templateService.findBySetNo(setNo);
		List delIds = new ArrayList();
		Column column = ColumnUtil.getColumnAndChilds(columns, colId);
		if(column == null){
			Column sourceColumn = (Column) publicMap.get(colId);
			column = sourceColumn;
		} else if(column.getQuoteId() > 0){
			Column sourceColumn = (Column) publicMap.get(column.getQuoteId());
			column.setColumns(sourceColumn.getColumns());
		}
		delIds.add(column.getId());
		String tempType = ColumnType.getTemplateType(column.getType());
		Map<String, List<InfoTemplate>> templateMap = templateService.getTemplateMap(infoTemplates);
		List<InfoTemplate> list = templateMap.get(tempType);
		InfoTemplate parentTemp = null;
 		if(CollectionUtils.isEmpty(list)){
 			return;
		}
 		for(InfoTemplate temp : infoTemplates){
 			if(temp.getId() == templateId){
 				parentTemp = temp;
 				break;
 			}
 		}
 		if(parentTemp == null){
 			return;
 		}
 		List<ReleaseRecord> releaseRecords = new ArrayList<ReleaseRecord>();
 		ReleaseRecord parentRecord = createOrgReleaseRecord(orgid, devType, column.getId(), templateId);
 		releaseRecords.add(parentRecord);
		List<Column> newColumns = ColumnUtil.normalizeColumn(column);
		newColumns.remove(column);
		String suffix = parentTemp.getName().substring(tempType.length());
		for(Column col : newColumns){
			delIds.add(col.getId());
			if(col.getType() == column.getType()){
				ReleaseRecord record = createOrgReleaseRecord(orgid, devType, col.getId(), templateId);
			}else{
				String childType = ColumnType.getTemplateType(col.getType());
				String name = childType + suffix;
				List<InfoTemplate> childList = templateMap.get(childType);
				if(CollectionUtils.isEmpty(childList)){
					continue;
				}
				for(InfoTemplate childTemp : childList){
					if(name.equals(childTemp.getName())){
						ReleaseRecord record = createOrgReleaseRecord(orgid, devType, col.getId(), childTemp.getId());
						releaseRecords.add(record);
						break;
					}
				}
			}
		}
		Map params = new HashMap();
		params.put("orgId", orgid);
		params.put("deviceType", devType);
		params.put("colIds", delIds);
		releaseRecordDao.deleteByColIds(params);
		releaseRecordDao.batchSaveRecords(releaseRecords);
	}

	@Override
	public void selectDevColTemplate(String orgid, String deviceId, String setNo, int templateId, int colId) throws Exception{
		List<Column> columns = columnService.findDevAllColumns(orgid, deviceId);
		List<Column> publicColumns = columnService.findOrgAllColumns(orgid, DevTypeEnum.ORG.getName());		//所有公用内容
 		//公用内容转成map
 		Map publicMap = ColumnUtil.allColumnToMap(publicColumns);
		List<InfoTemplate> infoTemplates = templateService.findBySetNo(setNo);
		List delIds = new ArrayList();
		Column column = ColumnUtil.getColumnAndChilds(columns, colId);
		if(column == null){
			Column sourceColumn = (Column) publicMap.get(colId);
			column = sourceColumn;
		} else if(column.getQuoteId() > 0){
			Column sourceColumn = (Column) publicMap.get(column.getQuoteId());
			column.setColumns(sourceColumn.getColumns());
		}
		delIds.add(column.getId());
		String tempType = ColumnType.getTemplateType(column.getType());
		Map<String, List<InfoTemplate>> templateMap = templateService.getTemplateMap(infoTemplates);
		List<InfoTemplate> list = templateMap.get(tempType);
		InfoTemplate parentTemp = null;
 		if(CollectionUtils.isEmpty(list)){
 			return;
		}
 		for(InfoTemplate temp : infoTemplates){
 			if(temp.getId() == templateId){
 				parentTemp = temp;
 				break;
 			}
 		}
 		if(parentTemp == null){
 			return;
 		}
 		List<ReleaseRecord> releaseRecords = new ArrayList<ReleaseRecord>();
 		ReleaseRecord parentRecord = createDevReleaseRecord(orgid, deviceId, column.getId(), templateId);
 		releaseRecords.add(parentRecord);
		List<Column> newColumns = ColumnUtil.normalizeColumn(column);
		newColumns.remove(column);
		String suffix = parentTemp.getName().substring(tempType.length());
		for(Column col : newColumns){
			delIds.add(col.getId());
			if(col.getType() == column.getType()){
				ReleaseRecord record = createDevReleaseRecord(orgid, deviceId, col.getId(), templateId);
			}else{
				String childType = ColumnType.getTemplateType(col.getType());
				String name = childType + suffix;
				List<InfoTemplate> childList = templateMap.get(childType);
				if(CollectionUtils.isEmpty(childList)){
					continue;
				}
				for(InfoTemplate childTemp : childList){
					if(name.equals(childTemp.getName())){
						ReleaseRecord record = createDevReleaseRecord(orgid, deviceId, col.getId(), childTemp.getId());
						releaseRecords.add(record);
						break;
					}
				}
			}
		}
		Map params = new HashMap();
		params.put("orgId", orgid);
		params.put("deviceId", deviceId);
		params.put("colIds", delIds);
		releaseRecordDao.deleteByColIds(params);
		releaseRecordDao.batchSaveRecords(releaseRecords);
	}
	
	@Override
	public void deleteAllByColids(List ids) throws Exception {
		releaseRecordDao.deleteAllByColIds(ids);
	}

	@Override
	public int addDevHomeTemplate(String orgId, String deviceId, int templateId) throws Exception{
		ReleaseRecord record = createDevHomeRecord(orgId, deviceId, templateId);
		return releaseRecordDao.saveReleaseRecord(record);
	}
	
	@Override
	public int addOrgHomeTemplate(String orgId, String deviceType,
			int templateId) throws Exception {
		ReleaseRecord record = createOrgHomeRecord(orgId, deviceType, templateId);
		return releaseRecordDao.saveReleaseRecord(record);
	}

	@Override
	public int saveOrgReleaseRecord(String orgId, String deviceType,
			int columnId, int templateId) throws Exception {
		ReleaseRecord record = createOrgReleaseRecord(orgId, deviceType, columnId, templateId);
		return releaseRecordDao.saveReleaseRecord(record);
	}

	@Override
	public int saveDevReleaseRecord(String orgId, String deviceId,
			int columnId, int templateId) throws Exception {
		ReleaseRecord record = createDevReleaseRecord(orgId, deviceId, columnId, templateId);
		return releaseRecordDao.saveReleaseRecord(record);
	}

	@Override
	public void addOrgColumnsRecord(Column column) throws Exception {
		List<ReleaseRecord> records = new ArrayList<ReleaseRecord>();
		InfoTemplate template = column.getInfoTemplate();
		ReleaseRecord parentRecord = createOrgReleaseRecord(column.getOrgId(), column.getDeviceType(), column.getId(), template.getId());
		records.add(parentRecord);
		List<Column> childs = column.getColumns();
		if(CollectionUtils.isEmpty(childs)){
			releaseRecordDao.batchSaveRecords(records);
			return;
		}
		String suffix = template.getName().substring(template.getType().length());
		String childTempType = ColumnType.getTemplateType(childs.get(0).getType());
		String tempName = childTempType+suffix;
		InfoTemplate defaultTemp = getMatchTemplate(template.getSetNo(), tempName);
		if(defaultTemp == null){
			return;
		}
		int tempId = defaultTemp.getId();
		for(Column col : childs){
			ReleaseRecord r = createOrgReleaseRecord(col.getOrgId(), col.getDeviceType(), col.getId(), tempId);
			records.add(r);
		}
		releaseRecordDao.batchSaveRecords(records);
	}

	@Override
	public void addDevColumnsRecord(Column column) throws Exception {
		List<ReleaseRecord> records = new ArrayList<ReleaseRecord>();
		InfoTemplate template = column.getInfoTemplate();
		ReleaseRecord parentRecord = createDevReleaseRecord(column.getOrgId(), column.getDeviceId(), column.getId(), template.getId());
		records.add(parentRecord);
		List<Column> childs = column.getColumns();
		if(CollectionUtils.isEmpty(childs)){
			releaseRecordDao.batchSaveRecords(records);
			return;
		}
		String suffix = template.getName().substring(template.getType().length());
		String childTempType = ColumnType.getTemplateType(childs.get(0).getType());
		String tempName = childTempType+suffix;
		InfoTemplate defaultTemp = getMatchTemplate(template.getSetNo(), tempName);
		if(defaultTemp == null){
			return;
		}
		int tempId = defaultTemp.getId();
		for(Column col : childs){
			ReleaseRecord r = createDevReleaseRecord(col.getOrgId(), col.getDeviceId(), col.getId(), tempId);
			records.add(r);
		}
		releaseRecordDao.batchSaveRecords(records);
	}
	
	private InfoTemplate getMatchTemplate(String setNo, String tempName) throws Exception{
		List<InfoTemplate> infoTemplates = templateService.findBySetNo(setNo);
		InfoTemplate defaultTemp = null;
		for(InfoTemplate temp : infoTemplates){
			if(temp.getName().equals(tempName)){
				defaultTemp = temp;
				break;
			}
		}
		return defaultTemp;
	}

	@Override
	public int updateSingleRecord(String orgid, String deviceId, String devType, int columnId, int templateId) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", orgid);
		params.put("deviceId", deviceId);
		params.put("devType", devType);
		params.put("colId", columnId);
		params.put("templateId", templateId);
		return releaseRecordDao.updateTemplateByColumn(params);
	}

	/**
	 * 删除设备发布信息
	 */
	@Override
	public int deleteDevReleaseRecord(String orgId, String deviceId) throws Exception {
		int result = releaseRecordDao.deleteByDeviceId(orgId, deviceId);
		if(result > 0){
			//删除发布数据
			String pubPath = PropertiesUtil.getRootPath()+"/"+PropertiesUtil.get("base.r2kfile")+"/"+PropertiesUtil.get("path.info.pub")+"/"+orgId+"/"+deviceId;
			File pubDir = new File(pubPath);
			FileUtils.deleteDirectory(pubDir);
		}
		return result;
	}
}
