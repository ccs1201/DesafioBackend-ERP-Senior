package com.ccs.erp.infrastructure.exceptionhandler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Collection;

/**
 * <a href="https://datatracker.ietf.org/doc/html/rfc7807">rfc7807</a>
 * <p>
 * RFC 7807                     Problem Details                  March 2016
 * </p>
 * <p>
 * For example, an HTTP response carrying JSON problem details:
 * <p>
 * HTTP/1.1 403 Forbidden
 * Content-Type: application/problem+json
 * Content-Language: en
 * </p>
 * {
 * "type": "https://example.com/probs/out-of-credit",
 * "title": "You do not have enough credit.",
 * "detail": "Your current balance is 30, but that costs 50.",
 * "instance": "/account/12345/msgs/abc",
 * "balance": 30,
 * "accounts": ["/account/12345",
 * "/account/67890"]
 * }
 * </p>
 * <p>
 * Here, the out-of-credit problem (identified by its type URI)
 * indicates the reason for the 403 in "title", gives a reference for
 * the specific problem occurrence with "instance", gives occurrence-
 * specific details in "detail", and adds two extensions; "balance"
 * conveys the account's balance, and "accounts" gives links where the
 * account can be topped up.
 * </p>
 * <p>
 * The ability to convey problem-specific extensions allows more than
 * one problem to be conveyed.  For example:
 * </p>
 * <p>
 * HTTP/1.1 400 Bad Request
 * Content-Type: application/problem+json
 * Content-Language: en
 * </p>
 * <p>
 * {
 * "type": "https://example.net/validation-error",
 * "title": "Your request parameters didn't validate.",
 * "invalid-params": [ {
 * "name": "age",
 * "reason": "must be a positive integer"
 * },
 * {
 * "name": "color",
 * "reason": "must be 'green', 'red' or 'blue'"}
 * ]
 * }
 * </p>
 * <p>
 * Note that this requires each of the subproblems to be similar enough
 * to use the same HTTP status code.  If they do not, the 207 (Multi-
 * Status) [RFC4918] code could be used to encapsulate multiple status
 * messages.
 * </p>
 */
@Builder
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiExceptionResponse {

    @Setter(AccessLevel.PRIVATE)
    @Builder.Default
    private OffsetDateTime timeStamp = OffsetDateTime.now();
    private int status;
    private String type; //Type serve para passarmos A URL do help caso exista.
    private String title;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String detail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Collection<FieldValidationError> details;



}

