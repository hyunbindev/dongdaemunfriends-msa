package com.baduk.baduk.exception;

import com.baduk.baduk.constants.excpetion.CommonExceptionConst;

public class BlameException extends CommonException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7198527534950260229L;
	public BlameException(CommonExceptionConst exceptionConst) {
		super(exceptionConst);
	}
	
	public BlameException(CommonExceptionConst exceptionConst ,String message) {
		super(exceptionConst,message);
	}
}
