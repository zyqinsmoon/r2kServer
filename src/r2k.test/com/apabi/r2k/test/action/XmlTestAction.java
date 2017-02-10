package com.apabi.r2k.test.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.HttpEntity;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.util.XMLErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.apabi.r2k.common.utils.HttpUtils;
import com.apabi.r2k.test.model.XmlTest;
import com.apabi.r2k.test.model.Xsd;

/**
 * XML自动测试框架
 * @author l.wen
 *
 */
@Controller("xmlTestAction")
public class XmlTestAction {
	
	final Logger log = LoggerFactory.getLogger(XmlTestAction.class);
	
	private static PropertiesConfiguration propertiesConfig; 
	private static final String propertiesUrl = "properties/xmlTest.properties";
	public static final String CONFIG_XML_TEST_PREFIXX = "xml.test.name";
	public static final String CONFIG_XML_PREFIXX = "url.xml.";
	public static final String CONFIG_XML_XSD_PREFIXX = "url.xml.xsd.";
	public static final String CONFIG_XSD_PREFIXX = "url.xsd.name";
	public static final String CONFIG_XSD_URL_PREFIXX = "url.xsd.url.";
	private static List<XmlTest> staticXmlTests;
	private static Map<String,Xsd> staticXsds;
	private XmlTest xmlTest;
	
	static{
		try {
			setPropertiesConfig(propertiesUrl);
			staticXsds = new HashMap<String, Xsd>();
			staticXsds.putAll(getAllXsd());
			staticXmlTests = new ArrayList<XmlTest>();
			staticXmlTests.addAll(getAllXmlProperties());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String toXmlTest() throws Exception {
		if(staticXmlTests == null){
			staticXmlTests = new ArrayList<XmlTest>();
		}
		return "toXMLTest";
	}
	
	public String add() throws Exception {
		if(xmlTest != null) {
			XmlTest xt = new XmlTest();
			xt.setXmlName(xmlTest.getXmlName());
			xt.setXmlUrl(xmlTest.getXmlUrl());
			xt.setIsTested(XmlTest.XMLTEST_NOT_TESTED);
			xt.setXsd(staticXsds.get(xmlTest.getXsd().getName()));
			staticXmlTests.add(xt);
			saveXmlTest(xt);
		}
		return "toXMLTest";
	}
	
	public String delete() throws Exception {
		if(xmlTest != null){
			String xmlTestName = xmlTest.getXmlName();
			if(xmlTestName != null){
				int size = staticXmlTests.size();
				for(int i =0; i < size; i++){
					XmlTest xt = staticXmlTests.get(i);
					if(xmlTestName.equals(xt.getXmlName())){
						staticXmlTests.remove(xt);
						removeXmlTest(xt);
						break;
					}
				}
			}
		}
		return "toXMLTest";
	}
	
	private void removeXmlTest(XmlTest xmlTest) throws Exception {
		if(xmlTest != null){
			String xmlName = xmlTest.getXmlName();
			propertiesConfig.clearProperty(CONFIG_XML_TEST_PREFIXX+"."+xmlName);
			propertiesConfig.clearProperty(CONFIG_XML_PREFIXX+xmlName);
			propertiesConfig.clearProperty(CONFIG_XML_XSD_PREFIXX+xmlName);
			propertiesConfig.save();
		}
	}
	
	public String autoXmlTest() throws Exception{
		log.info("autoXmlTest:xml校验开始");
		List<XmlTest> xmlTests = checkXml(staticXmlTests);
		int size = xmlTests.size();
		for(int i = 0; i < size; i++){
			XmlTest xmlTest = xmlTests.get(i);
			if(xmlTest.getResult() != null){
				log.info("无效xml数据:"+xmlTest.getXmlName()+",文件位置:"+xmlTest.getXmlUrl());
			}
		}
		return "toXMLTest";
	}
	
	//读取配置文件
	public static void setPropertiesConfig(String fileUrl) throws Exception {
		propertiesConfig = new PropertiesConfiguration(fileUrl);
	}
	
	//获取配置项
	public static String getProperty(String key) throws Exception {
		return propertiesConfig.getString(key);
	}
	
	//获取校验文件信息
	public static Map<String,Xsd> getAllXsd() throws Exception {
		Map<String,Xsd> xsds = new HashMap<String, Xsd>();
		Iterator iter = propertiesConfig.getKeys(CONFIG_XSD_PREFIXX);
		while(iter.hasNext()){
			String key = (String) iter.next();
			String xsdName = key.substring(CONFIG_XSD_PREFIXX.length()+1);
			String xsdUrl = propertiesConfig.getString(CONFIG_XSD_URL_PREFIXX+xsdName);
			Xsd xsd = new Xsd(xsdName, xsdUrl);
			xsds.put(xsdName, xsd);
		}
		return xsds;
	}
	
	//获取全部配置
	public static List<XmlTest> getAllXmlProperties() throws Exception {
		List<XmlTest> list = new ArrayList<XmlTest>();
		Iterator iter = propertiesConfig.getKeys(CONFIG_XML_TEST_PREFIXX);
		while(iter.hasNext()){
			String key = (String) iter.next();
			String xmlTestName = propertiesConfig.getString(key);
			if(xmlTestName != null){
				String xmlUrl = propertiesConfig.getString(CONFIG_XML_PREFIXX+xmlTestName);
				String xsdName = propertiesConfig.getString(CONFIG_XML_XSD_PREFIXX+xmlTestName);
				XmlTest xmlTest = new XmlTest();
				xmlTest.setXmlName(xmlTestName);
				xmlTest.setXmlUrl(xmlUrl);
				xmlTest.setXsd(staticXsds.get(xsdName));
				xmlTest.setIsTested(XmlTest.XMLTEST_NOT_TESTED);
				list.add(xmlTest);
			}
		}
		return list;
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
	
	//检查xml
	public List<XmlTest> checkXml(List<XmlTest> xmlTests) throws Exception {
		SAXParser saxParser = getSAXParser();
		SAXReader saxReader = new SAXReader();
		int size = xmlTests.size();
		String root = ServletActionContext.getServletContext().getRealPath("/");
		for(int i = 0; i < size; i++){
			XmlTest xmlTest = xmlTests.get(i);
			String xmlUrl = xmlTest.getXmlUrl();
			String xsdUrl = root + "/" + xmlTest.getXsd().getUrl();
			if(xsdUrl != null || !xsdUrl.isEmpty()){
				Document xmlDocument = null;
				if(xmlUrl.indexOf("http") !=-1){
					xmlDocument = getXMLFromNet(xmlUrl,saxReader);
				} else {
					File file = new File(xmlUrl);
					if(file.exists()){
						xmlDocument = saxReader.read(new File(xmlUrl));
					}else{
						xmlTest.setResult("xml文件不存在");
					}
				}
				if(xmlDocument!=null){
					saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", "file:"+xsdUrl);
					String result = checkSingleXml(xmlDocument, saxParser);
					xmlTest.setResult(result);
				}
			}
			xmlTest.setIsTested(XmlTest.XMLTEST_IS_TESTED);
		}
		return xmlTests;
	}
	
	//从网络流中获取xml文件
	private Document getXMLFromNet(String url, SAXReader saxReader) throws Exception {
		Document xmlDocument = null;
		HttpEntity httpEntity = HttpUtils.httpGet(url);
		xmlDocument = saxReader.read(httpEntity.getContent());
		return xmlDocument;
	}
	
	private void saveXmlTest(XmlTest xmlTest) throws Exception{
		String xmlName = xmlTest.getXmlName();
		propertiesConfig.addProperty(CONFIG_XML_TEST_PREFIXX+"."+xmlName, xmlTest.getXmlName());
		propertiesConfig.addProperty(CONFIG_XML_PREFIXX+xmlName, xmlTest.getXmlUrl());
		propertiesConfig.addProperty(CONFIG_XML_XSD_PREFIXX+xmlName, xmlTest.getXsd().getName());
		propertiesConfig.save();
	}
	
	public static SAXParser getSAXParser() throws Exception {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setValidating(true);
		saxParserFactory.setNamespaceAware(true);
		SAXParser saxParser = saxParserFactory.newSAXParser();
		saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		return saxParser;
	}
	
	public XmlTest getXmlTest() {
		return xmlTest;
	}

	public void setXmlTest(XmlTest xmlTest) {
		this.xmlTest = xmlTest;
	}

	public static List<XmlTest> getStaticXmlTests() {
		return staticXmlTests;
	}

	public static void setStaticXmlTests(List<XmlTest> staticXmlTests) {
		XmlTestAction.staticXmlTests = staticXmlTests;
	}
	
	public static Map<String, Xsd> getStaticXsds() {
		return staticXsds;
	}

	public static void setStaticXsds(Map<String, Xsd> staticXsds) {
		XmlTestAction.staticXsds = staticXsds;
	}

	public static void main(String[] args) throws Exception {
		new XmlTestAction().autoXmlTest();
	}
}
