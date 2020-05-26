package com.step.stupid.social.network.dto.exception.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ExceptionResponse {

    private String message;
    private String exception;
    private int status;
    private HttpStatus httpStatus;
    private String timestamp;
}
