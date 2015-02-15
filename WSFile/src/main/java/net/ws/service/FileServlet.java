package net.ws.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ws.data.JSonHandle;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class FileServlet extends HandleCre {
	 private HttpClient  client = new DefaultHttpClient();
	 private JSonHandle json;
	
	 
	public void exchangeToken (HttpServletRequest req) throws ClientProtocolException, IOException{
		
        HttpPost post = new HttpPost("https://accounts.google.com/o/oauth2/token");
        List <NameValuePair>pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("code", req.getParameter("code")));
        pairs.add(new BasicNameValuePair("client_id", "86839799265-1g56dvmspj1aj29f2a5jic1b2hk3jvgt.apps.googleusercontent.com"));
        pairs.add(new BasicNameValuePair("client_secret", "DMPVwn7KLiwJ9NW40Jj6B5W7"));
       pairs.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/file"));
        pairs.add(new BasicNameValuePair("grant_type", "authorization_code")); //Leave this line how it is   
        post.setEntity(new UrlEncodedFormEntity(pairs));
        org.apache.http.HttpResponse response = client.execute(post);
        String responseBody = EntityUtils.toString(response.getEntity());
        this.json = new JSonHandle();
        this.json.writeJson(responseBody, "src/main/webapp/WEB-INF/token.json");
        
       
	}
	public void geFileList () throws ClientProtocolException, IOException{
		
		 this.json = new JSonHandle();
	     String token =this.json.readJson("src/main/webapp/WEB-INF/token.json");
		HttpGet request= new HttpGet("https://www.googleapis.com/drive/v2/files");
        request.setHeader("Authorization", "Bearer "+token);
        org.apache.http.HttpResponse respo=client.execute(request);
        String therespond = EntityUtils.toString(respo.getEntity());
       this.json.writeJson(therespond, "src/main/webapp/WEB-INF/File-Info/filegoogle.json");
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
        this.exchangeToken(req);
        this.geFileList();
			 
	}
}
