package com.netbrasoft.gnuob.generic.security;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.LockModeType;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.GenericTypeService;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.Parameter;
import com.netbrasoft.gnuob.generic.Type;
import com.netbrasoft.gnuob.generic.security.Rule.Operation;

public class AccessControl<A extends Access, U extends User, S extends Site> {

	private class Subject {
		private Site site = new Site();
		private User user = new User();

		private Subject() {
		}
	}

	@EJB(beanName = "GenericTypeServiceImpl")
	private GenericTypeService<A> accessTypeService;

	@EJB(beanName = "GenericTypeServiceImpl")
	private GenericTypeService<U> userServiceImpl;

	@EJB(beanName = "GenericTypeServiceImpl")
	private GenericTypeService<S> siteServiceImpl;

	@SuppressWarnings("unchecked")
	private Subject authenticateSubject(InvocationContext ctx) throws GNUOpenBusinessServiceException {

		Subject subject = new Subject();

		for (Object parameter : ctx.getParameters()) {

			// Parameter is instance of MetaData?
			if (parameter instanceof MetaData) {

				// Cast parameter to a meta data object.
				MetaData metaData = (MetaData) parameter;

				// Check site credentials
				if (siteServiceImpl.count((S) new Site(metaData.getSite())) > 0) {
					subject.site = siteServiceImpl.find((S) new Site(metaData.getSite()), Paging.getInstance(-1, -1), OrderBy.NONE).get(0);
				} else {
					throw new GNUOpenBusinessServiceException(String.format("Given site [%s] doesn't have the right access, verify that the given site has access.", metaData.getSite()));
				}

				// Check user credentials
				if (userServiceImpl.count((U) new User(metaData.getUser(), metaData.getPassword())) > 0) {
					subject.user = userServiceImpl.find((U) new User(metaData.getUser(), metaData.getPassword()), Paging.getInstance(-1, -1), OrderBy.NONE).get(0);
				} else {
					throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access.", metaData.getUser()));
				}

				// Check user contain access to site.
				if (!subject.user.getSites().contains(subject.site)) {
					throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access for site [%s], verify that the given user have access to the site.", subject.user.getName(), subject.site.getName()));
				}
			}
		}

		return subject;
	}

	private Object createOperationAccess(Subject subject, InvocationContext ctx) throws Exception {

		// Checks if the method contains a access, or type object as parameter
		for (Object parameter : ctx.getParameters()) {

			// Check if user has create access.
			if (!subject.user.getAccess().getOperations().contains(Operation.CREATE)) {
				throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access.", subject.user.getName()));
			}

			// Parameter is instance of Access?
			if (parameter instanceof Access) {

				Access access = ((Access) parameter);

				access.setActive(true);
				access.setGroup(subject.user.getGroup());
				access.setSite(subject.site);
				access.setOwner(subject.user);

				if (access.getPermission() == null) {
					access.setPermission(new com.netbrasoft.gnuob.generic.security.Permission());
				}
			} else {
				if (parameter instanceof Type) {

					// Check if user has create access.
					if (!subject.user.getAccess().getOperations().contains(Operation.CREATE)) {
						throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access.", subject.user.getName()));
					}
				}
			}
		}

		return ctx.proceed();
	}

	private Object deleteOperationAccess(Subject subject, InvocationContext ctx) throws Exception {

		// Checks if the method contains a access, or type object as parameter
		for (Object parameter : ctx.getParameters()) {

			// Check if user has delete access.
			if (!subject.user.getAccess().getOperations().contains(Operation.DELETE)) {
				throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access.", subject.user.getName()));
			}

			// Parameter is instance of Access?
			if (parameter instanceof Access) {

				// Cast parameter to a access object and get id.
				long accessId = ((Access) parameter).getId();

				@SuppressWarnings("unchecked")
				Access access = accessTypeService.find((A) parameter, accessId, LockModeType.NONE);

				// If access object is in database?
				if (access != null && access.getActive() != null && access.getActive()) {

					boolean userOwnership = false;
					boolean groupOwnership = false;
					boolean othersOwnership = false;
					boolean rootOwnership = subject.user.getRoot() == null ? false : subject.user.getRoot();

					// User of access object is owner and has a delete
					// access?
					if (access.getOwner() != null && access.getOwner().equals(subject.user) && access.getPermission() != null && access.getPermission().getOwner().getOperations().contains(Operation.DELETE)) {
						userOwnership = true;
					} else {

						// Check if it has delete access based on
						// groups ownership.
						for (Group group : subject.user.getGroups()) {

							if (access.getGroup() != null && access.getGroup().equals(group) && access.getPermission() != null && access.getPermission().getGroup().getOperations().contains(Operation.DELETE)) {
								groupOwnership = true;
							}
						}

						// Check if it has delete access based on
						// others permission
						if (!groupOwnership) {
							if (access.getPermission() != null && access.getPermission().getOthers().getOperations().contains(Operation.DELETE)) {
								othersOwnership = true;
							}
						}
					}

					if (userOwnership || groupOwnership || othersOwnership || rootOwnership) {
						((Access) parameter).setActive(access.getActive());
						((Access) parameter).setGroup(access.getGroup());
						((Access) parameter).setPermission(access.getPermission());
						((Access) parameter).setSite(access.getSite());
						((Access) parameter).setOwner(access.getOwner());
					} else {
						throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access.", subject.user.getName()));
					}
				} else {
					throw new GNUOpenBusinessServiceException("Access object is not found in database, access is denied.");
				}
			} else {
				// Parameter is instance of Type?
				if (parameter instanceof Type) {

					// Check if user has delete access.
					if (!subject.user.getAccess().getOperations().contains(Operation.DELETE)) {
						throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access.", subject.user.getName()));
					}
				}
			}
		}

		return ctx.proceed();
	}

	private void disableAccessFilter() {
		accessTypeService.disableFilter(Access.NFQ1);
		accessTypeService.disableFilter(Access.NFQ2);
		accessTypeService.disableFilter(Access.NFQ3);
	}

	private void enableAccessFilter(Subject subject) {
		accessTypeService.enableFilter(Access.NFQ1, new Parameter("userId", subject.user.getId()));
		accessTypeService.enableFilter(Access.NFQ2, new Parameter("siteId", subject.site.getId()));
		accessTypeService.enableFilter(Access.NFQ3, new Parameter("active", true));
	}

	@AroundInvoke
	public Object intercept(InvocationContext ctx) throws Exception {

		OperationAccess operationAccess = ctx.getMethod().getAnnotation(OperationAccess.class);

		if (operationAccess != null) {

			switch (operationAccess.operation()) {
			case CREATE:
				return createOperationAccess(authenticateSubject(ctx), ctx);
			case READ:
				return readOperationAccess(authenticateSubject(ctx), ctx);
			case UPDATE:
				return updateOperationAccess(authenticateSubject(ctx), ctx);
			case NONE:
				return ctx.proceed();
			default: // DELETE
				return deleteOperationAccess(authenticateSubject(ctx), ctx);
			}
		} else {
			throw new GNUOpenBusinessServiceException("Operation access is not set on this method, access is denied.");
		}
	}

	private Object readOperationAccess(Subject subject, InvocationContext ctx) throws Exception {

		// Check if user has read access.
		if (!subject.user.getAccess().getOperations().contains(Operation.READ)) {
			throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access.", subject.user.getName()));
		}

		enableAccessFilter(subject);
		Object proceed = ctx.proceed();
		disableAccessFilter();

		return proceed;
	}

	private Object updateOperationAccess(Subject subject, InvocationContext ctx) throws Exception {

		// Checks if the method contains a access object as parameter
		for (Object parameter : ctx.getParameters()) {

			// Parameter is instance of Access?
			if (parameter instanceof Access) {

				// Check if user has update access.
				if (!subject.user.getAccess().getOperations().contains(Operation.UPDATE)) {
					throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access.", subject.user.getName()));
				}

				// Cast parameter to a access object and get id.
				long accessId = ((Access) parameter).getId();

				@SuppressWarnings("unchecked")
				Access access = accessTypeService.find((A) parameter, accessId, LockModeType.NONE);

				// If access object is in database?
				if (access != null && access.getActive() != null && access.getActive()) {

					boolean userOwnership = false;
					boolean groupOwnership = false;
					boolean othersOwnership = false;
					boolean rootOwnership = subject.user.getRoot() == null ? false : subject.user.getRoot();

					// User of access object is owner and has a update
					// access?
					if (access.getOwner() != null && access.getOwner().equals(subject.user) && access.getPermission() != null && access.getPermission().getOwner().getOperations().contains(Operation.UPDATE)) {
						userOwnership = true;
					} else {

						// Check if it has update access based on
						// groups ownership.
						for (Group group : subject.user.getGroups()) {

							if (access.getGroup() != null && access.getGroup().equals(group) && access.getPermission() != null && access.getPermission().getGroup().getOperations().contains(Operation.UPDATE)) {
								groupOwnership = true;
							}
						}

						// Check if it has update access based on
						// others permission
						if (!groupOwnership) {
							if (access.getPermission() != null && access.getPermission().getOthers().getOperations().contains(Operation.UPDATE)) {
								othersOwnership = true;
							}
						}
					}

					if (userOwnership || groupOwnership || othersOwnership || rootOwnership) {
						((Access) parameter).setActive(access.getActive());
						((Access) parameter).setGroup(access.getGroup());
						((Access) parameter).setPermission(access.getPermission());
						((Access) parameter).setSite(access.getSite());
						((Access) parameter).setOwner(access.getOwner());
					} else {
						throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access.", subject.user.getName()));
					}

				} else {
					throw new GNUOpenBusinessServiceException("Access object is not found in database, access is denied.");
				}
			} else {
				// Parameter is instance of Type?
				if (parameter instanceof Type) {

					// Check if user has update access.
					if (!subject.user.getAccess().getOperations().contains(Operation.UPDATE)) {
						throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access.", subject.user.getName()));
					}
				}
			}
		}

		return ctx.proceed();
	}
}
