package com.nals.hrm.exception;

public class UnprocessableEntityException extends Exception {

    private static final Integer serialVersionUID = 1;
    private  String message;
    private Integer id;

    public UnprocessableEntityException(String message, Integer id) {
        this.id = id;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public Integer getId() {
        return this.id;
    }
}
