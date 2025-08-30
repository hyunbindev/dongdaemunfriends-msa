package com.baduk.baduk.exception;

import com.baduk.baduk.constants.excpetion.CommonExceptionConst;

public class NewsException extends CommonException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7779153467588497591L;

	public NewsException(CommonExceptionConst exceptionConst) {
		super(exceptionConst);
	}
	
	public NewsException(CommonExceptionConst exceptionConst ,String message) {
		super(exceptionConst,message);
	}

}
