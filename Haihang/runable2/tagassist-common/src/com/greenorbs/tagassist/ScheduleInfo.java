package com.greenorbs.tagassist;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.activemq.console.Main;

@Table(name = "schedule")
@Entity
public class ScheduleInfo {
	private long _id;
	private String _airline;
	private String _flightNumber;
	private String _planeType;
	private String _operater;
	private String _via;
	private String _dst;

	private long _departTime;
	private String _remark;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public long getId() {
		return _id;
	}

	public void setId(long id) {
		_id = id;
	}

	@Column(name = "airline")
	public String getAirline() {
		return _airline;
	}

	public void setAirline(String airline) {
		_airline = airline;
	}

	@Column(name = "flight_number")
	public String getFlightNumber() {
		return _flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		_flightNumber = flightNumber;
	}

	@Column(name = "plane_type")
	public String getPlaneType() {
		return _planeType;
	}

	public void setPlaneType(String planeType) {
		_planeType = planeType;
	}

	@Column(name = "operater")
	public String getOperater() {
		return _operater;
	}

	public void setOperater(String operater) {
		_operater = operater;
	}

	@Column(name = "via")
	public String getVia() {
		return _via;
	}

	public void setVia(String via) {
		_via = via;
	}

	@Column(name = "depart_time")
	public long getDepartTime() {
		return _departTime;
	}

	public void setDepartTime(long departTime) {
		_departTime = departTime;
	}

	@Column(name = "remark")
	public String getRemark() {
		return _remark;
	}

	public void setRemark(String remark) {
		_remark = remark;
	}

	@Column(name = "dst")
	public String getDst() {
		return _dst;
	}

	public void setDst(String dst) {
		_dst = dst;
	}
	public static void main(String[] args) {
		System.out.println(new Date().getTime());
	}
}
