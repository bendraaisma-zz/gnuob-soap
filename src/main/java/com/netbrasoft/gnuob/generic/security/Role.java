package com.netbrasoft.gnuob.generic.security;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Role")
public enum Role {
  ADMINISTRATOR, MANAGER, EMPLOYEE, GUEST;
}
