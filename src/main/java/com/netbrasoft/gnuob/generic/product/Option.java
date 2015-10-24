package com.netbrasoft.gnuob.generic.product;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.netbrasoft.gnuob.generic.Type;

@Cacheable(value = false)
@Entity(name = Option.ENTITY)
@Table(name = Option.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = Option.ENTITY)
public class Option extends Type {

  private static final long serialVersionUID = -4350389615614303733L;
  protected static final String ENTITY = "Option";
  protected static final String TABLE = "GNUOB_OPTIONS";

  @Column(name = "POSITION")
  private Integer position = 0;

  @Column(name = "VALUE", nullable = false)
  private String value;

  @Column(name = "DESCRIPTION", nullable = false)
  private String description;

  @Column(name = "DISABLED", nullable = false)
  private boolean disabled;

  @OrderBy("position asc")
  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
  @JoinTable(name = "gnuob_options_gnuob_sub_options", joinColumns = {@JoinColumn(name = "GNUOB_OPTIONS_ID", referencedColumnName = "ID")},
      inverseJoinColumns = {@JoinColumn(name = "sub_options_ID", referencedColumnName = "ID")})
  private Set<SubOption> subOptions = new HashSet<SubOption>();

  @XmlElement(name = "description", required = true)
  public String getDescription() {
    return description;
  }

  @XmlElement(name = "position")
  public Integer getPosition() {
    return position;
  }

  public Set<SubOption> getSubOptions() {
    return subOptions;
  }

  @XmlElement(name = "value", required = true)
  public String getValue() {
    return value;
  }

  @Override
  public boolean isDetached() {
    for (final SubOption subOption : subOptions) {
      if (subOption.isDetached()) {
        return subOption.isDetached();
      }
    }
    return getId() > 0;
  }

  @XmlElement(name = "disabled", required = true)
  public boolean isDisabled() {
    return disabled;
  }

  private void positionSubOptions() {
    int position = 0;

    for (final SubOption subOption : subOptions) {
      subOption.setPosition(Integer.valueOf(position++));
    }
  }

  @Override
  public void prePersist() {
    positionSubOptions();
  }

  @Override
  public void preUpdate() {
    positionSubOptions();
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public void setSubOptions(Set<SubOption> subOptions) {
    this.subOptions = subOptions;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
