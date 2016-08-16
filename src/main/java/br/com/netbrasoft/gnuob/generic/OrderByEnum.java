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

import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ADMIN_NAME_1_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.AMOUNT_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.BESTSELLERS_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CONTRACT_ID_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.COUNTRY_CODE_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CREATION_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DESCRIPTION_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.DISCOUNT_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIRST_NAME_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.LAST_NAME_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.LATESTCOLLECTION_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MODIFICATION_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.NAME_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.NUMBER_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.OFFER_ID_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_ID_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PLACE_NAME_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.POSITION_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.POSTAL_CODE_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.RATING_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.RECOMMENDED_PROPERTY;
import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.VALUE_PROPERTY;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@XmlType(name = "orderBy")
public enum OrderByEnum {

  // @formatter:off
  RECOMMENDED(OrderEnum.DESC, RECOMMENDED_PROPERTY),
  HIGHEST_DISCOUNT(OrderEnum.DESC, DISCOUNT_PROPERTY),
  LOWEST_DISCOUNT(OrderEnum.ASC, DISCOUNT_PROPERTY),
  HIGHEST_PRICE(OrderEnum.DESC, AMOUNT_PROPERTY),
  LOWEST_PRICE(OrderEnum.ASC, AMOUNT_PROPERTY),
  HIGHEST_RATING(OrderEnum.DESC, RATING_PROPERTY),
  LOWEST_RATING(OrderEnum.ASC, RATING_PROPERTY),
  BESTSELLERS(OrderEnum.DESC, BESTSELLERS_PROPERTY),
  LATEST_COLLECTION(OrderEnum.DESC, LATESTCOLLECTION_PROPERTY),
  NAME_A_Z(OrderEnum.ASC, NAME_PROPERTY),
  NAME_Z_A(OrderEnum.DESC, NAME_PROPERTY),
  DESCRIPTION_A_Z(OrderEnum.ASC, DESCRIPTION_PROPERTY),
  DESCRIPTION_Z_A(OrderEnum.DESC, DESCRIPTION_PROPERTY),
  PROPERTY_A_Z(OrderEnum.ASC, PROPERTY),
  PROPERTY_Z_A(OrderEnum.DESC, PROPERTY),
  PLACE_NAME_A_Z(OrderEnum.ASC, PLACE_NAME_PROPERTY),
  PLACE_NAME_Z_A(OrderEnum.DESC, PLACE_NAME_PROPERTY),
  COUNTRY_CODE_A_Z(OrderEnum.ASC, COUNTRY_CODE_PROPERTY),
  COUNTRY_CODE_Z_A(OrderEnum.DESC, COUNTRY_CODE_PROPERTY),
  POSTAL_CODE_A_Z(OrderEnum.ASC, POSTAL_CODE_PROPERTY),
  POSTAL_CODE_Z_A(OrderEnum.DESC, POSTAL_CODE_PROPERTY),
  STATE_NAME_A_Z(OrderEnum.ASC, ADMIN_NAME_1_PROPERTY),
  STATE_NAME_Z_A(OrderEnum.DESC, ADMIN_NAME_1_PROPERTY),
  VALUE_A_Z(OrderEnum.ASC, VALUE_PROPERTY),
  VALUE_Z_A(OrderEnum.DESC, VALUE_PROPERTY),
  POSITION_A_Z(OrderEnum.ASC, POSITION_PROPERTY),
  POSITION_Z_A(OrderEnum.DESC, POSITION_PROPERTY),
  NONE(OrderEnum.NONE, EMPTY),
  FIRST_NAME_A_Z(OrderEnum.ASC, FIRST_NAME_PROPERTY),
  FIRST_NAME_Z_A(OrderEnum.DESC, FIRST_NAME_PROPERTY),
  LAST_NAME_A_Z(OrderEnum.ASC, LAST_NAME_PROPERTY),
  LAST_NAME_Z_A(OrderEnum.DESC, LAST_NAME_PROPERTY),
  CONTRACT_ID_A_Z(OrderEnum.ASC, CONTRACT_ID_PROPERTY),
  CONTRACT_ID_Z_A(OrderEnum.DESC, CONTRACT_ID_PROPERTY),
  ORDER_ID_A_Z(OrderEnum.ASC, ORDER_ID_PROPERTY),
  ORDER_ID_Z_A(OrderEnum.DESC, ORDER_ID_PROPERTY),
  OFFER_ID_A_Z(OrderEnum.ASC, OFFER_ID_PROPERTY),
  OFFER_ID_Z_A(OrderEnum.DESC, OFFER_ID_PROPERTY),
  NUMBER_A_Z(OrderEnum.ASC, NUMBER_PROPERTY),
  NUMBER_Z_A(OrderEnum.DESC, NUMBER_PROPERTY),
  CREATION_A_Z(OrderEnum.ASC, CREATION_PROPERTY),
  CREATION_Z_A(OrderEnum.DESC, CREATION_PROPERTY),
  MODIFICATION_A_Z(OrderEnum.ASC, MODIFICATION_PROPERTY),
  MODIFICATION_Z_A(OrderEnum.DESC, MODIFICATION_PROPERTY);
  // @formatter:on

  public enum OrderEnum {
    DESC, ASC, NONE;
  }

  private OrderEnum order;
  private String column;
  private String associationPath;
  private String alias;

  private OrderByEnum(final OrderEnum order, final String column) {
    this.order = order;
    this.column = column;
  }

  private OrderByEnum(final OrderEnum order, final String column, String associationPath, String alias) {
    this(order, column);
    this.associationPath = associationPath;
    this.alias = alias;
  }

  public String getColumn() {
    return column;
  }

  public OrderEnum getOrder() {
    return order;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this, SHORT_PREFIX_STYLE).toString();
  }

  public String getAssociationPath() {
    return associationPath;
  }

  public String getAlias() {
    return alias;
  }
}
