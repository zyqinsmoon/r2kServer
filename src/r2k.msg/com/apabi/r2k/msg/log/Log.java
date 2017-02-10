package com.apabi.r2k.msg.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.apabi.r2k.common.utils.DateUtil;
import com.apabi.r2k.common.utils.PropertiesUtil;

public class Log {

	private static List<String> logs = new ArrayList<String>();
	
	public static void addLog(String log){
		logs.add(log);
	}
	
	//追加一条日志
	public static void appendLog(Writer writer) throws Exception{
		if(logs.size() > 0){
			writer.append(logs.remove(0));
		}
	}
	
	//追加当前所有日志
	public void appendLogs(){
		final int millseconds = PropertiesUtil.getInt("heartbeat", 3) * 60 * 1000;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Writer writer = null;
				while(true){
					try {
						String logDir = PropertiesUtil.get("msg.log.dir") + "." + DateUtil.currentDate("yyyy-MM-dd");
						System.out.println("--------------日志输出路径:"+logDir);
						File logFile = new File(logDir);
						if(!logFile.getParentFile().exists()){
							logFile.getParentFile().mkdirs();
						}
						if(!logFile.exists()){
							logFile.createNewFile();
						}
						Thread.sleep(millseconds);
						writer = new FileWriter(logFile,true);
						int size = logs.size();
						for(int i = 0; i < size; i++){
							appendLog(writer);
						}
						writer.flush();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							if(writer != null){
								writer.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
}
