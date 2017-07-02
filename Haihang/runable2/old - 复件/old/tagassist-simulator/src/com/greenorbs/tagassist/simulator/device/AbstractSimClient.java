package com.greenorbs.tagassist.simulator.device;

import java.net.Socket;

public class AbstractSimClient {

	private Socket _socket;
	
	private String _ip;
	
	private int _port;

	public Socket getSocket() {
		return _socket;
	}

	public void setSocket(Socket socket) {
		_socket = socket;
	}

	public String getIP() {
		return _ip;
	}

	public void setIP(String ip) {
		_ip = ip;
	}

	public int getPort() {
		return _port;
	}

	public void setPort(int port) {
		_port = port;
	}
	
}
