package com.LegalLens.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LegalLens.backend.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long>{
    
}
