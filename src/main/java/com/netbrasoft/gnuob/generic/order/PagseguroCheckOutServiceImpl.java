package com.netbrasoft.gnuob.generic.order;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.Address;
import br.com.uol.pagseguro.domain.Item;
import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.Shipping;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.checkout.Checkout;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.enums.ShippingType;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.service.TransactionSearchService;
import br.com.uol.pagseguro.service.checkout.CheckoutService;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;

@Stateless(name = "PagseguroCheckOutServiceImpl")
public class PagseguroCheckOutServiceImpl<O extends Order> implements CheckOutService<O> {

   private static final String GNUOB_SITE_PROPERTY = "gnuob.site";
   private static final String PAGSEGURO_EMAIL_PROPERTY = "pagseguro.email";
   private static final String PAGSEGURO_PRODUCTION_TOKEN_PROPERTY = "pagseguro.production.token";
   private static final String PAGSEGURO_SANDBOX_TOKEN_PROPERTY = "pagseguro.sandbox.token";
   private static final String PAGSEGURO_CURRENCY_PROPERTY = "pagseguro.currency.id";
   private static final String GNUOB_SITE_PROPERTY_VALUE = "https://www.netbrasoft.com";
   private static final String PAGSEGURO_EMAIL_PROPERTY_VALUE = "badraaisma@msn.com";
   private static final String PAGSEGURO_PRODUCTION_TOKEN_PROPERTY_VALUE = "NO_PRODUCTION_TOKEN";
   private static final String PAGSEGURO_SANDBOX_TOKEN_PROPERTY_VALUE = "007D4EDFF33042E79EC7B8039B5F7FCE";
   private static final String PAGSEGURO_CURRENCY_PROPERTY_VALUE = "BRL";
   private static final String EMAIL_PROPERTY = System.getProperty(PAGSEGURO_EMAIL_PROPERTY,
         PAGSEGURO_EMAIL_PROPERTY_VALUE);
   private static final String PRODUCTION_TOKEN_PROPERTY = System.getProperty(PAGSEGURO_PRODUCTION_TOKEN_PROPERTY,
         PAGSEGURO_PRODUCTION_TOKEN_PROPERTY_VALUE);
   private static final String SANDBOX_TOKEN_PROPERTY = System.getProperty(PAGSEGURO_SANDBOX_TOKEN_PROPERTY,
         PAGSEGURO_SANDBOX_TOKEN_PROPERTY_VALUE);
   private static final String NOTIFICATION_URL_PROPERTY = System.getProperty(GNUOB_SITE_PROPERTY,
         GNUOB_SITE_PROPERTY_VALUE);
   private static final String CURRENCY_PROPERTY = System.getProperty(PAGSEGURO_CURRENCY_PROPERTY,
         PAGSEGURO_CURRENCY_PROPERTY_VALUE);

   public PagseguroCheckOutServiceImpl() {
      if (!PAGSEGURO_PRODUCTION_TOKEN_PROPERTY_VALUE.equals(PRODUCTION_TOKEN_PROPERTY)) {
         PagSeguroConfig.setProductionEnvironment();
      }
   }

   private String createCheckoutRequest(Checkout checkout) throws PagSeguroServiceException,
         ParserConfigurationException, SAXException, IOException {
      String email = EMAIL_PROPERTY;
      String productionToken = PRODUCTION_TOKEN_PROPERTY;
      String sandboxToken = SANDBOX_TOKEN_PROPERTY;

      return CheckoutService.createCheckoutRequest(new AccountCredentials(email, productionToken, sandboxToken),
            checkout, false);
   }

   private Address doAddress(com.netbrasoft.gnuob.generic.customer.Address address) {
      Address addressType = new Address();
      addressType.setCity(address.getCityName());
      addressType.setStreet(address.getStreet1());
      addressType.setComplement(address.getComplement());
      addressType.setCountry(address.getCountry());
      address.setDistrict(address.getDistrict());
      addressType.setNumber(address.getNumber());
      addressType.setPostalCode(address.getPostalCode());
      addressType.setState(address.getStateOrProvince());
      return addressType;
   }

   private void doAddress(com.netbrasoft.gnuob.generic.customer.Address address, Address addressType) {
      address.setCountry(addressType.getCountry());
      address.setStateOrProvince(addressType.getState());
      address.setCityName(addressType.getCity());
      address.setPostalCode(addressType.getPostalCode());
      address.setDistrict(addressType.getDistrict());
      address.setStreet1(addressType.getStreet());
      address.setNumber(addressType.getNumber());
      address.setComplement(addressType.getComplement());
   }

   @Override
   public void doCheckout(O order) {

      // set checkout request fields.
      Checkout checkout = new Checkout();
      checkout.setCurrency(Currency.valueOf(CURRENCY_PROPERTY));
      checkout.setExtraAmount(order.getExtraAmount().setScale(2));
      checkout.setNotificationURL(NOTIFICATION_URL_PROPERTY);
      checkout.setRedirectURL(NOTIFICATION_URL_PROPERTY);
      checkout.setReference(order.getOrderId());

      // set checkout shipping request details field..
      checkout.setShipping(doShipping(order));

      // set checkout items request details fields.
      checkout.setItems(doItems(order));

      // Set checkout sender request details fields.
      checkout.setSender(doSender(order));

      try {
         // Do checkout call.
         String response = createCheckoutRequest(checkout);
         order.setToken(response.split("code=")[1]);
      } catch (PagSeguroServiceException | ParserConfigurationException | SAXException | IOException e) {
         throw new GNUOpenBusinessServiceException("Exception from Pagseguro Checkout, please try again.", e);
      }
   }

   @Override
   public void doCheckoutDetails(O order) {
      try {
         Transaction transaction = searchByOrder(order);

         // Information about the payer.
         doSender(order, transaction.getSender());

         // Information about the payment.
         doPaymentDetails(order, transaction);

         // Status of the checkout session. If payment is completed, the
         // transaction identification number of the resulting transaction is
         // returned.
         order.setCheckoutStatus(transaction.getStatus().name());
      } catch (PagSeguroServiceException e) {
         throw new GNUOpenBusinessServiceException("Exception from Pagseguro Checkout, please try again.", e);
      }
   }

   @Override
   public void doCheckoutPayment(O order) {
      try {
         Transaction transaction = searchByOrder(order);

         // Information about the payment.
         order.getInvoice().getPayments().add(doPaymentInfo(transaction));

         // Status of the checkout session. If payment is completed, the
         // transaction identification number of the resulting transaction is
         // returned.
         order.setCheckoutStatus(transaction.getStatus().name());
      } catch (PagSeguroServiceException e) {
         throw new GNUOpenBusinessServiceException("Exception from Pagseguro Checkout, please try again.", e);
      }
   }

   private Item doItem(OrderRecord orderRecord) {
      Item item = new Item();
      item.setAmount(orderRecord.getAmount().setScale(2));
      item.setDescription(orderRecord.getDescription());
      item.setId(orderRecord.getNumber());
      item.setQuantity(orderRecord.getQuantity().intValue());
      item.setShippingCost(orderRecord.getShippingCost());
      item.setWeight(orderRecord.getItemWeight().longValue());
      item.setDescription(orderRecord.getDescription());
      return item;
   }

   private void doItem(OrderRecord orderRecord, Item item) {
      orderRecord.setDescription(item.getDescription());
      orderRecord.setAmount(item.getAmount());
      orderRecord.setQuantity(BigInteger.valueOf(item.getQuantity()));
   }

   private List<Item> doItems(O order) {

      List<Item> items = new ArrayList<Item>();

      // Details about each individual item included in the order.
      for (OrderRecord orderRecord : order.getRecords()) {
         items.add(doItem(orderRecord));
      }

      return items;
   }

   private void doPaymentDetails(O order, Transaction transaction) {
      order.setTransactionId(transaction.getCode());
      order.setOrderId(transaction.getReference());
      order.setDiscountTotal(transaction.getDiscountAmount());
      order.setExtraAmount(transaction.getExtraAmount());
      doShipping(order, transaction.getShipping());

      // Details about each individual item included in the order.
      for (Item item : transaction.getItems()) {
         for (OrderRecord orderRecord : order.getRecords()) {
            if (item.getId() == orderRecord.getNumber()) {
               doItem(orderRecord, item);
               break;
            }
         }
      }
   }

   private Payment doPaymentInfo(Transaction transaction) {
      Payment payment = new Payment();

      // Unique transaction ID of the payment.
      payment.setTransactionId(transaction.getCode());

      // Type of transaction.
      payment.setTransactionType(transaction.getType().name());

      // Indicates whether the payment is instant or delayed.
      payment.setPaymentType(transaction.getPaymentMethod().getType().getType());

      // The final amount charged, including any shipping and taxes from your
      // Merchant Profile.
      payment.setGrossAmount(transaction.getGrossAmount());

      // Amount deposited in your Pagseguro account after a currency
      // conversion.
      payment.setSettleAmount(transaction.getNetAmount());

      // Time/date stamp of payment.
      if (transaction.getEscrowEndDate() != null) {
         payment.setPaymentDate(new Date(transaction.getEscrowEndDate().getTime()));
      }

      // Reason that this payment is being held.
      payment.setHoldDecision(transaction.getCancellationSource());

      // Indica o número de parcelas que o comprador escolheu no pagamento com
      // cartão de crédito.
      payment.setInstallmentCount(BigInteger.valueOf(transaction.getInstallmentCount()));

      // Pagseguro fee amount charged for the transaction.
      payment.setFeeAmount(transaction.getFeeAmount());

      // The status of the payment.
      payment.setPaymentStatus(transaction.getStatus().name());

      return payment;
   }

   private Sender doSender(O order) {
      Sender sender = new Sender();
      sender.setEmail(order.getContract().getCustomer().getBuyerEmail());
      sender.setName(order.getContract().getCustomer().getFriendlyName());
      return sender;
   }

   private void doSender(O order, Sender sender) {
      order.getContract().getCustomer().setBuyerEmail(sender.getEmail());
      order.getContract().getCustomer().setFriendlyName(sender.getName());
      order.getContract().getCustomer().setContactPhone(sender.getPhone().toString());
   }

   private Shipping doShipping(O order) {
      Shipping shipping = new Shipping();

      BigDecimal cost = order.getShippingTotal().subtract(order.getShippingDiscount());
      shipping.setCost(cost.setScale(2));
      shipping.setType(ShippingType.valueOf(order.getShipment().getShipmentType()));

      // Address to which the order is shipped.
      shipping.setAddress(doAddress(order.getShipment().getAddress()));
      return shipping;
   }

   private void doShipping(O order, Shipping shipping) {
      order.setShippingTotal(shipping.getCost());
      order.getShipment().setShipmentType(shipping.getType().name());
      doAddress(order.getShipment().getAddress(), shipping.getAddress());
   }

   private Transaction searchByOrder(O order) throws PagSeguroServiceException {
      String email = EMAIL_PROPERTY;
      String productionToken = PRODUCTION_TOKEN_PROPERTY;
      String sandboxToken = SANDBOX_TOKEN_PROPERTY;

      return TransactionSearchService.searchByCode(new AccountCredentials(email, productionToken, sandboxToken),
            order.getTransactionId());
   }
}
