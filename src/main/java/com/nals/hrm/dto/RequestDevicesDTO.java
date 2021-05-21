package com.nals.hrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.*;
import java.lang.annotation.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RequestDevicesDTO{

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("technical_specification")
    private String technicalSpecification;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty(value = "assigned_by",access = JsonProperty.Access.WRITE_ONLY)
    private Integer assignedBy;

    @JsonProperty("device_name")
    @NotBlank(message = "device_name must not be blank")
    private String deviceName;

    @JsonProperty("expected_delivery_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "expected_delivery date must not be null")
    private LocalDate expectedDeliveryDate;

    @JsonProperty("number_device")
    @NumberFormatValidator
    @NotNull(message = "number_device must not be null")
    @Min(value = 1, message = "number_device must be greater than or equal to 1")
    private Integer numberDevice;

    @JsonProperty("email")
    private String email;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("user_id")
    @NotNull(message = "user_id must not be null")
    private Integer userId;

    @Documented
    @Min(value=0, message = "add a min msg" )
    @Digits(fraction = 0, integer = 10, message ="add a digit msg")
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = {})
    @ReportAsSingleViolation

    public @interface NumberFormatValidator {

        String message() default "invalid number";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

    }
}
