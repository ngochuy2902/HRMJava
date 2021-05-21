package com.nals.hrm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nals.hrm.model.UserRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DevicesDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty(value = "updated_by", access = JsonProperty.Access.WRITE_ONLY)
    private Integer updatedBy;

    @JsonProperty("device_name")
    @NotBlank(message = "device name must not be blank")
    private String deviceName;

    @JsonProperty("serial_number")
    @NotBlank(message = "serial number must not be blank")
    private String serialNumber;

    @JsonProperty("input_date")
    @NotBlank(message = "date must not be blank")
    private LocalDate inputDate;

    @JsonProperty("status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean status;

    @JsonProperty(value = "user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;

    @JsonProperty(value = "user_response", access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserRes userRes;
}
