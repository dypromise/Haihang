package com.greenorbs.tagassist.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.greenorbs.tagassist.Configuration;
import com.greenorbs.tagassist.ConfigurationException;

public class ConfigurationTest {

	@Test
	public void testSaveProperty() throws ConfigurationException{

		Configuration.saveProperty("a[@b]", "xyz");
		
		assertEquals(Configuration.getString("a[@b]"),"xyz");
		
	}

}
