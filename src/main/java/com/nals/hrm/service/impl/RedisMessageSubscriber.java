package com.nals.hrm.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nals.hrm.model.EmailContent;
import com.nals.hrm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class RedisMessageSubscriber implements MessageListener {

    ObjectMapper objectMapper = new ObjectMapper();

    private final EmailService emailService;

    @Autowired
    public RedisMessageSubscriber(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        try {
            EmailContent emailContent = objectMapper.readValue(message.toString(), EmailContent.class);
            String email = emailContent.getEmail();
            String subject = emailContent.getSubject();
            String content = emailContent.getContent();
            emailService.sendEmail(email, null, subject, content);
        } catch (JsonProcessingException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
