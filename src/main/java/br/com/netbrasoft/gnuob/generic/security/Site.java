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

package br.com.netbrasoft.gnuob.generic.security;

import static br.com.netbrasoft.gnuob.generic.JaxRsActivator.mapper;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DESCRIPTION_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.NAME_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SITE_ENTITY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SITE_TABLE_NAME;
import static org.apache.commons.beanutils.BeanUtils.copyProperties;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.velocity.context.Context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.netbrasoft.gnuob.generic.content.contexts.IContextVisitor;

@Cacheable(value = true)
@Table(name = SITE_TABLE_NAME)
@Entity(name = SITE_ENTITY_NAME)
@XmlRootElement(name = SITE_ENTITY_NAME)
public class Site extends AbstractAccess {

  private static final long serialVersionUID = 985676314568291633L;

  private String description;
  private String name;

  public Site() {
    super();
  }

  public Site(String json) throws IOException, IllegalAccessException, InvocationTargetException {
    copyProperties(this, mapper.readValue(json, Site.class));
  }

  public static Site getInstance() {
    return new Site();
  }

  public static Site getInstanceByJson(String json)
      throws IllegalAccessException, InvocationTargetException, IOException {
    return new Site(json);
  }

  public static Site getInstance(final String name) {
    final Site site = new Site();
    site.setName(name);
    return site;
  }

  @Override
  @JsonIgnore
  @Transient
  public boolean isDetached() {
    return isAbstractTypeDetached();
  }

  @Override
  public Context accept(final IContextVisitor visitor) {
    return visitor.visit(this);
  }

  @JsonProperty
  @XmlElement
  @Column(name = DESCRIPTION_COLUMN_NAME)
  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = NAME_COLUMN_NAME, nullable = false, unique = true)
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }
}
