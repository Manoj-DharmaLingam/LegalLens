package com.LegalLens.backend.service;


import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.LegalLens.backend.exception.ResourceNotFoundException;
import com.LegalLens.backend.model.Clause;
import com.LegalLens.backend.model.ComplianceResult;
import com.LegalLens.backend.model.ComplianceRule;
import com.LegalLens.backend.model.Contract;
import com.LegalLens.backend.repository.ClauseRepository;
import com.LegalLens.backend.repository.ComplianceResultRepository;
import com.LegalLens.backend.repository.ComplianceRuleRepository;
import com.LegalLens.backend.repository.ContractRepository;

@Service
public class ComplianceService {

    private final ContractRepository contractRepository;
    private final ClauseRepository clauseRepository;
    private final ComplianceResultRepository complianceResultRepository;
    private final ComplianceRuleRepository complianceRuleRepository;

    public ComplianceService(ContractRepository contractRepository,
                             ClauseRepository clauseRepository,
                             ComplianceResultRepository complianceResultRepository,
                             ComplianceRuleRepository complianceRuleRepository) {
        this.contractRepository = contractRepository;
        this.clauseRepository = clauseRepository;
        this.complianceResultRepository = complianceResultRepository;
        this.complianceRuleRepository = complianceRuleRepository;
    }

    public ComplianceResult checkCompliance(long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + contractId));

        List<Clause> clauses = clauseRepository.findByContract(contract);

        long highRisk = clauses.stream().filter(c -> "HIGH".equals(c.getRiskLevel())).count();
        long mediumRisk = clauses.stream().filter(c -> "MEDIUM".equals(c.getRiskLevel())).count();
        long lowRisk = clauses.stream().filter(c -> "LOW".equals(c.getRiskLevel())).count();

        int score = (int) Math.max(0, 100 - (highRisk * 15 + mediumRisk * 10 + lowRisk * 5));

        List<String> requiredTypes = Arrays.asList(
                "Payment Terms", "Confidentiality", "Termination", "Limitation of Liability", "Renewal"
        );

        StringBuilder issues = new StringBuilder();
        if (highRisk > 0) {
            issues.append("High risk clauses: ").append(highRisk).append(". ");
        }
        if (mediumRisk > 0) {
            issues.append("Medium risk clauses: ").append(mediumRisk).append(". ");
        }

        for (String required : requiredTypes) {
            boolean found = clauses.stream().anyMatch(c -> required.equals(c.getClauseType()));
            if (!found) {
                issues.append("MISSING: ").append(required).append(".");
            }
        }

        ComplianceResult result = complianceResultRepository.findByContract(contract)
                .orElse(new ComplianceResult());

        result.setContract(contract);
        result.setComplianceScore(score);
        result.setIssuesFound(issues.toString());

        return complianceResultRepository.save(result);
    }

    public List<ComplianceRule> getAllRules() {
        return complianceRuleRepository.findAll();
    }

    public ComplianceResult getResultByContract(long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + contractId));
        return complianceResultRepository.findByContract(contract)
                .orElseThrow(() -> new ResourceNotFoundException("Compliance result not found for contract: " + contractId));
    }
}