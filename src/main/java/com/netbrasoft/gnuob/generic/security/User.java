package com.netbrasoft.gnuob.generic.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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

    @Column(name = "ROLE", nullable = false)
    private String role;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    public User() {

    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
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

    @XmlElement(name = "role", required = true)
    public String getRole() {
        return role;
    }

    @XmlTransient
    public Boolean getRoot() {
        return root;
    }

    public Set<Site> getSites() {
        return sites;
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
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSites(Set<Site> sites) {
        this.sites = sites;
    }
}
