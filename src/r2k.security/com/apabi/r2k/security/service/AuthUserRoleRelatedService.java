package com.apabi.r2k.security.service;

import com.apabi.r2k.security.model.AuthUser;

/**用于处理用户和用户与其角色之间关联的各种持久化操作*/
public interface AuthUserRoleRelatedService {


	/**保存用户 同时保存用户与角色之间的关联*/
	public void saveAuthUser(AuthUser authUser,Long[] roleIds)throws Exception;
	
	
	public void saveRelated(Long[] roleIds,Long userId) throws Exception;
	
	/**批量删除用户（同时删除用户与角色的关联）*/
	public void removeUserByIds(String[] ids) throws Exception;
}
