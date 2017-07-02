package com.greenorbs.tagassist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "carriage")
@Entity
public class CarriageInfo extends AbstractPropertySupport implements Cloneable {

	private static final long serialVersionUID = 7528211965660278796L;

	// status codes
	public static final int STATUS_UNKNOWN = 0;
	public static final int STATUS_AVAILABLE = 1;
	public static final int STATUS_IN_USE = 2;
	
	// predefined properties
	public static final String PROPERTY_UUID = "uuid";
	public static final String PROPERTY_NUMBER = "Number";
	public static final String PROPERTY_NET_WEIGHT = "NetWeight";
	public static final String PROPERTY_FLIGHT_ID = "FlightId";
	public static final String PROPERTY_DESTINATION = "Destination";
	public static final String PROPERTY_STATUS = "Status";
	public static final String PROPERTY_REGIST_TIME = "RegistTime";
	
	// extension properties
	public static final String PROPERTY_BAGGAGE_COUNT = "BaggageCount";
	public static final String PROPERTY_CARRIAGE_ORDER = "CarriageOrder";
	
	@Id
	@Column(name = "uuid")
	public String getUUID() {
		return (String) this.getProperty(PROPERTY_UUID);
	}

	public void setUUID(String uuid) {
		this.setProperty(PROPERTY_UUID, uuid);
	}

	@Column(name = "number")
	public String getNumber() {
		return (String) this.getProperty(PROPERTY_NUMBER);
	}

	public void setNumber(String number) {
		this.setProperty(PROPERTY_NUMBER, number);
	}
	
	@Column(name = "netWeight", nullable = true)
	public Integer getNetWeight() {
		return (Integer) this.getProperty(PROPERTY_NET_WEIGHT);
	}
	
	public void setNetWeight(Integer netWeight) {
		this.setProperty(PROPERTY_NET_WEIGHT, netWeight);
	}
	
	@Column(name = "flightId", nullable = true)
	public String getFlightId() {
		return (String) this.getProperty(PROPERTY_FLIGHT_ID);
	}

	public void setFlightId(String flightId) {
		this.setProperty(PROPERTY_FLIGHT_ID, flightId);
	}

	@Column(name = "destination", nullable = true)
	public String getDestination() {
		return (String) this.getProperty(PROPERTY_DESTINATION);
	}
	
	public void setDestination(String destination) {
		this.setProperty(PROPERTY_DESTINATION, destination);
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return (Integer) this.getProperty(PROPERTY_STATUS);
	}

	public void setStatus(Integer status) {
		this.setProperty(PROPERTY_STATUS, status);
	}
	
	@Column(name = "registTime", nullable = true)
	public Long getRegistTime() {
		return (Long) this.getProperty(PROPERTY_REGIST_TIME);
	}

	public void setRegistTime(Long registTime) {
		this.setProperty(PROPERTY_REGIST_TIME, registTime);
	}
	
}
