package com.nals.hrm.service.impl;

import com.nals.hrm.dto.SkillsDTO;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Response;
import com.nals.hrm.model.Skills;
import com.nals.hrm.repository.SkillRepository;
import com.nals.hrm.service.ResponseService;
import com.nals.hrm.service.SkillService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final ResponseService responseService;
    private final ModelMapper modelMapper;

    @Autowired
    public SkillServiceImpl(SkillRepository skillRepository, ResponseService responseService, ModelMapper modelMapper) {
        this.skillRepository = skillRepository;
        this.responseService = responseService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response<SkillsDTO> save(SkillsDTO skillsDTO, BindingResult bindingResult) throws ViolatedException, ConflictException {
        if (bindingResult.hasErrors()) {
            throw new ViolatedException(bindingResult);
        }
        if (skillsDTO.getId() != null) {
            skillsDTO.setId(null);
        }
        Skills skill = skillRepository.findByNameAndDeletedAtNull(skillsDTO.getName());
        if(skill != null) {
            HashMap<String, String> causeList = new HashMap<>();
            causeList.put("name", skillsDTO.getName());
            throw new ConflictException("Record already exists", causeList);
        }
        skill = modelMapper.map(skillsDTO, Skills.class);
        skill.setActive(true);
        skill = skillRepository.save(skill);
        return responseService.dataResponseOne(skill, HttpStatus.CREATED.getReasonPhrase(), SkillsDTO.class);
    }

    @Override
    public void delete(Integer id) throws UnprocessableEntityException {
        Skills skill = skillRepository.findByIdAndDeletedAtNull(id);
        if (skill == null) {
            throw new UnprocessableEntityException("Record does not exist", id);
        }
        skill.setActive(false);
        skill.setDeletedAt(LocalDateTime.now());
        skillRepository.save(skill);
    }

    @Override
    public Response<SkillsDTO> updateSkill(Integer id, SkillsDTO skillsDTO, BindingResult bindingResult) throws ViolatedException, ConflictException, UnprocessableEntityException {
        if (bindingResult.hasErrors()) {
            throw new ViolatedException(bindingResult);
        }
        Skills skills = skillRepository.findByIdAndDeletedAtNull(id);
        if (skills == null) {
            throw new UnprocessableEntityException("Record does not exist", id);
        }
        Skills skillsName = skillRepository.findByNameAndDeletedAtNull(skillsDTO.getName());
        if (skillsName != null && !skillsName.getId().equals(skills.getId()) && !skillsName.getName().toLowerCase().equals(skills.getName().toLowerCase())) {
            HashMap<String, String> causeList = new HashMap<>();
            causeList.put("name", skillsDTO.getName());
            throw new ConflictException("Record already exists", causeList);
        }
        skillsDTO.setId(skills.getId());

        modelMapper.map(skillsDTO, skills);
        skillRepository.save(skills);
        return responseService.dataResponseOne(skills, HttpStatus.OK.getReasonPhrase(), SkillsDTO.class);
    }

    @Override
    public Response<SkillsDTO> findById(Integer id) throws ResourceNotFoundException {
        Skills skills = skillRepository.findByIdAndDeletedAtNull(id);
        if (skills == null) {
            throw new ResourceNotFoundException("Record does not exist", id);
        }
        return responseService.dataResponseOne(skills, HttpStatus.OK.getReasonPhrase(), SkillsDTO.class);
    }

    @Override
    public Response<SkillsDTO> findBySkill(int page, int sizePage, String name, String direct, String sort) {
        Pageable paging = getPagination(page, sizePage, direct, sort);
        Page<Skills> skills = skillRepository.findAllByNameContainingAndDeletedAtNull(paging, name);
        return responseService.dataResponse(skills, HttpStatus.OK.getReasonPhrase(), SkillsDTO.class);
    }

    public Pageable getPagination(int page, int sizePage, String direct, String sort) {
        Sort sortList = Sort.by(sort).descending();
        if (direct.equals("asc")) {
            sortList = Sort.by(sort).ascending();
        }
        return PageRequest.of(--page, sizePage, sortList);
    }
}
