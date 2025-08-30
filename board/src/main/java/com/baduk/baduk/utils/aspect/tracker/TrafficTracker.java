package com.baduk.baduk.utils.aspect.tracker;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 *  @author hyunbinDev
 *  
 *  접속자 추적 및 로깅
 */
@Aspect
@Component
@Slf4j
public class TrafficTracker {
	/**
	 * AOP Aspect servletRequetsAttributes 받아 로깅
	 * @param joinPoint
	 */
	@Before("@annotation(com.baduk.baduk.utils.aspect.tracker.TrafficTrackerAnno)")
	public void logTraffic(JoinPoint  joinPoint ) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(attributes == null) {
			log.warn("Request Attributes is null. Unable to log traffic.");
			return;
		}
		printTrafficLog(attributes.getRequest());
	}
	
	/**
	 * @author hyunbinDev
	 * @param request
	 * 
	 * " ip [method] requestURL :: user-agent "형식의 로그 print
	 */
	private void printTrafficLog(HttpServletRequest request) {
		log.info("{} [{}] :: {} :: {}",request.getRemoteAddr(), request.getMethod(), request.getRequestURL(), request.getHeader("User-Agent"));
	}
}
