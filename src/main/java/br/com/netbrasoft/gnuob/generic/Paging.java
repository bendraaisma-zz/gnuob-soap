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

package br.com.netbrasoft.gnuob.generic;

import static br.com.netbrasoft.gnuob.generic.JaxRsActivator.mapper;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAGING_NAME;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ZERO;
import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.databind.JsonMappingException;

@XmlRootElement(name = PAGING_NAME)
public class Paging {

  private int first;
  private int max;

  public Paging() {
    this(ZERO, ZERO);
  }

  public Paging(String json)
      throws JsonMappingException, IOException, IllegalAccessException, InvocationTargetException {
    copyProperties(this, mapper.readValue(json, Paging.class));
  }

  public static Paging getInstance(String json)
      throws JsonMappingException, IOException, IllegalAccessException, InvocationTargetException {
    return new Paging(json);
  }

  private Paging(final int first, final int max) {
    this.first = first;
    this.max = max;
  }

  public static Paging getInstance(final int first, final int max) {
    return new Paging(first, max);
  }

  @XmlElement
  public int getFirst() {
    return first;
  }

  public void setFirst(final int first) {
    this.first = first;
  }

  @XmlElement
  public int getMax() {
    return max;
  }

  public void setMax(final int max) {
    this.max = max;
  }

  @Override
  public String toString() {
    return reflectionToString(this, SHORT_PREFIX_STYLE);
  }
}
