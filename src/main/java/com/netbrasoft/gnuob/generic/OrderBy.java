package com.netbrasoft.gnuob.generic;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OrderBy")
public enum OrderBy {

    // @formatter:off
    RECOMMENDED(Order.DESC, "recommended"),
    HIGHEST_DISCOUNT(Order.DESC, "discount"),
    LOWEST_DISCOUNT(Order.ASC, "discount"),
    HIGHEST_PRICE(Order.DESC, "amount"),
    LOWEST_PRICE(Order.ASC, "amount"),
    HIGHEST_RATING(Order.DESC, "rating"),
    LOWEST_RATING(Order.ASC, "rating"),
    BESTSELLERS(Order.DESC, "bestsellers"),
    LATEST_COLLECTION(Order.DESC, "latestCollection"),
    TITLE_A_Z(Order.ASC, "name"),
    TITLE_Z_A(Order.DESC, "name"),
    POSITION_A_Z(Order.ASC, "position"),
    NONE(Order.NONE, "");
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
