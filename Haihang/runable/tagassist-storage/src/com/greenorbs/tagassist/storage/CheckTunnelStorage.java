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

import com.greenorbs.tagassist.CheckTunnelInfo;
import com.greenorbs.tagassist.DeviceInfo;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.message.CheckTunnelMessages;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.DeviceRegistered;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.DeviceUnregistered;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.HardwareInfoReport;
import com.greenorbs.tagassist.messagebus.util.querier.CheckTunnelQuerier;


/**
 * 
 * @author Lei Yang
 *
 */
public abstract class CheckTunnelStorage extends AbstractStorage<CheckTunnelInfo> {

	public abstract CheckTunnelInfo findByFlightId(String flightId);
	
	public abstract CheckTunnelInfo findByCheckTunnelId(String checkTunnelId);

	private CheckTunnelQuerier _checkTunnelQuerier;

	public CheckTunnelStorage(Identifiable identity) {
		super(identity);
		this._checkTunnelQuerier = new CheckTunnelQuerier(this._publisher);
	}

	public CheckTunnelStorage() {
		this(null);
	}

	@Override
	protected void initSubscriber() {
		this.subscribe(DeviceRegistered.class);
		this.subscribe(DeviceUnregistered.class);
		
		this.subscribe(CheckTunnelMessages.BindingChanged.class);
		this.subscribe(HardwareInfoReport.class);
	}

	@Override
	public void onMessage(AbstractMessage message) {
		if (message instanceof DeviceRegistered) {

			DeviceRegistered m = (DeviceRegistered) message;
			DeviceInfo deviceInfo = m.getDeviceInfo();
			if (deviceInfo != null && deviceInfo.getComponent() == Identifiable.COMPONENT_CHECK_TUNNEL) {
				String checkTunnelId = deviceInfo.getUUID();
				if (!this.contains(checkTunnelId)) {
					CheckTunnelInfo checkTunnelInfo = _checkTunnelQuerier.getCheckTunnelInfo(checkTunnelId);
					this.add(message, checkTunnelInfo);
				} else {
					CheckTunnelInfo checkTunnelInfo = this.findByCheckTunnelId(checkTunnelId);
					checkTunnelInfo.setDeviceInfo(deviceInfo);
//					CheckTunnelInfo old = this.itemUpdated(checkTunnelInfo);
//					this.fireItemUpdated(message, old, checkTunnelInfo);
				}
			}

		} else if (message instanceof DeviceUnregistered) {

			DeviceUnregistered m = (DeviceUnregistered) message;
			String deviceUUID = m.getDeviceUUID();
			if (this.contains(deviceUUID)) {
				CheckTunnelInfo checkTunnelInfo = this.findByCheckTunnelId(deviceUUID);
				this.remove(message, checkTunnelInfo);
			}

		} else if (message instanceof CheckTunnelMessages.BindingChanged) {
			
			CheckTunnelMessages.BindingChanged m = (CheckTunnelMessages.BindingChanged) message;
			CheckTunnelInfo checkTunnelInfo = this.findByCheckTunnelId(m.getSource());
			if (checkTunnelInfo != null) {
				if (m.getBound()) {
					checkTunnelInfo.setBoundFlightId(m.getFlightId());
				} else {
					checkTunnelInfo.setBoundFlightId(null);
				}
				
//				CheckTunnelInfo old = this.itemUpdated(checkTunnelInfo);
//				this.fireItemUpdated(message, old, checkTunnelInfo);
			}
			
		} else if (message instanceof HardwareInfoReport) {
			
			HardwareInfoReport report = (HardwareInfoReport) message;
			
			if (report.getComponent() == Identifiable.COMPONENT_CHECK_TUNNEL) {
				String checkTunnelId = report.getSource();
				CheckTunnelInfo checkTunnelInfo = this.findByCheckTunnelId(checkTunnelId);
				if (checkTunnelInfo != null) {
					checkTunnelInfo.getDeviceInfo().setName(report.getName());
					checkTunnelInfo.getDeviceInfo().setStatus(report.getStatus());
	
//					CheckTunnelInfo old = this.itemUpdated(checkTunnelInfo);
//					this.fireItemUpdated(message, old, checkTunnelInfo);
				}
			}
			
		}
	}

	@Override
	protected Collection<CheckTunnelInfo> crawl() {
		CheckTunnelInfo[] checkTunnelInfoList = _checkTunnelQuerier.getCheckTunnelList(null);

		if (checkTunnelInfoList != null) {
			return Arrays.asList(checkTunnelInfoList);
		} else {
			return null;
		}
	}
	
}
