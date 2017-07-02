package com.greenorbs.tagassist.util.test;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

import com.greenorbs.tagassist.util.FlightUtils;

public class FlightUtilsTest {
	
	@Test
	public void testParseFlightCode() throws ParseException {
		assertEquals("CP161", FlightUtils.parseFlightCode("CP161/20NOV/YVR/Y"));
		assertEquals("CP161Z", FlightUtils.parseFlightCode("CP161Z/20NOV/YVR/Y"));
		assertEquals("ABC1234Z", FlightUtils.parseFlightCode("ABC1234Z/20NOV/YVR/Y"));
		assertEquals("3U8893", FlightUtils.parseFlightCode("3U8893/"));
	}

	@Test
	public void testParseAirline() throws ParseException {
		assertEquals("CP", FlightUtils.parseAirline("CP161/20NOV/YVR/Y"));
		assertEquals("CP", FlightUtils.parseAirline("CP161Z/20NOV/YVR/Y"));
		assertEquals("ABC", FlightUtils.parseAirline("ABC1234Z/20NOV/YVR/Y"));
		assertEquals("3U", FlightUtils.parseAirline("3U8893/"));
	}
	
	@Test
	public void testParseAirlineThrowsException() {
		int n = 0;
		
		try {
			assertEquals("CP", FlightUtils.parseAirline(""));
		} catch (ParseException e) {
			n++;
		}
		
		try {
			assertEquals("CP", FlightUtils.parseAirline("CP161"));
		} catch (ParseException e) {
			n++;
		}
		
		try {
			assertEquals("CP", FlightUtils.parseAirline("ABCD161/20NOV/YVR/Y"));
		} catch (ParseException e) {
			n++;
		}
		
		assertEquals(3, n);
	}

	@Test
	public void testEncodeFlightCode() {
		assertArrayEquals(new byte[] {(byte) 0x31, (byte) 0x9F, (byte) 0xC5, (byte) 0x87, (byte) 0xFF} , 
				FlightUtils.encodeFlightCode("CP161"));
		
		assertArrayEquals(new byte[] {(byte) 0x5E, (byte) 0x0F, (byte) 0xC0, (byte) 0xA3, (byte) 0xDC} , 
				FlightUtils.encodeFlightCode("NW028S"));
		
		assertArrayEquals(new byte[] {(byte) 0x6D, (byte) 0xE7, (byte) 0x05, (byte) 0xC9, (byte) 0xFF} , 
				FlightUtils.encodeFlightCode("RUS1727"));
	}
	
	@Test
	public void testDecodeFlightCode() {
		assertEquals("CP161", 
				FlightUtils.decodeFlightCode(new byte[] {(byte) 0x31, (byte) 0x9F, (byte) 0xC5, (byte) 0x87, (byte) 0xFF}));
		
		assertEquals("NW028S", 
				FlightUtils.decodeFlightCode(new byte[] {(byte) 0x5E, (byte) 0x0F, (byte) 0xC0, (byte) 0xA3, (byte) 0xDC}));
		
		assertEquals("RUS1727", 
				FlightUtils.decodeFlightCode(new byte[] {(byte) 0x6D, (byte) 0xE7, (byte) 0x05, (byte) 0xC9, (byte) 0xFF}));
	}
	
}
