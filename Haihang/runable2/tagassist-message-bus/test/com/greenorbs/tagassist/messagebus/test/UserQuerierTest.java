package com.greenorbs.tagassist.messagebus.test;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.greenorbs.tagassist.UserInfo;
import com.greenorbs.tagassist.messagebus.MessageBusException;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.util.querier.UserQuerier;

public class UserQuerierTest {

	@Test
	public void testGetUserInfo() throws MessageBusException {
		Publisher publisher = new Publisher();
		publisher.start();
		
		String username = "admin";
		String password = "admin";
		UserQuerier querier = new UserQuerier(publisher);
		UserInfo userInfo = querier.getUserInfo(username, password);
		assertNotNull(userInfo);
		assertEquals(userInfo.getUsername(), username);
		assertTrue(StringUtils.isEmpty(userInfo.getPassword()));
		
		password = "ADMIN";
		userInfo = querier.getUserInfo(username, password);
		assertNull(userInfo);
	}

}
