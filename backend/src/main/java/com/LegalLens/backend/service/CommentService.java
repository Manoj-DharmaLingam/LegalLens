package com.LegalLens.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.LegalLens.backend.exception.ResourceNotFoundException;
import com.LegalLens.backend.model.Comment;
import com.LegalLens.backend.model.Contract;
import com.LegalLens.backend.model.User;
import com.LegalLens.backend.repository.CommentRepository;
import com.LegalLens.backend.repository.ContractRepository;
import com.LegalLens.backend.repository.UserRepository;



@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          ContractRepository contractRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
    }

    public Comment addComment(long contractId, String content, String username) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + contractId));
        User reviewer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Comment comment = new Comment(content, reviewer ,contract);

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByContract(long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + contractId));
        return commentRepository.findByContract(contract);
    }

    public boolean isOwner(long contractId, String username) {
    Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));
            
    return contract.getUploadedBy().getUsername().equals(username);
}

}
