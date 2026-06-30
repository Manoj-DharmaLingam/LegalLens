    package com.LegalLens.backend.model;


    import java.time.LocalDateTime;

    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.Table;

    @Entity
    @Table(name="audit_log")
    public class AuditLog {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    private String action;

    private String username;

    private String details;

        @Column
        private LocalDateTime logTimestamp;

        public AuditLog(){
            this.logTimestamp = LocalDateTime.now();
        }

        public AuditLog(String action, String username ,String details){
            this();
            this.action = action;
            this.username = username;
            this.details = details;
        }
        public Long getId() { 
            return id; }
        public void setId(Long id) { 
            this.id = id; }

        public String getAction() { 
            return action; }
        public void setAction(String action) { 
            this.action = action; }

        public String getUsername() { 
            return username; }
        public void setUsername(String username) { 
            this.username = username; }

        public String getDetails() { return details; }
        public void setDetails(String details) { 
            this.details = details; }

        public LocalDateTime getLogTimestamp() { return logTimestamp; }
        public void setLogTimestamp(LocalDateTime logTimestamp) { 
            this.logTimestamp = logTimestamp; }

    }
