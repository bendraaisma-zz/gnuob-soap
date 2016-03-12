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

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.COUNT_POSTAL_CODE_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_POSTAL_CODE_BY_ID_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.FIND_POSTAL_CODE_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GNUOB_WEB_SERVICE_TARGET_NAMESPACE;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MERGE_POSTAL_CODE_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.META_DATA_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ORDER_BY_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PAGING_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.PERSIST_POSTAL_CODE_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.POSTAL_CODE_PARAM_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.POSTAL_CODE_WEB_SERVICE_IMPL_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REFRESH_POSTAL_CODE_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.REMOVE_POSTAL_CODE_OPERATION_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.IGenericTypeWebService;
import com.netbrasoft.gnuob.generic.OrderByEnum;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.ISecuredGenericTypeService;
import com.netbrasoft.gnuob.monitor.AppSimonInterceptor;

@WebService(targetNamespace = GNUOB_WEB_SERVICE_TARGET_NAMESPACE)
@Stateless(name = POSTAL_CODE_WEB_SERVICE_IMPL_NAME)
@Interceptors(value = {AppSimonInterceptor.class})
public class PostalCodeWebServiceImpl<T extends PostalCode> implements IGenericTypeWebService<T> {

  @EJB(beanName = SECURED_GENERIC_TYPE_SERVICE_IMPL_NAME)
  private ISecuredGenericTypeService<T> securedGenericPostalCodeService;

  public PostalCodeWebServiceImpl() {}

  PostalCodeWebServiceImpl(final ISecuredGenericTypeService<T> securedGenericPostalCodeService) {
    this.securedGenericPostalCodeService = securedGenericPostalCodeService;
  }

  @Override
  @WebMethod(operationName = COUNT_POSTAL_CODE_OPERATION_NAME)
  public long count(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = POSTAL_CODE_PARAM_NAME) final T type) {
    try {
      return securedGenericPostalCodeService.count(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = FIND_POSTAL_CODE_BY_ID_OPERATION_NAME)
  public T find(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = POSTAL_CODE_PARAM_NAME) final T type) {
    try {
      return securedGenericPostalCodeService.find(credentials, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = FIND_POSTAL_CODE_OPERATION_NAME)
  public List<T> find(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = POSTAL_CODE_PARAM_NAME) final T type, @WebParam(name = PAGING_PARAM_NAME) final Paging paging,
      @WebParam(name = ORDER_BY_PARAM_NAME) final OrderByEnum orderingProperty) {
    try {
      return securedGenericPostalCodeService.find(credentials, type, paging, orderingProperty);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = MERGE_POSTAL_CODE_OPERATION_NAME)
  public T merge(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = POSTAL_CODE_PARAM_NAME) final T type) {
    try {
      return securedGenericPostalCodeService.merge(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = PERSIST_POSTAL_CODE_OPERATION_NAME)
  public T persist(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = POSTAL_CODE_PARAM_NAME) final T type) {
    try {
      return persistMergePostalCode(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private T persistMergePostalCode(final MetaData credentials, final T type) {
    if (type.isDetached()) {
      return securedGenericPostalCodeService.merge(credentials, type);
    }
    securedGenericPostalCodeService.persist(credentials, type);
    return type;
  }

  @Override
  @WebMethod(operationName = REFRESH_POSTAL_CODE_OPERATION_NAME)
  public T refresh(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = POSTAL_CODE_PARAM_NAME) final T type) {
    try {
      return securedGenericPostalCodeService.refresh(credentials, type, type.getId());
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  @Override
  @WebMethod(operationName = REMOVE_POSTAL_CODE_OPERATION_NAME)
  public void remove(@WebParam(name = META_DATA_PARAM_NAME, header = true) final MetaData credentials,
      @WebParam(name = POSTAL_CODE_PARAM_NAME) final T type) {
    try {
      securedGenericPostalCodeService.remove(credentials, type);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
