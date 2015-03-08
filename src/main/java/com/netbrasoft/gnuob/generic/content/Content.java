package com.netbrasoft.gnuob.generic.content;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.netbrasoft.gnuob.generic.security.Access;

@Entity(name = Content.ENTITY)
@Table(name = Content.TABLE)
@XmlRootElement(name = Content.ENTITY)
public class Content extends Access {

   private static final long serialVersionUID = -6963744731098668340L;
   protected static final String ENTITY = "Content";
   protected static final String TABLE = "GNUOB_CONTENTS";

   @Column(name = "POSITION")
   private Integer position;

   @Column(name = "NAME", nullable = false)
   private String name;

   @Column(name = "FORMAT", nullable = false)
   private String format;

   @Column(name = "CONTENT", columnDefinition = "MEDIUMBLOB", nullable = false)
   private byte[] content;

   @XmlElement(name = "content", required = true)
   @XmlMimeType("application/octet-stream")
   public byte[] getContent() {
      return content;
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

   public void setContent(byte[] content) {
      this.content = content;
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
