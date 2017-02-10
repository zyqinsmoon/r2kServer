package com.apabi.r2k.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class MulticastRunnable implements Runnable{
	Logger log = LoggerFactory.getLogger(this.getClass());
	public final static String SERVICE_INFO = "serverInfo";
	
	@PostConstruct
	public void init(){
		MulticastRunnable multicast = new MulticastRunnable();
		Thread thread = new Thread(multicast);
		thread.start();
	}
	
	@Override
	public void run(){
		//接到请求后，发送服务器信息
		int port = 9000;
		byte[] buffer = new byte[8192];
		MulticastSocket mss = null;
		InetAddress group = null;
		try {
			port = Integer.getInteger(PropertiesUtil.getValue("multicast.port"),9000);
			group = InetAddress.getByName(PropertiesUtil.getValue("multicast.host"));
			mss = new MulticastSocket(port);
			mss.joinGroup(group);
			log.info("服务器成功加入到组播组中");
		}catch (IOException e1) {
			log.error("服务器加入组播组失败");
			e1.printStackTrace();
		}
		//获取本机ip
		String ip = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress().toString();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		if(StringUtils.isBlank(ip)){
			log.error("服务器获取自身IP失败");
			return;
		}
		
		while(true){
			try {
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				mss.receive(dp);
				String str = new String(dp.getData(),0,dp.getLength());
				if(SERVICE_INFO.equals(str)){
					log.info("接收到获取服务器地址请求");
					Document doc = DocumentHelper.createDocument();
					Element r2k = doc.addElement("R2k");
					Element server = r2k.addElement("Serverip");
					server.setText(ip +":" + PropertiesUtil.getValue("server.port"));
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					XMLWriter xmlWriter = new XMLWriter(out);
					xmlWriter.write(doc);
					byte[] buffers = out.toByteArray();
					
					DatagramPacket dps = new DatagramPacket(buffers, buffers.length, group, port);
					log.info("通过组播发送服务器数据");
					mss.send(dps);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
