package com.apabi.r2k.admin.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.apabi.r2k.admin.model.ClientVersion;
import com.apabi.r2k.admin.service.ClientVersionService;
import com.apabi.r2k.admin.service.HomePageService;
import com.apabi.r2k.common.utils.DevTypeEnum;
import com.apabi.r2k.common.utils.PropertiesUtil;

import freemarker.template.Template;

@Service("homePageService")
public class HomePageServiceImpl implements HomePageService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Resource
	private ClientVersionService clientVersionService;
	@Resource
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	public void init(){
		publish();
	}
	
	@Override
	public void publish(){
		Map<String,Object> rootMap = new HashMap<String,Object>();
		Writer out = null;
        try {
        	log.info("开始发布首页");
        	List<ClientVersion> verlist = clientVersionService.getAllTypeLatestVersion();
        	ClientVersion androidLarge = null;
        	ClientVersion iPad = null;
        	ClientVersion iPhone = null;
        	for (ClientVersion client : verlist) {
				if(client.getDevType().equals("Android-Large#Android-Portrait")){
					androidLarge = client;
				} else if(client.getDevType().equals(DevTypeEnum.iPad.getName())){
					iPad = client;
				} else if(client.getDevType().equals(DevTypeEnum.iPhone)){
					iPhone = client;
				}
			}
        	if(androidLarge != null){
        		rootMap.put("androidLarge", androidLarge);
        	}
        	if(iPad != null){
        		rootMap.put("iPad", iPad);
        	}
        	if(iPhone != null){
        		rootMap.put("iPhone", iPhone);
        	}
        	rootMap.put("r2kUrl", PropertiesUtil.getValue("url.base.r2k"));
        	rootMap.put("asstUrl", PropertiesUtil.get("url.base.r2kfile")+"/"+PropertiesUtil.get("path.soft.tool")+"/"+PropertiesUtil.get("name.r2kAssist"));
        	String htmlPath = PropertiesUtil.getRootPath() + "/"+PropertiesUtil.getValue("base.r2k");
        	File newFile = new File(htmlPath + "/index.html.new");
            out = new OutputStreamWriter(new FileOutputStream(newFile),"UTF-8");  
            
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("index.ftl");  
            template.process(rootMap, out);
            out.close();
            //删除旧文件，拷贝新文件
            File oldFile = new File(htmlPath + "/index.html");
            FileUtils.deleteQuietly(oldFile);
            FileUtils.moveFile(newFile,oldFile);
            log.info("首页发布成功");
        }catch(Exception e){
        	e.printStackTrace();
        } finally { 
            if (null != out)  
                try {  
                    out.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
        }
	}

}
