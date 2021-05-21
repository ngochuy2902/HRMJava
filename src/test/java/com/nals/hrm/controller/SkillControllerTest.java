package com.nals.hrm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nals.hrm.dto.SkillsDTO;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.model.Response;
import com.nals.hrm.model.Skills;
import com.nals.hrm.repository.SkillRepository;
import com.nals.hrm.service.ResponseService;
import com.nals.hrm.service.SkillService;
import com.nals.hrm.service.impl.ResponseServiceImpl;
import com.nals.hrm.service.impl.SkillServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SkillController.class)
class SkillControllerTest {

    @MockBean
    SkillRepository skillRepository;
    @MockBean
    private SkillService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void onSetUp() throws ResourceNotFoundException {
        List<Skills> allSkills = preparedListSkills();
        Skills skills = allSkills.get(2);
        ModelMapper modelMapper = new ModelMapper();
        ResponseService responseService = new ResponseServiceImpl(modelMapper);
        SkillService skillService = new SkillServiceImpl(skillRepository, responseService, modelMapper);
        Sort sortDesc = Sort.by("name").descending();
        Pageable pageableSortDesc = PageRequest.of(0, 10, sortDesc);
        Page<Skills> skillsPageDesc = new PageImpl<>(allSkills, pageableSortDesc, allSkills.size());
        Mockito.when(skillRepository.findAllByNameContainingAndDeletedAtNull(pageableSortDesc, "")).thenReturn(skillsPageDesc);
        Response<SkillsDTO> skillsDTOResponse = skillService.findBySkill(1, 10, "", "desc", "name");
        Mockito.when(service.findBySkill(1, 10, "", "desc", "name")).thenReturn(skillsDTOResponse);
        Mockito.when(skillRepository.findByIdAndDeletedAtNull(1)).thenReturn(skills);
        Response<SkillsDTO> skillDTODetail = skillService.findById(1);
        Mockito.when(service.findById(1)).thenReturn(skillDTODetail);
    }

    @Test
    public void givenSkills_whenGetSkillsList_thenReturnStatusSuccess() throws Exception {
        String url = "http://localhost:8080/api/skills";
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)));
    }

    @Test
    public void givenSkills_whenGetSkillById_thenReturnStatusSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skills/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").exists())
                .andExpect(jsonPath("$.data.name").value("java"))
                .andReturn();
    }

    @Test
    public void givenSkill_whenUpdateSkill_thenReturnStatusSuccess() throws Exception {
        String url = "http://localhost:8080/api/skills/{id}";
        SkillsDTO skillsDTO = new SkillsDTO();
        skillsDTO.setName("java");
        String inputJson = objectMapper.writeValueAsString(skillsDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(url,1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(status().isOk())
                .andReturn();
    }

    private List<Skills> preparedListSkills() {
        Skills java = new Skills();
        java.setId(1);
        java.setName("java");
        java.setUpdatedAt(LocalDateTime.now());
        Skills php = new Skills();
        php.setName("php");
        php.setUpdatedAt(LocalDateTime.now());
        Skills react = new Skills();
        react.setName("react");
        react.setUpdatedAt(LocalDateTime.now());
        return Arrays.asList(react, php, java);
    }

    @Test
    void giveSkills_whenValidInput_thenReturns201() throws Exception {
        SkillsDTO skillsDTO = new SkillsDTO();
        skillsDTO.setName("java");
        mockMvc.perform(post("/api/skills")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(skillsDTO)))
                .andExpect(status().isCreated());
    }
}
