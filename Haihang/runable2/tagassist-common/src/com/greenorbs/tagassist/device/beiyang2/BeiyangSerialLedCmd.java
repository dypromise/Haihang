package com.greenorbs.tagassist.device.beiyang2;

public class BeiyangSerialLedCmd extends BeiyangSerialCmd {

	private byte _mode;

	private String _text;

	public String getText() {
		return _text;
	}

	public void setText(String text) {
		_text = text;
	}

	public BeiyangSerialLedCmd() {
		super(BeiyangSerials.COMMAND_CONTROL_LED);
	}

	public BeiyangSerialLedCmd(String text, byte mode) {
		this();
		this._text = text;
		this._mode = mode;
	}

	@Override
	public byte[] getContent() {

		if (_mode >= 9 || _text == null) {
			return new byte[0];
		}

		byte[] tmp = _text.getBytes();
		byte[] data = new byte[tmp.length + 2];
		data[0] = 0x01;
		data[1] = (byte) _mode;
		System.arraycopy(tmp, 0, data, 2, tmp.length);

		return data;

	}



}
