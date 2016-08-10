package net.ws.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.ws.util.JSonHandler;

public class FileDropbox extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = -6199022048135513914L;

    private void exchangeToken(HttpServletRequest req) throws ClientProtocolException, IOException {
        HttpPost poster = new HttpPost("https://api.dropbox.com/1/oauth2/token");
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("code", req.getParameter("code")));
        pairs.add(new BasicNameValuePair("client_id", "3nixhm6sjqf9aoy"));
        pairs.add(new BasicNameValuePair("client_secret", "ohjlad9lmkqw6zs"));
        pairs.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/filedropbox"));
        pairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
        poster.setEntity(new UrlEncodedFormEntity(pairs));
        HttpResponse httpResponse = new DefaultHttpClient().execute(poster);
        String response = EntityUtils.toString(httpResponse.getEntity());
        JSonHandler.writeJson(response, "src/main/webapp/WEB-INF/token2.json");
    }

    private void getFileList() throws ClientProtocolException, IOException {
        String token = JSonHandler.readJsonToken("src/main/webapp/WEB-INF/token2.json");
        HttpPost poster = new HttpPost("https://api.dropbox.com/1/delta");
        poster.setHeader("Authorization", "Bearer " + token);
        HttpResponse httpResponse = new DefaultHttpClient().execute(poster);
        String response = EntityUtils.toString(httpResponse.getEntity());
        JSonHandler.writeJson(response, "src/main/webapp/filedropbox.json");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.getFileList();
        resp.sendRedirect("index.html");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ClientProtocolException, IOException {
        this.exchangeToken(req);
        doPost(req, resp);
    }
}
