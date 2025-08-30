package com.baduk.baduk.exception;

import com.baduk.baduk.constants.excpetion.CommonExceptionConst;

public class FileIOException extends CommonException{
	
	private static final long serialVersionUID = -6284496095390515385L;

	public FileIOException(CommonExceptionConst exceptionConst) {
		super(exceptionConst);
	}
	
	public FileIOException(CommonExceptionConst exceptionConst,String message) {
		super(exceptionConst,message);
	}
}