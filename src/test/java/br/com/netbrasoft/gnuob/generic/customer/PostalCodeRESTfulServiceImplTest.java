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

package br.com.netbrasoft.gnuob.generic.customer;

import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.UNCHECKED_VALUE;
import static br.com.netbrasoft.gnuob.generic.OrderByEnum.NONE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import br.com.netbrasoft.gnuob.generic.IGenericTypeWebService;
import br.com.netbrasoft.gnuob.generic.Paging;
import br.com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeService;
import br.com.netbrasoft.gnuob.generic.security.MetaData;

public class PostalCodeRESTfulServiceImplTest {

  private IGenericTypeWebService<PostalCode> postalCodeRESTfulServiceImpl = new PostalCodeRESTfulServiceImpl();
  private MetaData mockCredentials;
  private Paging mockPaging;
  private ISecuredGenericTypeService<PostalCode> mockSecuredGenericPostalCodeService;
  private PostalCode spyPostalCode;

  @SuppressWarnings(UNCHECKED_VALUE)
  @Before
  public void setUp() throws Exception {
    mockCredentials = mock(MetaData.class);
    mockPaging = mock(Paging.class);
    spyPostalCode = spy(PostalCode.class);
    mockSecuredGenericPostalCodeService = mock(ISecuredGenericTypeService.class);
    postalCodeRESTfulServiceImpl = new PostalCodeRESTfulServiceImpl(mockSecuredGenericPostalCodeService);
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testCountPostalCode() {
    assertEquals("Count", 0, postalCodeRESTfulServiceImpl.count(mockCredentials, spyPostalCode));
    verify(mockSecuredGenericPostalCodeService, times(1)).count(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testCountPostalCodeGettingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericPostalCodeService.count(any(), any())).thenThrow(new RuntimeException());
    postalCodeRESTfulServiceImpl.count(mockCredentials, spyPostalCode);
  }

  @Test
  public void testDeleteCategoryWithNullPostalCodesAndSubCategories() {
    postalCodeRESTfulServiceImpl.remove(mockCredentials, spyPostalCode);
    verify(mockSecuredGenericPostalCodeService, times(1)).remove(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testDeleteCategoryGettingGNUOpenBusinessServiceException() {
    postalCodeRESTfulServiceImpl = new PostalCodeWebServiceImpl<>();
    postalCodeRESTfulServiceImpl.remove(mockCredentials, spyPostalCode);
  }

  @Test
  public void testFindPostalCode() {
    assertNull("Find", postalCodeRESTfulServiceImpl.find(mockCredentials, spyPostalCode));
    verify(mockSecuredGenericPostalCodeService, times(1)).find(any(), any(), anyLong());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testFindPostalCodeGetttingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericPostalCodeService.find(any(), any(), anyLong())).thenThrow(new RuntimeException());
    postalCodeRESTfulServiceImpl.find(mockCredentials, spyPostalCode);
  }

  @Test
  public void testFindPostalCodePaging() {
    assertTrue("Find", postalCodeRESTfulServiceImpl.find(mockCredentials, spyPostalCode, mockPaging, NONE).isEmpty());
    verify(mockSecuredGenericPostalCodeService, times(1)).find(any(), any(), any(), any(), anyVararg());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testFindPostalCodePagingGetttingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericPostalCodeService.find(any(), any(), any(), any(), anyVararg()))
        .thenThrow(new RuntimeException());
    postalCodeRESTfulServiceImpl.find(mockCredentials, spyPostalCode, mockPaging, NONE);
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testMergePostalCodeGettingGNUOpenBusinessServiceException() {
    when(spyPostalCode.isDetached()).thenReturn(true);
    when(mockSecuredGenericPostalCodeService.merge(any(), any())).thenThrow(new RuntimeException());
    postalCodeRESTfulServiceImpl.merge(mockCredentials, spyPostalCode);
  }

  @Test
  public void testMergeWithNoDetachedPostalCodes() {
    when(spyPostalCode.isDetached()).thenReturn(false);
    assertNull("Merge", postalCodeRESTfulServiceImpl.merge(mockCredentials, spyPostalCode));
    verify(mockSecuredGenericPostalCodeService, times(1)).merge(any(), any());
  }

  @Test
  public void testPersistWithDetachedPostalCodes() {
    when(spyPostalCode.isDetached()).thenReturn(true);
    assertNull("Persist", postalCodeRESTfulServiceImpl.persist(mockCredentials, spyPostalCode));
    verify(mockSecuredGenericPostalCodeService, times(1)).merge(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testPersistCategoryGettingGNUOpenBusinessServiceException() {
    when(spyPostalCode.isDetached()).thenReturn(true);
    when(mockSecuredGenericPostalCodeService.merge(any(), any())).thenThrow(new RuntimeException());
    postalCodeRESTfulServiceImpl.persist(mockCredentials, spyPostalCode);
  }

  @Test
  public void testPersistWithNoDetachedPostalCodes() {
    when(spyPostalCode.isDetached()).thenReturn(false);
    assertNotNull("Persist", postalCodeRESTfulServiceImpl.persist(mockCredentials, spyPostalCode));
    verify(mockSecuredGenericPostalCodeService, times(1)).persist(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testRefreshCategoryGettingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericPostalCodeService.refresh(any(), any(), anyLong())).thenThrow(new RuntimeException());
    postalCodeRESTfulServiceImpl.refresh(mockCredentials, spyPostalCode);
  }

  @Test
  public void testRefreshCategoryWithDetachedPostalCodes() {
    assertNull("Refresh", postalCodeRESTfulServiceImpl.refresh(mockCredentials, spyPostalCode));
    verify(mockSecuredGenericPostalCodeService, times(1)).refresh(any(), any(), anyLong());
  }
}
