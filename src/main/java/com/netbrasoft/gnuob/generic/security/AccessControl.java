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

package com.netbrasoft.gnuob.generic.security;

import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.CREATION_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.GENERIC_TYPE_SERVICE_IMPL_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.ID_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.MODIFICATION_COLUMN_NAME;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.SITE_ID;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.UNCHECKED;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.USER_ID;
import static com.netbrasoft.gnuob.generic.NetbrasoftSoapConstants.VERSION_COLUMN_NAME;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.LockModeType;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.springframework.beans.BeanUtils;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.AbstractType;
import com.netbrasoft.gnuob.generic.IGenericTypeService;
import com.netbrasoft.gnuob.generic.OrderByEnum;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.Parameter;
import com.netbrasoft.gnuob.generic.security.Rule.Operation;

import de.rtner.security.auth.spi.SimplePBKDF2;

public class AccessControl<A extends AbstractAccess, U extends User, S extends Site> {

  @SuppressWarnings(UNCHECKED)
  private class Subject {

    private S site = (S) Site.getInstance();
    private U user = (U) User.getInstance();

    Subject() {}

    public boolean isUserLinkedToSite() {
      return user.getSites().contains(site);
    }
  }

  @EJB(beanName = GENERIC_TYPE_SERVICE_IMPL_NAME)
  private IGenericTypeService<A> accessTypeService;

  @EJB(beanName = GENERIC_TYPE_SERVICE_IMPL_NAME)
  private IGenericTypeService<U> userServiceImpl;

  @EJB(beanName = GENERIC_TYPE_SERVICE_IMPL_NAME)
  private IGenericTypeService<S> siteServiceImpl;

  @AroundInvoke
  public Object intercept(final InvocationContext ctx) {
    switch (ctx.getMethod().getAnnotation(OperationAccess.class).operation()) {
      case CREATE:
        return createOperationAccess(ctx);
      case READ:
        return readOperationAccess(ctx);
      case UPDATE:
        return updateOperationAccess(ctx);
      case NONE:
        return noneOperationAccess(ctx);
      case DELETE:
      default:
        return deleteOperationAccess(ctx);
    }
  }

  private Object createOperationAccess(final InvocationContext ctx) {
    final Split split =
        SimonManager.getStopwatch("com.netbrasoft.gnuob.generic.security.AccessControl.createOperationAccess").start();
    try {
      return processCreateOperationAccess(ctx, authenticateSubject(getMetaData(ctx)), getAbstractType(ctx));
    } finally {
      split.stop();
    }
  }

  @SuppressWarnings(UNCHECKED)
  private Object processCreateOperationAccess(final InvocationContext ctx, final Subject subject,
      AbstractType abstractType) {
    if (isRoot(subject) || isAllowedTo(subject, Operation.CREATE)) {
      try {
        if (abstractType instanceof AbstractAccess) {
          createAccess(subject, (A) abstractType);
        }
        return ctx.proceed();
      } catch (final Exception e) {
        throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
    } else {
      throw new GNUOpenBusinessServiceException(String.format(
          "Given user [%s] doesn't have the right access to create this entity object, verify that the given user has access (ERR01)",
          subject.user.getName()));
    }
  }

  private void createAccess(final Subject subject, A access)
      throws IllegalAccessException, InvocationTargetException {
    access.setGroup(subject.user.getGroup());
    access.setSite(subject.site);
    access.setOwner(subject.user);
    if (access.getPermission() == null) {
      access.setPermission(Permission.getInstance());
      BeanUtils.copyProperties(subject.user.getPermission(), access,
          new String[] {ID_COLUMN_NAME, VERSION_COLUMN_NAME, CREATION_COLUMN_NAME, MODIFICATION_COLUMN_NAME});
    }
  }

  private Object readOperationAccess(final InvocationContext ctx) {
    final Split split =
        SimonManager.getStopwatch("com.netbrasoft.gnuob.generic.security.AccessControl.readOperationAccess").start();
    try {
      return processReadOperationAccess(ctx, authenticateSubject(getMetaData(ctx)));
    } finally {
      split.stop();
    }
  }

  private Object processReadOperationAccess(final InvocationContext ctx, Subject subject) {
    if (isRoot(subject) || isAllowedTo(subject, Operation.READ)) {
      enableAccessFilter(subject);
      try {
        return ctx.proceed();
      } catch (final Exception e) {
        throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      } finally {
        disableAccessFilter();
      }
    } else {
      throw new GNUOpenBusinessServiceException(String.format(
          "Given user [%s] doesn't have the right access to read this entity object, verify that the given user has access (ERR01)",
          subject.user.getName()));
    }
  }

  private void enableAccessFilter(final Subject subject) {
    accessTypeService.enableFilter(AbstractAccess.NFQ1, new Parameter(USER_ID, subject.user.getId()));
    accessTypeService.enableFilter(AbstractAccess.NFQ2, new Parameter(SITE_ID, subject.site.getId()));
  }

  private void disableAccessFilter() {
    accessTypeService.disableFilter(AbstractAccess.NFQ1);
    accessTypeService.disableFilter(AbstractAccess.NFQ2);
  }

  private Object noneOperationAccess(final InvocationContext ctx) {
    final Split split =
        SimonManager.getStopwatch("com.netbrasoft.gnuob.generic.security.AccessControl.noneOperationAccess").start();
    try {
      return ctx.proceed();
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      split.stop();
    }
  }

  private Object updateOperationAccess(InvocationContext ctx) {
    final Split split =
        SimonManager.getStopwatch("com.netbrasoft.gnuob.generic.security.AccessControl.updateOperationAccess").start();
    try {
      return processOperationAccess(ctx, authenticateSubject(getMetaData(ctx)), getAbstractType(ctx), Operation.UPDATE);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      split.stop();
    }
  }

  private Object deleteOperationAccess(final InvocationContext ctx) {
    final Split split =
        SimonManager.getStopwatch("com.netbrasoft.gnuob.generic.security.AccessControl.deleteOperationAccess").start();
    try {
      return processOperationAccess(ctx, authenticateSubject(getMetaData(ctx)), getAbstractType(ctx), Operation.DELETE);
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    } finally {
      split.stop();
    }
  }

  private MetaData getMetaData(final InvocationContext ctx) {
    return (MetaData) Arrays.asList(ctx.getParameters()).stream().filter(e -> e instanceof MetaData).findFirst().get();
  }

  private AbstractType getAbstractType(final InvocationContext ctx) {
    return (AbstractType) Arrays.asList(ctx.getParameters()).stream().filter(e -> e instanceof AbstractType).findFirst()
        .get();
  }

  @SuppressWarnings(UNCHECKED)
  private Object processOperationAccess(final InvocationContext ctx, final Subject subject, AbstractType abstractType,
      Operation operation) {
    if (isRoot(subject) || isAllowedTo(subject, operation)) {
      if (abstractType instanceof AbstractAccess) {
        final A access = getAbstractAccessObject(abstractType);
        if (isRoot(subject) || hasOwnership(subject, access, operation) || hasGroupOwnership(subject, access, operation)
            || hasOtherOwnership(access, operation)) {
          updateAccess((A) abstractType, access);
        } else {
          throw new GNUOpenBusinessServiceException(String.format(
              "Given user [%s] doesn't have the right access to %s this entity object, verify that the given user has access (ERR02)",
              subject.user.getName(), operation.toString().toLowerCase()));
        }
      }
      try {
        return ctx.proceed();
      } catch (final Exception e) {
        throw new GNUOpenBusinessServiceException(e.getMessage(), e);
      }
    } else {
      throw new GNUOpenBusinessServiceException(String.format(
          "Given user [%s] doesn't have the right access to %s this entity object, verify that the given user has access (ERR01)",
          subject.user.getName(), operation.toString().toLowerCase()));
    }
  }

  private boolean isRoot(final Subject subject) {
    return subject.user.getRoot() == null ? false : subject.user.getRoot();
  }

  private boolean isAllowedTo(final Subject subject, Operation operation) {
    return subject.user.getAccess().getOperations().contains(operation);
  }

  private void updateAccess(final A source, A target) {
    if (target.getPermission() == null) {
      target.setPermission(source.getPermission());
    }
    target.setGroup(source.getGroup());
    target.setSite(source.getSite());
    target.setOwner(source.getOwner());
    target.setCreation(source.getCreation());
    target.setModification(source.getModification());
  }

  private boolean hasOwnership(final Subject subject, final A access, Operation operation) {
    return access.getOwner() != null && access.getOwner().equals(subject.user) && access.getPermission() != null
        && access.getPermission().getOwner().getOperations().contains(operation);
  }

  private boolean hasGroupOwnership(final Subject subject, final A access, Operation operation) {
    for (final Group group : subject.user.getGroups()) {
      if (access.getGroup() != null && access.getGroup().equals(group) && access.getPermission() != null
          && access.getPermission().getGroup().getOperations().contains(operation)) {
        return true;
      }
    }
    return false;
  }

  private boolean hasOtherOwnership(final A access, Operation operation) {
    if (access.getPermission() != null && access.getPermission().getOthers().getOperations().contains(operation)) {
      return true;
    }
    return false;
  }

  @SuppressWarnings(UNCHECKED)
  private A getAbstractAccessObject(AbstractType abstractType) {
    A abstractAccess = accessTypeService.find((A) abstractType, abstractType.getId(), LockModeType.NONE);
    if (abstractAccess != null) {
      return abstractAccess;
    }
    throw new GNUOpenBusinessServiceException("Entity object is not found in database, access is denied (ERR03)");
  }

  private Subject authenticateSubject(final MetaData metaData) {
    final Split split =
        SimonManager.getStopwatch("com.netbrasoft.gnuob.generic.security.AccessControl.authenticateSubject").start();
    try {
      return processAuthenticatedSubject(metaData);
    } finally {
      split.stop();
    }
  }

  private Subject processAuthenticatedSubject(final MetaData metaData) {
    final Subject subject = new Subject();
    subject.site = getSiteAndCheckIt(metaData.getSite());
    subject.user = getUserAndCheckCredentials(metaData.getUser(), metaData.getPassword());
    if (!subject.isUserLinkedToSite()) {
      throw new GNUOpenBusinessServiceException(String.format(
          "Given user [%s] doesn't have the right access for site [%s], verify that the given user have access to the site (ERR04)",
          metaData.getUser(), metaData.getSite()));
    }
    return subject;
  }

  private S getSiteAndCheckIt(final String site) {
    if (containSite(site)) {
      return findSite(site);
    }
    throw new GNUOpenBusinessServiceException(String
        .format("Given site [%s] doesn't have the right access, verify that the given site has access (ERR01)", site));
  }

  private U getUserAndCheckCredentials(String userName, String candidatePassword) {
    if (containUser(userName)) {
      return verifyUserPassword(findUser(userName), candidatePassword);
    }
    throw new GNUOpenBusinessServiceException(String.format(
        "Given user [%s] doesn't have the right access, verify that the given user has access (ERR03)", userName));
  }

  @SuppressWarnings(UNCHECKED)
  private boolean containUser(final String user) {
    return userServiceImpl.count((U) User.getInstance(user)) > 0;
  }

  private U verifyUserPassword(U attachedUser, String candidatePassword) {
    if (!new SimplePBKDF2().verifyKeyFormatted(attachedUser.getPassword(), candidatePassword)) {
      throw new GNUOpenBusinessServiceException(
          String.format("Given user [%s] doesn't have the right access, verify that the given user has access (ERR02)",
              attachedUser.getName()));
    }
    return attachedUser;
  }

  @SuppressWarnings(UNCHECKED)
  private U findUser(String userName) {
    return userServiceImpl.find((U) User.getInstance(userName), Paging.getInstance(-1, -1), OrderByEnum.NONE).get(0);
  }

  @SuppressWarnings(UNCHECKED)
  private boolean containSite(final String site) {
    return siteServiceImpl.count((S) Site.getInstance(site)) > 0;
  }

  @SuppressWarnings(UNCHECKED)
  private S findSite(final String site) {
    return siteServiceImpl.find((S) Site.getInstance(site), Paging.getInstance(-1, -1), OrderByEnum.NONE).get(0);
  }
}
