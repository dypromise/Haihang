package com.greenorbs.tagassist.epc.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.greenorbs.tagassist.epc.EPC96;
import com.greenorbs.tagassist.epc.EPC96Encoder;

public class EPC96EncoderTest {

	@Test
	public void testBinEncode() {
		
		String encoded = EPC96Encoder.binEncode("0018234567" + "CP161");	
		assertEquals(EPC96.PREFIX_BIN + 
				"0000000000011000001000110100010101100111" + "111111111111" +
				"0011000110011111110001011000011111111111", encoded);
		
		encoded = EPC96Encoder.binEncode("0037336130" + "NW028S");
		assertEquals(EPC96.PREFIX_BIN + 
				"0000000000110111001100110110000100110000" + "111111111111" + 
				"0101111000001111110000001010001111011100", encoded);
		
		encoded = EPC96Encoder.binEncode("0037336130" + "RUS1727");
		assertEquals(EPC96.PREFIX_BIN + 
				"0000000000110111001100110110000100110000" + "111111111111" +
				"0110110111100111000001011100100111111111", encoded);
		
	}
	
	@Test
	public void testBinDecode() {
		
		String raw = EPC96Encoder.binDecode(EPC96.PREFIX_BIN + 
				"0000000000011000001000110100010101100111" + "111111111111" +
				"0011000110011111110001011000011111111111");
		assertEquals("0018234567" + "CP161", raw);
		
		raw = EPC96Encoder.binDecode(EPC96.PREFIX_BIN + 
				"0000000000110111001100110110000100110000" + "111111111111" +
				"0101111000001111110000001010001111011100");
		assertEquals("0037336130" + "NW028S", raw);
		
		raw = EPC96Encoder.binDecode(EPC96.PREFIX_BIN + 
				"0000000000110111001100110110000100110000" + "111111111111" +
				"0110110111100111000001011100100111111111");
		assertEquals("0037336130" + "RUS1727", raw);
		
	}
	
	@Test
	public void testHexEncode() {
		
		String encoded = EPC96Encoder.hexEncode("0018234567" + "CP161");	
		assertEquals(EPC96.PREFIX_HEX + "0018234567" + "FFF" + "319FC587FF", encoded);
		
		encoded = EPC96Encoder.hexEncode("0037336130" + "NW028S");
		assertEquals(EPC96.PREFIX_HEX + "0037336130" + "FFF" + "5E0FC0A3DC", encoded);
		
		encoded = EPC96Encoder.hexEncode("0037336130" + "RUS1727");
		assertEquals(EPC96.PREFIX_HEX + "0037336130" + "FFF" + "6DE705C9FF", encoded);
		
	}
	
	@Test
	public void testHexDecode() {
		
		String raw = EPC96Encoder.hexDecode(EPC96.PREFIX_HEX + "0018234567" + "FFF" + "319FC587FF");
		assertEquals("0018234567" + "CP161", raw);
		
		raw = EPC96Encoder.hexDecode(EPC96.PREFIX_HEX + "0037336130" + "FFF" + "5E0FC0A3DC");
		assertEquals("0037336130" + "NW028S", raw);
		
		raw = EPC96Encoder.hexDecode(EPC96.PREFIX_HEX + "0037336130" + "FFF" + "6DE705C9FF");
		assertEquals("0037336130" + "RUS1727", raw);
		
	}

}
