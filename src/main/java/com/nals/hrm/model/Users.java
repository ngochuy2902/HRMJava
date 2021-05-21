package com.nals.hrm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime deletedAt;
    private Integer updatedBy;
    private Boolean active;
    private String email;
    private LocalDateTime emailVerifiedAt;
    private String password;
    private String rememberToken;
    private String fullName;
    private String phoneNumber;
    private String project;
    private LocalDateTime startDate;
    private int gender;
    private Date birthday;
    private String employeeCode;
    private double remainingDaysOff;
    private String avatar;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
