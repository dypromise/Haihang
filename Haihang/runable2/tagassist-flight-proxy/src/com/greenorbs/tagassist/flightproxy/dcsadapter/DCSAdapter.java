/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 13, 2012
 */

package com.greenorbs.tagassist.flightproxy.dcsadapter;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.log4j.Logger;

/**
 * DCS = Departure Control System
 */
public class DCSAdapter extends Thread {
	
	protected static Logger _logger = Logger.getLogger(DCSAdapter.class);

	private static DCSAdapter _instance;
	
	public volatile boolean exit = false;
	private DCSReader _dcsReader;
	private HashSet<IDCSMessageConsumer> _messageConsumers;
	
	private DCSAdapter() {
		_dcsReader = new DCSReader();
		_messageConsumers = new HashSet<IDCSMessageConsumer>();
	}
	
	public static DCSAdapter instance() {
		// Double-checked locking
		if (null == _instance) {
			synchronized (DCSAdapter.class) {
				if (null == _instance) {
					_instance = new DCSAdapter();
				}
			}
		}
		return _instance;
	}

	public void registerMessageConsumer(IDCSMessageConsumer consumer) {
		_messageConsumers.add(consumer);
	}
	
	@Override
	public void run() {
		try {
			while (!exit) {
				ArrayList<DCSBlock> blocks = _dcsReader.read();
				if (blocks != null) {
					for (DCSBlock block : blocks) {
						this.consume(block);
						sleep(200);
					}
				} else {
					// TODO Read from configuration
					sleep(1000);
				}
			}
		} catch (InterruptedException e) {
			_logger.info("DCSAdapter.run() interrupted.");
		}
	}
	
	private void consume(DCSBlock block) {
		if (_messageConsumers.isEmpty()) {
			return;
		}

		ArrayList<DCSMessage> messageList = DCSBlockParser.parseBlock(block);
		if (messageList != null && !messageList.isEmpty()) {
			for (DCSMessage message : messageList) {
				if (FlightsFilter.allow(message.getFlightCode())) {
					for (IDCSMessageConsumer consumer : _messageConsumers) {
						consumer.onDCSMessage(message);
					}
					_logger.info("DCSMessage consumed: " + message.getBaggageNumberList());
				} else {
					_logger.info("DCSMessage ignored: " + message.getBaggageNumberList());
				}
			}
		}
	}

}
