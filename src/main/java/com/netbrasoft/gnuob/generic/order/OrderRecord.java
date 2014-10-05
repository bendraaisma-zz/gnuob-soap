package com.netbrasoft.gnuob.generic.order;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.netbrasoft.gnuob.generic.Type;
import com.netbrasoft.gnuob.generic.product.Product;

@Entity(name = OrderRecord.ENTITY)
@Table(name = OrderRecord.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = OrderRecord.ENTITY)
public class OrderRecord extends Type {

    private static final long serialVersionUID = 5394749104442554097L;
    protected static final String ENTITY = "OrderRecord";
    protected static final String TABLE = "GNUOB_ORDER_RECORDS";

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "NUMBER", nullable = false)
    private String number;

    @Column(name = "TAX", nullable = false)
    private BigDecimal tax;

    @Column(name = "QUANTITY", nullable = false)
    private BigInteger quantity;

    @Column(name = "ITEM_WEIGHT")
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

    @Transient
    private Product product;

    public OrderRecord() {
    }

    @XmlElement(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
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

    @XmlElement(name = "itemWeight")
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

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    @XmlElement(name = "number")
    public String getNumber() {
        return number;
    }

    public Product getProduct() {
        return product;
    }

    @XmlElement(name = "quantity", required = true)
    public BigInteger getQuantity() {
        return quantity;
    }

    @XmlElement(name = "tax")
    public BigDecimal getTax() {
        return tax;
    }

    @PrePersist
    protected void prePersistOrderRecord() {

        if (product != null) {
            name = name == null ? product.getName() : name;
            description = description == null ? product.getDescription() : description;
            amount = amount == null ? product.getAmount() : amount;
            number = number == null ? product.getNumber() : number;
            tax = tax == null ? product.getTax() : tax;
            itemWeight = itemWeight == null ? product.getItemWeight() : itemWeight;
            itemWeightUnit = itemWeightUnit == null ? product.getItemWeightUnit() : itemWeightUnit;
            itemLength = itemLength == null ? product.getItemLength() : itemLength;
            itemLengthUnit = itemLengthUnit == null ? product.getItemLengthUnit() : itemLengthUnit;
            itemWidth = itemWidth == null ? product.getItemWidth() : itemWidth;
            itemWidthUnit = itemWidthUnit == null ? product.getItemWidthUnit() : itemLengthUnit;
            itemHeight = itemHeight == null ? product.getItemHeight() : itemHeight;
            itemHeightUnit = itemHeightUnit == null ? product.getItemLengthUnit() : itemHeightUnit;
            itemUrl = itemUrl == null ? product.getItemUrl() : itemUrl;
        }
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
}
