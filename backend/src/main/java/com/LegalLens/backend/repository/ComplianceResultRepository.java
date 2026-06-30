package com.LegalLens.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LegalLens.backend.model.ComplianceResult;
import com.LegalLens.backend.model.Contract;

public interface ComplianceResultRepository extends JpaRepository<ComplianceResult, Long> {

    Optional<ComplianceResult> findByContract(Contract contract);
}
