package net.ws.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class AuthDropbox extends HttpServlet{
	public void authentication(HttpServletResponse resp) throws ClientProtocolException, IOException{
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("response_type", "code"));
		pairs.add(new BasicNameValuePair("client_id","3nixhm6sjqf9aoy"));
		pairs.add(new BasicNameValuePair("redirect_uri","http://localhost:8080/filedropbox"));
		String url = "https://www.dropbox.com/1/oauth2/authorize?"+URLEncodedUtils.format(pairs, "utf-8");
		resp.sendRedirect(url);
		System.out.println("This is dropbox");
	}
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		this.authentication(resp);
	
	}
}
