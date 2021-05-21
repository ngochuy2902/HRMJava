package com.nals.hrm.repository;

import com.nals.hrm.model.DaysOff;
import com.nals.hrm.model.Users;
import com.nals.hrm.model.VacationTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DayOffRepository extends JpaRepository<DaysOff, Integer>, JpaSpecificationExecutor<DaysOff> {

    DaysOff findByIdAndDeletedAtNull(Integer id);
    DaysOff findByUserAndVacationDayAndSessionDayOffAndDeletedAtNull(Users users, LocalDate vacationDay, Integer sessionDayOff);
}
