package com.greenorbs.tagassist.webservice.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.greenorbs.tagassist.user.IUserAuthen;
import com.greenorbs.tagassist.webservice.UserService;

public class UserServiceTest {

	@Test
	public void testLogin() {
		UserService userService = new UserService();
		assertEquals(IUserAuthen.SUCCESS, userService.login("cstest", "123"));
		assertEquals(IUserAuthen.FAILURE, userService.login("cstest", ""));
		assertEquals(IUserAuthen.FAILURE, userService.login("cstest", null));
	}

}
