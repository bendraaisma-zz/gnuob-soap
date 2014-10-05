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

    @Column(name = "REFERENCE", nullable = false)
    private String reference;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "HANDLING_TOTAL", nullable = false)
    private BigDecimal handlingTotal;

    @Column(name = "INSURANCE_TOTAL", nullable = false)
    private BigDecimal insuranceTotal;

    @Column(name = "SHIPPING_DISCOUNT", nullable = false)
    private BigDecimal shippingDiscount;

    @Column(name = "SHIPPING_TOTAL", nullable = false)
    private BigDecimal shippingTotal;

    public Offer() {

    }

    @XmlElement(name = "contract", required = true)
    public Contract getContract() {
        return contract;
    }

    @XmlElement(name = "description", required = true)
    public String getDescription() {
        return description;
    }

    @XmlElement(name = "handlingTotal", required = true)
    public BigDecimal getHandlingTotal() {
        return handlingTotal;
    }

    @XmlElement(name = "insuranceTotal", required = true)
    public BigDecimal getInsuranceTotal() {
        return insuranceTotal;
    }

    @XmlTransient
    @Transient
    public BigDecimal getItemTotal() {
        BigDecimal itemTotal = BigDecimal.ZERO;

        for (OfferRecord offerRecord : records) {
            itemTotal = itemTotal.add(offerRecord.getAmount().multiply(new BigDecimal(offerRecord.getQuantity())));
        }

        return itemTotal;
    }

    @XmlTransient
    @Transient
    public BigDecimal getMaxTotal() {
        return getOfferTotal().add(handlingTotal).add(insuranceTotal).add(shippingTotal).add(shippingDiscount);
    }

    @XmlTransient
    @Transient
    public BigDecimal getOfferTotal() {
        return getItemTotal().add(getTaxTotal());
    }

    public Set<OfferRecord> getRecords() {
        return records;
    }

    @XmlElement(name = "reference", required = true)
    public String getReference() {
        return reference;
    }

    @XmlElement(name = "shippingDiscount", required = true)
    public BigDecimal getShippingDiscount() {
        return shippingDiscount;
    }

    @XmlElement(name = "shippingTotal", required = true)
    public BigDecimal getShippingTotal() {
        return shippingTotal;
    }

    @XmlTransient
    @Transient
    public BigDecimal getTaxTotal() {
        BigDecimal taxTotal = BigDecimal.ZERO;

        for (OfferRecord offerRecord : records) {
            taxTotal = taxTotal.add(offerRecord.getTax()).multiply(new BigDecimal(offerRecord.getQuantity()));
        }

        return taxTotal;
    }

    @PrePersist
    public void prePersistReference() {
        if (reference == null || "".equals(reference.trim())) {
            reference = UUID.randomUUID().toString();
        }
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHandlingTotal(BigDecimal handlingTotal) {
        this.handlingTotal = handlingTotal;
    }

    public void setInsuranceTotal(BigDecimal insuranceTotal) {
        this.insuranceTotal = insuranceTotal;
    }

    public void setRecords(Set<OfferRecord> records) {
        this.records = records;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setShippingDiscount(BigDecimal shippingDiscount) {
        this.shippingDiscount = shippingDiscount;
    }

    public void setShippingTotal(BigDecimal shippingTotal) {
        this.shippingTotal = shippingTotal;
    }
}
