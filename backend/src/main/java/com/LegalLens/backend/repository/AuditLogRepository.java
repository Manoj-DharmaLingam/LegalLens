package com.LegalLens.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LegalLens.backend.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByOrderByLogTimestampDesc();
}
