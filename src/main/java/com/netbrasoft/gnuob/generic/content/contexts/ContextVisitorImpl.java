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

@Stateless(name = "ContextVisitorImpl")
@Interceptors(value = { AppSimonInterceptor.class })
public class ContextVisitorImpl implements ContextVisitor {

   @Override
   public Context visit(Category category) {
      final VelocityContext context = new VelocityContext();
      context.put("category", category);
      return context;
   }

   @Override
   public Context visit(Content content) {
      final VelocityContext context = new VelocityContext();
      context.put("content", content);
      return context;
   }

   @Override
   public Context visit(Contract contract) {
      final VelocityContext context = new VelocityContext();
      context.put("contract", contract);
      return context;
   }

   @Override
   public Context visit(Customer customer) {
      final VelocityContext context = new VelocityContext();
      context.put("customer", customer);
      return context;
   }

   @Override
   public Context visit(Group group) {
      final VelocityContext context = new VelocityContext();
      context.put("group", group);
      return context;
   }

   @Override
   public Context visit(Offer offer) {
      final VelocityContext context = new VelocityContext();
      context.put("offer", offer);
      return context;
   }

   @Override
   public Context visit(Order order) {
      final VelocityContext context = new VelocityContext();
      context.put("order", order);
      return context;
   }

   @Override
   public Context visit(Product product) {
      final VelocityContext context = new VelocityContext();
      context.put("product", product);
      return context;
   }

   @Override
   public Context visit(Setting setting) {
      final VelocityContext context = new VelocityContext();
      context.put("setting", setting);
      return context;
   }

   @Override
   public Context visit(Site site) {
      final VelocityContext context = new VelocityContext();
      context.put("site", site);
      return context;
   }

   @Override
   public Context visit(User user) {
      final VelocityContext context = new VelocityContext();
      context.put("user", user);
      return context;
   }
}
