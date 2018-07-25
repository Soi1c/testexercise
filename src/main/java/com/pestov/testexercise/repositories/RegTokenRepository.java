package com.pestov.testexercise.repositories;

import com.pestov.testexercise.models.RegToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegTokenRepository extends JpaRepository<RegToken, Long> {

	RegToken findByRegToken(String regtoken);

	RegToken save(RegToken regToken);

	void delete(RegToken regToken);
}
