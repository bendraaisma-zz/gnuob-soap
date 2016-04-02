package com.netbrasoft.gnuob.generic.product;

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MAX_QUANTITY_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MIN_QUANTITY_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.QUANTITY_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.STOCK_ENTITY_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.STOCK_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.STOCK_TABLE_NAME;

import java.math.BigInteger;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.netbrasoft.gnuob.generic.AbstractType;

@Cacheable(value = false)
@Entity(name = STOCK_ENTITY_NAME)
@Table(name = STOCK_TABLE_NAME)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = STOCK_ENTITY_NAME)
public class Stock extends AbstractType {

  private static final long serialVersionUID = 748737455712566437L;

  private BigInteger maxQuantity;
  private BigInteger minQuantity;
  private Product product;
  private BigInteger quantity;

  public Stock() {}

  @Override
  @Transient
  public boolean isDetached() {
    return isAbstractTypeDetached();
  }

  @Override
  public void prePersist() {}

  @Override
  public void preUpdate() {}

  @XmlElement(required = true)
  @Column(name = MAX_QUANTITY_COLUMN_NAME, nullable = false)
  public BigInteger getMaxQuantity() {
    return maxQuantity;
  }

  @XmlElement(required = true)
  @Column(name = MIN_QUANTITY_COLUMN_NAME, nullable = false)
  public BigInteger getMinQuantity() {
    return minQuantity;
  }

  @XmlTransient
  @OneToOne(cascade = CascadeType.ALL, mappedBy = STOCK_PARAM_NAME)
  public Product getProduct() {
    return product;
  }

  @XmlElement(required = true)
  @Column(name = QUANTITY_COLUMN_NAME, nullable = false)
  public BigInteger getQuantity() {
    return quantity;
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
