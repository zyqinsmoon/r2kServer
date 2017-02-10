package com.apabi.r2k.security.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.admin.model.User;
import com.apabi.r2k.common.base.PageRequestFactory;
import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.MD5;
import com.apabi.r2k.common.utils.PasswordRandom;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.service.AuthUserService;
import com.apabi.r2k.security.utils.SessionUtils;

@Controller("authUserAction")
@Scope("prototype")
public class AuthUserAction {

	// field
	private Logger log = LoggerFactory.getLogger(AuthUserAction.class);

	@Resource(name = "authUserService")
	private AuthUserService authUserService;
	private AuthUser user;
	private Page page;
	private List<AuthOrg> orglist;
	private List<User> userlist;
	private String orgId;
	private String password;
	private String flag;
	private Long id;
	private String[] userIds = null;

	// method

	public String showAll() {
		try {
			HttpServletRequest req = ServletActionContext.getRequest();
			orgId = SessionUtils.getAuthOrgId(req);
			PageRequest pageRequest = new PageRequest();
			PageRequestFactory.bindPageRequest(pageRequest, req);
			Map param = (Map) pageRequest.getFilters();
			page = this.authUserService.findByPageRequest(pageRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}

	// 检查用户名是否存在
	public String checkUserName() {
		boolean bool = false;
		try {
			String userName = user.getUserName();
			if (userName != null && !"".equals(userName)) {
				bool = this.authUserService.isUserNameExist(userName);
				flag = bool ? "1" : "0";
			} else {
				flag = "-1";
			}
		} catch (Exception e) {
			flag = "-1";
			e.printStackTrace();
		}
		return "checkname";
	}

	// 检查登录名是否存在
	public String checkLoginName() {
		boolean bool = false;
		try {
			String loginName = user.getLoginName();
			if (loginName != null && !"".equals(loginName)) {
				bool = this.authUserService.isLoginNameExist(loginName);
				flag = bool ? "1" : "0";
			} else {
				flag = "-1";
			}
		} catch (Exception e) {
			flag = "-1";
			e.printStackTrace();
		}
		return "checkname";
	}

	public String saveUser() {
		try {
			if (user != null) {
				Date now = new Date();
				user.setCrtDate(now);
				user.setLastUpdate(now);
				user.setPassword(new MD5().md5s(user.getPassword()));
				user.setIsAdmin(-1L);
				this.authUserService.save(user);
			} else {
				return "error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "save";
	}

	public String toUpdateUser(){
		try {
			if(id > 0){
				user = this.authUserService.getById(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "toupdate";
	}
	
	public String updateUser() {
		try {
			if (user != null) {
				Date now = new Date();
				user.setLastUpdate(now);
				user.setIsAdmin(-1L);
				this.authUserService.saveOrUpdate(user);
			} else {
				return "error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "udpate";
	}
	
	public String updatePwd(){
		try {
			if(this.password != null && !"".equals(this.password.trim())){
				if (user != null) {
					AuthUser au = this.authUserService.getById(user.getId());
					String md5pwd = new MD5().md5s(this.password.trim());
					if(au != null && md5pwd.equals(au.getPassword())){
						String pwd = user.getPassword();
						if(pwd != null && !"".equals(pwd)){
							Date now = new Date();
							user.setPassword(new MD5().md5s(pwd.trim()));
							user.setLastUpdate(now);
							this.authUserService.saveOrUpdate(user);
							this.flag = GlobalConstant.STATUS_SUCCESS;
						}else{
							this.flag = GlobalConstant.STATUS_FAILURE;
						}
					}else{
						this.flag = GlobalConstant.STATUS_FAILURE;
					}
				} else {
					this.flag = GlobalConstant.STATUS_FAILURE;
					return "error";
				}
			}else{
				this.flag = GlobalConstant.STATUS_FAILURE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "updatepwd";
	}
	
	public String resetsPwd(){
		try {
			log.debug("resetsPwd:开始重置密码");
			if (user != null) {
				AuthUser au = this.authUserService.getById(user.getId());
				log.debug("resetsPwd:得到userId"+user.getId());
				if(au != null){
					int pwdLength = PropertiesUtil.getInt("default_pwdLength");//获得配置文件密码长度
					this.password = PasswordRandom.getRandomPassword(pwdLength);//获得随机密码
					log.debug("resetsPwd:[pwdLength#"+pwdLength+"]生成随机密码"+this.password);
					if(this.password != null && !"".equals(this.password)){
						Date now = new Date();
						user.setPassword(new MD5().md5s(this.password.trim()));
						user.setLastUpdate(now);
						this.authUserService.saveOrUpdate(user);
						this.flag = GlobalConstant.STATUS_SUCCESS;
					}else{
						log.debug("resetsPwd:生成随机密码失败");
						this.flag = GlobalConstant.STATUS_FAILURE;
					}
				}else{
					log.debug("resetsPwd:获得用户id失败");
					this.flag = GlobalConstant.STATUS_FAILURE;
				}
			} else {
				log.debug("resetsPwd:重置密码失败");
				this.flag = GlobalConstant.STATUS_FAILURE;
				return "error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "resetpwd";
	}

	public String deleteUsers(){
		try {
			if(userIds != null && !"".equals(userIds)){
				this.authUserService.removeByIds(userIds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "delete";
	}
	
	// getter and setter

	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public List<AuthOrg> getOrglist() {
		return orglist;
	}
	public void setOrglist(List<AuthOrg> orglist) {
		this.orglist = orglist;
	}

	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public List<User> getUserlist() {
		return userlist;
	}
	public void setUserlist(List<User> userlist) {
		this.userlist = userlist;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public AuthUserService getAuthUserService() {
		return authUserService;
	}
	public void setAuthUserService(AuthUserService authUserService) {
		this.authUserService = authUserService;
	}
	public AuthUser getUser() {
		return user;
	}
	public void setUser(AuthUser user) {
		this.user = user;
	}
	public String[] getUserIds() {
		return userIds;
	}
	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
