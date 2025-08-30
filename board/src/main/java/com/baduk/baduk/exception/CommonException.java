package com.baduk.baduk.exception;

import org.springframework.http.HttpStatus;

import com.baduk.baduk.constants.excpetion.CommonExceptionConst;

public abstract class CommonException extends RuntimeException{
	private static final long serialVersionUID = 577083991592030467L;
	
	private String code;
	private HttpStatus httpStatus;
	private CommonExceptionConst exceptionConst;
	
	/**
	 * 공통 예외 상수를 기반으로 Exception 생성
	 * @param exceptionConst 공통 예외 인터페이스를 구현한 상수
	 */
	protected CommonException(CommonExceptionConst exceptionConst) {
		super(exceptionConst.getMsg());
		this.code = exceptionConst.getCode();
		this.httpStatus = exceptionConst.getHttpStatus();
		this.exceptionConst = exceptionConst;
	}
	
	/**
	 * 공통 예외 상수를 기반으로 Exception 생성 및 구체적 메세지 기반 Exception
	 * @param exceptionConst 공통 예외 인터페이스를 구현한 상수
	 * @param message 공통 예외 상수 기본 메세지가 아닌 구체적 메세지 기반
	 */
	protected CommonException(CommonExceptionConst exceptionConst, String message) {
		super(message);
		this.code = exceptionConst.getCode();
		this.httpStatus = exceptionConst.getHttpStatus();
		this.exceptionConst = exceptionConst;
	}
	
	/**
	 * 
	 * @return 예외처리 코드 반환
	 */
	public String getCode() {
		return this.code;
	}
	
	/**
	 * 
	 * @return HttpStatus 반환
	 */
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
	/**
	 * 
	 * @return 공통 예외 상수 반환
	 */
	public CommonExceptionConst getExceptionConst() {
		return this.exceptionConst;
	}
}