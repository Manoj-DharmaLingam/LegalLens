package com.LegalLens.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LegalLens.backend.model.Clause;
import com.LegalLens.backend.model.Contract;

public interface ClauseRepository extends JpaRepository<Clause, Long>{
    List<Clause> findByComment(Contract contract);
}
