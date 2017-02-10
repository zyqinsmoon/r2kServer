package com.apabi.r2k.security.service;

import java.util.Map;

import com.apabi.r2k.security.model.AuthRes;
import com.apabi.r2k.security.model.AuthRole;

/*角色资源关联Manager 主要处理角色创建并同时关联资源的逻辑*/

public interface AuthRoleResRelatedService {
	
	/**
	 * 
	 * 保存或更新角色信息 同时保存角色与资源的关联(保存时手动取sequence设置Id)
	 * @throws Exception 
	 * 
	 * */
	public void saveAuthRole(AuthRole authRole,Long[] resIds) throws Exception;
	/**
	 * 保存Role跟Res之间的关联
	 * */
	public void saveRelated(Long[] resIds,Long roleId) throws Exception;
	/**
	 * 保存或更新资源并同时更新资源与角色之间的关联 。
	 * 新建和更新会导致URL改变 因此需要通知权限MapCache。进行变更
	 * */
	public void saveAuthRes(AuthRes authRes) throws Exception;
	
	/**
	 * 批量删除角色并同时删除 角色-用户、角色-资源 之间的关联
	 * @throws Exception 
	 * */
	public void removeRolesAndAllRelated(final String[] ids) throws Exception;
	/**
	 * 删除资源 并同时删除资源和角色的关联
	 */
	public void removeRes(Long resId)throws Exception;
	/**
	 * #最复杂的操作尽量少用#
	 * 删除机构 并且删除机构下的所有角色（同时删除角色的各种关联），然后将该机构下的所有用户都提到该机构的父机构下
	 * @throws Exception 
	 * */
	public void removeOrgAndAllRelated(Long orgId) throws Exception;
	/**
	 * 按Id查询角色并查出该角色所有关联的资源
	 * 结果放在Map里 其中"角色"放在键<authRole>中。关联的资源放在键<authResList>中
	 * @throws Exception 
	 * */
	public Map getRoleAndRelatedRes(Long roleId) throws Exception;
	
	/**
	 * 测试用
	 * */
	public Map __getCache();
}
