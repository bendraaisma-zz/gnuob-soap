package com.netbrasoft.gnuob.generic.security;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.velocity.context.Context;

import com.netbrasoft.gnuob.generic.content.contexts.ContextVisitor;

@Cacheable(value = true)
@Table(name = Site.TABLE)
@Entity(name = Site.ENTITY)
@XmlRootElement(name = Site.ENTITY)
public class Site extends Access {

  private static final long serialVersionUID = 985676314568291633L;

  protected static final String ENTITY = "Site";
  protected static final String TABLE = "GNUOB_SITES";

  @Column(name = "NAME", nullable = false, unique = true)
  private String name;

  @Column(name = "DESCRIPTION")
  private String description;

  public Site() {

  }

  public Site(String name) {
    this.name = name;
  }

  @Override
  public Context accept(ContextVisitor visitor) {
    return visitor.visit(this);
  }

  @XmlElement(name = "description")
  public String getDescription() {
    return description;
  }

  @XmlElement(name = "name", required = true)
  public String getName() {
    return name;
  }

  @Override
  public void prePersist() {
    return;
  }

  @Override
  public void preUpdate() {
    return;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setName(String name) {
    this.name = name;
  }
}
