package com.LegalLens.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LegalLens.backend.model.Comment;
import com.LegalLens.backend.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER') or @contractService.isOwner(#contractId, authentication.name)")
    public ResponseEntity<Comment> addComment(@RequestParam Long contractId,
                                        @RequestBody String content,
                                        Authentication authentication) {
        return ResponseEntity.ok(commentService.addComment(contractId, content, authentication.getName()));
    }

    @GetMapping("/{contractId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEGAL_REVIEWER') or @contractService.isOwner(#contractId, authentication.name)")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long contractId) {
        return ResponseEntity.ok(commentService.getCommentsByContract(contractId));
    }
}
