package com.netbrasoft.gnuob.generic.category;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.netbrasoft.gnuob.generic.content.Content;
import com.netbrasoft.gnuob.generic.security.Access;

@Entity(name = Category.ENTITY)
@Table(name = Category.TABLE)
@XmlRootElement(name = Category.ENTITY)
public class Category extends Access {

	private static final long serialVersionUID = 8531470310780646179L;
	protected static final String ENTITY = "Category";
	protected static final String TABLE = "GNUOB_CATEGORIES";

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "DESCRIPTION", nullable = false)
	private String description;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@OrderBy("position asc")
	private Set<SubCategory> subCategories = new LinkedHashSet<SubCategory>();

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@OrderBy("position asc")
	private Set<Content> contents = new LinkedHashSet<Content>();

	@Column(name = "POSITION")
	private Integer position;

	public Set<Content> getContents() {
		return contents;
	}

	@XmlElement(name = "description")
	public String getDescription() {
		return description;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	@XmlTransient
	public Integer getPosition() {
		return position;
	}

	public Set<SubCategory> getSubCategories() {
		return subCategories;
	}

	private void positionContents() {
		int position = 0;

		for (Content content : contents) {
			content.setPosition(Integer.valueOf(position++));
		}
	}

	private void positionSubCategories() {
		int position = 0;

		for (SubCategory subCategory : subCategories) {
			subCategory.setPosition(Integer.valueOf(position++));
		}
	}

	@PrePersist
	@PreUpdate
	protected void prePersistUpdateCategory() {
		positionSubCategories();
		positionContents();
	}

	public void setContents(Set<Content> contents) {
		this.contents = contents;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public void setSubCategories(Set<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}
}
