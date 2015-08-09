package com.netbrasoft.gnuob.generic.order;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.security.Access;

@Cacheable(value = false)
@Entity(name = Order.ENTITY)
@Table(name = Order.TABLE)
@XmlRootElement(name = Order.ENTITY)
public class Order extends Access {

   private static final long serialVersionUID = -43318034570501796L;
   protected static final String ENTITY = "Order";
   protected static final String TABLE = "GNUOB_ORDERS";

   @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
   private Contract contract;

   @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true, fetch = FetchType.EAGER)
   @OrderBy("position asc")
   private Set<OrderRecord> records = new HashSet<OrderRecord>();

   @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true)
   private Shipment shipment;

   @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true, optional = false)
   private Invoice invoice;

   @Column(name = "ORDER_ID", nullable = false)
   private String orderId;

   @Transient
   private String notificationId;

   @Column(name = "TRANSACTION_ID")
   private String transactionId;

   @Column(name = "BILLING_AGREEMENT_ID")
   private String billingAgreementId;

   @Column(name = "ORDER_DESCRIPTION")
   private String orderDescription;

   @Column(name = "NOTE_TEXT")
   private String noteText;

   @Column(name = "TOKEN")
   private String token;

   @Column(name = "ORDER_TOTAL")
   private BigDecimal orderTotal;

   @Column(name = "DISCOUNT_TOTAL")
   private BigDecimal discountTotal;

   @Column(name = "ITEM_TOTAL")
   private BigDecimal itemTotal;

   @Column(name = "MAX_TOTAL")
   private BigDecimal maxTotal;

   @Column(name = "TAX_TOTAL")
   private BigDecimal taxTotal;

   @Column(name = "HANDLING_TOTAL", nullable = false)
   private BigDecimal handlingTotal;

   @Column(name = "INSURANCE_TOTAL", nullable = false)
   private BigDecimal insuranceTotal;

   @Column(name = "SHIPPING_DISCOUNT", nullable = false)
   private BigDecimal shippingDiscount;

   @Column(name = "SHIPPING_TOTAL")
   private BigDecimal shippingTotal;

   @Column(name = "EXTRA_AMOUNT", nullable = false)
   private BigDecimal extraAmount;

   @Column(name = "INSURANCE_OPTION_OFFERED")
   private Boolean insuranceOptionOffered;

   @Column(name = "CUSTOM")
   private String custom;

   @Column(name = "NOTE")
   private String note;

   @Column(name = "CHECKOUT_STATUS")
   private String checkoutStatus;

   @Column(name = "GIFT_MESSAGE")
   private String giftMessage;

   @Column(name = "GIFT_RECEIPT_ENABLE")
   private Boolean giftReceiptEnable;

   @Column(name = "GIFT_MESSAGE_ENABLE")
   private Boolean giftMessageEnable;

   @Column(name = "GIFT_WRAP_ENABLE")
   private Boolean giftWrapEnable;

   @Column(name = "GIFT_WRAP_NAME")
   private String giftWrapName;

   @Column(name = "GIFT_WRAP_AMOUNT")
   private BigDecimal giftWrapAmount;

   @Column(name = "ORDER_DATE")
   private Date orderDate;

   public Order() {

   }

   @XmlElement(name = "billingAgreementId")
   public String getBillingAgreementId() {
      return billingAgreementId;
   }

   @XmlElement(name = "checkoutStatus")
   public String getCheckoutStatus() {
      return checkoutStatus;
   }

   @XmlElement(name = "contract", required = true)
   public Contract getContract() {
      return contract;
   }

   @XmlElement(name = "custom")
   public String getCustom() {
      return custom;
   }

   @XmlElement(name = "discountTotal")
   public BigDecimal getDiscountTotal() {
      if (discountTotal == null) {
         discountTotal = BigDecimal.ZERO;

         for (final OrderRecord orderRecord : records) {
            discountTotal = discountTotal.add(orderRecord.getDiscountTotal());
         }
      }
      return discountTotal;
   }

   @XmlElement(name = "extraAmount", required = true)
   public BigDecimal getExtraAmount() {
      if (extraAmount == null) {
         extraAmount = getHandlingTotal().add(getTaxTotal()).add(getInsuranceTotal());
      }

      return extraAmount;
   }

   @XmlElement(name = "giftMessage")
   public String getGiftMessage() {
      return giftMessage;
   }

   @XmlElement(name = "giftMessageEnable")
   public Boolean getGiftMessageEnable() {
      return giftMessageEnable;
   }

   @XmlElement(name = "giftReceiptEnable")
   public Boolean getGiftReceiptEnable() {
      return giftReceiptEnable;
   }

   @XmlElement(name = "giftWrapAmount")
   public BigDecimal getGiftWrapAmount() {
      return giftWrapAmount;
   }

   @XmlElement(name = "giftWrapEnable")
   public Boolean getGiftWrapEnable() {
      return giftWrapEnable;
   }

   @XmlElement(name = "giftWrapName")
   public String getGiftWrapName() {
      return giftWrapName;
   }

   @XmlElement(name = "handlingTotal", required = true)
   public BigDecimal getHandlingTotal() {
      return handlingTotal;
   }

   @XmlElement(name = "insuranceOptionOffered")
   public Boolean getInsuranceOptionOffered() {
      return insuranceOptionOffered;
   }

   @XmlElement(name = "insuranceTotal", required = true)
   public BigDecimal getInsuranceTotal() {
      return insuranceTotal;
   }

   @XmlElement(name = "invoice", required = true)
   public Invoice getInvoice() {
      return invoice;
   }

   @XmlElement(name = "itemTotal")
   public BigDecimal getItemTotal() {
      if (itemTotal == null) {
         itemTotal = BigDecimal.ZERO;

         for (final OrderRecord orderRecord : records) {
            itemTotal = itemTotal.add(orderRecord.getItemTotal());
         }
      }
      return itemTotal;
   }

   @XmlElement(name = "maxTotal")
   public BigDecimal getMaxTotal() {
      if (maxTotal == null) {
         maxTotal = getOrderTotal();
      }

      return maxTotal;
   }

   @XmlElement(name = "note")
   public String getNote() {
      return note;
   }

   @XmlElement(name = "noteText")
   public String getNoteText() {
      return noteText;
   }

   @XmlElement(name = "notificationId")
   public String getNotificationId() {
      return notificationId;
   }

   @XmlElement(name = "orderDate")
   public Date getOrderDate() {
      return orderDate;
   }

   @XmlElement(name = "orderDescription")
   public String getOrderDescription() {
      return orderDescription;
   }

   @XmlElement(name = "orderId", required = true)
   public String getOrderId() {
      return orderId;
   }

   @XmlElement(name = "orderTotal")
   public BigDecimal getOrderTotal() {
      if (orderTotal == null) {
         orderTotal = getItemTotal().add(getShippingTotal()).subtract(getShippingDiscount()).add(getExtraAmount());
      }
      return orderTotal;
   }

   public Set<OrderRecord> getRecords() {
      return records;
   }

   @XmlElement(name = "shipment")
   public Shipment getShipment() {
      return shipment;
   }

   @XmlElement(name = "shippingDiscount", required = true)
   public BigDecimal getShippingDiscount() {
      return shippingDiscount;
   }

   @XmlElement(name = "shippingTotal")
   public BigDecimal getShippingTotal() {
      if (shippingTotal == null) {
         shippingTotal = BigDecimal.ZERO;

         for (final OrderRecord orderRecord : records) {
            shippingTotal = shippingTotal.add(orderRecord.getShippingTotal());
         }
      }
      return shippingTotal;
   }

   @XmlElement(name = "taxTotal")
   public BigDecimal getTaxTotal() {
      if (taxTotal == null) {
         taxTotal = BigDecimal.ZERO;

         for (final OrderRecord orderRecord : records) {
            taxTotal = taxTotal.add(orderRecord.getTaxTotal());
         }
      }
      return taxTotal;
   }

   @XmlElement(name = "token")
   public String getToken() {
      return token;
   }

   @XmlElement(name = "transactionId")
   public String getTransactionId() {
      return transactionId;
   }

   private void positionRecords() {
      int index = 0;

      for (final OrderRecord record : records) {
         record.setPosition(Integer.valueOf(index++));
      }
   }

   @Override
   public void prePersist() {
      if (orderId == null || "".equals(orderId.trim())) {
         orderId = UUID.randomUUID().toString();
      }

      if (orderDate == null) {
         orderDate = new Date();
      }

      positionRecords();
      getTaxTotal();
      getShippingTotal();
      getOrderTotal();
      getItemTotal();
      getMaxTotal();
      getDiscountTotal();
   }

   @Override
   public void preUpdate() {
      positionRecords();
   }

   public void setBillingAgreementId(String billingAgreementId) {
      this.billingAgreementId = billingAgreementId;
   }

   public void setCheckoutStatus(String checkoutStatus) {
      this.checkoutStatus = checkoutStatus;
   }

   public void setContract(Contract contract) {
      this.contract = contract;
   }

   public void setCustom(String custom) {
      this.custom = custom;
   }

   public void setDiscountTotal(BigDecimal discountTotal) {
      this.discountTotal = discountTotal;
   }

   public void setExtraAmount(BigDecimal extraAmount) {
      this.extraAmount = extraAmount;
   }

   public void setGiftMessage(String giftMessage) {
      this.giftMessage = giftMessage;
   }

   public void setGiftMessageEnable(Boolean giftMessageEnable) {
      this.giftMessageEnable = giftMessageEnable;
   }

   public void setGiftReceiptEnable(Boolean giftReceiptEnable) {
      this.giftReceiptEnable = giftReceiptEnable;
   }

   public void setGiftWrapAmount(BigDecimal giftWrapAmount) {
      this.giftWrapAmount = giftWrapAmount;
   }

   public void setGiftWrapEnable(Boolean giftWrapEnable) {
      this.giftWrapEnable = giftWrapEnable;
   }

   public void setGiftWrapName(String giftWrapName) {
      this.giftWrapName = giftWrapName;
   }

   public void setHandlingTotal(BigDecimal handlingTotal) {
      this.handlingTotal = handlingTotal;
   }

   public void setInsuranceOptionOffered(Boolean insuranceOptionOffered) {
      this.insuranceOptionOffered = insuranceOptionOffered;
   }

   public void setInsuranceTotal(BigDecimal insuranceTotal) {
      this.insuranceTotal = insuranceTotal;
   }

   public void setInvoice(Invoice invoice) {
      this.invoice = invoice;
   }

   public void setItemTotal(BigDecimal itemTotal) {
      this.itemTotal = itemTotal;
   }

   public void setMaxTotal(BigDecimal maxTotal) {
      this.maxTotal = maxTotal;
   }

   public void setNote(String note) {
      this.note = note;
   }

   public void setNoteText(String noteText) {
      this.noteText = noteText;
   }

   public void setNotificationId(String notificationId) {
      this.notificationId = notificationId;
   }

   public void setOrderDate(Date orderDate) {
      this.orderDate = orderDate;
   }

   public void setOrderDate(Timestamp orderDate) {
      this.orderDate = orderDate;
   }

   public void setOrderDescription(String orderDescription) {
      this.orderDescription = orderDescription;
   }

   public void setOrderId(String orderId) {
      this.orderId = orderId;
   }

   public void setOrderTotal(BigDecimal orderTotal) {
      this.orderTotal = orderTotal;
   }

   public void setRecords(Set<OrderRecord> records) {
      this.records = records;
   }

   public void setShipment(Shipment shipment) {
      this.shipment = shipment;
   }

   public void setShippingDiscount(BigDecimal shippingDiscount) {
      this.shippingDiscount = shippingDiscount;
   }

   public void setShippingTotal(BigDecimal shippingTotal) {
      this.shippingTotal = shippingTotal;
   }

   public void setTaxTotal(BigDecimal taxTotal) {
      this.taxTotal = taxTotal;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public void setTransactionId(String transactionId) {
      this.transactionId = transactionId;
   }
}
