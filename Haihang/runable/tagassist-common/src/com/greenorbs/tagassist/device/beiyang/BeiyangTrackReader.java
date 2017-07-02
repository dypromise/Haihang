/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Authors: Lei Yang, 2012-3-9
 */

package com.greenorbs.tagassist.device.beiyang;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.greenorbs.tagassist.Result;
import com.greenorbs.tagassist.ObjectCache;
import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IHardware;
import com.greenorbs.tagassist.device.IReader;
import com.greenorbs.tagassist.device.IdentifyListener;
import com.greenorbs.tagassist.device.Observation;
import com.greenorbs.tagassist.device.ObservationReport;
import com.greenorbs.tagassist.util.BeiyangCRC;
import com.greenorbs.tagassist.util.HexHelper;

public class BeiyangTrackReader extends AbstractBeiyangReader {

	private static final byte FRAME_HEADER = 0x1B;
	
	private Socket _socket;
	private BufferedInputStream _inputStream;
	private ObjectCache<Observation> _observationCache;
	
	static Logger _log = Logger.getLogger(BeiyangTrackReader.class);

	public BeiyangTrackReader() {
		this(null, 0);
	}

	public BeiyangTrackReader(String ip, int port) {
		super(ip, port);
		
		_socket = null;
		_inputStream = null;
		_observationCache = new ObjectCache<Observation>(5);
	}

	@Override
	public void startup() throws HardwareException {
		super.startup();

		this.initInputStream();
		
		this.start();
	}

	@Override
	public Result shutdown() throws HardwareException {
		try {
			this._socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			_log.error("It fails to close the socket.");
		}

		this._status = IHardware.STATUS_OFF;

		return super.shutdown();
	}

	public void run() {
		this._status = IHardware.STATUS_ON;
		
		do {
			int nRead = 0;
			
			try {
				nRead = _inputStream.read(_buffer);
			} catch (IOException e) {
//
//				e.printStackTrace();

				_log.error("Socket exception, we will initialize stream again." + e);
				this.initInputStream();

				
//				//check socket
//				if(!_socket.isConnected() || _socket.isClosed() ){
//					_log.error("Socket disconneted !!!" + e);
//					this.initInputStream();
//				}
//				
//				else{
//					_log.error("It fails to read data from the socket." + e);
//				}
			}
			
			if (nRead == 0) {
				continue;
			} else if (nRead == -1) {
				_log.info("The end of the stream has been reached.");
				break;
			}
			
			ObservationReport report = new ObservationReport();

			int offset = 0;

			while (offset < nRead) {
				
				_log.info("New observation report:"+Arrays.toString(_buffer));
				
				if (!verifyFrame(_buffer, offset)) {
					break;
				}
				
				if (isEndingFrame(_buffer, offset)) {
					_log.debug("Ending frame encountered: " + HexHelper.toReadable(this._buffer, offset, 11));
					break;
				}
				
				_log.debug("Frame received: " + HexHelper.toReadable(this._buffer, offset, 29));
				
				// 1 byte for frame header
				offset += 1;
				
				// 2 bytes for payload length
				offset += 2;
				
				// 24 bytes for payload
				Observation observation = this.parsePayload(_buffer, offset);
				offset += 24;
				
				_log.info(observation);
				
				String epc = observation.getEPC();
				if (_observationCache.get(epc) == null) {
					_observationCache.put(observation.getEPC(), observation);
					report.add(observation);
				} else {
					_log.debug("Frame ignored: duplicated observation.");
				}

				// 2 bytes for CRC
				offset += 2;
			}

			if (!report.isEmpty()) {
				report.setSucessfull(true);
				
				_log.debug("Identify event fired.");
				this.fireIdentifyEvent(report);
			}
			
		} while (this._status == IHardware.STATUS_ON);
	}
	
	private void initInputStream() {
		try {			
			_socket = new Socket(this.getIP(), this.getPort());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			_log.error("The host could not be determined.");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			_log.error("An I/O error occurs when creating the socket.");
			return;
		}
		
		try {
			_inputStream = new BufferedInputStream(_socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			_log.error("An I/O error occurs when creating the input stream.");
			return;
		}
	}
	
	/**
	 * Returns whether a frame is valid.
	 * 
	 * @param buffer
	 * 				the buffer that holds the frame
	 * @param frameOffset
	 * 				the offset of the frame in the buffer
	 * @return whether the given frame is valid
	 */
	private boolean verifyFrame(byte[] buffer, int frameOffset) {
		try {
			int originFrameOffset = frameOffset;
			
			// 1 byte for frame header
			byte header = buffer[frameOffset];
			frameOffset += 1;
			
			if (header != FRAME_HEADER) {
				_log.debug(String.format("Invalid frame header encountered: 0x%02X", header));
				return false;
			}
	
			// 2 bytes for payload length
			int payloadLength = ((buffer[frameOffset] & 0xFF) << 8) | (buffer[frameOffset + 1] & 0xFF);
			frameOffset += 2;
			
			if (payloadLength != 24 && payloadLength != 6) {
				_log.debug("Unsupported payload length: " + payloadLength);
				return false;
			}
			
			// Skip the whole payload
			frameOffset += payloadLength;
			
			// 2 bytes for CRC
			byte[] crc = new byte[] {buffer[frameOffset], buffer[frameOffset + 1]};
			frameOffset += 2;
			
			// Real CRC
			byte[] realCRC = BeiyangCRC.getCRC16(buffer, originFrameOffset + 1, 2 + payloadLength);
			
			if (realCRC[0] == crc[0] && realCRC[1] == crc[1]) {
				return true;
			} else {
				_log.debug("CRC verification failed.");
				return false;
			}
		} catch (Exception e) {
			_log.debug("Frame may be incomplete.");
			return false;
		}
	}
	
	/**
	 * Returns whether a verified frame is an ending frame.
	 *  
	 * @param buffer
	 * 				the buffer that holds the frame
	 * @param frameOffset
	 * 				the offset of the frame in the buffer
	 * @return whether the given frame is an ending frame
	 */
	private boolean isEndingFrame(byte[] buffer, int frameOffset) {
		return (buffer[frameOffset + 1] == 0x00 && buffer[frameOffset + 2] == 0x06);
	}
	
	/**
	 * Returns the observation carried by a payload. 
	 * 
	 * @param buffer
	 * 				the buffer that holds the payload
	 * @param payloadOffset
	 * 				the offset of the payload in the buffer
	 * @return the observation carried by the payload
	 */
	private Observation parsePayload(byte[] buffer, int payloadOffset) {
		// 2 bytes for device address (skip)
		payloadOffset += 2;
		
		// 2 bytes for command code (skip)
		payloadOffset += 2;
		
		// 1 byte for command id (skip)
		payloadOffset += 1;
		
		// 1 byte for payload header (skip)
		payloadOffset += 1;
		
		// 12 bytes for EPC
		String epc = HexHelper.byteToHex(buffer, payloadOffset, 12);
		payloadOffset += 12;
		
		// 4 bytes for RSSI
		int rssi = ((buffer[payloadOffset] & 0xFF) << 24) |
				   ((buffer[payloadOffset + 1] & 0xFF) << 16) |
				   ((buffer[payloadOffset + 2] & 0xFF) << 8 ) |
				   ((buffer[payloadOffset + 3] & 0xFF));
		payloadOffset += 4;
		
		// 1 byte for reading point (antenna)
		String readPoint = "" + (buffer[payloadOffset] & 0xFF);
		payloadOffset += 1;
		
		// 1 byte for reading count
		int count = buffer[payloadOffset] & 0xFF;
		payloadOffset += 1;
		
		Observation observation = new Observation();
		observation.setEPC(epc);
		observation.setRssi(rssi);
		observation.setTimeStamp(System.currentTimeMillis());
		observation.setReadPoint(readPoint);
		observation.setCount(count);
		
		return observation;
	}
	
	public static void main(String[] args) throws HardwareException {

		DOMConfigurator.configure("log4j.xml");

		IReader reader = new BeiyangTrackReader("192.168.50.101", 81);

		try {
			reader.addIdentifyListener(new IdentifyListener() {

				@Override
				public void identifyPerformed(ObservationReport report) {
					for (Observation observ : report) {
						System.out.println(observ);
					}

				}

			});
			reader.startup();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
