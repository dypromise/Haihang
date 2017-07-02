package com.greenorbs.tagassist.user;

public class BugUserAuthen implements IUserAuthen {

	@Override
	public int authenticate(String username, String password) {
		return IUserAuthen.SUCCESS;
	}

}
