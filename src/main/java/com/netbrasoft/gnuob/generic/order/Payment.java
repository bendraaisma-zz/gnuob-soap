package com.netbrasoft.gnuob.generic.order;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.netbrasoft.gnuob.generic.Type;

@Entity(name = Payment.ENTITY)
@Table(name = Payment.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = Payment.ENTITY)
public class Payment extends Type {

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

    @Column(name = "FEE_AMOUNT", nullable = false)
    private BigDecimal feeAmount;

    @Column(name = "SETTLE_AMOUNT")
    private BigDecimal settleAmount;

    @Column(name = "TAX_AMOUNT", nullable = false)
    private BigDecimal taxAmount;

    @Column(name = "EXCHANGE_RATE", nullable = false)
    private String exchangeRate;

    @Column(name = "PAYMENT_STATUS", nullable = false)
    private String paymentStatus;

    @Column(name = "PENDING_REASON", nullable = false)
    private String pendingReason;

    @Column(name = "REASON_CODE", nullable = false)
    private String reasonCode;

    @Column(name = "HOLD_DECISION")
    private String holdDecision;

    @Column(name = "PROTECTION_ELIGIBILITY_TYPE", nullable = false)
    private String protectionEligibilityType;

    @Column(name = "STORE_ID")
    private String storeId;

    @Column(name = "TERMINAL_ID")
    private String terminalId;

    @Column(name = "PAYMENT_REQUEST_ID")
    private String paymentRequestId;

    @Column(name = "PAYMENT_DATE", nullable = false)
    private Date paymentDate;

    @XmlElement(name = "exchangeRate", required = true)
    public String getExchangeRate() {
        return exchangeRate;
    }

    @XmlElement(name = "feeAmount", required = true)
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

    @XmlElement(name = "paymentDate", required = true)
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
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

    @XmlElement(name = "pendingReason", required = true)
    public String getPendingReason() {
        return pendingReason;
    }

    @XmlElement(name = "protectionEligibilityType", required = true)
    public String getProtectionEligibilityType() {
        return protectionEligibilityType;
    }

    @XmlElement(name = "reasonCode", required = true)
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

    @XmlElement(name = "taxAmount", required = true)
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

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public void setHoldDecision(String holdDecision) {
        this.holdDecision = holdDecision;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaymentRequestId(String paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setPendingReason(String pendingReason) {
        this.pendingReason = pendingReason;
    }

    public void setProtectionEligibilityType(String protectionEligibilityType) {
        this.protectionEligibilityType = protectionEligibilityType;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public void setSettleAmount(BigDecimal settleAmount) {
        this.settleAmount = settleAmount;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
