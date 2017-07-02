package com.greenorbs.tagassist.device.beiyang2;

public abstract class BeiyangSerialCmd {

	private byte _address = (byte) 0xFF;

	private byte _command;

	protected static byte END = 0x03;

	public BeiyangSerialCmd(byte cmdCode) {
		this._command = cmdCode;
	}

	public byte[] toBytes() {

		byte[] bytes = new byte[this.getFrameLength()];
		bytes[0] = this.getFrameLength();
		bytes[1] = this.getAddress();
		bytes[2] = this.getCommand();
		bytes[3] = this.getContentLength();
		System.arraycopy(this.getContent(), 0, bytes, 4,
				this.getContentLength());
		bytes[bytes.length - 2] = this.getCrc();
		bytes[bytes.length - 1] = END;

		return bytes;
	}

	public byte getFrameLength() {
		return (byte) (6 + this.getContent().length);
	}

	public byte getAddress() {
		return _address;
	}

	public void setAddress(byte address) {
		_address = address;
	}

	public byte getCommand() {
		return _command;
	}

	public void setCommand(byte command) {
		_command = command;
	}

	public byte getContentLength() {
		return (byte) this.getContent().length;
	}

	public abstract byte[] getContent();
	
	public int getPriority(){
		return 1;
	}

	public byte getCrc() {

		byte[] bytes = new byte[this.getFrameLength() - 2];
		bytes[0] = this.getFrameLength();
		bytes[1] = this.getAddress();
		bytes[2] = this.getCommand();
		bytes[3] = this.getContentLength();
		System.arraycopy(this.getContent(), 0, bytes, 4,
				this.getContentLength());

		byte crc = 0;

		for (int i = 0; i < this.getFrameLength() - 2; i++) {
			crc ^= bytes[i];
		}
		return (byte) ~crc;
	}
	// private byte[] encode(byte ins, byte[] message) {
	// byte[] data = new byte[message.length + 6];
	// data[0] = (byte) (message.length + 6);
	// data[1] = (byte) 0xFF;
	// data[2] = ins;
	// data[3] = (byte) message.length;
	// System.arraycopy(message, 0, data, 4, message.length);
	// byte x = 0;
	// for (int i = 0; i != message.length + 4; ++i)
	// x ^= data[i];
	// data[message.length + 4] = (byte) ~x;
	// data[message.length + 5] = 0x3;
	// return data;
	// }

}
