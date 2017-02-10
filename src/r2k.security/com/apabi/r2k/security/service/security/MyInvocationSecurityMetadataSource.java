package com.apabi.r2k.security.service.security;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import com.apabi.r2k.security.service.impl.InitRoleServiceImpl;


//核心组件 重写springsecurity的SecurityMetadataSource
//在数据库中读取角色与资源之间的关联关系 以替代原版在XML文件读取关联

@Service
public class MyInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	private Map<String,Collection<ConfigAttribute>> resourceMap=null;
	
	@Resource
	private InitRoleServiceImpl initRoleService;
	
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}
	//此函数由Spring SecurityInterceptor调用 将权限资源信息交给决策处理器
	public Collection<ConfigAttribute> getAttributes(Object object)throws IllegalArgumentException {
		this.resourceMap = initRoleService.resourceMapCache;
		String ourl=((FilterInvocation)object).getRequestUrl();
		String url=ourl;
		AntPathMatcher apm=new AntPathMatcher();
		int idx=ourl.indexOf("?");
		if(idx!=-1){
			url=ourl.substring(0, idx);
		}
		for(String key:resourceMap.keySet()){
			if(apm.match(key, url)){
				return resourceMap.get(key);
			}
		}
		return null;
	}

	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return true;
	}


}
