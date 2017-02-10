package com.apabi.r2k.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.xml.sax.InputSource;

public class JdomUtil {

	public static Document initXML(String xml) {
		StringReader read = new StringReader(xml);
		// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		// 创建一个新的SAXBuilder
		SAXBuilder sb = new SAXBuilder();
		try {
			Document doc = sb.build(source);
			return doc;
		} catch (JDOMException e) {
			// log.error(e.getMessage());
		} catch (IOException e) {
			// log.error(e.getMessage());
		}
		return null;
	}
	
	public static Format FormatXML(){  
        //格式化生成的xml文件，如果不进行格式化的话，生成的xml文件将会是很长的一行...  
        Format format = Format.getCompactFormat();  
        format.setEncoding("utf-8");  
        format.setIndent(" ");  
        return format;  
    }  
	
	/**
	 * 文件备份
	 * @param oldname
	 */
	public static void copyFile(String oldname) throws Exception{
		String bakname = oldname.replace(".xml", "_bak.xml");
		File oldfile = new File(oldname);
		File bakfile = new File(bakname);
		FileUtils.copyFile(oldfile, bakfile);
	}
}
