package com.nals.hrm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

    private static final Integer serialVersionUID = 1;
    private  String message;
    private Integer id;

    public ResourceNotFoundException(String message, Integer id) {
        this.message = message;
        this.id = id;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public Integer getId() {
        return this.id;
    }
}
