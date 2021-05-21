package com.nals.hrm.service;

import com.nals.hrm.dto.DaysOffDTO;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;

public interface EmailService {
    void sendEmail(String to, String cc, String subject, String body) throws MessagingException;
    String mailContent(List<DaysOffDTO> daysOffDTOList);
}
