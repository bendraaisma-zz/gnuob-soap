package com.netbrasoft.gnuob.generic.setting;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.velocity.context.Context;

import com.netbrasoft.gnuob.generic.content.contexts.ContextVisitor;
import com.netbrasoft.gnuob.generic.security.AbstractAccess;

@Cacheable(value = true)
@Entity(name = Setting.ENTITY)
@Table(name = Setting.TABLE)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = Setting.ENTITY)
public class Setting extends AbstractAccess {

  private static final long serialVersionUID = -1489369413428188989L;
  protected static final String ENTITY = "Setting";
  protected static final String TABLE = "GNUOB_SETTINGS";

  @Column(name = "PROPERTY", nullable = false)
  @XmlElement(name = "property", required = true)
  private String property;

  @Column(name = "VALUE", nullable = false)
  @XmlElement(name = "value", required = true)
  private String value;

  @Column(name = "DESCRIPTION", nullable = false)
  @XmlElement(name = "description")
  private String description;

  @Override
  public Context accept(final ContextVisitor visitor) {
    return visitor.visit(this);
  }

  public String getDescription() {
    return description;
  }

  public String getProperty() {
    return property;
  }

  public String getValue() {
    return value;
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

  public void setDescription(final String description) {
    this.description = description;
  }

  public void setProperty(final String property) {
    this.property = property;
  }

  public void setValue(final String value) {
    this.value = value;
  }
}
