package br.com.netbrasoft.gnuob.generic;

import static br.com.netbrasoft.gnuob.generic.JaxRsActivator.mapper;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PagingTest {

  private Paging spyPaging = Paging.getInstance(0, Integer.MAX_VALUE);

  @Before
  public void setUp() throws Exception {
    spyPaging = spy(Paging.class);
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testJsonPaging() throws IllegalAccessException, InvocationTargetException, IOException {
    final Paging paging = Paging.getInstance(1, 2);
    final Paging jsonPaging = Paging.getInstance(mapper.writeValueAsString(paging));
    assertEquals(paging.getFirst(), jsonPaging.getFirst());
    assertEquals(paging.getMax(), jsonPaging.getMax());
  }

  @Test
  public void testGetFirst() {
    assertNotNull("First", spyPaging.getFirst());
    verify(spyPaging, times(1)).getFirst();
  }

  @Test
  public void testGetMax() {
    assertNotNull("Max", spyPaging.getMax());
    verify(spyPaging, times(1)).getMax();
  }

  @Test
  public void testSetFirst() {
    spyPaging.setFirst(Integer.MAX_VALUE);
    assertEquals("First", Integer.MAX_VALUE, spyPaging.getFirst());
    verify(spyPaging, times(1)).setFirst(anyInt());
  }

  @Test
  public void testSetMax() {
    spyPaging.setMax(Integer.MAX_VALUE);
    assertEquals("Max", Integer.MAX_VALUE, spyPaging.getMax());
    verify(spyPaging, times(1)).setMax(anyInt());
  }

  @Test
  public void testToString() {
    assertEquals(ToStringBuilder.reflectionToString(spyPaging, SHORT_PREFIX_STYLE), spyPaging.toString());
  }
}
