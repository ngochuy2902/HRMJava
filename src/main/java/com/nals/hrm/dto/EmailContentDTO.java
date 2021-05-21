package com.nals.hrm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailContentDTO {

    @JsonProperty("subject")
    @NotBlank(message = "subject must not be blank")
    private String subject;

    @JsonProperty("content")
    @NotBlank(message = "content must not be blank")
    private String content;
}
