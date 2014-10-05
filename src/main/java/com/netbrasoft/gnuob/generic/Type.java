package com.netbrasoft.gnuob.generic;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAttribute;

@MappedSuperclass
public abstract class Type implements Serializable {

    private static final long serialVersionUID = 7895247154381678321L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Version
    @Column(name = "VERSION")
    private int version;

    @XmlAttribute(name = "id", required = false)
    public long getId() {
        return id;
    }

    @XmlAttribute(name = "version", required = false)
    public int getVersion() {
        return version;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
