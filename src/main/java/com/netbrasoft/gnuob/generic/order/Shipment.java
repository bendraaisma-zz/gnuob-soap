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

package com.netbrasoft.gnuob.generic.order;

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.NOT_SPECIFIED;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SHIPMENT_ENTITY_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SHIPMENT_TABLE_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SHIPMENT_TYPE_COLUMN_NAME;

import java.util.Arrays;

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

import org.apache.commons.lang3.StringUtils;

import com.netbrasoft.gnuob.generic.AbstractType;
import com.netbrasoft.gnuob.generic.customer.Address;

@Cacheable(value = false)
@Entity(name = SHIPMENT_ENTITY_NAME)
@Table(name = SHIPMENT_TABLE_NAME)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = SHIPMENT_ENTITY_NAME)
public class Shipment extends AbstractType {

  private static final long serialVersionUID = 7122488386952479304L;

  private Address address;
  private String shipmentType;

  @Override
  @Transient
  public boolean isDetached() {
    return Arrays.asList(new Boolean[] {isAbstractTypeDetached(), isAddressDetached()}).stream()
        .filter(e -> e.booleanValue()).count() > 0;
  }

  @Transient
  private boolean isAddressDetached() {
    return address != null && address.isDetached();
  }

  @XmlElement(required = true)
  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH},
      orphanRemoval = true, optional = false)
  public Address getAddress() {
    return address;
  }

  @XmlElement
  @Column(name = SHIPMENT_TYPE_COLUMN_NAME)
  public String getShipmentType() {
    if (StringUtils.isBlank(shipmentType)) {
      shipmentType = NOT_SPECIFIED;
    }
    return shipmentType;
  }

  public void setAddress(final Address address) {
    this.address = address;
  }

  public void setShipmentType(final String shipmentType) {
    this.shipmentType = shipmentType;
  }
}
