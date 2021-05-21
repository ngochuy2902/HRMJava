package com.nals.hrm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RequestDevices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime deletedAt;
    private Integer updatedBy;
    private String projectName;
    private String deviceName;
    private String technicalSpecification;
    private Integer numberDevice;
    private String reason;
    private Integer status;
    private Integer assignedBy;
    private LocalDateTime assignedAt;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate expectedDeliveryDate;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    private Users user;
}
