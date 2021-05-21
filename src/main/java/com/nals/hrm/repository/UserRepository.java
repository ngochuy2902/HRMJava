package com.nals.hrm.repository;

import com.nals.hrm.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {

    List<Users> findAllByFullNameContainingAndDeletedAtNull(String name);

    Users findByIdAndDeletedAtNull(Integer id);

    List<Users> findAllByDeletedAtNull();
}
