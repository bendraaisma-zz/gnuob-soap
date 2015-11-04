package com.netbrasoft.gnuob.generic.setting;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.GenericTypeWebService;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeServiceImpl;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = SettingWebServiceImpl.SETTING_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class SettingWebServiceImpl<T extends Setting> implements GenericTypeWebService<T> {

  protected static final String SETTING_WEB_SERVICE_IMPL_NAME = "SettingWebServiceImpl";

  @EJB(beanName = SecuredGenericTypeServiceImpl.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private transient SecuredGenericTypeService<T> securedGenericSettingService;

  @Override
  @WebMethod(operationName = "countSetting")
  public long count(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "Setting") final T type) {
    try {
      return securedGenericSettingService.count(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findSettingById")
  public T find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "Setting") final T type) {
    try {
      return securedGenericSettingService.find(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "findSetting")
  public List<T> find(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "Setting") final T type, @WebParam(name = "paging") final Paging paging,
      @WebParam(name = "orderBy") final OrderBy orderBy) {
    try {
      return securedGenericSettingService.find(metaData, type, paging, orderBy);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "mergeSetting")
  public T merge(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "Setting") final T type) {
    try {
      return securedGenericSettingService.merge(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "persistSetting")
  public T persist(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "Setting") final T type) {
    try {
      if (type.isDetached()) {
        return securedGenericSettingService.merge(metaData, type);
      }
      securedGenericSettingService.persist(metaData, type);
      return type;
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "refreshSetting")
  public T refresh(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "Setting") final T type) {
    try {
      return securedGenericSettingService.refresh(metaData, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = "removeSetting")
  public void remove(@WebParam(name = "metaData", header = true) final MetaData metaData, @WebParam(name = "Setting") final T type) {
    try {
      securedGenericSettingService.remove(metaData, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
