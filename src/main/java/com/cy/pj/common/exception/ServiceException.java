package com.cy.pj.common.exception;

/**
 * 自定义异常:目的是对错误信息进行更加清晰地描述
 * 
 * */
public class ServiceException extends RuntimeException{

	private static final long serialVersionUID = 5843835376260549700L;
	public ServiceException() { 
		super(); 
	} 
	public ServiceException(String message) { 
		super(message);

	} public ServiceException(Throwable cause) {
		super(cause);

	}

}
