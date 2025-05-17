package com.challenge.banregio.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class ChallengeExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, MissingRequestHeaderException.class})
    protected ProblemDetail handleConstraintViolationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("Error method: {}:{}", request.getMethod(), Arrays.toString(e.getDetailMessageArguments()));
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, Arrays.toString(e.getDetailMessageArguments()));
    }
}
