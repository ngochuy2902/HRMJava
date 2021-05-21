package com.nals.hrm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meta<T> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Pagination<T> pagination;
    private String message;
    private Map<String, Errors> errors;

    public Meta(String message) {
        this.message = message;
    }
}
