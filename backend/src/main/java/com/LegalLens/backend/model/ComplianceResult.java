package com.LegalLens.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "compliance_results")
public class ComplianceResult {

      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer complianceScore;

    @Column(columnDefinition = "TEXT")
    private String issuesFound;

    private LocalDateTime analyzedAt;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    public ComplianceResult() {
        this.analyzedAt = LocalDateTime.now();
    }

    public ComplianceResult(Contract contract, 
                            Integer complianceScore, 
                            String issuesFound) {
        //calculations in the service layer formula given
        this(); 
        this.contract = contract;
        this.complianceScore = complianceScore;
        this.issuesFound = issuesFound;
    }

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    public Integer getComplianceScore() { 
        return complianceScore; 
    }
    public void setComplianceScore(Integer complianceScore) { 
        this.complianceScore = complianceScore; 
    }
    public String getIssuesFound() { 
        return issuesFound; 
    }
    public void setIssuesFound(String issuesFound) { 
        this.issuesFound = issuesFound; 
    }
    public LocalDateTime getAnalyzedAt() { 
        return analyzedAt; 
    }
    public void setAnalyzedAt(LocalDateTime analyzedAt) { 
        this.analyzedAt = analyzedAt; 
    }
    public Contract getContract() { 
        return contract; 
    }
    public void setContract(Contract contract) { 
        this.contract = contract; 
    }
}

