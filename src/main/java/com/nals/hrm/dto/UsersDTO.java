package com.nals.hrm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UsersDTO {

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

    @JsonProperty("email")
    private String email;

    @JsonProperty("email_verified_at")
    private LocalDateTime emailVerifiedAt;

    @JsonProperty("password")
    private String password;

    @JsonProperty("remember_token")
    private String rememberToken;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("project")
    private String project;

    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @JsonProperty("gender")
    private Integer gender;

    @JsonProperty("birthday")
    private Date birthday;

    @JsonProperty("employee_code")
    private String employeeCode;

    @JsonProperty("remaining_days_off")
    private Boolean remainingDaysOff;

    @JsonProperty("avatar")
    private String avatar;
}
