package com.greenorbs.tagassist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "properties")
@Entity
public class PropertyInfo extends AbstractPropertySupport implements Cloneable {

	private static final long serialVersionUID = -3056269528344693451L;
	
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_VALUE = "value";
	@Id
	@Column(name = "name")
	public String getName() {
		return (String) this.getProperty(PROPERTY_NAME);
	}

	public void setName(String name) {
		this.setProperty(PROPERTY_NAME, name);
	}

	@Column(name = "value")
	public String getValue() {
		return (String) this.getProperty(PROPERTY_VALUE);
	}

	public void setValue(String value) {
		this.setProperty(PROPERTY_VALUE, value);
	}

}
