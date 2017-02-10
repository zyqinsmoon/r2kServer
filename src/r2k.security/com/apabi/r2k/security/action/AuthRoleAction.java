package com.apabi.r2k.security.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.security.model.AuthRole;
import com.apabi.r2k.security.service.AuthRoleService;
import com.apabi.r2k.security.utils.SessionUtils;

@Controller("authRoleAction")
public class AuthRoleAction {

	@Resource
	private AuthRoleService authRoleService;
	private Page page;
	
	private AuthRole authRole;
	private List<AuthRole> rolelist;
	private String orgId;
	
	
	public String index(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				page = authRoleService.findByPageRequest(pageRequest);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}


	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public AuthRole getAuthRole() {
		return authRole;
	}
	public void setAuthRole(AuthRole authRole) {
		this.authRole = authRole;
	}
	public List<AuthRole> getRolelist() {
		return rolelist;
	}
	public void setRolelist(List<AuthRole> rolelist) {
		this.rolelist = rolelist;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
