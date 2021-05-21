package com.nals.hrm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DaysOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime deletedAt;
    private Integer updatedBy;
    private String poName;
    private String poEmail;
    private LocalDate vacationDay;
    private Integer sessionDayOff;
    private String reasons;
    private String notes;
    private Boolean active;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    private VacationTypes vacationType;

    @ManyToOne
    private Users user;
}
