package com.apabi.r2k.common.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.util.XMLErrorHandler;

import com.apabi.r2k.common.template.TemplateProcessor;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlUtil {	
	private static XStream xStream;
	static{
		xStream = new XStream(new DomDriver());
		xStream.ignoreUnknownElements();

      }
	

	public static Object xmlToObj(String xml){
		return xStream.fromXML(xml);
	}
	
	public static Document getDataFromSolr(String url) throws Exception {
		HttpEntity entity = HttpUtils.httpGet(url);
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(entity.getContent());
		return doc;
	}
	
	public static List<Node> getNodesFromInputStream(InputStream in, String xpath) throws Exception{
		Document doc = getDocumentFromInputStream(in);
		List<Node> nodes = doc.selectNodes(xpath);
		return nodes;
	}
	
	public static Document getDocumentFromInputStream(InputStream in) throws Exception{
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(in);
		return doc;
	}

	public static Document getDocumentFromString(String xml) throws Exception{
		return DocumentHelper.parseText(xml);
	}
	

	public static String getXmlFromInputStream(InputStream in) throws Exception{
		Document doc = getDocumentFromInputStream(in);
		return doc.asXML();
	}
	
	//根据xpath表达式获取指定节点的值
	public static List getNodes(Document doc,String xpath){
		List list = doc.selectNodes(xpath);
		return list;
	}
	
	//根据xpath表达式获取指定单个节点的值
	public static Object getSingleNodes(Document doc,String xpath){
		Object obj = doc.selectSingleNode(xpath);
		return obj;
	}
	
	//获取parser
	public static SAXParser getSAXParser() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setValidating(true);
		saxParserFactory.setNamespaceAware(true);
		SAXParser saxParser = saxParserFactory.newSAXParser();
		saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		return saxParser;
	}
	
	//检查xml
	public static String checkSingleXml(Document xmlDocument,SAXParser saxParser) throws Exception {
		String result = null;
		XMLErrorHandler xmlErrorHandler = new XMLErrorHandler();
		SAXValidator saxValidator = new SAXValidator(saxParser.getXMLReader());
		saxValidator.setErrorHandler(xmlErrorHandler);
		saxValidator.validate(xmlDocument);
		if(xmlErrorHandler.getErrors().hasContent()){
			result = xmlErrorHandler.getErrors().getStringValue();
		} 
		return result;
	}
	
	public static InputStream createXmlStream(String file, Map model) throws Exception{
		String xml = TemplateProcessor.process(file, model);
		InputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		return in;
	}
	
	public static void main(String[] args) {
//		try {
//			String url = "http://localhost:8080/r2k/api/period/nq.D110000bjwb_20140728";
//			Map<String, String> headers = new HashMap<String, String>();
//			headers.put(HttpHeaders.USER_AGENT, "slave");
//			headers.put("Cookie", "orgid="+"tiyan"+";deviceid="+"fda3141d-77de-4a8d-8924-f5c71fdcb64f");
//			HttpEntity entity = HttpUtils.httpGet(url, headers);
//			Document doc = XmlUtil.getDocumentFromInputStream(entity.getContent());
//			System.out.println(doc.asXML());
//			Attribute idAttr = (Attribute) XmlUtil.getSingleNodes(doc, "//Paper/@id");	//获取报纸id
//			System.out.println(idAttr.getStringValue());
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
