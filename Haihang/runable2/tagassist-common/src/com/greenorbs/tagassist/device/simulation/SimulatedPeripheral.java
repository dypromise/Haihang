/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-13
 */

package com.greenorbs.tagassist.device.simulation;

import java.util.ArrayList;
import java.util.List;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IPeripheral;
import com.greenorbs.tagassist.device.InfraredRayDetectEvent;
import com.greenorbs.tagassist.device.InfraredRayListener;

public class SimulatedPeripheral extends Thread implements IPeripheral {

	private List<InfraredRayListener> _listeners = new ArrayList<InfraredRayListener>();

	@Override
	public void run() {
		
//		try {
//			Thread.sleep(9000);
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
//		
//		while(true){
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			
//			//first time detection
//			InfraredRayDetectEvent event = new InfraredRayDetectEvent(this, "1", System.currentTimeMillis());
//
//			for (InfraredRayListener l : this._listeners) {
//				l.objectDetected(event);
//			}
//			
//			try {
//				Thread.sleep(50);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			
//			//second time detection
//			InfraredRayDetectEvent event2 = new InfraredRayDetectEvent(this, "2", System.currentTimeMillis());
//
//			for (InfraredRayListener l : this._listeners) {
//				l.objectDetected(event2);
//			}
//		}
	}

	@Override
	public void addDetectionListener(InfraredRayListener listener) {
		this._listeners.add(listener);
	}

	@Override
	public void removeDetectionListener(InfraredRayListener listener) {
		this._listeners.remove(listener);
	}

	@Override
	public String[] getInfraredPoints() {
		return null;
	}

	@Override
	public void startup() throws HardwareException {
		this.start();
	}

	@Override
	public Result shutdown() throws HardwareException {
		return null;
	}

	@Override
	public Result reset() throws HardwareException {
		return null;
	}

	@Override
	public int getStatus() {
		return 0;
	}

	@Override
	public void speak(int mode) throws HardwareException {

	}

	@Override
	public void mute() throws HardwareException {

		System.out.println("Mute");

	}

	@Override
	public void turnOn(int lights) throws HardwareException {
		System.out.println("Turn on the lights");

	}

	@Override
	public void turnOff(int lights) throws HardwareException {
		System.out.println("turn off the lights");

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
