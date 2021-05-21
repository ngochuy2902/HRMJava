package com.nals.hrm.controller;

import com.nals.hrm.dto.DaysOffDTO;
import com.nals.hrm.dto.SkillsDTO;
import com.nals.hrm.exception.*;
import com.nals.hrm.model.DaysOff;
import com.nals.hrm.model.Response;
import com.nals.hrm.service.DayOffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDate;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class DayOffController {

    private final DayOffService dayOffService;

    @Autowired
    public DayOffController(DayOffService dayOffService) {
        this.dayOffService = dayOffService;
    }

    @PostMapping("/days-off")
    public ResponseEntity<Response<DaysOffDTO>> createDayOff(@Validated @RequestBody DaysOffDTO daysOffDTO, BindingResult bindingResult)
            throws ViolatedException, MessagingException, ConflictDayOffDataException {
        Response<DaysOffDTO> daysOffDTOResponse = dayOffService.save(daysOffDTO, bindingResult);
        return new ResponseEntity<>(daysOffDTOResponse, HttpStatus.CREATED);
    }

    @GetMapping("/days-off")
    public ResponseEntity<Response<DaysOffDTO>> getDaysOffList(@RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(name = "full_name", defaultValue = "") String fullName,
                                                               @RequestParam(name = "date_off_from", defaultValue = "") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateOffFrom,
                                                               @RequestParam(name = "date_off_to", defaultValue = "") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateOffTo,
                                                               @RequestParam(defaultValue = "createdAt") String sort,
                                                               @RequestParam(defaultValue = "") String direct,
                                                               @RequestParam(name = "page_size", defaultValue = "10") int pageSize,
                                                               @RequestParam(name = "user_id", defaultValue = "")  Integer userId) throws ResourceNotFoundException {
        Response<DaysOffDTO> daysOffDTOResponse = dayOffService.findAllByDayOff(page, fullName, dateOffFrom, dateOffTo, sort, direct, pageSize, userId);
        return new ResponseEntity<>(daysOffDTOResponse, HttpStatus.OK);
    }

    @PutMapping("/days-off/{id}")
    public ResponseEntity<Response<DaysOffDTO>> editDayOff(@PathVariable("id") Integer id, @Validated @RequestBody DaysOffDTO daysOffDTO, BindingResult bindingResult)
            throws UnprocessableEntityException, ViolatedException, MessagingException, ConflictDayOffDataException {
        Response<DaysOffDTO> daysOffDTOResponse = dayOffService.update(id,daysOffDTO,bindingResult);
        return new ResponseEntity<>(daysOffDTOResponse,HttpStatus.OK);
    }

    @DeleteMapping("/days-off/{id}")
    public ResponseEntity<Response<DaysOffDTO>> removeDayOff(@PathVariable("id") Integer id) throws UnprocessableEntityException {
        dayOffService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/days-off/{id}")
    public ResponseEntity<Response<DaysOffDTO>> GetDetailDayOff(@PathVariable("id") Integer id) throws ResourceNotFoundException {
        Response<DaysOffDTO> daysOffDTOResponse = dayOffService.findById(id);
        return new ResponseEntity<>(daysOffDTOResponse, HttpStatus.OK);
    }
}
