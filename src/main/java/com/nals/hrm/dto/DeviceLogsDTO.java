package com.nals.hrm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DeviceLogsDTO {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;

    @JsonProperty("updated_by")
    private Integer updatedBy;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("device_name")
    private String deviceName;

    @JsonProperty("serial_number")
    private String serialNumber;

    @JsonProperty("input_date")
    private LocalDateTime inputDate;

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("device_id")
    private Integer device;
}
