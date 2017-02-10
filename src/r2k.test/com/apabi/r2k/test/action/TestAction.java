package com.apabi.r2k.test.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.xml.sax.SAXException;

import com.apabi.r2k.api.exception.ApiException;
import com.apabi.r2k.common.exception.BusinessException;
import com.apabi.r2k.common.template.TemplateProcessor;
import com.apabi.r2k.common.utils.PropertiesUtil;
import com.apabi.r2k.msg.service.MsgConnector;
import com.apabi.r2k.msg.service.impl.MsgConnectorImpl;
import com.apabi.r2k.test.model.Test;
import com.apabi.r2k.test.service.TestService;
import com.opensymphony.xwork2.ActionSupport;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Controller("testAction")
public class TestAction extends ActionSupport implements ServletResponseAware{
	
	@Autowired
	private TestService testService;
	
	private String result;
	private String type;
	// 建议获得response request 的方式
	private HttpServletResponse response;
	@Resource(name="msgConnector")
	private MsgConnector msgConnector;
	private freemarker.ext.dom.NodeModel doc;
	private String publishPath;
	
	public TestService getTestService() {
		return testService;
	}

	public void setTestService(TestService testService) {
		this.testService = testService;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public MsgConnector getMsgConnector() {
		return msgConnector;
	}

	public void setMsgConnector(MsgConnector msgConnector) {
		this.msgConnector = msgConnector;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String test(){
		String flag = "success";
		System.out.println(".................."+flag);
//		Test test = new Test();
//		test.setName("test6");
//		test.setContent("test6test6test6");
//		testService.save(test);
//		testService.saveTests();
//		testService.saveServiceTest();
//		List<Test> list = testService.list();
//		for(Test t : list){
//			System.out.println("test[name:"+t.getName()+",content:"+t.getContent()+"]");
//		}
		HttpServletRequest req = ServletActionContext.getRequest();
		HttpServletResponse res = ServletActionContext.getResponse();
		try {
			msgConnector.serverHandler(req, res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 自动补齐
	 */
	public void suggest() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String callback = request.getParameter("callback");
			try {
						List<Map<String, String>> suggestList = new ArrayList<Map<String, String>>();
						for (int i = 0; i < 10; i++) {
							Map<String, String> suggestMap = new HashMap<String, String>();
							suggestMap.put("name", "test"+i);
							suggestMap.put("adminName1", "test"+i);
							suggestMap.put("countryName", "test"+i);
							suggestList.add(suggestMap);
						}
						JSONArray jsonArray = JSONArray.fromObject(suggestList);
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("geonames", jsonArray);

						response.setContentType("text/json; charset=utf-8");
						response.getWriter().write(
								callback + "(" + jsonObject + ");");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	// 测试返回类型为xml
	public String xmlResult() throws Exception {
		
		String urlStr = "http://localhost:8080/r2k/api/paper?orgid=tiyan&deviceid=5f38dca8-9c9c-455c-a207-9c9a6e07b1e2";
		HttpURLConnection conn = sendByPost(urlStr,null,"","");
		InputStream in = conn.getInputStream();
		if(null != in) {
			result = new String(StreamUtils.copyToByteArray(in),"UTF-8");
		}
		return "xmlResponse";
	}
	
	
	public HttpURLConnection sendByPost(String path, Map<String, String> params,
			String charsetName,String contentType) throws IOException {
		if(StringUtils.isNotBlank(path)) {
			
			if(StringUtils.isBlank(contentType)) {
				contentType = "application/x-www-form-urlencoded";
			}
			if(StringUtils.isBlank(charsetName)) {
				charsetName = "	utf-8";
			}
			
			StringBuffer sub = new StringBuffer();
			sub.append(path);
			if (null != params && params.size() > 0) {
				if (path.indexOf("?") == -1) {
					sub.append("?");
				} else {
					sub.append("&");
				}
				Iterator<String> it = params.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String value = params.get(key);
					sub.append(key + "=" + URLEncoder.encode(value, charsetName)
							+ "&");
				}
				sub.deleteCharAt(sub.length() - 1);
			}
	
			byte[] entity = sub.toString().getBytes();// 生成实体数据
			int num = entity.length - path.getBytes().length;
			num = num == 0 ? 0 : num - 1;
			HttpURLConnection conn = (HttpURLConnection) new URL(path)
					.openConnection();
					
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);// 允许对外输出数据
			conn.setRequestProperty("User-Agent", "Android-Large");
			conn.setRequestProperty("Content-Type",contentType);
			conn.setRequestProperty("Content-Length", String.valueOf(num));
			
			OutputStream outStream = conn.getOutputStream();
			outStream.write(entity);
			outStream.close();
			outStream.flush();
			return conn;
			}
		return null;
	}
	
	// 测试异常处理 api Exception 响应xml
	public void exceptionMapping() throws Exception {
		
		if(type.equals("business")) {
			throw BusinessException.businessExcepion();
		}else if(type.equals("api")) {
			result = TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1004","返回错误"));
			throw new ApiException(result);
		}else {
			throw new Exception("Common Exception");
		}
	}
	
	// 测试生成xml
	public String xmlStr() {
		result = TemplateProcessor.generateResponse("fail.xml", ApiException.makeMode("-1004","返回错误"));
		return "xmlResponse";
	}
	
	
	public void s (){
		List<Test> list =testService.list();
	    Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < list.size(); i++) {
			Test t = list.get(i);
			String name = t.getName();
		   List<Test> testlist = testService.nameList(name);
		   List<String> listname = new ArrayList<String>();
		   for (int j = 0; j < testlist.size(); j++) {
			  Test test =  testlist.get(j);
			  listname.add(test.getContent().toLowerCase());
		}
		    map.put(name, listname);
		   
			
		}
//		yiche.yiche(map);
	}

	public void client(){
		try {
			String url = "http://localhost:8080/r2k/tt/testtest.do?userid=tiyan";
			((MsgConnectorImpl)msgConnector).clientHttpHandler(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String tempTest(){
		try {
			String xmlpath = PropertiesUtil.getRootPath() + "/r2k/test.xml";
			File xmlfile = new File(xmlpath);
			doc = freemarker.ext.dom.NodeModel.parse(xmlfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "tempTest";
	}
	public String pubTest(){
		Writer out = null;
		try {
			//数据xml路径
			String rootpath = PropertiesUtil.getRootPath();
			String xmlpath = rootpath + "/r2k/test.xml";
			//发布路径
			publishPath = "/test.html";
			String createPath = rootpath + "/r2k/test.html";
			//模板路径
			String temppath = "/test.ftl";
			
			
			Map root = new HashMap();  
			File xmlfile = new File(xmlpath);
			root.put("doc", freemarker.ext.dom.NodeModel.parse(xmlfile));  
			out = new OutputStreamWriter(new FileOutputStream(createPath),"UTF-8");  
			
			Configuration cfg = new Configuration();
			cfg.setEncoding(Locale.getDefault(), "UTF-8");
			cfg.setServletContextForTemplateLoading(ServletActionContext.getServletContext(), "/");
			Template template = cfg.getTemplate(temppath);  
			template.process(root, out);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {  
            if (null != out)  
                try {  
                    out.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
        }   
		return "pubTest";
	}

	public freemarker.ext.dom.NodeModel getDoc() {
		return doc;
	}

	public void setDoc(freemarker.ext.dom.NodeModel doc) {
		this.doc = doc;
	}

	public String getPublishPath() {
		return publishPath;
	}

	public void setPublishPath(String publishPath) {
		this.publishPath = publishPath;
	}
}
