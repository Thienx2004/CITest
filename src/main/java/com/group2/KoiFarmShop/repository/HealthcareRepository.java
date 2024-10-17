package com.group2.KoiFarmShop.repository;

import com.group2.KoiFarmShop.entity.Certificate;
import com.group2.KoiFarmShop.entity.Healthcare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthcareRepository extends JpaRepository<Healthcare, Integer> {
}
