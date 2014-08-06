package com.netbrasoft.gnuob.generic;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Paging")
public final class Paging {

	public static Paging getInstance(int first, int max) {
		return new Paging(first, max);
	}

	private int first;
	private int max;

	public Paging() {

	}

	public Paging(int first, int max) {
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

	public void setFirst(int first) {
		this.first = first;
	}

	public void setMax(int max) {
		this.max = max;
	}
}
