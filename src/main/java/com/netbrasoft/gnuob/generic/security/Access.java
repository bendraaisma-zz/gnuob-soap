package com.netbrasoft.gnuob.generic.security;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
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
    @FilterDef(name = Access.NFQ2, parameters = @ParamDef(name = "siteId", type = "long")) })
@Filters({
    @Filter(
            // Select based on user ownership.
            name = Access.NFQ1, condition = "((owner_ID = (SELECT GNUOB_USERS.ID FROM GNUOB_USERS "
                    + " WHERE GNUOB_USERS.ID = :userId) "
                    + " AND (SELECT GNUOB_PERMISSIONS.ID FROM GNUOB_PERMISSIONS "
                    + " WHERE GNUOB_PERMISSIONS.ID = permission_ID AND GNUOB_PERMISSIONS.OWNER != 'NONE_ACCESS')) "
                    // Or select based on group ownership.
                    + " OR (group_ID IN(SELECT GNUOB_GROUPS.ID FROM GNUOB_GROUPS "
                    + "	INNER JOIN GNUOB_USERS_GNUOB_GROUPS "
                    + "	ON GNUOB_GROUPS.ID = GNUOB_USERS_GNUOB_GROUPS.groups_ID "
                    + "	INNER JOIN GNUOB_USERS "
                    + "	ON GNUOB_USERS_GNUOB_GROUPS.GNUOB_USERS_ID = GNUOB_USERS.ID "
                    + " AND GNUOB_USERS.ID = :userId) "
                    + " AND (SELECT GNUOB_PERMISSIONS.ID FROM GNUOB_PERMISSIONS "
                    + " WHERE GNUOB_PERMISSIONS.ID = permission_ID AND GNUOB_PERMISSIONS.GROUP != 'NONE_ACCESS')) "
                    // Or select based on other ownership.
                    + " OR (SELECT GNUOB_PERMISSIONS.ID FROM GNUOB_PERMISSIONS "
                    + " WHERE GNUOB_PERMISSIONS.ID = permission_ID AND GNUOB_PERMISSIONS.OTHERS != 'NONE_ACCESS')) "),
                    @Filter(name = Access.NFQ2, condition = "site_ID = :siteId") })
//@formatter:on
public abstract class Access extends Type {

    private static final long serialVersionUID = 6374088000216811805L;
    protected static final String ENTITY = "Access";
    public static final String TABLE = "GNUOB_ACCESS";

    public static final String NFQ1 = "filterByUserIdOrGroupIdsOrOtherIds";
    public static final String NFQ2 = "filterBySiteId";

    @ManyToOne(cascade = { CascadeType.PERSIST }, optional = false)
    private Site site;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, optional = false)
    private Permission permission;

    @ManyToOne(cascade = { CascadeType.PERSIST }, optional = false)
    private User owner;

    @ManyToOne(cascade = { CascadeType.PERSIST }, optional = false)
    private Group group;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = true;

    @XmlElement(name = "active", required = true)
    public Boolean getActive() {
        return active;
    }

    @XmlTransient
    public Group getGroup() {
        return group;
    }

    @XmlTransient
    public User getOwner() {
        return owner;
    }

    public Permission getPermission() {
        return permission;
    }

    @XmlTransient
    public Site getSite() {
        return site;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
