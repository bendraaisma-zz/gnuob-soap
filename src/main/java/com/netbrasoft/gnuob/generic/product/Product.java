package com.netbrasoft.gnuob.generic.product;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.netbrasoft.gnuob.generic.category.SubCategory;
import com.netbrasoft.gnuob.generic.content.Content;
import com.netbrasoft.gnuob.generic.security.Access;

@Cacheable(value = true)
@Entity(name = Product.ENTITY)
@Table(name = Product.TABLE)
@XmlRootElement(name = Product.ENTITY)
public class Product extends Access {

   private static final long serialVersionUID = -5818453495081202563L;
   protected static final String ENTITY = "Product";
   protected static final String TABLE = "GNUOB_PRODUCTS";

   @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @OrderBy("position asc")
   private Set<SubCategory> subCategories = new LinkedHashSet<SubCategory>();

   @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, optional = false)
   private Stock stock;

   @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER)
   @OrderBy("position asc")
   private Set<Content> contents = new HashSet<Content>();

   @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER)
   @OrderBy("position asc")
   private Set<Option> options = new HashSet<Option>();

   @Column(name = "NAME", nullable = false)
   private String name;

   @Column(name = "DESCRIPTION", nullable = false)
   private String description;

   @Column(name = "NUMBER", nullable = false)
   private String number;

   @Column(name = "AMOUNT", nullable = false)
   private BigDecimal amount;

   @Column(name = "TAX", nullable = false)
   private BigDecimal tax;

   @Column(name = "SHIPPING_COST", nullable = false)
   private BigDecimal shippingCost;

   @Column(name = "ITEM_WEIGHT", nullable = false)
   private BigDecimal itemWeight;

   @Column(name = "ITEM_WEIGHT_UNIT")
   private String itemWeightUnit;

   @Column(name = "ITEM_LENGTH")
   private BigDecimal itemLength;

   @Column(name = "ITEM_LENGTH_UNIT")
   private String itemLengthUnit;

   @Column(name = "ITEM_WIDTH")
   private BigDecimal itemWidth;

   @Column(name = "ITEM_WIDTH_UNIT")
   private String itemWidthUnit;

   @Column(name = "ITEM_HEIGHT")
   private BigDecimal itemHeight;

   @Column(name = "ITEM_HEIGHT_UNIT")
   private String itemHeightUnit;

   @Column(name = "ITEM_URL")
   private String itemUrl;

   @Column(name = "RECOMMENDED")
   private Boolean recommended;

   @Column(name = "RATING")
   private Integer rating;

   @Column(name = "DISCOUNT", nullable = false)
   private BigDecimal discount;

   @Column(name = "BESTSELLERS")
   private Boolean bestsellers;

   @Column(name = "LATEST_COLLECTION")
   private Boolean latestCollection;

   @XmlElement(name = "amount", required = true)
   public BigDecimal getAmount() {
      return amount;
   }

   @XmlElement(name = "bestsellers")
   public Boolean getBestsellers() {
      return bestsellers;
   }

   public Set<Content> getContents() {
      return contents;
   }

   @XmlElement(name = "description", required = true)
   public String getDescription() {
      return description;
   }

   @XmlElement(name = "discount", required = true)
   public BigDecimal getDiscount() {
      return discount;
   }

   @XmlElement(name = "itemHeight")
   public BigDecimal getItemHeight() {
      return itemHeight;
   }

   @XmlElement(name = "itemHeightUnit")
   public String getItemHeightUnit() {
      return itemHeightUnit;
   }

   @XmlElement(name = "itemLength")
   public BigDecimal getItemLength() {
      return itemLength;
   }

   @XmlElement(name = "itemLengthUnit")
   public String getItemLengthUnit() {
      return itemLengthUnit;
   }

   @XmlElement(name = "itemUrl")
   public String getItemUrl() {
      return itemUrl;
   }

   @XmlElement(name = "itemWeight", required = true)
   public BigDecimal getItemWeight() {
      return itemWeight;
   }

   @XmlElement(name = "itemWeightUnit")
   public String getItemWeightUnit() {
      return itemWeightUnit;
   }

   @XmlElement(name = "itemWidth")
   public BigDecimal getItemWidth() {
      return itemWidth;
   }

   @XmlElement(name = "itemWidthUnit")
   public String getItemWidthUnit() {
      return itemWidthUnit;
   }

   @XmlElement(name = "latestCollection")
   public Boolean getLatestCollection() {
      return latestCollection;
   }

   @XmlElement(name = "name", required = true)
   public String getName() {
      return name;
   }

   @XmlElement(name = "number", required = true)
   public String getNumber() {
      return number;
   }

   public Set<Option> getOptions() {
      return options;
   }

   @XmlElement(name = "rating")
   public Integer getRating() {
      return rating;
   }

   @XmlElement(name = "recommended")
   public Boolean getRecommended() {
      return recommended;
   }

   @XmlElement(name = "shippingCost", required = true)
   public BigDecimal getShippingCost() {
      return shippingCost;
   }

   @XmlElement(name = "stock", required = true)
   public Stock getStock() {
      return stock;
   }

   public Set<SubCategory> getSubCategories() {
      return subCategories;
   }

   @XmlElement(name = "tax", required = true)
   public BigDecimal getTax() {
      return tax;
   }

   private void positionContents() {
      int position = 0;

      for (Content content : contents) {
         content.setPosition(Integer.valueOf(position++));
      }
   }

   private void positionOptions() {
      int position = 0;

      for (Option option : options) {
         option.setPosition(Integer.valueOf(position++));
      }
   }

   private void positionSubCategories() {
      int position = 0;

      for (SubCategory subCategory : subCategories) {
         subCategory.setPosition(Integer.valueOf(position++));
      }
   }

   @Override
   public void prePersist() {
      positionSubCategories();
      positionContents();
      positionOptions();
   }

   @Override
   public void preUpdate() {
      positionSubCategories();
      positionContents();
      positionOptions();
   }

   public void setAmount(BigDecimal amount) {
      this.amount = amount;
   }

   public void setBestsellers(Boolean bestsellers) {
      this.bestsellers = bestsellers;
   }

   public void setContents(Set<Content> contents) {
      this.contents = contents;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setDiscount(BigDecimal discount) {
      this.discount = discount;
   }

   public void setItemHeight(BigDecimal itemHeight) {
      this.itemHeight = itemHeight;
   }

   public void setItemHeightUnit(String itemHeightUnit) {
      this.itemHeightUnit = itemHeightUnit;
   }

   public void setItemLength(BigDecimal itemLength) {
      this.itemLength = itemLength;
   }

   public void setItemLengthUnit(String itemLengthUnit) {
      this.itemLengthUnit = itemLengthUnit;
   }

   public void setItemUrl(String itemUrl) {
      this.itemUrl = itemUrl;
   }

   public void setItemWeight(BigDecimal itemWeight) {
      this.itemWeight = itemWeight;
   }

   public void setItemWeightUnit(String itemWeightUnit) {
      this.itemWeightUnit = itemWeightUnit;
   }

   public void setItemWidth(BigDecimal itemWidth) {
      this.itemWidth = itemWidth;
   }

   public void setItemWidthUnit(String itemWidthUnit) {
      this.itemWidthUnit = itemWidthUnit;
   }

   public void setLatestCollection(Boolean latestCollection) {
      this.latestCollection = latestCollection;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setNumber(String number) {
      this.number = number;
   }

   public void setOptions(Set<Option> options) {
      this.options = options;
   }

   public void setRating(Integer rating) {
      this.rating = rating;
   }

   public void setRecommended(Boolean recommended) {
      this.recommended = recommended;
   }

   public void setShippingCost(BigDecimal shippingCost) {
      this.shippingCost = shippingCost;
   }

   public void setStock(Stock stock) {
      this.stock = stock;
   }

   public void setSubCategories(Set<SubCategory> subCategories) {
      this.subCategories = subCategories;
   }

   public void setTax(BigDecimal tax) {
      this.tax = tax;
   }
}
