package com.project.model.token;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	private final String secretKey = "mySecretKey";
	
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }
	
	public String createAccessToken(int member_id) {
		return Jwts.builder()
				.setSubject(String.valueOf(member_id))
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+600000)) // 10분
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String createRefreshToken(int member_id) {
		return Jwts.builder()
				.setSubject(String.valueOf(member_id))
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+604800000)) // 7일
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public int getMember_id(String token) {
		return Integer.parseInt(Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject());
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}
