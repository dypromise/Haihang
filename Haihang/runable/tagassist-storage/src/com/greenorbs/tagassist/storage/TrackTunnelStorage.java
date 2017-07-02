/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Author: Longfei Shangguan, 2012-2-24
 */

package com.greenorbs.tagassist.storage;

import java.util.Arrays;
import java.util.Collection;

import com.greenorbs.tagassist.DeviceInfo;
import com.greenorbs.tagassist.TrackTunnelInfo;
import com.greenorbs.tagassist.device.Identifiable;
import com.greenorbs.tagassist.messagebus.io.AbstractMessage;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.DeviceRegistered;
import com.greenorbs.tagassist.messagebus.message.DataCenterMessages.DeviceUnregistered;
import com.greenorbs.tagassist.messagebus.message.GeneralMessages.HardwareInfoReport;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.LocationMoved;
import com.greenorbs.tagassist.messagebus.message.TrackTunnelMessages.TrackTunnelInfoReport;
import com.greenorbs.tagassist.messagebus.util.querier.TrackTunnelQuerier;


/**
 * 
 * @author Longfei Shangguan
 * @author Xuan Ding
 * @author Lei Yang
 */
public abstract class TrackTunnelStorage extends AbstractStorage<TrackTunnelInfo> {

	private TrackTunnelQuerier _trackTunnelQuerier;
	
	public abstract TrackTunnelInfo findByTrackTunnelId(String trackTunnelId);

	public TrackTunnelStorage(Identifiable identity) {
		super(identity);
		this._trackTunnelQuerier = new TrackTunnelQuerier(this._publisher);
	}

	public TrackTunnelStorage() {
		this(null);
	}

	@Override
	protected void initSubscriber() {
		this.subscribe(DeviceRegistered.class);
		this.subscribe(DeviceUnregistered.class);

		this.subscribe(LocationMoved.class);
		this.subscribe(TrackTunnelInfoReport.class);
		this.subscribe(HardwareInfoReport.class);
	}

	@Override
	public void onMessage(AbstractMessage message) {
		if (message instanceof DeviceRegistered) {

			DeviceRegistered m = (DeviceRegistered) message;
			DeviceInfo deviceInfo = m.getDeviceInfo();
			if (deviceInfo != null && deviceInfo.getComponent() == Identifiable.COMPONENT_TRACK_TUNNEL) {
				String trackTunnelId = deviceInfo.getUUID();
				if (!this.contains(trackTunnelId)) {
					TrackTunnelInfo trackTunnelInfo = _trackTunnelQuerier.getTrackTunnelInfo(trackTunnelId);
					this.add(message, trackTunnelInfo);
				} else {
					TrackTunnelInfo trackTunnelInfo = this.findByTrackTunnelId(trackTunnelId);
					trackTunnelInfo.setDeviceInfo(deviceInfo);
//					TrackTunnelInfo old = this.itemUpdated(trackTunnelInfo);
//					this.fireItemUpdated(message, old, trackTunnelInfo);
				}
			}

		}

		else if (message instanceof DeviceUnregistered) {

			DeviceUnregistered m = (DeviceUnregistered) message;
			String deviceUUID = m.getDeviceUUID();
			if (this.contains(deviceUUID)) {
				TrackTunnelInfo trackTunnelInfo = this.findByTrackTunnelId(deviceUUID);
				this.remove(message, trackTunnelInfo);
//				this.fireItemRemoved(message, trackTunnelInfo);
			}

		}

		// if the location of a specified track tunnel get changed.
		else if (message instanceof LocationMoved) {

			LocationMoved m = (LocationMoved) message;
			TrackTunnelInfo trackTunnelInfo = this.findByTrackTunnelId(m.getSource());
			if (trackTunnelInfo != null) {
				trackTunnelInfo.setLocationParam1(m.getLocationParam1());
				trackTunnelInfo.setLocationParam2(m.getLocationParam2());
//
//				TrackTunnelInfo old = this.itemUpdated(trackTunnelInfo);
//				this.fireItemUpdated(message, old, trackTunnelInfo);
			}

		}
		// if the name of a specified track tunnel get changed.
		else if (message instanceof HardwareInfoReport) {

			HardwareInfoReport report = (HardwareInfoReport) message;

			if (report.getComponent() == Identifiable.COMPONENT_TRACK_TUNNEL) {
				String trackTunnelId = report.getSource();
				TrackTunnelInfo trackTunnelInfo = this.findByTrackTunnelId(trackTunnelId);
				if (trackTunnelInfo != null) {
					trackTunnelInfo.getDeviceInfo().setName(report.getName());
					trackTunnelInfo.getDeviceInfo().setStatus(report.getStatus());
				
					if (message instanceof TrackTunnelInfoReport) {
						TrackTunnelInfoReport r2 = (TrackTunnelInfoReport) message;
						trackTunnelInfo.setLocationParam1(r2.getLocationParam1());
						trackTunnelInfo.setLocationParam2(r2.getLocationParam2());
					}
				
//					TrackTunnelInfo old = this.itemUpdated(trackTunnelInfo);
//					this.fireItemUpdated(message, old, trackTunnelInfo);
				}
			}

		}
	}

	@Override
	protected Collection<TrackTunnelInfo> crawl() {
		TrackTunnelInfo[] trackTunnelInfoList = _trackTunnelQuerier.getTrackTunnelList(null);

		if (trackTunnelInfoList != null) {
			return Arrays.asList(trackTunnelInfoList);
		} else {
			return null;
		}
	}

}
