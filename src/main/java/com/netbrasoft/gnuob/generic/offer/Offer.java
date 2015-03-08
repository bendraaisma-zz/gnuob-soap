package com.netbrasoft.gnuob.generic.offer;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.security.Access;

@Entity(name = Offer.ENTITY)
@Table(name = Offer.TABLE)
@XmlRootElement(name = Offer.ENTITY)
public class Offer extends Access {

   private static final long serialVersionUID = -3662500407068979105L;
   protected static final String ENTITY = "Offer";
   protected static final String TABLE = "GNUOB_OFFERS";

   @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, optional = false)
   private Contract contract;

   @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true, fetch = FetchType.EAGER)
   private Set<OfferRecord> records = new HashSet<OfferRecord>();

   @Column(name = "OFFER_ID", nullable = false)
   private String offerId;

   @Column(name = "OFFER_DESCRIPTION", columnDefinition = "TEXT")
   private String offerDescription;

   @Column(name = "OFFER_TOTAL")
   private BigDecimal offerTotal;

   @Column(name = "DISCOUNT_TOTAL")
   private BigDecimal discountTotal;

   @Column(name = "ITEM_TOTAL")
   private BigDecimal itemTotal;

   @Column(name = "TAX_TOTAL")
   private BigDecimal taxTotal;

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

   }

   @XmlElement(name = "contract", required = true)
   public Contract getContract() {
      return contract;
   }

   @XmlElement(name = "discountTotal")
   public BigDecimal getDiscountTotal() {
      if (discountTotal == null) {
         discountTotal = BigDecimal.ZERO;

         for (OfferRecord offerRecord : records) {
            discountTotal = discountTotal.add(offerRecord.getDiscountTotal());
         }
      }
      return discountTotal;
   }

   @XmlElement(name = "extraAmount", required = true)
   public BigDecimal getExtraAmount() {
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

         for (OfferRecord offerRecord : records) {
            itemTotal = itemTotal.add(offerRecord.getItemTotal());
         }
      }
      return itemTotal;
   }

   @XmlTransient
   @Transient
   public BigDecimal getMaxTotal() {
      return getItemTotal().add(getTaxTotal()).add(handlingTotal).add(insuranceTotal).add(getShippingTotal())
            .add(shippingDiscount).add(extraAmount).add(getDiscountTotal());
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
         offerTotal = getItemTotal().add(getTaxTotal());
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

         for (OfferRecord offerRecord : records) {
            shippingTotal = shippingTotal.add(offerRecord.getShippingTotal());
         }
      }
      return shippingTotal;
   }

   @XmlElement(name = "taxTotal")
   public BigDecimal getTaxTotal() {
      if (taxTotal == null) {
         taxTotal = BigDecimal.ZERO;

         for (OfferRecord offerRecord : records) {
            taxTotal = taxTotal.add(offerRecord.getTaxTotal());
         }
      }
      return taxTotal;
   }

   public void offerId(String offerId) {
      this.offerId = offerId;
   }

   @PrePersist
   public void prePersistOffer() {
      if (offerId == null || "".equals(offerId.trim())) {
         offerId = UUID.randomUUID().toString();
      }

      getTaxTotal();
      getShippingTotal();
      getOfferTotal();
      getItemTotal();
   }

   public void setContract(Contract contract) {
      this.contract = contract;
   }

   public void setDiscountTotal(BigDecimal discountTotal) {
      this.discountTotal = discountTotal;
   }

   public void setExtraAmount(BigDecimal extraAmount) {
      this.extraAmount = extraAmount;
   }

   public void setHandlingTotal(BigDecimal handlingTotal) {
      this.handlingTotal = handlingTotal;
   }

   public void setInsuranceTotal(BigDecimal insuranceTotal) {
      this.insuranceTotal = insuranceTotal;
   }

   public void setItemTotal(BigDecimal itemTotal) {
      this.itemTotal = itemTotal;
   }

   public void setOfferDescription(String offerDescription) {
      this.offerDescription = offerDescription;
   }

   public void setOfferId(String offerId) {
      this.offerId = offerId;
   }

   public void setOfferTotal(BigDecimal offerTotal) {
      this.offerTotal = offerTotal;
   }

   public void setRecords(Set<OfferRecord> records) {
      this.records = records;
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
}
