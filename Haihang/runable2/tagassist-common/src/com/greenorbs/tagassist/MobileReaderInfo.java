/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 22, 2012
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

@Table(name = "mobilereader")
@Entity
public class MobileReaderInfo extends AbstractPropertySupport implements Cloneable {

	private static final long serialVersionUID = -3546457129427439661L;

	public static final String PROPERTY_UUID = "UUID";
	
	public static final String PROPERTY_DEVICE_INFO = "DeviceInfo";
	
	public static final String PROPERTY_BOUND_FLIGHT_ID = "BoundFlightId";
	
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

	@Column(name = "boundFlightId", nullable = true)
	public String getBoundFlightId() {
		return (String) this.getProperty(PROPERTY_BOUND_FLIGHT_ID);
	}

	public void setBoundFlightId(String boundFlightId) {
		this.setProperty(PROPERTY_BOUND_FLIGHT_ID, boundFlightId);
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
		if (obj instanceof MobileReaderInfo) {
			MobileReaderInfo m = (MobileReaderInfo) obj;
			return this.getUUID().equals(m.getUUID());
		}
		return false;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		MobileReaderInfo copy = (MobileReaderInfo) super.clone();
		copy.setDeviceInfo((DeviceInfo) this.getDeviceInfo().clone());
		return copy;
	}
	
}
