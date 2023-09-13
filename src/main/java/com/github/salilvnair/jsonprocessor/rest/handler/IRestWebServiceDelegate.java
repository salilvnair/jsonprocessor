package com.github.salilvnair.jsonprocessor.rest.handler;

import com.github.salilvnair.jsonprocessor.rest.model.RestWebServiceRequest;
import com.github.salilvnair.jsonprocessor.rest.model.RestWebServiceResponse;

import java.util.Map;

public interface IRestWebServiceDelegate {
    RestWebServiceResponse invoke(RestWebServiceRequest request, Map<String, Object> restWsMap, Object... objects);
}
