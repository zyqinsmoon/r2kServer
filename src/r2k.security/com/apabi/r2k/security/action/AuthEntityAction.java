package com.apabi.r2k.security.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.security.model.AuthEntity;
import com.apabi.r2k.security.service.AuthEntityService;
import com.apabi.r2k.security.utils.SessionUtils;

@Controller("authEntityAction")
public class AuthEntityAction {
	
	@Resource
	private AuthEntityService authEntityService;
	private Page page;
	
	private AuthEntity authEntity;
	private List<AuthEntity> entitylist;
	private String orgId;
	
	
	public String index(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				page = authEntityService.findByPageRequest(pageRequest);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}

	
	public AuthEntity getAuthEntity() {
		return authEntity;
	}
	public void setAuthEntity(AuthEntity authEntity) {
		this.authEntity = authEntity;
	}
	public List<AuthEntity> getEntitylist() {
		return entitylist;
	}
	public void setEntitylist(List<AuthEntity> entitylist) {
		this.entitylist = entitylist;
	}
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
}
