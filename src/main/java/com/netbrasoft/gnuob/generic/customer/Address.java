package com.netbrasoft.gnuob.generic.customer;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.netbrasoft.gnuob.generic.Type;

@Cacheable(value = false)
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

  @Column(name = "STREET1", nullable = false)
  private String street1;

  @Column(name = "STREET2")
  private String street2;

  @Column(name = "NUMBER")
  private String number;

  @Column(name = "COMPLEMENT")
  private String complement;

  @Column(name = "DISTRICT")
  private String district;

  @Column(name = "CITY_NAME", nullable = false)
  private String cityName;

  @Column(name = "STATE_OR_PROVINCE", nullable = false)
  private String stateOrProvince;

  @Column(name = "POSTAL_CODE", nullable = false)
  private String postalCode;

  @Column(name = "COUNTRY", nullable = false)
  private String country;

  @Column(name = "COUNTRY_NAME")
  private String countryName;

  @Column(name = "PHONE")
  private String phone;

  public Address() {}

  @XmlElement(name = "cityName", required = true)
  public String getCityName() {
    return cityName;
  }

  @XmlElement(name = "complement")
  public String getComplement() {
    return complement;
  }

  @XmlElement(name = "country", required = true)
  public String getCountry() {
    return country;
  }

  @XmlElement(name = "countryName")
  public String getCountryName() {
    return countryName;
  }

  @XmlElement(name = "district")
  public String getDistrict() {
    return district;
  }

  @XmlElement(name = "internationalStateAndCity")
  public String getInternationalStateAndCity() {
    return internationalStateAndCity;
  }

  @XmlElement(name = "internationalStreet")
  public String getInternationalStreet() {
    return internationalStreet;
  }

  @XmlElement(name = "number")
  public String getNumber() {
    return number;
  }

  @XmlElement(name = "phone")
  public String getPhone() {
    return phone;
  }

  @XmlElement(name = "postalCode", required = true)
  public String getPostalCode() {
    return postalCode;
  }

  @XmlElement(name = "stateOrProvince", required = true)
  public String getStateOrProvince() {
    return stateOrProvince;
  }

  @XmlElement(name = "street1", required = true)
  public String getStreet1() {
    return street1;
  }

  @XmlElement(name = "street2")
  public String getStreet2() {
    return street2;
  }

  @Override
  public void prePersist() {
    return;
  }

  @Override
  public void preUpdate() {
    return;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public void setComplement(String complement) {
    this.complement = complement;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public void setInternationalStateAndCity(String internationalStateAndCity) {
    this.internationalStateAndCity = internationalStateAndCity;
  }

  public void setInternationalStreet(String internationalStreet) {
    this.internationalStreet = internationalStreet;
  }

  public void setNumber(String number) {
    this.number = number;
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
