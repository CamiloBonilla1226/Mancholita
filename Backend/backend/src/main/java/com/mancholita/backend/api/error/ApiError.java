package com.mancholita.backend.api.error;

import java.time.Instant;
import java.util.Map;

public class ApiError {
    public Instant timestamp = Instant.now();
    public int status;
    public String error;
    public String message;
    public String path;
    public Map<String, String> fieldErrors;

    public static ApiError of(int status, String error, String message, String path) {
        ApiError a = new ApiError();
        a.status = status;
        a.error = error;
        a.message = message;
        a.path = path;
        return a;
    }
}