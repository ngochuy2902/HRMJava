package com.nals.hrm.service;

import io.sentry.protocol.User;

public interface UserService {
    void save(User user);
}
