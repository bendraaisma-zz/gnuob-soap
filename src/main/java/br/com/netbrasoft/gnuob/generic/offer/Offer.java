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

package br.com.netbrasoft.gnuob.generic.offer;

import static br.com.netbrasoft.gnuob.generic.JaxRsActivator.mapper;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DISCOUNT_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.EXTRA_AMOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_OFFERS_GNUOB_OFFER_RECORDS_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_OFFERS_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.HANDLING_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.INSURANCE_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ITEM_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MAX_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.OFFER_DESCRIPTION_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.OFFER_ENTITY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.OFFER_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.OFFER_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.OFFER_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.POSITION_ASC;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.RECORDS_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SHIPPING_DISCOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SHIPPING_TOTAL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.START_POSITION_VALUE;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.TAX_TOTAL_COLUMN_NAME;
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
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Entity(name = OFFER_ENTITY_NAME)
@Table(name = OFFER_TABLE_NAME)
@XmlRootElement(name = OFFER_ENTITY_NAME)
public class Offer extends AbstractAccess {

  private static final long serialVersionUID = -3662500407068979105L;

  private Contract contract;
  private BigDecimal discountTotal;
  private BigDecimal extraAmount;
  private BigDecimal handlingTotal;
  private BigDecimal insuranceTotal;
  private BigDecimal itemTotal;
  private BigDecimal maxTotal;
  private String offerDescription;
  private String offerId;
  private BigDecimal offerTotal;
  private Set<OfferRecord> records;
  private BigDecimal shippingDiscount;
  private BigDecimal shippingTotal;
  private BigDecimal taxTotal;

  public Offer() {
    records = newHashSet();
  }

  public Offer(String json) throws IOException, IllegalAccessException, InvocationTargetException {
    copyProperties(this, mapper.readValue(json, Offer.class));
  }

  public static Offer getInstance() {
    return new Offer();
  }

  public static Offer getInstanceByJson(String json) throws IllegalAccessException, InvocationTargetException, IOException {
    return new Offer(json);
  }

  @Override
  @JsonIgnore
  @Transient
  public boolean isDetached() {
    return newArrayList(isAbstractTypeDetached(), isContractDetached(), isOfferRecordsDetached()).stream()
        .filter(e -> e.booleanValue()).count() > ZERO;
  }

  @JsonIgnore
  @Transient
  private boolean isContractDetached() {
    return contract != null && contract.isDetached();
  }

  @JsonIgnore
  @Transient
  private boolean isOfferRecordsDetached() {
    return records != null && records.stream().filter(e -> e.isDetached()).collect(counting()).intValue() > ZERO;
  }

  @Override
  public void prePersist() {
    getOfferId();
    getTaxTotal();
    getShippingTotal();
    getOfferTotal();
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
    int startPositionValue = START_POSITION_VALUE;
    for (final OfferRecord record : records) {
      record.setPosition(Integer.valueOf(startPositionValue++));
    }
  }

  @Override
  public Context accept(final IContextVisitor visitor) {
    return visitor.visit(this);
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @ManyToOne(cascade = {PERSIST, MERGE, REFRESH})
  public Contract getContract() {
    return contract;
  }

  public void setContract(final Contract contract) {
    this.contract = contract;
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

  @JsonProperty(required = true)
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
      maxTotal = getOfferTotal();
    }
    return maxTotal;
  }

  public void setMaxTotal(final BigDecimal maxTotal) {
    this.maxTotal = maxTotal;
  }

  @JsonProperty
  @XmlElement
  @Column(name = OFFER_DESCRIPTION_COLUMN_NAME)
  public String getOfferDescription() {
    return offerDescription;
  }

  public void setOfferDescription(final String offerDescription) {
    this.offerDescription = offerDescription;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = OFFER_ID_COLUMN_NAME, nullable = false)
  public String getOfferId() {
    if (isBlank(offerId)) {
      offerId = randomUUID().toString();
    }
    return offerId;
  }

  public void setOfferId(final String offerId) {
    this.offerId = offerId;
  }

  @JsonProperty
  @XmlElement
  @Column(name = OFFER_TOTAL_COLUMN_NAME)
  public BigDecimal getOfferTotal() {
    if (offerTotal == null) {
      offerTotal = getItemTotal().add(getShippingTotal()).subtract(getShippingDiscount()).add(getExtraAmount());
    }
    return offerTotal;
  }

  public void setOfferTotal(final BigDecimal offerTotal) {
    this.offerTotal = offerTotal;
  }

  @OrderBy(POSITION_ASC)
  @OneToMany(cascade = {PERSIST, MERGE, REMOVE, REFRESH}, orphanRemoval = true, fetch = EAGER)
  @JoinTable(name = GNUOB_OFFERS_GNUOB_OFFER_RECORDS_TABLE_NAME,
      joinColumns = {@JoinColumn(name = GNUOB_OFFERS_ID_COLUMN_NAME, referencedColumnName = ID_COLUMN_NAME)},
      inverseJoinColumns = {@JoinColumn(name = RECORDS_ID_COLUMN_NAME, referencedColumnName = ID_COLUMN_NAME)})
  public Set<OfferRecord> getRecords() {
    return records;
  }

  public void setRecords(final Set<OfferRecord> records) {
    this.records = records;
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
  @Column(name = SHIPPING_TOTAL_COLUMN_NAME, nullable = false)
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
      records.stream().forEach(e -> taxTotal.add(e.getTaxTotal()));
    }
    return taxTotal;
  }

  public void setTaxTotal(final BigDecimal taxTotal) {
    this.taxTotal = taxTotal;
  }
}
