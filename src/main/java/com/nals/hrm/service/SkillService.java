package com.nals.hrm.service;

import com.nals.hrm.dto.SkillsDTO;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Response;
import org.springframework.validation.BindingResult;

public interface SkillService {
    Response<SkillsDTO> save(SkillsDTO skillDTO, BindingResult bindingResult)
            throws ViolatedException, ConflictException;

    void delete(Integer id) throws UnprocessableEntityException;

    Response<SkillsDTO> findBySkill(int page, int sizePage, String name, String direct, String sort)
            throws ResourceNotFoundException;

    Response<SkillsDTO> findById(Integer id) throws ResourceNotFoundException;

    Response<SkillsDTO> updateSkill(Integer id, SkillsDTO skillsDTO, BindingResult bindingResult)
            throws ViolatedException, ResourceNotFoundException, ConflictException, UnprocessableEntityException;
}
