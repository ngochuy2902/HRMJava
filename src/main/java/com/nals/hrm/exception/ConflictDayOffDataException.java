package com.nals.hrm.exception;

import java.util.Map;

public class ConflictDayOffDataException extends Exception{
    private String message;
    private Map<String, String> causes;

    public ConflictDayOffDataException(String message, Map<String, String> causes) {
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
