package com.apabi.r2k.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.apabi.r2k.common.template.TemplateProcessor;

public class HttpUtils {
	
	public static final String PARAM_CHARSET_UTF8 = "UTF-8";
	public static final String SEPARATOR_AND = "&";
	public static final int DEFAULT_TIMEOUT = 60000;
	
	//将目标url请求回来的InputStream写入当前请求url的response中
	public static void generateResponse(String url,HttpServletResponse response) throws Exception, IOException{
		HttpEntity entity = httpGet(url);
		TemplateProcessor.outPutXmlSetResponse(response);
		InputStream  in = entity.getContent();
		OutputStream out = response.getOutputStream();
		int nRead = 0;
		byte[] buf = new byte[4096];
		// read from stream and write to file
		while ((nRead = in.read(buf)) >0) {
			out.write(buf, 0, nRead);
		}
		out.flush();
		out.close();
		in.close();
	}
	
	public static void generateResponse(String url, HttpServletResponse response, String contentType) throws Exception{
		HttpEntity entity = httpGet(url);
		response.setContentType(contentType);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache,no-store");
		InputStream  in = entity.getContent();
		OutputStream out = response.getOutputStream();
		int nRead = 0;
		byte[] buf = new byte[4096];
		// read from stream and write to file
		while ((nRead = in.read(buf)) >0) {
			out.write(buf, 0, nRead);
		}
		out.flush();
		out.close();
		in.close();
	}
	//http请求获取HttpEntity
	public static HttpEntity httpGet(String url) throws Exception{
		return httpGet(url, null);
	}
	
	//http请求获取httpEntity
	public static HttpEntity httpGet(String url, Map<String, String> httpHeaders) throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(DEFAULT_TIMEOUT).setSocketTimeout(DEFAULT_TIMEOUT).build();
		httpGet.setConfig(requestConfig);
		if(httpHeaders != null){
			Set<Entry<String, String>> headEntries = httpHeaders.entrySet();
			for(Entry<String, String> entry : headEntries){
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
		}
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			response = httpclient.execute(httpGet);
			int httpStatusCode = response.getStatusLine().getStatusCode();
			if(httpStatusCode==HttpStatus.SC_OK){
				entity = response.getEntity();
			}else{
				throw new Exception("url["+url+"]httpStatusCode["+httpStatusCode+"]");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				//response.close();
				//httpclient.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return entity;
	}
	
	public static HttpEntity httpPost(String url,Map<String,String> params) throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		List<NameValuePair> nvps = makeParams(params);
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(DEFAULT_TIMEOUT).setSocketTimeout(DEFAULT_TIMEOUT).build();
		httpPost.setConfig(requestConfig);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response = null;
		response = httpclient.execute(httpPost);
		HttpEntity entity =null;
		int httpStatusCode = response.getStatusLine().getStatusCode();
		if(httpStatusCode==HttpStatus.SC_OK){
			entity = response.getEntity();
		}
		return entity;
		
	}
	
	public static List<NameValuePair> makeParams(Map<String, String> params) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (params != null) {
			Set<Entry<String, String>> entries = params.entrySet();
			for (Entry<String, String> entry : entries) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		return nvps;
	}
	public static InputStream httpPostByStream(String strUrl,String data,String dataType) throws Exception{
		URL url = new URL(strUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setUseCaches(false);
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("content-type", dataType);
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("user-agent", "Android-Large");
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
		writer.write(data);
		writer.flush();
		writer.close();
		return conn.getInputStream();
	}
	
	/**
	 * 拼接url参数
	 * @param object	获取url参数的对象，其中对象的属性名为key，属性值为value，不对数组或属性类型为非基本类型的包装类进行特殊处理
	 * @param params	以map形式存放的url参数
	 * @param separator 拼接分隔符，默认为"&"
	 * @param charset	对特殊字符的转义字符集，默认为"UTF-8"
	 * @return	返回格式：key1=val1&key2=val2&key3=val3
	 */
	public static String joinUrlParam(Object object,Map<String, String> params, String separator,String charset) throws Exception{
		if(separator == null){
			separator = SEPARATOR_AND;
		}
		if(charset == null){
			charset = PARAM_CHARSET_UTF8;
		}
		List<String> paramList = new ArrayList<String>();
		if(object != null){
			Field[] fields = object.getClass().getDeclaredFields();
			for(Field field : fields){
				field.setAccessible(true);
				String key = field.getName();
				Object val = field.get(object);
				if(val != null && !Modifier.isStatic(field.getModifiers())){
					paramList.add(key + "=" + URLEncoder.encode(String.valueOf(val), charset));
				}
			}
		}
		if(params != null){
			Set<Entry<String, String>> entries = params.entrySet();
			for(Entry<String, String> entry : entries){
				String key = entry.getKey();
				String val = entry.getValue();
				if(key != null && val != null){
					paramList.add(key + "=" + URLEncoder.encode(val, charset));
				}
			}
		}
		return StringUtils.join(paramList.iterator(), separator);
		
	}
	
	public static String joinUrlParam(Object object,Map<String, String> params) throws Exception{
		return joinUrlParam(object, params, null, null);
	}
	
	public static String joinUrlParam(Map<String, String> params) throws Exception{
		return joinUrlParam(null, params, null, null);
	}
}
