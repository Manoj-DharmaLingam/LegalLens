package com.LegalLens.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="clauses")
public class Clause {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String clauseType;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String riskLevel;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    public Clause() {}

    public Clause(String clauseType,
                    String content,
                    String riskLevel){
                        this.clauseType = clauseType;
                        this.content = content;
                        this.riskLevel = riskLevel;
                    }

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    public String getClauseType() { 
        return clauseType; 
    }
    public void setClauseType(String clauseType) { 
        this.clauseType = clauseType; 
    }

    public String getContent() { 
        return content; 
    }
    public void setContent(String content) { 
        this.content = content; 
    }
    public String getRiskLevel() { 
        return riskLevel; 
    }
    public void setRiskLevel(String riskLevel) { 
        this.riskLevel = riskLevel; 
    }

    public Contract getContract() { 
        return contract; 
    }
    public void setContract(Contract contract) { 
        this.contract = contract; 
    }
}
