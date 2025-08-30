package com.baduk.baduk.constants.excpetion;

import org.springframework.http.HttpStatus;

public enum BlameExceptionConst implements CommonExceptionConst{
	NO_BLAME("저격글이 존재하지 않습니다.","BLAME_001",HttpStatus.NOT_FOUND),
	NO_BLAME_COMMENT("저격글의 댓글이 존재하지 않습니다.","BLAME_002",HttpStatus.NOT_FOUND);
	private final String msg;
	private final String code;
	private final HttpStatus httpStatus;
	
	BlameExceptionConst(String msg, String code, HttpStatus httpStatus) {
		this.msg = msg;
		this.code = code;
		this.httpStatus = httpStatus;
	}
	
	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
