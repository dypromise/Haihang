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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "log_checking")
@Entity
public class CheckingInfo implements Cloneable {
	
	private int _id;

	private String _checkTunnelId;
	
	private String _epc;
	
	private Float _rssi;
	
	private Date _time;
	
	private int _direction;
	
	private String _carriageId;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pkid")
	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	@Column(name = "checkTunnelId")
	public String getCheckTunnelId() {
		return _checkTunnelId;
	}

	public void setCheckTunnelId(String checkTunnelId) {
		_checkTunnelId = checkTunnelId;
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

	@Column(name = "direction")
	public int getDirection() {
		return _direction;
	}

	public void setDirection(int direction) {
		_direction = direction;
	}

	@Column(name = "carriageId")
	public String getCarriageId() {
		return _carriageId;
	}

	public void setCarriageId(String carriageId) {
		_carriageId = carriageId;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
