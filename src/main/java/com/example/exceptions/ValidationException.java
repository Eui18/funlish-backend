package com.example.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super(errors.get(0));
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}