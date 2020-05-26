package com.step.stupid.social.network.exception.handler;

import com.step.stupid.social.network.dto.exception.response.ExceptionResponse;
import com.step.stupid.social.network.exception.BadRequestException;
import com.step.stupid.social.network.exception.FileStorageException;
import com.step.stupid.social.network.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalRestControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> fieldErrorMap = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> {
                            String defaultMessage = fieldError.getDefaultMessage();
                            if (!StringUtils.isEmpty(defaultMessage)) {
                                return defaultMessage;
                            } else {
                                return "Something went wrong";
                            }
                        }
                ));
        fieldErrorMap.put("timestamp", new Date().toString());

        return new ResponseEntity<>(fieldErrorMap, headers, status);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        ExceptionResponse response = transformExceptionToResponse(
                e, 404, "Not found"
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        ExceptionResponse response = transformExceptionToResponse(e, 400, "Bad request");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {FileStorageException.class})
    public ResponseEntity<Object> handleFileStorageException(FileStorageException e) {
        ExceptionResponse response = transformExceptionToResponse(e, 400, "Bad request");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ExceptionResponse transformExceptionToResponse(Exception e,
                                                           int status,
                                                           String exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setException(exception);
        exceptionResponse.setStatus(status);
        exceptionResponse.setTimestamp(LocalDateTime.now().toString());

        return exceptionResponse;
    }
}
