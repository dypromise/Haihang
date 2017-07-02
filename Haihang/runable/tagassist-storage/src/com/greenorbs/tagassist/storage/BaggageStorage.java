/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Author: Shangguan Longfei, 2012-2-22
 */

package com.greenorbs.tagassist.storage;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.BaggageTrackedProperty;
import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.FlightTerminate;
import com.greenorbs.tagassist.messagebus.message.CheckTunnelMessages.BaggageCheckedIn;
import com.greenorbs.tagassist.messagebus.message.CheckTunnelMessages.BaggageCheckedOut;
import com.greenorbs.tagassist.messagebus.message.CheckTunnelMessages.BaggageDamaged;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.BaggageStatusChanged;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.BaggageArrival;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.BaggageRemoval;
import com.greenorbs.tagassist.messagebus.message.MobileReaderMessages.BoardingReport;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.BaggageTracked;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.BaggageTrackingLost;
import com.greenorbs.tagassist.messagebus.message.WristbandProxyMessages.BaggageIdentified;
import com.greenorbs.tagassist.messagebus.util.querier.BaggageQuerier;
import com.greenorbs.tagassist.messagebus.util.querier.FlightQuerier;
import com.greenorbs.tagassist.util.EPC96Utils;

/**
 * 
 * @author Longfei Shangguan
 * 
 */
public abstract class BaggageStorage extends AbstractStorage<BaggageInfo> {

	public abstract boolean removeByFlightId(String flightId);

	public abstract BaggageInfo findByNumber(String number);

	public abstract BaggageInfo findByEPC(String EPC);

	public abstract BaggageInfo findByEPC(String flightId, String EPC);

	public abstract List<BaggageInfo> findByFlightId(String flightId);

	public abstract List<BaggageInfo> findByStatus(String flightId, int status);

	private FlightQuerier _flightQuerier;
	private BaggageQuerier _baggageQuerier;
	public FlightQuerier getFlightQuerier() {
		return _flightQuerier;
	}
	public void setFlightQuerier(FlightQuerier flightQuerier) {
		_flightQuerier = flightQuerier;
	}
	public BaggageQuerier getBaggageQuerier() {
		return _baggageQuerier;
	}
	public void setBaggageQuerier(BaggageQuerier baggageQuerier) {
		_baggageQuerier = baggageQuerier;
	}
	

	public BaggageStorage(Identifiable identity) {
		super(identity);
		this._flightQuerier = new FlightQuerier(this._publisher);
		this._baggageQuerier = new BaggageQuerier(this._publisher);
	}

	public BaggageStorage() {
		this(null);
	}

	@Override
	protected void initSubscriber() {
		this.subscribe(BaggageArrival.class);
		this.subscribe(BaggageRemoval.class);
		this.subscribe(BaggageStatusChanged.class);
		this.subscribe(BaggageCheckedIn.class);
		this.subscribe(BaggageCheckedOut.class);
		this.subscribe(BaggageDamaged.class);
		this.subscribe(BaggageTracked.class);
		this.subscribe(BaggageIdentified.class);
		this.subscribe(BoardingReport.class);
		this.subscribe(FlightTerminate.class);
		this.subscribe(BaggageTrackingLost.class);
	}

	@Override
	public void onMessage(AbstractMessage message) {
		try {
			// new baggage arrives.
			if (message instanceof BaggageArrival) {

				BaggageArrival m = (BaggageArrival) message;
				BaggageInfo baggageInfo = m.getBaggageInfo();

				if (baggageInfo != null && StringUtils.isNotEmpty(baggageInfo.getNumber())) {
					if (null == this.findByNumber(baggageInfo.getNumber())) {
						baggageInfo.setStatus(BaggageInfo.STATUS_ARRIVED);
						baggageInfo.setLastTracedDevice(m.getSource());
						baggageInfo.setLastTracedTime((new Date()).getTime());

						this.add(message, baggageInfo);
			

						_log.debug("It is successful to add a new baggage.");
					}
				}

			}

			// remove the baggage from existing baggage list.
			else if (message instanceof BaggageRemoval) {

				BaggageRemoval m = (BaggageRemoval) message;
				BaggageInfo baggageInfo = m.getBaggageInfo();

				if (baggageInfo != null && StringUtils.isNotEmpty(baggageInfo.getNumber())) {
					BaggageInfo storedBaggage = this.findByNumber(baggageInfo.getNumber());
					if (storedBaggage != null) {
						storedBaggage.setStatus(BaggageInfo.STATUS_REMOVED);
						storedBaggage.setLastTracedDevice(m.getSource());
						storedBaggage.setLastTracedTime((new Date()).getTime());

						// BaggageInfo old = this.itemUpdated(storedBaggage);
						// this.fireItemUpdated(message, old, storedBaggage);

						_log.debug("It is successful to remove a new baggage.");
					}
				}

			}
			
			else if (message instanceof BaggageStatusChanged) {
				
				BaggageStatusChanged m = (BaggageStatusChanged) message;
				String baggageNumber = m.getBaggageNumber();
				
				if (StringUtils.isNotEmpty(baggageNumber)) {
					BaggageInfo storedBaggage = this.findByNumber(baggageNumber);
					if (storedBaggage != null) {
						storedBaggage.setStatus(m.getStatus());
						storedBaggage.setLastTracedDevice(m.getDevice());
						storedBaggage.setLastTracedTime(m.getTime());
					}
				}
				
			}
			
			// new baggage has been identified by the checkTunnel.
			else if (message instanceof BaggageCheckedIn) {

				BaggageCheckedIn messageCaptured = (BaggageCheckedIn) message;
				String epc = messageCaptured.getEPC();

//				if (StringUtils.isNotEmpty(epc) && EPC96Utils.isValid(epc)) {
				if (StringUtils.isNotEmpty(epc)) {
					
					BaggageInfo storedBaggage = this.findByEPC(epc);

					if (null == storedBaggage) {
						storedBaggage = this.fetchByEPC(message, epc);
					}

					if (storedBaggage != null) {
						if (messageCaptured.getWasCheckedInRight()) {
							storedBaggage.setStatus(BaggageInfo.STATUS_READ_BY_CHECKTUNNEL_RIGHT);
						} else {
							storedBaggage.setStatus(BaggageInfo.STATUS_READ_BY_CHECKTUNNEL_WRONG);
						}
						storedBaggage.setLastTracedDevice(messageCaptured.getSource());
						storedBaggage.setLastTracedTime((new Date()).getTime());

						// BaggageInfo old = this.itemUpdated(storedBaggage);
						// this.fireItemUpdated(message, old, storedBaggage);

						_log.debug("It is successful to check in a new baggage.[number:"+storedBaggage+",flightId:"+storedBaggage.getFlightId()+"]");
					}
				}

			}

			// baggage went back to the sorting pool again.
			else if (message instanceof BaggageCheckedOut) {

				BaggageCheckedOut messageCaptured = (BaggageCheckedOut) message;
				String epc = messageCaptured.getEPC();
//				if (StringUtils.isNotEmpty(epc) && EPC96Utils.isValid(epc)) {
				if (StringUtils.isNotEmpty(epc)) {
					BaggageInfo storedBaggage = this.findByEPC(epc);

					if (null == storedBaggage) {
						storedBaggage = this.fetchByEPC(message, epc);
					}

					if (storedBaggage != null) {
						storedBaggage.setStatus(BaggageInfo.STATUS_ARRIVED);
						storedBaggage.setLastTracedDevice(messageCaptured.getSource());
						storedBaggage.setLastTracedTime((new Date()).getTime());

						// BaggageInfo old = this.itemUpdated(storedBaggage);
						// this.fireItemUpdated(message, old, storedBaggage);

						_log.debug("It is successful to check out a new baggage.["+storedBaggage+",flightId:"+storedBaggage.getFlightId()+"]");
					}
				}

			}

			// baggage was damaged.
			else if (message instanceof BaggageDamaged) {

				BaggageDamaged messageCaptured = (BaggageDamaged) message;
				String baggageNumber = messageCaptured.getBaggageNumber();
				int damageStatus = messageCaptured.getDamageCode();

				if (StringUtils.isNotEmpty(baggageNumber)) {
					BaggageInfo storedBaggage = this.findByNumber(baggageNumber);

					if (null == storedBaggage) {
						storedBaggage = this.fetchByNumber(message, baggageNumber);
					}

					if (storedBaggage != null) {
						storedBaggage.setDamageCode(damageStatus);

						// BaggageInfo old = this.itemUpdated(storedBaggage);
						// this.fireItemUpdated(message, old, storedBaggage);

						_log.debug("It is successful to change the baggage's damaged.["+storedBaggage+",flightId:"+storedBaggage.getFlightId()+"]");
					}
				}
			}

			// baggage was identified by trackTunnel.
			else if (message instanceof BaggageTracked) {

				BaggageTracked messageCaptured = (BaggageTracked) message;
				String epc = messageCaptured.getEPC();

				// dingyang add
//				if (StringUtils.isNotEmpty(epc) && EPC96Utils.isValid(epc)) {
				if (StringUtils.isNotEmpty(epc)) {
					BaggageInfo storedBaggage = this.findByEPC(epc);

					if (null == storedBaggage) {
						storedBaggage = this.fetchByEPC(message, epc);
					}

					if (storedBaggage != null) {
						storedBaggage.setStatus(BaggageInfo.STATUS_IN_POOL);
						storedBaggage.setLastTracedDevice(messageCaptured.getSource());
						storedBaggage.setLastTracedTime((new Date()).getTime());

						BaggageTrackedProperty btp = new BaggageTrackedProperty();
						btp.setDistance(messageCaptured.getDistance());
						btp.setTimeStamp(System.currentTimeMillis());
						
						storedBaggage.setProperty(BaggageInfo.PROPERTY_TRACKED_DISTANCE, btp);

						storedBaggage.setProperty(BaggageInfo.PROPERTY_IS_MISSING,
								Boolean.FALSE);

						// BaggageInfo old = this.itemUpdated(storedBaggage);
						// this.fireItemUpdated(message, old, storedBaggage);

						_log.debug("It is successful to track a baaggage.["+storedBaggage+",flightId:"+storedBaggage.getFlightId()+"]");
					}
				}

			}

			// baggage was identified by wristband.
			else if (message instanceof BaggageIdentified) {

				BaggageIdentified messageCaptured = (BaggageIdentified) message;
				String epc = messageCaptured.getEPC();
//				if (StringUtils.isNotEmpty(epc) && EPC96Utils.isValid(epc)) {
				if (StringUtils.isNotEmpty(epc)) {
					BaggageInfo storedBaggage = this.findByEPC(epc);

					if (null == storedBaggage) {
						storedBaggage = this.fetchByEPC(message, epc);
					}

					if (storedBaggage != null) {
						storedBaggage.setStatus(BaggageInfo.STATUS_READ_BY_WRISTBAND);
						storedBaggage.setLastTracedDevice(messageCaptured.getSource());
						storedBaggage.setLastTracedTime((new Date()).getTime());

						// BaggageInfo old = this.itemUpdated(storedBaggage);
						// this.fireItemUpdated(message, old, storedBaggage);

						_log.debug("It is successful to identify a baggage.["+storedBaggage+",flightId:"+storedBaggage.getFlightId()+"]");
					}
				}

			}

			// terminate the specified flight.
			else if (message instanceof FlightTerminate) {

				FlightTerminate messageCaptured = (FlightTerminate) message;
				String flightId = messageCaptured.getFlightId();

				List<BaggageInfo> baggages = this.findByFlightId(flightId);
				for (BaggageInfo bag : baggages) {
					this.remove(message, bag);
				}
				_log.debug("It is successful to handle fligth terminate.");

			}

			// capture the boarding report from the mobile reader.
			else if (message instanceof BoardingReport) {

				BoardingReport report = (BoardingReport) message;

				for (String baggageNumber : report.getBoardedBaggageNumberList()) {
					BaggageInfo baggageInfo = this.findByNumber(baggageNumber);
					if (baggageInfo != null) {
						baggageInfo.setStatus(BaggageInfo.STATUS_READ_BY_MOBILEREADER_RIGHT);

						// BaggageInfo old = this.itemUpdated(baggageInfo);
						// this.fireItemUpdated(message, old, baggageInfo);
					}
				}

				List<BaggageInfo> expectedBaggageInfoList = this.findByFlightId(report.getFlightId());
				if (expectedBaggageInfoList != null) {
					for (BaggageInfo baggageInfo : expectedBaggageInfoList) {
						if (baggageInfo.getStatus() != BaggageInfo.STATUS_READ_BY_MOBILEREADER_RIGHT &&
								baggageInfo.getStatus() != BaggageInfo.STATUS_REMOVED) {
							baggageInfo.setStatus(BaggageInfo.STATUS_MISSING);
							// BaggageInfo old = this.itemUpdated(baggageInfo);
							// this.fireItemUpdated(message, old, baggageInfo);
						}
					}
				}

				for (String epc : report.getUnexpectedBaggageEPCList()) {
					try {
						BaggageInfo baggageInfo = this.findByEPC(epc);
						if (baggageInfo != null) {
							baggageInfo.setStatus(BaggageInfo.STATUS_READ_BY_MOBILEREADER_WRONG);
							// BaggageInfo old = this.itemUpdated(baggageInfo);
							// this.fireItemUpdated(message, old, baggageInfo);
						}
					} catch (Exception e) {
						e.printStackTrace();
						this._log.error(e.getMessage());
					}
				}

			}

			else if (message instanceof BaggageTrackingLost) {
				BaggageTrackingLost btl = (BaggageTrackingLost) message;
				BaggageInfo bi = this.findByEPC(btl.getEpc());

				if (bi != null) {
					bi.setProperty(BaggageInfo.PROPERTY_IS_MISSING, Boolean.TRUE);

					// BaggageInfo old = this.itemUpdated(bi);
					// this.fireItemUpdated(message, old, bi);

					_log.debug("It is successful to handle track lost of a new baggage.["+bi+",flightId:"+bi.getFlightId()+"]");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("It fails to handle the baggage message." + e);
		}

	}

	private synchronized BaggageInfo fetchByNumber(Object trigger, String baggageNumber) {
		BaggageInfo baggageInfo = _baggageQuerier.getBaggageInfo(baggageNumber);

		if (baggageInfo != null) {
			this.add(trigger, baggageInfo);
		}

		return this.findByNumber(baggageNumber);
	}

	private synchronized BaggageInfo fetchByEPC(Object trigger, String epc) {
		BaggageInfo baggageInfo = _baggageQuerier.getBaggageInfoByEPC(epc);

		if (baggageInfo != null) {
			this.add(trigger, baggageInfo);
		}

		return this.findByEPC(epc);
	}

	@Override
	protected synchronized Collection<BaggageInfo> crawl() {
		List<BaggageInfo> baggageCollection = new ArrayList<BaggageInfo>();

		FlightInfo[] flightInfoList = _flightQuerier.getFlightList();
		if (flightInfoList != null) {
			for (FlightInfo flightInfo : flightInfoList) {
				String flightId = flightInfo.getFlightId();
				BaggageInfo[] baggageInfoList = _baggageQuerier.getBaggageList(flightId);
				if (baggageInfoList != null) {
					baggageCollection.addAll(Arrays.asList(baggageInfoList));
				}
			}
		}

		return baggageCollection;
	}
	
	
}
