package com.nals.hrm.controller;

import com.nals.hrm.dto.EmailContentDTO;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Response;
import com.nals.hrm.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class SendEmailController {

    private final SendEmailService sendEmailService;

    @Autowired
    public SendEmailController(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
    }

    @PostMapping("/send-email")
    public ResponseEntity<Response<EmailContentDTO>> sendEmail(@Validated  @RequestBody EmailContentDTO emailContentDTO, BindingResult bindingResult) throws ViolatedException {
        Response<EmailContentDTO> emailContentDTOResponse = sendEmailService.sendEmail(emailContentDTO, bindingResult);
        return new ResponseEntity<>(emailContentDTOResponse, HttpStatus.OK);
    }
}
