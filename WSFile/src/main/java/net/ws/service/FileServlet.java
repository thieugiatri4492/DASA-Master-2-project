package net.ws.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.ws.googledrive.HandleCre;
import net.ws.util.JSonHandler;

public class FileServlet extends HandleCre {
    /**
     * 
     */
    private static final long serialVersionUID = -1619056607736087205L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.exchangeToken(req);
        this.getFileList();
        resp.sendRedirect("index.html");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.addFile("", "");
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ClientProtocolException, IOException {
        this.deleteFile("", "");
    }

    private void exchangeToken(HttpServletRequest req) throws ClientProtocolException, IOException {
        HttpPost poster = new HttpPost("https://accounts.google.com/o/oauth2/token");
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("code", req.getParameter("code")));
        pairs.add(new BasicNameValuePair("client_id",
                "86839799265-1g56dvmspj1aj29f2a5jic1b2hk3jvgt.apps.googleusercontent.com"));
        pairs.add(new BasicNameValuePair("client_secret", "DMPVwn7KLiwJ9NW40Jj6B5W7"));
        pairs.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/file"));
        pairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
        poster.setEntity(new UrlEncodedFormEntity(pairs));
        HttpResponse response = new DefaultHttpClient().execute(poster);
        String reponse = EntityUtils.toString(response.getEntity());
        JSonHandler.writeJson(reponse, "src/main/webapp/WEB-INF/token.json");

    }

    private void getFileList() throws ClientProtocolException, IOException {
        String token = JSonHandler.readJsonToken("src/main/webapp/WEB-INF/token.json");
        HttpGet getter = new HttpGet("https://www.googleapis.com/drive/v2/files");
        getter.setHeader("Authorization", "Bearer " + token);
        HttpResponse httpResponse = new DefaultHttpClient().execute(getter);
        String response = EntityUtils.toString(httpResponse.getEntity());
        JSonHandler.writeJson(response, "src/main/webapp/filegoogle.json");

    }

    private void openFile(String fileid) throws ClientProtocolException, IOException {
        HttpGet getter = new HttpGet("https://www.googleapis.com/drive/v2/files/" + fileid);
        String token = JSonHandler.readJsonToken("src/main/webapp/WEB-INF/token.json");
        getter.setHeader("Authorization", "Bearer " + token);
        new DefaultHttpClient().execute(getter);
    }

    private void addFile(String fileId, String newParent) throws ParseException, IOException {
        // add parent file
        HttpPost poster = new HttpPost("https://www.googleapis.com/drive/v2/files/" + fileId + "/parents");
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("id", newParent));
        String token = JSonHandler.readJsonToken("src/main/webapp/WEB-INF/token.json");
        poster.setHeader("Authorization", "Bearer " + token);
        poster.setEntity(new UrlEncodedFormEntity(pairs));
        new DefaultHttpClient().execute(poster);
    }

    private void deleteFile(String fileId, String oldParent) throws ClientProtocolException, IOException {
        // delete parent file
        String token = JSonHandler.readJsonToken("src/main/webapp/WEB-INF/token.json");
        HttpDelete deleter = new HttpDelete(
                "https://www.googleapis.com/drive/v2/files/" + fileId + "/parents/" + oldParent);
        deleter.setHeader("Authorization", "Bearer " + token);
        new DefaultHttpClient().execute(deleter);
    }

}
