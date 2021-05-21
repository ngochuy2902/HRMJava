package com.nals.hrm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class DaysOffList {


    @JsonProperty("vacation_day")
    @NotNull(message = "vacation day must not be null")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate vacationDay;

    @JsonProperty("session_day_id")
    @NotNull(message = "session day must not be null")
    private Integer sessionDayOffId;

    @JsonProperty("vacation_type_id")
    @NotNull(message = "vacation type must not be null")
    private Integer vacationTypeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DaysOffList)) return false;
        DaysOffList that = (DaysOffList) o;
        return Objects.equals(vacationDay, that.vacationDay) &&
                Objects.equals(sessionDayOffId, that.sessionDayOffId) &&
                Objects.equals(vacationTypeId, that.vacationTypeId);
    }
}
