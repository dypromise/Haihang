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

@Table(name = "checktunnel")
@Entity
public class CheckTunnelInfo extends AbstractPropertySupport implements Cloneable {
	
	private static final long serialVersionUID = 6466936753206420106L;

	public static final String PROPERTY_UUID = "uuid";
	
	public static final String PROPERTY_DEVICEI_INFO = "deviceinfo";
	
	public static final String PROPERTY_BOUND_FLIGHT_ID = "boundFlightId";
	
	public static final String PROPERTY_BOUND_CARRIAGE_ID = "boundCarriageId";
	
	public static final String PROPERTY_LOCATION_PARAM_1 = "locationParam1";
	
	public static final String PROPERTY_LOCATION_PARAM_2 = "locationParam2";
	
	public static final String PROPERTY_LOCATION_PARAM_3 = "locationParam3";
	
	@Id
	@Column(name = "uuid")
	public String getUUID() {
		return (String) getProperty(PROPERTY_UUID);
	}

	public void setUUID(String uuid) {
		this.setProperty(PROPERTY_UUID, uuid);
	}

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "uuid")
	public DeviceInfo getDeviceInfo() {
		return (DeviceInfo) this.getProperty(PROPERTY_DEVICEI_INFO);
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.setProperty(PROPERTY_DEVICEI_INFO, deviceInfo);

	}

	@Column(name = "boundFlightId", nullable = true)
	public String getBoundFlightId() {
		return (String) this.getProperty(PROPERTY_BOUND_FLIGHT_ID);
	}

	public void setBoundFlightId(String boundFlightId) {
		this.setProperty(PROPERTY_BOUND_FLIGHT_ID, boundFlightId);
	}
	
	@Column(name = "boundCarriageId", nullable = true)
	public String getBoundCarriageId() {
		return (String) this.getProperty(PROPERTY_BOUND_CARRIAGE_ID);
	}

	public void setBoundCarriageId(String boundCarriageId) {
		this.setProperty(PROPERTY_BOUND_CARRIAGE_ID, boundCarriageId);
	}

	@Column(name = "locationParam1", nullable = true)
	public String getLocationParam1() {

		return (String) this.getProperty(PROPERTY_LOCATION_PARAM_1);
	}

	public void setLocationParam1(String locationParam1) {
		this.setProperty(PROPERTY_LOCATION_PARAM_1, locationParam1);
	}

	@Column(name = "locationParam2", nullable = true)
	public Integer getLocationParam2() {
		return (Integer) this.getProperty(PROPERTY_LOCATION_PARAM_2);
	}

	public void setLocationParam2(Integer locationParam2) {
		this.setProperty(PROPERTY_LOCATION_PARAM_2, locationParam2);

	}

	@Column(name = "locationParam3", nullable = true)
	public Float getLocationParam3() {
		return (Float) this.getProperty(PROPERTY_LOCATION_PARAM_3);
	}

	public void setLocationParam3(Float locationParam3) {
		this.setProperty(PROPERTY_LOCATION_PARAM_3, locationParam3);
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
		if (obj instanceof CheckTunnelInfo) {
			CheckTunnelInfo c = (CheckTunnelInfo) obj;
			return this.getUUID().equals(c.getUUID());
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		CheckTunnelInfo checkTunnel = (CheckTunnelInfo) super.clone();
		checkTunnel.setDeviceInfo((DeviceInfo) this.getDeviceInfo().clone());
		return checkTunnel;
	}

}
