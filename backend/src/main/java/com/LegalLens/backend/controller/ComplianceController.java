package com.LegalLens.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LegalLens.backend.model.ComplianceResult;
import com.LegalLens.backend.model.ComplianceRule;
import com.LegalLens.backend.service.ComplianceService;

@RestController
@RequestMapping("/api/compliance")
public class ComplianceController {

    private final ComplianceService complianceService;

    public ComplianceController(ComplianceService complianceService) {
        this.complianceService = complianceService;
    }

    @GetMapping("/rules")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER')")
    public ResponseEntity<List<ComplianceRule>> getRules() {
        return ResponseEntity.ok(complianceService.getAllRules());
    }

    @PostMapping("/check")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER','CLIENT')")
    public ResponseEntity<ComplianceResult> checkCompliance(@RequestParam Long contractId) {
        return ResponseEntity.ok(complianceService.checkCompliance(contractId));
    }

    @GetMapping("/results/{contractId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER','CLIENT')")
    public ResponseEntity<ComplianceResult> getResult(@PathVariable Long contractId) {
        return ResponseEntity.ok(complianceService.getResultByContract(contractId));
    }
}
