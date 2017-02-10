package com.apabi.r2k.security.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.apabi.r2k.security.model.AuthEntity;
import com.apabi.r2k.security.model.AuthRole;
import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.service.AuthService;
import com.apabi.r2k.security.utils.SessionUtils;

@Controller("authAction")
public class AuthAction {

	private Logger log = LoggerFactory.getLogger(AuthAction.class);
	
	@Resource
	private AuthService authService;
	
	private List<AuthEntity> authEntities;
	
	private String[] roleIds;
	
	public String getAuthTree(){
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			AuthUser authUser = SessionUtils.getCurrentUser(req);
			if(authUser != null){
				List<AuthRole> authRoles = authUser.getAuthRoleList();
				roleIds = setToArray(authRoles);
				log.debug("getAuthTree:[authUser#"+authUser.getLoginName()+",org#"+authUser.getAuthOrg().getOrgId()+"]:获取当前用户的后台菜单");
				authEntities = authService.getAuthTree(roleIds);
			}
		} catch (Exception e) {
			log.error("getAuthTree:获取菜单失败:"+e.getMessage());
			e.printStackTrace();
		}
		return "tree";
	}

	private String[] setToArray(List<AuthRole> authRoles){
		int size = authRoles.size();
		String[] array = new String[size];
		int inx = 0;
		for(AuthRole authRole: authRoles){
			array[inx] = authRole.getRoleCode();
			inx++;
		}
		return array;
	}
	
	public List<AuthEntity> getAuthEntities() {
		return authEntities;
	}

	public void setAuthEntities(List<AuthEntity> authEntities) {
		this.authEntities = authEntities;
	}

	public String[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	} 
}
