package com.nals.hrm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRes {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("full_name")
    private String fullName;
}
