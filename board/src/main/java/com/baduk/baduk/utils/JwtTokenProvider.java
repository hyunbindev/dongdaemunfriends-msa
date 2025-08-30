package com.baduk.baduk.utils;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {
	@Value("${spring.jwt.access.secret}")
	private String accessTokenSecretKey;
	
	@Value("${spring.jwt.access.validity}")
	private long accessTokenValidity;
	
	@Value("${spring.jwt.refresh.sercret}")
	private String refreshTokenSecretKey;
	
	@Value("${spring.jwt.refresh.validity}")
	private long refreshTokenValidity;
	
	private Key accessKey;
	private Key refreshKey;
	
	@PostConstruct
	private void init() {
		this.accessKey = Keys.hmacShaKeyFor(accessTokenSecretKey.getBytes());
		this.refreshKey = Keys.hmacShaKeyFor(refreshTokenSecretKey.getBytes());
	}
	
	private String createKey(String uuid, long expireTime , Key key) {
		Claims claims = Jwts.claims();
		claims.put("uuid", uuid);
		
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime tokenValidity = now.plusSeconds(expireTime);
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(Date.from(now.toInstant()))
				.setExpiration(Date.from(tokenValidity.toInstant()))
				.signWith(key,SignatureAlgorithm.HS256)
				.compact();
	}
	
	private boolean validateToken(String token,Key key) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parse(token);
			return true;
		}catch(JwtException e) {
			throw e;
		}
	}
	
	public String createAccessToken(String uuid) {
		return createKey(uuid, this.accessTokenValidity, this.accessKey);
	}
	
	public String createRefreshToken(String uuid) {
		return createKey(uuid, this.refreshTokenValidity, this.refreshKey);
	}
	
	public boolean validateAccessToken(String token) {
		return validateToken(token,this.accessKey);
	}
	
	public boolean validateRefreshToken(String token) {
		return validateToken(token,this.refreshKey);
	}
	
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(this.accessKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		String uuid = (String)claims.get("uuid");
		UserDetails userDetails = new User(uuid, "", List.of());
		return new UsernamePasswordAuthenticationToken(userDetails, null, null);
	}
	
	public String getUserUuidFromRefreshToken(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(this.refreshKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		return (String)claims.get("uuid");
	}
}
