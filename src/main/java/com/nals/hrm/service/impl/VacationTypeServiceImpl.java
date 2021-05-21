package com.nals.hrm.service.impl;

import com.nals.hrm.dto.VacationTypesDTO;
import com.nals.hrm.model.Response;
import com.nals.hrm.model.VacationTypes;
import com.nals.hrm.repository.VacationTypeRepository;
import com.nals.hrm.service.ResponseService;
import com.nals.hrm.service.VacationTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VacationTypeServiceImpl implements VacationTypeService {

    private final VacationTypeRepository vacationTypeRepository;
    private final ResponseService responseService;

    public VacationTypeServiceImpl(VacationTypeRepository vacationTypeRepository, ResponseService responseService) {
        this.vacationTypeRepository = vacationTypeRepository;
        this.responseService = responseService;
    }

    @Override
    public Response<VacationTypesDTO> findAllVacationType() {
        List<VacationTypes> vacationTypes = vacationTypeRepository.findAllByDeletedAtNull();
        return responseService.dataResponseList(vacationTypes, HttpStatus.OK.getReasonPhrase(), VacationTypesDTO.class);
    }
}
