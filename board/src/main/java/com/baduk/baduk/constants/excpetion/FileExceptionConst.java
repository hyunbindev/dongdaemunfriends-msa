package com.baduk.baduk.constants.excpetion;

import org.springframework.http.HttpStatus;

public enum FileExceptionConst implements CommonExceptionConst{
	INVALID_FILE_TYPE("잘못된 파일 입니다.","FILE_001",HttpStatus.NOT_FOUND),
	FAIL_TO_SAVE("파일 저장 실패","FILE_002",HttpStatus.INTERNAL_SERVER_ERROR);
	private final String msg;
	private final String code;
	private final HttpStatus httpStatus;
	
	FileExceptionConst(String msg, String code, HttpStatus httpStatus) {
		this.msg = msg;
		this.code = code;
		this.httpStatus = httpStatus;
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
