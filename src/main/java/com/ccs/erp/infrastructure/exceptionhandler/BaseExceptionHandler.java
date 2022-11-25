package com.ccs.erp.infrastructure.exceptionhandler;


import com.ccs.erp.infrastructure.exceptionhandler.model.ApiExceptionResponse;
import com.ccs.erp.infrastructure.exceptionhandler.model.FieldValidationError;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

public class BaseExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public BaseExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private ApiExceptionResponse buildApiExceptionResponse(Exception e, HttpStatus httpStatus) {
        return ApiExceptionResponse.builder()
                .detail(e.getMessage())
                .status(httpStatus.value())
                .type(httpStatus.getReasonPhrase())
                .title(httpStatus.getReasonPhrase())
                .build();
    }

    private ApiExceptionResponse buildApiExceptionResponse(Exception e, HttpStatus httpStatus, @NotNull String title) {
        return ApiExceptionResponse.builder()
                .detail(e.getMessage())
                .status(httpStatus.value())
                .type(httpStatus.getReasonPhrase())
                .title(title)
                .build();
    }

    private ApiExceptionResponse buildApiExceptionResponse(HttpStatus httpStatus, @NotNull String detail, @NotNull String title) {
        return ApiExceptionResponse.builder()
                .detail(detail)
                .status(httpStatus.value())
                .type(httpStatus.getReasonPhrase())
                .title(title)
                .build();
    }

    protected ApiExceptionResponse buildApiValidationErrorResponse(HttpStatus status) {
        return ApiExceptionResponse.builder()
                .status(status.value())
                .type(status.getReasonPhrase())
                .build();
    }

    protected ResponseEntity<Object> buildResponseEntity(@NotNull HttpStatus httpStatus, @NotNull Exception exception, @NotNull String title) {
        return ResponseEntity.status(httpStatus).body(buildApiExceptionResponse(exception, httpStatus, title));

    }

    protected ResponseEntity<Object> buildResponseEntity(@NotNull HttpStatus httpStatus, @NotNull Exception exception) {
        return ResponseEntity.status(httpStatus).body(buildApiExceptionResponse(exception, httpStatus));

    }

    protected ResponseEntity<Object> buildResponseEntity(@NotNull HttpStatus httpStatus, @NotNull String detail, @NotNull String title) {
        return ResponseEntity.status(httpStatus).body(buildApiExceptionResponse(httpStatus, detail, title));
    }

    /**
     * <p><b>Constrói um {@link ApiExceptionResponse} contendo uma lista de {@link FieldValidationError}</b></p>
     *
     * <p>A lista contem todos os Fields (campos) que não passaram na validação do Beans Validation </p>
     *
     * @param ex
     * @param status
     * @return
     */
    protected ApiExceptionResponse buildWithFieldValidationsError(MethodArgumentNotValidException ex, HttpStatus status) {

        ApiExceptionResponse apiExceptionResponse
                = ApiExceptionResponse
                .builder()
                .status(status.value())
                .type(status.getReasonPhrase())
                .title("Um ou mais campos não são válidos.")
                .build();

        getFieldErrors(ex.getAllErrors(), apiExceptionResponse);

        return apiExceptionResponse;
    }

    /**
     * <p><b>Cria a lista de {@code FieldValidationError} com
     * cada erro de validação</b></></p>
     * <p>Cada FieldValidationError possui as informações do <br>
     * atributo que gerou o erro, a mensagem de validação e o valor rejeitado.</p>
     * <br>
     * Veja: {@link FieldValidationError}
     * <br>
     *
     * @param e                    lista de {@link ObjectError}
     * @param apiExceptionResponse a response que será devolvida ao cliente d API
     */
    private void getFieldErrors(List<ObjectError> e, ApiExceptionResponse apiExceptionResponse) {

        apiExceptionResponse.setDetails(new LinkedList<>());

        e.forEach(error -> {

            if (error instanceof FieldError fieldError) {

                apiExceptionResponse.getDetails().add(
                        FieldValidationError
                                .builder()
                                .field(fieldError.getField())
                                .fieldValidationMessage(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()))
                                .rejectedValue(String.format("%s", fieldError.getRejectedValue()))
                                .build());

            } else {
                apiExceptionResponse.getDetails().add(
                        FieldValidationError
                                .builder()
                                .field(error.getObjectName())
                                .fieldValidationMessage(messageSource.getMessage(error, LocaleContextHolder.getLocale()))
                                .build());
            }
        });
    }
}
