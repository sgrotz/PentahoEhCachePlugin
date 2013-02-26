package com.ehcache.pentaho.dummyPojo;

import java.io.Serializable;

public class SampleObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 277567342978497425L;

	public Double ID;
	
	public String firstName;
	
	public String lastName;
	
	
	// Default constructor
	public SampleObject() {
		
	}

	public Double getID() {
		return ID;
	}

	public void setID(Double iD) {
		this.ID = iD;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


}
