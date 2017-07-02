/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 10, 2012
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
import javax.persistence.Transient;

@Table(name = "wristband")
@Entity
public class WristbandInfo extends AbstractPropertySupport implements Cloneable {

	private static final long serialVersionUID = -2756985850075734699L;

	public static final String PROPERTY_UUID = "UUID";
	
	public static final String PROPERTY_DEVICE_INFO = "DeviceInfo";
	
	public static final String PROPERTY_SUB_STATUS = "SubStatus";
	
	public static final String PROPERTY_BATTERY = "Battery";
	
	public static final String PROPERTY_RFID_STATUS = "RFIDStatus";
	
	public static final String PROPERTY_BOUND_FLIGHT_LIST="BoundFlightList";
	
	@Id
	@Column(name = "uuid")
	public String getUUID() {
		return (String) this.getProperty(PROPERTY_UUID);
	}

	public void setUUID(String uuid) {
		String oldValue = this.getUUID();
		this.setProperty(PROPERTY_UUID, uuid);
		this.firePropertyChange(PROPERTY_UUID, oldValue, uuid);
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "uuid")
	public DeviceInfo getDeviceInfo() {
		return (DeviceInfo) this.getProperty(PROPERTY_DEVICE_INFO);
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.setProperty(PROPERTY_DEVICE_INFO, deviceInfo);
	}

	@Column(name = "subStatus", nullable = true)
	public Boolean getSubStatus() {
		return (Boolean) this.getProperty(PROPERTY_SUB_STATUS);
	}

	public void setSubStatus(Boolean subStatus) {
		this.setProperty(PROPERTY_SUB_STATUS, subStatus);
	}

	@Column(name = "battery", nullable = true)
	public Integer getBattery() {
		return (Integer) this.getProperty(PROPERTY_BATTERY);
	}

	public void setBattery(Integer battery) {
		this.setProperty(PROPERTY_BATTERY, battery);
	}

	@Column(name = "rfidStatus", nullable = true)
	public Boolean getRFIDStatus() {
		return (Boolean) this.getProperty(PROPERTY_RFID_STATUS);
	}

	public void setRFIDStatus(Boolean rfidStatus) {
		this.setProperty(PROPERTY_RFID_STATUS, rfidStatus);
	}

	@Transient
	public String[] getBoundFlightIdList() {
		return (String[]) this.getProperty(PROPERTY_BOUND_FLIGHT_LIST);
	}

	public void setBoundFlightIdList(String[] boundFlightIdList) {
		this.setProperty(PROPERTY_BOUND_FLIGHT_LIST, boundFlightIdList);
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
		if (obj instanceof WristbandInfo) {
			WristbandInfo w = (WristbandInfo) obj;
			return this.getUUID().equals(w.getUUID());
		}
		return false;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		WristbandInfo copy = (WristbandInfo) super.clone();
		copy.setDeviceInfo((DeviceInfo) this.getDeviceInfo().clone());
		return copy;
	}
	
}
