package net.ws.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class CreManager {
	private GoogleClientSecrets client_Secret;
	private HttpTransport transport;
	private JsonFactory jsonFactory;
	public static final List<String> SCOPES_REQUEST = Arrays.asList(
			// Required to access and manipulate files.
			" https://www.googleapis.com/auth/drive",
			"https://www.googleapis.com/auth/drive.file",
			" https://www.googleapis.com/auth/drive.readonly",
			"https://www.googleapis.com/auth/drive.metadata.readonly",
			// Required to identify the user in our data store.
			"https://www.googleapis.com/auth/userinfo.email",
			"https://www.googleapis.com/auth/userinfo.profile",
			"https://www.googleapis.com/auth/drive.appdata",
			"https://www.googleapis.com/auth/drive.apps.readonly");
	//private static AppEngineCredentialStore credentialStore = new AppEngineCredentialStore();

	public CreManager(GoogleClientSecrets clientSecrets,
			HttpTransport transport, JsonFactory factory) {
		this.client_Secret = clientSecrets;
		this.transport = transport;
		this.jsonFactory = factory;
	}

	public Credential buildEmpty() {
		return new GoogleCredential.Builder()
				.setClientSecrets(this.client_Secret).setTransport(transport)
				.setJsonFactory(jsonFactory).build();
	}

	public Credential get(String userId) {
		Credential credential = buildEmpty();
		if (CreStore.getInstance().load(userId, credential)) {
			return credential;
		}
		return null;
	}

	public void save(String userId, Credential credential) {
		CreStore.getInstance().store(userId, credential);
	}

	public void delete(String userId) {
		CreStore.getInstance().delete(userId);
	}

	public String getAuthorizationUrl() {
		GoogleAuthorizationCodeRequestUrl urlBuilder = new GoogleAuthorizationCodeRequestUrl(
				client_Secret.getWeb().getClientId(), client_Secret.getWeb()
						.getRedirectUris().get(0), SCOPES_REQUEST)
				.setAccessType("offline").setApprovalPrompt("force");
		return urlBuilder.build();
	}

	public Credential retrieve(String code) {
		try {
			GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(
					transport, jsonFactory, client_Secret.getWeb()
							.getClientId(), client_Secret.getWeb()
							.getClientSecret(), code, client_Secret.getWeb()
							.getRedirectUris().get(0)).execute();
			return buildEmpty()
					.setAccessToken(response.getAccessToken())
					.setRefreshToken(response.getRefreshToken())
					.setExpirationTimeMilliseconds(
							response.getExpiresInSeconds() * 1000);
		} catch (IOException e) {
			new RuntimeException(
					"An unknown problem occured while retrieving token");
		}
		return null;
	}

}
