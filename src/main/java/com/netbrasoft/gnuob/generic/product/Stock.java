package com.netbrasoft.gnuob.generic.product;

import java.math.BigInteger;

import javax.persistence.Cacheable;
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

import com.netbrasoft.gnuob.generic.AbstractType;

@Cacheable(value = false)
@Entity(name = Stock.ENTITY)
@Table(name = Stock.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = Stock.ENTITY)
public class Stock extends AbstractType {

  private static final long serialVersionUID = 748737455712566437L;
  protected static final String ENTITY = "Stock";
  protected static final String TABLE = "GNUOB_STOCKS";

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "stock")
  private Product product;

  @Column(name = "QUANTITY", nullable = false)
  private BigInteger quantity;

  @Column(name = "MIN_QUANTITY", nullable = false)
  private BigInteger minQuantity;

  @Column(name = "MAX_QUANTITY", nullable = false)
  private BigInteger maxQuantity;

  @XmlElement(name = "maxQuantity", required = true)
  public BigInteger getMaxQuantity() {
    return maxQuantity;
  }

  @XmlElement(name = "minQuantity", required = true)
  public BigInteger getMinQuantity() {
    return minQuantity;
  }

  @XmlTransient
  public Product getProduct() {
    return product;
  }

  @XmlElement(name = "quantity", required = true)
  public BigInteger getQuantity() {
    return quantity;
  }

  @Override
  public boolean isDetached() {
    return getId() > 0;
  }

  @Override
  public void prePersist() {
    return;
  }

  @Override
  public void preUpdate() {
    return;
  }

  public void setMaxQuantity(final BigInteger maxQuantity) {
    this.maxQuantity = maxQuantity;
  }

  public void setMinQuantity(final BigInteger minQuantity) {
    this.minQuantity = minQuantity;
  }

  public void setProduct(final Product product) {
    this.product = product;
  }

  public void setQuantity(final BigInteger quantity) {
    this.quantity = quantity;
  }
}
