package com.baduk.baduk.constants.excpetion;

import org.springframework.http.HttpStatus;

public enum JudgmentExceptionConst implements CommonExceptionConst{
	NO_JUDGMENT("존재하지 않는 재판 입니다.","JUDGMENT_001",HttpStatus.NOT_FOUND),
	NO_SELECTION("선택지가 존재하지 않습니다.","JUDGMENT_002",HttpStatus.BAD_REQUEST),
	DUPLICATED_VOTE("이미 투표한 사항입니다.","JUDGMENT_003",HttpStatus.CONFLICT),
	NO_JUDGMENT_VOTE("투표가 존재하지 않습니다.","JUDGMENT_004",HttpStatus.NOT_FOUND),
	NO_JUDGMENT_COMMENT("덧글이 존재하지 않습니다","JUDGMENT_005",HttpStatus.NOT_FOUND);
	private final String msg;
	private final String code;
	private final HttpStatus httpStatus;
	
	JudgmentExceptionConst(String msg, String code, HttpStatus httpStatus) {
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
