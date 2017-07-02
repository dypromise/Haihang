/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Xuan Ding, Mar 28, 2012
 */

package com.greenorbs.tagassist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "log_tracing")
@Entity
public class TracingInfo implements Cloneable {
	
	private int _id;
	
	private String _baggageNumber;
	
	private int _status;
	
	private String _device;
	
	private String _operator;
	
	private long _time;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pkid")
	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	@Column(name = "baggageNumber")
	public String getBaggageNumber() {
		return _baggageNumber;
	}

	public void setBaggageNumber(String baggageNumber) {
		_baggageNumber = baggageNumber;
	}

	@Column(name = "status")
	public int getStatus() {
		return _status;
	}

	public void setStatus(int status) {
		_status = status;
	}

	@Column(name = "device")
	public String getDevice() {
		return _device;
	}

	public void setDevice(String device) {
		_device = device;
	}

	@Column(name = "operator", nullable = true)
	public String getOperator() {
		return _operator;
	}

	public void setOperator(String operator) {
		_operator = operator;
	}

	@Column(name = "time")
	public long getTime() {
		return _time;
	}

	public void setTime(long time) {
		_time = time;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
