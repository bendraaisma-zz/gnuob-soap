package com.netbrasoft.gnuob.generic.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.velocity.context.Context;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.content.contexts.ContextVisitor;

import de.rtner.security.auth.spi.SimplePBKDF2;

@Cacheable(value = true)
@Entity(name = User.ENTITY)
@Table(name = User.TABLE)
@XmlRootElement(name = User.ENTITY)
public class User extends Access {

   private static final long serialVersionUID = 2439569681567208145L;
   protected static final String ENTITY = "User";

   protected static final String TABLE = "GNUOB_USERS";

   @ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
   private Set<Site> sites = new HashSet<Site>();

   @ManyToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
   private Set<Group> groups = new HashSet<Group>();

   @Column(name = "ROOT")
   private Boolean root;

   @Column(name = "ACCESS", nullable = false)
   @Enumerated(EnumType.STRING)
   private Rule access;

   @Column(name = "NAME", nullable = false, unique = true)
   private String name;

   @Column(name = "PASSWORD", nullable = false)
   private String password;

   @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
   @JoinTable(name = "GNUOB_ROLES", joinColumns = @JoinColumn(name = "GNUOB_USERS_ID"))
   @Column(name = "ROLE", nullable = false)
   @Enumerated(EnumType.STRING)
   private Set<Role> roles;

   @Column(name = "DESCRIPTION")
   private String description;

   public User() {

   }

   public User(String name) {
      this.name = name;
   }

   @Override
   public Context accept(ContextVisitor visitor) {
      return visitor.visit(this);
   }

   public Rule getAccess() {
      return access;
   }

   @XmlElement(name = "description")
   public String getDescription() {
      return description;
   }

   public Set<Group> getGroups() {
      return groups;
   }

   @XmlElement(name = "name", required = true)
   public String getName() {
      return name;
   }

   @XmlElement(name = "password", required = true)
   public String getPassword() {
      return password;
   }

   public Set<Role> getRoles() {
      return roles;
   }

   @XmlTransient
   public Boolean getRoot() {
      return root;
   }

   public Set<Site> getSites() {
      return sites;
   }

   @Override
   public void prePersist() {
      if (!(password.length() == 62 && password.matches("^[0-9A-F]{16}:\\d{4}:[0-9A-F]{40}"))) {
         throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't contain a valid password, verify that the given password is valid", name));
      }
   }

   @Override
   public void preUpdate() {
      if (!(password.length() == 62 && password.matches("^[0-9A-F]{16}:\\d{4}:[0-9A-F]{40}"))) {
         throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't contain a valid password, verify that the given password is valid", name));
      }
   }

   public void setAccess(Rule access) {
      this.access = access;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setGroups(Set<Group> groups) {
      this.groups = groups;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setPassword(String password) {
      this.password = new SimplePBKDF2().deriveKeyFormatted(password);
   }

   public void setRoles(Set<Role> roles) {
      this.roles = roles;
   }

   public void setRoot(Boolean root) {
      this.root = root;
   }

   public void setSites(Set<Site> sites) {
      this.sites = sites;
   }
}
