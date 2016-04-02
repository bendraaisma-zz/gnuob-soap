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

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ACCESS_ENTITY_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ACCESS_TABLE_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ACTIVE_COLUMN_NAME;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.netbrasoft.gnuob.generic.AbstractType;
import com.netbrasoft.gnuob.generic.content.contexts.IContextElement;

@Cacheable(value = true)
@Entity(name = ACCESS_ENTITY_NAME)
@Table(name = ACCESS_TABLE_NAME)
@Inheritance(strategy = InheritanceType.JOINED)
// @formatter:off
@FilterDefs({@FilterDef(name = AbstractAccess.NFQ1, parameters = @ParamDef(name = "userId", type = "long") ),
    @FilterDef(name = AbstractAccess.NFQ2, parameters = @ParamDef(name = "siteId", type = "long") )})
@Filters({@Filter(
    // Select based on user ownership.
    name = AbstractAccess.NFQ1, condition = "((owner_ID = (SELECT GNUOB_USERS.ID FROM GNUOB_USERS "
        + " WHERE GNUOB_USERS.ID = :userId) "
        + " AND (SELECT GNUOB_PERMISSIONS.ID FROM GNUOB_PERMISSIONS "
        + " WHERE GNUOB_PERMISSIONS.ID = permission_ID AND GNUOB_PERMISSIONS.OWNER != 'NONE_ACCESS')) "
        // Or select based on group ownership.
        + " OR (group_ID IN(SELECT GNUOB_GROUPS.ID FROM GNUOB_GROUPS "
        + "	INNER JOIN GNUOB_USERS_GNUOB_GROUPS "
        + "	ON GNUOB_GROUPS.ID = GNUOB_USERS_GNUOB_GROUPS.groups_ID "
        + "	INNER JOIN GNUOB_USERS "
        + "	ON GNUOB_USERS_GNUOB_GROUPS.GNUOB_USERS_ID = GNUOB_USERS.ID "
        + " AND GNUOB_USERS.ID = :userId) "
        + " AND (SELECT GNUOB_PERMISSIONS.ID FROM GNUOB_PERMISSIONS "
        + " WHERE GNUOB_PERMISSIONS.ID = permission_ID AND GNUOB_PERMISSIONS.GROUP != 'NONE_ACCESS')) "
        // Or select based on other ownership.
        + " OR (SELECT GNUOB_PERMISSIONS.ID FROM GNUOB_PERMISSIONS "
        + " WHERE GNUOB_PERMISSIONS.ID = permission_ID AND GNUOB_PERMISSIONS.OTHERS != 'NONE_ACCESS')) "),
    @Filter(name = AbstractAccess.NFQ2, condition = "site_ID = :siteId")})
// @formatter:on
public abstract class AbstractAccess extends AbstractType implements IContextElement {

  public static final String NFQ1 = "filterByUserIdOrGroupIdsOrOtherIds";
  public static final String NFQ2 = "filterBySiteId";

  private static final long serialVersionUID = 6374088000216811805L;

  private Boolean active = true;
  private Group group;
  private User owner;
  private Permission permission;
  private Site site;

  @XmlElement(required = true/* , defaultValue = "true" */)
  @Column(name = ACTIVE_COLUMN_NAME, nullable = false)
  public Boolean getActive() {
    return active;
  }

  @XmlTransient
  @ManyToOne(cascade = {CascadeType.PERSIST}, optional = false)
  public Group getGroup() {
    return group;
  }

  @XmlTransient
  @ManyToOne(cascade = {CascadeType.PERSIST}, optional = false)
  public User getOwner() {
    return owner;
  }

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, optional = false)
  public Permission getPermission() {
    return permission;
  }

  @XmlTransient
  @ManyToOne(cascade = {CascadeType.PERSIST}, optional = false)
  public Site getSite() {
    return site;
  }

  public void setActive(final Boolean active) {
    this.active = active;
  }

  public void setGroup(final Group group) {
    this.group = group;
  }

  public void setOwner(final User owner) {
    this.owner = owner;
  }

  public void setPermission(final Permission permission) {
    this.permission = permission;
  }

  public void setSite(final Site site) {
    this.site = site;
  }
}
