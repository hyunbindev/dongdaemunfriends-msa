package com.baduk.baduk.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.baduk.baduk.configuration.filter.JwtAuthenticationFilter;
import com.baduk.baduk.configuration.oauth.CustomAuthorizationRequestRepository;
import com.baduk.baduk.configuration.oauth.CustomOAuth2FailureHandler;
import com.baduk.baduk.configuration.oauth.CustomOAuth2UserService;
import com.baduk.baduk.configuration.oauth.OAuth2SuccessHandler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * @author hyunbinDev
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
	private final CustomOAuth2UserService customOAuth2userService;
	private final CustomOAuth2FailureHandler customOAuth2FailureHandler;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomAuthorizationRequestRepository authorizationRequestRepository;
	
	@Value("${spring.auth.redirect}")
	private String baseURL;
	
	@PostConstruct
	private void init() {
		int slashIndex = baseURL.indexOf("/", 8);
		if(slashIndex != -1) {
			this.baseURL = this.baseURL.substring(0, slashIndex);
			return;
		}
	}
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            /*.authorizeHttpRequests(auth -> auth
            		.anyRequest().permitAll() // 모든 요청 허용
            )*/
            .csrf(CsrfConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .formLogin(FormLoginConfigurer::disable)
            .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 비활성화
             )
            .oauth2Login(customConfigurer -> customConfigurer
            		.loginPage("/login")
            		.successHandler(oAuth2SuccessHandler)
            		.failureHandler(customOAuth2FailureHandler)
            		/**
            		 * @author hyunbinDev
            		 * @since 2025-08-22
            		 * 리다이렉션 세션정보를 레디스에 저장
            		 */
            		.authorizationEndpoint(authorization -> 
            					authorization.baseUri("/api/oauth2/authorization")
            					.authorizationRequestRepository(authorizationRequestRepository))
            		
            		.redirectionEndpoint(redir -> redir.baseUri("/api/login/oauth2/code/**"))
            		.userInfoEndpoint(endpointConfig ->endpointConfig.userService(customOAuth2userService)))
        	.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/login/**").permitAll()// 허용
                .requestMatchers("/actuator/**").access(this::manageApiPermission)
                .anyRequest().authenticated() // 나머지는 인증 필요
            )
        	//이건 h2 콘솔 땜문
        	.headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
        	.securityContext(security -> security
        			.securityContextRepository(new NullSecurityContextRepository()))
        	//.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
        	.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    /**
     * Spring api gate way에 cors 정책 위임
     * @since 2025-08-25
     * @return
     */
    @Deprecated
    private CorsConfigurationSource corsConfigurationSource() {
    	return request ->{
    		CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH"));
            config.setAllowedOriginPatterns(Collections.singletonList(baseURL));
            config.setAllowCredentials(true);
            config.setExposedHeaders(Collections.singletonList("X-Redirect"));
            return config;
    	};
    }
    /**
     * @author hyunbinDev
     * @param authentication
     * @param object
     * @since 2025-08-14
     * 메트릭 정보 local조회만 가능 하도록 ip 요청 제약
     */
    private AuthorizationDecision manageApiPermission(Supplier<Authentication> auth, RequestAuthorizationContext obj) {
    	//로컬환경
    	IpAddressMatcher localIpMatcher = new IpAddressMatcher("127.0.0.1");
    	//Docker 컨테이너 환경
    	IpAddressMatcher containerIpMatcher = new IpAddressMatcher("10.0.2.0/24");
        return new AuthorizationDecision(containerIpMatcher.matches(obj.getRequest()) || localIpMatcher.matches(obj.getRequest()));
    }
}
