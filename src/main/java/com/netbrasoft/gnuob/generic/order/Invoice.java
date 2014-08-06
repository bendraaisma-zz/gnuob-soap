package com.netbrasoft.gnuob.generic.order;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.netbrasoft.gnuob.generic.Type;
import com.netbrasoft.gnuob.generic.customer.Address;

@Entity(name = Invoice.ENTITY)
@Table(name = Invoice.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = Invoice.ENTITY)
public class Invoice extends Type {

	private static final long serialVersionUID = 5609152324488531802L;
	protected static final String ENTITY = "Invoice";
	protected static final String TABLE = "GNUOB_INVOICES";

	@Column(name = "INVOICE_ID", nullable = false)
	private String invoiceId;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Payment> payments = new HashSet<Payment>();

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true, optional = false)
	private Address address;

	public Invoice() {
	}

	@XmlElement(name = "address", required = true)
	public Address getAddress() {
		return address;
	}

	@XmlElement(name = "invoiceId")
	public String getInvoiceId() {
		return invoiceId;
	}

	@XmlElement(name = "payments")
	public Set<Payment> getPayments() {
		return payments;
	}

	@PrePersist
	public void prePersistInvoiceId() {
		if (invoiceId == null || invoiceId.trim().equals("")) {
			invoiceId = UUID.randomUUID().toString();
		}
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

}
