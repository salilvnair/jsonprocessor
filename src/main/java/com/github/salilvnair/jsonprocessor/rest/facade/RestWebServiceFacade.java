package com.github.salilvnair.jsonprocessor.rest.facade;

import com.github.salilvnair.jsonprocessor.rest.exception.RestWebServiceException;
import com.github.salilvnair.jsonprocessor.rest.handler.RestWebServiceDelegate;
import com.github.salilvnair.jsonprocessor.rest.handler.RestWebServiceHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.salilvnair.jsonprocessor.rest.model.RestWebServiceRequest;
import com.github.salilvnair.jsonprocessor.rest.model.RestWebServiceResponse;
import com.github.salilvnair.jsonprocessor.rest.retry.RetryExecutor;
import com.github.salilvnair.jsonprocessor.rest.retry.RetryExecutorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RestWebServiceFacade {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String REQUEST = "REQUEST";

    public static final String RESPONSE = "RESPONSE";

    public void initiate(RestWebServiceHandler handler, Map<String, Object> restWsMap, Object... objects) {
        if(handler == null) {
            throw new RestWebServiceException("Cannot initiate webservice call without a proper handler class");
        }
        RestWebServiceRequest request = null;
        if(!handler.emptyPayLoad()) {
            request = handler.prepareRequest(restWsMap, objects);
            if(handler.printLogs()) {
                printLogs(request, handler, REQUEST);
            }
        }
        RestWebServiceDelegate delegate = handler.delegate();
        if(delegate == null) {
            throw new RestWebServiceException("Cannot initiate webservice call without a proper client delegate bean for the handler", handler.webServiceName());
        }
        RestWebServiceResponse response = null;
        if(delegate.retry()) {
            RestWebServiceRequest finalRequest = request;
            try {
                 response =  new RetryExecutor()
                                    .maxRetries(delegate.maxRetries())
                                    .delay(delegate.delay(), delegate.delayTimeUnit())
                                    .configure(delegate.whiteListedExceptions())
                                    .execute(() -> delegate.invoke(finalRequest, restWsMap, objects));
            }
            catch (RetryExecutorException e) {
                throw new RestWebServiceException(e, handler.webServiceName());
            }
        }
        else {
            response = delegate.invoke(request, restWsMap, objects);
        }

        if(handler.printLogs()) {
            printLogs(response, handler, RESPONSE);
        }
        handler.processResponse(request, response, restWsMap, objects);
    }

    public void printLogs(Object requestResponse, RestWebServiceHandler handler, String type) {
        String webServiceName = handler.webServiceName();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{}";
        try {
            jsonString=mapper.writeValueAsString(requestResponse);
        }
        catch (Exception ex) {
            logger.error("RestWebServiceFacade>>printLogs>>caught exception:"+ex);
        }
        logger.info("====================================================" + webServiceName + " " + type + " BEGINS=================================================");
        logger.info(jsonString);
        logger.info("====================================================" + webServiceName + " " + type + " ENDS=================================================");
    }

}
