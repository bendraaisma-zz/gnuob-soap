package com.netbrasoft.gnuob.generic.product;

import java.math.BigInteger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.netbrasoft.gnuob.generic.Type;

@Entity(name = Stock.ENTITY)
@Table(name = Stock.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = Stock.ENTITY)
public class Stock extends Type {

    private static final long serialVersionUID = 748737455712566437L;
    protected static final String ENTITY = "Stock";
    protected static final String TABLE = "GNUOB_STOCKS";

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "stock")
    private Product product;

    @Column(name = "QUANTITY")
    private BigInteger quantity;

    @Column(name = "MIN_QUANTITY")
    private BigInteger minQuantity;

    @Column(name = "MAX_QUANTITY")
    private BigInteger maxQuantity;

    @XmlElement(name = "maxQuantity")
    public BigInteger getMaxQuantity() {
        return maxQuantity;
    }

    @XmlElement(name = "minQuantity")
    public BigInteger getMinQuantity() {
        return minQuantity;
    }

    @XmlTransient
    public Product getProduct() {
        return product;
    }

    @XmlElement(name = "quantity")
    public BigInteger getQuantity() {
        return quantity;
    }

    public void setMaxQuantity(BigInteger maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public void setMinQuantity(BigInteger minQuantity) {
        this.minQuantity = minQuantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }
}
