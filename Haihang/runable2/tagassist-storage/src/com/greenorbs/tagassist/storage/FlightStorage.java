/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-2-22
 */

package com.greenorbs.tagassist.storage;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.FlightInfo;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.message.AdministratorMessages.FlightTerminate;
import com.greenorbs.tagassist.messagebus.message.CheckTunnelMessages.BaggageCheckedIn;
import com.greenorbs.tagassist.messagebus.message.CheckTunnelMessages.BaggageCheckedOut;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.BaggageArrival;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.BaggageRemoval;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightCanceled;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightCheckingIn;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightClosed;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightDepartured;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightRescheduled;
import com.greenorbs.tagassist.messagebus.message.FlightProxyMessages.FlightScheduled;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.BaggageTracked;
import com.greenorbs.tagassist.messagebus.util.querier.FlightQuerier;
import com.greenorbs.tagassist.storage.query.IQuery;
import com.greenorbs.tagassist.storage.query.StorageFactory;

public abstract class FlightStorage extends AbstractStorage<FlightInfo> {

	private FlightQuerier _flightQuerier;

	public abstract FlightInfo findByFlightId(String flightId);

	public FlightStorage(Identifiable identity) {
		super(identity);
		this._flightQuerier = new FlightQuerier(this._publisher);
	}

	public FlightStorage() {
		this(null);
	}

	@Override
	protected void initSubscriber() {
		this.subscribe(FlightScheduled.class);
		this.subscribe(FlightRescheduled.class);
		this.subscribe(FlightCanceled.class);
		this.subscribe(FlightCheckingIn.class);
		this.subscribe(FlightClosed.class);
		this.subscribe(FlightDepartured.class);

		this.subscribe(FlightTerminate.class);

		this.subscribe(BaggageArrival.class);
		this.subscribe(BaggageRemoval.class);

		this.subscribe(BaggageCheckedIn.class);
		this.subscribe(BaggageCheckedOut.class);

		this.subscribe(BaggageTracked.class);
	}

	@Override
	public void onMessage(AbstractMessage message) {
		if (message instanceof FlightScheduled) {

			FlightScheduled m = (FlightScheduled) message;
			FlightInfo flightInfo = m.getFlightInfo();

			this.handleFlightScheduled(message, flightInfo);

		} else if (message instanceof FlightRescheduled) {

			FlightRescheduled m = (FlightRescheduled) message;
			FlightInfo flightInfo = m.getFlightInfo();

			this.handleFlightStatusChanged(message, flightInfo,	
					FlightInfo.STATUS_RESCHEDULED);

		} else if (message instanceof FlightCanceled) {

			FlightCanceled m = (FlightCanceled) message;
			FlightInfo flightInfo = m.getFlightInfo();

			this.handleFlightStatusChanged(message, flightInfo,	
					FlightInfo.STATUS_CANCELED);

		} else if (message instanceof FlightCheckingIn) {

			FlightCheckingIn m = (FlightCheckingIn) message;
			FlightInfo flightInfo = m.getFlightInfo();

			this.handleFlightStatusChanged(message, flightInfo,	
					FlightInfo.STATUS_CHECKING_IN);

		} else if (message instanceof FlightClosed) {

			FlightClosed m = (FlightClosed) message;
			FlightInfo flightInfo = m.getFlightInfo();

			this.handleFlightStatusChanged(message, flightInfo,	
					FlightInfo.STATUS_CLOSED);

		} else if (message instanceof FlightDepartured) {
			
			FlightDepartured m = (FlightDepartured) message;
			FlightInfo flightInfo = m.getFlightInfo();
			
			this.handleFlightStatusChanged(message, flightInfo, 
					FlightInfo.STATUS_DEPARTURED);
			
		} else if (message instanceof FlightTerminate) {

			FlightTerminate m = (FlightTerminate) message;
			String flightId = m.getFlightId();

			this.handleFlightStatusChanged(message, flightId, 
					FlightInfo.STATUS_DEPARTURED);

			FlightInfo flight = this.findByFlightId(flightId);
			if (flight != null) {
				flight.setProperty(FlightInfo.PROPERTY_SORTED_BAGGAGE_SIZE,	
						null);
				flight.setProperty(FlightInfo.PROPERTY_UNSORTED_BAGGAGE_SIZE, 
						null);
				flight.setProperty(FlightInfo.PROPERTY_MISSING_BAGGAGE_SIZE, 
						null);
				flight.setProperty(FlightInfo.PROPERTY_TOTAL_BAGGAGE_SIZE, 
						null);
			}

			this.remove(this, flight);

		}
	}

	private void countBaggageSize(final FlightInfo flightInfo) {
		Validate.notNull(flightInfo);

		flightInfo.setProperty(
				FlightInfo.PROPERTY_SORTED_BAGGAGE_SIZE,
				StorageFactory.getBaggageStorage().count(
						new CountBaggage(flightInfo.getFlightId(),
								CountBaggage.SORTED)));

		flightInfo.setProperty(
				FlightInfo.PROPERTY_UNSORTED_BAGGAGE_SIZE,
				StorageFactory.getBaggageStorage().count(
						new CountBaggage(flightInfo.getFlightId(),
								CountBaggage.UNSORTED)));

		flightInfo.setProperty(
				FlightInfo.PROPERTY_MISSING_BAGGAGE_SIZE,
				StorageFactory.getBaggageStorage().count(
						new CountBaggage(flightInfo.getFlightId(),
								CountBaggage.MISSING)));

		flightInfo.setProperty(
				FlightInfo.PROPERTY_TOTAL_BAGGAGE_SIZE,
				StorageFactory.getBaggageStorage().count(
						new CountBaggage(flightInfo.getFlightId(),
								CountBaggage.TOTAL)));

	}

	private void handleFlightScheduled(AbstractMessage message,
			final FlightInfo flightInfo) {
		if (null == flightInfo || StringUtils.isEmpty(flightInfo.getFlightId())) {
			return;
		}

		if (!this.contains(flightInfo.getFlightId())) {
			this.countBaggageSize(flightInfo);
			this.add(message, flightInfo);
		} else {
			// TODO MARK_1
		}
	}

	private void handleFlightStatusChanged(AbstractMessage message,
			FlightInfo flightInfo, int newStatus) {
		if (null == flightInfo || StringUtils.isEmpty(flightInfo.getFlightId())) {
			return;
		}

		FlightInfo storedFlight = this.findByFlightId(flightInfo.getFlightId());
		if (storedFlight != null) {
			storedFlight.setStatus(newStatus);
			if (newStatus == FlightInfo.STATUS_RESCHEDULED) {
				storedFlight.setEDT(flightInfo.getEDT());
			} else if (newStatus == FlightInfo.STATUS_DEPARTURED) {
				storedFlight.setADT(flightInfo.getADT());
			}
			// FlightInfo f = this.itemUpdated(storedFlight);
			// this.fireItemUpdated(message, f, storedFlight);
		} else {
			flightInfo.setStatus(newStatus);
			this.add(message, flightInfo);
		}
	}

	private void handleFlightStatusChanged(AbstractMessage message,
			String flightId, int newStatus) {
		if (StringUtils.isEmpty(flightId)) {
			return;
		}

		FlightInfo storedFlight = this.findByFlightId(flightId);
		if (storedFlight != null) {
			storedFlight.setStatus(newStatus);
			if (newStatus == FlightInfo.STATUS_SORT_TERMINATED) {
				// this.missingBaggages(flightId).addAll(this.unsortedBaggages(flightId));
				// this.unsortedBaggages(flightId).clear();
				// this.refreshBaggageStatistics(message, flightId);
			}
			// FlightInfo old = this.itemUpdated(storedFlight);
			// this.fireItemUpdated(message, old, storedFlight);
		}
	}

	@Override
	protected Collection<FlightInfo> crawl() {

		FlightInfo[] flightInfoList = _flightQuerier.getFlightList();

		if (flightInfoList != null) {
			for (FlightInfo flightInfo : flightInfoList) {
				this.countBaggageSize(flightInfo);
			}
		}

		if (flightInfoList != null) {
			return Arrays.asList(flightInfoList);
		} else {
			return null;
		}
	}
}

class CountBaggage implements IQuery<BaggageInfo> {

	static final int SORTED = 0;
	static final int UNSORTED = 1;
	static final int MISSING = 2;
	static final int TOTAL = 3;

	private String _flightId;
	private int _mode;

	public CountBaggage(String flight, int mode) {
		this._flightId = flight;
		this._mode = mode;

	}

	@Override
	public boolean accept(BaggageInfo item) {
		switch (this._mode) {
		case SORTED:
			if (item.getFlightId().equals(_flightId)) {
				return item.isSorted();
			}
			return false;
		case UNSORTED:
			if (item.getFlightId().equals(_flightId)) {
				return item.isUnsorted();
			}
			return false;
		case MISSING:
			if (item.getFlightId().equals(_flightId)) {
				return item.isMissing();
			}
			return false;
		case TOTAL:
			if (item.getFlightId().equals(_flightId)) {
				return true;
			}
			return false;
		default:
			return false;
		}

	}

	@Override
	public String toString() {
		switch (this._mode) {
		case SORTED:
			return "FlightStorage.CountSorrtedBaggageSize(FlightId:"
					+ this._flightId + ")";
		case UNSORTED:
			return "FlightStorage.CountUnsortedBaggageSize(FlightId:"
					+ this._flightId + ")";
		case MISSING:
			return "FlightStorage.CountMissingBaggageSize(FlightId:"
					+ this._flightId + ")";
		case TOTAL:
			return "FlightStorage.CountTotalBaggageSize(FlightId:"
					+ this._flightId + ")";
		default:
			return "Unknown";
		}

	}

}
