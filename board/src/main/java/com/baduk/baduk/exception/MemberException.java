package com.baduk.baduk.exception;

import com.baduk.baduk.constants.excpetion.CommonExceptionConst;

public class MemberException extends CommonException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4437438283265588194L;
	
	public MemberException(CommonExceptionConst exceptionConst) {
		super(exceptionConst);
	}
	
	public MemberException(CommonExceptionConst exceptionConst,String message) {
		super(exceptionConst,message);
	}
}