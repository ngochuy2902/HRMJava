package com.nals.hrm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Pagination<T> {

    @JsonProperty("current_page")
    private int currentPage;
    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("total")
    private Long total;
    @JsonProperty("last_page")
    private int lastPage;
}
