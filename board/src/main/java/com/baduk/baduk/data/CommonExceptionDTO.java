package com.baduk.baduk.data;

import com.baduk.baduk.exception.CommonException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonExceptionDTO {
	private String code;
	private String message;
	public CommonExceptionDTO(CommonException exception) {
		this.code = exception.getCode();
		this.message = exception.getMessage();
	}
}
