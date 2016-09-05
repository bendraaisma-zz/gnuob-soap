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

package br.com.netbrasoft.gnuob.generic.content;

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
import br.com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeService;
import br.com.netbrasoft.gnuob.generic.security.MetaData;

public class ContentRESTfulServiceImplTest {

  private IGenericTypeWebService<Content> contentRESTfulServiceImpl = new ContentRESTfulServiceImpl();
  private MetaData mockCredentials;
  private Paging mockPaging;
  private ISecuredGenericTypeService<Content> mockSecuredGenericContentService;
  private Content spyContent;

  @SuppressWarnings(UNCHECKED_VALUE)
  @Before
  public void setUp() throws Exception {
    mockCredentials = mock(MetaData.class);
    mockPaging = mock(Paging.class);
    spyContent = spy(Content.class);
    mockSecuredGenericContentService = mock(ISecuredGenericTypeService.class);
    contentRESTfulServiceImpl = new ContentWebServiceImpl<Content>(mockSecuredGenericContentService);
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testCountContent() {
    assertEquals("Count", 0, contentRESTfulServiceImpl.count(mockCredentials, spyContent));
    verify(mockSecuredGenericContentService, times(1)).count(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testCountContentGettingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericContentService.count(any(), any())).thenThrow(new RuntimeException());
    contentRESTfulServiceImpl.count(mockCredentials, spyContent);
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testDeleteCategoryGettingGNUOpenBusinessServiceException() {
    contentRESTfulServiceImpl = new ContentWebServiceImpl<>();
    contentRESTfulServiceImpl.remove(mockCredentials, spyContent);
  }

  @Test
  public void testDeleteCategoryWithNullContentsAndSubCategories() {
    contentRESTfulServiceImpl.remove(mockCredentials, spyContent);
    verify(mockSecuredGenericContentService, times(1)).remove(any(), any());
  }

  @Test
  public void testFindContent() {
    assertNull("Find", contentRESTfulServiceImpl.find(mockCredentials, spyContent));
    verify(mockSecuredGenericContentService, times(1)).find(any(), any(), anyLong());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testFindContentGetttingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericContentService.find(any(), any(), anyLong())).thenThrow(new RuntimeException());
    contentRESTfulServiceImpl.find(mockCredentials, spyContent);
  }

  @Test
  public void testFindContentPaging() {
    assertTrue("Find", contentRESTfulServiceImpl.find(mockCredentials, spyContent, mockPaging, OrderByEnum.NONE).isEmpty());
    verify(mockSecuredGenericContentService, times(1)).find(any(), any(), any(), any(), anyVararg());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testFindContentPagingGetttingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericContentService.find(any(), any(), any(), any(), anyVararg()))
        .thenThrow(new RuntimeException());
    contentRESTfulServiceImpl.find(mockCredentials, spyContent, mockPaging, OrderByEnum.NONE);
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testMergeContentGettingGNUOpenBusinessServiceException() {
    when(spyContent.isDetached()).thenReturn(true);
    when(mockSecuredGenericContentService.merge(any(), any())).thenThrow(new RuntimeException());
    contentRESTfulServiceImpl.merge(mockCredentials, spyContent);
  }

  @Test
  public void testMergeWithNoDetachedContents() {
    when(spyContent.isDetached()).thenReturn(false);
    assertNull("Merge", contentRESTfulServiceImpl.merge(mockCredentials, spyContent));
    verify(mockSecuredGenericContentService, times(1)).merge(any(), any());
  }

  @Test
  public void testPersistWithDetachedContents() {
    when(spyContent.isDetached()).thenReturn(true);
    assertNull("Persist", contentRESTfulServiceImpl.persist(mockCredentials, spyContent));
    verify(mockSecuredGenericContentService, times(1)).merge(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testPersistCategoryGettingGNUOpenBusinessServiceException() {
    when(spyContent.isDetached()).thenReturn(true);
    when(mockSecuredGenericContentService.merge(any(), any())).thenThrow(new RuntimeException());
    contentRESTfulServiceImpl.persist(mockCredentials, spyContent);
  }

  @Test
  public void testPersistWithNoDetachedContents() {
    when(spyContent.isDetached()).thenReturn(false);
    assertNotNull("Persist", contentRESTfulServiceImpl.persist(mockCredentials, spyContent));
    verify(mockSecuredGenericContentService, times(1)).persist(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testRefreshCategoryGettingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericContentService.refresh(any(), any(), anyLong())).thenThrow(new RuntimeException());
    contentRESTfulServiceImpl.refresh(mockCredentials, spyContent);
  }

  @Test
  public void testRefreshCategoryWithDetachedContents() {
    assertNull("Refresh", contentRESTfulServiceImpl.refresh(mockCredentials, spyContent));
    verify(mockSecuredGenericContentService, times(1)).refresh(any(), any(), anyLong());
  }
}
