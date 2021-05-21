package com.nals.hrm.service;

import com.nals.hrm.dto.EmailContentDTO;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.Response;
import org.springframework.validation.BindingResult;

public interface SendEmailService {
    Response<EmailContentDTO> sendEmail(EmailContentDTO emailContentDTO, BindingResult bindingResult) throws ViolatedException;
}
