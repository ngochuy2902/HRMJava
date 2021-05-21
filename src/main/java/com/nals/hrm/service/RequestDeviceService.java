package com.nals.hrm.service;

import com.nals.hrm.dto.RequestDevicesDTO;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.RequestDevices;
import com.nals.hrm.model.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;

public interface RequestDeviceService {

    Response<RequestDevicesDTO> save(RequestDevicesDTO requestDeviceDTO, BindingResult bindingResult) throws ViolatedException, ConflictException;

    void delete(Integer id) throws UnprocessableEntityException;

    Response<RequestDevicesDTO> findByRequestDevice(Pageable page, String fullName, String deviceName, List<Integer> status, LocalDate fromDate, LocalDate toDate) throws ResourceNotFoundException;

    Response<RequestDevicesDTO> findById(Integer id) throws ResourceNotFoundException;

    Response<RequestDevicesDTO> update(RequestDevicesDTO requestDeviceDTO, Integer id, BindingResult bindingResult) throws ViolatedException, ConflictException, UnprocessableEntityException;

    RequestDevices findByIdAndDeletedAtNull(Integer id);
}
