package com.greenorbs.tagassist.device.impinj.test;

import static org.junit.Assert.*;

import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.impinj.ImpinJReader;

import org.junit.Test;

public class ImpinJReaderTest {

	@Test
	public void testStartup()throws HardwareException {
		ImpinJReader reader = new ImpinJReader();
		reader.setIP("192.168.50.100");
		reader.setReaderConfigFile("specs/SET_READER_CONFIG.xml");
		reader.setRoSpecFile("specs/ADD_ROSPEC.xml");
		reader.startup();
	}

}
