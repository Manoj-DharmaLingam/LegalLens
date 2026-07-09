package com.LegalLens.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.LegalLens.backend.model.Comment;
import com.LegalLens.backend.model.Contract;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByContract(Contract contract);

    @Modifying
    @Query("update Comment c set c.reviewer = null where c.reviewer.id = :userId")
    int clearReviewerByUser(@Param("userId") Long userId);
}
