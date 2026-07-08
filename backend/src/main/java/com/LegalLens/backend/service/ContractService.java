package com.LegalLens.backend.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.LegalLens.backend.exception.ResourceNotFoundException;
import com.LegalLens.backend.model.Contract;
import com.LegalLens.backend.model.User;
import com.LegalLens.backend.repository.ContractRepository;
import com.LegalLens.backend.repository.UserRepository;

@Service
@Transactional
public class ContractService {
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    public ContractService(ContractRepository contractRepository, UserRepository userRepository) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    } 

    public List<Contract> getContractByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return contractRepository.findByUploadedBy(user);
    }

    public Contract getContractById(long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + id));
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
}