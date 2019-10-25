package x40240.grzegorz.wegrzynowicz.a2.app.model;

import java.io.Serializable;

public final class PersonInfo implements Serializable
	{
	private static final long serialVersionUID = 3202578332355446282L;
	
	public static final int GENDER_UNKNOWN   = 0;
	public static final int GENDER_MALE      = 1;
	public static final int GENDER_FEMALE    = 2;
	public static final int NOT_EMPLOYED     = 0;
    public static final int EMPLOYED         = 1;
	
	private String firstname;
	private String lastname;
	private int gender;
	private int employed;
	private String country;
	
	public String getFirstname() {
	    return firstname;
	}
	
	public void setFirstname (String firstname) {
	    this.firstname = firstname;
	}
	
	public String getLastname () {
	    return lastname;
	}
	
	public void setLastname (String lastname) {
	    this.lastname = lastname;
	}
	
	public int getGender () {
	    return gender;
	}
	
	public void setGender (int gender) {
	    this.gender = gender;
	}
	
	public int getEmployed() {
        return employed;
    }
    
    public void setEmployed (int employed) {
        this.employed = employed;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry (String country) {
        this.country = country;
    }
}
