package com.greenorbs.tagassist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.greenorbs.tagassist.util.BaggageUtils;

@Table(name = "baggage")
@Entity
public class BaggageInfo extends AbstractPropertySupport implements Cloneable,
		IPropertySupport {

	private static final long serialVersionUID = -6322064326116868546L;
	
	// damage codes
	public static final int DAMAGE_PERFECT = 0;
	public static final int DAMAGE_HAND = 1;
	public static final int DAMAGE_WHEEL = 2;
	public static final int DAMAGE_ZIPPER = 4;
	public static final int DAMAGE_HOLE = 8;
	public static final int DAMAGE_STAINED = 16;
	public static final int DAMAGE_TRANSSHAPE = 32;
	public static final int DAMAGE_LEAKAGE = 64;
	public static final int DAMAGE_CRACK = 128;
	public static final int DAMAGE_CRUSH = 256;

	// status codes
	public static final int STATUS_UNKNOWN = 0;
	public static final int STATUS_ARRIVED = 1;
	public static final int STATUS_IN_POOL = 2;
	public static final int STATUS_READ_BY_WRISTBAND = 3;
	public static final int STATUS_READ_BY_CHECKTUNNEL_RIGHT = 4;
	public static final int STATUS_READ_BY_MOBILEREADER_RIGHT = 5;
	public static final int STATUS_BOARDED = 6;
	public static final int STATUS_REMOVED = 7;
	public static final int STATUS_READ_BY_CHECKTUNNEL_WRONG = 8;
	public static final int STATUS_READ_BY_MOBILEREADER_WRONG = 9;
	public static final int STATUS_MISSING = 10;

	// predefined properties
	public static final String PROPERTY_ID = "Id";
	public static final String PROPERTY_NUMBER = "Number";
	public static final String PROPERTY_EPC = "EPC";
	public static final String PROPERTY_FLIGHT_ID = "FlightId";
	public static final String PROPERTY_DESTINATION = "Destination";
	public static final String PROPRETY_PASSENGER = "Passenger";
	public static final String PROPERTY_WEIGHT = "Weight";
	public static final String PROPERTY_BCLASS = "BClass";
	public static final String PROPERTY_DAMAGE_CODE = "DamageCode";
	public static final String PROPERTY_STATUS = "Status";
	public static final String PROPERTY_CARRIAGE_ID = "CarriageId";
	public static final String PROPERTY_CREATED_TIME = "CreatedTime";
	public static final String PROPERTY_LAST_UPDATED_TIME = "LastUpdatedTime";
	public static final String PROPERTY_LAST_TRACED_DEVICE = "LastTracedDevice";
	public static final String PROPERTY_LAST_TRACED_TIME = "LastTracedTime";
	public static final String PROPERTY_LAST_OPERATOR = "LastOperator";
	public static final String PROPERTY_REVISION = "Revision";

	// extension properties
	public static final String PROPERTY_TRACKED_DISTANCE = "TrackedDistance";
	public static final String PROPERTY_IS_MISSING = "IsMissing";
	public static final String PROPERTY_CARRIAGE_NUMBER = "CarriageNumber";
	public static final String PROPERTY_CARRIAGE_ORDER = "CarriageOrder";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pkid")
	public Integer getId() {
		return (Integer) this.getProperty(PROPERTY_ID);
	}

	public void setId(Integer id) {
		this.setProperty(PROPERTY_ID, id);
	}

	@Column(name = "number")
	public String getNumber() {
		return (String) this.getProperty(PROPERTY_NUMBER);
	}

	public void setNumber(String number) {
		this.setProperty(PROPERTY_NUMBER, number);
	}

	@Column(name = "epc")
	public String getEPC() {
		return (String) this.getProperty(PROPERTY_EPC);
	}

	public void setEPC(String epc) {
		this.setProperty(PROPERTY_EPC, epc);
	}

	@Column(name = "flightId")
	public String getFlightId() {
		return (String) this.getProperty(PROPERTY_FLIGHT_ID);
	}

	public void setFlightId(String flightId) {
		this.setProperty(PROPERTY_FLIGHT_ID, flightId);
	}
	
	@Column(name = "destination")
	public String getDestination() {
		return (String) this.getProperty(PROPERTY_DESTINATION);
	}
	
	public void setDestination(String destination) {
		this.setProperty(PROPERTY_DESTINATION, destination);
	}

	@Column(name = "passenger")
	public String getPassenger() {
		return (String) this.getProperty(PROPRETY_PASSENGER);
	}

	public void setPassenger(String passenger) {
		this.setProperty(PROPRETY_PASSENGER, passenger);
	}

	@Column(name = "weight", nullable = true)
	public Integer getWeight() {
		return (Integer) this.getProperty(PROPERTY_WEIGHT);
	}

	public void setWeight(Integer weight) {
		this.setProperty(PROPERTY_WEIGHT, weight);
	}

	@Column(name = "bclass", nullable = true)
	public String getBClass() {
		return (String) this.getProperty(PROPERTY_BCLASS);
	}

	public void setBClass(String bclass) {
		this.setProperty(PROPERTY_BCLASS, bclass);
	}

	@Column(name = "damageCode")
	public Integer getDamageCode() {
		return (Integer) this.getProperty(PROPERTY_DAMAGE_CODE);
	}

	public void setDamageCode(Integer damageCode) {
		this.setProperty(PROPERTY_DAMAGE_CODE, damageCode);
	}

	@Column(name = "status")
	public Integer getStatus() {
		return (Integer) this.getProperty(PROPERTY_STATUS);
	}

	public void setStatus(Integer status) {
		this.setProperty(PROPERTY_STATUS, status);
	}
	
	@Column(name = "carriageId")
	public String getCarriageId() {
		return (String) this.getProperty(PROPERTY_CARRIAGE_ID);
	}
	
	public void setCarriageId(String carriageId) {
		this.setProperty(PROPERTY_CARRIAGE_ID, carriageId);
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

	@Column(name = "lastTracedDevice", nullable = true)
	public String getLastTracedDevice() {
		return (String) this.getProperty(PROPERTY_LAST_TRACED_DEVICE);
	}

	public void setLastTracedDevice(String lastTracedDevice) {
		this.setProperty(PROPERTY_LAST_TRACED_DEVICE, lastTracedDevice);
	}

	@Column(name = "lastTracedTime", nullable = true)
	public Long getLastTracedTime() {
		return (Long) this.getProperty(PROPERTY_LAST_TRACED_TIME);
	}

	public void setLastTracedTime(Long lastTracedTime) {
		this.setProperty(PROPERTY_LAST_TRACED_TIME, lastTracedTime);
	}
	
	@Column(name = "lastOperator", nullable = true)
	public String getLastOperator() {
		return (String) this.getProperty(PROPERTY_LAST_OPERATOR);
	}

	public void setLastOperator(String lastOperator) {
		this.setProperty(PROPERTY_LAST_OPERATOR, lastOperator);
	}
	
	@Column(name = "revision")
	public Integer getRevision() {
		return (Integer) this.getProperty(PROPERTY_REVISION);
	}
	
	public void setRevision(Integer revision) {
		this.setProperty(PROPERTY_REVISION, revision);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof BaggageInfo) {
			BaggageInfo b = (BaggageInfo) obj;
			if (this.getNumber() == null && b.getNumber() == null) {
				return true;
			} else if (this.getNumber() != null) {
				return this.getNumber().equals(b.getNumber());
			}
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		return super.clone();
	}

	@Override
	public String toString() {
		return this.getNumber();
	}

	/**
	 * The following overriding of hashCode() will cause json-lib to fail.
	 */
	/*
	 * @Override public int hashCode() { return this._number.hashCode(); }
	 */

	@Transient
	@JsonIgnoreField
	public boolean isSorted() {
		return BaggageUtils.isSorted(this);
	}

	@Transient
	@JsonIgnoreField
	public boolean isUnsorted() {
		return BaggageUtils.isUnsorted(this);
	}

	@Transient
	@JsonIgnoreField
	public boolean isMissing() {
		return BaggageUtils.isMissing(this);
	}

	@Transient
	@JsonIgnoreField
	public boolean isRemoved() {
		return BaggageUtils.isRemoved(this);
	}

	@Transient
	@JsonIgnoreField
	public boolean isWronglySorted() {
		return BaggageUtils.isWronglySorted(this);
	}

}
