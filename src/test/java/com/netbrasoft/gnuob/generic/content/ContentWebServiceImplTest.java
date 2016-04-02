package com.netbrasoft.gnuob.generic.content;

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.UNCHECKED_VALUE;
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

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.IGenericTypeWebService;
import com.netbrasoft.gnuob.generic.OrderByEnum;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeService;
import com.netbrasoft.gnuob.generic.security.MetaData;

public class ContentWebServiceImplTest {

  private IGenericTypeWebService<Content> contentWebServiceImpl = new ContentWebServiceImpl<>();
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
    contentWebServiceImpl = new ContentWebServiceImpl<Content>(mockSecuredGenericContentService);
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testCountContent() {
    assertEquals("Count", 0, contentWebServiceImpl.count(mockCredentials, spyContent));
    verify(mockSecuredGenericContentService, times(1)).count(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testCountContentGettingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericContentService.count(any(), any())).thenThrow(new RuntimeException());
    contentWebServiceImpl.count(mockCredentials, spyContent);
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testDeleteCategoryGettingGNUOpenBusinessServiceException() {
    contentWebServiceImpl = new ContentWebServiceImpl<>();
    contentWebServiceImpl.remove(mockCredentials, spyContent);
  }

  @Test
  public void testDeleteCategoryWithNullContentsAndSubCategories() {
    contentWebServiceImpl.remove(mockCredentials, spyContent);
    verify(mockSecuredGenericContentService, times(1)).remove(any(), any());
  }

  @Test
  public void testFindContent() {
    assertNull("Find", contentWebServiceImpl.find(mockCredentials, spyContent));
    verify(mockSecuredGenericContentService, times(1)).find(any(), any(), anyLong());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testFindContentGetttingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericContentService.find(any(), any(), anyLong())).thenThrow(new RuntimeException());
    contentWebServiceImpl.find(mockCredentials, spyContent);
  }

  @Test
  public void testFindContentPaging() {
    assertTrue("Find", contentWebServiceImpl.find(mockCredentials, spyContent, mockPaging, OrderByEnum.NONE).isEmpty());
    verify(mockSecuredGenericContentService, times(1)).find(any(), any(), any(), any(), anyVararg());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testFindContentPagingGetttingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericContentService.find(any(), any(), any(), any(), anyVararg()))
        .thenThrow(new RuntimeException());
    contentWebServiceImpl.find(mockCredentials, spyContent, mockPaging, OrderByEnum.NONE);
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testMergeContentGettingGNUOpenBusinessServiceException() {
    when(spyContent.isDetached()).thenReturn(true);
    when(mockSecuredGenericContentService.merge(any(), any())).thenThrow(new RuntimeException());
    contentWebServiceImpl.merge(mockCredentials, spyContent);
  }

  @Test
  public void testMergeWithNoDetachedContents() {
    when(spyContent.isDetached()).thenReturn(false);
    assertNull("Merge", contentWebServiceImpl.merge(mockCredentials, spyContent));
    verify(mockSecuredGenericContentService, times(1)).merge(any(), any());
  }

  @Test
  public void testPersistWithDetachedContents() {
    when(spyContent.isDetached()).thenReturn(true);
    assertNull("Persist", contentWebServiceImpl.persist(mockCredentials, spyContent));
    verify(mockSecuredGenericContentService, times(1)).merge(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testPersistCategoryGettingGNUOpenBusinessServiceException() {
    when(spyContent.isDetached()).thenReturn(true);
    when(mockSecuredGenericContentService.merge(any(), any())).thenThrow(new RuntimeException());
    contentWebServiceImpl.persist(mockCredentials, spyContent);
  }

  @Test
  public void testPersistWithNoDetachedContents() {
    when(spyContent.isDetached()).thenReturn(false);
    assertNotNull("Persist", contentWebServiceImpl.persist(mockCredentials, spyContent));
    verify(mockSecuredGenericContentService, times(1)).persist(any(), any());
  }

  @Test(expected = GNUOpenBusinessServiceException.class)
  public void testRefreshCategoryGettingGNUOpenBusinessServiceException() {
    when(mockSecuredGenericContentService.refresh(any(), any(), anyLong())).thenThrow(new RuntimeException());
    contentWebServiceImpl.refresh(mockCredentials, spyContent);
  }

  @Test
  public void testRefreshCategoryWithDetachedContents() {
    assertNull("Refresh", contentWebServiceImpl.refresh(mockCredentials, spyContent));
    verify(mockSecuredGenericContentService, times(1)).refresh(any(), any(), anyLong());
  }
}
