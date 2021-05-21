package com.nals.hrm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictException extends Exception {

    private static final Integer serialVersionUID = 1;
    private String message;
    private Map<String, String> causes;

    public ConflictException(String message, Map<String, String> causes) {
        this.message = message;
        this.causes = causes;
    }

    public Map<String, String> getCauses() {
        return this.causes;
    }
    public String getMessage() {
        return this.message;
    }
}
