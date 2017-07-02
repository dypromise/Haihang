package com.greenorbs.tagassist.device.beiyang2;

public class BeiyangSerialStatusLightCmd extends BeiyangSerialCmd {

	public static final byte TURN_ON = 0x11;
	public static final byte TURN_OFF = 0x10;
	public static final byte BLINK = 0x12;

	private byte _yellow;

	public byte getYellow() {
		return _yellow;
	}

	public void setYellow(byte yellow) {
		_yellow = yellow;
	}

	public byte getRed() {
		return _red;
	}

	public void setRed(byte red) {
		_red = red;
	}

	private byte _red;

	public BeiyangSerialStatusLightCmd() {
		super(BeiyangSerials.COMMAND_CONTROL_STATUS_LIGHT);
	}

	public BeiyangSerialStatusLightCmd(byte yellow, byte red) {
		this();
		this._yellow = yellow;
		this._red = red;
	}

	@Override
	public byte[] getContent() {
		byte[] data = new byte[4];
		data[0] = _yellow;
		data[1] = _red;

		return data;

	}

}
