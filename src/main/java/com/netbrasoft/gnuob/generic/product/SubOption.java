package com.netbrasoft.gnuob.generic.product;

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DESCRIPTION_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DISABLED_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.POSITION_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SUB_OPTION_ENTITY_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SUB_OPTION_TABLE_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.VALUE_COLUMN_NAME;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.netbrasoft.gnuob.generic.AbstractType;

@Cacheable(value = false)
@Entity(name = SUB_OPTION_ENTITY_NAME)
@Table(name = SUB_OPTION_TABLE_NAME)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = SUB_OPTION_ENTITY_NAME)
public class SubOption extends AbstractType {

  private static final long serialVersionUID = -4350389615614303733L;

  private String description;
  private boolean disabled;
  private Integer position;
  private String value;

  public SubOption() {}

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
  @Column(name = DESCRIPTION_COLUMN_NAME, nullable = false)
  public String getDescription() {
    return description;
  }

  @XmlTransient
  @Column(name = POSITION_COLUMN_NAME)
  public Integer getPosition() {
    return position;
  }

  @XmlElement(required = true)
  @Column(name = VALUE_COLUMN_NAME, nullable = false)
  public String getValue() {
    return value;
  }

  @XmlElement(required = true)
  @Column(name = DISABLED_COLUMN_NAME, nullable = false)
  public boolean isDisabled() {
    return disabled;
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
