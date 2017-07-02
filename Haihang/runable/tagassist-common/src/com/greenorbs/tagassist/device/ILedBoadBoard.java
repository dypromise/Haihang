package com.greenorbs.tagassist.device;

public interface ILedBoadBoard extends IHardware {

	public static final byte MODE_SHOW = 0;
	public static final byte MODE_MOVE = 1;
	public static final byte MODE_FLASH = 2;
	public static final byte MODE_INV_SHOW = 3;
	public static final byte MODE_INV_MOVE = 4;
	public static final byte MODE_INV_FLASH = 5;
	public static final byte MODE_LIE_SHOW = 6;
	public static final byte MODE_LIE_MOVE = 7;
	public static final byte MODE_LIE_FLASH = 8;	
	
	public void publish(String text, int mode);

}
