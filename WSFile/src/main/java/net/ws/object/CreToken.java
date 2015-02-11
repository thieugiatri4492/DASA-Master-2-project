package net.ws.object;

public class CreToken {
	private String aToken;
	private String rToken;
	private long expiration;

	public CreToken(String access, String refresh, long expireTime) {
		this.aToken = access;
		this.rToken = refresh;
		this.expiration = expireTime;
	}
	public String getaToken() {
		return aToken;
	}

	public void setaToken(String aToken) {
		this.aToken = aToken;
	}

	public String getrToken() {
		return rToken;
	}

	public void setrToken(String rToken) {
		this.rToken = rToken;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	
}
