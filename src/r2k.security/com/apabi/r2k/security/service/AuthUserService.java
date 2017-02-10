package com.apabi.r2k.security.service;

import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.apabi.r2k.security.model.AuthUser;
@SuppressWarnings("rawtypes")
public interface AuthUserService {
	
	public Page findByPageRequest(PageRequest pr) throws Exception ;
	
	public AuthUser getByLoginName(String v) throws Exception;
	public Long generateKey() throws Exception;
	public void save(AuthUser authUser)throws Exception ;
	//分页查询一个机构下（包括其所有子机构下）的所有用户 机构id需加入到PageRequest的filter里 名字为orgId
	public Page findByPageRequestOrgFiltered(PageRequest pr)throws Exception;
	public List<AuthUser> findByOrgId(Long orgId)throws Exception;
	public void updateUserState(Integer state, Long uid) throws Exception;
	/**
	 * 检查用户名是否存在
	 * @param userName
	 * @return true 成功(不存在)，false失败(已存在)
	 * @throws Exception
	 */
	public Boolean isUserNameExist(String loginName)throws Exception;
	/**
	 * 检查登录名是否存在
	 * @param loginName
	 * @return true 成功(不存在)，false失败(已存在)
	 * @throws Exception
	 */
	public Boolean isLoginNameExist(String loginName)throws Exception;

	public void saveOrUpdate(AuthUser au) throws Exception;

	public void removeByIds(String[] ids) throws Exception;
	
	public AuthUser getById(Long id) throws Exception ;
	public boolean delete(Map<String, Object> map) throws Exception;
	/**
	 * 保存默认用户,返回密码
	 * @param orgId 机构表主键
	 */
	public String saveDefaultUser(String orgId) throws Exception;
}
