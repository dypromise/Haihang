package com.greenorbs.tagassist.device.beiyang2;

public class BeiyangSerials {

	public static final byte COMMAND_QUERY_INFRARED_RAY = (byte) 0x80;

	public static final byte COMMAND_CONTROL_REPLAY = (byte) 0x81;
	

	public static final byte COMMAND_CONTROL_LED = (byte) 0x82;

	public static final byte COMMAND_CONTROL_STATUS_LIGHT = (byte) 0x83;

	public static final byte ERROR_NONE = 0;

	public static final byte ERROR_LENGTH = 1;

	public static final byte ERROR_CRC = 17;

	public static final byte ERROR_PARAMETER = 84;

	public static final byte ERROR_UNSUPORTED_COMMAND = 85;

	public static final byte RESPONSE_END = 0x03;
}
