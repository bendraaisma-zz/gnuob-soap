/*
 * Copyright 2016 Netbrasoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package br.com.netbrasoft.gnuob.generic.utils;

import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._100_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._10_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._127_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._128_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._129_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._150_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._15_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._165_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._17_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._180_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._19_2_DOUBLE;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._19_4_DOUBLE;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._20_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._255_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._25_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._2_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._40_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._64_CHARS;
import static br.com.netbrasoft.gnuob.api.generic.NetbrasoftApiTestConstants._80_CHARS;
import static br.com.netbrasoft.gnuob.generic.security.Rule.DELETE_ACCESS;
import static br.com.netbrasoft.gnuob.generic.security.Rule.READ_ACCESS;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.netbrasoft.gnuob.generic.category.Category;
import br.com.netbrasoft.gnuob.generic.category.SubCategory;
import br.com.netbrasoft.gnuob.generic.content.Content;
import br.com.netbrasoft.gnuob.generic.contract.Contract;
import br.com.netbrasoft.gnuob.generic.customer.Address;
import br.com.netbrasoft.gnuob.generic.customer.Customer;
import br.com.netbrasoft.gnuob.generic.customer.PostalCode;
import br.com.netbrasoft.gnuob.generic.offer.Offer;
import br.com.netbrasoft.gnuob.generic.offer.OfferRecord;
import br.com.netbrasoft.gnuob.generic.order.Invoice;
import br.com.netbrasoft.gnuob.generic.order.Order;
import br.com.netbrasoft.gnuob.generic.order.OrderRecord;
import br.com.netbrasoft.gnuob.generic.order.Payment;
import br.com.netbrasoft.gnuob.generic.order.Shipment;
import br.com.netbrasoft.gnuob.generic.product.Option;
import br.com.netbrasoft.gnuob.generic.product.Product;
import br.com.netbrasoft.gnuob.generic.product.Stock;
import br.com.netbrasoft.gnuob.generic.product.SubOption;
import br.com.netbrasoft.gnuob.generic.security.Group;
import br.com.netbrasoft.gnuob.generic.security.Permission;
import br.com.netbrasoft.gnuob.generic.security.Site;
import br.com.netbrasoft.gnuob.generic.security.User;
import br.com.netbrasoft.gnuob.generic.setting.Setting;
import de.rtner.security.auth.spi.SimplePBKDF2;

public final class DummyInstanceHelper {

  public static Address getAddressInstance() {
    final Address address = new Address();
    address.setId(0L);
    address.setVersion(0);
    address.setCityName(_40_CHARS);
    address.setComplement(_40_CHARS);
    address.setCountry(_40_CHARS);
    address.setCountryName(_40_CHARS);
    address.setDistrict(_40_CHARS);
    address.setInternationalStateAndCity(_80_CHARS);
    address.setInternationalStreet(_40_CHARS);
    address.setNumber(_10_CHARS);
    address.setPhone(_20_CHARS);
    address.setPostalCode(_20_CHARS);
    address.setStateOrProvince(_40_CHARS);
    address.setStreet1(_100_CHARS);
    address.setStreet2(_100_CHARS);
    return address;
  }

  public static Category getCategoryInstance() {
    final Category category = new Category();
    category.setActive(true);
    category.setId(0L);
    category.setVersion(0);
    category.setName(_129_CHARS);
    category.setDescription(_129_CHARS);
    category.setPermission(getPermissionInstance());
    category.setPosition(Integer.MAX_VALUE);
    category.getContents().add(getContentInstance());
    category.getSubCategories().add(getSubCategoryInstance());
    return category;
  }

  public static Content getContentInstance() {
    final Content content = new Content();
    content.setActive(true);
    content.setId(0L);
    content.setVersion(0);
    content.setData(new byte[1]);
    content.setFormat(_128_CHARS);
    content.setName(_128_CHARS);
    content.setPermission(getPermissionInstance());
    return content;
  }

  public static Contract getContractInstance() {
    final Contract contract = new Contract();
    contract.setActive(true);
    contract.setId(0L);
    contract.setVersion(0);
    contract.setContractId(_127_CHARS);
    contract.setCustomer(getCustomerInstance());
    contract.setPermission(getPermissionInstance());
    return contract;
  }

  public static Customer getCustomerInstance() {
    final Customer customer = new Customer();
    customer.setActive(true);
    customer.setId(0L);
    customer.setVersion(0);
    customer.setAddress(getAddressInstance());
    customer.setBuyerEmail(_127_CHARS);
    customer.setBuyerMarketingEmail(_127_CHARS);
    customer.setContactPhone(_20_CHARS);
    final GregorianCalendar gregorianCalendar = new GregorianCalendar();
    gregorianCalendar.setTime(new Date());
    // FIXME: Fix criteria find by example to find also by date properties.
    // customer.setDateOfBirth(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
    customer.setFirstName(_64_CHARS);
    customer.setFriendlyName(_128_CHARS);
    customer.setLastName(_64_CHARS);
    customer.setMiddleName(_64_CHARS);
    customer.setPayer(_127_CHARS);
    customer.setPayerBusiness(_127_CHARS);
    customer.setPayerId(_20_CHARS);
    customer.setPayerStatus(_20_CHARS);
    customer.setPermission(getPermissionInstance());
    customer.setSalutation(_20_CHARS);
    customer.setSuffix(_20_CHARS);
    customer.setTaxId(_20_CHARS);
    customer.setTaxIdType(_20_CHARS);
    return customer;
  }

  public static Group getGroupInstance() {
    final Group group = new Group();
    group.setActive(true);
    group.setId(0L);
    group.setVersion(0);
    group.setName(_129_CHARS);
    group.setDescription(_129_CHARS);
    group.setPermission(getPermissionInstance());
    return group;
  }

  public static Invoice getInvoiceInstance() {
    final Invoice invoice = new Invoice();
    invoice.setId(0L);
    invoice.setVersion(0);
    invoice.setAddress(getAddressInstance());
    invoice.setInvoiceId(_127_CHARS);
    invoice.getPayments().add(getPaymentInstance());
    return invoice;
  }

  public static Offer getOfferInstance() {
    final Offer offer = new Offer();
    offer.setActive(true);
    offer.setId(0L);
    offer.setVersion(0);
    offer.setContract(getContractInstance());
    offer.setDiscountTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    offer.setExtraAmount(BigDecimal.valueOf(_19_2_DOUBLE));
    offer.setHandlingTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    offer.setInsuranceTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    offer.setItemTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    offer.setMaxTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    final GregorianCalendar gregorianCalendar = new GregorianCalendar();
    gregorianCalendar.setTime(new Date());
    // FIXME: Fix criteria find by example to find also by date properties.
    // offer.setOfferDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
    offer.setOfferDescription(_127_CHARS);
    offer.setOfferId(_64_CHARS);
    offer.setOfferTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    offer.setPermission(getPermissionInstance());
    offer.setShippingDiscount(BigDecimal.valueOf(_19_2_DOUBLE));
    offer.setShippingTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    offer.setTaxTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    offer.getRecords().add(getOfferRecordInstance());
    return offer;
  }

  public static OfferRecord getOfferRecordInstance() {
    final OfferRecord offerRecord = new OfferRecord();
    offerRecord.setId(0L);
    offerRecord.setVersion(0);
    offerRecord.setAmount(BigDecimal.valueOf(_19_2_DOUBLE));
    // FIXME: Fix criteria find by example to find also by date properties.
    // offerRecord.setDeliveryDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
    offerRecord.setDescription(_128_CHARS);
    offerRecord.setDiscount(BigDecimal.valueOf(_19_2_DOUBLE));
    offerRecord.setItemHeight(BigDecimal.valueOf(_19_2_DOUBLE));
    offerRecord.setItemHeightUnit(_20_CHARS);
    offerRecord.setItemLength(BigDecimal.valueOf(_19_2_DOUBLE));
    offerRecord.setItemLengthUnit(_20_CHARS);
    offerRecord.setItemUrl(_255_CHARS);
    offerRecord.setItemWeight(BigDecimal.valueOf(_19_2_DOUBLE));
    offerRecord.setItemWeightUnit(_20_CHARS);
    offerRecord.setItemWidth(BigDecimal.valueOf(_19_2_DOUBLE));
    offerRecord.setItemWidthUnit(_20_CHARS);
    offerRecord.setName(_128_CHARS);
    offerRecord.setOption(_128_CHARS);
    offerRecord.setOfferRecordId(_127_CHARS);
    offerRecord.setProduct(getProductInstance());
    offerRecord.setProductNumber(_64_CHARS);
    offerRecord.setQuantity(BigInteger.valueOf(Integer.MAX_VALUE));
    offerRecord.setShippingCost(BigDecimal.valueOf(_19_2_DOUBLE));
    offerRecord.setTax(BigDecimal.valueOf(_19_2_DOUBLE));
    return offerRecord;
  }

  public static Option getOptionInstance() {
    final Option option = new Option();
    option.setId(0L);
    option.setVersion(0);
    option.setDescription(_129_CHARS);
    option.setPosition(Integer.MAX_VALUE);
    option.setDisabled(Boolean.TRUE);
    option.setValue(_129_CHARS);
    option.getSubOptions().add(getSubOptionInstance());
    return option;
  }

  public static Order getOrderInstance() {
    final Order order = new Order();
    order.setActive(true);
    order.setId(0L);
    order.setVersion(0);
    order.setBillingAgreementId(_20_CHARS);
    order.setCheckout(_15_CHARS);
    order.setCheckoutStatus(_255_CHARS);
    order.setContract(getContractInstance());
    order.setCustom(_255_CHARS);
    order.setDiscountTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    order.setExtraAmount(BigDecimal.valueOf(_19_2_DOUBLE));
    order.setGiftMessage(_150_CHARS);
    order.setGiftMessageEnable(Boolean.TRUE);
    order.setGiftReceiptEnable(Boolean.TRUE);
    order.setGiftWrapAmount(BigDecimal.valueOf(_19_2_DOUBLE));
    order.setGiftWrapName(_25_CHARS);
    order.setGiftWrapEnable(Boolean.TRUE);
    order.setHandlingTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    order.setInsuranceOptionOffered(Boolean.TRUE);
    order.setInsuranceTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    order.setInvoice(getInvoiceInstance());
    order.setItemTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    order.setMaxTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    order.setNote(_165_CHARS);
    order.setNoteText(_255_CHARS);
    order.setNotificationId(_255_CHARS);
    final GregorianCalendar gregorianCalendar = new GregorianCalendar();
    gregorianCalendar.setTime(new Date());
    // FIXME: Fix criteria find by example to find also by date properties.
    // order.setOrderDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
    order.setOrderDescription(_127_CHARS);
    order.setOrderId(_64_CHARS);
    order.setOrderTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    order.setPermission(getPermissionInstance());
    order.setShipment(getShipmentInstance());
    order.setShippingDiscount(BigDecimal.valueOf(_19_2_DOUBLE));
    order.setShippingTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    order.setTaxTotal(BigDecimal.valueOf(_19_2_DOUBLE));
    order.setToken(_40_CHARS);
    order.setTransactionId(_64_CHARS);
    order.getRecords().add(getOrderRecordInstance());
    return order;
  }

  public static OrderRecord getOrderRecordInstance() {
    final OrderRecord orderRecord = new OrderRecord();
    orderRecord.setId(0L);
    orderRecord.setVersion(0);
    orderRecord.setAmount(BigDecimal.valueOf(_19_2_DOUBLE));
    // FIXME: Fix criteria find by example to find also by date properties.
    // orderRecord.setDeliveryDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
    orderRecord.setDescription(_128_CHARS);
    orderRecord.setDiscount(BigDecimal.valueOf(_19_2_DOUBLE));
    orderRecord.setItemHeight(BigDecimal.valueOf(_19_2_DOUBLE));
    orderRecord.setItemHeightUnit(_20_CHARS);
    orderRecord.setItemLength(BigDecimal.valueOf(_19_2_DOUBLE));
    orderRecord.setItemLengthUnit(_20_CHARS);
    orderRecord.setItemUrl(_255_CHARS);
    orderRecord.setItemWeight(BigDecimal.valueOf(_19_2_DOUBLE));
    orderRecord.setItemWeightUnit(_20_CHARS);
    orderRecord.setItemWidth(BigDecimal.valueOf(_19_2_DOUBLE));
    orderRecord.setItemWidthUnit(_20_CHARS);
    orderRecord.setName(_128_CHARS);
    orderRecord.setOption(_128_CHARS);
    orderRecord.setOrderRecordId(_127_CHARS);
    orderRecord.setProduct(getProductInstance());
    orderRecord.setProductNumber(_64_CHARS);
    orderRecord.setQuantity(BigInteger.valueOf(Integer.MAX_VALUE));
    orderRecord.setShippingCost(BigDecimal.valueOf(_19_2_DOUBLE));
    orderRecord.setTax(BigDecimal.valueOf(_19_2_DOUBLE));
    return orderRecord;
  }

  public static Payment getPaymentInstance() {
    final Payment payment = new Payment();
    payment.setId(0L);
    payment.setVersion(0);
    payment.setExchangeRate(_17_CHARS);
    payment.setFeeAmount(BigDecimal.valueOf(_19_2_DOUBLE));
    payment.setGrossAmount(BigDecimal.valueOf(_19_2_DOUBLE));
    payment.setHoldDecision(_20_CHARS);
    payment.setInstallmentCount(BigInteger.valueOf(Integer.MAX_VALUE));
    // FIXME: Fix criteria find by example to find also by date properties.
    // payment.setPaymentDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
    payment.setPaymentRequestId(_127_CHARS);
    payment.setPaymentStatus(_20_CHARS);
    payment.setPaymentType(_20_CHARS);
    payment.setPendingReason(_20_CHARS);
    payment.setProtectionEligibilityType(_128_CHARS);
    payment.setReasonCode(_20_CHARS);
    payment.setSettleAmount(BigDecimal.valueOf(_19_2_DOUBLE));
    payment.setStoreId(_20_CHARS);
    payment.setTaxAmount(BigDecimal.valueOf(_19_2_DOUBLE));
    payment.setTerminalId(_20_CHARS);
    payment.setTransactionId(_64_CHARS);
    payment.setTransactionType(_20_CHARS);
    return payment;
  }

  public static Permission getPermissionInstance() {
    final Permission permission = new Permission();
    permission.setId(0L);
    permission.setVersion(0);
    permission.setOwner(DELETE_ACCESS);
    permission.setGroup(DELETE_ACCESS);
    permission.setOthers(DELETE_ACCESS);
    return permission;
  }

  public static PostalCode getPostalCodeInstance() {
    final PostalCode postalCode = new PostalCode();
    postalCode.setId(0L);
    postalCode.setVersion(0);
    postalCode.setAccuracy(_100_CHARS);
    postalCode.setAdminCode1(_20_CHARS);
    postalCode.setAdminCode2(_20_CHARS);
    postalCode.setAdminCode3(_20_CHARS);
    postalCode.setAdminName1(_100_CHARS);
    postalCode.setAdminName2(_100_CHARS);
    postalCode.setAdminName3(_100_CHARS);
    postalCode.setCountryCode(_2_CHARS);
    postalCode.setLatitude(BigDecimal.valueOf(_19_4_DOUBLE));
    postalCode.setLongitude(BigDecimal.valueOf(_19_4_DOUBLE));
    postalCode.setPlaceName(_180_CHARS);
    postalCode.setPostalCode(_20_CHARS);
    return postalCode;
  }

  public static Product getProductInstance() {
    final Product product = new Product();
    product.setActive(true);
    product.setId(0L);
    product.setVersion(0);
    product.setAmount(BigDecimal.valueOf(_19_2_DOUBLE));
    product.setBestsellers(Boolean.TRUE);
    product.setDescription(_129_CHARS);
    product.setDiscount(BigDecimal.valueOf(_19_2_DOUBLE));
    product.setItemHeight(BigDecimal.valueOf(_19_2_DOUBLE));
    product.setItemHeightUnit(_20_CHARS);
    product.setItemLength(BigDecimal.valueOf(_19_2_DOUBLE));
    product.setItemLengthUnit(_20_CHARS);
    product.setItemUrl(_255_CHARS);
    product.setItemWeight(BigDecimal.valueOf(_19_2_DOUBLE));
    product.setItemWeightUnit(_20_CHARS);
    product.setItemWidth(BigDecimal.valueOf(_19_2_DOUBLE));
    product.setItemWidthUnit(_20_CHARS);
    product.setName(_129_CHARS);
    product.setNumber(_64_CHARS);
    product.setPermission(getPermissionInstance());
    product.setRating(Integer.MAX_VALUE);
    product.setRecommended(Boolean.TRUE);
    product.setShippingCost(BigDecimal.valueOf(_19_2_DOUBLE));
    product.setStock(getStockInstance());
    product.setTax(BigDecimal.valueOf(_19_2_DOUBLE));
    product.getOptions().add(getOptionInstance());
    product.getSubCategories().add(getSubCategoryInstance());
    return product;
  }

  public static Setting getSettingInstance() {
    final Setting setting = new Setting();
    setting.setActive(true);
    setting.setId(0L);
    setting.setVersion(0);
    setting.setProperty(_129_CHARS);
    setting.setValue(_129_CHARS);
    setting.setDescription(_129_CHARS);
    setting.setPermission(getPermissionInstance());
    return setting;
  }

  public static Shipment getShipmentInstance() {
    final Shipment shipment = new Shipment();
    shipment.setId(0L);
    shipment.setVersion(0);
    shipment.setShipmentType(_128_CHARS);
    shipment.setAddress(getAddressInstance());
    return shipment;
  }

  public static Site getSiteInstance() {
    final Site site = new Site();
    site.setActive(true);
    site.setId(0L);
    site.setVersion(0);
    site.setName(_129_CHARS);
    site.setDescription(_129_CHARS);
    site.setPermission(getPermissionInstance());
    return site;
  }

  public static Stock getStockInstance() {
    final Stock stock = new Stock();
    stock.setId(0L);
    stock.setVersion(0);
    stock.setMaxQuantity(BigInteger.valueOf(Integer.MAX_VALUE));
    stock.setMinQuantity(BigInteger.valueOf(Integer.MAX_VALUE));
    stock.setQuantity(BigInteger.valueOf(Integer.MAX_VALUE));
    return stock;
  }

  public static SubCategory getSubCategoryInstance() {
    final SubCategory subCategory = new SubCategory();
    subCategory.setId(0L);
    subCategory.setVersion(0);
    subCategory.setDescription(_129_CHARS);
    subCategory.setName(_129_CHARS);
    return subCategory;
  }

  public static SubOption getSubOptionInstance() {
    final SubOption subOption = new SubOption();
    subOption.setId(0L);
    subOption.setVersion(0);
    subOption.setDescription(_129_CHARS);
    subOption.setDisabled(Boolean.TRUE);
    subOption.setValue(_129_CHARS);
    return subOption;
  }

  public static User getUserInstance() {
    final User user = new User();
    user.setActive(true);
    user.setId(0L);
    user.setVersion(0);
    user.setAccess(READ_ACCESS);
    user.setDescription(_129_CHARS);
    user.setName("dba");
    user.setPassword(new SimplePBKDF2().deriveKeyFormatted("dba"));
    user.setPermission(getPermissionInstance());
    user.getSites().add(getSiteInstance());
    user.getGroups().add(getGroupInstance());
    return user;
  }
}
