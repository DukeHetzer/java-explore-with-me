package ru.practicum.ewm.explore.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;

@Slf4j
@RestControllerAdvice("ru.practicum.ewm")
public class ErrorHandler {
    @ExceptionHandler({BadRequestException.class, ConstraintViolationException.class, DuplicateKeyException.class,
            MethodArgumentNotValidException.class, MissingServletRequestParameterException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentHandler(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({NotFoundException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandler(Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({ConditionException.class, HasNoAccessException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse notAllowedHandler(Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({ConflictException.class, DataIntegrityViolationException.class,
            HttpClientErrorException.Conflict.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conditionsNotMetHandler(Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}