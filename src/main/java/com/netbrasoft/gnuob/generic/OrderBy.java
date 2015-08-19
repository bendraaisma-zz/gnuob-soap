package com.netbrasoft.gnuob.generic;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderBy")
public enum OrderBy {

   // @formatter:off
   RECOMMENDED(Order.DESC, "recommended"), HIGHEST_DISCOUNT(Order.DESC, "discount"), LOWEST_DISCOUNT(Order.ASC,"discount"),
   HIGHEST_PRICE(Order.DESC, "amount"), LOWEST_PRICE(Order.ASC, "amount"), HIGHEST_RATING(Order.DESC, "rating"),
   LOWEST_RATING(Order.ASC, "rating"), BESTSELLERS(Order.DESC, "bestsellers"), LATEST_COLLECTION(Order.DESC, "latestCollection"),
   TITLE_A_Z(Order.ASC, "name"), TITLE_Z_A(Order.DESC, "name"), POSITION_A_Z(Order.ASC, "position"), NONE(Order.NONE, ""),
   FIRST_NAME_A_Z(Order.ASC, "firstName"), FIRST_NAME_Z_A(Order.DESC, "firstName"), LAST_NAME_A_Z(Order.ASC, "lastName"),
   LAST_NAME_Z_A(Order.DESC, "lastName"), CONTRACT_ID_A_Z(Order.ASC, "contractId"), CONTRACT_ID_Z_A(Order.DESC, "contractId"),
   ORDER_ID_A_Z(Order.ASC, "orderId"), ORDER_ID_Z_A(Order.DESC, "orderId"),  OFFER_ID_A_Z(Order.ASC, "offerId"),
   OFFER_ID_Z_A(Order.DESC, "offerId"), NUMBER_A_Z(Order.ASC, "number"), NUMBER_Z_A(Order.DESC, "number"),
   CREATION_A_Z(Order.ASC, "creation"), CREATION_Z_A(Order.DESC, "creation"), MODIFICATION_A_Z(Order.ASC, "modification"),
   MODIFICATION_Z_A(Order.DESC, "modification");
   // @formatter:on

   public enum Order {
      DESC, ASC, NONE;
   }

   private Order filter;
   private String column;

   private OrderBy(Order filter, String column) {
      this.filter = filter;
      this.column = column;
   }

   public String getColumn() {
      return column;
   }

   public Order getOrderBy() {
      return filter;
   }

   public void setColumn(String column) {
      this.column = column;
   }

   public void setFilter(Order filter) {
      this.filter = filter;
   }
}
