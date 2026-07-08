package com.LegalLens.backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LegalLens.backend.model.ClauseTemplate;
import com.LegalLens.backend.model.ComplianceRule;
import com.LegalLens.backend.model.User;
import com.LegalLens.backend.service.AdminService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/templates")
    public ResponseEntity<List<ClauseTemplate>> getTemplates() {
        return ResponseEntity.ok(adminService.getAllTemplates());
    }

    @PostMapping("/templates")
    public ResponseEntity<ClauseTemplate> createTemplate(@RequestBody @NonNull ClauseTemplate template) {
        return ResponseEntity.ok(adminService.createTemplate(template));
    }

    @PutMapping("/templates/{id}")
    public ResponseEntity<ClauseTemplate> updateTemplate(@PathVariable long id, @RequestBody ClauseTemplate template) {
        return ResponseEntity.ok(adminService.updateTemplate(id, template));
    }

    @DeleteMapping("/templates/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable long id) {
        adminService.deleteTemplate(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rules")
    public ResponseEntity<List<ComplianceRule>> getRules() {
        return ResponseEntity.ok(adminService.getAllRules());
    }

    @PostMapping("/rules")
    public ResponseEntity<ComplianceRule> createRule(@RequestBody @NonNull ComplianceRule rule) {
        return ResponseEntity.ok(adminService.createRule(rule));
    }

    @PutMapping("/rules/{id}")
    public ResponseEntity<ComplianceRule> updateRule(@PathVariable long id, @RequestBody ComplianceRule rule) {
        return ResponseEntity.ok(adminService.updateRule(id, rule));
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable long id) {
        adminService.deleteRule(id);
        return ResponseEntity.ok().build();
    }
}
