package com.netbrasoft.gnuob.generic.security;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Rule")
public enum Rule {
//@formatter:off
  CREATE_ACCESS(Arrays.asList(Operation.CREATE, Operation.READ)),
  READ_ACCESS(Arrays.asList(Operation.READ)),
  UPDATE_ACCESS(Arrays.asList(Operation.CREATE, Operation.READ, Operation.UPDATE)),
  DELETE_ACCESS(Arrays.asList(Operation.CREATE, Operation.READ, Operation.UPDATE, Operation.DELETE)),
  NONE_ACCESS(Arrays.asList(Operation.NONE));
//@formatter:on

  public enum Operation {
    CREATE, READ, UPDATE, DELETE, NONE;
  }

  private List<Operation> operations;

  private Rule(final List<Operation> operations) {
    setOperations(operations);
  }

  public List<Operation> getOperations() {
    return operations;
  }

  public void setOperations(final List<Operation> operations) {
    this.operations = operations;
  }
}
