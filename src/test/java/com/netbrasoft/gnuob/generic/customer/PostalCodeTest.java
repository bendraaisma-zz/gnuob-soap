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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PostalCodeTest {

  private PostalCode spyPostalCode;

  @Before
  public void setUp() throws Exception {
    spyPostalCode = spy(PostalCode.class);
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testGetAccuracy() {
    assertNull("Accuracy", spyPostalCode.getAccuracy());
    verify(spyPostalCode, times(1)).getAccuracy();
  }

  @Test
  public void testGetAdminCode1() {
    assertNull("AdminCode1", spyPostalCode.getAdminCode1());
    verify(spyPostalCode, times(1)).getAdminCode1();
  }

  @Test
  public void testGetAdminCode2() {
    assertNull("AdminCode2", spyPostalCode.getAdminCode2());
    verify(spyPostalCode, times(1)).getAdminCode2();
  }

  @Test
  public void testGetAdminCode3() {
    assertNull("AdminCode3", spyPostalCode.getAdminCode3());
    verify(spyPostalCode, times(1)).getAdminCode3();
  }

  @Test
  public void testGetAdminName1() {
    assertNull("AdminName1", spyPostalCode.getAdminName1());
    verify(spyPostalCode, times(1)).getAdminName1();
  }

  @Test
  public void testGetAdminName2() {
    assertNull("AdminName2", spyPostalCode.getAdminName2());
    verify(spyPostalCode, times(1)).getAdminName2();
  }

  @Test
  public void testGetAdminName3() {
    assertNull("AdminName3", spyPostalCode.getAdminName3());
    verify(spyPostalCode, times(1)).getAdminName3();
  }

  @Test
  public void testGetCountryCode() {
    assertNull("CountryCode", spyPostalCode.getCountryCode());
    verify(spyPostalCode, times(1)).getCountryCode();
  }

  @Test
  public void testGetLatitude() {
    assertNull("Latitude", spyPostalCode.getLatitude());
    verify(spyPostalCode, times(1)).getLatitude();
  }

  @Test
  public void testGetLongitude() {
    assertNull("Longitude", spyPostalCode.getLongitude());
    verify(spyPostalCode, times(1)).getLongitude();
  }

  @Test
  public void testGetPlaceName() {
    assertNull("PlaceName", spyPostalCode.getPlaceName());
    verify(spyPostalCode, times(1)).getPlaceName();
  }

  @Test
  public void testGetPostalCode() {
    assertNull("PostalCode", spyPostalCode.getPostalCode());
    verify(spyPostalCode, times(1)).getPostalCode();
  }

  @Test
  public void testPostalCodeIsDetached() {
    spyPostalCode.setId(1L);
    assertTrue("Contract", spyPostalCode.isDetached());
    verify(spyPostalCode, times(1)).isDetached();
  }

  @Test
  public void testPostalCodeIsNotDetached() {
    spyPostalCode.setId(0L);
    assertFalse("Contract", spyPostalCode.isDetached());
    verify(spyPostalCode, times(1)).isDetached();
  }

  @Test
  public void testPrePersist() {
    spyPostalCode.prePersist();
  }

  @Test
  public void testPreUpdate() {
    spyPostalCode.preUpdate();
  }

  @Test
  public void testSetAccuracy() {
    spyPostalCode.setAccuracy("Holding");
    assertEquals("Accuracy", "Holding", spyPostalCode.getAccuracy());
    verify(spyPostalCode, times(1)).setAccuracy(anyString());
  }

  @Test
  public void testSetAdminCode1() {
    spyPostalCode.setAdminCode1("Holding");
    assertEquals("AdminCode1", "Holding", spyPostalCode.getAdminCode1());
    verify(spyPostalCode, times(1)).setAdminCode1(anyString());
  }

  @Test
  public void testSetAdminCode2() {
    spyPostalCode.setAdminCode2("Holding");
    assertEquals("AdminCode2", "Holding", spyPostalCode.getAdminCode2());
    verify(spyPostalCode, times(1)).setAdminCode2(anyString());
  }

  @Test
  public void testSetAdminCode3() {
    spyPostalCode.setAdminCode3("Holding");
    assertEquals("AdminCode3", "Holding", spyPostalCode.getAdminCode3());
    verify(spyPostalCode, times(1)).setAdminCode3(anyString());
  }

  @Test
  public void testSetAdminName1() {
    spyPostalCode.setAdminName1("Holding");
    assertEquals("AdminName1", "Holding", spyPostalCode.getAdminName1());
    verify(spyPostalCode, times(1)).setAdminName1(anyString());
  }

  @Test
  public void testSetAdminName2() {
    spyPostalCode.setAdminName2("Holding");
    assertEquals("AdminName2", "Holding", spyPostalCode.getAdminName2());
    verify(spyPostalCode, times(1)).setAdminName2(anyString());
  }

  @Test
  public void testSetAdminName3() {
    spyPostalCode.setAdminName3("Holding");
    assertEquals("AdminName3", "Holding", spyPostalCode.getAdminName3());
    verify(spyPostalCode, times(1)).setAdminName3(anyString());
  }

  @Test
  public void testSetCountryCode() {
    spyPostalCode.setCountryCode("Holding");
    assertEquals("CountryCode", "Holding", spyPostalCode.getCountryCode());
    verify(spyPostalCode, times(1)).setCountryCode(anyString());
  }

  @Test
  public void testSetLatitude() {
    spyPostalCode.setLatitude(null);
    assertNull("Latitude", spyPostalCode.getLatitude());
    verify(spyPostalCode, times(1)).setLatitude(null);
  }

  @Test
  public void testSetLongitude() {
    spyPostalCode.setLongitude(null);
    assertNull("Longitude", spyPostalCode.getLongitude());
    verify(spyPostalCode, times(1)).setLongitude(null);
  }

  @Test
  public void testSetPlaceName() {
    spyPostalCode.setPlaceName("Holding");
    assertEquals("PlaceName", "Holding", spyPostalCode.getPlaceName());
    verify(spyPostalCode, times(1)).setPlaceName(anyString());
  }

  @Test
  public void testSetPostalCode() {
    spyPostalCode.setPostalCode("Holding");
    assertEquals("PostalCode", "Holding", spyPostalCode.getPostalCode());
    verify(spyPostalCode, times(1)).setPostalCode(anyString());
  }
}
