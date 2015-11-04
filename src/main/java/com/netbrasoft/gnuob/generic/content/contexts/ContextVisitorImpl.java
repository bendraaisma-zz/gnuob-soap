package com.netbrasoft.gnuob.generic.content.contexts;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

import com.netbrasoft.gnuob.generic.category.Category;
import com.netbrasoft.gnuob.generic.content.Content;
import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.offer.Offer;
import com.netbrasoft.gnuob.generic.order.Order;
import com.netbrasoft.gnuob.generic.product.Product;
import com.netbrasoft.gnuob.generic.security.Group;
import com.netbrasoft.gnuob.generic.security.Site;
import com.netbrasoft.gnuob.generic.security.User;
import com.netbrasoft.gnuob.generic.setting.Setting;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@Stateless(name = ContextVisitorImpl.CONTEXT_VISITOR_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class ContextVisitorImpl implements ContextVisitor {

  public static final String CONTEXT_VISITOR_IMPL_NAME = "ContextVisitorImpl";

  @Override
  public Context visit(final Category category) {
    final VelocityContext context = new VelocityContext();
    context.put("category", category);
    return context;
  }

  @Override
  public Context visit(final Content content) {
    final VelocityContext context = new VelocityContext();
    context.put("content", content);
    return context;
  }

  @Override
  public Context visit(final Contract contract) {
    final VelocityContext context = new VelocityContext();
    context.put("contract", contract);
    return context;
  }

  @Override
  public Context visit(final Customer customer) {
    final VelocityContext context = new VelocityContext();
    context.put("customer", customer);
    return context;
  }

  @Override
  public Context visit(final Group group) {
    final VelocityContext context = new VelocityContext();
    context.put("group", group);
    return context;
  }

  @Override
  public Context visit(final Offer offer) {
    final VelocityContext context = new VelocityContext();
    context.put("offer", offer);
    return context;
  }

  @Override
  public Context visit(final Order order) {
    final VelocityContext context = new VelocityContext();
    context.put("order", order);
    return context;
  }

  @Override
  public Context visit(final Product product) {
    final VelocityContext context = new VelocityContext();
    context.put("product", product);
    return context;
  }

  @Override
  public Context visit(final Setting setting) {
    final VelocityContext context = new VelocityContext();
    context.put("setting", setting);
    return context;
  }

  @Override
  public Context visit(final Site site) {
    final VelocityContext context = new VelocityContext();
    context.put("site", site);
    return context;
  }

  @Override
  public Context visit(final User user) {
    final VelocityContext context = new VelocityContext();
    context.put("user", user);
    return context;
  }
}
