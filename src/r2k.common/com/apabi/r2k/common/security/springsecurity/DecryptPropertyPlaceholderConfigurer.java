package com.apabi.r2k.common.security.springsecurity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;

import com.apabi.r2k.common.security.springsecurity.des.DESUtil;


public class DecryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	// 重新定义父类中的这个同名属性
	 private Resource[] locations;   
	//用于指定密钥文件
	    private String keyLocation; 
	    private String fileEncoding;
	    private boolean ignoreResourceNotFound = false;

	    public void setKeyLocation(String keyLocation)  {
	        this.keyLocation = keyLocation;
	    }
	    public void setLocations(Resource[] locations)  {
	        this.locations = locations;
	    }
		public void setFileEncoding(String encoding) {
			this.fileEncoding = encoding;
		}
		public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
			this.ignoreResourceNotFound = ignoreResourceNotFound;
		}
	    public void loadProperties(Properties props) throws IOException  {
	        if (this.locations != null)  {
	        	
	        	DefaultPropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
	            for (int i = 0; i < this.locations.length; i++)  {
	                Resource location = this.locations[i];
	                if (logger.isInfoEnabled())  {
	                    logger.info("Loading properties file from " + location);
	                }
	                InputStream is = null;
	                try  {
	                    is = location.getInputStream();
	                    
	                 
//	                       //加载密钥
//	                    Key key = DESEncryptUtil.getKey(keyLocation.getInputStream());
//	                    //对属性文件进行解密
//	                    is = DESEncryptUtil.doDecrypt(key, is);
//						//将解密后的属性流装载到props中
	                    if (location.getFilename().endsWith(".xml")) {
							propertiesPersister.loadFromXml(props, is);
						}
						else {
							if (fileEncoding != null) {
								propertiesPersister.load(props, new InputStreamReader(is, this.fileEncoding));
							}
							else {
								
								propertiesPersister.load(props, is);
								//判断是否存在加密的字段
								if(!StringUtils.isEmpty(keyLocation)){
								    System.out.println(keyLocation);
								    String[] securityKey= getSecurityKey(keyLocation);
									for(int j = 0; j < securityKey.length; j++) {
										//解密加密字段
										String desValue = DESUtil.getDesString(props.getProperty(securityKey[j]));
										props.setProperty(securityKey[j], desValue);
									}
								}
							
							}
						}
	                } 	catch (IOException ex) {
						if (ignoreResourceNotFound) {
							if (logger.isWarnEnabled()) {
								logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
							}
						}
						else {
							throw ex;
						}
					}finally  {
	                    if (is != null)
	                        is.close();
	                }
	            }
	        }
	    }
	    //加密多个字段格式为[A,B,C]本方法将字串放进String数字
	  private String[] getSecurityKey(String str){
		 String[] securityKey = str.split("\\,");
         return securityKey;
	  }
	  
	}



