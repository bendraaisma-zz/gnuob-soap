package com.netbrasoft.gnuob.generic.security;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.LockModeType;

import org.javasimon.SimonManager;
import org.javasimon.Split;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.AbstractType;
import com.netbrasoft.gnuob.generic.GenericTypeService;
import com.netbrasoft.gnuob.generic.GenericTypeServiceImpl;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.Parameter;
import com.netbrasoft.gnuob.generic.security.Rule.Operation;

import de.rtner.security.auth.spi.SimplePBKDF2;

public class AccessControl<A extends AbstractAccess, U extends User, S extends Site> {

  private class Subject {
    private Site site = new Site();
    private User user = new User();

    private Subject() {
      // Empty constructor.
    }
  }

  @EJB(beanName = GenericTypeServiceImpl.GENERIC_TYPE_SERVICE_IMPL_NAME)
  private GenericTypeService<A> accessTypeService;

  @EJB(beanName = GenericTypeServiceImpl.GENERIC_TYPE_SERVICE_IMPL_NAME)
  private GenericTypeService<U> userServiceImpl;

  @EJB(beanName = GenericTypeServiceImpl.GENERIC_TYPE_SERVICE_IMPL_NAME)
  private GenericTypeService<S> siteServiceImpl;

  @SuppressWarnings("unchecked")
  private Subject authenticateSubject(final InvocationContext ctx) {

    final Split split = SimonManager.getStopwatch("com.netbrasoft.gnuob.generic.security.AccessControl.authenticateSubject").start();

    final SimplePBKDF2 simplePBKDF2 = new SimplePBKDF2();

    final Subject subject = new Subject();

    for (final Object parameter : ctx.getParameters()) {

      // Parameter is instance of MetaData?
      if (parameter instanceof MetaData) {

        // Cast parameter to a meta data object.
        final MetaData metaData = (MetaData) parameter;

        // Check site credentials
        if (siteServiceImpl.count((S) new Site(metaData.getSite())) > 0) {
          subject.site = siteServiceImpl.find((S) new Site(metaData.getSite()), new Paging(-1, -1), OrderBy.NONE).get(0);
        } else {
          split.stop();
          throw new GNUOpenBusinessServiceException(String.format("Given site [%s] doesn't have the right access, verify that the given site has access", metaData.getSite()));
        }

        // Check user credentials
        if (userServiceImpl.count((U) new User(metaData.getUser())) > 0) {
          subject.user = userServiceImpl.find((U) new User(metaData.getUser()), new Paging(-1, -1), OrderBy.NONE).get(0);

          if (!simplePBKDF2.verifyKeyFormatted(subject.user.getPassword(), metaData.getPassword())) {
            split.stop();
            throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access", metaData.getUser()));
          }
        } else {
          split.stop();
          throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access", metaData.getUser()));
        }

        // Check user contain access to site.
        if (!subject.user.getSites().contains(subject.site)) {
          split.stop();
          throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access for site [%s], verify that the given user have access to the site",
              subject.user.getName(), subject.site.getName()));
        }
      }
    }

    split.stop();
    return subject;
  }

  private Object createOperationAccess(final Subject subject, final InvocationContext ctx) {

    final Split split = SimonManager.getStopwatch("com.netbrasoft.gnuob.generic.security.AccessControl.createOperationAccess").start();

    // Checks if the method contains a access, or type object as parameter
    for (final Object parameter : ctx.getParameters()) {

      // Check if user has create access.
      if (!subject.user.getAccess().getOperations().contains(Operation.CREATE)) {
        split.stop();
        throw new GNUOpenBusinessServiceException(
            String.format("Given user [%s] doesn't have the right access to create this entity object, verify that the given user has access", subject.user.getName()));
      }

      // Parameter is instance of Access?
      if (parameter instanceof AbstractAccess) {

        final AbstractAccess access = (AbstractAccess) parameter;

        access.setGroup(subject.user.getGroup());
        access.setSite(subject.site);
        access.setOwner(subject.user);

        if (access.getPermission() == null) {
          // Give access object same permission as creator.
          access.setPermission(new com.netbrasoft.gnuob.generic.security.Permission());
          access.getPermission().setOwner(subject.user.getPermission().getOwner());
          access.getPermission().setGroup(subject.user.getPermission().getGroup());
          access.getPermission().setOthers(subject.user.getPermission().getOthers());
        }
      } else {
        // Parameter is instance of Type?
        // Check if user has create access.
        if (parameter instanceof AbstractType && !subject.user.getAccess().getOperations().contains(Operation.CREATE)) {
          split.stop();
          throw new GNUOpenBusinessServiceException(
              String.format("Given user [%s] doesn't have the right access to create this entity object, verify that the given user has access", subject.user.getName()));
        }
      }
    }

    try {
      split.stop();
      return ctx.proceed();
    } catch (final Exception e) {
      split.stop();
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private Object deleteOperationAccess(final Subject subject, final InvocationContext ctx) {

    final Split split = SimonManager.getStopwatch("com.netbrasoft.gnuob.generic.security.AccessControl.deleteOperationAccess").start();

    // Checks if the method contains a access, or type object as parameter
    for (final Object parameter : ctx.getParameters()) {

      // Check if user has delete access.
      if (!subject.user.getAccess().getOperations().contains(Operation.DELETE)) {
        split.stop();
        throw new GNUOpenBusinessServiceException(
            String.format("Given user [%s] doesn't have the right access to delete this entity object, verify that the given user has access", subject.user.getName()));
      }

      // Parameter is instance of Access?
      if (parameter instanceof AbstractAccess) {

        // Cast parameter to a access object and get id.
        final long accessId = ((AbstractAccess) parameter).getId();

        @SuppressWarnings("unchecked")
        final AbstractAccess access = accessTypeService.find((A) parameter, accessId, LockModeType.NONE);

        // If access object is in database?
        if (access != null) {

          boolean userOwnership = false;
          boolean groupOwnership = false;
          boolean othersOwnership = false;
          final boolean rootOwnership = subject.user.getRoot() == null ? false : subject.user.getRoot();

          // User of access object is owner and has a delete
          // access?
          if (access.getOwner() != null && access.getOwner().equals(subject.user) && access.getPermission() != null
              && access.getPermission().getOwner().getOperations().contains(Operation.DELETE)) {
            userOwnership = true;
          } else {

            // Check if it has delete access based on
            // groups ownership.
            for (final Group group : subject.user.getGroups()) {

              if (access.getGroup() != null && access.getGroup().equals(group) && access.getPermission() != null
                  && access.getPermission().getGroup().getOperations().contains(Operation.DELETE)) {
                groupOwnership = true;
              }
            }

            // Check if it has delete access based on
            // others permission
            if (!groupOwnership && access.getPermission() != null && access.getPermission().getOthers().getOperations().contains(Operation.DELETE)) {
              othersOwnership = true;
            }
          }

          if (userOwnership || groupOwnership || othersOwnership || rootOwnership) {
            ((AbstractAccess) parameter).setGroup(access.getGroup());
            ((AbstractAccess) parameter).setPermission(access.getPermission());
            ((AbstractAccess) parameter).setSite(access.getSite());
            ((AbstractAccess) parameter).setOwner(access.getOwner());
            ((AbstractAccess) parameter).setCreation(access.getCreation());
            ((AbstractAccess) parameter).setModification(access.getModification());
          } else {
            split.stop();
            throw new GNUOpenBusinessServiceException(
                String.format("Given user [%s] doesn't have the right access to delete this entity object, verify that the given user has access", subject.user.getName()));
          }
        } else {
          split.stop();
          throw new GNUOpenBusinessServiceException("Enity object is not found in database, access is denied");
        }
      } else {
        // Parameter is instance of Type?
        // Check if user has delete access.
        if (parameter instanceof AbstractType && !subject.user.getAccess().getOperations().contains(Operation.DELETE)) {
          split.stop();
          throw new GNUOpenBusinessServiceException(
              String.format("Given user [%s] doesn't have the right access to delete this entity object, verify that the given user has access", subject.user.getName()));
        }
      }
    }

    try {
      split.stop();
      return ctx.proceed();
    } catch (final Exception e) {
      split.stop();
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private void disableAccessFilter() {
    accessTypeService.disableFilter(AbstractAccess.NFQ1);
    accessTypeService.disableFilter(AbstractAccess.NFQ2);
  }

  private void enableAccessFilter(final Subject subject) {
    accessTypeService.enableFilter(AbstractAccess.NFQ1, new Parameter("userId", subject.user.getId()));
    accessTypeService.enableFilter(AbstractAccess.NFQ2, new Parameter("siteId", subject.site.getId()));
  }

  @AroundInvoke
  public Object intercept(final InvocationContext ctx) {

    final OperationAccess operationAccess = ctx.getMethod().getAnnotation(OperationAccess.class);

    if (operationAccess != null) {

      switch (operationAccess.operation()) {
        case CREATE:
          return createOperationAccess(authenticateSubject(ctx), ctx);
        case READ:
          return readOperationAccess(authenticateSubject(ctx), ctx);
        case UPDATE:
          return updateOperationAccess(authenticateSubject(ctx), ctx);
        case NONE:
          return noneOperationAccess(ctx);
        default: // DELETE
          return deleteOperationAccess(authenticateSubject(ctx), ctx);
      }
    } else {
      throw new GNUOpenBusinessServiceException("Operation access is not set on this method, access is denied");
    }
  }

  private Object noneOperationAccess(final InvocationContext ctx) {
    try {
      return ctx.proceed();
    } catch (final Exception e) {
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }

  private Object readOperationAccess(final Subject subject, final InvocationContext ctx) {

    final Split split = SimonManager.getStopwatch("com.netbrasoft.gnuob.generic.security.AccessControl.readOperationAccess").start();

    // Check if user has read access.
    if (!subject.user.getAccess().getOperations().contains(Operation.READ)) {
      split.stop();
      throw new GNUOpenBusinessServiceException(
          String.format("Given user [%s] doesn't have the right access to read this entity object, verify that the given user has access", subject.user.getName()));
    }

    enableAccessFilter(subject);
    Object proceed;

    try {
      proceed = ctx.proceed();
    } catch (final Exception e) {
      split.stop();
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }

    disableAccessFilter();

    split.stop();
    return proceed;
  }

  private Object updateOperationAccess(final Subject subject, final InvocationContext ctx) {

    final Split split = SimonManager.getStopwatch("com.netbrasoft.gnuob.generic.security.AccessControl.updateOperationAccess").start();

    // Checks if the method contains a access object as parameter
    for (final Object parameter : ctx.getParameters()) {

      // Parameter is instance of Access?
      if (parameter instanceof AbstractAccess) {

        // Check if user has update access.
        if (!subject.user.getAccess().getOperations().contains(Operation.UPDATE)) {
          split.stop();
          throw new GNUOpenBusinessServiceException(
              String.format("Given user [%s] doesn't have the right access to update this entity object, verify that the given user has access", subject.user.getName()));
        }

        // Cast parameter to a access object and get id.
        final long accessId = ((AbstractAccess) parameter).getId();

        @SuppressWarnings("unchecked")
        final AbstractAccess access = accessTypeService.find((A) parameter, accessId, LockModeType.NONE);

        // If access object is in database?
        if (access != null) {

          boolean userOwnership = false;
          boolean groupOwnership = false;
          boolean othersOwnership = false;
          final boolean rootOwnership = subject.user.getRoot() == null ? false : subject.user.getRoot();

          // User of access object is owner and has a update
          // access?
          if (access.getOwner() != null && access.getOwner().equals(subject.user) && access.getPermission() != null
              && access.getPermission().getOwner().getOperations().contains(Operation.UPDATE)) {
            userOwnership = true;
          } else {

            // Check if it has update access based on
            // groups ownership.
            for (final Group group : subject.user.getGroups()) {

              if (access.getGroup() != null && access.getGroup().equals(group) && access.getPermission() != null
                  && access.getPermission().getGroup().getOperations().contains(Operation.UPDATE)) {
                groupOwnership = true;
              }
            }

            // Check if it has update access based on
            // others permission
            if (!groupOwnership && access.getPermission() != null && access.getPermission().getOthers().getOperations().contains(Operation.UPDATE)) {
              othersOwnership = true;
            }
          }

          if (userOwnership || groupOwnership || othersOwnership || rootOwnership) {
            ((AbstractAccess) parameter).setGroup(access.getGroup());
            ((AbstractAccess) parameter).setPermission(access.getPermission());
            ((AbstractAccess) parameter).setSite(access.getSite());
            ((AbstractAccess) parameter).setOwner(access.getOwner());
            ((AbstractAccess) parameter).setCreation(access.getCreation());
            ((AbstractAccess) parameter).setModification(access.getModification());
          } else {
            split.stop();
            throw new GNUOpenBusinessServiceException(
                String.format("Given user [%s] doesn't have the right access to update this entity object, verify that the given user has access", subject.user.getName()));
          }

        } else {
          // If access object isn't in database than test it by using create operation access.
          return createOperationAccess(subject, ctx);
        }
      } else {
        // Parameter is instance of Type?
        // Check if user has update access.
        if (parameter instanceof AbstractType && !subject.user.getAccess().getOperations().contains(Operation.UPDATE)) {
          split.stop();
          throw new GNUOpenBusinessServiceException(
              String.format("Given user [%s] doesn't have the right access to update this entity object, verify that the given user has access", subject.user.getName()));
        }
      }
    }

    try {
      split.stop();
      return ctx.proceed();
    } catch (final Exception e) {
      split.stop();
      throw new GNUOpenBusinessServiceException(e.getMessage(), e);
    }
  }
}
