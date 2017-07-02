package com.greenorbs.tagassist.simulator.device;

import java.net.ServerSocket;

import com.greenorbs.tagassist.device.HardwareException;

public class SimReaderServer {

	private ServerSocket _server;

	private int _port;

	public SimReaderServer() {

	}

	public int getPort() {
		return _port;
	}

	public void setPort(int port) {
		_port = port;
	}

	public void open() throws HardwareException {

		try {
			if (this._server == null) {
				this._server = new ServerSocket(_port);
			}

		} catch (Exception e) {
			throw new HardwareException(e);
		}

	}

	public void close() throws HardwareException {

		try {
			if (this._server != null) {
				this._server.close();
			}
		} catch (Exception e) {
			throw new HardwareException(e);
		}

	}

}
