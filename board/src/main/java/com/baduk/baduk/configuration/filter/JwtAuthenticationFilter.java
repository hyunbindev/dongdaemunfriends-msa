package com.baduk.baduk.configuration.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import com.baduk.baduk.service.RefreshTokenService;
import com.baduk.baduk.utils.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenService refreshTokenService;
	
	@Value("${spring.auth.redirect}")
	private String redirect;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		/**
		 * healthCheck 제외 로깅
		 * @author hyunbinDev
		 * @since 2025-08-21
		 * 메트릭 정보 조회 로깅은 제외
		 */
		String path = request.getRequestURI();
		if(!path.startsWith("/actuator/")) {
			log.info("{} [{}] :: {} :: {}", 
		             request.getRemoteAddr(), 
		             request.getMethod(), 
		             request.getRequestURL(), 
		             request.getHeader("User-Agent"));	
		}
		
		String accessToken = request.getHeader("Authorization");
		try {
			if(accessToken != null && jwtTokenProvider.validateAccessToken(accessToken)) {
				Authentication auth = jwtTokenProvider.getAuthentication(accessToken);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			doFilter(request, response, filterChain);
		}catch(JwtException e) {
			if(e instanceof ExpiredJwtException) {
				reIssueAccessToken(request,response);
				return;
			}
			if(e instanceof MalformedJwtException) {
	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            response.setContentType("application/json;charset=UTF-8");
	            String json = "{\"error\": \"refresh token이 만료되었거나 잘못되었습니다. 재로그인 해주세요.\"}";
	            response.getWriter().write(json);
	            response.getWriter().flush();
				return;
			}
		}
	}
	
	private void reIssueAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {
		String refreshToken = getRefreshTokenFromCookie(request);
		//리프레시 토큰 없을 경우
		if(refreshToken == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"refresh token이 만료되었거나 잘못되었습니다. 재로그인 해주세요."); 
			return;
		}
		//리프레시 토큰이 있을경우 이용하여 다시 엑세스 토큰 밝급
		try {
			String userUuid = jwtTokenProvider.getUserUuidFromRefreshToken(refreshToken);
			if(refreshTokenService.getToken(userUuid).equals(refreshToken)) {
				String newAccessToken = jwtTokenProvider.createAccessToken(userUuid);
				String redirectUrl = UriComponentsBuilder
						.fromUriString(this.redirect)
						.queryParam("accessToken", newAccessToken)
						.build().toUriString();
				response.sendRedirect(redirectUrl);
			}
		}catch(JwtException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"refresh token이 만료되었거나 잘못되었습니다. 재로그인 해주세요.");
			return;
		}
	}
	
	private String getRefreshTokenFromCookie(HttpServletRequest request) {
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("refreshToken".equals(cookie.getName())) {
	                return cookie.getValue();
	            }
	        }
	    }
	    return null;
	}
	
	//JwtToken Exception Response
	private void responseInvalidTokenResponse(HttpServletResponse response) {
		
	}
}