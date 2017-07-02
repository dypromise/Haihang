package com.greenorbs.tagassist.simulator.device;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IPeripheral;
import com.greenorbs.tagassist.device.InfraredRayListener;

public class SimPeripheralClient extends AbstractSimClient implements IPeripheral{

	@Override
	public void addDetectionListener(InfraredRayListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDetectionListener(InfraredRayListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getInfraredPoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startup() throws HardwareException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Result shutdown() throws HardwareException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result reset() throws HardwareException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void speak(int mode) throws HardwareException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mute() throws HardwareException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnOn(int lights) throws HardwareException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnOff(int lights) throws HardwareException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIP(String ip) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocalPort(int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTargetPort(int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTargetPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDetectionInterval(long interval) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getDetectionInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

}
