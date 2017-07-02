package com.greenorbs.tagassist.device;

public class HardwareException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5077158472263228462L;

	public HardwareException() {
		super();
	}

	public HardwareException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public HardwareException(String message, Throwable cause) {
		super(message, cause);
	}

	public HardwareException(Throwable cause) {
		super(cause);
	}

	public HardwareException(String message) {
		super(message);
	}
	

	
}
