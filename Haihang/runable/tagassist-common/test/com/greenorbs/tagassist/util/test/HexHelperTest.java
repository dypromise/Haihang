package com.greenorbs.tagassist.util.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.greenorbs.tagassist.util.HexHelper;

public class HexHelperTest {

	@Test
	public void testHexToBin() {
		assertEquals("0000000100100011010001010110011110001001101010111100110111101111", 
				HexHelper.hexToBin("0123456789ABCDEF"));
	}

	@Test
	public void testBinToHex() {
		assertEquals("0123456789ABCDEF", 
				HexHelper.binToHex("0000000100100011010001010110011110001001101010111100110111101111"));
	}
	
	@Test
	public void testHexToByte() {
		assertArrayEquals(new byte[] {0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF} , 
				HexHelper.hexToByte("0123456789ABCDEF"));
	}
	
	@Test
	public void testByteToHex() {
		assertEquals("0123456789ABCDEF", 
				HexHelper.byteToHex(new byte[] {0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF}));
	}
	
}
