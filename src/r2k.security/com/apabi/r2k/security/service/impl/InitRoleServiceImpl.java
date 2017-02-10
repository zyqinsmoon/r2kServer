package com.apabi.r2k.security.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import com.apabi.r2k.security.model.AuthRes;
import com.apabi.r2k.security.service.AuthResService;

public class InitRoleServiceImpl{
	
	@Resource
	private AuthResService authResService;
	
	/**
	 * 单实例
	 */
	private static final InitRoleServiceImpl instance = new InitRoleServiceImpl();
	public static InitRoleServiceImpl getInstance(){
		return instance;	
	}
	//权限资源缓存
	public  Map<String,Collection<ConfigAttribute>> resourceMapCache  =new HashMap<String, Collection<ConfigAttribute>>();

	@SuppressWarnings("unused")
	private void loadConfigDefine() throws Exception{

		
		List<AuthRes> authResRoleList= authResService.loadAllAuthResRole();
		
		for(AuthRes authRes:authResRoleList){
				
				if(resourceMapCache.containsKey(authRes.getResUrl())){
					List<ConfigAttribute> list=(List) resourceMapCache.get(authRes.getResUrl());
					SecurityConfig sc=new SecurityConfig(authRes.getRoleCode());
					if(!list.contains(sc))list.add(sc);
				}
				else {
					List<ConfigAttribute> list=new ArrayList<ConfigAttribute>();
					list.add(new SecurityConfig(authRes.getRoleCode()));
					resourceMapCache.put(authRes.getResUrl(), list);
				}
				
		}
		//System.out.println(resourceMapCache.size());
	}
	
	/**
	 * 批量处理给url去除roleid
	 * @param urlMap key是url value是roleCode,每个url对应一个roleid，一个url对应多个roleCode时存在多个map中然后将map存入list中
	 */
	public void removeMapCache(List<Map<String,String>> urlMapList){
		for(Map<String, String> urlMap:urlMapList){
			for (Map.Entry<String, String> nv : urlMap.entrySet()) {
				removeMapCache(nv.getKey(),nv.getValue());
			
			}
		}
			
	}
	
	/**
	 * 批量处理给url添加roleid
	 * @param urlMapList
	 */
	
	public void putMapCache(List<Map<String,String>> urlMapList){
		for(Map<String, String> urlMap:urlMapList){
			for (Map.Entry<String, String> nv : urlMap.entrySet()) {
				putMapCache(nv.getKey(),nv.getValue());
			
			}
		}
	}
	/**
	 * 清除整个URL映射
	 * */
	public void removeURL(String url){
		if(resourceMapCache.containsKey(url)){
			resourceMapCache.remove(url);
		}
	}
	
	
	/**
	 * 批量处理给url去除roleid
	 * @param url
	 * @param roleId
	 */
	
	public void removeMapCache(String url,String roleCode){
		System.out.println("remove url:"+url+" code:"+roleCode);
		List<ConfigAttribute> roleList = (List)this.resourceMapCache.get(url);
		if(roleList!=null)roleList.remove(new SecurityConfig(roleCode));
	}
	public void removeMapCache(String url){
		if(this.resourceMapCache.containsKey(url))this.resourceMapCache.remove(url);
		}
	
	
	/**
	 * 单条处理给url添加roleid
	 * @param url
	 * @param roleId
	 */
	public void putMapCache(String url,String roleCode){
		System.out.println("add url:"+url+" code:"+roleCode);
		SecurityConfig sc=new SecurityConfig(roleCode);
		List<ConfigAttribute> list=(List<ConfigAttribute>) resourceMapCache.get(url);
		if(list==null){
			list=new ArrayList<ConfigAttribute>();						
		}
		
		if(!list.contains(sc)){
			list.add(new SecurityConfig(roleCode));
			resourceMapCache.put(url,list);
		}
								
	}
	
}
