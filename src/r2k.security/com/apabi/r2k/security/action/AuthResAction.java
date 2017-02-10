package com.apabi.r2k.security.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.security.model.AuthRes;
import com.apabi.r2k.security.service.AuthResService;
import com.apabi.r2k.security.utils.SessionUtils;

@Controller("authResAction")
public class AuthResAction {

	@Resource
	private AuthResService authResService;
	private Page page;
	
	private AuthRes authRes;
	private List<AuthRes> reslist;
	private String orgId;
	
	
	public String index(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			String orgId = SessionUtils.getAuthOrgId(req);
			if(orgId != null){
				PageRequest pageRequest= new PageRequest();
				PageRequestFactory.bindPageRequest(pageRequest,req);
				page = authResService.findByPageRequest(pageRequest);
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
	public AuthRes getAuthRes() {
		return authRes;
	}
	public void setAuthRes(AuthRes authRes) {
		this.authRes = authRes;
	}
	public List<AuthRes> getReslist() {
		return reslist;
	}
	public void setReslist(List<AuthRes> reslist) {
		this.reslist = reslist;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
