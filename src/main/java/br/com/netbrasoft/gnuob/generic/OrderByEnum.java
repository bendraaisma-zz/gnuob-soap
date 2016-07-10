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

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@XmlType(name = "orderBy")
public enum OrderByEnum {

  // @formatter:off
  RECOMMENDED(OrderEnum.DESC, "recommended"),
  HIGHEST_DISCOUNT(OrderEnum.DESC, "discount"),
  LOWEST_DISCOUNT(OrderEnum.ASC, "discount"),
  HIGHEST_PRICE(OrderEnum.DESC, "amount"),
  LOWEST_PRICE(OrderEnum.ASC, "amount"),
  HIGHEST_RATING(OrderEnum.DESC, "rating"),
  LOWEST_RATING(OrderEnum.ASC, "rating"),
  BESTSELLERS(OrderEnum.DESC, "bestsellers"),
  LATEST_COLLECTION(OrderEnum.DESC, "latestCollection"),
  NAME_A_Z(OrderEnum.ASC, "name"),
  NAME_Z_A(OrderEnum.DESC, "name"),
  DESCRIPTION_A_Z(OrderEnum.ASC, "description"),
  DESCRIPTION_Z_A(OrderEnum.DESC, "description"),
  PROPERTY_A_Z(OrderEnum.ASC, "property"),
  PROPERTY_Z_A(OrderEnum.DESC, "property"),
  PLACE_NAME_A_Z(OrderEnum.ASC, "placeName"),
  PLACE_NAME_Z_A(OrderEnum.DESC, "placeName"),
  COUNTRY_CODE_A_Z(OrderEnum.ASC, "countryCode"),
  COUNTRY_CODE_Z_A(OrderEnum.DESC, "countryCode"),
  POSTAL_CODE_A_Z(OrderEnum.ASC, "postalCode"),
  POSTAL_CODE_Z_A(OrderEnum.DESC, "postalCode"),
  STATE_NAME_A_Z(OrderEnum.ASC, "adminName1"),
  STATE_NAME_Z_A(OrderEnum.DESC, "adminName1"),
  VALUE_A_Z(OrderEnum.ASC, "value"),
  VALUE_Z_A(OrderEnum.DESC, "value"),
  POSITION_A_Z(OrderEnum.ASC, "position"),
  POSITION_Z_A(OrderEnum.DESC, "position"),
  NONE(OrderEnum.NONE, ""),
  FIRST_NAME_A_Z(OrderEnum.ASC, "firstName"),
  FIRST_NAME_Z_A(OrderEnum.DESC, "firstName"),
  LAST_NAME_A_Z(OrderEnum.ASC, "lastName"),
  LAST_NAME_Z_A(OrderEnum.DESC, "lastName"),
  CONTRACT_ID_A_Z(OrderEnum.ASC, "contractId"),
  CONTRACT_ID_Z_A(OrderEnum.DESC, "contractId"),
  ORDER_ID_A_Z(OrderEnum.ASC, "orderId"),
  ORDER_ID_Z_A(OrderEnum.DESC, "orderId"),
  OFFER_ID_A_Z(OrderEnum.ASC, "offerId"),
  OFFER_ID_Z_A(OrderEnum.DESC, "offerId"),
  NUMBER_A_Z(OrderEnum.ASC, "number"),
  NUMBER_Z_A(OrderEnum.DESC, "number"),
  CREATION_A_Z(OrderEnum.ASC, "creation"),
  CREATION_Z_A(OrderEnum.DESC, "creation"),
  MODIFICATION_A_Z(OrderEnum.ASC, "modification"),
  MODIFICATION_Z_A(OrderEnum.DESC, "modification");
  // @formatter:on

  public enum OrderEnum {
    DESC, ASC, NONE;
  }

  private OrderEnum order;
  private String column;

  private OrderByEnum(final OrderEnum order, final String column) {
    this.order = order;
    this.column = column;
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
}
