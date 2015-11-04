package com.netbrasoft.gnuob.generic.offer;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.velocity.context.Context;

import com.netbrasoft.gnuob.generic.content.contexts.ContextVisitor;
import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.security.AbstractAccess;

@Cacheable(value = false)
@Entity(name = Offer.ENTITY)
@Table(name = Offer.TABLE)
@XmlRootElement(name = Offer.ENTITY)
public class Offer extends AbstractAccess {

  private static final long serialVersionUID = -3662500407068979105L;
  protected static final String ENTITY = "Offer";
  protected static final String TABLE = "GNUOB_OFFERS";

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
  private Contract contract;

  @OrderBy("position asc")
  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, orphanRemoval = true, fetch = FetchType.EAGER)
  @JoinTable(name = "gnuob_offers_gnuob_offer_records", joinColumns = {@JoinColumn(name = "GNUOB_OFFERS_ID", referencedColumnName = "ID")},
      inverseJoinColumns = {@JoinColumn(name = "records_ID", referencedColumnName = "ID")})
  private Set<OfferRecord> records = new HashSet<OfferRecord>();

  @Column(name = "OFFER_ID", nullable = false)
  private String offerId;

  @Column(name = "OFFER_DESCRIPTION")
  private String offerDescription;

  @Column(name = "OFFER_TOTAL")
  private BigDecimal offerTotal;

  @Column(name = "DISCOUNT_TOTAL")
  private BigDecimal discountTotal;

  @Column(name = "ITEM_TOTAL")
  private BigDecimal itemTotal;

  @Column(name = "TAX_TOTAL")
  private BigDecimal taxTotal;

  @Column(name = "MAX_TOTAL")
  private BigDecimal maxTotal;

  @Column(name = "HANDLING_TOTAL", nullable = false)
  private BigDecimal handlingTotal;

  @Column(name = "INSURANCE_TOTAL", nullable = false)
  private BigDecimal insuranceTotal;

  @Column(name = "SHIPPING_DISCOUNT", nullable = false)
  private BigDecimal shippingDiscount;

  @Column(name = "SHIPPING_TOTAL", nullable = false)
  private BigDecimal shippingTotal;

  @Column(name = "EXTRA_AMOUNT", nullable = false)
  private BigDecimal extraAmount;

  public Offer() {
    // Empty constructor.
  }

  @Override
  public Context accept(final ContextVisitor visitor) {
    return visitor.visit(this);
  }

  @XmlElement(name = "contract", required = true)
  public Contract getContract() {
    return contract;
  }

  @XmlElement(name = "discountTotal")
  public BigDecimal getDiscountTotal() {
    if (discountTotal == null) {
      discountTotal = BigDecimal.ZERO;

      for (final OfferRecord offerRecord : records) {
        discountTotal = discountTotal.add(offerRecord.getDiscountTotal());
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

  @XmlElement(name = "handlingTotal", required = true)
  public BigDecimal getHandlingTotal() {
    return handlingTotal;
  }

  @XmlElement(name = "insuranceTotal", required = true)
  public BigDecimal getInsuranceTotal() {
    return insuranceTotal;
  }

  @XmlElement(name = "itemTotal")
  public BigDecimal getItemTotal() {
    if (itemTotal == null) {
      itemTotal = BigDecimal.ZERO;

      for (final OfferRecord offerRecord : records) {
        itemTotal = itemTotal.add(offerRecord.getItemTotal());
      }
    }
    return itemTotal;
  }

  @XmlElement(name = "maxTotal")
  public BigDecimal getMaxTotal() {
    if (maxTotal == null) {
      maxTotal = getOfferTotal();
    }

    return maxTotal;
  }

  @XmlElement(name = "offerDescription")
  public String getOfferDescription() {
    return offerDescription;
  }

  @XmlElement(name = "offerId", required = true)
  public String getOfferId() {
    return offerId;
  }

  @XmlElement(name = "offerTotal")
  public BigDecimal getOfferTotal() {
    if (offerTotal == null) {
      offerTotal = getItemTotal().add(getShippingTotal()).subtract(getShippingDiscount()).add(getExtraAmount());
    }

    return offerTotal;
  }

  public Set<OfferRecord> getRecords() {
    return records;
  }

  @XmlElement(name = "shippingDiscount", required = true)
  public BigDecimal getShippingDiscount() {
    return shippingDiscount;
  }

  @XmlElement(name = "shippingTotal")
  public BigDecimal getShippingTotal() {
    if (shippingTotal == null) {
      shippingTotal = BigDecimal.ZERO;

      for (final OfferRecord offerRecord : records) {
        shippingTotal = shippingTotal.add(offerRecord.getShippingTotal());
      }
    }
    return shippingTotal;
  }

  @XmlElement(name = "taxTotal")
  public BigDecimal getTaxTotal() {
    if (taxTotal == null) {
      taxTotal = BigDecimal.ZERO;

      for (final OfferRecord offerRecord : records) {
        taxTotal = taxTotal.add(offerRecord.getTaxTotal());
      }
    }
    return taxTotal;
  }

  @Override
  public boolean isDetached() {
    if (contract != null && contract.isDetached()) {
      return contract.isDetached();
    }
    for (final OfferRecord offerRecord : records) {
      if (offerRecord.isDetached()) {
        return offerRecord.isDetached();
      }
    }
    return getId() > 0;
  }

  public void offerId(final String offerId) {
    this.offerId = offerId;
  }

  private void positionRecords() {
    int index = 0;

    for (final OfferRecord record : records) {
      record.setPosition(Integer.valueOf(index++));
    }
  }

  @Override
  public void prePersist() {
    if (offerId == null || "".equals(offerId.trim())) {
      offerId = UUID.randomUUID().toString();
    }

    positionRecords();

    getTaxTotal();
    getShippingTotal();
    getOfferTotal();
    getItemTotal();
    getMaxTotal();
    getDiscountTotal();
  }

  @Override
  public void preUpdate() {
    positionRecords();
  }

  public void setContract(final Contract contract) {
    this.contract = contract;
  }

  public void setDiscountTotal(final BigDecimal discountTotal) {
    this.discountTotal = discountTotal;
  }

  public void setExtraAmount(final BigDecimal extraAmount) {
    this.extraAmount = extraAmount;
  }

  public void setHandlingTotal(final BigDecimal handlingTotal) {
    this.handlingTotal = handlingTotal;
  }

  public void setInsuranceTotal(final BigDecimal insuranceTotal) {
    this.insuranceTotal = insuranceTotal;
  }

  public void setItemTotal(final BigDecimal itemTotal) {
    this.itemTotal = itemTotal;
  }

  public void setMaxTotal(final BigDecimal maxTotal) {
    this.maxTotal = maxTotal;
  }

  public void setOfferDescription(final String offerDescription) {
    this.offerDescription = offerDescription;
  }

  public void setOfferId(final String offerId) {
    this.offerId = offerId;
  }

  public void setOfferTotal(final BigDecimal offerTotal) {
    this.offerTotal = offerTotal;
  }

  public void setRecords(final Set<OfferRecord> records) {
    this.records = records;
  }

  public void setShippingDiscount(final BigDecimal shippingDiscount) {
    this.shippingDiscount = shippingDiscount;
  }

  public void setShippingTotal(final BigDecimal shippingTotal) {
    this.shippingTotal = shippingTotal;
  }

  public void setTaxTotal(final BigDecimal taxTotal) {
    this.taxTotal = taxTotal;
  }
}
