package com.ccs.erp.infrastructure.exceptionhandler;

import com.ccs.erp.infrastructure.configuration.beanvalidation.exception.EntityValidationException;
import com.ccs.erp.infrastructure.configuration.beanvalidation.exception.FieldValidationException;
import com.ccs.erp.infrastructure.exception.*;
import com.ccs.erp.infrastructure.exceptionhandler.model.ApiValidationErrorResponse;
import com.ccs.erp.infrastructure.exceptionhandler.model.FieldValidationError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>{>@code RestControllerAdvice} Responsável pelo encaminhado das
 * Exceptions da API para o consumidor</p>
 *
 * @author Cleber Souza
 * @version 1.0
 * @since 22/08/2022
 */
@SuppressWarnings("NullableProblems")
@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends BaseExceptionHandler {

    @Autowired
    private MessageSource messageSource;
    private static final String INVALID_FIELD_VALUES = "Valor inválido para um ou mais campos, verifique";

    public ApiExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * <p><b>Handler genérico para capturar qualquer exceção não tratada pelo nosso handler</b></p>
     *
     * @param e uma exceção não prevista no handler
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> unCaughtHandler(Exception e) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Uncaught error, please contact SYS Admin. Details: %s", e.getMessage()), "An unexpected error occur.");
    }

    /**
     * <p><b>Handler genérico para capturar qualquer {@link RuntimeException} não tratada pelo nosso handler</b></p>
     *
     * @param e uma exceção não prevista no handler
     * @return ResponseEntity
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runTimeExceptionHandler(RuntimeException e) {

        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,
                String.format("Uncaught error, please contact SYS Admin. Details: %s", e.getMessage()),
                "An unexpected error occur.");
    }

    /**
     * <p><b>Hanlder para exceptions do tipo {@link MethodArgumentNotValidException}</b></p>
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return {@link ResponseEntity} contento os campos inválidos no body
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(status).body(buildWithFieldValidationsError(ex, status));
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
//    @ApiResponse(responseCode = "409", description = "Violação da integridade dos dados.")
    ResponseEntity<Object> constraintViolationExceptionHandler(ConstraintViolationException e) {

        Throwable rootCause = ExceptionUtils.getRootCause(e);

        return buildResponseEntity(HttpStatus.CONFLICT, rootCause.getMessage(), "Violação de integridade de dados. Verifique os detalhes:");
    }

    @ExceptionHandler(EntityValidationException.class)
//    @ApiResponse(responseCode = "400", description = INVALID_FIELD_VALUES)
    public ResponseEntity<Object> entityValidationExceptionHandler(EntityValidationException e) {

        ApiValidationErrorResponse apiValidationErrorResponse = ApiValidationErrorResponse.builder()
                .type(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        getBindingResults(e.getBindingResult().getAllErrors(), apiValidationErrorResponse);


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiValidationErrorResponse);
    }

    @ExceptionHandler(FieldValidationException.class)
//    @ApiResponse(responseCode = "400", description = INVALID_FIELD_VALUES)
    public ResponseEntity<Object> fieldValidationExceptionHandler(FieldValidationException e) {
        return buildResponseEntity(
                HttpStatus.BAD_REQUEST, e, INVALID_FIELD_VALUES);
    }


    @ExceptionHandler(RepositoryEntityNotFoundException.class)
//    @ApiResponse(responseCode = "404", description = "Nenhum registro localizado para os parâmetros informados.")
    public ResponseEntity<?> entityNotFoundExceptionHandler(RepositoryEntityNotFoundException e) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, e, "Nenhum registro localizado com os parâmetros informados.");
    }

    @ExceptionHandler(BusinessLogicException.class)
//    @ApiResponse(responseCode = "400", description = "Ocorreu uma exceção de regra de negocio.")
    public ResponseEntity<?> businessLogicExceptionHandler(BusinessLogicException e) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, e, INVALID_FIELD_VALUES);
    }

    @ExceptionHandler(ServiceException.class)
//    @ApiResponse(responseCode = "400", description = "Ocorreu uma exceção ma camada de serviço.")
    public ResponseEntity<Object> serviceExceptionHandler(ServiceException e) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, e, INVALID_FIELD_VALUES);
    }

    @ExceptionHandler(RepositoryEntityInUseException.class)
//    @ApiResponse(responseCode = "409", description = "Registro em uso não pode ser removido")
    public ResponseEntity<?> entityInUseExceptionHandler(RepositoryEntityInUseException e) {

        return buildResponseEntity(HttpStatus.CONFLICT, e,
                "Registro em uso e não pode ser removido, considere inativar para que não seja mais exibido");
    }

    @ExceptionHandler(RepositoryEntityPersistException.class)
//    @ApiResponse(responseCode = "500", description = "O registro não pode gravado")
    public ResponseEntity<?> entityPersistExceptionHandler(RepositoryEntityPersistException e) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e, INVALID_FIELD_VALUES);
    }

//    @ExceptionHandler(GenericEntityUpdateMergerUtilException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ApiResponse(responseCode = "400", description = "Não foi possível converter o model de representação em entidade")
//    public ResponseEntity<?> genericEntityUpdateMergerUtilExceptionHandler(GenericEntityUpdateMergerUtilException e) {
//        return buildResponseEntity(HttpStatus.BAD_REQUEST, e, INVALID_FIELD_VALUES);
//    }

    @ExceptionHandler(RepositoryEntityUpdateException.class)
//    @ApiResponse(responseCode = "400", description = "Registro não pode ser atualizado")
    public ResponseEntity<?> entityUpdateExceptionHandler(RepositoryEntityUpdateException e) {

        Throwable rootCause = ExceptionUtils.getRootCause(e);

        if (rootCause instanceof PropertyBindingException) {

            return propertyBindingExceptionHandler((PropertyBindingException) rootCause, HttpStatus.BAD_REQUEST);

        } else if (rootCause instanceof InvalidFormatException) {

            return iInvalidFormatExceptionHandler((InvalidFormatException) rootCause, HttpStatus.BAD_REQUEST);
        }

        return buildResponseEntity(HttpStatus.BAD_REQUEST, e, INVALID_FIELD_VALUES);
    }

    @ExceptionHandler(RepositoryDataIntegrityViolationException.class)
//    @ApiResponse(responseCode = "409", description = "Ocorreu uma violação de constraint")
    public ResponseEntity<?> repositoryDataIntegrityViolationExceptionHandler(RepositoryDataIntegrityViolationException e) {

        //   Throwable rootCause = ExceptionUtils.getRootCause(e);

        return buildResponseEntity(HttpStatus.CONFLICT, e.getMessage(), INVALID_FIELD_VALUES);
    }

    @ExceptionHandler(JsonParseException.class)
//    @ApiResponse(responseCode = "400", description = "Malformed data, check JSON syntax")
    public ResponseEntity<?> jsonParseExceptionHandler(JsonParseException e) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, e, INVALID_FIELD_VALUES);
    }

//    @ExceptionHandler(RelatorioJasperException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ApiResponse(responseCode = "500", description = "Report generation error")
//    public ResponseEntity<?> relatorioJasperExceptionHandler(RelatorioJasperException e) {
//        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e, "Erro ao gerar relatório");
//    }

  /*  @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ApiResponse(responseCode = "412", description = "malformed data")
    public ResponseEntity<?> jsonParseExceptionHandler(NullPointerException e){
        return buildResponseEntity(HttpStatus.PRECONDITION_FAILED, e);
    }*/


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NotNull Exception ex, @Nullable Object body, @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request) {


        if (body == null) {
            return buildResponseEntity(status, ex, INVALID_FIELD_VALUES);

        } else {
            return new ResponseEntity<>(body, headers, status);
        }
    }

    private ApiValidationErrorResponse validationErrorsHandler(MethodArgumentNotValidException ex, HttpStatus status) {

        ApiValidationErrorResponse apiValidationErrorResponse
                = ApiValidationErrorResponse
                .builder()
                .status(status.value())
                .type(status.getReasonPhrase())
                .build();

        getBindingResults(ex.getAllErrors(), apiValidationErrorResponse);

        return apiValidationErrorResponse;
    }

    /**
     * Não mexa na sequência dos IFs a ordem importa. ;)
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof JsonParseException) {
            return jsonParseExceptionHandler((JsonParseException) rootCause, status);
        }

        if (rootCause instanceof InvalidFormatException) {
            return iInvalidFormatExceptionHandler((InvalidFormatException) rootCause, status);
        }

        if (rootCause instanceof PropertyBindingException) {
            return propertyBindingExceptionHandler((PropertyBindingException) rootCause, status);
        }

//        if (rootCause instanceof JsonMappingException) {
//            return jsonMappingExceptionHandler((JsonMappingException) rootCause, status);
//        }


        return buildResponseEntity(status,
                "Malformed data. O formato do dados deve coincidir com o formato JSON.",
                "Valor inválido para um ou mais campos");
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return this.methodArgumentTypeMismatchExceptionHandler((MethodArgumentTypeMismatchException) ex, status);
        }

        return ResponseEntity.status(status).body(buildResponseEntity(status, ex));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String detail = String.format("O recurso solicitado: %s, não existe.", ex.getRequestURL());

        return buildResponseEntity(status, detail, "Recurso nao existe.");

    }

    private ResponseEntity<Object> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e, HttpStatus status) {
        String detail =
                String.format("Parâmetro %s inválido. Esperado %s recebido %s",
                        e.getName(), Objects.requireNonNull(e.getRequiredType()).getSimpleName(),
                        Objects.requireNonNull(e.getValue()).getClass().getSimpleName());

        var errorResponse = buildApiValidationErrorResponse(status);

        errorResponse.getDetails().add(
                FieldValidationError
                        .builder()
                        .fieldValidationMessage(detail)
                        .rejectedValue(e.getValue().toString())
                        .build()
        );


        return ResponseEntity.status(status).body(errorResponse);
    }

    private ResponseEntity<Object> jsonParseExceptionHandler(JsonParseException e, HttpStatus status) {
        var errorResponse = buildApiValidationErrorResponse(status);

        String message = e.getMessage()
                .replace("\n at [Source: (org.springframework.util.StreamUtils$NonClosingInputStream); line: 4, column: 21]", "");

        errorResponse.getDetails().add(
                FieldValidationError
                        .builder()
                        .fieldValidationMessage(message)
                        .build());

        return ResponseEntity.status(status).body(errorResponse);
    }

    private ResponseEntity<Object> propertyBindingExceptionHandler(PropertyBindingException e, HttpStatus status) {

        return buildResponseEntity(status,
                String.format("Propriedade %s não existe.", e.getPropertyName()), INVALID_FIELD_VALUES);
    }

    private ResponseEntity<Object> iInvalidFormatExceptionHandler(InvalidFormatException e, HttpStatus status) {

        var errorResponse = buildApiValidationErrorResponse(status);
        errorResponse.setDetails(new ArrayList<>());

        e.getPath().forEach(path -> errorResponse
                .getDetails()
                .add(
                        FieldValidationError
                                .builder()
                                .field(path.getFieldName())
                                .fieldValidationMessage(
                                        String.format("Esperado %s recebido %s",
                                                e.getTargetType().getSimpleName(), e.getValue().getClass().getSimpleName()))
                                .rejectedValue((String) e.getValue())
                                .build()));

        return ResponseEntity.status(status).body(errorResponse);
    }

    private ResponseEntity<Object> jsonMappingExceptionHandler(JsonMappingException e, HttpStatus status) {

        var errorResponse = buildApiValidationErrorResponse(status);

        e.getPath().forEach(path -> errorResponse
                .getDetails()
                .add(
                        FieldValidationError
                                .builder()
                                .field(path.getFieldName())
                                .fieldValidationMessage(e.getOriginalMessage())
                                .build()));

        return ResponseEntity.status(status).body(errorResponse);
    }


    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    private void getBindingResults(List<ObjectError> e, ApiValidationErrorResponse apiValidationErrorResponse) {
        e.forEach(error -> {

            if (error instanceof FieldError fieldError) {

                apiValidationErrorResponse.getDetails().add(
                        FieldValidationError
                                .builder()
                                .field(fieldError.getField())
                                .fieldValidationMessage(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()))
                                .rejectedValue(String.format("%s", fieldError.getRejectedValue()))
                                .build());

            } else {
                apiValidationErrorResponse.getDetails().add(
                        FieldValidationError
                                .builder()
                                .field(error.getObjectName())
                                .fieldValidationMessage(messageSource.getMessage(error, LocaleContextHolder.getLocale()))
                                .build());
            }
        });
    }


    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.bindExceptionHandler(ex);
    }

    private ResponseEntity<Object> bindExceptionHandler(BindException e) {

        ApiValidationErrorResponse apiValidationErrorResponse = ApiValidationErrorResponse.builder()
                .type(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        getBindingResults(e.getBindingResult().getAllErrors(), apiValidationErrorResponse);


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiValidationErrorResponse);
    }
}
