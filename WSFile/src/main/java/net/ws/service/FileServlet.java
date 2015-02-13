package net.ws.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import sun.net.www.protocol.http.HttpURLConnection;
import net.ws.object.ClientFile;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class FileServlet extends HandleCre {
	

	private String downloadFileContent(Drive service, File file)
			throws IOException {
		GenericUrl url = new GenericUrl(file.getDownloadUrl());
		HttpResponse response = service.getRequestFactory()
				.buildGetRequest(url).execute();
		try {
			return new Scanner(response.getContent()).useDelimiter("\\A")
					.next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//	resp.sendRedirect("https://www.googleapis.com/drive/v2/files?code="+req.getParameter("code"));
		System.out.println(req.getParameter("code"));
		HttpClient  client = new DefaultHttpClient();
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
       /* System.out.println(responseBody);
        System.out.println("response:-------------------");
		System.out.println(response.getStatusLine());
		Header[] headers = response.getAllHeaders();
		for(Header h:headers){
			System.out.println(h.getName() + ": " + h.getValue());
		}*/
        
        HttpGet request= new HttpGet("https://www.googleapis.com/drive/v2/files");
        request.setHeader("Authorization", "Bearer "+"ya29.GQEXozQza5qWB3OfU-zNOdbLcplR_jcAiGZ86YtiDLv0F-yaqs1IRxEEoOUtP8oZyQy5WaZF-fLmmw");
        org.apache.http.HttpResponse respo=client.execute(request);
        String therespond = EntityUtils.toString(respo.getEntity());
        System.out.println(therespond);
        
			 
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Drive service = getDriveService(getCredential(req, resp));
		ClientFile clientFile = new ClientFile(req.getReader());
		File file = clientFile.toFile();

		if (!clientFile.content.equals("")) {
			file = service
					.files()
					.insert(file,
							ByteArrayContent.fromString(clientFile.mimeType,
									clientFile.content)).execute();
		} else {
			file = service.files().insert(file).execute();
		}
		sendJson(resp, file.getId());
	}
}
