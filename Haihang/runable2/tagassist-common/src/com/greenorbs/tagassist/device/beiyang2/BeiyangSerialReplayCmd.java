package com.greenorbs.tagassist.device.beiyang2;

public class BeiyangSerialReplayCmd extends BeiyangSerialCmd {

	public static final byte ON = 0x01;

	public static final byte OFF = 0x00;

	public byte _red;

	public byte _green;

	public byte _blue;

	public byte getRed() {
		return _red;
	}

	public void setRed(byte red) {
		_red = red;
	}

	public byte getGreen() {
		return _green;
	}

	public void setGreen(byte green) {
		_green = green;
	}

	public byte getBlue() {
		return _blue;
	}

	public void setBlue(byte blue) {
		_blue = blue;
	}

	public BeiyangSerialReplayCmd() {
		super(BeiyangSerials.COMMAND_CONTROL_REPLAY);
	}

	public BeiyangSerialReplayCmd(byte red, byte green, byte blue) {
		this();
		this._red = red;
		this._green = green;
		this._blue = blue;
	}

	public BeiyangSerialReplayCmd(int red, int green, int blue) {
		this((byte) red, (byte) green, (byte) blue);
	}

	@Override
	public byte[] getContent() {
		byte[] data = new byte[5];
		data[3] = data[4] = (byte) 0xFF;

		data[0] = _red;
		data[1] = _green;
		data[2] = _blue;

		return data;
	}

}
