package com.nals.hrm.repository;

import com.nals.hrm.model.VacationTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacationTypeRepository extends JpaRepository<VacationTypes, Integer> {
    VacationTypes findByIdAndDeletedAtNull(Integer id);

    List<VacationTypes> findAllByDeletedAtNull();
}
