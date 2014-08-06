package com.netbrasoft.gnuob.generic.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.netbrasoft.gnuob.generic.Type;

@Entity(name = Address.ENTITY)
@Table(name = Address.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = Address.ENTITY)
public class Address extends Type {

	private static final long serialVersionUID = 1448149897957104670L;
	protected static final String ENTITY = "Address";
	protected static final String TABLE = "GNUOB_ADDRESSES";

	@Column(name = "INTERNATIONAL_STATE_AND_CITY")
	private String internationalStateAndCity;

	@Column(name = "INTERNATIONAL_STREET")
	private String internationalStreet;

	@Column(name = "STREET1")
	private String street1;

	@Column(name = "STREET2")
	private String street2;

	@Column(name = "CITY_NAME")
	private String cityName;

	@Column(name = "STATE_OR_PROVINCE")
	private String stateOrProvince;

	@Column(name = "POSTAL_CODE")
	private String postalCode;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "COUNTRY_NAME")
	private String countryName;

	@Column(name = "PHONE")
	private String phone;

	public Address() {
	}

	@XmlElement(name = "cityName")
	public String getCityName() {
		return cityName;
	}

	@XmlElement(name = "country")
	public String getCountry() {
		return country;
	}

	@XmlElement(name = "countryName")
	public String getCountryName() {
		return countryName;
	}

	@XmlElement(name = "internationalStateAndCity")
	public String getInternationalStateAndCity() {
		return internationalStateAndCity;
	}

	@XmlElement(name = "internationalStreet")
	public String getInternationalStreet() {
		return internationalStreet;
	}

	@XmlElement(name = "phone")
	public String getPhone() {
		return phone;
	}

	@XmlElement(name = "postalCode")
	public String getPostalCode() {
		return postalCode;
	}

	@XmlElement(name = "stateOrProvince")
	public String getStateOrProvince() {
		return stateOrProvince;
	}

	@XmlElement(name = "street1")
	public String getStreet1() {
		return street1;
	}

	@XmlElement(name = "street2")
	public String getStreet2() {
		return street2;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public void setInternationalStateAndCity(String internationalStateAndCity) {
		this.internationalStateAndCity = internationalStateAndCity;
	}

	public void setInternationalStreet(String internationalStreet) {
		this.internationalStreet = internationalStreet;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setStateOrProvince(String stateOrProvince) {
		this.stateOrProvince = stateOrProvince;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

}
