package com.apabi.r2k.common.service;

import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.ManagedProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.XmlWebApplicationContext;
@Service("beanLoader")
@Lazy(false)
public final class BeanLoader implements ApplicationContextAware {

	private static ApplicationContext ctx = null;

	private BeanLoader() {

	}

	public static Object getBean(String name) {
		return ctx.getBean(name);
	}
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		ctx = arg0;

	}
	public static String  getPropValue(String beanid,String propertyName,String type) throws Exception{
		String beanName = null;
		BeanDefinition bd = ((XmlWebApplicationContext)ctx).getBeanFactory().getBeanDefinition(beanid);
		PropertyValue pv = bd.getPropertyValues().getPropertyValue(propertyName);
		ManagedProperties mp = (ManagedProperties) pv.getValue();
		if(mp!=null){
			Set<Entry<Object,Object>> entries = mp.entrySet();
			for(Entry<Object,Object> e:entries){
				TypedStringValue name = (TypedStringValue) e.getKey();
				if(type.equals(name.getValue())){
					TypedStringValue value = (TypedStringValue) e.getValue();
					beanName = value.getValue();
					break;
						}
				//System.out.println(name.getValue()+","+val.getValue());
			}
	}else{
		throw new Exception("beanid["+beanid+"],propertyName["+propertyName+"]没有取到相应的实现方法");
	}
	    return beanName;
	}
	
}
