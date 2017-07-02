package com.greenorbs.tagassist.device;

public interface IStatusLight extends IHardware{

	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int YELLOW = 4;

	void turnOn(int light) throws HardwareException;

	void blink(int light) throws HardwareException;
	
	void turnOff(int light) throws HardwareException;
}
