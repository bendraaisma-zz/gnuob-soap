package com.netbrasoft.gnuob.generic.content.contexts;

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

public interface ContextVisitor {

   Context visit(Category category);

   Context visit(Content content);

   Context visit(Contract contract);

   Context visit(Customer customer);

   Context visit(Group group);

   Context visit(Offer offer);

   Context visit(Order order);

   Context visit(Product product);

   Context visit(Setting setting);

   Context visit(Site site);

   Context visit(User user);
}
