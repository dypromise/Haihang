/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-9
 */

package com.greenorbs.tagassist.device.beiyang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.ICautionLight;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.IPeripheral;
import com.greenorbs.tagassist.device.ISpeaker;
import com.greenorbs.tagassist.device.InfraredRayDetectEvent;
import com.greenorbs.tagassist.device.InfraredRayListener;
import com.greenorbs.tagassist.util.BeiyangCRC;
import com.greenorbs.tagassist.util.HexHelper;
import com.greenorbs.tagassist.util.SocketUtils;
import com.sun.jna.Native;

public class BeiyangPeripheral implements IPeripheral, Runnable {

	private static Logger _log = Logger.getLogger(BeiyangPeripheral.class);

	private int _status;

	private String _ip;

	private int _targetPort = 4001;

	private int _localPort = 6001;

	private boolean _isOpen;

	private int _handle;

	private byte[] _buffer = new byte[FRAME_LENGTH * 10];

	private byte _commandCounter = 1;

	private long _interval = 50;

	protected static BeiyangPeripheralDriver _driver;

	/* The infrared ray related properties */
	protected Set<InfraredRayListener> _infraredRayListeners = new HashSet<InfraredRayListener>();

	protected static final String[] INFRARED_RAY_POINTS = { "1", "2" };

	public static final int FRAME_LENGTH = 17;

	static {
		_driver = (BeiyangPeripheralDriver) Native.loadLibrary("UDPSDK",
				BeiyangPeripheralDriver.class);
	}

	public BeiyangPeripheral() {

	}

	public BeiyangPeripheral(int localPort, String ip, int targetPort) {
		this.setIP(ip);
		this.setLocalPort(localPort);
		this.setTargetPort(targetPort);
	}

	public String getIP() {
		return _ip;
	}

	public void setIP(String ip) {
		_ip = ip;
	}

	public int getLocalPort() {
		return this._localPort;
	}

	public void setLocalPort(int port) {
		this._localPort = port;
	}

	public int getTargetPort() {
		return this._targetPort;
	}

	public void setTargetPort(int port) {
		this._targetPort = port;
	}

	/********** IHardware interface *************/
	@Override
	public void startup() throws HardwareException {

		if (this._isOpen == false) {

			this._handle = _driver.BYRD_initPort(this.getLocalPort(),
					this.getTargetPort(), this._ip);

			if (this._handle == -1) {
				throw new HardwareException(
						"It fails to init the Beiyang controller.");
			}

			this._isOpen = true;

			this.turnOff(ICautionLight.RED | ICautionLight.YELLOW
					| ICautionLight.GREEN);
			this.mute();
		}

		this._status = IHardware.STATUS_ON;

		new Thread(this,"Beiyang Peripheral").start();

	}

	@Override
	public Result shutdown() throws HardwareException {

		if (this._isOpen) {

			this.turnOff(ICautionLight.RED | ICautionLight.YELLOW
					| ICautionLight.GREEN);

			this.mute();

			int rtn = _driver.BYRD_closePort(this._handle);
			if (rtn == -1) {
				throw new HardwareException(
						"It fails to close the Beiyang controller");
			}

			this._isOpen = false;

			this._status = IHardware.STATUS_OFF;

			return Result.SUCCESS;
		}

		return Result.ERROR;
	}

	@Override
	public Result reset() throws HardwareException {

		try {

			this.shutdown();

			this.startup();

			return Result.SUCCESS;

		} catch (HardwareException e) {

			_log.error("It fails reset the Beiyang Controller");

			return Result.ERROR;
		}
	}

	@Override
	public int getStatus() {

		return this._status;
	}

	@Override
	public String[] getInfraredPoints() {
		return INFRARED_RAY_POINTS;
	}

	/**************** Helper method ****************/

	private byte nextCommandCounter() {

		if (this._commandCounter > 254) {
			this._commandCounter = 0;
		}
		return this._commandCounter;
	}

	private byte[] assemblyFrame(Byte[] data) {

		byte[] frame = new byte[10 + data.length];

		int dataLen = frame.length - 5;

		// frame head
		frame[0] = 0x1B;
		frame[1] = (byte) (dataLen >> 8);
		frame[2] = (byte) (dataLen);
		// device address
		frame[3] = (byte) 0xFF;
		frame[4] = (byte) 0xFF;
		// function code
		frame[5] = (byte) 0x01;
		frame[6] = (byte) 0x50;

		frame[7] = this.nextCommandCounter();

		for (int i = 0; i < data.length; i++) {
			frame[8 + i] = data[i];
		}

		byte[] crc = BeiyangCRC.getCRC16(frame, 1, frame.length - 3);

		BeiyangCRC.printHex(crc);

		frame[frame.length - 2] = crc[0];
		frame[frame.length - 1] = crc[1];

		return frame;
	}

	/************* InfraredRay Interface *********************/

	@Override
	public void addDetectionListener(InfraredRayListener listener) {
		Validate.notNull(listener);
		this._infraredRayListeners.add(listener);
	}

	@Override
	public void removeDetectionListener(InfraredRayListener listener) {
		Validate.notNull(listener);
		this._infraredRayListeners.remove(listener);
	}

	protected void fireInfraredRayDetection(String rayPoint, long time) {

		InfraredRayDetectEvent event = new InfraredRayDetectEvent(this,
				rayPoint, time);

		_log.info("InfraredRayDetection:" + event);

		for (InfraredRayListener l : this._infraredRayListeners) {
			l.objectDetected(event);
		}
	}

	/**
	 * This thread is used to handling the infrared rays.
	 */
	public void run() {
		try {

			System.out.println(this.getStatus());
			while (this.getStatus() == IHardware.STATUS_ON) {
				try {
					Thread.sleep(_interval);
				} catch (InterruptedException e) {
				}

				Arrays.fill(this._buffer, (byte) 0);
				int length = _driver.BYRD_recInfo(this._handle, this._buffer,
						this._buffer.length);
				System.out
						.println("length:"+length+", time:"+System.currentTimeMillis()+", status:"+this.getStatus());
				int base = 0;

				while (base < length) {

					int rayPoint = _buffer[base + 8];

					System.out.println("base:" + base);

					if (this._status == IHardware.STATUS_ON) {
						System.out.println("going to fire InfraredRay");
						this.fireInfraredRayDetection(String.valueOf(rayPoint),
								System.currentTimeMillis());
					}

					base += FRAME_LENGTH;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/* =======ICautionLight Interface============== */
	@Override
	public void turnOn(int lights) throws HardwareException {

		if (this._isOpen == false) {
			throw new HardwareException("The Beiyang controller is not open.");
		}

		List<Byte> data = new ArrayList<Byte>();

		if ((lights & ICautionLight.RED) > 0) {
			data.add((byte) 0x01);
			data.add((byte) 0x01);
		}
		if ((lights & ICautionLight.YELLOW) > 0) {
			data.add((byte) 0x02);
			data.add((byte) 0x01);
		}
		if ((lights & ICautionLight.GREEN) > 0) {
			data.add((byte) 0x03);
			data.add((byte) 0x01);
		}

		byte len = (byte) (data.size() / 2);

		data.add(0, len);

		byte[] frame = this.assemblyFrame(data.toArray(new Byte[0]));

		_log.debug("It is going to send the frame[ "
				+ HexHelper.byteToHex(frame) + " ]to controller");
		int rtn = _driver.BYRD_sendInfo(this._handle, frame, frame.length);
		if (rtn == 0) {
			throw new HardwareException("It fails to turn on the lights.");
		}

	}

	@Override
	public void turnOff(int lights) throws HardwareException {
		if (this._isOpen == false) {
			throw new HardwareException("The Beiyang controller is not open.");
		}

		List<Byte> data = new ArrayList<Byte>();
		if ((lights & ICautionLight.RED) > 0) {
			data.add((byte) 0x01);
			data.add((byte) 0x00);
		}

		if ((lights & ICautionLight.YELLOW) > 0) {
			data.add((byte) 0x02);
			data.add((byte) 0x00);
		}

		if ((lights & ICautionLight.GREEN) > 0) {
			data.add((byte) 0x03);
			data.add((byte) 0x00);
		}

		byte len = (byte) (data.size() / 2);
		data.add(0, len);

		byte[] frame = this.assemblyFrame(data.toArray(new Byte[0]));

		int rtn = _driver.BYRD_sendInfo(this._handle, frame, frame.length);

		if (rtn == 0) {
			throw new HardwareException("It fails to turn off the lights");
		}

	}

	@Override
	public void speak(int mode) throws HardwareException {
		if (this._isOpen == false) {
			throw new HardwareException("The Beiyang controller is not open");
		}

		List<Byte> data = new ArrayList<Byte>();

		if (mode == ISpeaker.LONG_MODE) {
			data.add((byte) 0x04);
			data.add((byte) 0x01);
		} else if (mode == ISpeaker.SHORT_MODE) {
			data.add((byte) 0x05);
			data.add((byte) 0x01);
		}

		data.add(0, (byte) (data.size() / 2));

		byte[] frame = this.assemblyFrame(data.toArray(new Byte[0]));

		int rtn = _driver.BYRD_sendInfo(this._handle, frame, frame.length);

		if (rtn == 0) {
			throw new HardwareException("It fails to turn off the lights");
		}

	}

	@Override
	public void mute() throws HardwareException {
		if (this._isOpen == false) {
			throw new HardwareException("The Beiyang controller is not open");
		}

		List<Byte> data = new ArrayList<Byte>();

		data.add((byte) 0x04);
		data.add((byte) 0x00);
		data.add((byte) 0x05);
		data.add((byte) 0x00);

		data.add(0, (byte) (data.size() / 2));

		byte[] frame = this.assemblyFrame(data.toArray(new Byte[0]));

		int rtn = _driver.BYRD_sendInfo(this._handle, frame, frame.length);

		if (rtn == 0) {
			throw new HardwareException("It fails to turn off the lights");
		}
	}

	@Override
	public void setDetectionInterval(long interval) {
		this._interval = interval;
	}

	@Override
	public long getDetectionInterval() {
		return this._interval;
	}

	public static void main(String[] args) {
		try {
			DOMConfigurator.configure("log4j.xml");

			BeiyangPeripheral _controller = new BeiyangPeripheral(6001,
					"192.168.50.198", 4001);

			_controller.addDetectionListener(new InfraredRayListener() {

				@Override
				public void objectDetected(InfraredRayDetectEvent event) {

					System.out.println(event);

					System.out.println("**********");
				}

			});

			_controller.startup();
			//
			_controller.turnOn(ICautionLight.GREEN | ICautionLight.YELLOW
					| ICautionLight.GREEN);

			// _controller.mute();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
