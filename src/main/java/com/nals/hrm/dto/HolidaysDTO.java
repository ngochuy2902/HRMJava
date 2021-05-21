package com.nals.hrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class HolidaysDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty(value = "updated_by", access = JsonProperty.Access.WRITE_ONLY)
    private Integer updatedBy;

    @JsonProperty("date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "date must not be null")
    private LocalDate date;

    @JsonProperty("notes")
    @NotBlank(message = "notes must not be blank")
    private String notes;
}
