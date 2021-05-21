package com.nals.hrm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    private Meta<T> meta;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public Response(Meta<T> meta) {
        this.meta = meta;
    }
}
