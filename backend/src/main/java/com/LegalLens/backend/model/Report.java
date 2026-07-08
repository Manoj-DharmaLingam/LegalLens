package com.LegalLens.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reportType;
    private LocalDateTime generatedDate;
    @Column(columnDefinition="TEXT")
    private String summary;

    public Report(){}

    public Report(String reportType,String summary)
    {
        this();
        this.reportType = reportType;
        this.summary = summary;
    }

     public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getReportType() { 
        return reportType; 
    }
    public void setReportType(String reportType) { 
        this.reportType = reportType; 
    }

    public LocalDateTime getGeneratedDate() { 
        return generatedDate; 
    }
    public void setGeneratedDate(LocalDateTime generatedDate) { 
        this.generatedDate = generatedDate; 
    }

    public String getSummary() { 
        return summary; 
    }
    public void setSummary(String summary) { 
        this.summary = summary; 
    }
}
