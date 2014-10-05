package com.netbrasoft.gnuob.generic;

public final class Parameter {

    public static Parameter getInstance(String name, Object value) {
        return new Parameter(name, value);
    }

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
