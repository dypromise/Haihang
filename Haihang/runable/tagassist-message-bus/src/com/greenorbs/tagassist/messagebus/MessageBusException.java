/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 27, 2012
 */

package com.greenorbs.tagassist.messagebus;

public class MessageBusException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8962187969442710939L;

	public MessageBusException(Exception e) {
		super(e);
	}
	
	public MessageBusException(String message) {
		super(message);
	}

}
