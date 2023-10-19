package com.github.salilvnair.jsonprocessor.rest.handler;

import com.github.salilvnair.jsonprocessor.rest.model.RestWebServiceRequest;
import com.github.salilvnair.jsonprocessor.rest.model.RestWebServiceResponse;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface RestWebServiceDelegate {
    RestWebServiceResponse invoke(RestWebServiceRequest request, Map<String, Object> restWsMap, Object... objects);

    default boolean retry() {return false;}

    default List<String> whiteListedExceptions() { return Collections.emptyList(); }

    default int delay() { return 0; }

    default int maxRetries() {return 0;}

    default TimeUnit delayTimeUnit() { return TimeUnit.MINUTES;}
}
