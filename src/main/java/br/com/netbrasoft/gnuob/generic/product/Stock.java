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
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MAX_QUANTITY_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MIN_QUANTITY_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.QUANTITY_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.STOCK_ENTITY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.STOCK_PARAM_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.STOCK_TABLE_NAME;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.InheritanceType.SINGLE_TABLE;
import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.netbrasoft.gnuob.generic.AbstractType;

@Cacheable(value = false)
@Entity(name = STOCK_ENTITY_NAME)
@Table(name = STOCK_TABLE_NAME)
@Inheritance(strategy = SINGLE_TABLE)
@XmlRootElement(name = STOCK_ENTITY_NAME)
public class Stock extends AbstractType {

  private static final long serialVersionUID = 748737455712566437L;

  private BigInteger maxQuantity;
  private BigInteger minQuantity;
  private Product product;
  private BigInteger quantity;

  public Stock() {
    super();
  }

  public Stock(String json) throws IOException, IllegalAccessException, InvocationTargetException {
    copyProperties(this, mapper.readValue(json, Stock.class));
  }

  public static Stock getInstance() {
    return new Stock();
  }

  public static Stock getInstanceByJson(String json)
      throws IllegalAccessException, InvocationTargetException, IOException {
    return new Stock(json);
  }

  @Override
  @JsonIgnore
  @Transient
  public boolean isDetached() {
    return isAbstractTypeDetached();
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = MAX_QUANTITY_COLUMN_NAME, nullable = false)
  public BigInteger getMaxQuantity() {
    return maxQuantity;
  }

  public void setMaxQuantity(final BigInteger maxQuantity) {
    this.maxQuantity = maxQuantity;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = MIN_QUANTITY_COLUMN_NAME, nullable = false)
  public BigInteger getMinQuantity() {
    return minQuantity;
  }

  public void setMinQuantity(final BigInteger minQuantity) {
    this.minQuantity = minQuantity;
  }

  @JsonIgnore
  @XmlTransient
  @OneToOne(cascade = {PERSIST, MERGE, REFRESH, REMOVE}, mappedBy = STOCK_PARAM_NAME)
  public Product getProduct() {
    return product;
  }

  public void setProduct(final Product product) {
    this.product = product;
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

  @Override
  public String toString() {
    return reflectionToString(this, SHORT_PREFIX_STYLE);
  }
}
