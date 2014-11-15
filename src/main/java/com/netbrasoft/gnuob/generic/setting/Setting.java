package com.netbrasoft.gnuob.generic.setting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.netbrasoft.gnuob.generic.security.Access;

@Entity(name = Setting.ENTITY)
@Table(name = Setting.TABLE)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = Setting.ENTITY)
public class Setting extends Access {

    private static final long serialVersionUID = -1489369413428188989L;
    protected static final String ENTITY = "Setting";
    protected static final String TABLE = "GNUOB_SETTINGS";

    @Column(name = "PROPERTY", nullable = false)
    @XmlElement(name = "property", required = true)
    private String property;

    @Column(name = "VALUE", nullable = false)
    @XmlElement(name = "value", required = true)
    private String value;

    @Column(name = "DESCRIPTION", nullable = false, columnDefinition = "TEXT")
    @XmlElement(name = "description")
    private String description;

    public String getDescription() {
        return description;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
