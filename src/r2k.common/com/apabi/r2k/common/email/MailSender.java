package com.apabi.r2k.common.email;
/*
 * 邮件发送器
 */

import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;

@Component("mailSender")
public class MailSender extends Mail {
	
	@Resource(name="mailSenderConfig")
	private JavaMailSender sender;
	
	@Resource(name="emailFreeMarker")
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	public JavaMailSender getSender() {
		return sender;
	}

	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}

	public FreeMarkerConfigurer getFreeMarkerConfigurer() {
		return freeMarkerConfigurer;
	}

	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.freeMarkerConfigurer = freeMarkerConfigurer;
	}
	
	private String getMailText(Map<String,Object> root,String templateName){  
        String htmlText="";    
           try {    
               //通过指定模板名获取FreeMarker模板实例    
               Template tpl = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);    
               htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl,root);    
           } catch (Exception e) {    
               e.printStackTrace();    
           }    
           return htmlText;    
   } 

	public void sendMessage(Map<String,Object> root,String templateName) throws MessagingException{
		try {
		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg,false,"UTF-8");
		helper.setTo(to);
		helper.setFrom(from, personal);
		helper.setSubject(subject);
		String htmlText = getMailText(root,templateName);
		helper.setText(htmlText, true);
		sender.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
