package com.netbrasoft.gnuob.generic;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Paging")
public final class Paging {

  private int first;

  private int max;

  public Paging() {
    // Empty constructor.
  }

  public Paging(final int first, final int max) {
    this.first = first;
    this.max = max;
  }

  @XmlElement(name = "first")
  public int getFirst() {
    return first;
  }

  @XmlElement(name = "max")
  public int getMax() {
    return max;
  }

  public void setFirst(final int first) {
    this.first = first;
  }

  public void setMax(final int max) {
    this.max = max;
  }
}
