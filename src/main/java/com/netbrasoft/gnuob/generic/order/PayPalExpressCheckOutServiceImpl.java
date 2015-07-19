package com.netbrasoft.gnuob.generic.order;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceRef;

import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxb.JAXBDataBinding;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.customer.Address;

import ebay.api.paypalapi.DoExpressCheckoutPaymentReq;
import ebay.api.paypalapi.DoExpressCheckoutPaymentRequestType;
import ebay.api.paypalapi.DoExpressCheckoutPaymentResponseType;
import ebay.api.paypalapi.GetExpressCheckoutDetailsReq;
import ebay.api.paypalapi.GetExpressCheckoutDetailsRequestType;
import ebay.api.paypalapi.GetExpressCheckoutDetailsResponseType;
import ebay.api.paypalapi.PayPalAPIAAInterface;
import ebay.api.paypalapi.PayPalAPIInterfaceService;
import ebay.api.paypalapi.SetExpressCheckoutReq;
import ebay.api.paypalapi.SetExpressCheckoutRequestType;
import ebay.api.paypalapi.SetExpressCheckoutResponseType;
import ebay.apis.corecomponenttypes.BasicAmountType;
import ebay.apis.corecomponenttypes.MeasureType;
import ebay.apis.eblbasecomponents.AckCodeType;
import ebay.apis.eblbasecomponents.AddressNormalizationStatusCodeType;
import ebay.apis.eblbasecomponents.AddressOwnerCodeType;
import ebay.apis.eblbasecomponents.AddressStatusCodeType;
import ebay.apis.eblbasecomponents.AddressType;
import ebay.apis.eblbasecomponents.CountryCodeType;
import ebay.apis.eblbasecomponents.CurrencyCodeType;
import ebay.apis.eblbasecomponents.CustomSecurityHeaderType;
import ebay.apis.eblbasecomponents.DoExpressCheckoutPaymentRequestDetailsType;
import ebay.apis.eblbasecomponents.DoExpressCheckoutPaymentResponseDetailsType;
import ebay.apis.eblbasecomponents.ErrorType;
import ebay.apis.eblbasecomponents.GetExpressCheckoutDetailsResponseDetailsType;
import ebay.apis.eblbasecomponents.ItemCategoryType;
import ebay.apis.eblbasecomponents.PayerInfoType;
import ebay.apis.eblbasecomponents.PaymentDetailsItemType;
import ebay.apis.eblbasecomponents.PaymentDetailsType;
import ebay.apis.eblbasecomponents.PaymentInfoType;
import ebay.apis.eblbasecomponents.SetExpressCheckoutRequestDetailsType;
import ebay.apis.eblbasecomponents.UserIdPasswordType;

@Stateless(name = "PayPalExpressCheckOutServiceImpl")
public class PayPalExpressCheckOutServiceImpl<O extends Order> implements CheckOutService<O> {

   private static final String GNUOB_SITE_NOTIFICATION_PROPERTY = "gnuob.site.notification";
   private static final String GNUOB_SITE_REDIRECT_PROPERTY = "gnuob.site.redirect";
   private static final String GNUOB_SITE_CANCEL_PROPERTY = "gnuob.site.cancel";
   private static final String PAYPAL_SITE_PROPERTY = "paypal.site";
   private static final String PAYPAL_SUBJECT_PROPERTY = "paypal.subject";
   private static final String PAYPAL_SIGNATURE_PROPERTY = "paypal.signature";
   private static final String PAYPAL_PASSWORD_PROPERTY = "paypal.password";
   private static final String PAYPAL_USERNAME_PROPERTY = "paypal.username";
   private static final String PAYPAL_VERSION_PROPERTY = "paypal.version";
   private static final String PAYPAL_SERVICE_NUMBER_PROPERTY = "paypal.serviceNumber";
   private static final String GNUOB_SITE_NOTIFICATION_PROPERTY_VALUE = "http://localhost:8080/notification.html";
   private static final String GNUOB_SITE_REDIRECT_PROPERTY_VALUE = "http://localhost:8080/confirmation.html";
   private static final String GNUOB_SITE_CANCEL_PROPERTY_VALUE = "http://localhost:8080/cancel.html";
   private static final String PAYPAL_SITE_PROPERTY_VALUE = "https://api-3t.sandbox.paypal.com/2.0/";
   private static final String PAYPAL_SUBJECT_PROPERTY_VALUE = "badraaisma@msn.com";
   private static final String PAYPAL_SIGNATURE_PROPERTY_VALUE = "AFcWxV21C7fd0v3bYYYRCpSSRl31A7mmuIDpb.xZccUUAyCy0P.XGaWg";
   private static final String PAYPAL_PASSWORD_PROPERTY_VALUE = "UU9AJQJYE2CAMP54";
   private static final String PAYPAL_USERNAME_PROPERTY_VALUE = "BR2GaGfg_api1.netbrasoft.com";
   private static final String PAYPAL_VERSION_PROPERTY_VALUE = "121.0";
   private static final String PAYPAL_SERVICE_NUMBER_VALUE = "-";
   private static final String VERSION_PROPERTY = System.getProperty(PAYPAL_VERSION_PROPERTY, PAYPAL_VERSION_PROPERTY_VALUE);
   private static final String NOTIFY_URL_PROPERTY = System.getProperty(GNUOB_SITE_NOTIFICATION_PROPERTY, GNUOB_SITE_NOTIFICATION_PROPERTY_VALUE);
   private static final String REDIRECT_URL_PROPERTY = System.getProperty(GNUOB_SITE_REDIRECT_PROPERTY, GNUOB_SITE_REDIRECT_PROPERTY_VALUE);
   private static final String CANCEL_URL_PROPERTY = System.getProperty(GNUOB_SITE_CANCEL_PROPERTY, GNUOB_SITE_CANCEL_PROPERTY_VALUE);
   private static final String CUSTOMER_SERVICE_NUMBER_PROPERTY = System.getProperty(PAYPAL_SERVICE_NUMBER_PROPERTY, PAYPAL_SERVICE_NUMBER_VALUE);
   private static final String SUBJECT_PROPERTY = System.getProperty(PAYPAL_SUBJECT_PROPERTY, PAYPAL_SUBJECT_PROPERTY_VALUE);
   private static final String SIGNATURE_PROPERTY = System.getProperty(PAYPAL_SIGNATURE_PROPERTY, PAYPAL_SIGNATURE_PROPERTY_VALUE);
   private static final String PASSWORD_PROPERTY = System.getProperty(PAYPAL_PASSWORD_PROPERTY, PAYPAL_PASSWORD_PROPERTY_VALUE);
   private static final String USERNAME_PROPERTY = System.getProperty(PAYPAL_USERNAME_PROPERTY, PAYPAL_USERNAME_PROPERTY_VALUE);
   private static final String SITE_PROPERTY = System.getProperty(PAYPAL_SITE_PROPERTY, PAYPAL_SITE_PROPERTY_VALUE);

   @WebServiceRef(wsdlLocation = "https://www.paypalobjects.com/wsdl/PayPalSvc.wsdl")
   private PayPalAPIInterfaceService payPalAPIInterfaceService;

   private AddressType doAddress(Address address) {
      final AddressType addressType = new AddressType();
      addressType.setAddressID(String.valueOf(address.getId()));
      addressType.setAddressNormalizationStatus(AddressNormalizationStatusCodeType.NORMALIZED);
      addressType.setAddressOwner(AddressOwnerCodeType.PAY_PAL);
      addressType.setAddressStatus(AddressStatusCodeType.NONE);
      addressType.setCityName(address.getCityName());
      addressType.setCountry(Enum.valueOf(CountryCodeType.class, address.getCountry().replace("_", "")));
      addressType.setCountryName(address.getCountryName());
      addressType.setExternalAddressID(String.valueOf(address.getId()));
      addressType.setInternationalName("International shipment address");
      addressType.setInternationalStateAndCity(address.getInternationalStateAndCity());
      addressType.setInternationalStreet(address.getInternationalStreet());
      addressType.setName("Shipment address");
      addressType.setPhone(address.getPhone());
      addressType.setPostalCode(address.getPostalCode());
      addressType.setStateOrProvince(address.getStateOrProvince());
      addressType.setStreet1(address.getStreet1());
      addressType.setStreet2(address.getStreet2());
      return addressType;
   }

   private void doAddressType(Address address, AddressType addressType) {
      address.setCityName(addressType.getCityName());
      if (addressType.getCountry() != null) {
         address.setCountry("_" + addressType.getCountry().value());
      }
      address.setCountryName(addressType.getCountryName());
      address.setInternationalStateAndCity(addressType.getInternationalStateAndCity());
      address.setInternationalStreet(addressType.getInternationalStreet());
      address.setPhone(addressType.getPhone());
      address.setPostalCode(addressType.getPostalCode());
      address.setStateOrProvince(addressType.getStateOrProvince());
      address.setStreet1(addressType.getStreet1());
      address.setStreet2(addressType.getStreet2());
   }

   private BigDecimal doBasicAmountType(BasicAmountType basicAmountType) {
      return basicAmountType == null ? null : new BigDecimal(basicAmountType.getValue());
   }

   private BasicAmountType doBasicAmountType(BigDecimal value) {
      try {
         if (value != null) {
            final NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
            final BasicAmountType basicAmountType = new BasicAmountType();
            basicAmountType.setCurrencyID(CurrencyCodeType.valueOf(NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getCurrencyCode()));
            basicAmountType.setValue(numberFormat.parse(numberFormat.format(value)).toString());
            return basicAmountType;
         }
         return null;
      } catch (final ParseException e) {
         return null;
      }
   }

   @Override
   public void doCheckout(O order) {

      // SetExpressCheckout fields.
      final SetExpressCheckoutReq setExpressCheckoutReq = new SetExpressCheckoutReq();
      final SetExpressCheckoutRequestType setExpressCheckoutRequestType = new SetExpressCheckoutRequestType();
      setExpressCheckoutRequestType.setVersion(VERSION_PROPERTY);

      // SetExpressCheckout request fields.
      setExpressCheckoutRequestType.setSetExpressCheckoutRequestDetails(doSetExpressCheckoutRequestsDetailsType(order));

      // SetExpressCheckout fields.
      setExpressCheckoutReq.setSetExpressCheckoutRequest(setExpressCheckoutRequestType);

      // Do SetExpressCheckout call.
      final SetExpressCheckoutResponseType setExpressCheckoutResponseType = getPayPalAPIAAInterface().setExpressCheckout(setExpressCheckoutReq);

      if (setExpressCheckoutResponseType.getAck() == AckCodeType.SUCCESS) {
         order.setToken(setExpressCheckoutResponseType.getToken());
      } else {
         throw new GNUOpenBusinessServiceException("Exception from Paypal Express Checkout [errors=" + getParameterErrors(setExpressCheckoutResponseType.getErrors()) + "], please try again.");
      }
   }

   @Override
   public void doCheckoutDetails(O order) {

      // GetExpressCheckoutDetails Response Fields
      final GetExpressCheckoutDetailsReq getExpressCheckoutDetailsReq = new GetExpressCheckoutDetailsReq();
      final GetExpressCheckoutDetailsRequestType getExpressCheckoutDetailsRequestType = new GetExpressCheckoutDetailsRequestType();
      getExpressCheckoutDetailsRequestType.setVersion(VERSION_PROPERTY);

      // The timestamped token value that was returned by SetExpressCheckout
      // response and passed on GetExpressCheckoutDetails request.
      getExpressCheckoutDetailsRequestType.setToken(order.getToken());

      // GetExpressCheckoutDetails Response fields.
      getExpressCheckoutDetailsReq.setGetExpressCheckoutDetailsRequest(getExpressCheckoutDetailsRequestType);

      // Do GetExpressCheckoutDetails call.
      final GetExpressCheckoutDetailsResponseType getExpressCheckoutDetailsResponseType = getPayPalAPIAAInterface().getExpressCheckoutDetails(getExpressCheckoutDetailsReq);

      if (getExpressCheckoutDetailsResponseType.getAck() == AckCodeType.SUCCESS) {

         final GetExpressCheckoutDetailsResponseDetailsType getExpressCheckoutDetailsResponseDetailsType = getExpressCheckoutDetailsResponseType.getGetExpressCheckoutDetailsResponseDetails();

         // The timestamped token value that was returned by
         // SetExpressCheckout
         // response and passed on GetExpressCheckoutDetails request.
         order.setToken(getExpressCheckoutDetailsResponseDetailsType.getToken());

         // Information about the payer.
         doPayerInfoType(order, getExpressCheckoutDetailsResponseDetailsType.getPayerInfo());

         // A free-form field for your own use, as set by you in the Custom
         // element of the SetExpressCheckout request.
         order.setCustom(getExpressCheckoutDetailsResponseDetailsType.getCustom());

         // Your own invoice or tracking number, as set by you in the element
         // of
         // the same name in the SetExpressCheckout request.
         order.getInvoice().setInvoiceId(getExpressCheckoutDetailsResponseDetailsType.getInvoiceID());

         // Buyer's contact phone number.
         order.getContract().getCustomer().setContactPhone(getExpressCheckoutDetailsResponseDetailsType.getContactPhone());

         // Information about the payment.
         doPaymentDetailsTypes(order, getExpressCheckoutDetailsResponseDetailsType.getPaymentDetails().get(0));

         // Text entered by the buyer on the PayPal website if you set the
         // AllowNote field to 1 in SetExpressCheckout.
         order.setNote(getExpressCheckoutDetailsResponseDetailsType.getNote());

         // Shipping options and insurance.

         // Status of the checkout session. If payment is completed, the
         // transaction identification number of the resulting transaction is
         // returned.
         order.setCheckoutStatus(getExpressCheckoutDetailsResponseDetailsType.getCheckoutStatus());

         // Gift message entered by the buyer on the PayPal checkout pages.
         order.setGiftMessage(getExpressCheckoutDetailsResponseDetailsType.getGiftMessage());

         // Whether the buyer requested a gift receipt.
         order.setGiftReceiptEnable(Boolean.valueOf(getExpressCheckoutDetailsResponseDetailsType.getGiftReceiptEnable()));

         // Returns the gift wrap amount only if the buyer selects the gift
         // option on the PayPal pages.
         order.setGiftWrapAmount(doBasicAmountType(getExpressCheckoutDetailsResponseDetailsType.getGiftWrapAmount()));

         // Buyer's email address if the buyer provided it on the PayPal
         // pages.
         order.getContract().getCustomer().setBuyerMarketingEmail(getExpressCheckoutDetailsResponseDetailsType.getBuyerMarketingEmail());

         // Survey response the buyer selects on the PayPal pages.

         // Payment request information for each bucket in the cart.
      } else {
         throw new GNUOpenBusinessServiceException("Exception from Paypal Express Checkout Details [errors=" + getParameterErrors(getExpressCheckoutDetailsResponseType.getErrors()) + "], please try again.");
      }
   }

   @Override
   public void doCheckoutPayment(O order) {

      // Do Express Checkout Payment Request Fields
      final DoExpressCheckoutPaymentReq doExpressCheckoutPaymentReq = new DoExpressCheckoutPaymentReq();
      final DoExpressCheckoutPaymentRequestType doExpressCheckoutPaymentRequestType = new DoExpressCheckoutPaymentRequestType();
      doExpressCheckoutPaymentRequestType.setVersion(VERSION_PROPERTY);

      // Do Express Checkout Payment Request Fields.
      doExpressCheckoutPaymentRequestType.setDoExpressCheckoutPaymentRequestDetails(doExpressCheckoutPaymentRequestDetailsType(order));

      // Do Express Checkout Payment Fields.
      doExpressCheckoutPaymentReq.setDoExpressCheckoutPaymentRequest(doExpressCheckoutPaymentRequestType);

      // Do Express Checkout Payment call.
      final DoExpressCheckoutPaymentResponseType doExpressCheckoutPaymentResponseType = getPayPalAPIAAInterface().doExpressCheckoutPayment(doExpressCheckoutPaymentReq);

      if (doExpressCheckoutPaymentResponseType.getAck() == AckCodeType.SUCCESS) {

         final DoExpressCheckoutPaymentResponseDetailsType doExpressCheckoutPaymentResponseDetailsType = doExpressCheckoutPaymentResponseType.getDoExpressCheckoutPaymentResponseDetails();

         // The timestamped token value that was returned by
         // SetExpressCheckout
         // response and passed on GetExpressCheckoutDetails request.
         order.setToken(doExpressCheckoutPaymentResponseDetailsType.getToken());

         // Information about the payment.
         for (final PaymentInfoType paymentInfoType : doExpressCheckoutPaymentResponseDetailsType.getPaymentInfo()) {
            order.getInvoice().getPayments().add(doPaymentInfoType(paymentInfoType));
         }

         // The text entered by the buyer on the PayPal website if you set
         // the
         // AllowNote field to 1 in SetExpressCheckout.
         order.setNote(doExpressCheckoutPaymentResponseDetailsType.getNote());

         // The ID of the billing agreement associated with the Express
         // Checkout
         // transaction.
         order.setBillingAgreementId(doExpressCheckoutPaymentResponseDetailsType.getBillingAgreementID());
      } else {
         throw new GNUOpenBusinessServiceException("Exception from Paypal Express Checkout [errors=" + getParameterErrors(doExpressCheckoutPaymentResponseType.getErrors()) + "], please try again.");
      }
   }

   private DoExpressCheckoutPaymentRequestDetailsType doExpressCheckoutPaymentRequestDetailsType(O order) {
      final DoExpressCheckoutPaymentRequestDetailsType doExpressCheckoutPaymentRequestDetailsType = new DoExpressCheckoutPaymentRequestDetailsType();

      // The timestamped token value that was returned in the
      // SetExpressCheckout
      // response and passed in the GetExpressCheckoutDetails request.
      doExpressCheckoutPaymentRequestDetailsType.setToken(order.getToken());

      // Unique PayPal buyer account identification number as returned in the
      // GetExpressCheckoutDetails response.
      doExpressCheckoutPaymentRequestDetailsType.setPayerID(order.getContract().getCustomer().getPayerId());

      // Information about the payment.
      doExpressCheckoutPaymentRequestDetailsType.getPaymentDetails().add(doPaymentDetailsType(order));

      // Shipping options and insurance.

      // The gift message the buyer entered on the PayPal pages.
      doExpressCheckoutPaymentRequestDetailsType.setGiftMessage(order.getGiftMessage());

      // Whether the buyer selected a gift receipt on the PayPal pages.
      if (order.getGiftMessageEnable() != null) {
         doExpressCheckoutPaymentRequestDetailsType.setGiftReceiptEnable("true");
      }

      // Return the gift wrap name only if the buyer selected the gift option
      // on
      // the PayPal pages.
      if (order.getGiftWrapName() != null) {
         doExpressCheckoutPaymentRequestDetailsType.setGiftWrapName(order.getGiftWrapName());
      }

      // Amount only if the buyer selected the gift option on the PayPal
      // pages.
      if (order.getGiftWrapAmount() != null) {
         doExpressCheckoutPaymentRequestDetailsType.setGiftWrapAmount(doBasicAmountType(order.getGiftWrapAmount()));
      }

      // The buyer email address opted in by the buyer on the PayPal pages.
      doExpressCheckoutPaymentRequestDetailsType.setBuyerMarketingEmail(order.getContract().getCustomer().getBuyerMarketingEmail());
      return doExpressCheckoutPaymentRequestDetailsType;
   }

   private BigDecimal doMesureType(MeasureType measureType) {
      return measureType == null ? null : BigDecimal.valueOf(measureType.getValue());
   }

   private MeasureType doMesureType(String unit, BigDecimal value) {

      if (unit != null && !unit.trim().isEmpty() && value != null) {
         final MeasureType measureType = new MeasureType();
         measureType.setUnit(unit);
         measureType.setValue(value.doubleValue());
      }

      return null;
   }

   @Override
   public O doNotification(O order) {
      return order;
   }

   private void doPayerInfoType(O order, PayerInfoType payerInfo) {
      order.getContract().getCustomer().setPayer(payerInfo.getPayer());
      order.getContract().getCustomer().setPayerId(payerInfo.getPayerID());
      order.getContract().getCustomer().setPayerStatus(payerInfo.getPayerStatus().value());
      order.getContract().getCustomer().setFirstName(payerInfo.getPayerName().getFirstName());
      order.getContract().getCustomer().setSuffix(payerInfo.getPayerName().getSuffix());
      order.getContract().getCustomer().setMiddleName(payerInfo.getPayerName().getMiddleName());
      order.getContract().getCustomer().setLastName(payerInfo.getPayerName().getLastName());
      order.getContract().getCustomer().setSalutation(payerInfo.getPayerName().getSalutation());
      if (payerInfo.getPayerCountry() != null) {
         order.getContract().getCustomer().getAddress().setCountry("_" + payerInfo.getPayerCountry().value());
      }
      order.getContract().getCustomer().setPayerBusiness(payerInfo.getPayerBusiness());
      if (payerInfo.getTaxIdDetails() != null) {
         order.getContract().getCustomer().setTaxIdType(payerInfo.getTaxIdDetails().getTaxIdType());
      }
      if (payerInfo.getTaxIdDetails() != null) {
         order.getContract().getCustomer().setTaxId(payerInfo.getTaxIdDetails().getTaxId());
      }
      order.getContract().getCustomer().setContactPhone(payerInfo.getContactPhone());

      doAddressType(order.getShipment().getAddress(), payerInfo.getAddress());
   }

   private PaymentDetailsItemType doPaymentDetailsItemType(OrderRecord orderRecord) {
      final PaymentDetailsItemType paymentDetailsItemType = new PaymentDetailsItemType();

      paymentDetailsItemType.setName(orderRecord.getName());
      paymentDetailsItemType.setDescription(orderRecord.getDescription());
      paymentDetailsItemType.setAmount(doBasicAmountType(orderRecord.getAmount()));
      paymentDetailsItemType.setNumber(orderRecord.getNumber());
      paymentDetailsItemType.setQuantity(orderRecord.getQuantity());
      paymentDetailsItemType.setTax(doBasicAmountType(orderRecord.getTax()));
      paymentDetailsItemType.setItemWeight(doMesureType(orderRecord.getItemWeightUnit(), orderRecord.getItemWeight()));
      paymentDetailsItemType.setItemLength(doMesureType(orderRecord.getItemLengthUnit(), orderRecord.getItemLength()));
      paymentDetailsItemType.setItemWidth(doMesureType(orderRecord.getItemWidthUnit(), orderRecord.getItemWidth()));
      paymentDetailsItemType.setItemHeight(doMesureType(orderRecord.getItemHeightUnit(), orderRecord.getItemHeight()));
      paymentDetailsItemType.setItemURL(orderRecord.getItemUrl());
      paymentDetailsItemType.setItemCategory(ItemCategoryType.PHYSICAL);
      return paymentDetailsItemType;
   }

   private void doPaymentDetailsItemType(OrderRecord orderRecord, PaymentDetailsItemType paymentDetailsItemType) {
      orderRecord.setName(paymentDetailsItemType.getName());
      orderRecord.setDescription(paymentDetailsItemType.getDescription());
      orderRecord.setAmount(doBasicAmountType(paymentDetailsItemType.getAmount()));
      orderRecord.setNumber(paymentDetailsItemType.getNumber());
      orderRecord.setQuantity(paymentDetailsItemType.getQuantity());
      orderRecord.setTax(doBasicAmountType(paymentDetailsItemType.getTax()));
      orderRecord.setItemWeight(doMesureType(paymentDetailsItemType.getItemWeight()));
      orderRecord.setItemLength(doMesureType(paymentDetailsItemType.getItemLength()));
      orderRecord.setItemWidth(doMesureType(paymentDetailsItemType.getItemWidth()));
      orderRecord.setItemHeight(doMesureType(paymentDetailsItemType.getItemHeight()));
      orderRecord.setItemUrl(paymentDetailsItemType.getItemURL());
   }

   private PaymentDetailsType doPaymentDetailsType(O order) {

      // Information about the payment.
      final PaymentDetailsType paymentDetailsType = new PaymentDetailsType();

      // Total cost of the transaction to the buyer.
      paymentDetailsType.setOrderTotal(doBasicAmountType(order.getOrderTotal()));

      // Sum of cost of all items in this order.
      paymentDetailsType.setItemTotal(doBasicAmountType(order.getItemTotal()));

      // Total shipping costs for this order.
      paymentDetailsType.setShippingTotal(doBasicAmountType(order.getShippingTotal()));

      // Shipping discount for this order
      paymentDetailsType.setShippingDiscount(doBasicAmountType(order.getShippingDiscount().negate()));

      // Indicates whether insurance is available as an option the buyer can
      // choose on the PayPal Review page
      if (order.getInsuranceTotal() != null && order.getInsuranceTotal().compareTo(BigDecimal.ZERO) == 1) {
         paymentDetailsType.setInsuranceOptionOffered("true");

         // Total shipping insurance costs for this order.
         paymentDetailsType.setInsuranceTotal(doBasicAmountType(order.getInsuranceTotal()));
      }

      // Total handling costs for this order.
      paymentDetailsType.setHandlingTotal(doBasicAmountType(order.getHandlingTotal()));

      // Sum of tax for all items in this order.
      paymentDetailsType.setTaxTotal(doBasicAmountType(order.getTaxTotal()));

      // Description of items the buyer is purchasing.
      paymentDetailsType.setOrderDescription(order.getOrderDescription());

      // Your URL for receiving Instant Payment Notification (IPN) about this
      // transaction.
      paymentDetailsType.setNotifyURL(NOTIFY_URL_PROPERTY);

      // Address to which the order is shipped.
      if (order.getShipment() != null) {
         paymentDetailsType.setShipToAddress(doAddress(order.getShipment().getAddress()));
      }

      // Details about each individual item included in the order.
      for (final OrderRecord orderRecord : order.getRecords()) {
         paymentDetailsType.getPaymentDetailsItem().add(doPaymentDetailsItemType(orderRecord));
      }

      // Note to the merchant.
      paymentDetailsType.setNoteText(order.getNoteText());

      // A free-form field for your own use.
      paymentDetailsType.setCustom(order.getCustom());

      // Transaction identification number of the transaction that was
      // created.
      // Note: This field is only returned after a successful transaction for
      // DoExpressCheckout has occurred.
      if (order.getTransactionId() != null) {
         paymentDetailsType.setTransactionId(order.getTransactionId());
      } else {
         order.setTransactionId(order.getToken());
      }

      return paymentDetailsType;
   }

   private void doPaymentDetailsTypes(O order, PaymentDetailsType paymentDetailsType) {
      order.setOrderTotal(doBasicAmountType(paymentDetailsType.getOrderTotal()));
      order.setItemTotal(doBasicAmountType(paymentDetailsType.getItemTotal()));
      order.setShippingTotal(doBasicAmountType(paymentDetailsType.getShippingTotal()));
      order.setInsuranceTotal(doBasicAmountType(paymentDetailsType.getInsuranceTotal()));
      order.setShippingDiscount(doBasicAmountType(paymentDetailsType.getShippingDiscount()).negate());
      order.setInsuranceOptionOffered(new Boolean(paymentDetailsType.getInsuranceOptionOffered()));
      order.setHandlingTotal(doBasicAmountType(paymentDetailsType.getHandlingTotal()));
      order.setTaxTotal(doBasicAmountType(paymentDetailsType.getTaxTotal()));
      order.setOrderDescription(paymentDetailsType.getOrderDescription());
      order.setCustom(paymentDetailsType.getCustom());
      order.getInvoice().setInvoiceId(paymentDetailsType.getInvoiceID());
      doAddressType(order.getShipment().getAddress(), paymentDetailsType.getShipToAddress());

      // Details about each individual item included in the order.
      for (final PaymentDetailsItemType paymentDetailsItemType : paymentDetailsType.getPaymentDetailsItem()) {
         for (final OrderRecord orderRecord : order.getRecords()) {
            if (paymentDetailsItemType.getNumber() != null && paymentDetailsItemType.getNumber().equalsIgnoreCase(orderRecord.getNumber())) {
               doPaymentDetailsItemType(orderRecord, paymentDetailsItemType);
               break;
            }
         }
      }

      order.setNoteText(paymentDetailsType.getNoteText());
   }

   private Payment doPaymentInfoType(PaymentInfoType paymentInfoType) {
      final Payment payment = new Payment();

      // Unique transaction ID of the payment.
      payment.setTransactionId(paymentInfoType.getTransactionID());

      // Type of transaction.
      payment.setTransactionType(paymentInfoType.getTransactionType().value());

      // Indicates whether the payment is instant or delayed.
      payment.setPaymentType(paymentInfoType.getPaymentType().value());

      // Time/date stamp of payment.
      payment.setPaymentDate(new Date(paymentInfoType.getPaymentDate().toGregorianCalendar().getTimeInMillis()));

      // The final amount charged, including any shipping and taxes from your
      // Merchant Profile.
      payment.setGrossAmount(doBasicAmountType(paymentInfoType.getGrossAmount()));

      // PayPal fee amount charged for the transaction.
      payment.setFeeAmount(doBasicAmountType(paymentInfoType.getFeeAmount()));

      // Amount deposited in your PayPal account after a currency conversion.
      payment.setSettleAmount(doBasicAmountType(paymentInfoType.getSettleAmount()));

      // Tax charged on the transaction.
      payment.setTaxAmount(doBasicAmountType(paymentInfoType.getTaxAmount()));

      // Exchange rate if a currency conversion occurred. Relevant only if
      // your
      // are billing in their non-primary currency. If the buyer chooses to
      // pay
      // with a currency other than the non-primary currency, the conversion
      // occurs in the buyer's account.
      payment.setExchangeRate(paymentInfoType.getExchangeRate());

      // The status of the payment.
      payment.setPaymentStatus(paymentInfoType.getPaymentStatus().value());

      // Reason the payment is pending.
      payment.setPendingReason(paymentInfoType.getPendingReason().value());

      // Reason for a reversal if TransactionType is reversal.
      payment.setReasonCode(paymentInfoType.getReasonCode().value());

      // Reason that this payment is being held.
      payment.setHoldDecision(paymentInfoType.getHoldDecision());

      // The kind of seller protection in force for the transaction.
      payment.setProtectionEligibilityType(paymentInfoType.getProtectionEligibilityType());

      // StoreId as entered in the transaction.
      payment.setStoreId(paymentInfoType.getStoreID());

      // TerminalId as entered in the transaction.
      payment.setTerminalId(paymentInfoType.getTerminalID());

      // Unique identifier of the specific payment request. The value should
      // match the one you passed in the DoExpressCheckout request.
      payment.setPaymentRequestId(paymentInfoType.getPaymentRequestID());

      return payment;
   }

   @Override
   public void doRefundTransaction(O order) {
      // TODO Auto-generated method stub

   }

   private SetExpressCheckoutRequestDetailsType doSetExpressCheckoutRequestsDetailsType(O order) {
      // SetExpressCheckout request fields.
      final SetExpressCheckoutRequestDetailsType setExpressCheckoutRequestDetailsType = new SetExpressCheckoutRequestDetailsType();

      // The expected maximum total amount of the complete order, including
      // shipping cost and tax charges.
      setExpressCheckoutRequestDetailsType.setMaxAmount(doBasicAmountType(order.getMaxTotal()));

      // URL to which the buyer's browser is returned after choosing to pay
      // with
      // PayPal. For digital goods.
      setExpressCheckoutRequestDetailsType.setReturnURL(REDIRECT_URL_PROPERTY);

      // URL to which the buyer is returned if the buyer does not approve the
      // use of PayPal to pay you. For digital goods.
      setExpressCheckoutRequestDetailsType.setCancelURL(CANCEL_URL_PROPERTY);

      // Indicates whether or not you require the buyer's shipping address on
      // file with PayPal be a confirmed address.
      setExpressCheckoutRequestDetailsType.setReqConfirmShipping("1");

      // Determines where or not PayPal displays shipping address fields on
      // the
      // PayPal pages.
      setExpressCheckoutRequestDetailsType.setNoShipping("0");

      // Enables the buyer to enter a note to the merchant on the PayPal page
      // during checkout.
      setExpressCheckoutRequestDetailsType.setAllowNote("1");

      // SetExpressCheckout request details fields.
      setExpressCheckoutRequestDetailsType.getPaymentDetails().add(doPaymentDetailsType(order));

      // Determines whether or not the PayPal pages should display the
      // shipping
      // address set by you in this SetExpressCheckout request.
      setExpressCheckoutRequestDetailsType.setAddressOverride("1");

      // Merchant Customer Service number displayed on the PayPal pages.
      setExpressCheckoutRequestDetailsType.setCustomerServiceNumber(CUSTOMER_SERVICE_NUMBER_PROPERTY);

      // Enables the gift message widget on the PayPal pages.
      if (order.getGiftMessageEnable() != null && order.getGiftMessageEnable()) {
         setExpressCheckoutRequestDetailsType.setGiftMessageEnable("1");
      }

      // Enable gift receipt widget on the PayPal pages.
      if (order.getGiftReceiptEnable() != null && order.getGiftReceiptEnable()) {
         setExpressCheckoutRequestDetailsType.setGiftReceiptEnable("1");
      }

      // Enable gift wrap widget on the PayPal pages.
      if (order.getGiftWrapEnable() != null && order.getGiftWrapEnable()) {
         setExpressCheckoutRequestDetailsType.setGiftWrapEnable("1");
         setExpressCheckoutRequestDetailsType.setGiftWrapName(order.getGiftWrapName());
         setExpressCheckoutRequestDetailsType.setGiftWrapAmount(doBasicAmountType(order.getGiftWrapAmount()));
      }

      // PageStyle, logos etc. on PayPal site.

      // Email address of the buyer as entered during checkout.
      setExpressCheckoutRequestDetailsType.setBuyerEmail(order.getContract().getCustomer().getBuyerEmail());

      // Set brand name.
      setExpressCheckoutRequestDetailsType.setBrandName("-");

      // Your own invoice or tracking number.
      setExpressCheckoutRequestDetailsType.setInvoiceID(order.getInvoice().getInvoiceId());
      return setExpressCheckoutRequestDetailsType;
   }

   @Override
   public void doTransactionDetails(O order) {
      // TODO Auto-generated method stub

   }

   private String getParameterErrors(List<ErrorType> errors) {

      final class PayPalError extends ErrorType {

         public PayPalError(ErrorType errorType) {
            setErrorCode(errorType.getErrorCode());
            setLongMessage(errorType.getLongMessage());
            setSeverityCode(errorType.getSeverityCode());
            setShortMessage(errorType.getShortMessage());
         }

         @Override
         public String toString() {
            return "{errorCode=" + getErrorCode() + " longMessage=" + getLongMessage() + " severityCode=" + getSeverityCode().value() + " shortMessage=" + getShortMessage() + "}";
         }
      }

      final StringBuilder stringBuilder = new StringBuilder();

      PayPalError payPalError = null;
      for (final ErrorType errorType : errors) {
         payPalError = new PayPalError(errorType);
         stringBuilder.append(payPalError.toString());
      }

      return stringBuilder.toString();
   }

   private PayPalAPIAAInterface getPayPalAPIAAInterface() {
      final PayPalAPIAAInterface port = payPalAPIInterfaceService.getPayPalAPIAA();

      final UserIdPasswordType userIdPasswordType = new UserIdPasswordType();
      userIdPasswordType.setUsername(USERNAME_PROPERTY);
      userIdPasswordType.setPassword(PASSWORD_PROPERTY);
      userIdPasswordType.setSignature(SIGNATURE_PROPERTY);
      userIdPasswordType.setSubject(SUBJECT_PROPERTY);

      final CustomSecurityHeaderType customSecurityHeaderType = new CustomSecurityHeaderType();
      customSecurityHeaderType.setCredentials(userIdPasswordType);

      final List<Header> headers = new ArrayList<Header>();
      try {
         final Header header = new Header(new QName("urn:ebay:api:PayPalAPI", "RequesterCredentials"), customSecurityHeaderType, new JAXBDataBinding(CustomSecurityHeaderType.class));
         headers.add(header);
      } catch (final JAXBException e) {
         throw new GNUOpenBusinessServiceException("Exception from Paypal Express Requester Credentials, please try again.", e);
      }

      ((BindingProvider) port).getRequestContext().put(Header.HEADER_LIST, headers);

      ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, SITE_PROPERTY);

      return port;
   }
}
