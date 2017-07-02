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

@Table(name = "log_tracking")
@Entity
public class TrackingInfo implements Cloneable {
	
	private int _id;

	private String _trackTunnelId;
	
	private String _epc;
	
	private Float _rssi;
	
	private Date _time;
	
	private String _poolId;
	
	private float _distance;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pkid")
	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	@Column(name = "trackTunnelId")
	public String getTrackTunnelId() {
		return _trackTunnelId;
	}

	public void setTrackTunnelId(String trackTunnelId) {
		_trackTunnelId = trackTunnelId;
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

	public void setTime(Date date) {
		_time = date;
	}

	@Column(name = "poolId")
	public String getPoolId() {
		return _poolId;
	}

	public void setPoolId(String poolId) {
		_poolId = poolId;
	}

	@Column(name = "distance")
	public float getDistance() {
		return _distance;
	}

	public void setDistance(float distance) {
		_distance = distance;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
