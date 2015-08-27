package com.netbrasoft.gnuob.generic.category;

import java.util.LinkedHashSet;
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
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.netbrasoft.gnuob.generic.Type;
import com.netbrasoft.gnuob.generic.content.Content;

@Cacheable(value = true)
@Entity(name = SubCategory.ENTITY)
@Table(name = SubCategory.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = SubCategory.ENTITY)
public class SubCategory extends Type {

   private static final long serialVersionUID = -5835673403321034535L;
   protected static final String ENTITY = "SubCategory";
   protected static final String TABLE = "GNUOB_SUB_CATEGORIES";

   @OrderBy("position asc")
   @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER)
   @JoinTable(name = "gnuob_sub_categories_gnuob_sub_categories", joinColumns = { @JoinColumn(name = "GNUOB_SUB_CATEGORIES_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "subCategories_ID", referencedColumnName = "ID") })
   private Set<SubCategory> subCategories = new LinkedHashSet<SubCategory>();

   @OrderBy("position asc")
   @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER)
   @JoinTable(name = "gnuob_sub_categories_gnuob_contents", joinColumns = { @JoinColumn(name = "GNUOB_SUB_CATEGORIES_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "contents_ID", referencedColumnName = "ID") })
   private Set<Content> contents = new LinkedHashSet<Content>();

   @Column(name = "NAME", nullable = false)
   private String name;

   @Column(name = "DESCRIPTION", nullable = false)
   private String description;

   @Column(name = "POSITION")
   private Integer position;

   @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
   public Set<Content> getContents() {
      return contents;
   }

   @XmlElement(name = "description")
   public String getDescription() {
      return description;
   }

   @XmlElement(name = "name")
   public String getName() {
      return name;
   }

   @XmlTransient
   public Integer getPosition() {
      return position;
   }

   public Set<SubCategory> getSubCategories() {
      return subCategories;
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

   public void setContents(Set<Content> contents) {
      this.contents = contents;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setPosition(Integer position) {
      this.position = position;
   }

   public void setSubCategories(Set<SubCategory> subCategories) {
      this.subCategories = subCategories;
   }

}
