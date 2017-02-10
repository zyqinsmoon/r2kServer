package com.apabi.r2k.admin.action;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.apabi.r2k.admin.service.ColumnService;
import com.apabi.r2k.admin.service.DeviceService;
import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.security.utils.SessionUtils;
import com.opensymphony.xwork2.ActionContext;

@Scope("prototype")
@Controller("quoteAction")
public class QuoteAction {
	@Autowired
	private ColumnService columnService;
	@Autowired
	private DeviceService deviceService;
	private Logger log = LoggerFactory.getLogger(QuoteAction.class);
	
	private String deviceId;//设备ID
	private String devType;
	private Page page;
	private int[] checked;
	private int parentId;
	private String orgId;
	private String actionName;
	private String setNo;
	private String homeName;
	private String deviceName;
	private String devTypeName;
	
	public String index(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				log.info("分页查询开始");
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				Map params = (Map) pageRequest.getFilters();
				params.put("orgId", orgId);
				params.put("deviceType", DevTypeEnum.ORG.getName());
				params.put("quoteDevId", deviceId);
				Map session = ActionContext.getContext().getSession();
				if(session.get(deviceId) != null){
					params.put("hideWelcome", "true");
				}
				page = columnService.quotePageQuery(pageRequest);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return "index";
	}
	
	public String addDevQuoted(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId) && checked != null){
				columnService.addDevQuoted(orgId, deviceId, checked, setNo, homeName);
			}
		} catch (Exception e1) {
			log.error(e1.getMessage(),e1);
		}
		setActionName();
		return "device";
	}
	
	public String addQuote(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null && checked != null){
				for(int quoteId : checked){
					if(columnService.getDeviceQuote(quoteId, deviceId, parentId,orgId) == null){
						Column quoteColumn = columnService.getColumnById(quoteId);
						String type = quoteColumn.getType();
						Column column = new Column();
						column.setQuoteId(quoteId);
						column.setDeviceId(deviceId);
						column.setOrgId(orgId);
						column.setParentId(parentId);
						column.setCrtDate(new Date());
						column.setStatus(Column.STATUS_UNPUBLISH);
						//引用类型
						column.setType(Column.TYPE_QUOTE_PREFIX + type);
						column.setSort(quoteColumn.getSort());
						columnService.addColumn(column);
					}
				}
			}
		} catch (NumberFormatException e1) {
			log.error(e1.getMessage(),e1);
		} catch (Exception e1) {
			log.error(e1.getMessage(),e1);
		}
		return "device";
	}
	
	public String orgQuoted(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId)){
				log.info("分页查询开始");
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				Map params = (Map) pageRequest.getFilters();
				params.put("orgId", orgId);
				params.put("deviceType", DevTypeEnum.ORG.getName());
				params.put("quoteDevType", devType);
				//已经存在标题的，不需要引用标题，栏目下不需用引用标题
//				if(parentId > 0 || columnService.getDeviceWelcome(orgId, deviceId) != null){
//					params.put("hideWelcome", "true");
//				}
				Map session = ActionContext.getContext().getSession();
				if(session.get(devType) != null){
					params.put("hideWelcome", "true");
				}
				page = columnService.quotePageQuery(pageRequest);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		setActionName();
		devTypeName = DevTypeEnum.findName(devType).getValue();
		return "index";
	}
	
	public String addOrgQuoted(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			if(StringUtils.isNotBlank(orgId) && checked != null){
				columnService.addOrgQuoted(orgId, devType, checked, setNo, homeName);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		setActionName();
		return "org";
	}
	
	private void setActionName(){
		if(StringUtils.isNotBlank(deviceId)){
			actionName = "device";
		}
		if(devType.equals(DevTypeEnum.ORG.getName())){
			actionName = "org";
		}
		if(devType.equals(DevTypeEnum.AndroidLarge.getName())){
			actionName = "showLarge";
		}
		if(devType.equals(DevTypeEnum.AndroidPortrait.getName())){
			actionName = "showPortrait";
		}
		if(devType.equals(DevTypeEnum.AndroidPad.getName())){
			actionName = "showAndroidPad";
		}
		if(devType.equals(DevTypeEnum.AndroidPhone.getName())){
			actionName = "showAndroidPhone";
		}
		if(devType.equals(DevTypeEnum.iPad.getName())){
			actionName = "showIPad";
		}
		if(devType.equals(DevTypeEnum.iPhone.getName())){
			actionName = "showIPhone";
		}
	}
	
	public void setColumnService(ColumnService columnService) {
		this.columnService = columnService;
	}

	public ColumnService getColumnService() {
		return columnService;
	}
	
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	public DeviceService getDeviceService() {
		return deviceService;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public int[] getChecked() {
		return checked;
	}

	public void setChecked(int[] checked) {
		this.checked = checked;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getSetNo() {
		return setNo;
	}

	public void setSetNo(String setNo) {
		this.setNo = setNo;
	}

	public String getHomeName() {
		return homeName;
	}

	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDevTypeName() {
		return devTypeName;
	}

	public void setDevTypeName(String devTypeName) {
		this.devTypeName = devTypeName;
	}

}
