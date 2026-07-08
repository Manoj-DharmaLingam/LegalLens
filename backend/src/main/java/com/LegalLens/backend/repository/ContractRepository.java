package com.LegalLens.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LegalLens.backend.model.Contract;
import com.LegalLens.backend.model.User;

public interface  ContractRepository extends JpaRepository<Contract, Long> {
        List<Contract> findByUploadedBy(User user);
}
