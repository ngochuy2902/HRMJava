package com.nals.hrm.service;

import com.nals.hrm.dto.VacationTypesDTO;
import com.nals.hrm.model.Response;

public interface VacationTypeService {

    Response<VacationTypesDTO> findAllVacationType();
}
