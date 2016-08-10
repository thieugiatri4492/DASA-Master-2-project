package net.ws.googledrive;

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
    private GoogleClientSecrets clientSecrets;
    private HttpTransport transport;
    private JsonFactory jsonFactory;
    public static final List<String> SCOPES_REQUEST = Arrays.asList(" https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/drive.file", " https://www.googleapis.com/auth/drive.readonly",
            "https://www.googleapis.com/auth/drive.metadata.readonly", "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/userinfo.profile", "https://www.googleapis.com/auth/drive.appdata",
            "https://www.googleapis.com/auth/drive.apps.readonly");

    public CreManager(GoogleClientSecrets clientSecrets, HttpTransport transport, JsonFactory factory) {
        this.clientSecrets = clientSecrets;
        this.transport = transport;
        this.jsonFactory = factory;
    }

    public Credential buildEmpty() {
        return new GoogleCredential.Builder().setClientSecrets(this.clientSecrets).setTransport(transport)
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
                clientSecrets.getWeb().getClientId(), clientSecrets.getWeb().getRedirectUris().get(0), SCOPES_REQUEST)
                        .setAccessType("offline").setApprovalPrompt("force");
        return urlBuilder.build();
    }

    public Credential retrieve(String code) {
        try {
            GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(transport, jsonFactory,
                    clientSecrets.getWeb().getClientId(), clientSecrets.getWeb().getClientSecret(), code,
                    clientSecrets.getWeb().getRedirectUris().get(0)).execute();
            return buildEmpty().setAccessToken(response.getAccessToken()).setRefreshToken(response.getRefreshToken())
                    .setExpirationTimeMilliseconds(response.getExpiresInSeconds() * 1000);
        } catch (IOException e) {
            System.err.println("An unknown problem occured while retrieving token");
        }
        return null;
    }

}
