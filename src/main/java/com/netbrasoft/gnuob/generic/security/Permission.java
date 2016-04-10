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

package com.netbrasoft.gnuob.generic.security;

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GROUP_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.OTHERS_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.OWNER_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PERMISSION_ENTITY_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PERMISSION_TABLE_NAME;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.netbrasoft.gnuob.generic.AbstractType;

@Cacheable(value = true)
@Entity(name = PERMISSION_ENTITY_NAME)
@Table(name = PERMISSION_TABLE_NAME)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = PERMISSION_ENTITY_NAME)
public class Permission extends AbstractType {

  private static final long serialVersionUID = 3108374497171836688L;

  private Rule group = Rule.READ_ACCESS;
  private Rule others = Rule.READ_ACCESS;
  private Rule owner = Rule.DELETE_ACCESS;

  public Permission() {}

  @Override
  @Transient
  public boolean isDetached() {
    return isAbstractTypeDetached();
  }

  @Override
  public void prePersist() {
    getGroup();
    getOwner();
    getOthers();
  }

  @Override
  public void preUpdate() {
    prePersist();
  }

  @XmlElement(required = true)
  @Column(name = GROUP_COLUMN_NAME, nullable = false)
  @Enumerated(EnumType.STRING)
  public Rule getGroup() {
    return group == null ? group = Rule.READ_ACCESS : group;
  }

  @XmlElement(required = true)
  @Column(name = OTHERS_COLUMN_NAME, nullable = false)
  @Enumerated(EnumType.STRING)
  public Rule getOthers() {
    return others == null ? others = Rule.READ_ACCESS : others;
  }

  @XmlElement(required = true)
  @Column(name = OWNER_COLUMN_NAME, nullable = false)
  @Enumerated(EnumType.STRING)
  public Rule getOwner() {
    return owner == null ? owner = Rule.READ_ACCESS : owner;
  }

  public void setGroup(final Rule group) {
    this.group = group;
  }

  public void setOthers(final Rule others) {
    this.others = others;
  }

  public void setOwner(final Rule owner) {
    this.owner = owner;
  }
}
