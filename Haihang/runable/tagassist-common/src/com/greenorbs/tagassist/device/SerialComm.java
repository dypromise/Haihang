package com.greenorbs.tagassist.device;

import gnu.io.*;

import java.io.*;
import java.util.Arrays;

public class SerialComm {
	private OutputStream out;
	private byte[] tempBytes;
	private int pos;
	private SerialPort port;
	
	public SerialComm(String portName,
			int speed,
			ISerialReader reader,
			boolean has_end) throws HardwareException {
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			this.port = (SerialPort) portIdentifier.open("TAGASSIST", 2000);
			this.port.setSerialPortParams(speed, 
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			this.out = this.port.getOutputStream();
			this.port.addEventListener(new SerialPortListener
					(this.port.getInputStream(), reader, has_end));
			this.port.notifyOnDataAvailable(true);
			tempBytes = new byte[128];
		} catch (Exception e) {
			throw new HardwareException(e);
		}
	}
	
	public void write(byte[] data) {
		try {
			this.out.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		this.port.close();
	}
	
	private class SerialPortListener implements SerialPortEventListener {
		private InputStream in;
		private ISerialReader reader;
		private boolean has_end;
		
		public SerialPortListener(InputStream in, ISerialReader reader, boolean has_end) {
			this.in = in;
			this.reader = reader;
			this.has_end = has_end;
		}
		
		@Override
		public void serialEvent(SerialPortEvent arg0) {
			byte[] buffer = new byte[128];
			try {
				int len = this.in.read(buffer);
				for (int i = 0; i != len; ++i) {
					byte tmp = buffer[i];
					tempBytes[pos++] = tmp;
					if (has_end
						&& pos > 2
						&& tempBytes[pos - 2] == 13
						&& tempBytes[pos - 1] == 10) {
						reader.process(Arrays.copyOf(tempBytes, pos - 2));
						pos = 0;
					} else if (pos == tempBytes[0]) {
						reader.process(Arrays.copyOf(tempBytes, pos));
						pos = 0;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}

