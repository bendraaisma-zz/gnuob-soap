package com.netbrasoft.gnuob.generic.offer;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.netbrasoft.gnuob.exception.GNUOpenBusinessServiceException;
import com.netbrasoft.gnuob.generic.GenericTypeWebService;
import com.netbrasoft.gnuob.generic.OrderBy;
import com.netbrasoft.gnuob.generic.Paging;
import com.netbrasoft.gnuob.generic.contract.Contract;
import com.netbrasoft.gnuob.generic.customer.Customer;
import com.netbrasoft.gnuob.generic.security.MetaData;
import com.netbrasoft.gnuob.generic.security.SecuredGenericTypeService;

@WebService(targetNamespace = "http://gnuob.netbrasoft.com/")
@Stateless(name = "OfferWebServiceImpl")
public class OfferWebServiceImpl<O extends Offer> implements GenericTypeWebService<O> {

    @EJB(beanName = "SecuredGenericTypeServiceImpl")
    private SecuredGenericTypeService<O> securedGenericOfferService;

    @EJB(beanName = "SecuredGenericTypeServiceImpl")
    private SecuredGenericTypeService<Contract> securedGenericContractService;

    @EJB(beanName = "SecuredGenericTypeServiceImpl")
    private SecuredGenericTypeService<Customer> securedGenericCustomerService;

    @Override
    @WebMethod(operationName = "countOffer")
    public long count(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O type) {
        try {
            return securedGenericOfferService.count(metadata, type);
        } catch (Exception e) {
            throw new GNUOpenBusinessServiceException(e.getMessage(), e);
        }
    }

    @Override
    @WebMethod(operationName = "findOfferById")
    public O find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O type) {
        try {
            return securedGenericOfferService.find(metadata, type, type.getId());
        } catch (Exception e) {
            throw new GNUOpenBusinessServiceException(e.getMessage(), e);
        }
    }

    @Override
    @WebMethod(operationName = "findOffer")
    public List<O> find(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O type, @WebParam(name = "paging") Paging paging, @WebParam(name = "orderBy") OrderBy orderBy) {
        try {
            return securedGenericOfferService.find(metadata, type, paging, orderBy);
        } catch (Exception e) {
            throw new GNUOpenBusinessServiceException(e.getMessage(), e);
        }
    }

    @Override
    @WebMethod(operationName = "mergeOffer")
    public O merge(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O type) {
        try {
            securedGenericOfferService.merge(metadata, type);
            return type;
        } catch (Exception e) {
            throw new GNUOpenBusinessServiceException(e.getMessage(), e);
        }
    }

    @Override
    @WebMethod(operationName = "persistOffer")
    public O persist(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O type) {
        try {
            securedGenericCustomerService.persist(metadata, type.getContract().getCustomer());
            securedGenericContractService.persist(metadata, type.getContract());
            securedGenericOfferService.persist(metadata, type);
            return type;
        } catch (Exception e) {
            throw new GNUOpenBusinessServiceException(e.getMessage(), e);
        }
    }

    @Override
    @WebMethod(operationName = "refreshOffer")
    public O refresh(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O type) {
        try {
            return securedGenericOfferService.refresh(metadata, type, type.getId());
        } catch (Exception e) {
            throw new GNUOpenBusinessServiceException(e.getMessage(), e);
        }
    }

    @Override
    @WebMethod(operationName = "removeOffer")
    public void remove(@WebParam(name = "metaData", header = true) MetaData metadata, @WebParam(name = "offer") O type) {
        try {
            securedGenericOfferService.remove(metadata, type);
        } catch (Exception e) {
            throw new GNUOpenBusinessServiceException(e.getMessage(), e);
        }
    }

}
