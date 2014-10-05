package com.netbrasoft.gnuob.generic.security;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "metaData")
@XmlType(propOrder = { "site", "user", "password" })
public class MetaData {

    private String site;
    private String user;
    private String password;

    private final boolean enabled;

    public MetaData() {
        this(true);
    }

    public MetaData(boolean enabled) {
        this.enabled = enabled;
    }

    @XmlElement(name = "password", required = true)
    public String getPassword() {
        return password;
    }

    @XmlElement(name = "site", required = true)
    public String getSite() {
        return site;
    }

    @XmlElement(name = "user", required = true)
    public String getUser() {
        return user;
    }

    @XmlTransient
    public boolean isEnabled() {
        return enabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setUser(String user) {
        this.user = user;
    }
}