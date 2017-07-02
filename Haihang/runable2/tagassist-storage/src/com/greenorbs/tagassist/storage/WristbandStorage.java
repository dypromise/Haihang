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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.greenorbs.tagassist.DeviceInfo;
import com.greenorbs.tagassist.WristbandInfo;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.DeviceRegistered;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.DeviceUnregistered;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.HardwareInfoReport;
import com.greenorbs.tagassist.messagebus.message.WristbandProxyMessages;
import com.greenorbs.tagassist.messagebus.message.WristbandProxyMessages.WristbandInfoReport;
import com.greenorbs.tagassist.messagebus.util.querier.WristbandQuerier;

public abstract class WristbandStorage extends AbstractStorage<WristbandInfo> {

	private WristbandQuerier _wristbandQuerier;

	public abstract WristbandInfo findByWristbandId(String wristbandId);

	public WristbandStorage(Identifiable identity) {
		super(identity);
		this._wristbandQuerier = new WristbandQuerier(this._publisher);
	}

	public WristbandStorage() {
		this(null);
	}

	@Override
	protected void initSubscriber() {
		this.subscribe(DeviceRegistered.class);
		this.subscribe(DeviceUnregistered.class);

		this.subscribe(WristbandProxyMessages.BindingChanged.class);
		this.subscribe(WristbandInfoReport.class);
		this.subscribe(HardwareInfoReport.class);
	}

	@Override
	public void onMessage(AbstractMessage message) {
		if (message instanceof DeviceRegistered) {

			DeviceRegistered m = (DeviceRegistered) message;
			DeviceInfo deviceInfo = m.getDeviceInfo();
			if (deviceInfo != null
					&& deviceInfo.getComponent() == Identifiable.COMPONENT_WRISTBAND_PROXY) {
				String wristbandId = deviceInfo.getUUID();
				if (!this.contains(wristbandId)) {
					WristbandInfo wristbandInfo = _wristbandQuerier
							.getWristbandInfo(wristbandId);
					this.add(message, wristbandInfo);
					// this.fireItemAdded(message, wristbandInfo);
				} else {
					WristbandInfo wristbandInfo = this
							.findByWristbandId(wristbandId);
					wristbandInfo.setDeviceInfo(deviceInfo);
					// WristbandInfo old = this.itemUpdated(wristbandInfo);
					// this.fireItemUpdated(message, old, wristbandInfo);
				}
			}

		} else if (message instanceof DeviceUnregistered) {

			DeviceUnregistered m = (DeviceUnregistered) message;
			String deviceUUID = m.getDeviceUUID();
			if (this.contains(deviceUUID)) {
				WristbandInfo wristbandInfo = this
						.findByWristbandId(deviceUUID);
				this.remove(message, wristbandInfo);
				// this.fireItemRemoved(message, wristbandInfo);
			}

		} else if (message instanceof WristbandProxyMessages.BindingChanged) {

			_log.debug("receive and going to handle the BindingChangedMessage("
					+ message + ")");
			handleBindingChanged((WristbandProxyMessages.BindingChanged) message);

		} else if (message instanceof HardwareInfoReport) {

			HardwareInfoReport report = (HardwareInfoReport) message;

			if (report.getComponent() == Identifiable.COMPONENT_WRISTBAND_PROXY) {
				String wristbandId = report.getSource();
				WristbandInfo wristbandInfo = this
						.findByWristbandId(wristbandId);
				if (wristbandInfo != null) {
					wristbandInfo.getDeviceInfo().setName(report.getName());
					wristbandInfo.getDeviceInfo().setStatus(report.getStatus());

					_log.debug("receive hardware info report:["
							+ report.getName() + ", status:"
							+ report.getStatus());

					if (message instanceof WristbandInfoReport) {
						WristbandInfoReport r2 = (WristbandInfoReport) message;
						if (r2.getWristbandInfo() != null) {
							wristbandInfo.setSubStatus(r2.getWristbandInfo()
									.getSubStatus());
							wristbandInfo.setBattery(r2.getWristbandInfo()
									.getBattery());
							wristbandInfo.setRFIDStatus(r2.getWristbandInfo()
									.getRFIDStatus());
						}
					}

					// WristbandInfo old = this.itemUpdated(wristbandInfo);
					// this.fireItemUpdated(message, old, wristbandInfo);
				}
			}

		}
	}

	/*--------Message handlers---------*/
	public void handleBindingChanged(
			WristbandProxyMessages.BindingChanged message) {
		String wristbandId = message.getSource();
		String flightId = message.getFlightId();
		boolean bound = message.getBound();

		if (bound) {
			if (this.findByWristbandId(wristbandId) != null
					&& !this.isBound(wristbandId, flightId)) {
				this.addBinding(message, wristbandId, flightId);
			}
		} else {
			if (this.findByWristbandId(wristbandId) != null
					&& this.isBound(wristbandId, flightId)) {
				this.removeBinding(message, wristbandId, flightId);
			}
		}
	}

	/*--------supporting methods----------*/
	public boolean isBound(String wristbandId, String flightId) {
		WristbandInfo wristbandInfo = this.findByWristbandId(wristbandId);

		if (wristbandInfo != null
				&& wristbandInfo.getBoundFlightIdList() != null) {
			List<String> flights = new ArrayList<String>(
					Arrays.asList(wristbandInfo.getBoundFlightIdList()));
			return flights.contains(flightId);
		}

		return false;
	}

	protected void addBinding(WristbandProxyMessages.BindingChanged message,
			String wristbandId, String flightId) {

		_log.debug("wristband[" + wristbandId + "] add a new binding ["
				+ message.getFlightId() + "]");
		WristbandInfo wristbandInfo = this.findByWristbandId(wristbandId);

		int length = wristbandInfo.getBoundFlightIdList().length;
		String[] newFlights = new String[length + 1];
		System.arraycopy(wristbandInfo.getBoundFlightIdList(), 0, newFlights,
				0, length);
		newFlights[length] = flightId;
		wristbandInfo.setBoundFlightIdList(newFlights);

		// WristbandInfo old = this.itemUpdated(wristbandInfo);
		// this.fireItemUpdated(message, old, wristbandInfo);
	}

	protected void removeBinding(WristbandProxyMessages.BindingChanged message,
			String wristbandId, String flightId) {

		_log.debug("wristband[" + wristbandId + "] remove a binding["
				+ message.getFlightId() + "]");

		WristbandInfo wristbandInfo = this.findByWristbandId(wristbandId);
		String[] flights = wristbandInfo.getBoundFlightIdList();
		if (flights != null) {
			List<String> list = new ArrayList<String>(Arrays.asList(flights));
			list.remove(flightId);
			flights = list.toArray(new String[list.size()]);
			wristbandInfo.setBoundFlightIdList(flights);

			// WristbandInfo old = this.itemUpdated(wristbandInfo);
			// this.fireItemUpdated(message, old, wristbandInfo);
		}
	}

	@Override
	protected Collection<WristbandInfo> crawl() {
		WristbandInfo[] wristbandInfoList = _wristbandQuerier
				.getWristbandList(null);

		if (wristbandInfoList != null) {
			return new ArrayList<WristbandInfo>(
					Arrays.asList(wristbandInfoList));
		} else {
			return null;
		}
	}

}
