package com.nals.hrm.service;

import com.nals.hrm.dto.SkillsDTO;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Response;
import com.nals.hrm.model.Skills;
import com.nals.hrm.repository.SkillRepository;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
class SkillServiceTest {

    @MockBean
    SkillRepository skillRepository;

    @Autowired
    private SkillService skillService;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private BindingResult bindingResult;

    @BeforeEach
    public void onSetUp() {
        String skillName = "python";
        Skills skillResult = new Skills();
        skillResult.setId(1);
        skillResult.setName(skillName);
        skillResult.setActive(true);
        skillResult.setCreatedAt(LocalDateTime.now());
        skillResult.setUpdatedAt(LocalDateTime.now());

        List<Skills> allSkills = preparedListSkills();
        List<Skills> allSkillsRevert = preparedListSkills();
        Skills skills = allSkills.get(2);
        Collections.reverse(allSkillsRevert);
        List<Skills> searchSkill = new ArrayList<>();
        searchSkill.add(allSkills.get(2));
        Sort sortDesc = Sort.by("name").descending();
        Sort sortAsc = Sort.by("name").ascending();
        Pageable pageableSortDesc = PageRequest.of(0, 10, sortDesc);
        Pageable pageableSortAsc = PageRequest.of(0, 10, sortAsc);
        Page<Skills> skillsPageDesc = new PageImpl<>(allSkills, pageableSortDesc, allSkills.size());
        Page<Skills> skillsPageAsc = new PageImpl<>(allSkillsRevert, pageableSortAsc, allSkillsRevert.size());
        Page<Skills> skillsPageSearch = new PageImpl<>(searchSkill, pageableSortDesc, searchSkill.size());
        Mockito.when(skillRepository.findAllByNameContainingAndDeletedAtNull(pageableSortDesc, "")).thenReturn(skillsPageDesc);
        Mockito.when(skillRepository.findAllByNameContainingAndDeletedAtNull(pageableSortAsc, "")).thenReturn(skillsPageAsc);
        Mockito.when(skillRepository.findAllByNameContainingAndDeletedAtNull(pageableSortAsc, "java")).thenReturn(skillsPageSearch);
        Mockito.when(skillRepository.findByIdAndDeletedAtNull(1)).thenReturn(skills);

        Mockito.when(skillRepository.findByNameAndDeletedAtNull("java")).thenReturn(null);
        Mockito.when(skillRepository.findByNameAndDeletedAtNull("php")).thenReturn(allSkills.get(1));
        //Mockito.when(skillRepository.save(skills)).thenReturn(skills);

        Mockito.when(skillRepository.save(any(Skills.class))).thenReturn(skillResult);
    }

    @BeforeEach
    public void onCreateSetup() {
        String skillName = "java";
        Skills skillResult = new Skills();
        skillResult.setId(1);
        skillResult.setName(skillName);
        skillResult.setActive(true);
        skillResult.setCreatedAt(LocalDateTime.now());
        skillResult.setUpdatedAt(LocalDateTime.now());

        Mockito.when(skillRepository.findByNameAndDeletedAtNull(skillName)).thenReturn(skillResult);
    }

    @Test
    public void given3Skills_whenGetAllSkillSortDefaultAndNotSearch_thenReturn3Record() throws ResourceNotFoundException {
        Response<SkillsDTO> skillsDTOResponse = skillService.findBySkill(1, 10, "", "desc", "name");
        List<SkillsDTO> skillsDTOList = (List<SkillsDTO>) skillsDTOResponse.getData();
        Assert.assertEquals(skillsDTOList.size(), 3);
    }

    @Test
    public void givenSkills_whenGetAllSkillSortAscAndNotSearch_thenReturnSkillsListSortAsc()
            throws ResourceNotFoundException {
        List<SkillsDTO> skillsDTOSortAsc = preparedListSkillsSortAsc();
        Response<SkillsDTO> skillsDTOResponse = skillService.findBySkill(1, 10, "", "asc", "name");
        List<SkillsDTO> skillsDTOList = (List<SkillsDTO>) skillsDTOResponse.getData();
        for (int i = 0; i < skillsDTOList.size() - 1; i++) {
            Assert.assertEquals(skillsDTOList.get(i).getName(), skillsDTOSortAsc.get(i).getName());
        }
    }

    @Test
    public void givenSkills_whenGetAllSkillSortDefaultAndSearchBySkillName_thenReturnSkillListSearchBySkillName()
            throws ResourceNotFoundException {
        String skillName = "java";
        Response<SkillsDTO> skillsDTOResponse = skillService.findBySkill(1, 10, "java", "asc", "name");
        List<SkillsDTO> skillsDTOList = (List<SkillsDTO>) skillsDTOResponse.getData();
        Assert.assertEquals(skillsDTOList.get(0).getName(), skillName);
    }

    @Test
    public void givenSkills_whenGetSkillById_thenReturnSkillDetail() throws ResourceNotFoundException {
        SkillsDTO skillsDTO = preparedSkillDTO();
        Response<SkillsDTO> skillsDTOResponse = skillService.findById(1);
        SkillsDTO skill = (SkillsDTO) skillsDTOResponse.getData();
        Assert.assertEquals(skill.getId(),skillsDTO.getId());
        Assert.assertEquals(skill.getName(),skillsDTO.getName());
        Assert.assertEquals(skill.getUpdatedAt(),skillsDTO.getUpdatedAt());
    }

    @Test
    public void givenSkill_whenGetSkillByIdNotFound_thenReturnResourceNotFoundExceptionIsThrow() {
            ResourceNotFoundException notFoundException = null;
            try{
                skillService.findById(0);
            }catch (ResourceNotFoundException e){
                notFoundException = e;
            }
            Assert.assertNotNull(notFoundException);
    }

    @Test
    public void giveSkill_whenUpdateSkill_thenReturnSkillUpdated() throws ViolatedException, ResourceNotFoundException, UnprocessableEntityException, ConflictException {
        SkillsDTO skillsDTO = new SkillsDTO(1,"python");
        Response<SkillsDTO> skillsDTOResponse = skillService.updateSkill(1, skillsDTO, bindingResult);
        SkillsDTO skillsDTOUpdated = (SkillsDTO) skillsDTOResponse.getData();
        Assert.assertEquals(skillsDTO.getName(), skillsDTOUpdated.getName());
    }

    @Test
    public void giveSkill_whenUpdateSkillIdNotFound_thenReturnUnprocessableEntityException() throws ViolatedException, ResourceNotFoundException, ConflictException {
        UnprocessableEntityException unprocessableEntityException = null;
        SkillsDTO skillsDTO = preparedSkillDTO();
        try {
            skillService.updateSkill(0, skillsDTO, bindingResult);
        } catch (UnprocessableEntityException e) {
            unprocessableEntityException = e;
        }
        Assert.assertNotNull(unprocessableEntityException);
    }

    @SneakyThrows
    @Test
    public void giveSkill_whenUpdateSkillWithNameExist_thenReturnConflictException(){
        ConflictException conflictException = null;
        SkillsDTO skillsDTO = new SkillsDTO();
        skillsDTO.setName("php");
        try {
            skillService.updateSkill(1, skillsDTO, bindingResult);
        } catch (ConflictException e) {
            conflictException = e;
        }
        Assert.assertNotNull(conflictException);
    }

//    @SneakyThrows
//    @Test
//    public void giveSkill_whenUpdateSkillWithNameNull_thenReturnViolatedException(){
//        ViolatedException violatedException = null;
//        SkillsDTO skillsDTO = preparedSkillDTO();
//        skillsDTO.setName("");
//        try {
//            skillService.updateSkill(1, skillsDTO, bindingResult);
//        } catch (ViolatedException e) {
//            violatedException = e;
//        }
//        Assert.assertNotNull(violatedException);
//    }

    @Test
    public void givenSkills_whenCreateSkills_thenReturnSkills() throws ConflictException, ViolatedException {
        SkillsDTO skillsDTO = new SkillsDTO(1,"python");
        Response<SkillsDTO> skillsDTOResponse = skillService.save(skillsDTO, bindingResult);
        SkillsDTO skillsDTOCreated = (SkillsDTO) skillsDTOResponse.getData();
        Assert.assertEquals(skillsDTOCreated.getName(), skillsDTO.getName());
    }

    @Test
    public void givenSkills_whenCreateSkills_thenReturnConflictError() throws ViolatedException {
        SkillsDTO skillsDTO = new SkillsDTO(1,"java");
        ConflictException conflictException = null;
        try {
            skillService.save(skillsDTO, bindingResult);
        } catch (ConflictException e) {
            conflictException = e;
        }
        Assert.assertEquals("Record already exists", conflictException.getMessage());
    }

    private SkillsDTO preparedSkillDTO(){
        SkillsDTO skillsDTO = new SkillsDTO();
        skillsDTO.setId(1);
        skillsDTO.setName("java");
        skillsDTO.setUpdatedAt(LocalDateTime.parse("2020-11-20T11:17:55.398"));
        return skillsDTO;
    }

    private List<SkillsDTO> preparedListSkillsSortAsc() {
        List<Skills> skillsList = preparedListSkills();
        List<SkillsDTO> skillsDTOList = modelMapper.map(skillsList, new TypeToken<List<SkillsDTO>>() {
        }.getType());
        List<SkillsDTO> skillsListSort = new ArrayList<>();
        int lengthSkillsList = skillsDTOList.size() - 1;
        for (int i = 0; i < lengthSkillsList; i++) {
            skillsListSort.add(skillsDTOList.get(lengthSkillsList - i));
        }
        return skillsListSort;
    }

    private List<Skills> preparedListSkills() {
        Skills java = new Skills();
        java.setId(1);
        java.setName("java");
        java.setUpdatedAt(LocalDateTime.parse("2020-11-20T11:17:55.398"));
        Skills php = new Skills();
        php.setId(2);
        php.setName("php");
        php.setUpdatedAt(LocalDateTime.now());
        Skills react = new Skills();
        react.setName("react");
        react.setUpdatedAt(LocalDateTime.now());
        return Arrays.asList(react, php, java);
    }
}
