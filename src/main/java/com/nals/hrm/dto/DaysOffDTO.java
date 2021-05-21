package com.nals.hrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nals.hrm.model.DaysOffList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DaysOffDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("po_name")
    @NotBlank(message = "PO's Name must not be blank")
    private String poName;

    @JsonProperty("po_email")
    @Pattern(regexp = "\\w+.@nal.vn", message = "PO's Email must match with @nal.vn")
    private String poEmail;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("vacation_day")
    private LocalDate vacationDay;

    @JsonProperty("session_day_off")
    @Min(value = 1)
    @Max(value = 3)
    private Integer sessionDayOff;

    @JsonProperty("reasons")
    private String reasons;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("vacation_type_id")
    private Integer vacationTypeId;

    @JsonProperty("user_id")
    @NotNull(message = "User Id must not be blank")
    private Integer userId;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("email")
    private String email;

    @JsonProperty(value = "days_off_lists", access = JsonProperty.Access.WRITE_ONLY)
    private List<@NotNull @Valid DaysOffList> daysOffLists;

    @JsonProperty(value = "session_day_off_name", access = JsonProperty.Access.WRITE_ONLY)
    private String sessionDayOffName;

    @JsonProperty(value = "vacation_type_name", access = JsonProperty.Access.WRITE_ONLY)
    private String vacationTypeName;
}
