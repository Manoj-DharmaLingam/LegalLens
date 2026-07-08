package com.LegalLens.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "compliance_rules")
public class ComplianceRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String severityLevel;

    public ComplianceRule(String ruleName,
                        String description
                        ,String severityLevel) {
            this.ruleName = ruleName;
            this.description = description;
            this.severityLevel = severityLevel;
                        }
    
    public ComplianceRule() {}

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getRuleName() { 
        return ruleName; 
    }
    public void setRuleName(String ruleName) { 
        this.ruleName = ruleName; 
    }

    public String getDescription() { 
        return description; 
    }
    public void setDescription(String description) { 
        this.description = description; 
    }

    public String getSeverityLevel() { 
        return severityLevel; 
    }
    public void setSeverityLevel(String severityLevel) { 
        this.severityLevel = severityLevel; 
    }
}
