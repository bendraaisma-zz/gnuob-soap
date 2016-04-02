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

package com.netbrasoft.gnuob.generic.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.collections.Sets.newSet;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.netbrasoft.gnuob.generic.content.contexts.ContextVisitorImpl;
import com.netbrasoft.gnuob.generic.contract.Contract;

public class OrderTest {

  private Contract mockContract;
  private OrderRecord mockOrderRecord;
  private Order spyOrder;

  @Before
  public void setUp() throws Exception {
    mockContract = mock(Contract.class);
    mockOrderRecord = mock(OrderRecord.class);
    spyOrder = spy(Order.class);
    spyOrder.setContract(mockContract);
    spyOrder.setRecords(newSet(mockOrderRecord));
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testAccept() {
    assertNotNull("Context", spyOrder.accept(new ContextVisitorImpl()));
    verify(spyOrder, times(1)).accept(any());
  }

  @Test
  public void testGetContract() {
    assertNotNull("Contract", spyOrder.getContract());
    verify(spyOrder, times(1)).getContract();
  }

  @Test
  public void testGetDiscountTotal() {
    when(mockOrderRecord.getDiscountTotal()).thenReturn(BigDecimal.ZERO);
    assertNotNull("DiscountTotal", spyOrder.getDiscountTotal());
    verify(spyOrder, times(1)).getDiscountTotal();
  }

  @Test
  public void testGetExtraAmount() {
    when(mockOrderRecord.getTaxTotal()).thenReturn(BigDecimal.ZERO);
    assertNotNull("ExtraAmount", spyOrder.getExtraAmount());
    verify(spyOrder, times(1)).getExtraAmount();
  }

  @Test
  public void testGetHandlingTotal() {
    assertNotNull("HandlingTotal", spyOrder.getHandlingTotal());
    verify(spyOrder, times(1)).getHandlingTotal();
  }

  @Test
  public void testGetInsuranceTotal() {
    assertNotNull("InsuranceTotal", spyOrder.getInsuranceTotal());
    verify(spyOrder, times(1)).getInsuranceTotal();
  }

  @Test
  public void testGetItemTotal() {
    when(mockOrderRecord.getItemTotal()).thenReturn(BigDecimal.ZERO);
    assertNotNull("ItemTotal", spyOrder.getItemTotal());
    verify(spyOrder, times(1)).getItemTotal();
  }

  @Test
  public void testGetMaxTotal() {
    when(mockOrderRecord.getItemTotal()).thenReturn(BigDecimal.ZERO);
    when(mockOrderRecord.getShippingTotal()).thenReturn(BigDecimal.ZERO);
    when(mockOrderRecord.getTaxTotal()).thenReturn(BigDecimal.ZERO);
    assertNotNull("MaxTotal", spyOrder.getMaxTotal());
    verify(spyOrder, times(1)).getMaxTotal();
  }

  @Test
  public void testGetOrderDescription() {
    assertNull("OrderDescription", spyOrder.getOrderDescription());
    verify(spyOrder, times(1)).getOrderDescription();
  }

  @Test
  public void testGetOrderId() {
    assertNotNull("OrderId", spyOrder.getOrderId());
    verify(spyOrder, times(1)).getOrderId();
  }

  @Test
  public void testGetOrderTotal() {
    when(mockOrderRecord.getItemTotal()).thenReturn(BigDecimal.ZERO);
    when(mockOrderRecord.getShippingTotal()).thenReturn(BigDecimal.ZERO);
    when(mockOrderRecord.getTaxTotal()).thenReturn(BigDecimal.ZERO);
    assertNotNull("OrderTotal", spyOrder.getOrderTotal());
    verify(spyOrder, times(1)).getOrderTotal();
  }

  @Test
  public void testGetRecords() {
    assertNotNull("Records", spyOrder.getRecords());
    verify(spyOrder, times(1)).getRecords();
  }

  @Test
  public void testGetShippingDiscount() {
    assertNotNull("ShippingDiscount", spyOrder.getShippingDiscount());
    verify(spyOrder, times(1)).getShippingDiscount();
  }

  @Test
  public void testGetShippingTotal() {
    when(mockOrderRecord.getShippingTotal()).thenReturn(BigDecimal.ZERO);
    assertNotNull("ShippingTotal", spyOrder.getShippingTotal());
    verify(spyOrder, times(1)).getShippingTotal();
  }

  @Test
  public void testGetTaxTotal() {
    when(mockOrderRecord.getTaxTotal()).thenReturn(BigDecimal.ZERO);
    assertNotNull("TaxTotal", spyOrder.getTaxTotal());
    verify(spyOrder, times(1)).getTaxTotal();
  }

  @Test
  public void testOrderIsDetached() {
    spyOrder.setId(1L);
    when(mockContract.isDetached()).thenReturn(false);
    when(mockOrderRecord.isDetached()).thenReturn(false);
    assertTrue("Order", spyOrder.isDetached());
    verify(spyOrder, times(1)).isDetached();
    verify(mockContract, never()).isDetached();
    verify(mockOrderRecord, never()).isDetached();
  }

  @Test
  public void testOrderIsDetachedByContract() {
    spyOrder.setId(0L);
    when(mockContract.isDetached()).thenReturn(true);
    when(mockOrderRecord.isDetached()).thenReturn(false);
    assertTrue("Order", spyOrder.isDetached());
    verify(spyOrder, times(1)).isDetached();
    verify(mockContract, times(1)).isDetached();
    verify(mockOrderRecord, never()).isDetached();
  }

  @Test
  public void testOrderIsDetachedByRecords() {
    spyOrder.setId(0L);
    when(mockContract.isDetached()).thenReturn(false);
    when(mockOrderRecord.isDetached()).thenReturn(true);
    assertTrue("Order", spyOrder.isDetached());
    verify(spyOrder, times(1)).isDetached();
    verify(mockContract, times(1)).isDetached();
    verify(mockOrderRecord, times(1)).isDetached();
  }

  @Test
  public void testOrderIsNotDetached() {
    spyOrder.setId(0L);
    when(mockContract.isDetached()).thenReturn(false);
    when(mockOrderRecord.isDetached()).thenReturn(false);
    assertFalse("Order", spyOrder.isDetached());
    verify(spyOrder, times(1)).isDetached();
    verify(mockContract, times(1)).isDetached();
    verify(mockOrderRecord, times(1)).isDetached();
  }

  @Test
  public void testOrderIsNotDetachedByRecordsIsNull() {
    spyOrder.setId(0L);
    spyOrder.setRecords(null);
    when(mockContract.isDetached()).thenReturn(false);
    assertFalse("Order", spyOrder.isDetached());
    verify(spyOrder, times(1)).isDetached();
    verify(mockContract, times(1)).isDetached();
    verify(mockOrderRecord, never()).isDetached();
  }

  @Test
  public void testOrderIsNotDetachedWithContractIsNull() {
    spyOrder.setId(0L);
    spyOrder.setContract(null);
    when(mockOrderRecord.isDetached()).thenReturn(false);
    assertFalse("Order", spyOrder.isDetached());
    verify(spyOrder, times(1)).isDetached();
    verify(mockContract, never()).isDetached();
    verify(mockOrderRecord, times(1)).isDetached();
  }

  @Test
  public void testPrePersist() {
    when(mockOrderRecord.getDiscountTotal()).thenReturn(BigDecimal.ZERO);
    when(mockOrderRecord.getItemTotal()).thenReturn(BigDecimal.ZERO);
    when(mockOrderRecord.getShippingTotal()).thenReturn(BigDecimal.ZERO);
    when(mockOrderRecord.getTaxTotal()).thenReturn(BigDecimal.ZERO);
    spyOrder.prePersist();
    verify(spyOrder, times(1)).getOrderId();
    verify(spyOrder, times(2)).getTaxTotal();
    verify(spyOrder, times(2)).getShippingTotal();
    verify(spyOrder, times(2)).getOrderTotal();
    verify(spyOrder, times(2)).getItemTotal();
    verify(spyOrder, times(1)).getMaxTotal();
    verify(spyOrder, times(1)).getDiscountTotal();
    verify(mockOrderRecord, times(1)).setPosition(anyInt());
  }

  @Test
  public void testPreUpdate() {
    spyOrder.preUpdate();
    verify(mockOrderRecord, times(1)).setPosition(anyInt());
  }

  @Test
  public void testSetContract() {
    spyOrder.setContract(null);
    assertNull("Contract", spyOrder.getContract());
    verify(spyOrder, times(1)).setContract(null);
  }

  @Test
  public void testSetDiscountTotal() {
    spyOrder.setDiscountTotal(BigDecimal.ZERO);
    assertNotNull("DiscountTotal", spyOrder.getDiscountTotal());
    verify(spyOrder, times(1)).setDiscountTotal(any());
  }

  @Test
  public void testSetExtraAmount() {
    spyOrder.setExtraAmount(BigDecimal.ZERO);
    assertNotNull("ExtraAmount", spyOrder.getExtraAmount());
    verify(spyOrder, times(1)).setExtraAmount(any());
  }

  @Test
  public void testSetHandlingTotal() {
    spyOrder.setHandlingTotal(BigDecimal.ZERO);
    assertNotNull("HandlingTotal", spyOrder.getHandlingTotal());
    verify(spyOrder, times(1)).setHandlingTotal(any());
  }

  @Test
  public void testSetInsuranceTotal() {
    spyOrder.setInsuranceTotal(BigDecimal.ZERO);
    assertNotNull("InsuranceTotal", spyOrder.getInsuranceTotal());
    verify(spyOrder, times(1)).setInsuranceTotal(any());
  }

  @Test
  public void testSetItemTotal() {
    spyOrder.setItemTotal(BigDecimal.ZERO);
    assertNotNull("ItemTotal", spyOrder.getItemTotal());
    verify(spyOrder, times(1)).setItemTotal(any());
  }

  @Test
  public void testSetMaxTotal() {
    spyOrder.setMaxTotal(BigDecimal.ZERO);
    assertNotNull("MaxTotal", spyOrder.getMaxTotal());
    verify(spyOrder, times(1)).setMaxTotal(any());
  }

  @Test
  public void testSetOrderDescription() {
    spyOrder.setOrderDescription("Folly words widow one downs few age every seven.");
    assertEquals("OrderDescription", "Folly words widow one downs few age every seven.",
        spyOrder.getOrderDescription());
    verify(spyOrder, times(1)).setOrderDescription(anyString());
  }

  @Test
  public void testSetOrderId() {
    spyOrder.setOrderId("Id");
    assertEquals("OrderId", "Id", spyOrder.getOrderId());
    verify(spyOrder, times(1)).setOrderId(anyString());
  }

  @Test
  public void testSetOrderIdWithEmptyString() {
    spyOrder.setOrderId("");
    assertNotEquals("OrderId", " ", spyOrder.getOrderId());
    verify(spyOrder, times(1)).setOrderId(anyString());
  }

  @Test
  public void testSetOrderTotal() {
    spyOrder.setOrderTotal(BigDecimal.ZERO);
    assertNotNull("OrderTotal", spyOrder.getOrderTotal());
    verify(spyOrder, times(1)).setOrderTotal(any());
  }

  @Test
  public void testSetRecords() {
    spyOrder.setRecords(null);
    assertNull("Records", spyOrder.getRecords());
    verify(spyOrder, times(1)).setRecords(null);
  }

  @Test
  public void testSetShippingDiscount() {
    spyOrder.setShippingDiscount(BigDecimal.ZERO);
    assertNotNull("ShippingDiscount", spyOrder.getShippingDiscount());
    verify(spyOrder, times(1)).setShippingDiscount(any());
  }

  @Test
  public void testSetShippingTotal() {
    spyOrder.setShippingTotal(BigDecimal.ZERO);
    assertNotNull("ShippingTotal", spyOrder.getShippingTotal());
    verify(spyOrder, times(1)).setShippingTotal(any());
  }

  @Test
  public void testSetTaxTotal() {
    spyOrder.setTaxTotal(BigDecimal.ZERO);
    assertNotNull("TaxTotal", spyOrder.getTaxTotal());
    verify(spyOrder, times(1)).setTaxTotal(any());
  }
}
