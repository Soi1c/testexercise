package com.pestov.testexercise.repositories;

import com.pestov.testexercise.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser, Long> {

    Optional<CustomUser> findByEmail(String email);
}
