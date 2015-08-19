package com.netbrasoft.gnuob.generic.content.contexts;

import org.apache.velocity.context.Context;

public interface ContextElement {
   Context accept(ContextVisitor visitor);
}
