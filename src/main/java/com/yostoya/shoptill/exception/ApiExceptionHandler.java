package com.yostoya.shoptill.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.yostoya.shoptill.domain.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler implements ErrorController {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatusCode statusCode,
                                                             WebRequest request) {

        log.error(exception.getMessage());
        return new ResponseEntity<>(getHttpException(
                exception.getMessage(),resolve(statusCode.value())), statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode statusCode,
                                                                  WebRequest request) {

        log.error(exception.getMessage());
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String fieldMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(getHttpException(
                fieldMessage, resolve(statusCode.value())), statusCode);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<HttpResponse> sQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {

        log.error(exception.getMessage());
        final String reason = exception.getMessage().contains("Duplicate entry") ?
                "Information already exists" :
                exception.getMessage();

        return new ResponseEntity<>(getHttpException(
                reason,BAD_REQUEST), BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException(BadCredentialsException exception) {

        log.error(exception.getMessage());
        return new ResponseEntity<>(getHttpException(
                exception.getMessage() + ", Incorrect email or password", BAD_REQUEST),  BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpResponse> apiException(ApiException exception) {

        log.error(exception.getMessage());
        return new ResponseEntity<>(getHttpException(exception.getMessage(),BAD_REQUEST), BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException(AccessDeniedException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(getHttpException(
                "Access denied. You don't have access",FORBIDDEN), FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> exception(Exception exception) {
        log.error(exception.getMessage());
        final String reason = exception.getMessage() != null ?
                (exception.getMessage().contains("expected 1, actual 0") ? "Record not found" : exception.getMessage()) : "Some error occurred";
        return new ResponseEntity<>(getHttpException(
                reason, INTERNAL_SERVER_ERROR), INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<HttpResponse> exception(JWTDecodeException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(getHttpException(
                "Could not decode the token", INTERNAL_SERVER_ERROR), INTERNAL_SERVER_ERROR);

    }

    private HttpResponse getHttpException(String message, HttpStatus status) {
        return HttpResponse.builder()
                .timestamp(now().toString())
                .message(message)
                .status(status)
                .statusCode(status.value())
                .build();
    }
}
