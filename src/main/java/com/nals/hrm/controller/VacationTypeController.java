package com.nals.hrm.controller;

import com.nals.hrm.dto.VacationTypesDTO;
import com.nals.hrm.model.Response;
import com.nals.hrm.service.VacationTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class VacationTypeController {

    private final VacationTypeService vacationTypeService;

    public VacationTypeController(VacationTypeService vacationTypeService) {
        this.vacationTypeService = vacationTypeService;
    }

    @GetMapping("/vacation-type")
    public ResponseEntity<Response<VacationTypesDTO>> getVacationTypeList() {
        Response<VacationTypesDTO> vacationTypesDTOResponse = vacationTypeService.findAllVacationType();
        return new ResponseEntity<>(vacationTypesDTOResponse, HttpStatus.OK);
    }
}
