package com.nals.hrm.service;

import com.nals.hrm.dto.HolidaysDTO;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

public interface HolidayService {

    Response<HolidaysDTO> findByHoliday(Pageable page, String year) throws ResourceNotFoundException;

    Response<HolidaysDTO> save(HolidaysDTO holidayDTO, BindingResult bindingResult) throws Exception;

    Response<HolidaysDTO> findHolidayById(Integer id) throws ResourceNotFoundException;

    Response<HolidaysDTO> updateHoliday(Integer id, HolidaysDTO holidayDTO, BindingResult bindingResult)
            throws ViolatedException,ResourceNotFoundException, ConflictException;

    void deleteHoliday(Integer id) throws UnprocessableEntityException;
}
