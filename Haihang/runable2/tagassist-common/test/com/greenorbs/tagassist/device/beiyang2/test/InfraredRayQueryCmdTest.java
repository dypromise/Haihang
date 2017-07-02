package com.greenorbs.tagassist.device.beiyang2.test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import com.greenorbs.tagassist.device.beiyang2.BeiyangInfraredRayQueryCmd;
import com.greenorbs.tagassist.device.beiyang2.BeiyangSerialCmd;

import edu.emory.mathcs.backport.java.util.Arrays;

public class InfraredRayQueryCmdTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testToBytes() {
		BeiyangInfraredRayQueryCmd cmd = new BeiyangInfraredRayQueryCmd();
		byte[] result = new byte[] { 0x06, (byte) 0xff, (byte) 0x80, 0x00,
				(byte) 0x86, 0x03 };
		
		System.out.println(Arrays.toString(cmd.toBytes()));
		System.out.println(Arrays.toString(result));

		for (int i = 0; i < result.length; i++) {
			assertEquals(cmd.toBytes()[i], result[i]);
		}
	}

}
