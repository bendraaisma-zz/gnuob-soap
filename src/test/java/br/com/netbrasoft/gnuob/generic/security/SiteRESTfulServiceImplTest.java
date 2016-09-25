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
package br.com.netbrasoft.gnuob.generic.security;

import static br.com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.UNCHECKED_VALUE;
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
import br.com.netbrasoft.gnuob.generic.OrderByEnum;
import br.com.netbrasoft.gnuob.generic.Paging;

public class SiteRESTfulServiceImplTest {

  private IGenericTypeWebService<Site> siteRESTfulServiceImpl = new SiteRESTfulServiceImpl();
  private MetaData mockCredentials;
  private Paging mockPaging;
  private ISecuredGenericTypeService<Site> mockSecuredGenericSiteService;
  private Site spySite;

  @SuppressWarnings(UNCHECKED_VALUE)
  @Before
  public void setUp() throws Exception {
    mockCredentials = mock(MetaData.class);
    mockPaging = mock(Paging.class);
    spySite = spy(Site.class);
    mockSecuredGenericSiteService = mock(ISecuredGenericTypeService.class);
    siteRESTfulServiceImpl = new SiteRESTfulServiceImpl(mockSecuredGenericSiteService);
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testCountSite() {
    assertEquals("Count", 0, siteRESTfulServiceImpl.count(mockCredentials, spySite));
    verify(mockSecuredGenericSiteService, times(1)).count(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testCountSiteGettingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericSiteService.count(any(), any())).thenThrow(new RuntimeException());
    siteRESTfulServiceImpl.count(mockCredentials, spySite);
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testDeleteCategoryGettingGNUOpenBusinessServiceException() {
    siteRESTfulServiceImpl = new SiteWebServiceImpl<>();
    siteRESTfulServiceImpl.remove(mockCredentials, spySite);
  }

  @Test
  public void testDeleteCategoryWithNullSitesAndSubCategories() {
    siteRESTfulServiceImpl.remove(mockCredentials, spySite);
    verify(mockSecuredGenericSiteService, times(1)).remove(any(), any());
  }

  @Test
  public void testFindSite() {
    assertNull("Find", siteRESTfulServiceImpl.find(mockCredentials, spySite));
    verify(mockSecuredGenericSiteService, times(1)).find(any(), any(), anyLong());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testFindSiteGetttingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericSiteService.find(any(), any(), anyLong())).thenThrow(new RuntimeException());
    siteRESTfulServiceImpl.find(mockCredentials, spySite);
  }

  @Test
  public void testFindSitePaging() {
    assertTrue("Find", siteRESTfulServiceImpl.find(mockCredentials, spySite, mockPaging, OrderByEnum.NONE).isEmpty());
    verify(mockSecuredGenericSiteService, times(1)).find(any(), any(), any(), any(), anyVararg());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testFindSitePagingGetttingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericSiteService.find(any(), any(), any(), any(), anyVararg())).thenThrow(new RuntimeException());
    siteRESTfulServiceImpl.find(mockCredentials, spySite, mockPaging, OrderByEnum.NONE);
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testMergeSiteGettingGNUOpenBusinessServiceException() {
    when(spySite.isDetached()).thenReturn(true);
    when(mockSecuredGenericSiteService.merge(any(), any())).thenThrow(new RuntimeException());
    siteRESTfulServiceImpl.merge(mockCredentials, spySite);
  }

  @Test
  public void testMergeWithNoDetachedSites() {
    when(spySite.isDetached()).thenReturn(false);
    assertNull("Merge", siteRESTfulServiceImpl.merge(mockCredentials, spySite));
    verify(mockSecuredGenericSiteService, times(1)).merge(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testPersistCategoryGettingGNUOpenBusinessServiceException() {
    when(spySite.isDetached()).thenReturn(true);
    when(mockSecuredGenericSiteService.merge(any(), any())).thenThrow(new RuntimeException());
    siteRESTfulServiceImpl.persist(mockCredentials, spySite);
  }

  @Test
  public void testPersistWithDetachedSites() {
    when(spySite.isDetached()).thenReturn(true);
    assertNull("Persist", siteRESTfulServiceImpl.persist(mockCredentials, spySite));
    verify(mockSecuredGenericSiteService, times(1)).merge(any(), any());
  }

  @Test
  public void testPersistWithNoDetachedSites() {
    when(spySite.isDetached()).thenReturn(false);
    assertNotNull("Persist", siteRESTfulServiceImpl.persist(mockCredentials, spySite));
    verify(mockSecuredGenericSiteService, times(1)).persist(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testRefreshCategoryGettingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericSiteService.refresh(any(), any(), anyLong())).thenThrow(new RuntimeException());
    siteRESTfulServiceImpl.refresh(mockCredentials, spySite);
  }

  @Test
  public void testRefreshCategoryWithDetachedSites() {
    assertNull("Refresh", siteRESTfulServiceImpl.refresh(mockCredentials, spySite));
    verify(mockSecuredGenericSiteService, times(1)).refresh(any(), any(), anyLong());
  }
}
