package com.LegalLens.backend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LegalLens.backend.exception.ResourceNotFoundException;
import com.LegalLens.backend.model.ClauseTemplate;
import com.LegalLens.backend.model.ComplianceRule;
import com.LegalLens.backend.model.User;
import com.LegalLens.backend.repository.ClauseTemplateRepository;
import com.LegalLens.backend.repository.CommentRepository;
import com.LegalLens.backend.repository.ComplianceRuleRepository;
import com.LegalLens.backend.repository.ContractRepository;
import com.LegalLens.backend.repository.UserRepository;

@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final UserRepository userRepository;
    private final ClauseTemplateRepository clauseTemplateRepository;
    private final ComplianceRuleRepository complianceRuleRepository;
    private final ContractRepository contractRepository;
    private final CommentRepository commentRepository;

    public AdminService(UserRepository userRepository,
                        ClauseTemplateRepository clauseTemplateRepository,
                        ComplianceRuleRepository complianceRuleRepository,
                        ContractRepository contractRepository,
                        CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.clauseTemplateRepository = clauseTemplateRepository;
        this.complianceRuleRepository = complianceRuleRepository;
        this.contractRepository = contractRepository;
        this.commentRepository = commentRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(long id) {
        logger.info("AdminService.deleteUser called with id={}", id);
        boolean exists = userRepository.existsById(id);
        logger.info("userRepository.existsById({}) => {}", id, exists);
        if (!exists) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        int contractsDetached = contractRepository.clearUploadedByUser(id);
        int commentsDetached = commentRepository.clearReviewerByUser(id);
        logger.info("Detached {} contracts and {} comments from user id={}", contractsDetached, commentsDetached, id);

        userRepository.deleteById(id);
    }

    public List<ClauseTemplate> getAllTemplates() {
        return clauseTemplateRepository.findAll();
    }

    public ClauseTemplate createTemplate(ClauseTemplate template) {
        return clauseTemplateRepository.save(template);
    }

    public ClauseTemplate updateTemplate(long id, ClauseTemplate templateDetails) {
        ClauseTemplate existing = clauseTemplateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + id));
        existing.setName(templateDetails.getName());
        existing.setCategory(templateDetails.getCategory());
        existing.setDefaultContent(templateDetails.getDefaultContent());
        existing.setRecommendedRiskLevel(templateDetails.getRecommendedRiskLevel());
        return clauseTemplateRepository.save(existing);
    }

    public void deleteTemplate(long id) {
        if (!clauseTemplateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Template not found with id: " + id);
        }
        clauseTemplateRepository.deleteById(id);
    }

    public List<ComplianceRule> getAllRules() {
        return complianceRuleRepository.findAll();
    }

    public ComplianceRule createRule(ComplianceRule rule) {
        return complianceRuleRepository.save(rule);
    }

    public ComplianceRule updateRule(long id, ComplianceRule ruleDetails) {
        ComplianceRule existing = complianceRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found with id: " + id));
        existing.setRuleName(ruleDetails.getRuleName());
        existing.setDescription(ruleDetails.getDescription());
        existing.setSeverityLevel(ruleDetails.getSeverityLevel());
        return complianceRuleRepository.save(existing);
    }

    public void deleteRule(long id) {
        if (!complianceRuleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rule not found with id: " + id);
        }
        complianceRuleRepository.deleteById(id);
    }
}
