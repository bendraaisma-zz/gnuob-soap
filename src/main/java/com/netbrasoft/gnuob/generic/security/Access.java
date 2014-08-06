package com.netbrasoft.gnuob.generic.security;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.netbrasoft.gnuob.generic.Type;

@Entity(name = Access.ENTITY)
@Table(name = Access.TABLE)
@Inheritance(strategy = InheritanceType.JOINED)
//@formatter:off
@FilterDefs({
		@FilterDef(name = Access.NFQ1, parameters = @ParamDef(name = "userId", type = "long")),
		@FilterDef(name = Access.NFQ2, parameters = @ParamDef(name = "siteId", type = "long")),
		@FilterDef(name = Access.NFQ3, parameters = @ParamDef(name = "active", type = "boolean")),
		@FilterDef(name = Access.NFQ4, parameters = @ParamDef(name = "permission", type = "int")) })
@Filters({
		@Filter(// Select user id based on user association if the user has
				// access.
		name = Access.NFQ1, condition = "(user_ID = (SELECT GNUOB_USERS.ID FROM GNUOB_USERS "
				+ " WHERE GNUOB_USERS.RULE != 'NO_ACCESS' "
				+ " AND GNUOB_USERS.ID = :userId) "
				// Or select group id's based on the group associations where
				// the user has access to.
				+ " OR group_ID IN(SELECT GNUOB_GROUPS.ID FROM GNUOB_GROUPS "
				+ "	INNER JOIN GNUOB_USERS_GNUOB_GROUPS "
				+ "	ON GNUOB_GROUPS.ID = GNUOB_USERS_GNUOB_GROUPS.groups_ID "
				+ "	INNER JOIN GNUOB_USERS "
				+ "	ON GNUOB_USERS_GNUOB_GROUPS.GNUOB_USERS_ID = GNUOB_USERS.ID "
				+ "	AND GNUOB_USERS.RULE != 'NO_ACCESS' "
				+ " AND GNUOB_USERS.ID = :userId))"),
		@Filter(name = Access.NFQ2, condition = "site_ID = :siteId"),
		@Filter(name = Access.NFQ3, condition = "active = :active"),
		// Test permission if user or group, or root has no 'NO_ACCESS' Rule?
		@Filter(name = Access.NFQ4, condition = "permission_ID IN ( "
				+ "	SELECT GNUOB_PERMISSIONS.ID "
				+ " FROM GNUOB_PERMISSIONS "
				+ " WHERE (GNUOB_PERMISSIONS.USER != 'NO_ACCESS' AND 1 = :permission) "
				+ "	OR (GNUOB_PERMISSIONS.GROUP != 'NO_ACCESS' AND 2 = :permission) "
				+ "	OR (GNUOB_PERMISSIONS.ROOT != 'NO_ACCESS' AND 3 = :permission))") })
//@formatter:on
public abstract class Access extends Type {

	private static final long serialVersionUID = 6374088000216811805L;
	protected static final String ENTITY = "Access";
	public static final String TABLE = "GNUOB_ACCESS";

	public static final String NFQ1 = "filterByUserIdOrGroupIds";
	public static final String NFQ2 = "filterBySiteId";
	public static final String NFQ3 = "filterByActive";
	public static final String NFQ4 = "filterByPermission";

	@ManyToOne(cascade = { CascadeType.PERSIST }, optional = false)
	private Site site;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, optional = false)
	private Permission permission;

	@ManyToOne(cascade = { CascadeType.PERSIST }, optional = false)
	private User user;

	@ManyToOne(cascade = { CascadeType.PERSIST }, optional = false)
	private Group group;

	@Column(name = "ACTIVE", nullable = false)
	private Boolean active = true;

	@XmlTransient
	public Boolean getActive() {
		return active;
	}

	@XmlTransient
	public Group getGroup() {
		return group;
	}

	@XmlTransient
	public Permission getPermission() {
		return permission;
	}

	@XmlTransient
	public Site getSite() {
		return site;
	}

	@XmlTransient
	public User getUser() {
		return user;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
