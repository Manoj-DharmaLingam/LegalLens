package com.LegalLens.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.LegalLens.backend.model.Contract;
import com.LegalLens.backend.model.User;

public interface  ContractRepository extends JpaRepository<Contract, Long> {
        List<Contract> findByUploadedBy(User user);

        @Modifying
        @Query("update Contract c set c.uploadedBy = null where c.uploadedBy.id = :userId")
        int clearUploadedByUser(@Param("userId") Long userId);
}
