package com.github.salilvnair.jsonprocessor.rest.handler;

import com.github.salilvnair.jsonprocessor.rest.model.RestWebServiceRequest;
import com.github.salilvnair.jsonprocessor.rest.model.RestWebServiceResponse;

import java.util.Map;

public interface IRestWebServiceHandler {
    IRestWebServiceDelegate delegate();
    RestWebServiceRequest prepareRequest(Map<String, Object> restWsMap, Object... objects);
    void processResponse(RestWebServiceRequest request, RestWebServiceResponse response, Map<String, Object> restWsMap, Object... objects);
    default String webServiceName() { return null; };
    default boolean printLogs() { return true; };
    default boolean emptyPayLoad() { return false; };
}
