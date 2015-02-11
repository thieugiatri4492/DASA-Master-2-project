package net.ws.service;

import java.util.HashMap;
import java.util.Map;

import net.ws.object.CreToken;

import com.google.api.client.auth.oauth2.Credential;

public class CreStore {
    private Map<String,CreToken> engineStore;
    private static CreStore cStore =null;
    
    private CreStore(){
    	this.engineStore =new HashMap<String, CreToken>();
    }
    public static CreStore getInstance(){
    	return (cStore ==null)? cStore=new CreStore():cStore;
    }
    public boolean load(String userId, Credential credential){
    	if(cStore.engineStore.containsKey(userId)){
    		CreToken token = cStore.engineStore.get(userId);
    		credential.setAccessToken(token.getaToken());
    		credential.setRefreshToken(token.getrToken());
    		credential.setExpirationTimeMilliseconds(token.getExpiration());
			return true;
    	}
    	else return false;
    }
    public void delete (String userId){
    	cStore.engineStore.remove(userId);
    }
    public void store (String userID, Credential credential){
    	CreToken token =new CreToken(credential.getAccessToken(),credential.getRefreshToken(),
    			                     credential.getExpirationTimeMilliseconds());
    	engineStore.put(userID, token);
    }
}
