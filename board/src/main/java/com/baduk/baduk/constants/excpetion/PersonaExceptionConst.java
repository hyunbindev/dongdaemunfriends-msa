package com.baduk.baduk.constants.excpetion;

import org.springframework.http.HttpStatus;

public enum PersonaExceptionConst implements CommonExceptionConst{
	NO_Persona("Persona가 존재하지 않습니다","PERSONA_001",HttpStatus.NOT_FOUND),
	DUPLICATED_PERSONA_ATTEMPT("이미 작성자 추측을 시도 했습니다.","PERSONA_002",HttpStatus.LOCKED);
	private final String msg;
	private final String code;
	private final HttpStatus httpStatus;
	
	PersonaExceptionConst(String msg, String code, HttpStatus httpStatus){
		this.msg = msg;
		this.code = code;
		this.httpStatus = httpStatus;
	}
	
	@Override
	public String getMsg() {
		// TODO Auto-generated method stub
		return this.msg;
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return this.code;
	}

	@Override
	public HttpStatus getHttpStatus() {
		// TODO Auto-generated method stub
		return this.httpStatus;
	}

}
