package br.com.netbrasoft.gnuob.generic.security;

import static br.com.netbrasoft.gnuob.generic.JaxRsActivator.mapper;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MetaDataTest {

  private MetaData spyMetaData;

  @Before
  public void setUp() throws Exception {
    spyMetaData = spy(MetaData.class);
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testJsonMetaData() throws IllegalAccessException, InvocationTargetException, IOException {
    final MetaData metaData = MetaData.getInstance("site", "user", "password");
    final MetaData jsonMetaData = MetaData.getInstanceByJson(mapper.writeValueAsString(metaData));
    assertEquals(metaData.getSite(), jsonMetaData.getSite());
    assertEquals(metaData.getUser(), jsonMetaData.getUser());
    assertEquals(metaData.getPassword(), jsonMetaData.getPassword());
  }

  @Test
  public void testGetInstance() {
    final MetaData instance = MetaData.getInstance();
    assertNotNull("Instance", instance);
    assertNull("User", instance.getUser());
    assertNull("Password", instance.getPassword());
    assertNull("Site", instance.getSite());
  }

  @Test
  public void testGetInstanceByUserPasswordAndSite() {
    final MetaData instance = MetaData.getInstance("Site", "User", "Password");
    assertNotNull("Instance", instance);
    assertEquals("User", "User", instance.getUser());
    assertEquals("Password", "Password", instance.getPassword());
    assertEquals("Site", "Site", instance.getSite());
  }

  @Test
  public void testGetPassword() {
    assertNull("Password", spyMetaData.getPassword());
    verify(spyMetaData, times(1)).getPassword();
  }

  @Test
  public void testGetSite() {
    assertNull("Password", spyMetaData.getSite());
    verify(spyMetaData, times(1)).getSite();
  }

  @Test
  public void testGetUser() {
    assertNull("User", spyMetaData.getUser());
    verify(spyMetaData, times(1)).getUser();
  }

  @Test
  public void testSetPassword() {
    spyMetaData.setPassword("Folly words widow one downs few age every seven.");
    assertEquals("Password", "Folly words widow one downs few age every seven.", spyMetaData.getPassword());
    verify(spyMetaData, times(1)).setPassword(anyString());
  }

  @Test
  public void testSetSite() {
    spyMetaData.setSite("Folly words widow one downs few age every seven.");
    assertEquals("Site", "Folly words widow one downs few age every seven.", spyMetaData.getSite());
    verify(spyMetaData, times(1)).setSite(anyString());
  }

  @Test
  public void testSetUser() {
    spyMetaData.setUser("Folly words widow one downs few age every seven.");
    assertEquals("User", "Folly words widow one downs few age every seven.", spyMetaData.getUser());
    verify(spyMetaData, times(1)).setUser(anyString());
  }

  @Test
  public void testToString() {
    assertEquals(ToStringBuilder.reflectionToString(spyMetaData, SHORT_PREFIX_STYLE), spyMetaData.toString());
  }
}
