/*
 * Copyright 2016 Netbrasoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.netbrasoft.gnuob.generic;

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CREATION_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ID_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MODIFICATION_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.VERSION_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ZERO;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@Cacheable(value = false)
@MappedSuperclass
public abstract class AbstractType implements Serializable {

  private static final long serialVersionUID = 7895247154381678321L;

  private Timestamp creation;
  private long id;
  private Timestamp modification;
  private int version;

  @Transient
  public abstract boolean isDetached();

  @Transient
  public abstract void prePersist();

  @PrePersist
  protected void prePersistType() {
    creation = new Timestamp(System.currentTimeMillis());
    modification = new Timestamp(System.currentTimeMillis());
    prePersist();
  }

  @Transient
  public abstract void preUpdate();

  @PreUpdate
  protected void preUpdateType() {
    modification = new Timestamp(System.currentTimeMillis());
    preUpdate();
  }

  @XmlTransient
  @Column(name = CREATION_COLUMN_NAME)
  public Timestamp getCreation() {
    return creation;
  }

  @XmlAttribute
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = ID_COLUMN_NAME)
  public long getId() {
    return id;
  }

  @XmlTransient
  @Column(name = MODIFICATION_COLUMN_NAME)
  public Timestamp getModification() {
    return modification;
  }

  @XmlAttribute
  @Version
  @Column(name = VERSION_COLUMN_NAME)
  public int getVersion() {
    return version;
  }

  @Transient
  public boolean isAbstractTypeDetached() {
    return id > ZERO;
  }

  public void setCreation(final Timestamp creation) {
    this.creation = creation;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public void setModification(final Timestamp modification) {
    this.modification = modification;
  }

  public void setVersion(final int version) {
    this.version = version;
  }
}
