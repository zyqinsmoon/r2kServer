package com.apabi.r2k.security.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.model.AuthUserRole;
import com.apabi.r2k.security.service.AuthUserRoleRelatedService;
import com.apabi.r2k.security.service.AuthUserRoleService;
import com.apabi.r2k.security.service.AuthUserService;

/**用于处理用户和用户与其角色之间关联的各种持久化操作*/
@Service("authUserRoleRelatedService")
public class AuthUserRoleRelatedServiceImpl implements AuthUserRoleRelatedService{

	@Resource
	private AuthUserRoleService authUserRoleService;	
	@Resource
	private AuthUserService authUserService;
	/**保存用户 同时保存用户与角色之间的关联*/
	public void saveAuthUser(AuthUser authUser,Long[] roleIds)throws Exception{
		Date date=new Date();
		if(authUser.getId()!=null){
			authUser.setLastUpdate(date);		
			authUserService.saveOrUpdate(authUser);
		}
		else {
			Long id=authUserService.generateKey();//手动拿到sequence key
			authUser.setId(id);
			authUser.setCrtDate(date);
			authUserService.save(authUser);			
		}		
		//如果roleIds不为空 就保存其关联
		if(roleIds!=null)saveRelated(roleIds,authUser.getId());
	}
	
	
	public void saveRelated(Long[] roleIds,Long userId) throws Exception {
		Date date=new Date();
		int i;
		authUserRoleService.removeByUserId(userId);
		for(i=0;i<roleIds.length;i++){
			if(roleIds[i]==null)break;
			AuthUserRole authroleRole=new AuthUserRole(roleIds[i],userId);
			authroleRole.setCrtDate(date);
			authUserRoleService.saveOrUpdate(authroleRole);			
		}						
	}
	
	/**批量删除用户（同时删除用户与角色的关联）*/
	public void removeUserByIds(String[] ids) throws Exception{
		authUserService.removeByIds(ids);
		for(String id:ids){
			authUserRoleService.removeByUserId(Long.parseLong(id));
		}
	}
}
