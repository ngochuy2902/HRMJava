package com.nals.hrm.service.impl;

import com.nals.hrm.dto.EmailContentDTO;
import com.nals.hrm.exception.ViolatedException;
import com.nals.hrm.model.EmailContent;
import com.nals.hrm.model.Response;
import com.nals.hrm.service.ResponseService;
import com.nals.hrm.service.SendEmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    private final RedisMessagePublisher redisMessagePublisher;
    private final ResponseService responseService;
    private final ModelMapper modelMapper;

    @Autowired
    public SendEmailServiceImpl(RedisMessagePublisher redisMessagePublisher, ResponseService responseService, ModelMapper modelMapper) {
        this.redisMessagePublisher = redisMessagePublisher;
        this.responseService = responseService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response<EmailContentDTO> sendEmail(EmailContentDTO emailContentDTO, BindingResult bindingResult) throws ViolatedException {
        if (bindingResult.hasErrors()) {
            throw new ViolatedException(bindingResult);
        }
        EmailContent emailContent = modelMapper.map(emailContentDTO, EmailContent.class);
        redisMessagePublisher.publish(emailContent);
        return responseService.dataResponseOne(emailContent, HttpStatus.OK.getReasonPhrase(), EmailContentDTO.class);
    }
}
