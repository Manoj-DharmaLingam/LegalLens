package com.LegalLens.backend.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.LegalLens.backend.exception.BusinessValidationException;
import com.LegalLens.backend.exception.ResourceNotFoundException;
import com.LegalLens.backend.model.Clause;
import com.LegalLens.backend.model.Contract;
import com.LegalLens.backend.repository.ClauseRepository;
import com.LegalLens.backend.repository.ContractRepository;

@Service
public class ClauseService {

    private final ClauseRepository clauseRepository;
    private final ContractRepository contractRepository;

    public ClauseService(ClauseRepository clauseRepository, ContractRepository contractRepository) {
        this.clauseRepository = clauseRepository;
        this.contractRepository = contractRepository;
    }

    public List<Clause> analyzeContract(long contractId) throws IOException {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + contractId));

        if ("COMPLETED".equals(contract.getStatus())) {
            throw new BusinessValidationException("Cannot analyze a contract that is already marked as COMPLETED.");
        }

        if (contract.getFileUrl() == null || contract.getFileUrl().trim().isEmpty()) {
            throw new BusinessValidationException("Contract file path is missing or invalid.");
        }

        // Align with the identical absolute home space directory used during contract uploads
        String userHome = System.getProperty("user.home");
        String filePath = userHome + File.separator + "legallens_uploads" + File.separator + contract.getFileUrl();
        File file = new File(filePath);

        if (!file.exists()) {
            throw new ResourceNotFoundException("The physical file for the contract could not be located at path: " + filePath);
        }

        String text;
        if (contract.getFileUrl().endsWith(".pdf")) {
            try (PDDocument document = Loader.loadPDF(file)) {
                PDFTextStripper stripper = new PDFTextStripper();
                text = stripper.getText(document);
            }
        } else {
            text = new String(Files.readAllBytes(file.toPath()));
        }

        String lowerText = text.toLowerCase();
        List<Clause> detectedClauses = new ArrayList<>();

        String[][] keywords = {
            {"payment", "Payment Terms", "LOW"},
            {"confidential", "Confidentiality", "LOW"},
            {"terminate", "Termination", "MEDIUM"},
            {"liability", "Limitation of Liability", "HIGH"},
            {"renew", "Renewal", "LOW"}
        };

        for (String[] entry : keywords) {
            String keyword = entry[0];
            String clauseType = entry[1];
            String riskLevel = entry[2];

            int index = lowerText.indexOf(keyword);
            if (index != -1) {
                int end = Math.min(index + 200, text.length());
                String snippet = text.substring(index, end) + "...";

                String snippetLower = snippet.toLowerCase();
                if (snippetLower.contains("unlimited liability")) {
                    riskLevel = "HIGH";
                } else if (snippetLower.contains("penalty")) {
                    riskLevel = "MEDIUM";
                }

                Clause clause = new Clause();
                clause.setClauseType(clauseType);
                clause.setContent(snippet);
                clause.setRiskLevel(riskLevel);
                clause.setContract(contract);

                detectedClauses.add(clause);
            }
        }

        contract.setStatus("UNDER_REVIEW");
        contractRepository.save(contract);

        return clauseRepository.saveAll(detectedClauses);
    }

    public List<Clause> getClausesByContract(long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + contractId));
        return clauseRepository.findByContract(contract);
    }

    public Clause updateClause(long id, Clause clauseDetails) {
        Clause clause = clauseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clause not found with id: " + id));
        clause.setClauseType(clauseDetails.getClauseType());
        clause.setContent(clauseDetails.getContent());
        clause.setRiskLevel(clauseDetails.getRiskLevel());
        return clauseRepository.save(clause);
    }

    public void deleteClause(long id) {
        if (!clauseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Clause not found with id: " + id);
        }
        clauseRepository.deleteById(id);
    }
}
