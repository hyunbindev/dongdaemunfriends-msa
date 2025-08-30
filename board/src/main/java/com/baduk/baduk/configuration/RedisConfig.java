package com.baduk.baduk.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

@Configuration
public class RedisConfig {
	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int port;
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(host,port);
	}
	/**
	 * JwtToken 관리용 redis Template
	 * @author hyunbinDev
	 * @param connectionFactory
	 * @return
	 */
	@Bean("jwtTokenRedisTemplate")
	public RedisTemplate<String, String> jwtTokenRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		return template;
	}
	/**
	 * NewsViewCount 관리용 redis Template
	 * @author hyunbinDev
	 * @param connectionFactory
	 * @return
	 */
	@Bean("newsViewRedisTemplate")
	public RedisTemplate<String,String> newsViewRedisTemplate(RedisConnectionFactory connectionFactory){
		RedisTemplate<String,String> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		return template;
	}
	
	@Bean("oauthRedisTemplate")
	public RedisTemplate<String, OAuth2AuthorizationRequest> oAuthRedisTemplate(RedisConnectionFactory connectionFactory){
		RedisTemplate<String, OAuth2AuthorizationRequest> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		return redisTemplate;
	}
}
