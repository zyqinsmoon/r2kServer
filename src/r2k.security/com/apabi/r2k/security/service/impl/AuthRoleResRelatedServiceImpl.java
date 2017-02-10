package com.apabi.r2k.security.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.apabi.r2k.security.model.AuthEntity;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.security.model.AuthRes;
import com.apabi.r2k.security.model.AuthResRole;
import com.apabi.r2k.security.model.AuthRole;
import com.apabi.r2k.security.model.AuthUser;
import com.apabi.r2k.security.service.AuthEntityService;
import com.apabi.r2k.security.service.AuthOrgService;
import com.apabi.r2k.security.service.AuthResRoleService;
import com.apabi.r2k.security.service.AuthResService;
import com.apabi.r2k.security.service.AuthRoleResRelatedService;
import com.apabi.r2k.security.service.AuthRoleService;
import com.apabi.r2k.security.service.AuthUserRoleService;
import com.apabi.r2k.security.service.AuthUserService;

/*角色资源关联Service 主要处理角色创建并同时关联资源的逻辑*/
@Service("authRoleResRelatedService")
@SuppressWarnings("unchecked")
public class AuthRoleResRelatedServiceImpl implements AuthRoleResRelatedService {
	@Resource
	private InitRoleServiceImpl initRoleServiceImpl;
	@Resource
	private AuthResService authResService;
	@Resource
	private AuthResRoleService authResRoleService;
	@Resource
	private AuthRoleService authRoleService;
	@Resource
	private AuthUserService authUserService;;
	@Resource
	private AuthUserRoleService authUserRoleService;
	@Resource
	private AuthOrgService authOrgService;
	@Resource
	private AuthEntityService authEntityService;
	private final Long DEFAULT_SA_ROLE_ID=1L;
	
	/**
	 * 
	 * 保存或更新角色信息 同时保存角色与资源的关联(保存时手动取sequence设置Id)
	 * 
	 * */
	public void saveAuthRole(AuthRole authRole,Long[] resIds)throws Exception{
		Date date=new Date();
		if(authRole.getId()!=null){
			authRole.setLastUpdate(date);
			authRole.setRoleCode(null);
			authRole.setEnabled(1L);
			authRoleService.saveOrUpdate(authRole);

		}
		else {
			Long id=authRoleService.generateKey();
			authRole.setId(id);
			authRole.setEnabled(1L);
			authRole.setRoleCode(null);
			authRoleService.save(authRole);
		}
		if(resIds==null)resIds=new Long[0];
		saveRelated(resIds,authRole.getId());
	}
	/**
	 * 保存Role跟Res之间的关联
	 * */
	public void saveRelated(Long[] resIds,Long roleId) throws Exception {
		Date date=new Date();
		int i;		
		List<AuthResRole> arrList=authResRoleService.getByRoleId(roleId);//把以前的关联拿出来
		for(i=0;i<resIds.length;i++){//创建新的关联
			for(AuthResRole arr:arrList){//循环比较 判断一个新资源是新增的还是不变的			
				if(resIds[i].equals(arr.getResId())) {					
					arrList.remove(arr);
					resIds[i]=null;
					break;
				}
			}
		}
		String roleCode=authRoleService.get(roleId).getRoleCode();	
		for(i=0;i<resIds.length;i++){
			if(resIds[i]!=null){
				AuthRes ar=authResService.get(resIds[i]);
				if(ar!=null)initRoleServiceImpl.putMapCache(ar.getResUrl(), roleCode);
				AuthResRole authResRole=new AuthResRole(resIds[i],roleId);
				authResRole.setCrtDate(date);
				authResRoleService.saveOrUpdate(authResRole);
			}
		}
		for(AuthResRole arr:arrList){
			AuthRes ar=authResService.get(arr.getResId());
			authResRoleService.removeById(arr.getId());
			if(ar!=null)initRoleServiceImpl.removeMapCache(ar.getResUrl(), roleCode);
		}		

	}
	
	/**
	 * 保存或更新资源并同时更新资源与角色之间的关联 。
	 * 新建和更新会导致URL改变 因此需要通知权限MapCache。进行变更
	 * */
	public void saveAuthRes(AuthRes authRes) throws Exception{
		Date date=new Date();
		AuthRes oldRes=null;
		if(authRes.getId()!=null){
			oldRes=authResService.get(authRes.getId());
			authRes.setLastUpdate(date);
			authRes.setRoleCode(null);
			authResService.saveOrUpdate(authRes);
			if(oldRes!=null&&!oldRes.getResUrl().equals(authRes.getResUrl())){
				initRoleServiceImpl.putMapCache(authRes.getResUrl(), AuthRole.ROLE_PREFIX+DEFAULT_SA_ROLE_ID);
				initRoleServiceImpl.removeURL(oldRes.getResUrl());
			}
		}

		else {
			Long id=authResService.generateKey();
			authRes.setId(id);
			authRes.setRoleCode(null);
			authResService.save(authRes);
			//默认将该资源与超级管理员角色进行关联
			AuthResRole authResRole=new AuthResRole(authRes.getId(),DEFAULT_SA_ROLE_ID);
			authResRoleService.saveOrUpdate(authResRole);
			initRoleServiceImpl.putMapCache(authRes.getResUrl(), AuthRole.ROLE_PREFIX+DEFAULT_SA_ROLE_ID);
		}
		
		
	}
	
	/**
	 * 批量删除角色并同时删除 角色-用户、角色-资源 之间的关联
	 * @throws Exception 
	 * */
	public void removeRolesAndAllRelated(final String[] ids) throws Exception{
		authRoleService.removeByIds(ids);
		for(String rid:ids){
			Long roleId=Long.parseLong(rid);
			List<AuthResRole> arrList=authResRoleService.getByRoleId(roleId);
			authResRoleService.removeByRoleId(roleId);
			authUserRoleService.removeByRoleId(roleId);
			for(AuthResRole arr:arrList){
				AuthRes ar=authResService.get(arr.getResId());
				initRoleServiceImpl.removeMapCache(ar.getResUrl(), AuthRole.ROLE_PREFIX+roleId);
			}
			
		}

	}
	/**
	 * 删除资源 并同时删除资源和角色的关联
	 */
	public void removeRes(Long resId)throws Exception{
		List<AuthResRole> arrList=null;
		AuthRes ar=null;
		try{
			ar=authResService.get(resId);
			authResService.removeById(resId);
			
			
			authResRoleService.removeByResId(resId);
		}catch (Exception e) {
			throw e;
		}	
		initRoleServiceImpl.removeMapCache(ar.getResUrl());
		
		
		
	}
	/**
	 * #最复杂的操作尽量少用#
	 * 删除机构 并且删除机构下的所有角色（同时删除角色的各种关联），然后将该机构下的所有用户都提到该机构的父机构下
	 * @throws Exception 
	 * */
	public void removeOrgAndAllRelated(Long orgId) throws Exception{
		Date date=new Date();
		AuthOrg ao=authOrgService.get(orgId);
//		Long parentOrgId=ao.getParentId();
		String authOrgId = ao.getOrgId();
		List<AuthRole> roleList=authRoleService.getRolesByOrgId(orgId);
		List<AuthUser> userList=authUserService.findByOrgId(orgId);
		String[] roleIds=new String[roleList.size()];
		int i=0;
		for(AuthRole ar:roleList){
			roleIds[i++]=ar.getId().toString();
		}
		authOrgService.removeById(orgId);
		if(roleList.size()>0)removeRolesAndAllRelated(roleIds);
		for(AuthUser au:userList){
			au.setAuthOrgId(authOrgId);
			au.setLastUpdate(date);
			authUserService.saveOrUpdate(au);
		}
	}	
	/**
	 * 按Id查询角色并查出该角色所有关联的资源
	 * 结果放在Map里 其中"角色"放在键<authRole>中。关联的资源放在键<authResList>中
	 * @throws Exception 
	 * */
	public Map getRoleAndRelatedRes(Long roleId) throws Exception{
		Map result=new HashMap();
		result.put("authRole", authRoleService.get(roleId));
		List authResList;
		List<AuthEntity>authEntityList;
		authEntityList=new ArrayList<AuthEntity>();
		authResList=authResService.loadAuthResByRoleId(roleId);
		for(AuthRes ar : (List<AuthRes>)authResList){
			if(ar.getType()==0) {
				AuthEntity ae=authEntityService.get(ar.getEntityId());
				if(!authEntityList.contains(ae))authEntityList.add(ae);
			}
		}					
		authResList.addAll(authEntityList);
		result.put("authResList", authResList);
		return result;
	}
	
	/**
	 * 测试用
	 * */
	public Map __getCache(){
		return initRoleServiceImpl.resourceMapCache;
	}
	
	public InitRoleServiceImpl getInitRoleServiceImpl() {
		return initRoleServiceImpl;
	}
	public void setInitRoleServiceImpl(InitRoleServiceImpl initRoleServiceImpl) {
		this.initRoleServiceImpl = initRoleServiceImpl;
	}
	public AuthResService getAuthResService() {
		return authResService;
	}
	public void setAuthResService(AuthResService authResService) {
		this.authResService = authResService;
	}
	public AuthResRoleService getAuthResRoleService() {
		return authResRoleService;
	}
	public void setAuthResRoleService(AuthResRoleService authResRoleService) {
		this.authResRoleService = authResRoleService;
	}
	public AuthRoleService getAuthRoleService() {
		return authRoleService;
	}
	public void setAuthRoleService(AuthRoleService authRoleService) {
		this.authRoleService = authRoleService;
	}
	public AuthUserService getAuthUserService() {
		return authUserService;
	}
	public void setAuthUserService(AuthUserService authUserService) {
		this.authUserService = authUserService;
	}
	public AuthUserRoleService getAuthUserRoleService() {
		return authUserRoleService;
	}
	public void setAuthUserRoleService(AuthUserRoleService authUserRoleService) {
		this.authUserRoleService = authUserRoleService;
	}
	public AuthOrgService getAuthOrgService() {
		return authOrgService;
	}
	public void setAuthOrgService(AuthOrgService authOrgService) {
		this.authOrgService = authOrgService;
	}
	public AuthEntityService getAuthEntityService() {
		return authEntityService;
	}
	public void setAuthEntityService(AuthEntityService authEntityService) {
		this.authEntityService = authEntityService;
	}
}
