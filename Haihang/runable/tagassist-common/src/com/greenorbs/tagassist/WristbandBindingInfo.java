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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "wristband_binding")
@Entity
public class WristbandBindingInfo {
	
	private int _id;

	private String _wristbandId;
	
	private String _flightId;
	
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

	@Column(name = "flightId")
	public String getFlightId() {
		return _flightId;
	}

	public void setFlightId(String flightId) {
		_flightId = flightId;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
