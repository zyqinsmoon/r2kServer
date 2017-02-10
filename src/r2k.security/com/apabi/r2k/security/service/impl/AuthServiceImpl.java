package com.apabi.r2k.security.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.apabi.r2k.security.model.AuthEntity;
import com.apabi.r2k.security.service.AuthEntityService;
import com.apabi.r2k.security.service.AuthService;


@Service("authService")
@SuppressWarnings("unchecked")
public class AuthServiceImpl implements AuthService{
	
	@Resource
	private AuthEntityService authEntityService;

	/**
	 * 获取权限树数据 如果是超级管理员则获取所有的entity节点。
	 * 正常的情况是如果一个entity下没有资源 将不会获取这个entity。
	 * 因为是通过先拿到角色所拥有资源再通过资源拿到与他们关联的entity;
	 * @throws Exception 
	 * */
	public List<AuthEntity> getAuthTree(String[] roleIds) throws Exception{		
		List<AuthEntity>authEntityList;	
		authEntityList = authEntityService.getAuthTree(roleIds);
		return authEntityList;
	}
}
