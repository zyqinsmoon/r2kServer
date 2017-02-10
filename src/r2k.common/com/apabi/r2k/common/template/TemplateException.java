package com.apabi.r2k.common.template;


public class TemplateException extends Exception {

	private static final long serialVersionUID = -1532247619099231683L;
	
	public TemplateException() {
	}

	public TemplateException(final String message) {
		super(message);
	}

	public TemplateException(final Throwable cause) {
		super(cause);
	}

	public TemplateException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
