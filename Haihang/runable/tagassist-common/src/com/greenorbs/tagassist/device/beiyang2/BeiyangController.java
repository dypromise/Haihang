package com.greenorbs.tagassist.device.beiyang2;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.log4j.Logger;

import com.greenorbs.tagassist.device.HardwareException;

public class BeiyangController extends Thread implements
		SerialPortEventListener {

	protected Logger _logger = Logger.getLogger(BeiyangController.class);

	private boolean _opened;

	private String _serialPort;

	private int _baudRate;

	private long _rayQueryPeriod = 50;

	private SerialPort _serial;

	private OutputStream _out;

	private InputStream _in;

	private byte[] _innerBuffer = new byte[512];

	// To buffer the command from upper layer
	private PriorityQueue<BeiyangSerialCmd> _commandQueue;

	private Queue<Byte> _dataQueue;

	public BeiyangController() {
		
		_dataQueue = new LinkedBlockingQueue<Byte>();

		_commandQueue = new PriorityQueue<BeiyangSerialCmd>(100,
				new Comparator<BeiyangSerialCmd>() {
					@Override
					public int compare(BeiyangSerialCmd cmd1,
							BeiyangSerialCmd cmd2) {
						return cmd1.getPriority() - cmd2.getPriority();
					}

				});

		this.start();
	}

	public Queue<Byte> getDataQueue() {
		return _dataQueue;
	}

	public String getSerialPort() {
		return _serialPort;
	}

	public void setSerialPort(String serialPort) {
		_serialPort = serialPort;
	}

	public int getBaudRate() {
		return _baudRate;
	}

	public void setBaudRate(int baudRate) {
		_baudRate = baudRate;
	}

	public void open() throws HardwareException {

		if (this._opened) {
			return;
		}

		try {
			CommPortIdentifier identifier = CommPortIdentifier
					.getPortIdentifier(this._serialPort);
			_serial = (SerialPort) identifier.open("TAGASSIST", 2000);
			_serial.setSerialPortParams(this.getBaudRate(),
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			_out = _serial.getOutputStream();
			_in = _serial.getInputStream();

			_serial.notifyOnDataAvailable(true);

			_serial.addEventListener(this);

			this._opened = true;

		} catch (Exception e) {
			e.printStackTrace();
			throw new HardwareException("fail to connect the controller.", e);
		}
	}

	public void close() {
		_opened = false;
		_serial.close();
	}

	/**
	 * The query interval for infrared ray query. The default query interval is
	 * 50ms
	 * 
	 * @return
	 */

	// private byte[] encode(byte ins, byte[] message) {
	// byte[] data = new byte[message.length + 6];
	// data[0] = (byte) (message.length + 6);
	// data[1] = (byte) 0xFF;
	// data[2] = ins;
	// data[3] = (byte) message.length;
	// System.arraycopy(message, 0, data, 4, message.length);
	// byte x = 0;
	// for (int i = 0; i != message.length + 4; ++i)
	// x ^= data[i];
	// data[message.length + 4] = (byte) ~x;
	// data[message.length + 5] = 0x3;
	// return data;
	// }

	// public synchronized void write(byte command, byte[] data)
	// throws IOException {
	//
	// _out.write(encode(command, data));
	//
	// }

	/**
	 * The controller is a thread. When there is any command that not the
	 * infrared ray query, the controller query the infrared ray status with
	 * 50ms. If any command arrival, the infrared ray stops and pop a command to
	 * send.
	 */
	public void run() {

		while (true) {

			if (!this._opened) {
				continue;
			}

			try {
				Thread.sleep(this.getQueryRayPeriod());
			} catch (InterruptedException e) {
			}

			try {
				// check the command queue
				BeiyangSerialCmd cmd = this._commandQueue.poll();
				if (cmd != null) {
					this._out.write(cmd.toBytes());
				} else {
					this._out.write(BeiyangInfraredRayQueryCmd.QUERY_COMMAND
							.toBytes());
				}
			} catch (Exception e) {
				e.printStackTrace();
				_logger.error("it fails to write the serial command.", e);
			}

		}

	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		_logger.debug("read serial event.");
		try {

			synchronized (this._innerBuffer) {
				int len = _in.read(_innerBuffer);

				for (int i = 0; i < len; i++) {
					this._dataQueue.add(new Byte(_innerBuffer[i]));

					// if the size of data in the queue is greater than 1M, the
					// old one is deleted in order to avoid the heap overflow.
					if (this._dataQueue.size() > 1048576) {
						this._dataQueue.poll();
					}
				}

			}

		} catch (Exception e) {
			_logger.error("it fails to handle the serial event.", e);
			e.printStackTrace();
		}

	}

	public void sendCommand(BeiyangSerialCmd cmd) {
		// ignore the infrared ray query command.
		if (!cmd.equals(BeiyangInfraredRayQueryCmd.QUERY_COMMAND)) {
			this._commandQueue.add(cmd);
		}
	}

	public long getQueryRayPeriod() {
		return _rayQueryPeriod;
	}

	public void setQueryRayPeriod(long rayQueryPeriod) {
		_rayQueryPeriod = rayQueryPeriod;
	}

}
