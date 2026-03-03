package com.mancholita.backend.api.error;

import java.time.Instant;
import java.util.Map;

public class ApiError {

    public String timestamp;
    public int status;
    public String error;
    public String message;
    public String path;
    private Map<String, String> fieldErrors;

    public static ApiError of(int status, String error, String message, String path) {
        ApiError api = new ApiError();
        api.timestamp = Instant.now().toString();
        api.status = status;
        api.error = error;
        api.message = message;
        api.path = path;
        return api;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}