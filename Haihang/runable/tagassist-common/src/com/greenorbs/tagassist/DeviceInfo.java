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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "device")
@Entity
public class DeviceInfo extends AbstractPropertySupport implements Cloneable {
	
	private static final long serialVersionUID = 1633187271841536629L;

	public static final String PROPERTY_UUID = "uuid";
	public static final String PROPERTY_COMPONENT = "component";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_STATUS= "status";
	public static final String PROPERTY_REGISTERED = "registered";
	public static final String PROPERTY_REGISTER_TIME = "registeredTime";
	public static final String PROPERTY_SOFTWARE_VERSION = "softwareVersion";
	public static final String PROPERTY_HOSTNAME = "hostname";
	public static final String PROPERTY_MAC_ADDRESS = "macAddress";
	public static final String PROPERTY_LAST_ACTIVE_IP = "lastActiveIp";
	public static final String PROPERTY_LAST_ACTIVE_TIME = "lastActiveTime";
	public static final String PROPERTY_REMARK = "remark";

	@Id
	@Column(name = "uuid")
	public String getUUID() {
		return (String) this.getProperty(PROPERTY_UUID);
	}

	public void setUUID(String uuid) {
		this.setProperty(PROPERTY_UUID, uuid);
	}

	@Column(name = "component")
	public Integer getComponent() {
		return (Integer) this.getProperty(PROPERTY_COMPONENT);
	}

	public void setComponent(Integer component) {
		this.setProperty(PROPERTY_COMPONENT, component);
	}

	@Column(name = "name")
	public String getName() {
		return (String) this.getProperty(PROPERTY_NAME);
	}

	public void setName(String name) {
		this.setProperty(PROPERTY_NAME, name);
	}

	@Column(name = "status")
	public Integer getStatus() {
		return (Integer) this.getProperty(PROPERTY_STATUS);
	}

	public void setStatus(Integer status) {
		this.setProperty(PROPERTY_STATUS, status);

	}

	@Column(name = "registered")
	public Boolean getRegistered() {
		return (Boolean) this.getProperty(PROPERTY_REGISTERED);
	}

	public void setRegistered(Boolean registered) {
		this.setProperty(PROPERTY_REGISTERED, registered);
	}

	@Column(name = "registTime", nullable = true)
	public Long getRegistTime() {
		return (Long) this.getProperty(PROPERTY_REGISTER_TIME);
	}

	public void setRegistTime(Long registTime) {
		this.setProperty(PROPERTY_REGISTER_TIME, registTime);
	}
	
	@Column(name = "softwareVersion", nullable = true)
	public String getSoftwareVersion() {
		return (String) this.getProperty(PROPERTY_SOFTWARE_VERSION);
	}
	
	public void setSoftwareVersion(String softwareVersion) {
		this.setProperty(PROPERTY_SOFTWARE_VERSION, softwareVersion);
	}
	
	@Column(name = "hostname", nullable = true)
	public String getHostname() {
		return (String) this.getProperty(PROPERTY_HOSTNAME);
	}
	
	public void setHostname(String hostname) {
		this.setProperty(PROPERTY_HOSTNAME, hostname);
	}
	
	@Column(name = "macAddress", nullable = true)
	public String getMacAddress() {
		return (String) this.getProperty(PROPERTY_MAC_ADDRESS);
	}
	
	public void setMacAddress(String lastActiveMac) {
		this.setProperty(PROPERTY_MAC_ADDRESS, lastActiveMac);
	}
	
	@Column(name = "lastActiveIp", nullable = true)
	public String getLastActiveIp() {
		return (String) this.getProperty(PROPERTY_LAST_ACTIVE_IP);
	}
	
	public void setLastActiveIp(String lastActiveIp) {
		this.setProperty(PROPERTY_LAST_ACTIVE_IP, lastActiveIp);
	}
	
	@Column(name = "lastActiveTime", nullable = true)
	public Long getLastActiveTime() {
		return (Long) this.getProperty(PROPERTY_LAST_ACTIVE_TIME);
	}
	
	public void setLastActiveTime(Long lastActiveTime) {
		this.setProperty(PROPERTY_LAST_ACTIVE_TIME, lastActiveTime);
	}

	@Column(name = "remark", nullable = true)
	public String getRemark() {
		return (String) this.getProperty(PROPERTY_REMARK);
	}

	public void setRemark(String remark) {
		this.setProperty(PROPERTY_REMARK, remark);
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DeviceInfo) {
			DeviceInfo d = (DeviceInfo) obj;
			if (this.getUUID() != null) {
				return this.getUUID().equals(d.getUUID());
			}
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
