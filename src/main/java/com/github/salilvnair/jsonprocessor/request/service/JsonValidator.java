package com.github.salilvnair.jsonprocessor.request.service;

import com.github.salilvnair.jsonprocessor.request.context.JsonValidatorContext;
import com.github.salilvnair.jsonprocessor.request.context.ValidationMessage;
import com.github.salilvnair.jsonprocessor.request.core.JsonRequest;
import com.github.salilvnair.jsonprocessor.request.helper.JsonValidatorUtil;

import java.util.List;

public interface JsonValidator {
    static List<ValidationMessage> validate(JsonRequest request, JsonValidatorContext jsonValidatorContext) {
        jsonValidatorContext.setRootRequest(request);
        return JsonValidatorUtil.validate(jsonValidatorContext);
    }
    static List<ValidationMessage> validate(List<?> requestList, JsonValidatorContext jsonValidatorContext) {
        jsonValidatorContext.setRootList(requestList);
        return JsonValidatorUtil.validate(jsonValidatorContext);
    }
}
