package ru.practicum.ewm.explore.exception;


public class ErrorResponse extends RuntimeException {
    public ErrorResponse(String message) {
        super(message);
    }
}