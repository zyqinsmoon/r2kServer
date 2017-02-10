package com.apabi.r2k.msg.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * 客户端消息处理结果集合,单例
 * @author l.wen
 *
 */
public class MsgResults {

	private static MsgResults instance = new MsgResults();
	private static List msgQueue;
	
	static{
		msgQueue = new ArrayList();
	}
	
	//获取消息处理结果集合类实例
	public static MsgResults getInstance(){
		return instance;
	}
	
	//获取第一个处理结果消息
	public static Object getTopMsg(){
		if(CollectionUtils.isEmpty(msgQueue)){
			return null;
		}
		return msgQueue.remove(0);
	}
	
	//获取前num个消息
	public static List getMsgs(int num){
		if(CollectionUtils.isEmpty(msgQueue)){
			return null;
		}
		int size = msgQueue.size();
		int count = size >= num? num : size;
		List result = new ArrayList();
		for(int i = 0; i < count; i++){
			result.add(msgQueue.remove(0));
		}
		return result;
	}
	
	//获取所有消息
	public static List getMsgs(){
		if(CollectionUtils.isEmpty(msgQueue)){
			return null;
		}
		List result = new ArrayList();
		result.addAll(msgQueue);
		msgQueue.clear();
		return result;
	}
	
	public static void addMsg(Object obj){
		if(obj != null){
			msgQueue.add(obj);
		}
	}
}
