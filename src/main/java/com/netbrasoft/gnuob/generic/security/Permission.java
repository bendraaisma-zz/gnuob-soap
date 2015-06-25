package com.netbrasoft.gnuob.generic.security;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.netbrasoft.gnuob.generic.Type;

@Cacheable(value = true)
@Entity(name = Permission.ENTITY)
@Table(name = Permission.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = Permission.ENTITY)
public class Permission extends Type {

   private static final long serialVersionUID = 3108374497171836688L;
   protected static final String ENTITY = "Permission";
   protected static final String TABLE = "GNUOB_PERMISSIONS";

   @Column(name = "OWNER", nullable = false)
   @Enumerated(EnumType.STRING)
   private Rule owner = Rule.DELETE_ACCESS;

   @Column(name = "\"GROUP\"", nullable = false)
   @Enumerated(EnumType.STRING)
   private Rule group = Rule.READ_ACCESS;

   @Column(name = "OTHERS", nullable = false)
   @Enumerated(EnumType.STRING)
   private Rule others = Rule.READ_ACCESS;

   @XmlElement(name = "group", required = true)
   public Rule getGroup() {
      return group;
   }

   @XmlElement(name = "others", required = true)
   public Rule getOthers() {
      return others;
   }

   @XmlElement(name = "owner", required = true)
   public Rule getOwner() {
      return owner;
   }

   @Override
   public void prePersist() {
      return;
   }

   @Override
   public void preUpdate() {
      return;
   }

   public void setGroup(Rule group) {
      this.group = group;
   }

   public void setOthers(Rule others) {
      this.others = others;
   }

   public void setOwner(Rule owner) {
      this.owner = owner;
   }

}
