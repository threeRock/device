package io.jianxun.service;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -7589268055969461273L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}
