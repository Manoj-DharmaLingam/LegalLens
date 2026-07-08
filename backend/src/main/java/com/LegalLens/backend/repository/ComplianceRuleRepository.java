package com.LegalLens.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LegalLens.backend.model.ComplianceRule;


public interface ComplianceRuleRepository extends JpaRepository<ComplianceRule, Long> {
}
