package com.LegalLens.backend.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.LegalLens.backend.exception.ResourceNotFoundException;
import com.LegalLens.backend.model.Contract;
import com.LegalLens.backend.model.User;
import com.LegalLens.backend.repository.ComplianceResultRepository;
import com.LegalLens.backend.repository.ContractRepository;
import com.LegalLens.backend.repository.UserRepository;

@Service
@Transactional
public class ContractService {
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final ComplianceResultRepository complianceResultRepository;

    public ContractService(ContractRepository contractRepository,
                           UserRepository userRepository,
                           ComplianceResultRepository complianceResultRepository) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
        this.complianceResultRepository = complianceResultRepository;
    }

    public List<Contract> getAllContracts() {
        List<Contract> contracts = contractRepository.findAll();
        attachComplianceScores(contracts);
        return contracts;
    } 

    public List<Contract> getContractByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        List<Contract> contracts = contractRepository.findByUploadedBy(user);
        attachComplianceScores(contracts);
        return contracts;
    }

    public Contract getContractById(long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + id));
        attachComplianceScore(contract);
        return contract;
    }    

    public Contract updateStatus(Long id, String status) {
        Contract existingcontract = getContractById(id);
        existingcontract.setStatus(status);
        return contractRepository.save(existingcontract);
    }

    public Contract uploadContract(MultipartFile file, String username, String contractName, String contractType, String description) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

  
        String userHome = System.getProperty("user.home");
        File uploadDirectory = new File(userHome + File.separator + "legallens_uploads");
        
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File destination = new File(uploadDirectory, fileName);
        file.transferTo(destination);
        
        Contract contract = new Contract(contractName, contractType, description, user, fileName);
        contract.setStatus("UPLOADED");
        
        return contractRepository.save(contract);
    }

    public void deleteContract(long id) {
        if (!contractRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contract not found with id: " + id);
        }
        contractRepository.deleteById(id);
    }

    private void attachComplianceScores(List<Contract> contracts) {
        if (contracts.isEmpty()) {
            return;
        }

        Map<Long, Integer> scoreByContractId = complianceResultRepository.findAll().stream()
                .filter(result -> result.getContract() != null && result.getContract().getId() != null)
                .collect(Collectors.toMap(
                result -> result.getContract().getId(),
                result -> result.getComplianceScore(),
                        (existing, replacement) -> replacement));

        contracts.forEach(contract -> contract.setComplianceScore(scoreByContractId.get(contract.getId())));
    }

    private void attachComplianceScore(Contract contract) {
        complianceResultRepository.findByContract(contract)
                .ifPresent(result -> contract.setComplianceScore(result.getComplianceScore()));
    }
}