package com.nals.hrm.repository;

import com.nals.hrm.model.Holidays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface HolidayRepository extends JpaRepository<Holidays, Integer> {

    @Query(value = "SELECT * FROM hrm.holidays p WHERE p.date Like %:year% and deleted_at is null", nativeQuery = true)
    Page<Holidays> findHolidayByYear(Pageable page, @Param("year") String year);

    Holidays findByIdAndDeletedAtNull(Integer id);

    Boolean existsByDateAndDeletedAtNull(LocalDate date);

    Boolean  existsByDateAndIdAndDeletedAtNull(LocalDate date,Integer id);
}
