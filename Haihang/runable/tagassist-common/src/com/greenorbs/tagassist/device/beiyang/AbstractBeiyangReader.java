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

import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IReader;
import com.greenorbs.tagassist.device.IdentifyListener;
import com.greenorbs.tagassist.device.ObservationReport;
import com.greenorbs.tagassist.device.ReadPointNotFoundException;

public abstract class AbstractBeiyangReader extends Thread implements IReader {

	protected Logger _log = Logger.getLogger(this.getClass());

	protected List<IdentifyListener> _identifyListeners = new Vector<IdentifyListener>();

	protected int _status;

	protected String _ip;

	protected int _port;

	protected int _commandCounter = 1;

	protected int _handle = 0;

	protected static final String[] READ_POINTS = { "1", "2", "3", "4" };

	protected byte[] _buffer = new byte[1024];

	protected boolean _asyn;

	public AbstractBeiyangReader() {

	}

	public AbstractBeiyangReader(String ip, int port) {
		this._ip = ip;
		this._port = port;
	}

	protected int nextCommandCounter() {

		if (++_commandCounter > 254)
			_commandCounter = 1;
		return _commandCounter;
	}

	@Override
	public void startup() throws HardwareException {
		_log.info("It is going to start to the beiyang reader");
	}

	@Override
	public Result shutdown() throws HardwareException {
		_log.info("The syste is shutting down.");

		return Result.SUCCESS;
	}

	@Override
	public Result reset() throws HardwareException {

		_log.info("It is going to reset the beiyang reader.");

		this.shutdown();

		this.startup();

		_log.info("It is successful to rest the beiyang reader.");

		return Result.SUCCESS;
	}

	@Override
	public int getStatus() {
		return this._status;
	}

	@Override
	public void setIP(String ip) {
		this._ip = ip;
	}

	@Override
	public String getIP() {
		return this._ip;
	}

	@Override
	public void setPort(int port) {
		this._port = port;
	}

	@Override
	public int getPort() {
		return this._port;
	}

	@Override
	public int getReadPointPower(String readpoint) throws HardwareException,
			ReadPointNotFoundException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setReadPointPower(String readpoint, int value)
			throws HardwareException, ReadPointNotFoundException {

		throw new UnsupportedOperationException();
	}

	@Override
	public String[] getReadPoints() throws HardwareException {
		return READ_POINTS;
	}

	@Override
	public ObservationReport identify(String[] antennas)
			throws HardwareException, ReadPointNotFoundException {

		throw new UnsupportedOperationException();
	}

	@Override
	public void addIdentifyListener(IdentifyListener listener) {

		this._identifyListeners.add(listener);

	}

	@Override
	public void removeIdentifyListener(IdentifyListener listener) {

		this._identifyListeners.remove(listener);
		
	}

	protected void fireIdentifyEvent(ObservationReport report) {

		report.setTime(new Date());
		System.out.println("this._identifyListeners:"+this._identifyListeners);
		for (IdentifyListener l : this._identifyListeners) {
			try {
				l.identifyPerformed(report);
			} catch (Exception e) {
				e.printStackTrace();
				_log.error("It fails to notify the identifylistener.");
			}
		}
	}

}
