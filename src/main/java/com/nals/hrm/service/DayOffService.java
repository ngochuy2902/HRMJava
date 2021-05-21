package com.nals.hrm.service;

import com.nals.hrm.dto.DaysOffDTO;
import com.nals.hrm.exception.ConflictDayOffDataException;
import com.nals.hrm.exception.ResourceNotFoundException;
import com.nals.hrm.exception.UnprocessableEntityException;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Response;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import java.time.LocalDate;

public interface DayOffService {

    Response<DaysOffDTO> findAllByDayOff(int page, String fullName, LocalDate dateOffFrom, LocalDate dateOffTo,String sort,String direct,int pageSize, Integer userId) throws ResourceNotFoundException;
    Response<DaysOffDTO> update(Integer id, DaysOffDTO daysOffDTO, BindingResult bindingResult) throws ViolatedException, UnprocessableEntityException, MessagingException, ConflictDayOffDataException;
    void delete(Integer id) throws UnprocessableEntityException;
    Response<DaysOffDTO> save(DaysOffDTO daysOffDTO, BindingResult bindingResult) throws ViolatedException, MessagingException, ConflictDayOffDataException;
    Response<DaysOffDTO> findById(Integer id) throws ResourceNotFoundException;
}
