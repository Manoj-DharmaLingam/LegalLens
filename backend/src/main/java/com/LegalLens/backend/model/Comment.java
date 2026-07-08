package com.LegalLens.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    public Comment() {
        this.createdAt = LocalDateTime.now();
    }

    public Comment(String content , User reviewer , Contract contract){
        this.content = content;
        this.reviewer = reviewer;
        this.contract = contract;
    }

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getContent() { 
        return content; 
    }
    public void setContent(String content) { 
        this.content = content; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    public User getReviewer() { 
        return reviewer; 
    }
    public void setReviewer(User reviewer) { 
        this.reviewer = reviewer; 
    }

    public Contract getContract() { 
        return contract; 
    }
    public void setContract(Contract contract) { 
        this.contract = contract; 
    }
}
