/*
 * . * Copyright 2016 Netbrasoft
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

package br.com.netbrasoft.gnuob.generic.order;

import static br.com.netbrasoft.gnuob.generic.JaxRsActivator.mapper;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.EXCHANGE_RATE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FEE_AMOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GROSS_AMOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.HOLD_DECISION_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.INSTALLMENT_COUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAYMENT_DATE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAYMENT_ENTITY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAYMENT_REQUEST_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAYMENT_STATUS_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAYMENT_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAYMENT_TYPE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PENDING_REASON_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.POSITION_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PROTECTION_ELIGIBILITY_TYPE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REASON_CODE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SETTLE_AMOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.STORE_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.TAX_AMOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.TERMINAL_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.TRANSACTION_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.TRANSACTION_TYPE_COLUMN_NAME;
import static javax.persistence.InheritanceType.SINGLE_TABLE;
import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.netbrasoft.gnuob.generic.AbstractType;

@Cacheable(value = false)
@Entity(name = PAYMENT_ENTITY_NAME)
@Table(name = PAYMENT_TABLE_NAME)
@Inheritance(strategy = SINGLE_TABLE)
@XmlRootElement(name = PAYMENT_ENTITY_NAME)
public class Payment extends AbstractType {

  private static final long serialVersionUID = -4794906337826218748L;

  private String exchangeRate;
  private BigDecimal feeAmount;
  private BigDecimal grossAmount;
  private String holdDecision;
  private BigInteger installmentCount;
  private Date paymentDate;
  private String paymentRequestId;
  private String paymentStatus;
  private String paymentType;
  private String pendingReason;
  private Integer position;
  private String protectionEligibilityType;
  private String reasonCode;
  private BigDecimal settleAmount;
  private String storeId;
  private BigDecimal taxAmount;
  private String terminalId;
  private String transactionId;
  private String transactionType;

  public Payment() {
    super();
  }

  public Payment(String json) throws IOException, IllegalAccessException, InvocationTargetException {
    copyProperties(this, mapper.readValue(json, Payment.class));
  }

  public static Payment getInstance() {
    return new Payment();
  }

  public static Payment getInstanceByJson(String json)
      throws IllegalAccessException, InvocationTargetException, IOException {
    return new Payment(json);
  }

  @Override
  @JsonIgnore
  @Transient
  public boolean isDetached() {
    return isAbstractTypeDetached();
  }

  @JsonProperty
  @XmlElement
  @Column(name = EXCHANGE_RATE_COLUMN_NAME)
  public String getExchangeRate() {
    return exchangeRate;
  }

  public void setExchangeRate(final String exchangeRate) {
    this.exchangeRate = exchangeRate;
  }

  @JsonProperty
  @XmlElement
  @Column(name = FEE_AMOUNT_COLUMN_NAME)
  public BigDecimal getFeeAmount() {
    return feeAmount;
  }

  public void setFeeAmount(final BigDecimal feeAmount) {
    this.feeAmount = feeAmount;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = GROSS_AMOUNT_COLUMN_NAME, nullable = false)
  public BigDecimal getGrossAmount() {
    return grossAmount;
  }

  public void setGrossAmount(final BigDecimal grossAmount) {
    this.grossAmount = grossAmount;
  }

  @JsonProperty
  @XmlElement
  @Column(name = HOLD_DECISION_COLUMN_NAME)
  public String getHoldDecision() {
    return holdDecision;
  }

  public void setHoldDecision(final String holdDecision) {
    this.holdDecision = holdDecision;
  }

  @JsonProperty
  @XmlElement
  @Column(name = INSTALLMENT_COUNT_COLUMN_NAME)
  public BigInteger getInstallmentCount() {
    return installmentCount;
  }

  public void setInstallmentCount(final BigInteger installmentCount) {
    this.installmentCount = installmentCount;
  }

  @JsonProperty
  @XmlElement
  @Column(name = PAYMENT_DATE_COLUMN_NAME)
  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(final Date paymentDate) {
    this.paymentDate = paymentDate;
  }

  @JsonProperty
  @XmlElement
  @Column(name = PAYMENT_REQUEST_ID_COLUMN_NAME)
  public String getPaymentRequestId() {
    return paymentRequestId;
  }

  public void setPaymentRequestId(final String paymentRequestId) {
    this.paymentRequestId = paymentRequestId;
  }

  @JsonProperty
  @XmlElement(required = true)
  @Column(name = PAYMENT_STATUS_COLUMN_NAME, nullable = false)
  public String getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(final String paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  @JsonProperty
  @XmlElement(required = true)
  @Column(name = PAYMENT_TYPE_COLUMN_NAME, nullable = false)
  public String getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(final String paymentType) {
    this.paymentType = paymentType;
  }

  @JsonProperty
  @XmlElement
  @Column(name = PENDING_REASON_COLUMN_NAME)
  public String getPendingReason() {
    return pendingReason;
  }

  public void setPendingReason(final String pendingReason) {
    this.pendingReason = pendingReason;
  }

  @JsonIgnore
  @XmlTransient
  @Column(name = POSITION_COLUMN_NAME)
  public Integer getPosition() {
    return position;
  }

  public void setPosition(final Integer position) {
    this.position = position;
  }

  @JsonProperty
  @XmlElement
  @Column(name = PROTECTION_ELIGIBILITY_TYPE_COLUMN_NAME)
  public String getProtectionEligibilityType() {
    return protectionEligibilityType;
  }

  public void setProtectionEligibilityType(final String protectionEligibilityType) {
    this.protectionEligibilityType = protectionEligibilityType;
  }

  @JsonProperty
  @XmlElement
  @Column(name = REASON_CODE_COLUMN_NAME)
  public String getReasonCode() {
    return reasonCode;
  }

  public void setReasonCode(final String reasonCode) {
    this.reasonCode = reasonCode;
  }

  @JsonProperty
  @XmlElement
  @Column(name = SETTLE_AMOUNT_COLUMN_NAME)
  public BigDecimal getSettleAmount() {
    return settleAmount;
  }

  public void setSettleAmount(final BigDecimal settleAmount) {
    this.settleAmount = settleAmount;
  }

  @JsonProperty
  @XmlElement
  @Column(name = STORE_ID_COLUMN_NAME)
  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(final String storeId) {
    this.storeId = storeId;
  }

  @JsonProperty
  @XmlElement
  @Column(name = TAX_AMOUNT_COLUMN_NAME)
  public BigDecimal getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(final BigDecimal taxAmount) {
    this.taxAmount = taxAmount;
  }

  @JsonProperty
  @XmlElement
  @Column(name = TERMINAL_ID_COLUMN_NAME)
  public String getTerminalId() {
    return terminalId;
  }

  public void setTerminalId(final String terminalId) {
    this.terminalId = terminalId;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = TRANSACTION_ID_COLUMN_NAME, nullable = false)
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(final String transactionId) {
    this.transactionId = transactionId;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = TRANSACTION_TYPE_COLUMN_NAME, nullable = false)
  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(final String transactionType) {
    this.transactionType = transactionType;
  }

  @Override
  public String toString() {
    return reflectionToString(this, SHORT_PREFIX_STYLE);
  }
}
