package com.nals.hrm.service;

import com.nals.hrm.dto.DevicesDTO;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;


public interface DeviceService {

    Response<DevicesDTO> getAllDevice(Pageable page, String name, String holder, Boolean status,
                                      LocalDate dateForm, LocalDate dateTo, String sort)
            throws ResourceNotFoundException;

    Response<DevicesDTO> getHolidayById(Integer id) throws ResourceNotFoundException;

    Response<DevicesDTO> save(DevicesDTO devicesDTO, BindingResult bindingResult) throws Exception;

    Response<DevicesDTO> updateDevice(Integer id, DevicesDTO deviceDTO, BindingResult bindingResult)
            throws ViolatedException, ResourceNotFoundException, ConflictException;

    void deleteDevice(Integer id) throws UnprocessableEntityException;
}
