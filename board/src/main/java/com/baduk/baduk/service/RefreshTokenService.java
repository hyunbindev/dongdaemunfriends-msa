package com.baduk.baduk.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
	
	private final RedisTemplate<String, String> jwtRedisTemplate;
	
	@Value("${spring.jwt.refresh.validity}")
	private long REFRESH_TOKEN_EXPIRE_TIME;
	
	public RefreshTokenService(@Qualifier("jwtTokenRedisTemplate") RedisTemplate<String, String> jwtRedisTemplate) {
        this.jwtRedisTemplate = jwtRedisTemplate;
    }

	
	public void saveToken(String userUuid, String refreshToken) {
		this.jwtRedisTemplate.opsForValue().set("RT:" + userUuid, refreshToken,REFRESH_TOKEN_EXPIRE_TIME,TimeUnit.SECONDS);
	}
	
	public String getToken(String userUuid) {
		return this.jwtRedisTemplate.opsForValue().get("RT:"+userUuid);
	}
}