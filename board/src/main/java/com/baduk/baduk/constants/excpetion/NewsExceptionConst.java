package com.baduk.baduk.constants.excpetion;

import org.springframework.http.HttpStatus;

public enum NewsExceptionConst implements CommonExceptionConst{
	
	NO_NEWS("기사를 찾을 수 없습니다.","NEWS_001",HttpStatus.NOT_FOUND),
	NO_NEWS_COMMENT("덧글을 찾을 수 없습니다.","NEWS_002",HttpStatus.NOT_FOUND),
	DUPLICATED_NEWS_RECOMMEND("중복된 추천 요청입니다.","NEWS_003",HttpStatus.BAD_REQUEST),
	NO_NEWS_RECOMMEND("추천 내역이 없습니다.","NEWS_004",HttpStatus.BAD_REQUEST);
	
	private final String msg;
	private final String code;
	private final HttpStatus httpStatus;
	
	NewsExceptionConst(String msg, String code, HttpStatus httpStatus){
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
