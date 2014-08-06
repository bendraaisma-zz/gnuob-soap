package com.netbrasoft.gnuob.generic.security;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.GenericTypeService;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.Parameter;
import com.netbrasoft.gnuob.generic.Type;
import com.netbrasoft.gnuob.generic.security.Rule.Operation;

public class AccessControl<A extends Access, U extends User, S extends Site> {

	private enum Permission {
		USER, GROUP, ROOT;

		private static final int USER_VALUE = 1;
		private static final int ROOT_VALUE = 3;
		private static final int GROUP_VALUE = 2;

		public int intValue() {
			switch (this) {
			case GROUP:
				return GROUP_VALUE;
			case ROOT:
				return ROOT_VALUE;
			default:
				return USER_VALUE;
			}
		}
	}

	private class Subject {
		private Site site = new Site();
		private User user = new User();
		private Permission permission = Permission.USER;

		private Subject() {
		}
	}

	@EJB(beanName = "GenericTypeServiceImpl")
	private GenericTypeService<A> accessTypeService;

	@EJB(beanName = "GenericTypeServiceImpl")
	private GenericTypeService<U> userServiceImpl;

	@EJB(beanName = "GenericTypeServiceImpl")
	private GenericTypeService<S> siteServiceImpl;

	private Object createOperationAccess(Subject subject, InvocationContext ctx) throws Exception {

		// Check user contain access to site.
		if (!subject.user.getSites().contains(subject.site)) {
			throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access for site [%s], verify that the given user have access to the site", subject.user.getName(), subject.site.getName()));
		}

		// Checks if the method contains a access, or type object as parameter
		for (Object parameter : ctx.getParameters()) {

			// Parameter is instance of Access?
			if (parameter instanceof Access) {

				// Check if user has create access.
				if (subject.user.getRule().getOperations().contains(Operation.CREATE)) {
					Access access = ((Access) parameter);
					access.setActive(true);
					access.setGroup(subject.user.getGroup());
					access.setPermission(new com.netbrasoft.gnuob.generic.security.Permission());
					access.setSite(subject.site);
					access.setUser(subject.user);
				} else {
					throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access", subject.user.getName()));
				}
			} else {
				if (parameter instanceof Type) {

					// Check if user has create access.
					if (!subject.user.getRule().getOperations().contains(Operation.CREATE)) {
						throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access", subject.user.getName()));
					}
				}
			}
		}

		return ctx.proceed();
	}

	private Object deleteOperationAccess(Subject subject, InvocationContext ctx) throws Exception {

		// Check user contain access to site.
		if (!subject.user.getSites().contains(subject.site)) {
			throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access for site [%s], verify that the given user have access to the site", subject.user.getName(), subject.site.getName()));
		}

		// Checks if the method contains a access, or type object as parameter
		for (Object parameter : ctx.getParameters()) {

			// Parameter is instance of Access?
			if (parameter instanceof Access) {

				// Cast parameter to a access object and get id.
				long accessId = ((Access) parameter).getId();

				@SuppressWarnings("unchecked")
				Access access = accessTypeService.find((A) parameter, accessId);

				// If access object is in database?
				if (access != null && access.getActive() != null && access.getActive()) {

					boolean userOwnership = false;
					boolean groupOwnership = false;

					// User of access object is owner and has a delete
					// access?
					if (access.getUser() != null && access.getUser().equals(subject.user) && access.getPermission() != null && access.getPermission().getUser().getOperations().contains(Operation.DELETE)) {
						userOwnership = true;
					} else {

						// Looks that the user is not the owner,
						// check if it has delete access based on
						// groups ownership.
						for (Group group : subject.user.getGroups()) {

							if (access.getGroup() != null && access.getGroup().equals(group) && access.getPermission() != null && access.getPermission().getGroup().getOperations().contains(Operation.DELETE)) {
								groupOwnership = true;
							}
						}
					}

					if (userOwnership || groupOwnership) {
						((Access) parameter).setActive(access.getActive());
						((Access) parameter).setGroup(access.getGroup());
						((Access) parameter).setPermission(access.getPermission());
						((Access) parameter).setSite(access.getSite());
						((Access) parameter).setUser(access.getUser());
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
					if (!subject.user.getRule().getOperations().contains(Operation.DELETE)) {
						throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access", subject.user.getName()));
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
		accessTypeService.disableFilter(Access.NFQ4);
	}

	private void enableAccessFilter(Subject subject) {
		accessTypeService.enableFilter(Access.NFQ1, new Parameter("userId", subject.user.getId()));
		accessTypeService.enableFilter(Access.NFQ2, new Parameter("siteId", subject.site.getId()));
		accessTypeService.enableFilter(Access.NFQ3, new Parameter("active", true));
		accessTypeService.enableFilter(Access.NFQ4, new Parameter("permission", subject.permission.intValue()));
	}

	@SuppressWarnings("unchecked")
	private Subject getSubject(InvocationContext ctx) throws GNUOpenBusinessServiceException {

		Subject subject = new Subject();

		for (Object parameter : ctx.getParameters()) {

			// Parameter is instance of MetaData?
			if (parameter instanceof MetaData) {

				// Cast parameter to a meta data object.
				MetaData metaData = (MetaData) parameter;

				// Check site credentialsW
				if (siteServiceImpl.count((S) new Site(metaData.getSite())) > 0) {
					subject.site = siteServiceImpl.find((S) new Site(metaData.getSite()), Paging.getInstance(0, 1), OrderBy.NONE).get(0);
				} else {
					throw new GNUOpenBusinessServiceException(String.format("Given site [%s] doesn't have the right access, verify that the given site has access.", metaData.getSite()));
				}

				// Check user credentials
				if (userServiceImpl.count((U) new User(metaData.getUser(), metaData.getPassword())) > 0) {
					subject.user = userServiceImpl.find((U) new User(metaData.getUser(), metaData.getPassword()), Paging.getInstance(0, 1), OrderBy.NONE).get(0);
				} else {
					throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access.", metaData.getUser()));
				}
			}
		}

		return subject;
	}

	@AroundInvoke
	public Object intercept(InvocationContext ctx) throws Exception {

		OperationAccess operationAccess = ctx.getMethod().getAnnotation(OperationAccess.class);

		if (operationAccess != null) {

			switch (operationAccess.operation()) {
			case CREATE:
				return createOperationAccess(getSubject(ctx), ctx);
			case READ:
				return readOperationAccess(getSubject(ctx), ctx);
			case UPDATE:
				return updateOperationAccess(getSubject(ctx), ctx);
			case NONE:
				return ctx.proceed();
			default: // DELETE
				return deleteOperationAccess(getSubject(ctx), ctx);
			}
		} else {
			throw new GNUOpenBusinessServiceException("Operation access is not set on this method, access is denied.");
		}
	}

	private Object readOperationAccess(Subject subject, InvocationContext ctx) throws Exception {

		// Check user contain access to site.
		if (!subject.user.getSites().contains(subject.site)) {
			throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access for site [%s], verify that the given user have access to the site.", subject.user.getName(), subject.site.getName()));
		}

		// Checks if the method contains a access object as parameter
		for (Object parameter : ctx.getParameters()) {

			// Parameter is instance of Access?
			if (parameter instanceof Access) {

				// Check if user has read access.
				if (!subject.user.getRule().getOperations().contains(Operation.READ)) {

					// Check if a group has read access.
					for (Group group : subject.user.getGroups()) {
						if (group.getRule().getOperations().contains(Operation.READ)) {
							subject.permission = Permission.GROUP;
						}
					}

					if (subject.permission != Permission.GROUP) {
						throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access.", subject.user.getName()));
					}
				}
			} else {
				// Parameter is instance of Type?
				if (parameter instanceof Type) {

					// Check if user has delete access.
					if (!subject.user.getRule().getOperations().contains(Operation.READ)) {
						throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access", subject.user.getName()));
					}
				}
			}
		}

		enableAccessFilter(subject);
		Object proceed = ctx.proceed();
		disableAccessFilter();

		return proceed;
	}

	private Object updateOperationAccess(Subject subject, InvocationContext ctx) throws Exception {

		// Check user contain access to site.
		if (!subject.user.getSites().contains(subject.site)) {
			throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access for site [%s], verify that the given user have access to the site.", subject.user.getName(), subject.site.getName()));
		}

		// Checks if the method contains a access object as parameter
		for (Object parameter : ctx.getParameters()) {

			// Parameter is instance of Access?
			if (parameter instanceof Access) {

				// Cast parameter to a access object and get id.
				long accessId = ((Access) parameter).getId();

				@SuppressWarnings("unchecked")
				Access access = accessTypeService.find((A) parameter, accessId);

				// If access object is in database?
				if (access != null && access.getActive() != null && access.getActive()) {

					boolean userOwnership = false;
					boolean groupOwnership = false;

					// User of access object is owner and has a update
					// access?
					if (access.getUser() != null && access.getUser().equals(subject.user) && access.getPermission() != null && access.getPermission().getUser().getOperations().contains(Operation.UPDATE)) {
						userOwnership = true;
					} else {

						// Looks that the user is not the owner,
						// check if it has update access based on
						// groups ownership.
						for (Group group : subject.user.getGroups()) {

							if (access.getGroup() != null && access.getGroup().equals(group) && access.getPermission() != null && access.getPermission().getGroup().getOperations().contains(Operation.UPDATE)) {
								groupOwnership = true;
							}
						}

					}

					if (userOwnership || groupOwnership) {
						((Access) parameter).setActive(access.getActive());
						((Access) parameter).setGroup(access.getGroup());
						((Access) parameter).setPermission(access.getPermission());
						((Access) parameter).setSite(access.getSite());
						((Access) parameter).setUser(access.getUser());
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
					if (!subject.user.getRule().getOperations().contains(Operation.UPDATE)) {
						throw new GNUOpenBusinessServiceException(String.format("Given user [%s] doesn't have the right access, verify that the given user has access", subject.user.getName()));
					}
				}
			}
		}

		return ctx.proceed();
	}
}
