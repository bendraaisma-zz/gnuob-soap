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

package com.netbrasoft.gnuob.generic.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.netbrasoft.gnuob.generic.content.contexts.ContextVisitorImpl;

public class CustomerTest {

  private Customer spyCustomer;
  private Address mockAddress;

  @Before
  public void setUp() throws Exception {
    mockAddress = mock(Address.class);
    spyCustomer = spy(Customer.class);
    spyCustomer.setAddress(mockAddress);
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testAccept() {
    assertNotNull("Context", spyCustomer.accept(new ContextVisitorImpl()));
    verify(spyCustomer, times(1)).accept(any());
  }

  @Test
  public void testGetAddress() {
    assertNotNull("Address", spyCustomer.getAddress());
    verify(spyCustomer, times(1)).getAddress();
  }

  @Test
  public void testGetBuyerEmail() {
    assertNull("BuyerEmail", spyCustomer.getBuyerEmail());
    verify(spyCustomer, times(1)).getBuyerEmail();
  }

  @Test
  public void testGetBuyerMarketingEmail() {
    assertNull("BuyerMarketingEmail", spyCustomer.getBuyerMarketingEmail());
    verify(spyCustomer, times(1)).getBuyerMarketingEmail();
  }

  @Test
  public void testGetContactPhone() {
    assertNull("ContactPhone", spyCustomer.getContactPhone());
    verify(spyCustomer, times(1)).getContactPhone();
  }

  @Test
  public void testGetDateOfBirth() {
    assertNull("DateOfBirth", spyCustomer.getDateOfBirth());
    verify(spyCustomer, times(1)).getDateOfBirth();
  }

  @Test
  public void testGetFirstName() {
    assertNull("FirstName", spyCustomer.getFirstName());
    verify(spyCustomer, times(1)).getFirstName();
  }

  @Test
  public void testGetFriendlyNameWithMiddleNameIsNull() {
    when(spyCustomer.getMiddleName()).thenReturn(null);
    when(spyCustomer.getFirstName()).thenReturn("First");
    when(spyCustomer.getLastName()).thenReturn("Last");
    assertEquals("FriendlyName", "First Last", spyCustomer.getFriendlyName());
    verify(spyCustomer, times(1)).getFriendlyName();
  }

  @Test
  public void testGetFriendlyNameWithMiddleNameIsEmpty() {
    when(spyCustomer.getMiddleName()).thenReturn(" ");
    when(spyCustomer.getFirstName()).thenReturn("First");
    when(spyCustomer.getLastName()).thenReturn("Last");
    assertEquals("FriendlyName", "First Last", spyCustomer.getFriendlyName());
    verify(spyCustomer, times(1)).getFriendlyName();
  }

  @Test
  public void testGetFriendlyNameWithMiddleNameIsNotEmpty() {
    when(spyCustomer.getMiddleName()).thenReturn("Middle");
    when(spyCustomer.getFirstName()).thenReturn("First");
    when(spyCustomer.getLastName()).thenReturn("Last");
    assertEquals("FriendlyName", "First Middle Last", spyCustomer.getFriendlyName());
    verify(spyCustomer, times(1)).getFriendlyName();
  }

  @Test
  public void testGetLastName() {
    assertNull("LastName", spyCustomer.getLastName());
    verify(spyCustomer, times(1)).getLastName();
  }

  @Test
  public void testGetMiddleName() {
    assertNull("MiddleName", spyCustomer.getMiddleName());
    verify(spyCustomer, times(1)).getMiddleName();
  }

  @Test
  public void testGetPayer() {
    assertNull("Payer", spyCustomer.getPayer());
    verify(spyCustomer, times(1)).getPayer();
  }

  @Test
  public void testGetPayerBusiness() {
    assertNull("PayerBusiness", spyCustomer.getPayerBusiness());
    verify(spyCustomer, times(1)).getPayerBusiness();
  }

  @Test
  public void testGetPayerId() {
    assertNull("PayerId", spyCustomer.getPayerId());
    verify(spyCustomer, times(1)).getPayerId();
  }

  @Test
  public void testGetPayerStatus() {
    assertNull("PayerStatus", spyCustomer.getPayerStatus());
    verify(spyCustomer, times(1)).getPayerStatus();
  }

  @Test
  public void testGetSalutation() {
    assertNull("Salutation", spyCustomer.getSalutation());
    verify(spyCustomer, times(1)).getSalutation();
  }

  @Test
  public void testGetSuffix() {
    assertNull("Suffix", spyCustomer.getSuffix());
    verify(spyCustomer, times(1)).getSuffix();
  }

  @Test
  public void testGetTaxId() {
    assertNull("TaxId", spyCustomer.getTaxId());
    verify(spyCustomer, times(1)).getTaxId();
  }

  @Test
  public void testGetTaxIdType() {
    assertNull("TaxIdType", spyCustomer.getTaxIdType());
    verify(spyCustomer, times(1)).getTaxIdType();
  }

  @Test
  public void testCustomerIsDetached() {
    spyCustomer.setId(1L);
    when(mockAddress.isDetached()).thenReturn(false);
    assertTrue("Contract", spyCustomer.isDetached());
    verify(spyCustomer, times(1)).isDetached();
  }

  @Test
  public void testCustomerIsDetachedByAddress() {
    spyCustomer.setId(0L);
    when(mockAddress.isDetached()).thenReturn(true);
    assertTrue("Contract", spyCustomer.isDetached());
    verify(spyCustomer, times(1)).isDetached();
    verify(mockAddress, times(1)).isDetached();
  }

  @Test
  public void testCustomerIsNotDetached() {
    spyCustomer.setId(0L);
    when(mockAddress.isDetached()).thenReturn(false);
    assertFalse("Contract", spyCustomer.isDetached());
    verify(spyCustomer, times(1)).isDetached();
    verify(mockAddress, times(1)).isDetached();
  }

  @Test
  public void testCustomerIsNotDetachedWithAddressIsNull() {
    spyCustomer.setId(0L);
    spyCustomer.setAddress(null);
    assertFalse("Contract", spyCustomer.isDetached());
    verify(spyCustomer, times(1)).isDetached();
  }

  @Test
  public void testPrePersist() {
    spyCustomer.prePersist();
  }

  @Test
  public void testPreUpdate() {
    spyCustomer.preUpdate();
  }

  @Test
  public void testSetAddress() {
    spyCustomer.setAddress(null);
    assertNull("Address", spyCustomer.getAddress());
    verify(spyCustomer, times(1)).setAddress(null);
  }

  @Test
  public void testSetBuyerEmail() {
    spyCustomer.setBuyerEmail("Holding");
    assertEquals("BuyerEmail", "Holding", spyCustomer.getBuyerEmail());
    verify(spyCustomer, times(1)).setBuyerEmail(anyString());
  }

  @Test
  public void testSetBuyerMarketingEmail() {
    spyCustomer.setBuyerMarketingEmail("Holding");
    assertEquals("BuyerMarketingEmail", "Holding", spyCustomer.getBuyerMarketingEmail());
    verify(spyCustomer, times(1)).setBuyerMarketingEmail(anyString());
  }

  @Test
  public void testSetContactPhone() {
    spyCustomer.setContactPhone("Holding");
    assertEquals("ContactPhone", "Holding", spyCustomer.getContactPhone());
    verify(spyCustomer, times(1)).setContactPhone(anyString());
  }

  @Test
  public void testSetDateOfBirth() {
    spyCustomer.setDateOfBirth(null);
    assertNull("DateOfBirth", spyCustomer.getDateOfBirth());
    verify(spyCustomer, times(1)).setDateOfBirth(null);
  }

  @Test
  public void testSetFirstName() {
    spyCustomer.setFirstName("Holding");
    assertEquals("FirstName", "Holding", spyCustomer.getFirstName());
    verify(spyCustomer, times(1)).setFirstName(anyString());
  }

  @Test
  public void testSetFriendlyName() {
    spyCustomer.setFriendlyName("Holding");
    assertEquals("FriendlyName", "Holding", spyCustomer.getFriendlyName());
    verify(spyCustomer, times(1)).setFriendlyName(anyString());
  }

  @Test
  public void testSetLastName() {
    spyCustomer.setLastName("Holding");
    assertEquals("LastName", "Holding", spyCustomer.getLastName());
    verify(spyCustomer, times(1)).setLastName(anyString());
  }

  @Test
  public void testSetMiddleName() {
    spyCustomer.setMiddleName("Holding");
    assertEquals("MiddleName", "Holding", spyCustomer.getMiddleName());
    verify(spyCustomer, times(1)).setMiddleName(anyString());
  }

  @Test
  public void testSetPayer() {
    spyCustomer.setPayer("Holding");
    assertEquals("Payer", "Holding", spyCustomer.getPayer());
    verify(spyCustomer, times(1)).setPayer(anyString());
  }

  @Test
  public void testSetPayerBusiness() {
    spyCustomer.setPayerBusiness("Holding");
    assertEquals("PayerBusiness", "Holding", spyCustomer.getPayerBusiness());
    verify(spyCustomer, times(1)).setPayerBusiness(anyString());
  }

  @Test
  public void testSetPayerId() {
    spyCustomer.setPayerId("Holding");
    assertEquals("PayerId", "Holding", spyCustomer.getPayerId());
    verify(spyCustomer, times(1)).setPayerId(anyString());
  }

  @Test
  public void testSetPayerStatus() {
    spyCustomer.setPayerStatus("Holding");
    assertEquals("PayerStatus", "Holding", spyCustomer.getPayerStatus());
    verify(spyCustomer, times(1)).setPayerStatus(anyString());
  }

  @Test
  public void testSetSalutation() {
    spyCustomer.setSalutation("Holding");
    assertEquals("Salutation", "Holding", spyCustomer.getSalutation());
    verify(spyCustomer, times(1)).setSalutation(anyString());
  }

  @Test
  public void testSetSuffix() {
    spyCustomer.setSuffix("Holding");
    assertEquals("Suffix", "Holding", spyCustomer.getSuffix());
    verify(spyCustomer, times(1)).setSuffix(anyString());
  }

  @Test
  public void testSetTaxId() {
    spyCustomer.setTaxId("Holding");
    assertEquals("TaxId", "Holding", spyCustomer.getTaxId());
    verify(spyCustomer, times(1)).setTaxId(anyString());
  }

  @Test
  public void testSetTaxIdType() {
    spyCustomer.setTaxIdType("Holding");
    assertEquals("TaxIdType", "Holding", spyCustomer.getTaxIdType());
    verify(spyCustomer, times(1)).setTaxIdType(anyString());
  }
}
