package net.ws.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ws.data.JSonHandle;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class FileServlet extends HandleCre {
	private HttpClient client = new DefaultHttpClient();
	private HttpGet getter;
	private HttpPost poster;
	private String therespond;
	private String token;
	private JSonHandle json;

	public void exchangeToken(HttpServletRequest req)
			throws ClientProtocolException, IOException {

		this.poster = new HttpPost("https://accounts.google.com/o/oauth2/token");
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("code", req.getParameter("code")));
		pairs.add(new BasicNameValuePair("client_id",
				"86839799265-1g56dvmspj1aj29f2a5jic1b2hk3jvgt.apps.googleusercontent.com"));
		pairs.add(new BasicNameValuePair("client_secret",
				"DMPVwn7KLiwJ9NW40Jj6B5W7"));
		pairs.add(new BasicNameValuePair("redirect_uri",
				"http://localhost:8080/file"));
		pairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
		this.poster.setEntity(new UrlEncodedFormEntity(pairs));
		org.apache.http.HttpResponse response = client.execute(this.poster);
		this.therespond = EntityUtils.toString(response.getEntity());
		this.json = new JSonHandle();
		this.json.writeJson(this.therespond,
				"src/main/webapp/WEB-INF/token.json");

	}

	public void geFileList() throws ClientProtocolException, IOException {

		this.json = new JSonHandle();
		 token = this.json
				.readJsonToken("src/main/webapp/WEB-INF/token.json");
		this.getter = new HttpGet("https://www.googleapis.com/drive/v2/files");
		this.getter.setHeader("Authorization", "Bearer " + token);
		org.apache.http.HttpResponse respo = client.execute(this.getter);
		this.therespond = EntityUtils.toString(respo.getEntity());
		this.json.writeJson(this.therespond,
				"src/main/webapp/filegoogle.json");
		
	}

	public void openFile(String fileid) throws ClientProtocolException,
			IOException {
		this.getter = new HttpGet("https://www.googleapis.com/drive/v2/files/"
				+ fileid);
		 token = this.json
				.readJsonToken("src/main/webapp/WEB-INF/token.json");
		this.getter.setHeader("Authorization", "Bearer " + token);
		org.apache.http.HttpResponse respo = client.execute(this.getter);
		this.therespond = EntityUtils.toString(respo.getEntity());
		System.out.println(therespond);
	}
	public void moveFile_P1(String fileid, String parentnew) throws ParseException, IOException{
		// add parent file
				this.poster = new HttpPost("https://www.googleapis.com/drive/v2/files/"
						+ fileid + "/parents");
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("id", parentnew));
				 token = this.json
						.readJsonToken("src/main/webapp/WEB-INF/token.json");
				this.poster.setHeader("Authorization", "Bearer " + token);
				this.poster.setEntity(new UrlEncodedFormEntity(pairs));
				org.apache.http.HttpResponse response = client.execute(this.poster);
				this.therespond = EntityUtils.toString(response.getEntity());
				System.out.println(this.therespond);
	}
	public void moveFile_P2(String fileid, String parentold)
			throws ClientProtocolException, IOException {
		// delete parent file
		 token = this.json
				.readJsonToken("src/main/webapp/WEB-INF/token.json");
		HttpDelete deleting = new HttpDelete(
				"https://www.googleapis.com/drive/v2/files/" + fileid
						+ "/parents/" + parentold);
		deleting.setHeader("Authorization", "Bearer " + token);
		org.apache.http.HttpResponse respo = client.execute(deleting);
		this.therespond = EntityUtils.toString(respo.getEntity());
		System.out.println(this.therespond);

	}
    
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		this.exchangeToken(req);
		this.geFileList();
		this.openFile("0BwtjpqoMxwEOdnp3N2RNcHhlcmM");
		System.out.println(req.getRequestURI());

	}
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		this.moveFile_P1("", "");
	}
	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ClientProtocolException, IOException{
		this.moveFile_P2("","");
		
	}
}
