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

package br.com.netbrasoft.gnuob.generic.order;

import static br.com.netbrasoft.gnuob.generic.JaxRsActivator.mapper;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.BILLING_AGREEMENT_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CHECKOUT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CHECKOUT_STATUS_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CUSTOM_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DISCOUNT_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.EXTRA_AMOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GIFT_MESSAGE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GIFT_MESSAGE_ENABLE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GIFT_RECEIPT_ENABLE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GIFT_WRAP_AMOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GIFT_WRAP_ENABLE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GIFT_WRAP_NAME_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_ORDERS_GNUOB_ORDER_RECORDS_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_ORDERS_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.HANDLING_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.INSURANCE_OPTION_OFFERED_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.INSURANCE_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ITEM_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MAX_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.NOTE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.NOTE_TEXT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_DATE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_DESCRIPTION_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_ENTITY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PENDING;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.POSITION_ASC;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.RECORDS_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SHIPPING_DISCOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SHIPPING_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.START_POSITION_VALUE;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.TAX_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.TOKEN_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.TRANSACTION_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ZERO;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.counting;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;
import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.velocity.context.Context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.netbrasoft.gnuob.generic.content.contexts.IContextVisitor;
import br.com.netbrasoft.gnuob.generic.contract.Contract;
import br.com.netbrasoft.gnuob.generic.security.AbstractAccess;

@Cacheable(value = false)
@Entity(name = ORDER_ENTITY_NAME)
@Table(name = ORDER_TABLE_NAME)
@XmlRootElement(name = ORDER_ENTITY_NAME)
public class Order extends AbstractAccess {

  private static final long serialVersionUID = -43318034570501796L;

  private String billingAgreementId;
  private String checkout;
  private String checkoutStatus;
  private Contract contract;
  private String custom;
  private BigDecimal discountTotal;
  private BigDecimal extraAmount;
  private String giftMessage;
  private Boolean giftMessageEnable;
  private Boolean giftReceiptEnable;
  private BigDecimal giftWrapAmount;
  private Boolean giftWrapEnable;
  private String giftWrapName;
  private BigDecimal handlingTotal;
  private Boolean insuranceOptionOffered;
  private BigDecimal insuranceTotal;
  private Invoice invoice;
  private BigDecimal itemTotal;
  private BigDecimal maxTotal;
  private String note;
  private String noteText;
  private String notificationId;
  private Date orderDate;
  private String orderDescription;
  private String orderId;
  private BigDecimal orderTotal;
  private Set<OrderRecord> records;
  private Shipment shipment;
  private BigDecimal shippingDiscount;
  private BigDecimal shippingTotal;
  private BigDecimal taxTotal;
  private String token;
  private String transactionId;

  public Order() {
    records = newHashSet();
  }

  public Order(String json) throws IOException, IllegalAccessException, InvocationTargetException {
    copyProperties(this, mapper.readValue(json, Order.class));
  }

  public static Order getInstance() {
    return new Order();
  }

  public static Order getInstanceByJson(String json)
      throws IllegalAccessException, InvocationTargetException, IOException {
    return new Order(json);
  }

  @Override
  @JsonIgnore
  @Transient
  public boolean isDetached() {
    return newArrayList(isAbstractTypeDetached(), isContractDetached(), isInvoiceDetached(), isShipmentDetachted(),
        isOrderRecordsDetached()).stream().filter(e -> e.booleanValue()).count() > ZERO;
  }

  @JsonIgnore
  @Transient
  private boolean isContractDetached() {
    return contract != null && contract.isDetached();
  }

  @Transient
  private boolean isOrderRecordsDetached() {
    return records != null && records.stream().filter(e -> e.isDetached()).collect(counting()).intValue() > ZERO;
  }

  @JsonIgnore
  @Transient
  private boolean isInvoiceDetached() {
    return invoice != null && invoice.isDetached();
  }

  @JsonIgnore
  @Transient
  private boolean isShipmentDetachted() {
    return shipment != null && shipment.isDetached();
  }

  @Override
  public void prePersist() {
    getOrderId();
    getCustom();
    getCheckoutStatus();
    getTaxTotal();
    getShippingTotal();
    getOrderTotal();
    getItemTotal();
    getMaxTotal();
    getDiscountTotal();
    reinitAllPositionRecords();
  }

  @Override
  public void preUpdate() {
    reinitAllPositionRecords();
  }

  private void reinitAllPositionRecords() {
    int startPosition = START_POSITION_VALUE;
    for (final OrderRecord record : records) {
      record.setPosition(Integer.valueOf(startPosition++));
    }
  }

  @Override
  public Context accept(final IContextVisitor visitor) {
    return visitor.visit(this);
  }

  @JsonProperty
  @XmlElement
  @Column(name = BILLING_AGREEMENT_ID_COLUMN_NAME)
  public String getBillingAgreementId() {
    return billingAgreementId;
  }

  public void setBillingAgreementId(final String billingAgreementId) {
    this.billingAgreementId = billingAgreementId;
  }

  @JsonProperty
  @XmlElement
  @Column(name = CHECKOUT_COLUMN_NAME)
  public String getCheckout() {
    return checkout;
  }

  public void setCheckout(final String checkout) {
    this.checkout = checkout;
  }

  @JsonProperty
  @XmlElement
  @Column(name = CHECKOUT_STATUS_COLUMN_NAME)
  public String getCheckoutStatus() {
    if (isBlank(checkoutStatus)) {
      checkoutStatus = PENDING;
    }
    return checkoutStatus;
  }

  public void setCheckoutStatus(final String checkoutStatus) {
    this.checkoutStatus = checkoutStatus;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @OneToOne(cascade = {PERSIST, MERGE, REFRESH})
  public Contract getContract() {
    return contract;
  }

  public void setContract(final Contract contract) {
    this.contract = contract;
  }

  @JsonProperty
  @XmlElement
  @Column(name = CUSTOM_COLUMN_NAME)
  public String getCustom() {
    if (isBlank(custom)) {
      custom = randomUUID().toString();
    }
    return custom;
  }

  public void setCustom(final String custom) {
    this.custom = custom;
  }

  @JsonProperty
  @XmlElement
  @Column(name = DISCOUNT_TOTAL_COLUMN_NAME)
  public BigDecimal getDiscountTotal() {
    if (discountTotal == null) {
      discountTotal = BigDecimal.ZERO;
      records.stream().forEach(e -> discountTotal.add(e.getDiscountTotal()));
    }
    return discountTotal;
  }

  public void setDiscountTotal(final BigDecimal discountTotal) {
    this.discountTotal = discountTotal;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = EXTRA_AMOUNT_COLUMN_NAME, nullable = false)
  public BigDecimal getExtraAmount() {
    if (extraAmount == null) {
      extraAmount = getHandlingTotal().add(getTaxTotal()).add(getInsuranceTotal());
    }
    return extraAmount;
  }

  public void setExtraAmount(final BigDecimal extraAmount) {
    this.extraAmount = extraAmount;
  }

  @JsonProperty
  @XmlElement
  @Column(name = GIFT_MESSAGE_COLUMN_NAME)
  public String getGiftMessage() {
    return giftMessage;
  }

  public void setGiftMessage(final String giftMessage) {
    this.giftMessage = giftMessage;
  }

  @JsonProperty
  @XmlElement
  @Column(name = GIFT_MESSAGE_ENABLE_COLUMN_NAME)
  public Boolean getGiftMessageEnable() {
    return giftMessageEnable;
  }

  public void setGiftMessageEnable(final Boolean giftMessageEnable) {
    this.giftMessageEnable = giftMessageEnable;
  }

  @JsonProperty
  @XmlElement
  @Column(name = GIFT_RECEIPT_ENABLE_COLUMN_NAME)
  public Boolean getGiftReceiptEnable() {
    return giftReceiptEnable;
  }

  public void setGiftReceiptEnable(final Boolean giftReceiptEnable) {
    this.giftReceiptEnable = giftReceiptEnable;
  }

  @JsonProperty
  @XmlElement
  @Column(name = GIFT_WRAP_AMOUNT_COLUMN_NAME)
  public BigDecimal getGiftWrapAmount() {
    return giftWrapAmount;
  }

  public void setGiftWrapAmount(final BigDecimal giftWrapAmount) {
    this.giftWrapAmount = giftWrapAmount;
  }

  @JsonProperty
  @XmlElement
  @Column(name = GIFT_WRAP_ENABLE_COLUMN_NAME)
  public Boolean getGiftWrapEnable() {
    return giftWrapEnable;
  }

  public void setGiftWrapEnable(final Boolean giftWrapEnable) {
    this.giftWrapEnable = giftWrapEnable;
  }

  @JsonProperty
  @XmlElement
  @Column(name = GIFT_WRAP_NAME_COLUMN_NAME)
  public String getGiftWrapName() {
    return giftWrapName;
  }

  public void setGiftWrapName(final String giftWrapName) {
    this.giftWrapName = giftWrapName;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = HANDLING_TOTAL_COLUMN_NAME, nullable = false)
  public BigDecimal getHandlingTotal() {
    if (handlingTotal == null) {
      handlingTotal = BigDecimal.ZERO;
    }
    return handlingTotal;
  }

  public void setHandlingTotal(final BigDecimal handlingTotal) {
    this.handlingTotal = handlingTotal;
  }

  @JsonProperty
  @XmlElement
  @Column(name = INSURANCE_OPTION_OFFERED_COLUMN_NAME)
  public Boolean getInsuranceOptionOffered() {
    return insuranceOptionOffered;
  }

  public void setInsuranceOptionOffered(final Boolean insuranceOptionOffered) {
    this.insuranceOptionOffered = insuranceOptionOffered;
  }

  @JsonProperty
  @XmlElement(required = true)
  @Column(name = INSURANCE_TOTAL_COLUMN_NAME, nullable = false)
  public BigDecimal getInsuranceTotal() {
    if (insuranceTotal == null) {
      insuranceTotal = BigDecimal.ZERO;
    }
    return insuranceTotal;
  }

  public void setInsuranceTotal(final BigDecimal insuranceTotal) {
    this.insuranceTotal = insuranceTotal;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @OneToOne(cascade = {PERSIST, MERGE, REMOVE, REFRESH}, orphanRemoval = true, optional = false)
  public Invoice getInvoice() {
    return invoice;
  }

  public void setInvoice(final Invoice invoice) {
    this.invoice = invoice;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_TOTAL_COLUMN_NAME)
  public BigDecimal getItemTotal() {
    if (itemTotal == null) {
      itemTotal = BigDecimal.ZERO;
      records.stream().forEach(e -> itemTotal.add(e.getItemTotal()));
    }
    return itemTotal;
  }

  public void setItemTotal(final BigDecimal itemTotal) {
    this.itemTotal = itemTotal;
  }

  @JsonProperty
  @XmlElement
  @Column(name = MAX_TOTAL_COLUMN_NAME)
  public BigDecimal getMaxTotal() {
    if (maxTotal == null) {
      maxTotal = getOrderTotal();
    }
    return maxTotal;
  }

  public void setMaxTotal(final BigDecimal maxTotal) {
    this.maxTotal = maxTotal;
  }

  @JsonProperty
  @XmlElement
  @Column(name = NOTE_COLUMN_NAME)
  public String getNote() {
    return note;
  }

  public void setNote(final String note) {
    this.note = note;
  }

  @JsonProperty
  @XmlElement
  @Column(name = NOTE_TEXT_COLUMN_NAME)
  public String getNoteText() {
    return noteText;
  }

  public void setNoteText(final String noteText) {
    this.noteText = noteText;
  }

  @JsonProperty
  @XmlElement
  @Transient
  public String getNotificationId() {
    return notificationId;
  }

  public void setNotificationId(final String notificationId) {
    this.notificationId = notificationId;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ORDER_DATE_COLUMN_NAME)
  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(final Date orderDate) {
    this.orderDate = orderDate;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ORDER_DESCRIPTION_COLUMN_NAME)
  public String getOrderDescription() {
    return orderDescription;
  }

  public void setOrderDescription(final String orderDescription) {
    this.orderDescription = orderDescription;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = ORDER_ID_COLUMN_NAME, nullable = false)
  public String getOrderId() {
    if (isBlank(orderId)) {
      orderId = randomUUID().toString();
    }
    return orderId;
  }

  public void setOrderId(final String orderId) {
    this.orderId = orderId;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ORDER_TOTAL_COLUMN_NAME)
  public BigDecimal getOrderTotal() {
    if (orderTotal == null) {
      orderTotal = getItemTotal().add(getShippingTotal()).subtract(getShippingDiscount()).add(getExtraAmount());
    }
    return orderTotal;
  }

  public void setOrderTotal(final BigDecimal orderTotal) {
    this.orderTotal = orderTotal;
  }

  @OrderBy(POSITION_ASC)
  @OneToMany(cascade = {PERSIST, MERGE, REMOVE, REFRESH}, orphanRemoval = true, fetch = EAGER)
  @JoinTable(name = GNUOB_ORDERS_GNUOB_ORDER_RECORDS_TABLE_NAME,
      joinColumns = {@JoinColumn(name = GNUOB_ORDERS_ID_COLUMN_NAME, referencedColumnName = ID_COLUMN_NAME)},
      inverseJoinColumns = {@JoinColumn(name = RECORDS_ID_COLUMN_NAME, referencedColumnName = ID_COLUMN_NAME)})
  public Set<OrderRecord> getRecords() {
    return records;
  }

  public void setRecords(final Set<OrderRecord> records) {
    this.records = records;
  }

  @JsonProperty
  @XmlElement
  @OneToOne(cascade = {PERSIST, MERGE, REMOVE, REFRESH}, orphanRemoval = true)
  public Shipment getShipment() {
    return shipment;
  }

  public void setShipment(final Shipment shipment) {
    this.shipment = shipment;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = SHIPPING_DISCOUNT_COLUMN_NAME, nullable = false)
  public BigDecimal getShippingDiscount() {
    if (shippingDiscount == null) {
      shippingDiscount = BigDecimal.ZERO;
    }
    return shippingDiscount;
  }

  public void setShippingDiscount(final BigDecimal shippingDiscount) {
    this.shippingDiscount = shippingDiscount;
  }

  @JsonProperty
  @XmlElement
  @Column(name = SHIPPING_TOTAL_COLUMN_NAME)
  public BigDecimal getShippingTotal() {
    if (shippingTotal == null) {
      shippingTotal = BigDecimal.ZERO;
      records.stream().forEach(e -> shippingTotal.add(e.getShippingTotal()));
    }
    return shippingTotal;
  }

  public void setShippingTotal(final BigDecimal shippingTotal) {
    this.shippingTotal = shippingTotal;
  }

  @JsonProperty
  @XmlElement
  @Column(name = TAX_TOTAL_COLUMN_NAME)
  public BigDecimal getTaxTotal() {
    if (taxTotal == null) {
      taxTotal = BigDecimal.ZERO;
      records.forEach(e -> taxTotal.add(e.getTaxTotal()));
    }
    return taxTotal;
  }

  public void setTaxTotal(final BigDecimal taxTotal) {
    this.taxTotal = taxTotal;
  }

  @XmlElement
  @Column(name = TOKEN_COLUMN_NAME)
  public String getToken() {
    return token;
  }

  public void setToken(final String token) {
    this.token = token;
  }

  @JsonProperty
  @XmlElement
  @Column(name = TRANSACTION_ID_COLUMN_NAME)
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(final String transactionId) {
    this.transactionId = transactionId;
  }
}
