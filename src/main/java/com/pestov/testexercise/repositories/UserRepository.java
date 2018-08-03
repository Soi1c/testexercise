package com.pestov.testexercise.repositories;

import com.pestov.testexercise.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<CustomUser, Long> {

    CustomUser findByEmail(String email);

	CustomUser findAllById(Long id);
}
