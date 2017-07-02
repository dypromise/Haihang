/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 7, 2012
 */

package com.greenorbs.tagassist;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "tracktunnel")
@Entity
public class TrackTunnelInfo extends AbstractPropertySupport implements Cloneable {
	
	private static final long serialVersionUID = 446384644520108346L;

	public static final String PROPERTY_UUID = "UUID";
	
	public static final String PROPERTY_DEVICE_INFO = "DeviceInfo";
	
	public static final String PROPERTY_LOCATION_PARAM_1 = "LocationParam1";
	
	public static final String PROPERTY_LOCATION_PARAM_2 = "LocationParam2";
	
	@Id
	@Column(name = "uuid")
	public String getUUID() {
		return (String) this.getProperty(PROPERTY_UUID);
	}

	public void setUUID(String uuid) {
		this.setProperty(PROPERTY_UUID, uuid);
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "uuid")
	public DeviceInfo getDeviceInfo() {
		return (DeviceInfo) this.getProperty(PROPERTY_DEVICE_INFO);
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.setProperty(PROPERTY_DEVICE_INFO, deviceInfo);
	}

	@Column(name = "locationParam1", nullable = true)
	public String getLocationParam1() {
		return (String) this.getProperty(PROPERTY_LOCATION_PARAM_1);
	}

	public void setLocationParam1(String locationParam1) {
		this.setProperty(PROPERTY_LOCATION_PARAM_1, locationParam1);
	}

	@Column(name = "locationParam2", nullable = true)
	public Float getLocationParam2() {
		return (Float) this.getProperty(PROPERTY_LOCATION_PARAM_2);
	}

	public void setLocationParam2(Float locationParam2) {
		this.setProperty(PROPERTY_LOCATION_PARAM_2, locationParam2);
	}
	
	@Override
	public String toString() {
		if (this.getDeviceInfo() != null) {
			return this.getDeviceInfo().getName();
		} else {
			return "unknown";
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TrackTunnelInfo) {
			TrackTunnelInfo t = (TrackTunnelInfo) obj;
			return this.getUUID().equals(t.getUUID());
		}
		return false;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		TrackTunnelInfo copy = (TrackTunnelInfo) super.clone();
		copy.setDeviceInfo((DeviceInfo) this.getDeviceInfo().clone());
		return copy;
	}
	
}
