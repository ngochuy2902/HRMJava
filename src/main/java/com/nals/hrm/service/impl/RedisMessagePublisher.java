package com.nals.hrm.service.impl;

import com.nals.hrm.model.EmailContent;
import com.nals.hrm.model.Users;
import com.nals.hrm.repository.UserRepository;
import com.nals.hrm.service.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisMessagePublisher implements MessagePublisher {

    private final RedisTemplate<String, EmailContent> redisTemplate;
    private final ChannelTopic topic;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public RedisMessagePublisher(RedisTemplate<String, EmailContent> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(EmailContent emailContent) {
        List<Users> usersList = userRepository.findAllByDeletedAtNull();
        EmailContent content = new EmailContent();
        for (Users users : usersList) {
            content.setEmail(users.getEmail());
            content.setSubject(emailContent.getSubject());
            content.setContent(emailContent.getContent());
            redisTemplate.convertAndSend(topic.getTopic(), content);
        }
    }
}
