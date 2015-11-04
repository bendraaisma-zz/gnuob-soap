package com.netbrasoft.gnuob.generic.category;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.velocity.context.Context;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.netbrasoft.gnuob.generic.content.Content;
import com.netbrasoft.gnuob.generic.content.contexts.ContextVisitor;
import com.netbrasoft.gnuob.generic.security.AbstractAccess;

@Cacheable(value = true)
@Entity(name = Category.ENTITY)
@Table(name = Category.TABLE)
@XmlRootElement(name = Category.ENTITY)
public class Category extends AbstractAccess {

  private static final long serialVersionUID = 8531470310780646179L;
  protected static final String ENTITY = "Category";
  protected static final String TABLE = "GNUOB_CATEGORIES";

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "DESCRIPTION", nullable = false)
  private String description;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
  @OrderBy("position asc")
  @JoinTable(name = "gnuob_categories_gnuob_sub_categories", joinColumns = {@JoinColumn(name = "GNUOB_CATEGORIES_ID", referencedColumnName = "ID")},
      inverseJoinColumns = {@JoinColumn(name = "subCategories_ID", referencedColumnName = "ID")})
  private Set<SubCategory> subCategories = new LinkedHashSet<SubCategory>();

  @OrderBy("position asc")
  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
  @JoinTable(name = "gnuob_categories_gnuob_contents", joinColumns = {@JoinColumn(name = "GNUOB_CATEGORIES_ID", referencedColumnName = "ID")},
      inverseJoinColumns = {@JoinColumn(name = "contents_ID", referencedColumnName = "ID")})
  private Set<Content> contents = new LinkedHashSet<Content>();

  @Column(name = "POSITION")
  private Integer position;

  @Override
  public Context accept(final ContextVisitor visitor) {
    return visitor.visit(this);
  }

  @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  public Set<Content> getContents() {
    return contents;
  }

  @XmlElement(name = "description", required = true)
  public String getDescription() {
    return description;
  }

  @XmlElement(name = "name", required = true)
  public String getName() {
    return name;
  }

  @XmlElement(name = "position")
  public Integer getPosition() {
    return position;
  }

  public Set<SubCategory> getSubCategories() {
    return subCategories;
  }

  @Override
  public boolean isDetached() {
    for (final SubCategory subCategory : subCategories) {
      if (subCategory.isDetached()) {
        return subCategory.isDetached();
      }
    }
    for (final Content content : contents) {
      if (content.isDetached()) {
        return content.isDetached();
      }
    }
    return getId() > 0;
  }

  private void positionContents() {
    int index = 0;

    for (final Content content : contents) {
      content.setPosition(Integer.valueOf(index++));
    }
  }

  private void positionSubCategories() {
    int index = 0;

    for (final SubCategory subCategory : subCategories) {
      subCategory.setPosition(Integer.valueOf(index++));
    }
  }

  @Override
  public void prePersist() {
    positionSubCategories();
    positionContents();

  }

  @Override
  public void preUpdate() {
    positionSubCategories();
    positionContents();
  }

  public void setContents(final Set<Content> contents) {
    this.contents = contents;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setPosition(final Integer position) {
    this.position = position;
  }

  public void setSubCategories(final Set<SubCategory> subCategories) {
    this.subCategories = subCategories;
  }
}
