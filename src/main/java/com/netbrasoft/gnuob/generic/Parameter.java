package com.netbrasoft.gnuob.generic;

public final class Parameter {

  private final String name;
  private final Object value;

  public Parameter(String name, Object value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return this.name;
  }

  public Object getValue() {
    return this.value;
  }
}
