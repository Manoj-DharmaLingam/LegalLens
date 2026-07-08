package com.LegalLens.backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.LegalLens.backend.model.Contract;
import com.LegalLens.backend.service.ContractService;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER')")
    public ResponseEntity<List<Contract>> allContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER','CLIENT')")
    public ResponseEntity<List<Contract>> getMethodByUsername(@PathVariable String username) {
        return ResponseEntity.ok(contractService.getContractByUser(username));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER','CLIENT')")
    public ResponseEntity<Contract> getMethodById(@PathVariable long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('LEGAL_REVIEWER', 'CLIENT')")
    public ResponseEntity<Contract> uploadContract(
            @RequestParam("file") MultipartFile file,
            Authentication authentication,
            @RequestParam String contractName,
            @RequestParam String contractType,
            @RequestParam String description) throws IOException {
        return ResponseEntity.ok(contractService.uploadContract(file, authentication.getName(), contractName, contractType, description));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER')")
    public ResponseEntity<String> deleteContract(@PathVariable long id) {
        contractService.deleteContract(id);
        return ResponseEntity.ok("Contract deleted successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER')")
    public ResponseEntity<Contract> putMethodName(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(contractService.updateStatus(id, status));
    }
}
