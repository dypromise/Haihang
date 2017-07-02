package com.greenorbs.tagassist.device.beiyang2;

import java.util.Arrays;

import org.apache.commons.lang.Validate;

public class BeiyangSerialResponse {

	private byte[] _data;

	public BeiyangSerialResponse(byte[] data) {
		Validate.notNull(data);
		this._data = data;
	}

	public boolean isValid() {
		if (this._data == null || this._data.length < 1) {
			return false;
		}

		byte crc = 0;
		for (int i = 0; i < this.getFrameLength() - 2; i++) {
			crc ^= this._data[i];
		}
		crc = (byte) ~crc;
		if (crc != this._data[this.getFrameLength() - 2]) {
			return false;
		}

		if (this.getEnd() != BeiyangSerials.RESPONSE_END) {
			return false;
		}

		if (this.getStatus() != BeiyangSerials.ERROR_NONE) {
			return false;
		}

		return true;
	}

	public byte getFrameLength() {
		return _data[0];
	}

	public byte getAddress() {
		return _data[1];
	}

	public byte getStatus() {
		return _data[2];
	}

	public byte getContentLength() {
		return _data[3];
	}

	public byte[] getContent() {
		return Arrays.copyOfRange(_data, 4, 4 + this.getContentLength());
	}

	public byte getCrc() {
		return _data[_data.length - 2];
	}

	public byte getEnd() {
		return _data[_data.length - 1];
	}

	@Override
	public String toString() {
		return Arrays.toString(this._data);
	}

}
