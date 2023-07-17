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
    @ExceptionHandler({ValidationException.class, ConstraintViolationException.class,
            MissingServletRequestParameterException.class, DuplicateKeyException.class,
            BadRequestException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentExceptionHandler(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({NullPointerException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFoundExceptionHandler(Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({NotAllowedException.class, ConditionsNotMetException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse notAllowedExceptionHandler(Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, HttpClientErrorException.Conflict.class,
            DataIntegrityViolationException.class, ConflictRequestException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conditionsNotMetExceptionHandler(Exception e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}