package net.ws.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class FileDropbox extends HttpServlet {
		private HttpGet getter;
		private HttpPost poster;
		private HttpClient client = new DefaultHttpClient();
		
	public void authentication() throws ClientProtocolException, IOException{
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("response_type", "code"));
		pairs.add(new BasicNameValuePair("client_id","3nixhm6sjqf9aoy"));
		String url = "https://www.dropbox.com/1/oauth2/authorize?"+URLEncodedUtils.format(pairs, "utf-8");
		getter = new HttpGet(url);
		getter.getParams().setParameter(CookieSpecPNames.DATE_PATTERNS, Arrays.asList("EEE, d MMM yyyy HH:mm:ss z"));
		System.out.println(url);
		HttpResponse theresp=client.execute(getter);
		Header[] headers = theresp.getAllHeaders();
		for (Header header : headers) {
			System.out.println("Key : " + header.getName() 
			      + " ,Value : " + header.getValue());
		}
		/*HttpResponse theresp=client.execute(getter);
		String answer = EntityUtils.toString(theresp.getEntity());
		System.out.println(answer);*/
		System.out.println("This is dropbox");
	}
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ClientProtocolException, IOException{
		this.authentication();
	}
}
