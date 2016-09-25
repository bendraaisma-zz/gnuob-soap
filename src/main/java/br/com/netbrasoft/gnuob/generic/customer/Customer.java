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
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.BUYER_EMAIL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.BUYER_MARKETING_EMAIL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CONTACT_PHONE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CUSTOMER_ENTITY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CUSTOMER_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DATE_OF_BIRTH_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIRST_NAME_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FRIENDLY_NAME_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.LAST_NAME_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MIDDLE_NAME_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAYER_BUSINESS_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAYER_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAYER_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAYER_STATUS_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SALUTATION_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SUFFIX_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.TAX_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.TAX_ID_TYPE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ZERO;
import static com.google.common.collect.Lists.newArrayList;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.velocity.context.Context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.netbrasoft.gnuob.generic.content.contexts.IContextVisitor;
import br.com.netbrasoft.gnuob.generic.security.AbstractAccess;

@Cacheable(value = true)
@Entity(name = CUSTOMER_ENTITY_NAME)
@Table(name = CUSTOMER_TABLE_NAME)
@XmlRootElement(name = CUSTOMER_ENTITY_NAME)
public class Customer extends AbstractAccess {

  private static final long serialVersionUID = -4021500012055256731L;

  private Address address;
  private String buyerEmail;
  private String buyerMarketingEmail;
  private String contactPhone;
  private Date dateOfBirth;
  private String firstName;
  private String friendlyName;
  private String lastName;
  private String middleName;
  private String payer;
  private String payerBusiness;
  private String payerId;
  private String payerStatus;
  private String salutation;
  private String suffix;
  private String taxId;
  private String taxIdType;

  public Customer() {
    super();
  }

  public Customer(String json) throws IOException, IllegalAccessException, InvocationTargetException {
    copyProperties(this, mapper.readValue(json, Customer.class));
  }

  public static Customer getInstance() {
    return new Customer();
  }

  public static Customer getInstanceByJson(String json)
      throws IllegalAccessException, InvocationTargetException, IOException {
    return new Customer(json);
  }

  @Override
  @JsonIgnore
  @Transient
  public boolean isDetached() {
    return newArrayList(isAbstractTypeDetached(), isAddressDetached()).stream().filter(e -> e.booleanValue())
        .count() > ZERO;
  }

  @JsonIgnore
  @Transient
  private boolean isAddressDetached() {
    return address != null && address.isDetached();
  }

  @Override
  public Context accept(final IContextVisitor visitor) {
    return visitor.visit(this);
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @OneToOne(cascade = {PERSIST, MERGE, REMOVE}, orphanRemoval = true)
  public Address getAddress() {
    return address;
  }

  public void setAddress(final Address address) {
    this.address = address;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = BUYER_EMAIL_COLUMN_NAME, nullable = false)
  public String getBuyerEmail() {
    return buyerEmail;
  }

  public void setBuyerEmail(final String buyerEmail) {
    this.buyerEmail = buyerEmail;
  }

  @JsonProperty
  @XmlElement
  @Column(name = BUYER_MARKETING_EMAIL_COLUMN_NAME)
  public String getBuyerMarketingEmail() {
    return buyerMarketingEmail;
  }

  public void setBuyerMarketingEmail(final String buyerMarketingEmail) {
    this.buyerMarketingEmail = buyerMarketingEmail;
  }

  @JsonProperty
  @XmlElement
  @Column(name = CONTACT_PHONE_COLUMN_NAME)
  public String getContactPhone() {
    return contactPhone;
  }

  public void setContactPhone(final String contactPhone) {
    this.contactPhone = contactPhone;
  }

  @JsonProperty
  @XmlElement
  @Column(name = DATE_OF_BIRTH_COLUMN_NAME)
  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(final Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = FIRST_NAME_COLUMN_NAME, nullable = false)
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  @JsonProperty
  @XmlElement
  @Column(name = FRIENDLY_NAME_COLUMN_NAME)
  public String getFriendlyName() {
    if (isBlank(friendlyName)) {
      if (isNotBlank(getMiddleName())) {
        friendlyName = getFirstName() + " " + getMiddleName() + " " + getLastName();
      } else {
        friendlyName = getFirstName() + " " + getLastName();
      }
    }
    return friendlyName;
  }

  public void setFriendlyName(final String friendlyName) {
    this.friendlyName = friendlyName;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = LAST_NAME_COLUMN_NAME, nullable = false)
  public String getLastName() {
    return lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  @JsonProperty
  @XmlElement
  @Column(name = MIDDLE_NAME_COLUMN_NAME)
  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(final String middleName) {
    this.middleName = middleName;
  }

  @JsonProperty
  @XmlElement
  @Column(name = PAYER_COLUMN_NAME)
  public String getPayer() {
    return payer;
  }

  public void setPayer(final String payer) {
    this.payer = payer;
  }

  @JsonProperty
  @XmlElement
  @Column(name = PAYER_BUSINESS_COLUMN_NAME)
  public String getPayerBusiness() {
    return payerBusiness;
  }

  public void setPayerBusiness(final String payerBusiness) {
    this.payerBusiness = payerBusiness;
  }

  @JsonProperty
  @XmlElement
  @Column(name = PAYER_ID_COLUMN_NAME)
  public String getPayerId() {
    return payerId;
  }

  public void setPayerId(final String payerId) {
    this.payerId = payerId;
  }

  @JsonProperty
  @XmlElement
  @Column(name = PAYER_STATUS_COLUMN_NAME)
  public String getPayerStatus() {
    return payerStatus;
  }

  public void setPayerStatus(final String payerStatus) {
    this.payerStatus = payerStatus;
  }

  @JsonProperty
  @XmlElement
  @Column(name = SALUTATION_COLUMN_NAME)
  public String getSalutation() {
    return salutation;
  }

  public void setSalutation(final String salutation) {
    this.salutation = salutation;
  }

  @JsonProperty
  @XmlElement
  @Column(name = SUFFIX_COLUMN_NAME)
  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(final String suffix) {
    this.suffix = suffix;
  }

  @JsonProperty
  @XmlElement
  @Column(name = TAX_ID_COLUMN_NAME)
  public String getTaxId() {
    return taxId;
  }

  public void setTaxId(final String taxId) {
    this.taxId = taxId;
  }

  @JsonProperty
  @XmlElement
  @Column(name = TAX_ID_TYPE_COLUMN_NAME)
  public String getTaxIdType() {
    return taxIdType;
  }

  public void setTaxIdType(final String taxIdType) {
    this.taxIdType = taxIdType;
  }
}
