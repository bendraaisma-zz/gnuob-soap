package com.netbrasoft.gnuob.generic.order;

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

import com.netbrasoft.gnuob.generic.Type;
import com.netbrasoft.gnuob.generic.customer.Address;

@Cacheable(value = false)
@Entity(name = Shipment.ENTITY)
@Table(name = Shipment.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = Shipment.ENTITY)
public class Shipment extends Type {

  private static final long serialVersionUID = 7122488386952479304L;
  protected static final String ENTITY = "Shipment";
  protected static final String TABLE = "GNUOB_SHIPMENTS";

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, orphanRemoval = true, optional = false)
  private Address address;

  @Column(name = "SHIPMENT_TYPE")
  private String shipmentType = "NOT_SPECIFIED";

  public Shipment() {

  }

  @XmlElement(name = "address", required = true)
  public Address getAddress() {
    return address;
  }

  @XmlElement(name = "shipmentType")
  public String getShipmentType() {
    return shipmentType;
  }

  @Override
  public void prePersist() {
    return;
  }

  @Override
  public void preUpdate() {
    return;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public void setShipmentType(String shipmentType) {
    this.shipmentType = shipmentType;
  }

}
