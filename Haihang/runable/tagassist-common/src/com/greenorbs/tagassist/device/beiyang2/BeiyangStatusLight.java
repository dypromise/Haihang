package com.greenorbs.tagassist.device.beiyang2;

import java.io.IOException;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.IStatusLight;

public class BeiyangStatusLight implements IStatusLight {

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

	@Override
	public void turnOn(int light) throws HardwareException {

		BeiyangSerialStatusLightCmd cmd = new BeiyangSerialStatusLightCmd();
		if ((light & YELLOW) > 0) {
			cmd.setYellow(BeiyangSerialStatusLightCmd.TURN_ON);
		}
		if ((light & RED) > 0) {
			cmd.setRed(BeiyangSerialStatusLightCmd.TURN_ON);
		}

		this._controller.sendCommand(cmd);
	}

	@Override
	public void blink(int light) throws HardwareException {

		BeiyangSerialStatusLightCmd cmd = new BeiyangSerialStatusLightCmd();
		if ((light & YELLOW) > 0) {
			cmd.setYellow(BeiyangSerialStatusLightCmd.BLINK);
		}
		if ((light & RED) > 0) {
			cmd.setRed(BeiyangSerialStatusLightCmd.BLINK);
		}

		this._controller.sendCommand(cmd);

	}

	@Override
	public void turnOff(int light) throws HardwareException {

		BeiyangSerialStatusLightCmd cmd = new BeiyangSerialStatusLightCmd();
		if ((light & YELLOW) > 0) {
			cmd.setYellow(BeiyangSerialStatusLightCmd.TURN_OFF);
		}
		if ((light & RED) > 0) {
			cmd.setRed(BeiyangSerialStatusLightCmd.TURN_OFF);
		}

		this._controller.sendCommand(cmd);

	}

}
