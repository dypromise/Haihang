package com.greenorbs.tagassist;

import java.text.ParseException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.greenorbs.tagassist.util.FlightUtils;

@Table(name = "flight")
@Entity
public class FlightInfo extends AbstractPropertySupport implements Cloneable {

	private static final long serialVersionUID = 209635380695028885L;
	
	public static final int STATUS_SCHEDULED = 1;
	public static final int STATUS_RESCHEDULED = 2;
	@Deprecated public static final int STATUS_DELAYED = 2;
	public static final int STATUS_CANCELED = 3;

	public static final int STATUS_CHECKING_IN = 4;
	public static final int STATUS_CLOSED = 5;
	@Deprecated public static final int STATUS_SORT_TERMINATED = 6;
	public static final int STATUS_DEPARTURED = 7;
	
	public static final String PROPERTY_ID="id";
	public static final String PROPERTY_FLIGHT_ID = "flightId";
	public static final String PROPERTY_ORIGIN = "origin";
	public static final String PROPERTY_DESTINATION = "destination";
	public static final String PROPERTY_VIA = "via";
	public static final String PROPERTY_SDT = "sdt";
	public static final String PROPERTY_EDT = "edt";
	public static final String PROPERTY_ADT = "adt";
	public static final String PROPERTY_STATUS = "status";
	public static final String PROPERTY_CREATED_TIME = "createdTime";
	public static final String PROPERTY_LAST_UPDATED_TIME = "lastUpdatedTime";
	public static final String PROPERTY_SORTED_BAGGAGE_SIZE = "SortedBaggageSize";
	public static final String PROPERTY_UNSORTED_BAGGAGE_SIZE= "UnsortedBaggageSize";
	public static final String PROPERTY_MISSING_BAGGAGE_SIZE = "MissingBaggageSize";
	public static final String PROPERTY_TOTAL_BAGGAGE_SIZE = "TotalBaggageSize";
	
	@Deprecated
	public static final String PROPERTY_DEPART_TIME = "departTime";
	@Deprecated
	public static final String PROPERTY_ARRIVE_TIME ="arriveTime";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pkid")
	public Integer getId() {
		return (Integer)this.getProperty(PROPERTY_ID);
	}

	public void setId(Integer id) {
		this.setProperty(PROPERTY_ID, id);
	}

	@Column(name = "flightId")
	public String getFlightId() {
		return (String)this.getProperty(PROPERTY_FLIGHT_ID);
	}

	public void setFlightId(String flightId) {
		this.setProperty(PROPERTY_FLIGHT_ID, flightId);
	}

	@Transient
	@JsonIgnoreField
	public String getFlightCode() {
		try {
			return FlightUtils.parseFlightCode(this.getFlightId());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@Column(name = "origin", nullable = true)
	public String getOrigin() {
		return (String)this.getProperty(PROPERTY_ORIGIN);
	}

	public void setOrigin(String origin) {
		this.setProperty(PROPERTY_ORIGIN, origin);
	}

	@Column(name = "destination", nullable = true)
	public String getDestination() {
		return (String)this.getProperty(PROPERTY_DESTINATION);
	}

	public void setDestination(String destination) {
		this.setProperty(PROPERTY_DESTINATION, destination);
	}

	@Column(name = "via", nullable = true)
	public String getVia() {
		return (String)this.getProperty(PROPERTY_VIA);
	}

	public void setVia(String via) {
		this.setProperty(PROPERTY_VIA, via);
	}

	@Column(name = "sdt", nullable = true)
	public Long getSDT() {
		return (Long)this.getProperty(PROPERTY_SDT);
	}

	public void setSDT(Long sdt) {
		this.setProperty(PROPERTY_SDT, sdt);
	}

	@Column(name = "edt", nullable = true)
	public Long getEDT() {
		return (Long)this.getProperty(PROPERTY_EDT);
	}

	public void setEDT(Long edt) {
		this.setProperty(PROPERTY_EDT, edt);
	}

	@Column(name = "adt", nullable = true)
	public Long getADT() {
		return (Long)this.getProperty(PROPERTY_ADT);
	}

	public void setADT(Long adt) {
		this.setProperty(PROPERTY_ADT, adt);
	}

	@Column(name = "status")
	public Integer getStatus() {
		return (Integer)this.getProperty(PROPERTY_STATUS);
	}

	public void setStatus(Integer status) {
		
		this.setProperty(PROPERTY_STATUS, status);
	}
	
	@Column(name = "createdTime", nullable = true)
	public Long getCreatedTime() {
		return (Long) this.getProperty(PROPERTY_CREATED_TIME);
	}
	
	public void setCreatedTime(Long createdTime) {
		this.setProperty(PROPERTY_CREATED_TIME, createdTime);
	}
	
	@Column(name = "lastUpdatedTime", nullable = true)
	public Long getLastUpdatedTime() {
		return (Long) this.getProperty(PROPERTY_LAST_UPDATED_TIME);
	}
	
	public void setLastUpdatedTime(Long lastUpdatedTime) {
		this.setProperty(PROPERTY_LAST_UPDATED_TIME, lastUpdatedTime);
	}

	@Deprecated
	@Column(name = "departTime")
	public Long getDepartTime() {
		return (Long)this.getProperty(PROPERTY_DEPART_TIME);
	}

	@Deprecated
	public void setDepartTime(Long departTime) {
		this.setProperty(PROPERTY_DEPART_TIME, departTime);
	}

	@Deprecated
	@Column(name = "arriveTime")
	public Long getArriveTime() {
		return (Long)this.getProperty(PROPERTY_ARRIVE_TIME);
	}

	@Deprecated
	public void setArriveTime(Long arriveTime) {
		this.setProperty(PROPERTY_ARRIVE_TIME, arriveTime);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FlightInfo) {
			FlightInfo f = (FlightInfo) obj;
			return this.getFlightId().equals(f.getFlightId());
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return this.getFlightId();
	}
	
}
