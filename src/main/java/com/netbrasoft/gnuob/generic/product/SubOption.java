package com.netbrasoft.gnuob.generic.product;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.netbrasoft.gnuob.generic.AbstractType;

@Cacheable(value = false)
@Entity(name = SubOption.ENTITY)
@Table(name = SubOption.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = SubOption.ENTITY)
public class SubOption extends AbstractType {

  private static final long serialVersionUID = -4350389615614303733L;
  protected static final String ENTITY = "SubOption";
  protected static final String TABLE = "GNUOB_SUB_OPTIONS";

  @Column(name = "POSITION")
  private Integer position = 0;

  @Column(name = "VALUE", nullable = false)
  private String value;

  @Column(name = "DESCRIPTION", nullable = false)
  private String description;

  @Column(name = "DISABLED", nullable = false)
  private boolean disabled;

  @XmlElement(name = "description", required = true)
  public String getDescription() {
    return description;
  }

  @XmlTransient
  public Integer getPosition() {
    return position;
  }

  @XmlElement(name = "value", required = true)
  public String getValue() {
    return value;
  }

  @Override
  public boolean isDetached() {
    return getId() > 0;
  }

  @XmlElement(name = "disabled", required = true)
  public boolean isDisabled() {
    return disabled;
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

  public void setDisabled(final boolean disabled) {
    this.disabled = disabled;
  }

  public void setPosition(final Integer position) {
    this.position = position;
  }

  public void setValue(final String value) {
    this.value = value;
  }
}
