import java.io.IOException;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpTest {
	String info = "";
	
	public static void main(String[] args) {
		String areaCode = "101010100";
		HttpTest ht = new HttpTest();
		ht.httpTest(areaCode);
		ht.httpTest(areaCode);
	}
	
	public void httpTest(String areaCode){
		String url = "http://www.weather.com.cn/data/sk/101010100.html";; 
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		if("".equals(info)){
			httpGet.setHeader(HttpHeaders.IF_MODIFIED_SINCE, ""+new Date());    
		} else {
			httpGet.setHeader(HttpHeaders.IF_MODIFIED_SINCE, info);    
		}
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			Header h = response.getFirstHeader(HttpHeaders.IF_MODIFIED_SINCE);
			info = h.getValue();
			System.out.println("info = " + info);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
