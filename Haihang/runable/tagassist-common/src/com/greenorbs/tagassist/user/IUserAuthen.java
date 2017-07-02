package com.greenorbs.tagassist.user;

public interface IUserAuthen {

	public static final int FAILURE = 0;
	
	public static final int SUCCESS = 1;

	public int authenticate(String username, String password);

}
