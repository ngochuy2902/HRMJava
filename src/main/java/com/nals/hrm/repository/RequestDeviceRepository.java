package com.nals.hrm.repository;

import com.nals.hrm.model.RequestDevices;
import com.nals.hrm.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RequestDeviceRepository extends JpaRepository<RequestDevices, Integer> {

    Page<RequestDevices> findByUserInAndDeviceNameContainingAndStatusInAndExpectedDeliveryDateGreaterThanEqualAndAndExpectedDeliveryDateLessThanEqualAndDeletedAtNull(Pageable page,
                                                                                                                                                      List<Users> users,
                                                                                                                                                      String deviceName,
                                                                                                                                                      List<Integer> status,
                                                                                                                                                      LocalDate fromDate,
                                                                                                                                                      LocalDate toDate);

    RequestDevices findByIdAndDeletedAtNull(Integer id);
}
