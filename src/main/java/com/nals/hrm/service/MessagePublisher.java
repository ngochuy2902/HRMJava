package com.nals.hrm.service;

import com.nals.hrm.model.EmailContent;

public interface MessagePublisher {
    void publish(EmailContent emailContent);
}
