package com.pestov.testexercise.repositories;

import com.pestov.testexercise.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

	User findAllById(Long id);
}
