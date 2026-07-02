package com.LegalLens.backend.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.LegalLens.backend.model.Clause;
import com.LegalLens.backend.model.Report;
import com.LegalLens.backend.repository.ClauseRepository;
import com.LegalLens.backend.repository.ContractRepository;
import com.LegalLens.backend.repository.ReportRepository;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ContractRepository contractRepository;
    private final ClauseRepository clauseRepository;

    public ReportService(ReportRepository reportRepository,
                         ContractRepository contractRepository,
                         ClauseRepository clauseRepository) {
        this.reportRepository = reportRepository;
        this.contractRepository = contractRepository;
        this.clauseRepository = clauseRepository;
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report generateContractSummaryReport() {
        long total = contractRepository.count();
        Report report = new Report();
        report.setReportType("Contract Summary");
        report.setSummary("Total contracts in system: " + total);
        return reportRepository.save(report);
    }

    public Report generateComplianceOverview() {
        Report report = new Report();
        report.setReportType("Compliance Overview");
        report.setSummary("Aggregated compliance metrics across all reviewed contracts.");
        return reportRepository.save(report);
    }

    public Report generateRiskAnalysis() {
        List<Clause> allClauses = clauseRepository.findAll();
        long count = allClauses.stream().filter(c -> "HIGH".equals(c.getRiskLevel())).count();
        Report report = new Report();
        report.setReportType("Risk Analysis");
        report.setSummary("Total high risk clauses detected: " + count);
        return reportRepository.save(report);
    }
}