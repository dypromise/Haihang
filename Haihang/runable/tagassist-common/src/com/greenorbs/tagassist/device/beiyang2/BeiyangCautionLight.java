package com.greenorbs.tagassist.device.beiyang2;

import java.io.IOException;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.ICautionLight;
import com.greenorbs.tagassist.device.IHardware;

public class BeiyangCautionLight implements ICautionLight {

	private BeiyangController _controller;

	public BeiyangController getController() {
		return _controller;
	}

	public void setController(BeiyangController controller) {
		_controller = controller;
	}

	@Override
	public void startup() throws HardwareException {
		if (this._controller != null) {
			this._controller.open();
		}
	}

	@Override
	public Result shutdown() throws HardwareException {
		if (this._controller != null) {
			this._controller.close();
		}
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

	@Override
	public void turnOn(int light) throws HardwareException {

		BeiyangSerialReplayCmd cmd;

		switch (light) {
		case BLACK:
			cmd = new BeiyangSerialReplayCmd(0, 0, 0);
			break;
		case RED:
			cmd = new BeiyangSerialReplayCmd(1, 0, 0);
			break;
		case GREEN:
			cmd = new BeiyangSerialReplayCmd(0, 1, 0);
			break;
		case BLUE:
			cmd = new BeiyangSerialReplayCmd(0, 0, 1);
			break;
		case YELLOW:
			cmd = new BeiyangSerialReplayCmd(1, 1, 0);
			break;
		case PURPLE:
			cmd = new BeiyangSerialReplayCmd(1, 0, 1);
			break;
		case CYAN:
			cmd = new BeiyangSerialReplayCmd(0, 1, 1);
			break;
		case WHITE:
			cmd = new BeiyangSerialReplayCmd(1, 1, 1);
			break;
		default:
			return;
		}
		this._controller.sendCommand(cmd);
	}

	@Override
	public void turnOff(int lights) throws HardwareException {
		this.turnOn(BLACK);
	}

}
