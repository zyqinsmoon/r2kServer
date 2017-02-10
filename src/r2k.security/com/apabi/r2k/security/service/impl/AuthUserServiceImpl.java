package com.apabi.r2k.security.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.common.utils.GlobalConstant;
import com.apabi.r2k.common.utils.MD5;
import com.apabi.r2k.common.utils.PasswordRandom;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.security.dao.AuthUserDao;
import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.service.AuthUserService;

@Service("authUserService")
public class AuthUserServiceImpl implements AuthUserService{

	@Resource
	private AuthUserDao authUserDao;
	
	
	public Page findByPageRequest(PageRequest pr) throws Exception {
		return authUserDao.findByPageRequest(pr);
	}
	
	public AuthUser getByLoginName(String v) throws Exception {
		return authUserDao.getByLoginName(v);
	}
	public Long generateKey() throws Exception {
		// TODO Auto-generated method stub
		return authUserDao.generateKey();
	}
	//分页查询一个机构下（包括其所有子机构下）的所有用户 机构id需加入到PageRequest的filter里 名字为orgId
	public Page findByPageRequestOrgFiltered(PageRequest pr) throws Exception{
		
		return authUserDao.findByPageRequestOrgFiltered(pr);
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateUserState(Integer state, Long uid) throws Exception {
		authUserDao.updateUserState(state,uid);
		
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void save(AuthUser authUser) throws Exception {
		authUserDao.saveOrUpdate(authUser);
		
	}

	@Override
	public List<AuthUser> findByOrgId(Long orgId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	//检查用户名是否存在
	public Boolean isUserNameExist(String userName) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userName", userName);
		AuthUser user = this.authUserDao.getUserObject(map);
		return (user != null ? false : true);
	}
	
	//检查登录名是否存在
	public Boolean isLoginNameExist(String loginName) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("loginName", loginName);
		AuthUser user = this.authUserDao.getUserObject(map);
		return (user != null ? false : true);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void saveOrUpdate(AuthUser au) throws Exception {
		authUserDao.saveOrUpdate(au);
		
	}

	//批量删除用户
	public void removeByIds(String[] ids) throws Exception {
		this.authUserDao.removeByIds(ids);
	}
	
	//获取用户对象
	public AuthUser getById(Long id) throws Exception {
		return this.authUserDao.getById(id);
	}

	//删除用户
	public boolean delete(Map<String, Object> map) throws Exception {
		int count = this.authUserDao.delete(map);
		if(count > 0){
			return true;
		}
		return false;
	}

	//保存默认用户
	public String saveDefaultUser(String orgId) throws Exception{
		Date now = new Date();
		AuthUser au = new AuthUser();
		au.setCrtDate(now);
		au.setLastUpdate(now);
		au.setAuthOrgId(orgId);
		String username = orgId + GlobalConstant.DEFALUT_USERNAME;
		au.setUserName(username);
		au.setLoginName(username);
		//String userpwd = GlobalConstant.DEFALUT_USERPWD;
		int pwdLength = PropertiesUtil.getInt("default_pwdLength");//获得配置文件中密码的长度
		String userpwd = PasswordRandom.getRandomPassword(pwdLength);//生成随机密码
		au.setPassword(new MD5().md5s(userpwd));
		au.setEnabled(AuthUser.USER_STATUS_ENABLED);
		au.setIsAdmin(AuthUser.USER_ISADMIN_NO);
		au.setUserDesc(orgId + "机构默认用户");
		this.authUserDao.saveOrUpdate(au);
		return userpwd;
	}

	
}
