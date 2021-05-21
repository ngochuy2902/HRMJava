package com.nals.hrm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nals.hrm.dto.DaysOffDTO;
import com.nals.hrm.dto.SkillsDTO;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.model.DaysOff;
import com.nals.hrm.model.DaysOffList;
import com.nals.hrm.repository.DayOffRepository;
import com.nals.hrm.service.DayOffService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@WebMvcTest(DayOffController.class)
class DayOffControllerTest {

    @MockBean
    private DayOffService dayOffService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void onSetUp() throws UnprocessableEntityException {
        Integer id = 2;

        Mockito.doNothing().when(dayOffService).delete(any(Integer.class));
    }

    @Test
    void giveDaysOff_whenCreateDaysOffValid_returnStatus200() throws Exception {
        DaysOffDTO daysOffDTO = prepareDaysOffDTO();
        mockMvc.perform(post("/api/days-off")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(daysOffDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void givenDaysOff_whenDeleteDaysOffWithIdExist_return204() throws Exception {
        String url = "/api/days-off/{id}";
        mockMvc.perform(MockMvcRequestBuilders.delete(url,2))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
    }

    public DaysOff prepareDaysOff() {
        DaysOff daysOff = new DaysOff();
        daysOff.setId(2);
        daysOff.setPoName("Giang Truong");
        daysOff.setPoEmail("huyhn@nal.vn");
        daysOff.setVacationDay(LocalDate.parse("06/12/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        daysOff.setSessionDayOff(2);
        daysOff.setReasons("flu");
        daysOff.setNotes("abc");
        daysOff.setActive(false);
        daysOff.setDeletedAt(LocalDateTime.now());
        return  daysOff;
    }

    public DaysOffDTO prepareDaysOffDTO() {
        DaysOffDTO daysOffDTO = new DaysOffDTO();
        daysOffDTO.setPoName("PO Name");
        daysOffDTO.setPoEmail("po@nal.vn");
        DaysOffList daysOffList = new DaysOffList();
        daysOffList.setVacationDay(LocalDate.parse("06/12/2020", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        daysOffList.setSessionDayOffId(1);
        daysOffList.setVacationTypeId(2);
        List<DaysOffList> daysOffLists = new ArrayList<>();
        daysOffLists.add(daysOffList);
        daysOffDTO.setDaysOffLists(daysOffLists);
        daysOffDTO.setReasons("full");
        daysOffDTO.setNotes("abc");
        daysOffDTO.setUserId(1);
        return daysOffDTO;
    }
}