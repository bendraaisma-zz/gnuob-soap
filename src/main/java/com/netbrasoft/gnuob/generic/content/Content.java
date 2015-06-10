package com.netbrasoft.gnuob.generic.content;

import java.util.Base64;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.netbrasoft.gnuob.generic.security.Access;

@Cacheable(value = true)
@Entity(name = Content.ENTITY)
@Table(name = Content.TABLE)
@XmlRootElement(name = Content.ENTITY)
public class Content extends Access {

   private static final long serialVersionUID = -6963744731098668340L;
   protected static final String ENTITY = "Content";
   protected static final String TABLE = "GNUOB_CONTENTS";

   @Column(name = "POSITION")
   private Integer position = 0;

   @Column(name = "NAME", nullable = false)
   private String name;

   @Column(name = "FORMAT", nullable = false)
   private String format;

   @Column(name = "CONTENT", columnDefinition = "MEDIUMBLOB", nullable = false)
   private byte[] data;

   @XmlElement(name = "content", required = true)
   @XmlMimeType("application/octet-stream")
   public byte[] getData() {
      return Base64.getDecoder().decode(data);
   }

   @XmlElement(name = "format", required = true)
   public String getFormat() {
      return format;
   }

   @XmlElement(name = "name", required = true)
   public String getName() {
      return name;
   }

   @XmlTransient
   public Integer getPosition() {
      return position;
   }

   public void setData(byte[] data) {
      this.data = Base64.getEncoder().encode(data);
   }

   public void setFormat(String format) {
      this.format = format;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setPosition(Integer position) {
      this.position = position;
   }
}
