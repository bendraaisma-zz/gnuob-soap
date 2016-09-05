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

package br.com.netbrasoft.gnuob.generic.setting;

import static br.com.netbrasoft.gnuob.generic.JaxRsActivator.mapper;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DESCRIPTION_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PROPERTY_COLUMN_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SETTING_ENTITY_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SETTING_TABLE_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.VALUE_COLUMN_NAME;
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
import br.com.netbrasoft.gnuob.generic.security.AbstractAccess;

@Cacheable(value = true)
@Entity(name = SETTING_ENTITY_NAME)
@Table(name = SETTING_TABLE_NAME)
@XmlRootElement(name = SETTING_ENTITY_NAME)
public class Setting extends AbstractAccess {

  private static final long serialVersionUID = -1489369413428188989L;

  private String description;
  private String property;
  private String value;

  public Setting() {
    super();
  }

  public Setting(String json) throws IOException, IllegalAccessException, InvocationTargetException {
    copyProperties(this, mapper.readValue(json, Setting.class));
  }

  public static Setting getInstance() {
    return new Setting();
  }

  public static Setting getInstanceByJson(String json) throws IllegalAccessException, InvocationTargetException, IOException {
    return new Setting(json);
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
  @Column(name = DESCRIPTION_COLUMN_NAME, nullable = false)
  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = PROPERTY_COLUMN_NAME, nullable = false)
  public String getProperty() {
    return property;
  }

  public void setProperty(final String property) {
    this.property = property;
  }

  @JsonProperty(required = true)
  @XmlElement(required = true)
  @Column(name = VALUE_COLUMN_NAME, nullable = false)
  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }
}
