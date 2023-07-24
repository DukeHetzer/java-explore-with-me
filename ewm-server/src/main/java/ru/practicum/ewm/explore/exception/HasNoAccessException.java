package ru.practicum.ewm.explore.exception;

public class HasNoAccessException extends RuntimeException {
    public HasNoAccessException(String message) {
        super(message);
    }
}