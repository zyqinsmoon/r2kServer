package com.apabi.r2k.common.email;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.apabi.r2k.common.utils.PropertiesUtil;

@Component("sendMailUtil")
public class SendMailUtil {
	
	@Resource(name="mailSender")
	private MailSender mailSender;
	
    public SendMailUtil() {
    	super();
    }
    
    private String getMailSubject(String templatePath){
    	String subject = null;
    	String filePath = getClass().getClassLoader().getResource(templatePath).getFile();
    	BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)),"utf-8"));
			String ss = "";
			Pattern p = Pattern.compile("<subjectText>(.*?)</subjectText>");
			while((ss = br.readLine()) != null){
				Matcher matcher = p.matcher(ss);
				if(matcher.find()){
					subject = matcher.group(1);
					break;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
	    }
        return subject;    	
    }
   
	public  void sendMail(String emailAdress,Map<String,Object> mailContent) throws Exception{
		try {
			String templatePath = PropertiesUtil.getValue("mail.template");
		    mailSender.setSubject(getMailSubject(templatePath));
		    mailSender.setTo(emailAdress);//设置发件箱
		    mailSender.sendMessage(mailContent,templatePath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(" sendMail false  to "+emailAdress);
		}
	}
}
