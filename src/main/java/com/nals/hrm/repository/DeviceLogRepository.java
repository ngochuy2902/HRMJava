package com.nals.hrm.repository;

import com.nals.hrm.model.DeviceLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceLogRepository extends JpaRepository<DeviceLogs,Integer> {
}
