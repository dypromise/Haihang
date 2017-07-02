/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Haoxiang Liu, 2012-2-8
 */
package com.greenorbs.tagassist.device;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains methods and fields that represent result of 
 * an identification process. Each observation is associated with 
 * a certain reading point, which refers to a reading antenna.
 * 
 * @author Haoxiang
 * 
 */

public class Observation {

	private String _readPoint="";

	private long _timeStamp;

	private String _epc="";
	
	private String _data="";

	private int _rssi;
	
	private int _channelIndex;

	private int _count;
	
	private double _phaseAngle;
	
	private double _dopperFrequency;
	
	private int _peekRssi;

	
	public Observation(){
		
	}
	
	public Observation(String epc, String readPoint, int rssi, int count, long timestamp){
		this._epc = epc;
		this._readPoint = readPoint;
		this._rssi = rssi;
		this._count = count;
		this._timeStamp = timestamp;
	}
	//dingyang add
	public Observation(String epc, String readPoint, int rssi, int count, long timestamp,String data){
		this._epc = epc;
		this._readPoint = readPoint;
		this._rssi = rssi;
		this._count = count;
		this._timeStamp = timestamp;
		this._data = data;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Observation) {
			Observation obs = (Observation) obj;
			return _epc.equals(obs._epc);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this._epc.hashCode();
	}

	public String getReadoint() {
		return _readPoint;
	}

	public void setReadPoint(String readPoint) {
		_readPoint = readPoint;
	}

	public long getTimeStamp() {
		return _timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		_timeStamp = timeStamp;
	}

	public String getEPC() {
		return _epc;
	}

	public void setEPC(String epc) {
		_epc = epc;
	}
	
	//dingyang add
	public String getData() {
		return _data;
	}

	public void setData(String data) {
		_data = data;
	}

	public int getRssi() {
		return _rssi;
	}

	public void setRssi(int rssi) {
		_rssi = rssi;
	}

	public int getCount() {
		return _count;
	}

	public void setCount(int count) {
		_count = count;
	}

	public int getChannelIndex() {
		return _channelIndex;
	}

	public void setChannelIndex(int channelIndex) {
		_channelIndex = channelIndex;
	}

	public double getPhaseAngle() {
		return _phaseAngle;
	}

	public void setPhaseAngle(double phaseAngle) {
		_phaseAngle = phaseAngle;
	}

	public double getDopperFrequency() {
		return _dopperFrequency;
	}

	public void setDopperFrequency(double dopperFrequency) {
		_dopperFrequency = dopperFrequency;
	}

	public int getPeekRssi() {
		return _peekRssi;
	}

	public void setPeekRssi(int peekRssi) {
		_peekRssi = peekRssi;
	}
	
	//dingyang change
	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append("{observation:{");
		sb.append("epc:"+this._epc+",");
		//dingyang change
		sb.append("data:"+this._data+",");
		sb.append("channelIndex:"+this._channelIndex+",");
		sb.append("count:"+this._count+",");
		sb.append("dopperFrequency:"+this._dopperFrequency+",");
		sb.append("peekRssi:"+this._peekRssi+",");
		sb.append("phaseAngle:"+this._phaseAngle+",");
		sb.append("readPoint:"+this._readPoint+",");
		sb.append("rssi:"+this._rssi+",");
		sb.append("timestamp:"+this._timeStamp);
		sb.append("}}");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Observation a = new Observation();
		HashSet<Observation> set = new HashSet<Observation>();
		set.add(a);
		System.out.println(new Observation());
	}

}
