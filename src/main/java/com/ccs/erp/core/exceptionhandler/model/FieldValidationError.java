package com.ccs.erp.core.exceptionhandler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class FieldValidationError {
    private String field;
    private String fieldValidationMessage;
    private String rejectedValue;

}
