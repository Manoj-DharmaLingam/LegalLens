package com.LegalLens.backend.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.LegalLens.backend.model.Contract;
import com.LegalLens.backend.model.User;
import com.LegalLens.backend.repository.ContractRepository;
import com.LegalLens.backend.repository.UserRepository;

//contractName , contractType , description , uploadedBy , status , time
@Service
public class ContractService {
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    public ContractService(ContractRepository contractRepository,UserRepository userRepository){
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
    }

    public List<Contract> getAllContracts(){
        return contractRepository.findAll();
    } 

    public List<Contract> getContractByUser(String username){
        User user = userRepository.findByUsername(username).orElseThrow();
        return contractRepository.findByUploadedBy(user);
    }

    public Contract getContractById(long id){
        return contractRepository.findById(id).orElseThrow();
    }    

    public Contract updateStatus(Long id,String status){
        Contract existingcontract = getContractById(id);
        existingcontract.setStatus(status);
        return contractRepository.save(existingcontract);
    }

    public Boolean isOwner(Long id,String username){
        Contract contract = getContractById(id);
        return contract.getUploadedBy().getUsername().equals(username);
    }

    public Contract uploadContract(MultipartFile file, String username, String contractName, String contractType, String description) throws IOException {

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            File uploadDirectory = new File("/uploads");
            if(!uploadDirectory.exists()){
                uploadDirectory.mkdir();
            }
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            File destination = new File(uploadDirectory, fileName);
            file.transferTo(destination);
            Contract contract = new Contract(contractName, contractType, description, user);
            contract.setStatus("UPLOADED");
            
            return contractRepository.save(contract);
    }

    public void deleteContract(long id) {
        contractRepository.deleteById(id);
    }
}
