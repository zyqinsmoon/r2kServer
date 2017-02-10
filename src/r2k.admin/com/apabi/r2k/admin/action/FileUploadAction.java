package com.apabi.r2k.admin.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller("fileUploadAction")
public class FileUploadAction implements ServletResponseAware{
	private Logger log = LoggerFactory.getLogger(FileUploadAction.class);
	private File file;
	private String filePath;
	HttpServletResponse response;
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public String upload(){
		HttpServletRequest req = ServletActionContext.getRequest();
		// 计算目录位置
		String dir = System.getProperty("user.dir");
		dir = dir.substring(0, dir.lastIndexOf("bin"));
		String root = dir + "webapps";
		String path = "/r2k/upload/" + java.util.UUID.randomUUID().toString() + ".jpg";
		try {
			File destFile = new File(root + path);
			String rootpathString = root + path;
			//System.out.println(rootpathString);
			destFile.getParentFile().mkdirs();
			FileUtils.copyFile(file, destFile);
			response.getWriter().print(path);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
	public String delete(){
		// 计算目录位置
		String dir = System.getProperty("user.dir");
		dir = dir.substring(0, dir.lastIndexOf("bin"));
		String root = dir + "webapps";
		File file = new File(root + filePath);
		if(file.exists()){
			file.delete();
		}
		return null;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

	
}
