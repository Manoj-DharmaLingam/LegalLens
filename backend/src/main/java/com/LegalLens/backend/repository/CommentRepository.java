package com.LegalLens.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LegalLens.backend.model.Comment;
import com.LegalLens.backend.model.Contract;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByContract(Contract contract);
}
