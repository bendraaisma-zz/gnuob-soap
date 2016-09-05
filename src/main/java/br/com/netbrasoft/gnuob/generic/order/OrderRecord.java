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
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.AMOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DELIVERY_DATE_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DESCRIPTION_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DISCOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_ORDER_RECORDS_GNUOB_OPTIONS_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_ORDER_RECORDS_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ITEM_HEIGHT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ITEM_HEIGHT_UNIT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ITEM_LENGTH_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ITEM_LENGTH_UNIT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ITEM_URL_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ITEM_WEIGHT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ITEM_WEIGHT_UNIT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ITEM_WIDTH_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ITEM_WIDTH_UNIT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.NAME_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.OPTIONS_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.OPTION_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_RECORD_ENTITY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_RECORD_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_RECORD_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.POSITION_ASC;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.POSITION_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PRODUCT_NUMBER_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.QUANTITY_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SHIPPING_COST_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.START_POSITION_VALUE;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.TAX_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ZERO;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.Integer.valueOf;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.counting;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.InheritanceType.SINGLE_TABLE;
import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.netbrasoft.gnuob.generic.AbstractType;
import br.com.netbrasoft.gnuob.generic.product.Option;
import br.com.netbrasoft.gnuob.generic.product.Product;

@Cacheable(value = false)
@Entity(name = ORDER_RECORD_ENTITY_NAME)
@Table(name = ORDER_RECORD_TABLE_NAME)
@Inheritance(strategy = SINGLE_TABLE)
@XmlRootElement(name = ORDER_RECORD_ENTITY_NAME)
public class OrderRecord extends AbstractType {

  private static final long serialVersionUID = 5394749104442554097L;

  private BigDecimal amount;
  private Date deliveryDate;
  private String description;
  private BigDecimal discount;
  private BigDecimal itemHeight;
  private String itemHeightUnit;
  private BigDecimal itemLength;
  private String itemLengthUnit;
  private String itemUrl;
  private BigDecimal itemWeight;
  private String itemWeightUnit;
  private BigDecimal itemWidth;
  private String itemWidthUnit;
  private String name;
  private String option;
  private Set<Option> options;
  private String orderRecordId;
  private Integer position;
  private Product product;
  private String productNumber;
  private BigInteger quantity;
  private BigDecimal shippingCost;
  private BigDecimal tax;

  public OrderRecord() {
    options = newHashSet();
  }

  public OrderRecord(String json) throws IOException, IllegalAccessException, InvocationTargetException {
    copyProperties(this, mapper.readValue(json, OrderRecord.class));
  }

  public static OrderRecord getInstance() {
    return new OrderRecord();
  }

  public static OrderRecord getInstanceByJson(String json)
      throws IllegalAccessException, InvocationTargetException, IOException {
    return new OrderRecord(json);
  }

  @Override
  @JsonIgnore
  @Transient
  public boolean isDetached() {
    return newArrayList(isAbstractTypeDetached(), isOptionsDetached()).stream().filter(e -> e.booleanValue())
        .count() > ZERO;
  }

  @JsonIgnore
  @Transient
  private boolean isOptionsDetached() {
    return options != null && options.stream().filter(e -> e.isDetached()).collect(counting()).intValue() > ZERO;
  }

  @Override
  public void prePersist() {
    getOrderRecordId();
    getName();
    getDescription();
    getAmount();
    getProductNumber();
    getTax();
    getShippingCost();
    getItemWeight();
    getItemWeightUnit();
    getItemLength();
    getItemLengthUnit();
    getItemWidth();
    getItemWidthUnit();
    getItemHeight();
    getItemHeightUnit();
    getItemUrl();
    reinitAllPositionOptions();
  }

  @Override
  public void preUpdate() {
    prePersist();
  }

  private void reinitAllPositionOptions() {
    int startPositionValue = START_POSITION_VALUE;
    for (final Option opt : options) {
      opt.setPosition(valueOf(startPositionValue++));
    }
  }

  @JsonProperty
  @XmlElement
  @Column(name = AMOUNT_COLUMN_NAME, nullable = false)
  public BigDecimal getAmount() {
    if (product != null && amount == null) {
      amount = product.getAmount().subtract(getDiscount());
    }
    return amount;
  }

  public void setAmount(final BigDecimal amount) {
    this.amount = amount;
  }

  @JsonProperty
  @XmlElement
  @Column(name = DELIVERY_DATE_COLUMN_NAME)
  public Date getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(final Date deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  @JsonProperty
  @XmlElement
  @Column(name = DESCRIPTION_COLUMN_NAME)
  public String getDescription() {
    if (product != null && isBlank(description)) {
      description = product.getDescription();
    }
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @JsonProperty
  @XmlElement
  @Column(name = DISCOUNT_COLUMN_NAME)
  public BigDecimal getDiscount() {
    if (product != null && discount == null) {
      discount = product.getDiscount();
    }
    return discount;
  }

  public void setDiscount(final BigDecimal discount) {
    this.discount = discount;
  }

  @JsonIgnore
  @Transient
  @XmlTransient
  public BigDecimal getDiscountTotal() {
    if (getDiscount() != null && getQuantity() != null) {
      return getDiscount().multiply(new BigDecimal(getQuantity()));
    }
    return BigDecimal.ZERO;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_HEIGHT_COLUMN_NAME)
  public BigDecimal getItemHeight() {
    if (product != null && itemHeight == null) {
      itemHeight = product.getItemHeight();
    }
    return itemHeight;
  }

  public void setItemHeight(final BigDecimal itemHeight) {
    this.itemHeight = itemHeight;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_HEIGHT_UNIT_COLUMN_NAME)
  public String getItemHeightUnit() {
    if (product != null && isBlank(itemHeightUnit)) {
      itemHeightUnit = product.getItemHeightUnit();
    }
    return itemHeightUnit;
  }

  public void setItemHeightUnit(final String itemHeightUnit) {
    this.itemHeightUnit = itemHeightUnit;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_LENGTH_COLUMN_NAME)
  public BigDecimal getItemLength() {
    if (product != null && itemLength == null) {
      itemLength = product.getItemLength();
    }
    return itemLength;
  }

  public void setItemLength(final BigDecimal itemLength) {
    this.itemLength = itemLength;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_LENGTH_UNIT_COLUMN_NAME)
  public String getItemLengthUnit() {
    if (product != null && isBlank(itemLengthUnit)) {
      itemLengthUnit = product.getItemLengthUnit();
    }
    return itemLengthUnit;
  }

  public void setItemLengthUnit(final String itemLengthUnit) {
    this.itemLengthUnit = itemLengthUnit;
  }

  @JsonIgnore
  @Transient
  @XmlTransient
  public BigDecimal getItemTotal() {
    if (getAmount() != null && getQuantity() != null) {
      return getAmount().multiply(new BigDecimal(getQuantity()));
    }
    return BigDecimal.ZERO;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_URL_COLUMN_NAME)
  public String getItemUrl() {
    if (product != null && isBlank(itemUrl)) {
      itemUrl = product.getItemUrl();
    }
    return itemUrl;
  }

  public void setItemUrl(final String itemUrl) {
    this.itemUrl = itemUrl;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_WEIGHT_COLUMN_NAME)
  public BigDecimal getItemWeight() {
    if (product != null && itemWeight == null) {
      itemWeight = product.getItemWeight();
    }
    return itemWeight;
  }

  public void setItemWeight(final BigDecimal itemWeight) {
    this.itemWeight = itemWeight;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_WEIGHT_UNIT_COLUMN_NAME)
  public String getItemWeightUnit() {
    if (product != null && isBlank(itemWeightUnit)) {
      itemWeightUnit = product.getItemWeightUnit();
    }
    return itemWeightUnit;
  }

  public void setItemWeightUnit(final String itemWeightUnit) {
    this.itemWeightUnit = itemWeightUnit;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_WIDTH_COLUMN_NAME)
  public BigDecimal getItemWidth() {
    if (product != null && itemWidth == null) {
      itemWidth = product.getItemWidth();
    }
    return itemWidth;
  }

  public void setItemWidth(final BigDecimal itemWidth) {
    this.itemWidth = itemWidth;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_WIDTH_UNIT_COLUMN_NAME)
  public String getItemWidthUnit() {
    if (product != null && isBlank(itemWidthUnit)) {
      itemWidthUnit = product.getItemWidthUnit();
    }
    return itemWidthUnit;
  }

  public void setItemWidthUnit(final String itemWidthUnit) {
    this.itemWidthUnit = itemWidthUnit;
  }

  @JsonProperty
  @XmlElement
  @Column(name = NAME_COLUMN_NAME)
  public String getName() {
    if (product != null && isBlank(name)) {
      name = product.getName();
    }
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @JsonProperty
  @XmlElement
  @Column(name = OPTION_COLUMN_NAME)
  public String getOption() {
    return option;
  }

  public void setOption(final String option) {
    this.option = option;
  }

  public void setOptions(final Set<Option> options) {
    this.options = options;
  }

  @OrderBy(POSITION_ASC)
  @OneToMany(cascade = {PERSIST, MERGE}, fetch = EAGER)
  @JoinTable(name = GNUOB_ORDER_RECORDS_GNUOB_OPTIONS_TABLE_NAME,
      joinColumns = {@JoinColumn(name = GNUOB_ORDER_RECORDS_ID_COLUMN_NAME, referencedColumnName = ID_COLUMN_NAME)},
      inverseJoinColumns = {@JoinColumn(name = OPTIONS_ID_COLUMN_NAME, referencedColumnName = ID_COLUMN_NAME)})
  public Set<Option> getOptions() {
    return options;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ORDER_RECORD_ID_COLUMN_NAME, nullable = false)
  public String getOrderRecordId() {
    if (orderRecordId == null || isBlank(orderRecordId)) {
      orderRecordId = randomUUID().toString();
    }
    return orderRecordId;
  }

  public void setOrderRecordId(final String orderRecordId) {
    this.orderRecordId = orderRecordId;
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

  @JsonIgnore
  @Transient
  public Product getProduct() {
    return product;
  }

  public void setProduct(final Product product) {
    this.product = product;
  }

  @JsonProperty
  @XmlElement
  @Column(name = PRODUCT_NUMBER_COLUMN_NAME, nullable = false)
  public String getProductNumber() {
    if (product != null && isBlank(productNumber)) {
      productNumber = product.getNumber();
    }
    return productNumber;
  }

  public void setProductNumber(final String productNumber) {
    this.productNumber = productNumber;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = QUANTITY_COLUMN_NAME, nullable = false)
  public BigInteger getQuantity() {
    return quantity;
  }

  public void setQuantity(final BigInteger quantity) {
    this.quantity = quantity;
  }

  @JsonProperty
  @XmlElement
  @Column(name = SHIPPING_COST_COLUMN_NAME)
  public BigDecimal getShippingCost() {
    if (product != null && shippingCost == null) {
      shippingCost = product.getShippingCost();
    }
    return shippingCost;
  }

  public void setShippingCost(final BigDecimal shippingCost) {
    this.shippingCost = shippingCost;
  }

  @JsonIgnore
  @Transient
  @XmlTransient
  public BigDecimal getShippingTotal() {
    if (getShippingCost() != null && getQuantity() != null) {
      return getShippingCost().multiply(new BigDecimal(getQuantity()));
    }
    return BigDecimal.ZERO;
  }

  @JsonProperty
  @XmlElement
  @Column(name = TAX_COLUMN_NAME)
  public BigDecimal getTax() {
    if (product != null && tax == null) {
      tax = product.getTax();
    }
    return tax;
  }

  public void setTax(final BigDecimal tax) {
    this.tax = tax;
  }

  @JsonIgnore
  @Transient
  @XmlTransient
  public BigDecimal getTaxTotal() {
    if (getTax() != null && getQuantity() != null) {
      return getTax().multiply(new BigDecimal(getQuantity()));
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String toString() {
    return reflectionToString(this, SHORT_PREFIX_STYLE);
  }
}
