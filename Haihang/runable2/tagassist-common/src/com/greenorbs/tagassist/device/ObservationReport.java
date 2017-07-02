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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class ObservationReport implements Iterable<Observation> {

	private boolean _sucessfull = true;

	private Date _time;

	public Set<Observation> _observationList = Collections
			.synchronizedSet(new HashSet<Observation>());

	public boolean isSucessfull() {
		return _sucessfull;
	}

	public void setSucessfull(boolean sucessfull) {
		this._sucessfull = sucessfull;
	}

	public Date getTime() {
		return _time;
	}

	public void setTime(Date time) {
		_time = time;
	}

	public void add(Observation observation) {
		this._observationList.add(observation);
	}

	public void remove(Observation observation) {
		this._observationList.remove(observation);
	}

	public int size() {
		return this._observationList.size();
	}

	public Iterator<Observation> iterator() {
		return this._observationList.iterator();
	}

	public boolean isEmpty() {
		return this._observationList.isEmpty();
	}

	public boolean contains(Observation observation) {
		return this._observationList.contains(observation);
	}
	
	public boolean contains(ObservationReport report){
		return this._observationList.containsAll(report._observationList);
	}

	public void addAll(ObservationReport report) {
		if (report != null) {
			this._observationList.addAll(report._observationList);
		}
	}

	public void clear() {
		this._observationList.clear();
	}

	@Override
	public String toString() {
		return this._observationList.toString();
	}

	public static void main(String[] args) {
		ObservationReport report = new ObservationReport();
		report.add(new Observation());
		System.out.println(report);
	}

}
