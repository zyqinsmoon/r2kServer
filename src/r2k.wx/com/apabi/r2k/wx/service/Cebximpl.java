package com.apabi.r2k.wx.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.apabi.r2k.common.utils.DateUtil;
import com.apabi.r2k.common.utils.DigestUtils;
import com.apabi.r2k.common.utils.HttpUtils;

public class Cebximpl{
	
	private String BASEURL_CIRCULATE="http://service.apabi.com/";//借阅基地址
    private String orgid;
	private String userName;
    private String metaid;
	public void downloadcebx(){
		this.orgid= "yitan";
		this.userName=orgid;
		String baseurl="D:\\cebxdownload\\";
		try {
		List<String> mateidlist = getmateidlist();
		for (String mateid:mateidlist) {
			this.metaid = mateid;
			String cfxurl = makeCfxUrl();
			String cebxurl = getcebxurl(cfxurl);
			downcebx(baseurl, cebxurl);
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//this.metaid="m.20130820-ZDLW-889-0267";
		
	}
	public static String getcebxurl(String cfxurl) throws Exception{
		ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
		HttpEntity entity = HttpUtils.httpGet(cfxurl);
		InputStream in = entity.getContent();		  
		int len = 0;
		byte[] buf = new byte[1024];
		while((len = in.read(buf)) != -1){
			outputstream.write(buf,0,len);
		}
		String xml = new String(outputstream.toByteArray(),"utf-16le");
		int st = xml.indexOf("<ContentURI>");
		int en = xml.indexOf("</ContentURI>");
		String contentUri = xml.substring((st+12), en);
		if(contentUri.indexOf(";")>-1){
			contentUri = contentUri.split(";")[0];
		}
		in.close();
	    return contentUri;
	}
	
	public static void downcebx(String baseurl,String cebxurl) throws Exception{
		String[] str = cebxurl.split("\\/");
		String mapPath = str[4];
	    String savepath = baseurl+"\\"+mapPath+"\\";
	    File file=new File(savepath+str[5]);
	    if(!file.getParentFile().exists()){
	    	file.getParentFile().mkdirs();
	    }
	    if(!file.exists()){
	    	file.createNewFile();
	    	 HttpEntity entity = HttpUtils.httpGet(cebxurl);
	 		InputStream in = entity.getContent();	
	    	 OutputStream os=null;
			    try{
			    os=new FileOutputStream(file);
			    byte buffer[]=new byte[4096];
			    long a = System.currentTimeMillis();
			    System.out.println("---------------------begin down["+cebxurl+"]");
			    while((in.read(buffer))!=-1){
			    os.write(buffer);
			    }
			    os.flush();
			    long b = System.currentTimeMillis();
			    System.out.println((b-a)+"---------------------end down["+cebxurl+"]");
			    }catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    in.close();
	    }else{
		    System.out.println("["+savepath+str[5]+"]文件已经存在");

	    }
	   
	}
	
	
	private String makeSign(){
		String key = "apabi";
		String devicetype = "2";
		//md5(metaid+objected+orgcode+devicetype+usercode+ DateTime.Now.Date.ToString("yyyyMMdd") +秘钥)，
		//秘钥是"apabi",例如：
		//m.20080807-m801-w009-042tiyan2testuser00120140513apabi
		String sign = metaid+orgid+devicetype+userName+DateUtil.currentDate("yyyyMMdd")+key;
       return DigestUtils.md5ToHex(sign);
	}
	private String makeCfxUrl(){
		String method= "api/ebook/circulate/gettrigger";
		//metaid = "m.20120924-YPT-889-0403";
		///BASEURL_CIRCULATE +/api/ebook/circulate/gettrigger?metaid=m.20080807-m801-w009-042&usercode=testuser001&objectid=&devicetype=2&type=borrow&orgcode=tiyan&sign=D44E62F09DBBF0E45FDFFAAF2A3777EC
		String url = BASEURL_CIRCULATE+method+"?metaid="+metaid+"&usercode="+userName+"&objectid="+"&devicetype=2&type=borrow&orgcode="+orgid+"&sign="+makeSign();
 	  System.out.println("makeCfxUrl["+url+"]");
    return url;
	}
	
	private List<String> getmateidlist(){
		File file = new File("d:\\book.xml");
		SAXReader saxReader = new SAXReader();
		Document doc ;
		List<String> mateidlist = new ArrayList<String>();
		try {
			doc = saxReader.read(file);
			List<Node> books = doc.selectNodes("//mateid");
			for (Node node :books) {
				mateidlist.add(node.getText().trim());
		}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mateidlist;
		
	}
	
    public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMetaid() {
		return metaid;
	}
	public void setMetaid(String metaid) {
		this.metaid = metaid;
	}
	
	public static void main(String[] args) {
		/*File file = new File("d:\\book.xml");
		SAXReader saxReader = new SAXReader();
		Document doc ;
		List<String> mateidlist = new ArrayList<String>();
		try {
			doc = saxReader.read(file);
			List<Node> books = doc.selectNodes("//mateid");
			for (Node node :books) {
			System.out.println(node.getText());
		}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		Cebximpl c = new Cebximpl();
		c.downloadcebx();
	}
}
