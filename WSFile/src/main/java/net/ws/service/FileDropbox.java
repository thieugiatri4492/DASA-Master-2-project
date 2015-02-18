package net.ws.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ws.data.JSonHandle;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
		private String therespond;
		private JSonHandle json;
		private String token;
		public void exchangeToken(HttpServletRequest req)
				throws ClientProtocolException, IOException {
			this.poster = new HttpPost("https://api.dropbox.com/1/oauth2/token");
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("code", req.getParameter("code")));
			pairs.add(new BasicNameValuePair("client_id",
					"3nixhm6sjqf9aoy"));
			pairs.add(new BasicNameValuePair("client_secret",
					"ohjlad9lmkqw6zs"));
			pairs.add(new BasicNameValuePair("redirect_uri",
					"http://localhost:8080/filedropbox"));
			pairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
			this.poster.setEntity(new UrlEncodedFormEntity(pairs));
			org.apache.http.HttpResponse response = client.execute(this.poster);
			this.therespond = EntityUtils.toString(response.getEntity());
			this.json = new JSonHandle();
			this.json.writeJson(this.therespond,
					"src/main/webapp/WEB-INF/token2.json");

		}
		public void geFileList() throws ClientProtocolException, IOException {

			this.json = new JSonHandle();
			 token = this.json
					.readJsonToken("src/main/webapp/WEB-INF/token2.json");
			 List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			 	pairs.add(new BasicNameValuePair("file_limit", "10000"));
				pairs.add(new BasicNameValuePair("list", "true"));
				System.out.println("https://api.dropbox.com/1/metadata/auto/?"+URLEncodedUtils.format(pairs, "utf-8"));
			this.getter = new HttpGet("https://api.dropbox.com/1/metadata/auto/?"+URLEncodedUtils.format(pairs, "utf-8"));
			this.getter.setHeader("Authorization", "Bearer " + token);
			org.apache.http.HttpResponse respo = client.execute(this.getter);
			this.therespond = EntityUtils.toString(respo.getEntity());
			this.json.writeJson(this.therespond,
					"src/main/webapp/filedropbox.json");
			
		}
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ClientProtocolException, IOException{
		this.exchangeToken(req);
		this.geFileList();
	}
}
