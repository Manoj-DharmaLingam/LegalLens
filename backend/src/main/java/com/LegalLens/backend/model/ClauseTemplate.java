package com.LegalLens.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


//only for admin :)
@Entity
@Table(name="clause_templates")
public class ClauseTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category; //Payment , Liability

    @Column(columnDefinition = "TEXT")
    private String defaultContent;

    private String recommendedRiskLevel;

    public ClauseTemplate() {}

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getCategory() { 
        return category; 
    }
    public void setCategory(String category) { 
        this.category = category; 
    }

    public String getDefaultContent() { 
        return defaultContent; 
    }
    public void setDefaultContent(String defaultContent) { 
        this.defaultContent = defaultContent; 
    }

    public String getRecommendedRiskLevel() { 
        return recommendedRiskLevel; 
    }
    public void setRecommendedRiskLevel(String recommendedRiskLevel) { 
        this.recommendedRiskLevel = recommendedRiskLevel; 
    }
}
