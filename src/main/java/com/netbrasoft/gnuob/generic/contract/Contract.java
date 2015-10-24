package com.netbrasoft.gnuob.generic.contract;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.velocity.context.Context;

import com.netbrasoft.gnuob.generic.content.contexts.ContextVisitor;
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.offer.Offer;
import com.netbrasoft.gnuob.generic.order.Order;
import com.netbrasoft.gnuob.generic.security.Access;

@Cacheable(value = true)
@Entity(name = Contract.ENTITY)
@Table(name = Contract.TABLE)
@XmlRootElement(name = Contract.ENTITY)
public class Contract extends Access {

  private static final long serialVersionUID = -2215842699700777956L;
  protected static final String ENTITY = "Contract";
  protected static final String TABLE = "GNUOB_CONTRACTS";

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, optional = false, fetch = FetchType.EAGER)
  private Customer customer;

  @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "contract")
  private Set<Order> orders = new HashSet<Order>();

  @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "contract")
  private Set<Offer> offers = new HashSet<Offer>();

  @Column(name = "CONTRACT_ID", nullable = false)
  private String contractId;

  public Contract() {}

  @Override
  public Context accept(ContextVisitor visitor) {
    return visitor.visit(this);
  }

  @XmlElement(name = "contractId", required = true)
  public String getContractId() {
    return contractId;
  }

  @XmlElement(name = "customer", required = true)
  public Customer getCustomer() {
    return customer;
  }

  @XmlTransient
  public Set<Offer> getOffers() {
    return offers;
  }

  @XmlTransient
  public Set<Order> getOrders() {
    return orders;
  }

  @Override
  public boolean isDetached() {
    if (customer != null && customer.isDetached()) {
      return customer.isDetached();
    }
    return getId() > 0;
  }

  @Override
  public void prePersist() {
    if (contractId == null || "".equals(contractId.trim())) {
      contractId = UUID.randomUUID().toString();
    }
  }

  @Override
  public void preUpdate() {
    return;
  }

  public void setContractId(String contractId) {
    this.contractId = contractId;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public void setOffers(Set<Offer> offers) {
    this.offers = offers;
  }

  public void setOrders(Set<Order> orders) {
    this.orders = orders;
  }

}
