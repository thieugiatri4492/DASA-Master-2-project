package net.ws.service;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class HandleCre extends HttpServlet{
	protected static final HttpTransport TRANSPORT = new NetHttpTransport();
	protected static final JsonFactory JSON_FACTORY = new JacksonFactory();
	public static final String KEY_SESSION_USERID = "user_id";
	public static final String DEFAULT_MIMETYPE = "text/plain";
	public static final String CLIENT_SECRETS_FILE_PATH= "/WEB-INF/client_secret.json";
	private CreManager credentialManager = null;

	@Override
	public void init() throws ServletException {
		super.init();
		// init credential manager
		credentialManager = new CreManager(
				getClientSecrets(), TRANSPORT, JSON_FACTORY);
	}
	private GoogleClientSecrets getClientSecrets() {
		// TODO: do not read on each request
		Reader reader = new InputStreamReader(getServletContext()
				.getResourceAsStream(CLIENT_SECRETS_FILE_PATH));
		try {
			return GoogleClientSecrets.load(JSON_FACTORY, reader);
		} catch (IOException e) {
			throw new RuntimeException("No client_secrets.json found");
		}
	}
	protected void sendJson(HttpServletResponse resp, int code, Object obj) {
		try {
			// TODO(burcud): Initialize Gson instance for once.
			resp.setContentType("application/json");
			resp.getWriter().print(new Gson().toJson(obj).toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	protected void sendJson(HttpServletResponse resp, Object obj) {
		sendJson(resp, 200, obj);
	}
	protected void sendError(HttpServletResponse resp, int code, String message) {
		try {
			resp.sendError(code, message);
		} catch (IOException e) {
			throw new RuntimeException(message);
		}
	}
	protected void sendGoogleJsonResponseError(HttpServletResponse resp,
			GoogleJsonResponseException e) {
		sendError(resp, e.getStatusCode(), e.getLocalizedMessage());
	}
	protected Credential getCredential(HttpServletRequest req,
			HttpServletResponse resp) {
		String userId = (String) req.getSession().getAttribute(KEY_SESSION_USERID);
		if (userId != null) {
			System.out.println("return succes");
			return credentialManager.get(userId);
		}
		return null;
	}
	protected void loginIfRequired(HttpServletRequest req,
			HttpServletResponse resp) {
		Credential credential = getCredential(req, resp);
		if (credential == null) {
			// redirect to authorization url
			try {
				resp.sendRedirect(credentialManager.getAuthorizationUrl());
			} catch (IOException e) {
				throw new RuntimeException("Can't redirect to auth page");
			}
		}
	}
	protected void handleCallbackIfRequired(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String code = req.getParameter("code");
		System.out.println(code);
		if (code != null) {
			// retrieve new credentials with code
			Credential credential = credentialManager.retrieve(code);
			
			// request userinfo
			Oauth2 service = getOauth2Service(credential);
			try {
				Userinfoplus about = service.userinfo().get().execute();
				String id = about.getId();
				credentialManager.save(id, credential);
				req.getSession().setAttribute(KEY_SESSION_USERID, id);
				 resp.sendRedirect("/file");
			} catch (IOException e) {
				throw new RuntimeException("Can't handle the OAuth2 callback, " + 
						"make sure that code is valid.");
			}
		} else {
			resp.sendRedirect("/auth/login");
		}
		
	}
	protected Oauth2 getOauth2Service(Credential credential) {
		return new Oauth2.Builder(TRANSPORT, JSON_FACTORY, credential).build();
	}
	protected Drive getDriveService(Credential credential) {
		return new Drive.Builder(TRANSPORT, JSON_FACTORY, credential).build();
	}
	protected void deleteCredential(HttpServletRequest req,
			HttpServletResponse resp) {
		String userId = (String) req.getSession().getAttribute(KEY_SESSION_USERID);
		if (userId != null) {
			credentialManager.delete(userId);
			req.getSession().removeAttribute(KEY_SESSION_USERID);
		}
	}
	
}

