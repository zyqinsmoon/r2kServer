package com.apabi.r2k.common.utils;

import java.lang.reflect.Field;

public class ObjectUtil {

	public static void resetNullValue(Object object) throws Exception{
		Field[] fields = object.getClass().getDeclaredFields();
		for(Field field : fields){
			field.setAccessible(true);
			Object value = field.get(object);
			if(value == null){
				if(field.getType().equals(String.class)){
					field.set(object, field.getType().newInstance());
				}
			}
		}
	}
}
