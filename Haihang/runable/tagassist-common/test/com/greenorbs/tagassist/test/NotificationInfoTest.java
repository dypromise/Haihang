/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-6
 */

package com.greenorbs.tagassist.test;

import static org.junit.Assert.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenorbs.tagassist.NotificationInfo;

public class NotificationInfoTest implements PropertyChangeListener{

	NotificationInfo notification = new NotificationInfo();
	static int count = 0;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.notification.addPropertyChangeListener(NotificationInfo.PROPERTY_CONTENT, this);
		this.notification.addPropertyChangeListener(NotificationInfo.PROPERTY_DELETED, this);
		this.notification.addPropertyChangeListener(NotificationInfo.PROPERTY_EXPIRE, this);
		this.notification.addPropertyChangeListener(NotificationInfo.PROPERTY_TIME, this);
		this.notification.addPropertyChangeListener(NotificationInfo.PROPERTY_UUID, this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetUUID() {
		this.notification.setUUID("1234455");
		assertEquals(this.notification.getUUID(),"1234455");
	}

	@Test
	public void testGetContent() {
		this.notification.setContent("content");
		assertEquals(this.notification.getContent(),"content");
	}

	@Test
	public void testGetTime() {
		this.notification.setTime(new Long(12344));
		assertEquals(this.notification.getTime(),new Long(12344));
	}

	@Test
	public void testGetExpire() {
		
		this.notification.setExpire(new Long(233));
		assertEquals(this.notification.getExpire(), new Long(233));
		
	}

	@Test
	public void testGetDeleted() {
		this.notification.setDeleted(Boolean.TRUE);
		assertEquals(this.notification.getDeleted(), Boolean.TRUE);
	}


	@Test
	public void testClone() throws Exception {
		NotificationInfo n = (NotificationInfo) this.notification.clone();
		n.setContent("avc");
		assertEquals(n.getContent(), "avc");
	}
	
	@Test
	public void testPropertyChange(){
		
		System.out.println(count);
		assertEquals(count, 6);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		System.out.println(evt.getPropertyName());
		
		this.count++;
		
	}

}
