package com.netbrasoft.gnuob.generic.order;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.netbrasoft.gnuob.generic.AbstractType;

@Cacheable(value = false)
@Entity(name = Payment.ENTITY)
@Table(name = Payment.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = Payment.ENTITY)
public class Payment extends AbstractType {

  private static final long serialVersionUID = -4794906337826218748L;
  protected static final String ENTITY = "Payment";
  protected static final String TABLE = "GNUOB_PAYMENTS";

  @Column(name = "TRANSACTION_ID", nullable = false)
  private String transactionId;

  @Column(name = "TRANSACTION_TYPE", nullable = false)
  private String transactionType;

  @Column(name = "PAYMENT_TYPE", nullable = false)
  private String paymentType;

  @Column(name = "GROSS_AMOUNT", nullable = false)
  private BigDecimal grossAmount;

  @Column(name = "FEE_AMOUNT")
  private BigDecimal feeAmount;

  @Column(name = "SETTLE_AMOUNT")
  private BigDecimal settleAmount;

  @Column(name = "TAX_AMOUNT")
  private BigDecimal taxAmount;

  @Column(name = "EXCHANGE_RATE")
  private String exchangeRate;

  @Column(name = "PAYMENT_STATUS", nullable = false)
  private String paymentStatus;

  @Column(name = "PENDING_REASON")
  private String pendingReason;

  @Column(name = "REASON_CODE")
  private String reasonCode;

  @Column(name = "HOLD_DECISION")
  private String holdDecision;

  @Column(name = "PROTECTION_ELIGIBILITY_TYPE")
  private String protectionEligibilityType;

  @Column(name = "STORE_ID")
  private String storeId;

  @Column(name = "TERMINAL_ID")
  private String terminalId;

  @Column(name = "PAYMENT_REQUEST_ID")
  private String paymentRequestId;

  @Column(name = "PAYMENT_DATE")
  private Date paymentDate;

  @Column(name = "INSTALLMENT_COUNT")
  private BigInteger installmentCount;

  @Column(name = "POSITION")
  private Integer position;

  @XmlElement(name = "exchangeRate")
  public String getExchangeRate() {
    return exchangeRate;
  }

  @XmlElement(name = "feeAmount")
  public BigDecimal getFeeAmount() {
    return feeAmount;
  }

  @XmlElement(name = "grossAmount", required = true)
  public BigDecimal getGrossAmount() {
    return grossAmount;
  }

  @XmlElement(name = "holdDecision")
  public String getHoldDecision() {
    return holdDecision;
  }

  @XmlElement(name = "installmentCount")
  public BigInteger getInstallmentCount() {
    return installmentCount;
  }

  @XmlElement(name = "paymentDate")
  public Date getPaymentDate() {
    return paymentDate;
  }

  @XmlElement(name = "paymentRequestId")
  public String getPaymentRequestId() {
    return paymentRequestId;
  }

  @XmlElement(name = "paymentStatus", required = true)
  public String getPaymentStatus() {
    return paymentStatus;
  }

  @XmlElement(name = "paymentType", required = true)
  public String getPaymentType() {
    return paymentType;
  }

  @XmlElement(name = "pendingReason")
  public String getPendingReason() {
    return pendingReason;
  }

  @XmlTransient
  public Integer getPosition() {
    return position;
  }

  @XmlElement(name = "protectionEligibilityType")
  public String getProtectionEligibilityType() {
    return protectionEligibilityType;
  }

  @XmlElement(name = "reasonCode")
  public String getReasonCode() {
    return reasonCode;
  }

  @XmlElement(name = "settleAmount")
  public BigDecimal getSettleAmount() {
    return settleAmount;
  }

  @XmlElement(name = "storeId")
  public String getStoreId() {
    return storeId;
  }

  @XmlElement(name = "taxAmount")
  public BigDecimal getTaxAmount() {
    return taxAmount;
  }

  @XmlElement(name = "terminalId")
  public String getTerminalId() {
    return terminalId;
  }

  @XmlElement(name = "transactionId", required = true)
  public String getTransactionId() {
    return transactionId;
  }

  @XmlElement(name = "transactionType", required = true)
  public String getTransactionType() {
    return transactionType;
  }

  @Override
  public boolean isDetached() {
    return getId() > 0;
  }

  @Override
  public void prePersist() {
    return;
  }

  @Override
  public void preUpdate() {
    return;
  }

  public void setExchangeRate(final String exchangeRate) {
    this.exchangeRate = exchangeRate;
  }

  public void setFeeAmount(final BigDecimal feeAmount) {
    this.feeAmount = feeAmount;
  }

  public void setGrossAmount(final BigDecimal grossAmount) {
    this.grossAmount = grossAmount;
  }

  public void setHoldDecision(final String holdDecision) {
    this.holdDecision = holdDecision;
  }

  public void setInstallmentCount(final BigInteger installmentCount) {
    this.installmentCount = installmentCount;
  }

  public void setPaymentDate(final Date paymentDate) {
    this.paymentDate = paymentDate;
  }

  public void setPaymentRequestId(final String paymentRequestId) {
    this.paymentRequestId = paymentRequestId;
  }

  public void setPaymentStatus(final String paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  public void setPaymentType(final String paymentType) {
    this.paymentType = paymentType;
  }

  public void setPendingReason(final String pendingReason) {
    this.pendingReason = pendingReason;
  }

  public void setPosition(final Integer position) {
    this.position = position;
  }

  public void setProtectionEligibilityType(final String protectionEligibilityType) {
    this.protectionEligibilityType = protectionEligibilityType;
  }

  public void setReasonCode(final String reasonCode) {
    this.reasonCode = reasonCode;
  }

  public void setSettleAmount(final BigDecimal settleAmount) {
    this.settleAmount = settleAmount;
  }

  public void setStoreId(final String storeId) {
    this.storeId = storeId;
  }

  public void setTaxAmount(final BigDecimal taxAmount) {
    this.taxAmount = taxAmount;
  }

  public void setTerminalId(final String terminalId) {
    this.terminalId = terminalId;
  }

  public void setTransactionId(final String transactionId) {
    this.transactionId = transactionId;
  }

  public void setTransactionType(final String transactionType) {
    this.transactionType = transactionType;
  }
}
