/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 26, 2012
 */

package com.greenorbs.tagassist;

public class Result {

	public static final int CODE_UNKNOWN = 0;
	public static final int CODE_SUCCESS = 1;
	public static final int CODE_FAILURE = 2;
	public static final int CODE_ERROR = 3;

	private int _code;

	private String _description;

	public static final Result UNKNOWN = new ImmutableResult(CODE_UNKNOWN);
	public static final Result SUCCESS = new ImmutableResult(CODE_SUCCESS);
	public static final Result FAILURE = new ImmutableResult(CODE_FAILURE);
	public static final Result ERROR = new ImmutableResult(CODE_ERROR);

	public Result(int code, String description) {
		_code = code;
		_description = description;
	}

	public Result(int code) {
		this(code, null);
	}

	public Result() {
		this(CODE_UNKNOWN);
	}

	public int getCode() {
		return _code;
	}

	public void setCode(int code) {
		_code = code;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	private static class ImmutableResult extends Result {

		private static final String MESSAGE = "This is an immutable object that cannot be modified. If you want to modify it, please renew a Result object.";

		public ImmutableResult(int code) {
			super(code);
		}

		@Override
		public void setCode(int code) {
			throw new UnsupportedOperationException(MESSAGE);
		}

		@Override
		public void setDescription(String description) {
			throw new UnsupportedOperationException(MESSAGE);
		}
		
	}

}
