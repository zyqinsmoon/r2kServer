package com.apabi.r2k.common.base;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.apabi.r2k.admin.model.Article;
import com.apabi.r2k.admin.model.Column;
import com.apabi.r2k.admin.model.Paper;
import com.apabi.r2k.admin.model.PaperAuth;
import com.apabi.r2k.admin.model.PrjEnum;
import com.apabi.r2k.admin.model.Subscribe;
import com.apabi.r2k.admin.model.Suggest;
import com.apabi.r2k.admin.model.Topic;
import com.apabi.r2k.admin.model.TopicAuth;
import com.apabi.r2k.admin.model.TopicCondition;
import com.apabi.r2k.common.solr.SolrResult;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.common.utils.ShuYuanResult;
import com.apabi.r2k.msg.model.R2kMessage;
import com.apabi.r2k.paper.model.MsgBody;
import com.apabi.r2k.security.model.AuthOrg;
import com.apabi.r2k.wx.model.Category;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ServerModelTrandsform {

	private static XStream xStream;
	public final static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	static{
		initXmlUtils();
	}
	
	private static void initXmlUtils(){
		xStream = new XStream(new DomDriver());
		xStream.setMode(XStream.NO_REFERENCES);
		xStream.ignoreUnknownElements();
		xStream.processAnnotations(Topic.class);
		xStream.processAnnotations(Article.class);
		xStream.processAnnotations(TopicCondition.class);
		xStream.processAnnotations(SolrResult.class);
		xStream.processAnnotations(TopicAuth.class);
		xStream.processAnnotations(PaperAuth.class);
		xStream.processAnnotations(Subscribe.class);
		xStream.processAnnotations(Paper.class);
		xStream.processAnnotations(Suggest.class);
		xStream.processAnnotations(AuthOrg.class);
		xStream.processAnnotations(ShuYuanResult.class);
		xStream.processAnnotations(MsgBody.class);
		xStream.processAnnotations(Column.class);
		xStream.processAnnotations(Category.class);
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
	
	public static String objectToXml(Object object){
		String rootStart="<R2k>\n";
		String rootEnd="</R2k>";
		StringBuilder xml = new StringBuilder(XML_HEAD+rootStart);
		if(object != null){
			if(object instanceof List){
				List list = (List)object;
				if(list.size() > 0){
					Class clazz = list.get(0).getClass();
					int size = list.size();
					String listStart = null;
					String listEnd = null;
					StringBuilder tmpXml = new StringBuilder();
					if(clazz.equals(Topic.class)){
						listStart = "<Topics>\n";
						listEnd = "</Topics>";
						for(int i = 0; i < size; i++){
							Topic topic = (Topic) list.get(i);
							tmpXml.append(xStream.toXML(topic));
						}
					}else if(clazz.equals(Article.class)){
						listStart = "<Articles>\n";
						listEnd = "</Articles>";
						for(int i = 0; i < size; i++){
							Article art = (Article) list.get(i);
							tmpXml.append(xStream.toXML(art));
						}
					}else if(clazz.equals(TopicAuth.class)){
						listStart = "<TopicRecommends id=\""+((TopicAuth)list.get(0)).getOrgId().toLowerCase()+"\">\n";
						listEnd = "</TopicRecommends>";
						for(int i = 0; i < size; i++){
							TopicAuth ta = (TopicAuth) list.get(i);
							tmpXml.append(xStream.toXML(ta));
						}
						
					}else if(clazz.equals(PaperAuth.class)){
						listStart = "<PaperRecommends id=\""+((PaperAuth)list.get(0)).getOrgId().toLowerCase()+"\">\n";
						listEnd = "</PaperRecommends>";
						for(int i = 0; i < size; i++){
							PaperAuth pa = (PaperAuth) list.get(i);
							tmpXml.append(xStream.toXML(pa));
						}
					}else if(clazz.equals(AuthOrg.class)){
						listStart = "<OrgSuggests>\n";
						listEnd = "</OrgSuggests>";
						for(int i = 0; i < size; i++){
							AuthOrg org = (AuthOrg) list.get(i);
							tmpXml.append(xStream.toXML(org));
						}
					}else if(clazz.equals(Column.class)){
						listStart = "<Columns>\n";
						listEnd = "</Columns>";
						for(int i = 0; i < size; i++){
							Column col = (Column) list.get(i);
							tmpXml.append(xStream.toXML(col));
						}
					}
					xml.append(listStart+tmpXml+listEnd);
				}
			}else{
				xml.append(xStream.toXML(object));
			}
		}
		xml.append(rootEnd);
		return xml.toString();
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
	
	public static void main(String[] args) {
		List list = new ArrayList();
		for (int i = 0; i <4; i++) {
			R2kMessage r = new R2kMessage();
			PrjEnum p = new PrjEnum();
			p.setEnumCode("a");
			p.setEnumName("11");
			r.setMsg(p);
			r.setMsgType("ss");
			list.add(r);
		}
		String xml = ServerModelTrandsform.objToXml(list);
		List<R2kMessage> r2k = (List<R2kMessage>)ServerModelTrandsform.xmlToObj(xml);
		for (int i = 0; i < r2k.size(); i++) {
			Object obj = r2k.get(i).getMsg();
			String msgType = r2k.get(i).getMsgType();
			System.out.println(msgType);
		}
		
		System.out.println(r2k.size());
		
	//	System.out.println(xml);
	}
	public static Object xmlToObj(String xml,String alias,Class clazz,String...attributes){
		xStream.alias(alias, clazz);
		for(String attr : attributes){
			xStream.useAttributeFor(clazz, attr);
		}
		return xStream.fromXML(xml);
	} 
}
