package com.apabi.r2k.test.model;

/**
 * xml自动测试实体
 * @author l.wen
 *
 */
public class XmlTest {

	private String xmlName;			//xml名称
	private String xmlUrl;			//xml路径
	private Xsd xsd;				//校验用xsd
	private String result;			//测试结果信息
	private int isTested;			//是否已经测试，0：未测试，1：已测试

	public static final int XMLTEST_IS_TESTED = 1;
	public static final int XMLTEST_NOT_TESTED = 0;
	
	public String getXmlName() {
		return xmlName;
	}

	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}

	public String getXmlUrl() {
		return xmlUrl;
	}

	public void setXmlUrl(String xmlUrl) {
		this.xmlUrl = xmlUrl;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Xsd getXsd() {
		return xsd;
	}

	public void setXsd(Xsd xsd) {
		this.xsd = xsd;
	}

	public int getIsTested() {
		return isTested;
	}

	public void setIsTested(int isTested) {
		this.isTested = isTested;
	}
}
