package com.LegalLens.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LegalLens.backend.model.Report;
import com.LegalLens.backend.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/contracts")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER', 'CLIENT')")
    public ResponseEntity<Report> contractReport() {
        return ResponseEntity.ok(reportService.generateContractSummaryReport());
    }

    @GetMapping("/compliance")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER', 'CLIENT')")
    public ResponseEntity<Report> complianceReport() {
        return ResponseEntity.ok(reportService.generateComplianceOverview());
    }

    @GetMapping("/risks")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER', 'CLIENT')")
    public ResponseEntity<Report> riskReport() {
        return ResponseEntity.ok(reportService.generateRiskAnalysis());
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER', 'CLIENT')")
    public ResponseEntity<List<Report>> allReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }
}
