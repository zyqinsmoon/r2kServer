package com.apabi.r2k.common.utils;

import java.io.StringWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ClientModelTrandsform {

	private static XStream xStream;
	public final static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	static{
		initXmlUtils();
	}
	
	private static void initXmlUtils(){
		xStream = new XStream(new DomDriver());
		xStream.setMode(XStream.NO_REFERENCES);
		xStream.ignoreUnknownElements();
		xStream.processAnnotations(ShuYuanResult.class);
	}
	
	public static Object xmlToObject(Object source){
		if(source instanceof String){
			return xmlToObj((String)source);
		}else if(source instanceof Document){
			return xmlToObj(((Document)source).asXML());
		}else if(source instanceof Element){
			return xmlToObj(((Element)source).asXML());
		}else if(source instanceof Node){
			return xmlToObj(((Node)source).asXML());
		}
		return null;
	}
	
	
	public static String listToXml(List list){
		StringBuilder xml = new StringBuilder();
		if(list != null){
			int size = list.size();
			for(int i = 0; i < size; i++){
				xml.append(xStream.toXML(list.get(i)));
			}
		}
		return xml.toString();
	}
	
	public static String objToXml(Object object){
		StringWriter writer = new StringWriter();
		xStream.toXML(object, writer);
		return XML_HEAD+writer.toString();
	}
	
	public static Object xmlToObj(String xml){
		return xStream.fromXML(xml);
	}
}
