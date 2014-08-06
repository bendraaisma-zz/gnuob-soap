package com.netbrasoft.gnuob.generic.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.netbrasoft.gnuob.generic.Type;

@Entity(name = Permission.ENTITY)
@Table(name = Permission.TABLE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = Permission.ENTITY)
public class Permission extends Type {

	private static final long serialVersionUID = 3108374497171836688L;
	protected static final String ENTITY = "Permission";
	protected static final String TABLE = "GNUOB_PERMISSIONS";

	@Column(name = "ROOT", nullable = false)
	@Enumerated(EnumType.STRING)
	private Rule root = Rule.DELETE_ACCESS;

	@Column(name = "\"GROUP\"", nullable = false)
	@Enumerated(EnumType.STRING)
	private Rule group = Rule.DELETE_ACCESS;

	@Column(name = "\"USER\"", nullable = false)
	@Enumerated(EnumType.STRING)
	private Rule user = Rule.DELETE_ACCESS;

	@XmlElement(name = "group", required = true)
	public Rule getGroup() {
		return group;
	}

	@XmlElement(name = "root", required = true)
	public Rule getRoot() {
		return root;
	}

	@XmlElement(name = "user", required = true)
	public Rule getUser() {
		return user;
	}

	public void setGroup(Rule group) {
		this.group = group;
	}

	public void setRoot(Rule root) {
		this.root = root;
	}

	public void setUser(Rule user) {
		this.user = user;
	}

}
