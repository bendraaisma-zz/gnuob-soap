package com.netbrasoft.gnuob.generic.order;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.security.Access;

@Entity(name = Order.ENTITY)
@Table(name = Order.TABLE)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = Order.ENTITY)
public class Order extends Access {

	private static final long serialVersionUID = -43318034570501796L;
	protected static final String ENTITY = "Order";
	protected static final String TABLE = "GNUOB_ORDERS";

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, optional = false)
	@XmlElement(name = "contract", required = true)
	private Contract contract;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<OrderRecord> records = new HashSet<OrderRecord>();

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true)
	@XmlElement(name = "shipment", required = true)
	private Shipment shipment;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true)
	@XmlElement(name = "invoice", required = true)
	private Invoice invoice;

	@Column(name = "ORDER_ID", nullable = false)
	@XmlElement(name = "orderId", required = true)
	private String orderId;

	@Column(name = "TRANSACTION_ID")
	@XmlElement(name = "transactionId")
	private String transactionId;

	@Column(name = "BILLING_AGREEMENT_ID")
	@XmlElement(name = "billingAgreementID")
	private String billingAgreementID;

	@Column(name = "ORDER_DESCRIPTION")
	@XmlElement(name = "orderDescription")
	private String orderDescription;

	@Column(name = "NOTE_TEXT")
	@XmlElement(name = "noteText")
	private String noteText;

	@Column(name = "TOKEN")
	@XmlElement(name = "token")
	private String token;

	@Column(name = "ORDER_TOTAL")
	@XmlElement(name = "orderTotal")
	private BigDecimal orderTotal;

	@Column(name = "ITEM_TOTAL")
	@XmlElement(name = "itemTotal")
	private BigDecimal itemTotal;

	@Column(name = "TAX_TOTAL")
	@XmlElement(name = "taxTotal")
	private BigDecimal taxTotal;

	@Column(name = "HANDLING_TOTAL", nullable = false)
	@XmlElement(name = "handlingTotal", required = true)
	private BigDecimal handlingTotal = BigDecimal.ZERO;

	@Column(name = "INSURANCE_TOTAL", nullable = false)
	@XmlElement(name = "insuranceTotal", required = true)
	private BigDecimal insuranceTotal = BigDecimal.ZERO;

	@Column(name = "SHIPPING_DISCOUNT", nullable = false)
	@XmlElement(name = "shippingDiscount", required = true)
	private BigDecimal shippingDiscount = BigDecimal.ZERO;

	@Column(name = "SHIPPING_TOTAL", nullable = false)
	@XmlElement(name = "shippingTotal", required = true)
	private BigDecimal shippingTotal = BigDecimal.ZERO;

	@Column(name = "INSURANCE_OPTION_OFFERED")
	@XmlElement(name = "insuranceOptionOffered")
	private Boolean insuranceOptionOffered;

	@Column(name = "CUSTOM")
	@XmlElement(name = "custom")
	private String custom;

	@Column(name = "NOTE")
	@XmlElement(name = "note")
	private String note;

	@Column(name = "CHECKOUT_STATUS")
	@XmlElement(name = "checkoutStatus")
	private String checkoutStatus;

	@Column(name = "GIFT_MESSAGE")
	@XmlElement(name = "giftMessage")
	private String giftMessage;

	@Column(name = "GIFT_RECEIPT_ENABLE")
	@XmlElement(name = "giftReceiptEnable")
	private Boolean giftReceiptEnable;

	@Column(name = "GIFT_MESSAGE_ENABLE")
	@XmlElement(name = "giftMessageEnable")
	private Boolean giftMessageEnable;

	@Column(name = "GIFT_WRAP_ENABLE")
	@XmlElement(name = "giftWrapEnable")
	private Boolean giftWrapEnable;

	@Column(name = "GIFT_WRAP_NAME")
	@XmlElement(name = "giftWrapName")
	private String giftWrapName;

	@Column(name = "GIFT_WRAP_AMOUNT")
	@XmlElement(name = "giftWrapAmount")
	private BigDecimal giftWrapAmount;

	public Order() {

	}

	public String getBillingAgreementID() {
		return billingAgreementID;
	}

	public String getCheckoutStatus() {
		return checkoutStatus;
	}

	public Contract getContract() {
		return contract;
	}

	public String getCustom() {
		return custom;
	}

	public String getGiftMessage() {
		return giftMessage;
	}

	public Boolean getGiftMessageEnable() {
		return giftMessageEnable;
	}

	public Boolean getGiftReceiptEnable() {
		return giftReceiptEnable;
	}

	public BigDecimal getGiftWrapAmount() {
		return giftWrapAmount;
	}

	public Boolean getGiftWrapEnable() {
		return giftWrapEnable;
	}

	public String getGiftWrapName() {
		return giftWrapName;
	}

	public BigDecimal getHandlingTotal() {
		return handlingTotal;
	}

	public Boolean getInsuranceOptionOffered() {
		return insuranceOptionOffered;
	}

	public BigDecimal getInsuranceTotal() {
		return insuranceTotal;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public BigDecimal getItemTotal() {

		if (itemTotal == null) {
			itemTotal = BigDecimal.ZERO;

			for (OrderRecord orderRecord : records) {
				itemTotal = itemTotal.add(orderRecord.getAmount().multiply(new BigDecimal(orderRecord.getQuantity())));
			}
		}

		return itemTotal;
	}

	@XmlTransient
	@Transient
	public BigDecimal getMaxTotal() {
		return getItemTotal().add(getTaxTotal()).add(handlingTotal).add(insuranceTotal).add(shippingTotal).add(shippingDiscount);
	}

	public String getNote() {
		return note;
	}

	public String getNoteText() {
		return noteText;
	}

	public String getOrderDescription() {
		return orderDescription;
	}

	public String getOrderId() {
		return orderId;
	}

	public BigDecimal getOrderTotal() {
		if (orderTotal == null) {
			orderTotal = getItemTotal().add(getTaxTotal());
		}

		return orderTotal;
	}

	public Set<OrderRecord> getRecords() {
		return records;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public BigDecimal getShippingDiscount() {
		return shippingDiscount;
	}

	public BigDecimal getShippingTotal() {
		return shippingTotal;
	}

	public BigDecimal getTaxTotal() {
		if (taxTotal == null) {

			taxTotal = BigDecimal.ZERO;

			for (OrderRecord orderRecord : records) {
				taxTotal = taxTotal.add(orderRecord.getTax()).multiply(new BigDecimal(orderRecord.getQuantity()));
			}
		}

		return taxTotal;
	}

	public String getToken() {
		return token;
	}

	public String getTransactionId() {
		return transactionId;
	}

	@PrePersist
	public void prePersistOrderId() {
		if (orderId == null || orderId.trim().equals("")) {
			orderId = UUID.randomUUID().toString();
		}
	}

	public void setBillingAgreementID(String billingAgreementID) {
		this.billingAgreementID = billingAgreementID;
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

	public void setNote(String note) {
		this.note = note;
	}

	public void setNoteText(String noteText) {
		this.noteText = noteText;
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
