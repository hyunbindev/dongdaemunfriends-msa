package com.baduk.gateway.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthorizationFilter implements GatewayFilter{
	
	private String accessKey;
	private String refreshKey;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			//access token 추출
			List<String> accessToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
			
			if(accessToken.size()<1) {
				//토큰 없을 경우 예외처리
				throw new Exception();
			}
			
		}catch(Exception e) {
			
		}
		return null;
	}
}
