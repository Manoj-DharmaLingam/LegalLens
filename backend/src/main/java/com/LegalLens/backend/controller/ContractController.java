package com.LegalLens.backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.LegalLens.backend.model.Contract;
import com.LegalLens.backend.service.ContractService;





@RestController
@RequestMapping("/api/contracts")
public class ContractController{

    private ContractService contractService;

    public ContractController(ContractService contractService){
        this.contractService = contractService;
    }
    
    @GetMapping
    public ResponseEntity<List<Contract>> allContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    @GetMapping("/{username}")
    public  ResponseEntity<List<Contract>> getMethodByUsername(@PathVariable String username) {
        return ResponseEntity.ok(contractService.getContractByUser(username));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Contract> getMethodById(@PathVariable long id) {
        return ResponseEntity.ok(contractService.getContractById(id));
    }

    @PostMapping("/upload")
        public ResponseEntity<Contract> uploadContract(
        @RequestParam("file") MultipartFile file,
        @RequestParam String username,
        @RequestParam String contractName,
        @RequestParam String contractType,
        @RequestParam String description) throws IOException {
        return ResponseEntity.ok(contractService.uploadContract(file, username, contractName, contractType, description));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContract(@PathVariable long id) {
        contractService.deleteContract(id);
        return ResponseEntity.ok("Delted Bro :(");
    }

    @PutMapping("update/{id}")
    public Contract putMethodName(@PathVariable Long id ,@RequestBody String status) {
        return contractService.updateStatus(id, status);
    }
}
