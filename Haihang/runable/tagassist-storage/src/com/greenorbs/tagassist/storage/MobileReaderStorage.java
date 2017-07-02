/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Authors: Longfei Shangguan, Ding Xuan, Lei Yang, 2012-2-23
 */

package com.greenorbs.tagassist.storage;

import java.util.Arrays;
import java.util.Collection;

import com.greenorbs.tagassist.DeviceInfo;
import com.greenorbs.tagassist.MobileReaderInfo;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.DeviceRegistered;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.DeviceUnregistered;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.HardwareInfoReport;
import com.greenorbs.tagassist.messagebus.message.MobileReaderMessages;
import com.greenorbs.tagassist.messagebus.util.querier.MobileReaderQuerier;

public abstract class MobileReaderStorage extends AbstractStorage<MobileReaderInfo> {

	private MobileReaderQuerier _mobileReaderQuerier;
	
	public abstract MobileReaderInfo findByMobileId(String id);

	public MobileReaderStorage(Identifiable identity) {
		super(identity);
		this._mobileReaderQuerier = new MobileReaderQuerier(this._publisher);
	}

	public MobileReaderStorage() {
		this(null);
	}

	@Override
	protected void initSubscriber() {
		this.subscribe(DeviceRegistered.class);
		this.subscribe(DeviceUnregistered.class);

		this.subscribe(MobileReaderMessages.BindingChanged.class);
		this.subscribe(HardwareInfoReport.class);
	}

	@Override
	public void onMessage(AbstractMessage message) {
		if (message instanceof DeviceRegistered) {

			DeviceRegistered m = (DeviceRegistered) message;
			DeviceInfo deviceInfo = m.getDeviceInfo();
			if (deviceInfo != null
					&& deviceInfo.getComponent() == Identifiable.COMPONENT_MOBILE_READER) {
				String mobileReaderId = deviceInfo.getUUID();
				if (!this.contains(mobileReaderId)) {
					MobileReaderInfo mobileReaderInfo = _mobileReaderQuerier
							.getMobileReaderInfo(mobileReaderId);
					this.add(message, mobileReaderInfo);
				} else {
					MobileReaderInfo mobileReaderInfo = this.findByMobileId(mobileReaderId);
					mobileReaderInfo.setDeviceInfo(deviceInfo);
					// MobileReaderInfo old =
					// this.itemUpdated(mobileReaderInfo);
					// this.fireItemUpdated(message, old, mobileReaderInfo);
				}
			}

		}

		else if (message instanceof DeviceUnregistered) {

			DeviceUnregistered m = (DeviceUnregistered) message;
			String deviceUUID = m.getDeviceUUID();
			if (this.contains(deviceUUID)) {
				MobileReaderInfo mobileReaderInfo = this.findByMobileId(deviceUUID);
				this.remove(message, mobileReaderInfo);
			}

		}

		// config the binding information of mobile reader.
		else if (message instanceof MobileReaderMessages.BindingChanged) {

			MobileReaderMessages.BindingChanged m = (MobileReaderMessages.BindingChanged) message;
			MobileReaderInfo mobileReaderInfo = this.findByMobileId(m.getSource());

			if (mobileReaderInfo != null) {
				if (m.getBound()) {
					mobileReaderInfo.setBoundFlightId(m.getFlightId());
				} else {
					mobileReaderInfo.setBoundFlightId(null);
				}

//				MobileReaderInfo old = this.itemUpdated(mobileReaderInfo);
//				this.fireItemUpdated(message, old, mobileReaderInfo);
			}
		}

		// rename the mobile reader.
		else if (message instanceof HardwareInfoReport) {

			HardwareInfoReport report = (HardwareInfoReport) message;

			if (report.getComponent() == Identifiable.COMPONENT_MOBILE_READER) {
				String mobileReaderId = report.getSource();
				MobileReaderInfo mobileReaderInfo = this.findByMobileId(mobileReaderId);
				if (mobileReaderInfo != null) {
					mobileReaderInfo.getDeviceInfo().setName(report.getName());
					mobileReaderInfo.getDeviceInfo().setStatus(report.getStatus());

//					MobileReaderInfo old = this.itemUpdated(mobileReaderInfo);
//					this.fireItemUpdated(message, old, mobileReaderInfo);
				}
			}
		}

	}

	protected Collection<MobileReaderInfo> crawl() {
		MobileReaderInfo[] mobileReaderInfoList = _mobileReaderQuerier.getMobileReaderList();

		if (mobileReaderInfoList != null) {
			return Arrays.asList(mobileReaderInfoList);
		} else {
			return null;
		}
	}
}
