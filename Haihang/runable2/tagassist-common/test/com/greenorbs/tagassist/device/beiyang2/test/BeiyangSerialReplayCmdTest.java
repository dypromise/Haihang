package com.greenorbs.tagassist.device.beiyang2.test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import com.greenorbs.tagassist.device.beiyang2.BeiyangSerialReplayCmd;

public class BeiyangSerialReplayCmdTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testToBytes() {
		BeiyangSerialReplayCmd cmd = new BeiyangSerialReplayCmd();
		cmd.setRed(BeiyangSerialReplayCmd.ON);
		cmd.setGreen(BeiyangSerialReplayCmd.OFF);
		cmd.setBlue(BeiyangSerialReplayCmd.ON);

	}

}
