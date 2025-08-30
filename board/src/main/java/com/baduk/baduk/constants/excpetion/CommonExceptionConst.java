package com.baduk.baduk.constants.excpetion;

import org.springframework.http.HttpStatus;

public interface CommonExceptionConst {
	public String getMsg();
	public String getCode();
	public HttpStatus getHttpStatus();
}
