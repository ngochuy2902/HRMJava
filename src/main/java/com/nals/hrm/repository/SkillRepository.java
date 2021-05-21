package com.nals.hrm.repository;

import com.nals.hrm.model.Skills;
import com.nals.hrm.service.SkillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skills, Integer> {

    Page<Skills> findAllByNameContainingAndDeletedAtNull(Pageable page, String name);

    Skills findByIdAndDeletedAtNull(Integer id);

    Skills findByNameAndDeletedAtNull(String name);
}
