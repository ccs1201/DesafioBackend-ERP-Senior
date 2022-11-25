package com.ccs.erp.infrastructure.exceptionhandler;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class RealmeetExceptionHandler extends BaseExceptionHandler {

    public RealmeetExceptionHandler(MessageSource messageSource) {
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
        // e.printStackTrace();
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
        // e.printStackTrace();
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Uncaught error, please contact SYS Admin. Details: %s", e.getMessage()), "An unexpected error occur.");
    }

    /**
     * <p><b>Handler para exceptions do tipo {@link RealmeetEntityNotFoundException}</b></p>
     *
     * @param
     * @return
     */
//    @ExceptionHandler(RealmeetEntityNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ResponseEntity<Object> handleNotFoundException(RealmeetEntityNotFoundException e) {
//        return buildResponseEntity(HttpStatus.NOT_FOUND, e.getMessage(), String.format("%s não localizada.", e.getEntityName()));
//    }

    /**
     * <p><b>Handler para exceptions do tipo {@link RealmeetServiceException}</b></p>
     *
     * @param e
     * @return
     */
//    @ExceptionHandler(RealmeetServiceException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<Object> handleRealmeetServiceException(RealmeetServiceException e) {
//        return buildResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage(), "Requisição inválida.");
//    }

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
}
