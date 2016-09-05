/*
 * Copyright 2016 Netbrasoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package br.com.netbrasoft.gnuob.generic.customer;

import static br.com.netbrasoft.gnuob.generic.JaxRsActivator.mapper;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ADDRESS_ENTITY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ADDRESS_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CITY_NAME_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.COMPLEMENT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.COUNTRY_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.COUNTRY_NAME_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DISTRICT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.INTERNATIONAL_STATE_AND_CITY_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.INTERNATIONAL_STREET_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.NUMBER_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PHONE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.POSTAL_CODE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.STATE_OR_PROVINCE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.STREET1_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.STREET2_COLUMN_NAME;
import static javax.persistence.InheritanceType.SINGLE_TABLE;
import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.netbrasoft.gnuob.generic.AbstractType;

@Cacheable(value = false)
@Entity(name = ADDRESS_ENTITY_NAME)
@Table(name = ADDRESS_TABLE_NAME)
@Inheritance(strategy = SINGLE_TABLE)
@XmlRootElement(name = ADDRESS_ENTITY_NAME)
public class Address extends AbstractType {

  private static final long serialVersionUID = 1448149897957104670L;

  private String cityName;
  private String complement;
  private String country;
  private String countryName;
  private String district;
  private String internationalStateAndCity;
  private String internationalStreet;
  private String number;
  private String phone;
  private String postalCode;
  private String stateOrProvince;
  private String street1;
  private String street2;

  public Address() {
    super();
  }

  public Address(String json) throws IllegalAccessException, InvocationTargetException, IOException {
    copyProperties(this, mapper.readValue(json, Address.class));
  }

  public static Address getInstance() {
    return new Address();
  }

  public static Address getInstanceByJson(String json) throws IllegalAccessException, InvocationTargetException, IOException {
    return new Address(json);
  }

  @Override
  @JsonIgnore
  @Transient
  public boolean isDetached() {
    return isAbstractTypeDetached();
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = CITY_NAME_COLUMN_NAME, nullable = false)
  public String getCityName() {
    return cityName;
  }

  public void setCityName(final String cityName) {
    this.cityName = cityName;
  }

  @JsonProperty
  @XmlElement
  @Column(name = COMPLEMENT_COLUMN_NAME)
  public String getComplement() {
    return complement;
  }

  public void setComplement(final String complement) {
    this.complement = complement;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = COUNTRY_COLUMN_NAME, nullable = false)
  public String getCountry() {
    return country;
  }

  public void setCountry(final String country) {
    this.country = country;
  }

  @JsonProperty
  @XmlElement
  @Column(name = COUNTRY_NAME_COLUMN_NAME)
  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(final String countryName) {
    this.countryName = countryName;
  }

  @JsonProperty
  @XmlElement
  @Column(name = DISTRICT_COLUMN_NAME)
  public String getDistrict() {
    return district;
  }

  public void setDistrict(final String district) {
    this.district = district;
  }

  @JsonProperty
  @XmlElement
  @Column(name = INTERNATIONAL_STATE_AND_CITY_COLUMN_NAME)
  public String getInternationalStateAndCity() {
    return internationalStateAndCity;
  }

  public void setInternationalStateAndCity(final String internationalStateAndCity) {
    this.internationalStateAndCity = internationalStateAndCity;
  }

  @JsonProperty
  @XmlElement
  @Column(name = INTERNATIONAL_STREET_COLUMN_NAME)
  public String getInternationalStreet() {
    return internationalStreet;
  }

  public void setInternationalStreet(final String internationalStreet) {
    this.internationalStreet = internationalStreet;
  }

  @JsonProperty
  @XmlElement
  @Column(name = NUMBER_COLUMN_NAME)
  public String getNumber() {
    return number;
  }

  public void setNumber(final String number) {
    this.number = number;
  }

  @JsonProperty
  @XmlElement
  @Column(name = PHONE_COLUMN_NAME)
  public String getPhone() {
    return phone;
  }

  public void setPhone(final String phone) {
    this.phone = phone;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = POSTAL_CODE_COLUMN_NAME, nullable = false)
  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(final String postalCode) {
    this.postalCode = postalCode;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = STATE_OR_PROVINCE_COLUMN_NAME, nullable = false)
  public String getStateOrProvince() {
    return stateOrProvince;
  }

  public void setStateOrProvince(final String stateOrProvince) {
    this.stateOrProvince = stateOrProvince;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = STREET1_COLUMN_NAME, nullable = false)
  public String getStreet1() {
    return street1;
  }

  public void setStreet1(final String street1) {
    this.street1 = street1;
  }

  @JsonProperty
  @XmlElement
  @Column(name = STREET2_COLUMN_NAME)
  public String getStreet2() {
    return street2;
  }

  public void setStreet2(final String street2) {
    this.street2 = street2;
  }

  @Override
  public String toString() {
    return reflectionToString(this, SHORT_PREFIX_STYLE);
  }
}
