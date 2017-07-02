package com.greenorbs.tagassist.device;

import com.greenorbs.tagassist.Result;

public interface IResumableHardware extends IHardware {

	/**
	 * After receiving this message, the hardware is paused, whose status keeps static.
	 * @throws HardwareException
	 */
	public Result pause() throws HardwareException;
	
	/**
	 * This message enables the paused hardware resuming its execution.
	 * @throws HardwareException
	 */
	public Result resume() throws HardwareException;

}
