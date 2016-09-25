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

package br.com.netbrasoft.gnuob.generic.product;

import static br.com.netbrasoft.gnuob.generic.JaxRsActivator.mapper;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.AMOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.BESTSELLERS_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CONTENTS_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DESCRIPTION_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DISCOUNT_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_PRODUCTS_GNUOB_CONTENTS_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_PRODUCTS_GNUOB_OPTIONS_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_PRODUCTS_GNUOB_SUB_CATEGORIES_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_PRODUCTS_ID_COLUMN_NAME;
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
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.LATEST_COLLECTION_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.NAME_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.NUMBER_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.OPTIONS_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.POSITION_ASC;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PRODUCT_ENTITY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PRODUCT_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.RATING_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.RECOMMENDED_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SHIPPING_COST_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.START_POSITION_VALUE;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SUB_CATEGORIES_ID_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.TAX_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ZERO;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.Integer.valueOf;
import static java.util.stream.Collectors.counting;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;
import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.velocity.context.Context;
import org.hibernate.annotations.Cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.netbrasoft.gnuob.generic.category.SubCategory;
import br.com.netbrasoft.gnuob.generic.content.Content;
import br.com.netbrasoft.gnuob.generic.content.contexts.IContextVisitor;
import br.com.netbrasoft.gnuob.generic.security.AbstractAccess;

@Cacheable(value = true)
@Entity(name = PRODUCT_ENTITY_NAME)
@Table(name = PRODUCT_TABLE_NAME)
@XmlRootElement(name = PRODUCT_ENTITY_NAME)
public class Product extends AbstractAccess {

  private static final long serialVersionUID = -5818453495081202563L;

  private BigDecimal amount;
  private Boolean bestsellers;
  private Set<Content> contents;
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
  private Boolean latestCollection;
  private String name;
  private String number;
  private Set<Option> options;
  private Integer rating;
  private Boolean recommended;
  private BigDecimal shippingCost;
  private Stock stock;
  private Set<SubCategory> subCategories;
  private BigDecimal tax;

  public Product() {
    contents = newHashSet();
    options = newHashSet();
    subCategories = newHashSet();
  }

  public Product(String json) throws IOException, IllegalAccessException, InvocationTargetException {
    copyProperties(this, mapper.readValue(json, Product.class));
  }

  public static Product getInstance() {
    return new Product();
  }

  public static Product getInstanceByJson(String json)
      throws IllegalAccessException, InvocationTargetException, IOException {
    return new Product(json);
  }

  @Override
  @JsonIgnore
  @Transient
  public boolean isDetached() {
    return newArrayList(isAbstractTypeDetached(), isStockDetached(), isSubCategoriesDetached(), isContentsDetached(),
        isOptionsDetached()).stream().filter(e -> e.booleanValue()).count() > ZERO;
  }

  @JsonIgnore
  @Transient
  private boolean isStockDetached() {
    return stock != null && stock.isDetached();
  }

  @JsonIgnore
  @Transient
  private boolean isSubCategoriesDetached() {
    return subCategories != null
        && subCategories.stream().filter(e -> e.isDetached()).collect(counting()).intValue() > ZERO;
  }

  @JsonIgnore
  @Transient
  private boolean isContentsDetached() {
    return contents != null && contents.stream().filter(e -> e.isDetached()).collect(counting()).intValue() > ZERO;
  }

  @JsonIgnore
  @Transient
  private boolean isOptionsDetached() {
    return options != null && options.stream().filter(e -> e.isDetached()).collect(counting()).intValue() > ZERO;
  }

  @Override
  public void prePersist() {
    reinitAllPositionContents();
    reinitAllPositionOptions();
    reinitAllPositionSubCategories();
  }

  @Override
  public void preUpdate() {
    prePersist();
  }

  private void reinitAllPositionContents() {
    int startPosition = START_POSITION_VALUE;
    for (final Content content : contents) {
      content.setPosition(valueOf(startPosition++));
    }
  }

  private void reinitAllPositionOptions() {
    int startPosition = START_POSITION_VALUE;
    for (final Option option : options) {
      option.setPosition(valueOf(startPosition++));
    }
  }

  private void reinitAllPositionSubCategories() {
    int startPosition = START_POSITION_VALUE;
    for (final SubCategory subCategory : subCategories) {
      subCategory.setPosition(valueOf(startPosition++));
    }
  }

  @Override
  public Context accept(final IContextVisitor visitor) {
    return visitor.visit(this);
  }

  @XmlElement(required = true)
  @Column(name = AMOUNT_COLUMN_NAME, nullable = false)
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(final BigDecimal amount) {
    this.amount = amount;
  }

  @JsonProperty
  @XmlElement
  @Column(name = BESTSELLERS_COLUMN_NAME)
  public Boolean getBestsellers() {
    return bestsellers;
  }

  public void setBestsellers(final Boolean bestsellers) {
    this.bestsellers = bestsellers;
  }

  @Cache(usage = READ_ONLY)
  @OrderBy(POSITION_ASC)
  @OneToMany(cascade = {PERSIST, MERGE, REFRESH}, fetch = EAGER)
  @JoinTable(name = GNUOB_PRODUCTS_GNUOB_CONTENTS_TABLE_NAME,
      joinColumns = {@JoinColumn(name = GNUOB_PRODUCTS_ID_COLUMN_NAME, referencedColumnName = ID_COLUMN_NAME)},
      inverseJoinColumns = {@JoinColumn(name = CONTENTS_ID_COLUMN_NAME, referencedColumnName = ID_COLUMN_NAME)})
  public Set<Content> getContents() {
    return contents;
  }

  public void setContents(final Set<Content> contents) {
    this.contents = contents;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = DESCRIPTION_COLUMN_NAME, nullable = false)
  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = DISCOUNT_COLUMN_NAME, nullable = false)
  public BigDecimal getDiscount() {
    return discount;
  }

  public void setDiscount(final BigDecimal discount) {
    this.discount = discount;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_HEIGHT_COLUMN_NAME)
  public BigDecimal getItemHeight() {
    return itemHeight;
  }

  public void setItemHeight(final BigDecimal itemHeight) {
    this.itemHeight = itemHeight;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_HEIGHT_UNIT_COLUMN_NAME)
  public String getItemHeightUnit() {
    return itemHeightUnit;
  }

  public void setItemHeightUnit(final String itemHeightUnit) {
    this.itemHeightUnit = itemHeightUnit;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_LENGTH_COLUMN_NAME)
  public BigDecimal getItemLength() {
    return itemLength;
  }

  public void setItemLength(final BigDecimal itemLength) {
    this.itemLength = itemLength;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_LENGTH_UNIT_COLUMN_NAME)
  public String getItemLengthUnit() {
    return itemLengthUnit;
  }

  public void setItemLengthUnit(final String itemLengthUnit) {
    this.itemLengthUnit = itemLengthUnit;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_URL_COLUMN_NAME)
  public String getItemUrl() {
    return itemUrl;
  }

  public void setItemUrl(final String itemUrl) {
    this.itemUrl = itemUrl;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = ITEM_WEIGHT_COLUMN_NAME, nullable = false)
  public BigDecimal getItemWeight() {
    return itemWeight;
  }

  public void setItemWeight(final BigDecimal itemWeight) {
    this.itemWeight = itemWeight;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_WEIGHT_UNIT_COLUMN_NAME)
  public String getItemWeightUnit() {
    return itemWeightUnit;
  }

  public void setItemWeightUnit(final String itemWeightUnit) {
    this.itemWeightUnit = itemWeightUnit;
  }

  @JsonProperty
  @XmlElement
  @Column(name = ITEM_WIDTH_COLUMN_NAME)
  public BigDecimal getItemWidth() {
    return itemWidth;
  }

  public void setItemWidth(final BigDecimal itemWidth) {
    this.itemWidth = itemWidth;
  }

  @XmlElement
  @Column(name = ITEM_WIDTH_UNIT_COLUMN_NAME)
  public String getItemWidthUnit() {
    return itemWidthUnit;
  }

  public void setItemWidthUnit(final String itemWidthUnit) {
    this.itemWidthUnit = itemWidthUnit;
  }

  @JsonProperty
  @XmlElement
  @Column(name = LATEST_COLLECTION_COLUMN_NAME)
  public Boolean getLatestCollection() {
    return latestCollection;
  }

  public void setLatestCollection(final Boolean latestCollection) {
    this.latestCollection = latestCollection;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = NAME_COLUMN_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = NUMBER_COLUMN_NAME, nullable = false)
  public String getNumber() {
    return number;
  }

  public void setNumber(final String number) {
    this.number = number;
  }

  @OrderBy(POSITION_ASC)
  @OneToMany(cascade = {PERSIST, MERGE, REMOVE, REFRESH}, fetch = EAGER)
  @JoinTable(name = GNUOB_PRODUCTS_GNUOB_OPTIONS_TABLE_NAME,
      joinColumns = {@JoinColumn(name = GNUOB_PRODUCTS_ID_COLUMN_NAME, referencedColumnName = ID_COLUMN_NAME)},
      inverseJoinColumns = {@JoinColumn(name = OPTIONS_ID_COLUMN_NAME, referencedColumnName = ID_COLUMN_NAME)})
  public Set<Option> getOptions() {
    return options;
  }

  public void setOptions(final Set<Option> options) {
    this.options = options;
  }

  @JsonProperty
  @XmlElement
  @Column(name = RATING_COLUMN_NAME)
  public Integer getRating() {
    return rating;
  }

  public void setRating(final Integer rating) {
    this.rating = rating;
  }

  @JsonProperty
  @XmlElement
  @Column(name = RECOMMENDED_COLUMN_NAME)
  public Boolean getRecommended() {
    return recommended;
  }

  public void setRecommended(final Boolean recommended) {
    this.recommended = recommended;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = SHIPPING_COST_COLUMN_NAME, nullable = false)
  public BigDecimal getShippingCost() {
    return shippingCost;
  }

  public void setShippingCost(final BigDecimal shippingCost) {
    this.shippingCost = shippingCost;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @OneToOne(cascade = {PERSIST, MERGE, REMOVE, REFRESH}, optional = false)
  public Stock getStock() {
    return stock;
  }

  public void setStock(final Stock stock) {
    this.stock = stock;
  }

  @OrderBy(POSITION_ASC)
  @ManyToMany(cascade = {PERSIST, REMOVE, REFRESH}, fetch = EAGER)
  @JoinTable(name = GNUOB_PRODUCTS_GNUOB_SUB_CATEGORIES_TABLE_NAME,
      joinColumns = {@JoinColumn(name = GNUOB_PRODUCTS_ID_COLUMN_NAME, referencedColumnName = ID_COLUMN_NAME)},
      inverseJoinColumns = {@JoinColumn(name = SUB_CATEGORIES_ID_COLUMN_NAME, referencedColumnName = ID_COLUMN_NAME)})
  public Set<SubCategory> getSubCategories() {
    return subCategories;
  }

  public void setSubCategories(final Set<SubCategory> subCategories) {
    this.subCategories = subCategories;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = TAX_COLUMN_NAME, nullable = false)
  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(final BigDecimal tax) {
    this.tax = tax;
  }
}
