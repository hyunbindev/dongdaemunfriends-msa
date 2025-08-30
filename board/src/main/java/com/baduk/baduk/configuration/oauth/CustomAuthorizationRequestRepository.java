package com.baduk.baduk.configuration.oauth;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @auth hyunbinDev
 * @since 2025-08-22
 * oauth2 provider 에게 인증 받기위해 redirect 될때 세션을 생성하기때문
 * 완전한 stateless 방식으로 전환하여 was clustering을 하기 위함
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest>{
	private final RedisTemplate<String, OAuth2AuthorizationRequest> redisTemplate;
	
	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		
		return redisTemplate.opsForValue().get(request.getParameter("state"));
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
			HttpServletResponse response) {
		//3초간 상태 저장
		redisTemplate.opsForValue().set(authorizationRequest.getState(), authorizationRequest, Duration.ofSeconds(3));
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
			HttpServletResponse response) {
		return redisTemplate.opsForValue().get(request.getParameter("state"));
	}

}
