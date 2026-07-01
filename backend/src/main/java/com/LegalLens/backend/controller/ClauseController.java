package com.LegalLens.backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LegalLens.backend.model.Clause;
import com.LegalLens.backend.service.ClauseService;

@RestController
@RequestMapping("/api/clauses")
public class ClauseController {

    private final ClauseService clauseService;

    public ClauseController(ClauseService clauseService) {
        this.clauseService = clauseService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER','CLIENT')")
    public ResponseEntity<List<Clause>> getClauses(@RequestParam long contractId) {
        return ResponseEntity.ok(clauseService.getClausesByContract(contractId));
    }

    @PostMapping("/analyze")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER')")
    public ResponseEntity<List<Clause>> analyzeContract(@RequestParam long contractId) throws IOException {
        return ResponseEntity.ok(clauseService.analyzeContract(contractId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER')")
    public ResponseEntity<Clause> updateClause(@PathVariable long id, @RequestBody Clause clause) {
        return ResponseEntity.ok(clauseService.updateClause(id, clause));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER')")
    public ResponseEntity<String> deleteClause(@PathVariable long id) {
        clauseService.deleteClause(id);
        return ResponseEntity.ok("Clause deleted successfully");
    }
}
