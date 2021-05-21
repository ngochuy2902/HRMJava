package com.nals.hrm.service.impl;

import com.nals.hrm.dto.HolidaysDTO;
import com.nals.hrm.exception.ConflictException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Holidays;
import com.nals.hrm.model.Response;
import com.nals.hrm.repository.HolidayRepository;
import com.nals.hrm.service.HolidayService;
import com.nals.hrm.service.ResponseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;
    private final ResponseService responseService;
    private final ModelMapper modelMapper;

    @Autowired
    public HolidayServiceImpl(HolidayRepository holidayRepository, ResponseService responseService, ModelMapper modelMapper) {
        this.holidayRepository = holidayRepository;
        this.responseService = responseService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response<HolidaysDTO> findByHoliday(Pageable page, String year) throws ResourceNotFoundException {
        Page<Holidays> holidays = holidayRepository.findHolidayByYear(page, year);
        return responseService.dataResponse(holidays, HttpStatus.OK.getReasonPhrase(), HolidaysDTO.class);
    }

    @Override
    public Response<HolidaysDTO> save(HolidaysDTO holidayDTO, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new ViolatedException(bindingResult);
        }
        if (holidayDTO.getId() != null) {
            holidayDTO.setId(null);
        }
        Boolean checkExistDate = holidayRepository.existsByDateAndDeletedAtNull(holidayDTO.getDate());
        if (checkExistDate) {
            throw new ConflictException("Record already exists", new HashMap<>());
        }
        Holidays holidays = modelMapper.map(holidayDTO, Holidays.class);
        Holidays holidayResponse = holidayRepository.save(holidays);
        return responseService.dataResponseOne(holidayResponse, HttpStatus.OK.getReasonPhrase(), HolidaysDTO.class);
    }

    @Override
    public Response<HolidaysDTO> findHolidayById(Integer id) throws ResourceNotFoundException {
        Holidays holidays = holidayRepository.findByIdAndDeletedAtNull(id);
        if (holidays == null) {
            throw new ResourceNotFoundException("Holiday does not exist", id);
        }
        return responseService.dataResponseOne(holidays, HttpStatus.OK.getReasonPhrase(), HolidaysDTO.class);
    }

    @Override
    public Response<HolidaysDTO> updateHoliday(Integer id, HolidaysDTO holidayDTO, BindingResult bindingResult)
            throws ViolatedException, ResourceNotFoundException, ConflictException {
        if (bindingResult.hasErrors()) {
            throw new ViolatedException(bindingResult);
        }
        Holidays holiday = holidayRepository.findByIdAndDeletedAtNull(id);
        if (holiday == null) {
            throw new ResourceNotFoundException("Holiday does not exist", id);
        }
        LocalDate date = holidayDTO.getDate();
        Boolean checkExistDate = holidayRepository.existsByDateAndDeletedAtNull(date);
        Boolean checkUpdate = holidayRepository.existsByDateAndIdAndDeletedAtNull(date, id);
        if (checkExistDate && !checkUpdate) {
            throw new ConflictException("Record already exists", new HashMap<>());
        }
        holidayDTO.setId(id);
        modelMapper.map(holidayDTO, holiday);
        holidayRepository.save(holiday);
        return responseService.dataResponseOne(holiday, HttpStatus.OK.getReasonPhrase(), HolidaysDTO.class);
    }

    @Override
    public void deleteHoliday(Integer id) throws UnprocessableEntityException {
        Holidays holidays = holidayRepository.findByIdAndDeletedAtNull(id);
        if (holidays == null) {
            throw new UnprocessableEntityException("Holiday does not exist", id);
        }
        holidays.setDeletedAt(LocalDateTime.now());
        holidayRepository.save(holidays);
    }
}
