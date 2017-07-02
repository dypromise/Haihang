package com.greenorbs.tagassist.epc.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.greenorbs.tagassist.epc.EPC96;
import com.greenorbs.tagassist.epc.EPCException;

public class EPC96Test {

	@Test
	public void test() throws EPCException {
		EPC96 epc = new EPC96(EPC96.PREFIX_HEX + "0018234567" + "FFF" + "319FC587FF");
		assertEquals("0018234567", epc.getBaggageNumber());
		assertEquals("CP161", epc.getFlightCode());
	}

}
