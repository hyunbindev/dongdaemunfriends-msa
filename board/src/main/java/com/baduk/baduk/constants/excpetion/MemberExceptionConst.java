package com.baduk.baduk.constants.excpetion;

import org.springframework.http.HttpStatus;

public enum MemberExceptionConst implements CommonExceptionConst{
	NO_MEMBER("사용자가 존재하지 않습니다.","MEMBER_001",HttpStatus.NOT_FOUND),
	DUPLICATED_PARAMS("중복된 값입니다.","MEMBER_002",HttpStatus.BAD_REQUEST),
	NO_AUTHORITY("권한이 없습니다.", "MEMBER_003", HttpStatus.FORBIDDEN);
	
	private final String msg;
	private final String code;
	private final HttpStatus httpStatus;
	
	MemberExceptionConst(String msg , String code , HttpStatus HttpStatus) {
		this.msg = msg;
		this.code = code;
		this.httpStatus = HttpStatus;
	}
	
	@Override
	public String getMsg() {
		return this.msg;
	}
	
	@Override
	public String getCode() {
		return this.code;
	}
	
	@Override
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
}