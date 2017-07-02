/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Feb 12, 2012
 */

package com.greenorbs.tagassist;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "log_identification")
@Entity
public class IdentificationInfo implements Cloneable {

	private int _id;
	
	private String _wristbandId;
	
	private String _epc;
	
	private Float _rssi;
	
	private Date _time;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pkid")
	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	@Column(name = "wristbandId")
	public String getWristbandId() {
		return _wristbandId;
	}

	public void setWristbandId(String wristbandId) {
		_wristbandId = wristbandId;
	}

	@Column(name = "epc")
	public String getEPC() {
		return _epc;
	}

	public void setEPC(String epc) {
		_epc = epc;
	}

	@Column(name = "rssi", nullable = true)
	public Float getRssi() {
		return _rssi;
	}

	public void setRssi(Float rssi) {
		_rssi = rssi;
	}

	@Column(name = "time")
	public Date getTime() {
		return _time;
	}

	public void setTime(Date time) {
		_time = time;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
