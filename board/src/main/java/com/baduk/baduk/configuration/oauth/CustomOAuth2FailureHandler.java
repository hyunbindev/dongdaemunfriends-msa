package com.baduk.baduk.configuration.oauth;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler{

	/*
	 * oAuth로그인 실패시 cookie 초기화 및 500 status 리턴
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		Cookie deleteCookie = new Cookie("refreshToken",null);
		deleteCookie.setMaxAge(0);
		response.addCookie(deleteCookie);
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		exception.printStackTrace();
	}
}
