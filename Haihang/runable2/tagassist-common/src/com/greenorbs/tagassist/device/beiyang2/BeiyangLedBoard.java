package com.greenorbs.tagassist.device.beiyang2;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.ILedBoadBoard;

public class BeiyangLedBoard implements ILedBoadBoard {

	protected BeiyangController _controller;

	public BeiyangController getController() {
		return _controller;
	}

	public void setController(BeiyangController controller) {
		_controller = controller;
	}

	@Override
	public void publish(String text, int mode) {
		// if (mode >= 9)
		// return;
		// byte ins = (byte) 0x82;
		// byte[] tmp = text.getBytes();
		// byte[] data = new byte[tmp.length + 2];
		// data[0] = 0x01;
		// data[1] = (byte) mode;
		// System.arraycopy(tmp, 0, data, 2, tmp.length);

		this._controller
				.sendCommand(new BeiyangSerialLedCmd(text, (byte) mode));
		// this._controller.write(ins, data);

	}

	@Override
	public void startup() throws HardwareException {
		if (this._controller != null) {
			this._controller.open();
		}
	}

	@Override
	public Result shutdown() throws HardwareException {
		this._controller.close();
		return Result.SUCCESS;
	}

	@Override
	public Result reset() throws HardwareException {
		this.shutdown();
		this.startup();
		return Result.SUCCESS;
	}

	@Override
	public int getStatus() {
		return IHardware.STATUS_ON;
	}

}
