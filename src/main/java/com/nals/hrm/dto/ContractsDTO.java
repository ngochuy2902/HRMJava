package com.nals.hrm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ContractsDTO {

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

    @JsonProperty("contract_date_begin")
    private LocalDateTime contractDateBegin;

    @JsonProperty("contract_date_end")
    private LocalDateTime contractDateEnd;

    @JsonProperty("contract_date_end")
    private int contractStatus;

    @JsonProperty("user_id")
    private Integer userId;
}
