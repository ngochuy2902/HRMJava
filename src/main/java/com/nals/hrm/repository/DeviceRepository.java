package com.nals.hrm.repository;

import com.nals.hrm.model.Devices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Devices, Integer>, JpaSpecificationExecutor<Devices> {

    Devices findByIdAndDeletedAtNull(Integer id);
}
