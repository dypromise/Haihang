/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 13, 2012
 */

package com.greenorbs.tagassist.flightproxy;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.epc.EPC96Encoder;
import com.greenorbs.tagassist.epc.EPC_DYEncoder;
import com.greenorbs.tagassist.flightproxy.dcsadapter.DCSMessage;
import com.greenorbs.tagassist.flightproxy.dcsadapter.IDCSMessageConsumer;
import com.greenorbs.tagassist.messagebus.io.Publisher;
import com.greenorbs.tagassist.messagebus.io.Subscriber;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.FlightTerminate;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.BaggageArrival;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.BaggageRemoval;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightScheduled;
import com.greenorbs.tagassist.messagebus.message.MessageBase;
import com.greenorbs.tagassist.messagebus.util.BusMessageHandler;
import com.greenorbs.tagassist.messagebus.util.cache.BaggageCache;
import com.greenorbs.tagassist.messagebus.util.cache.FlightCache;
import com.greenorbs.tagassist.util.EPC96Utils;
import com.greenorbs.tagassist.util.FlightUtils;
import com.greenorbs.tagassist.util.HexHelper;

public class FlightProxyWorker extends BusMessageHandler implements IDCSMessageConsumer {

	private FlightCache _flightCache;
	private BaggageCache _baggageCache;

	public FlightProxyWorker(Publisher publisher, Subscriber subscriber) {
		super(publisher, subscriber);

		_flightCache = new FlightCache(publisher);
		_baggageCache = new BaggageCache(publisher);
	}

	@Override
	public void initialize() {
		super.initialize();

		_flightCache.initialize();
		_baggageCache.initialize(_flightCache.flights());
		_logger.info("Cache initialized.");
	}

	@Override
	protected void initSubscriber() {
		this.subscribe(FlightScheduled.class);
		this.subscribe(FlightTerminate.class);
	}

	@Override
	protected MessageBase handleMessage(MessageBase message) {
		if (message instanceof FlightScheduled) {

			FlightScheduled m = (FlightScheduled) message;
			FlightInfo flightInfo = m.getFlightInfo();

			this.handleFlightScheduled(flightInfo);

		} else if (message instanceof FlightTerminate) {

			FlightTerminate m = (FlightTerminate) message;
			String flightId = m.getFlightId();

			this.handleFlightTerminate(flightId);

		}

		return null;
	}

	@Override
	public void onDCSMessage(DCSMessage message) {
		switch (message.getType()) {
		case DCSMessage.TYPE_SOURCE: {
			this.handleBaggageArrival(message);
			break;
		}
		case DCSMessage.TYPE_REMOVAL: {
			this.handleBaggageRemoval(message.getBaggageNumberList());
			break;
		}
		default:
			break;
		}
	}

	private void handleBaggageArrival(DCSMessage message) {
		String flightId = message.getFlightId();

		if (!_flightCache.contains(flightId)) {
			_flightCache.update(flightId);

			/*
			 * The following may not be the right thing to do.
			 */
			if (!_flightCache.contains(flightId)) {
				FlightInfo flightInfo = new FlightInfo();
				flightInfo.setFlightId(flightId);
				flightInfo.setStatus(FlightInfo.STATUS_SCHEDULED);
				// And no more properties we can set here.
				_flightCache.put(flightInfo);

				_logger.info(String.format("Flight scheduled: flightId=%s", flightInfo.getFlightId()));

				this.publish(new FlightScheduled(flightInfo));

				// Sleep for a little while.
				try {
					_logger.info("Sleep for a little while.");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					_logger.error(e);
				}
			}
		}

		String flightCode = null;
		try {
			flightCode = FlightUtils.parseFlightCode(flightId);
		} catch (ParseException e) {
			_logger.error(e);
			return;
		}

		if (flightCode != null) {
			for (String baggageNumber : message.getBaggageNumberList()) {
				if (!_baggageCache.contains(baggageNumber)) {
					BaggageInfo baggageInfo = new BaggageInfo();
					baggageInfo.setNumber(baggageNumber);
					baggageInfo.setFlightId(message.getFlightId());
//					baggageInfo.setEPC(EPC96Utils.hexEncode(baggageNumber, flightCode));
					// dingyang modified
					baggageInfo.setEPC("00"+EPC_DYEncoder.str2hex(baggageNumber)+"00");
					baggageInfo.setDestination(message.getDestination());
					baggageInfo.setBClass(message.getBClass());
					baggageInfo.setPassenger(message.getPassenger());
					if (message.getBaggageCount() > 0) {
						baggageInfo.setWeight(message.getBaggageWeight() / message.getBaggageCount());
					}
					baggageInfo.setStatus(BaggageInfo.STATUS_ARRIVED);
					_baggageCache.put(baggageInfo);

					_logger.info(String.format("Baggage arrived: epc=%s, flightId=%s", baggageInfo.getEPC(),
							baggageInfo.getFlightId()));

					this.publish(new BaggageArrival(baggageInfo));
				} else {
					BaggageInfo baggageInfo = _baggageCache.get(baggageNumber);
					_logger.info(String.format("Duplicated baggage arrival ignored: epc=%s, flightId=%s",
							baggageInfo.getEPC(), baggageInfo.getFlightId()));
				}
			}
		}
	}

	private void handleBaggageRemoval(ArrayList<String> baggageNumberList) {
		for (String baggageNumber : baggageNumberList) {
			// Update baggage cache before moving on
			_baggageCache.update(baggageNumber);

			if (_baggageCache.contains(baggageNumber)) {
				BaggageInfo baggageInfo = _baggageCache.get(baggageNumber);
				if (baggageInfo.getStatus() != BaggageInfo.STATUS_REMOVED) {
					int oldStatus = baggageInfo.getStatus();
					baggageInfo.setStatus(BaggageInfo.STATUS_REMOVED);

					_logger.info(String.format("Baggage removed: epc=%s, flightId=%s", baggageInfo.getEPC(),
							baggageInfo.getFlightId()));

					this.publish(new BaggageRemoval(baggageInfo, oldStatus));
				}
			}
		}
	}

	private void handleFlightScheduled(FlightInfo flightInfo) {
		if (null == flightInfo || StringUtils.isEmpty(flightInfo.getFlightId())) {
			return;
		}

		try {
			String scheduledFlightId = flightInfo.getFlightId();
			if (_flightCache.contains(scheduledFlightId)) {
				HashSet<String> toTerminate = new HashSet<String>();

				String flightCode = FlightUtils.parseFlightCode(scheduledFlightId);
				for (FlightInfo storedFlight : _flightCache.flights()) {
					String storedFlightId = storedFlight.getFlightId();
					if (storedFlightId.startsWith(flightCode) && !storedFlightId.equals(scheduledFlightId)) {
						toTerminate.add(storedFlightId);
					}
				}

				for (String flightId : toTerminate) {
					_logger.info(String.format("Terminating flight: flightId=%s", flightId));
					this.publish(new FlightTerminate(flightId));
				}
			}
		} catch (Exception e) {
			_logger.error(e);
		}
	}

	private void handleFlightTerminate(String flightId) {
		_flightCache.remove(flightId);
		_baggageCache.clear(flightId);
	}

}
