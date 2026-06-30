package com.LegalLens.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LegalLens.backend.model.ClauseTemplate;


public interface ClauseTemplateRepository extends JpaRepository<ClauseTemplate, Long> {
}
