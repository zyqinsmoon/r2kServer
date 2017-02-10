package com.apabi.r2k.common.solr;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.apabi.r2k.common.base.ServerModelTrandsform;
import com.apabi.r2k.common.utils.HttpUtils;

public class SolrUtil {

	public static final String PARAM_CHARSET_UTF8 = "UTF-8";
	public static final String SEPARATOR_AND = "&";
	
	public static final String HTTP_TYPE_GET = "get";
	public static final String HTTP_TYPE_POST = "post";
	
	public static final String RESULT_FROM = "from";
	public static final String RESULT_TO = "to";
	public static final String RESULT_TOTAL = "total";
	public static final String RESULT_LIST = "result";
	
	//创建solr查询的分页参数
	public static SolrParam getSolrPageParam(int pageNumber, int pageSize){
		SolrParam solrParam = new SolrParam();
		int from = pageSize * (pageNumber - 1) + 1;
		int to = pageNumber * pageSize;
		solrParam.setFrom(from);
		solrParam.setTo(to);
		return solrParam;
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
	
	public static InputStream sendSolrRequest(String url,Object data,String httpType,String dataType) throws Exception{
		InputStream in = null;
		if(httpType.equals(HTTP_TYPE_GET)){
			HttpEntity entity = HttpUtils.httpGet(url);
			in = entity.getContent();
		}else if(httpType.equals(HTTP_TYPE_POST)){
			in = HttpUtils.httpPostByStream(url, (String)data, dataType);
		}
		return in;
	}
	
	//post方式发送solr请求
	public static InputStream postSolrRequest(String url,Object data, String dataType) throws Exception{
		return sendSolrRequest(url, data, HTTP_TYPE_POST, dataType);
	}
	
	//get方式发送solr请求
	public static InputStream getSolrRequest(String url) throws Exception{
		return sendSolrRequest(url, null, HTTP_TYPE_GET, null);
	}
	
	public static Map getSolrResponse(InputStream in, String xpath) throws Exception{
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(in);
		
		Element root = doc.getRootElement();
		List<Node> nodes = root.selectNodes(xpath);
		List results = new ArrayList();
		for(Node node : nodes){
			results.add(ServerModelTrandsform.xmlToObject(node));
		}
		Map response = new HashMap();
		response.put(RESULT_LIST, results);
		Attribute attrTotal = root.attribute(RESULT_TOTAL);
		Attribute attrFrom = root.attribute(RESULT_FROM);
		Attribute attrTo = root.attribute(RESULT_TO);
		if(attrTotal != null){
			response.put(RESULT_TOTAL, Integer.parseInt(attrTotal.getText()));
		}
		if(attrFrom != null){
			response.put(RESULT_FROM, Integer.parseInt(attrFrom.getText()));
		}
		if(attrTo != null){
			response.put(RESULT_TO, Integer.parseInt(attrTo.getText()));
		}
		return response;
	}
}
