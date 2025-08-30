package com.baduk.baduk.exception;

import com.baduk.baduk.constants.excpetion.CommonExceptionConst;

public class JudgmentException extends CommonException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6203087062734360802L;

	public JudgmentException(CommonExceptionConst exceptionConst) {
		super(exceptionConst);
	}
	public JudgmentException(CommonExceptionConst exceptionConst ,String message) {
		super(exceptionConst,message);
	}
}
