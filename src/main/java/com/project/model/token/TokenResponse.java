package com.project.model.token;

public class TokenResponse {

	private String accessToken;
	private String refreshToken;
	private int m_role;
	
	public TokenResponse(String accessToken, String refreshToken, int m_role) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.m_role = m_role;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public int getM_role() {
		return m_role;
	}

	public void setM_role(int m_role) {
		this.m_role = m_role;
	}
	
}
