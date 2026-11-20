package bac.ac.libis.service;

import java.util.LinkedHashMap;
import java.util.Map;

public class ValidationException extends Exception {
    private final Map<String, String> errors;

    public ValidationException() {
        this.errors = new LinkedHashMap<>();
    }

    public ValidationException(Map<String, String> errors) {
        this.errors = new LinkedHashMap<>(errors);
    }

    public void addError(String field, String message) {
        errors.put(field, message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
