package com.greenorbs.tagassist.device;

import com.greenorbs.tagassist.Result;
import com.impinj.octane.OctaneSdkException;

public interface IHardware {
	
	public static final int COMMAND_RESET		= 1;
	public static final int COMMAND_PAUSE		= 2;
	public static final int COMMAND_RESUME		= 3;
	public static final int COMMAND_SHUTDOWN	= 4;

	public static final int STATUS_UNKNOWN	= 0;
	public static final int STATUS_ON 		= 1;
	public static final int STATUS_OFF 		= 2;
	public static final int STATUS_ERROR 	= 3;
	public static final int STATUS_PAUSE    = 4;
	public static final int STATUS_IDLE 	= 5;
	
	/**
	 * Start up the hardware
	 * 
	 * @throws HardwareException, if the startup cannot be performed
	 * @throws OctaneSdkException 
	 */
	public void startup() throws HardwareException, OctaneSdkException;
	
	/**
	 * Shut down the hardware
	 * 
	 * @throws HardwareException, if the shutdown cannot be performed
	 */
	public Result shutdown() throws HardwareException;

	/**
	 * Reset the hardware
	 * @throws HardwareException
	 */
	public Result reset() throws HardwareException;
	
	/**
	 * Get the current status of the hardware
	 * @return the current status of the hardware
	 */
	public int getStatus();
	
}
