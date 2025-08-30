package com.baduk.baduk.configuration.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.baduk.baduk.service.RefreshTokenService;
import com.baduk.baduk.utils.JwtTokenProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenService refreshTokenService;
	
	@Value("${spring.jwt.refresh.validity}")
	private int refreshTokenValidity;
	@Value("${spring.auth.redirect}")
	private String redirect;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication
			) throws IOException, ServletException {
		String memberUuid = authentication.getName();
		String accessToken = jwtTokenProvider.createAccessToken(memberUuid);
		String refreshToken = jwtTokenProvider.createRefreshToken(memberUuid);
		
		//redis refresh token 저장
		refreshTokenService.saveToken(memberUuid, refreshToken);
		
		//쿠키설정
		Cookie cookie = new Cookie("refreshToken",refreshToken);
		cookie.setHttpOnly(true);
		//https 가 아직 사용 안되는 환경 이기땜문에 false
		cookie.setSecure(false);
		cookie.setPath("/");
		cookie.setMaxAge(refreshTokenValidity);
		response.addCookie(cookie);
		
		//프론트엔드단 리다이렉션
		String redirectUrl = UriComponentsBuilder
				.fromUriString(this.redirect)
				.queryParam("accessToken", accessToken)
				.build().toUriString();
		
		response.sendRedirect(redirectUrl);
	}
}