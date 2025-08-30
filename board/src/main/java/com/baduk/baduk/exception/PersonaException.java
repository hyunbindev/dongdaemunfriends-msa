package com.baduk.baduk.exception;

import com.baduk.baduk.constants.excpetion.CommonExceptionConst;

public class PersonaException extends CommonException{
	
	private static final long serialVersionUID = 1479137618906750257L;
	
	public PersonaException(CommonExceptionConst exceptionConst) {
		super(exceptionConst);
	}
	public PersonaException(CommonExceptionConst exceptionConst, String message) {
		super(exceptionConst, message);
	}

}
