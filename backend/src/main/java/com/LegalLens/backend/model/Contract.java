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
import jakarta.persistence.Transient;

@Entity
@Table(name="contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String contractName;

    private String contractType;  
    
    @Column(columnDefinition="TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    private LocalDateTime uploadDate;

    private String status;

    @Transient
    private Integer complianceScore;

    public Contract(){
        uploadDate = LocalDateTime.now();
        status = "Pending";
    }
    private String fileUrl; 
    
    public Contract(String contractName , String contractType ,String description,User uploadedBy,String fileUrl){
        this();
        this.contractName = contractName;
        this.contractType = contractType;
        this.description = description;
        this.uploadedBy = uploadedBy;
        this.fileUrl = fileUrl;
    }
    public Long getId() { 
        return id; }
    public void setId(Long id) { 
        this.id = id; }

    public String getContractName() { 
        return contractName; }
    public void setContractName(String contractName) { this.contractName = contractName; }

    public String getContractType() { 
        return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }

    public String getDescription() { 
        return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public User getUploadedBy() { 
        return uploadedBy; }
    public void setUploadedBy(User uploadedBy) { this.uploadedBy = uploadedBy; }

     public String getStatus() { 
        return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getComplianceScore() {
        return complianceScore;
    }

    public void setComplianceScore(Integer complianceScore) {
        this.complianceScore = complianceScore;
    }

    public LocalDateTime getUploadDate() { 
        return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
}
