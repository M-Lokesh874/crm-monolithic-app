package com.crm.service.impl;

import com.crm.dto.LeadDTO;
import com.crm.dto.LeadUpdateDTO;
import com.crm.entity.Lead;
import com.crm.entity.User;
import com.crm.entity.Customer;
import com.crm.exception.BusinessException;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.LeadRepository;
import com.crm.repository.UserRepository;
import com.crm.repository.CustomerRepository;
import com.crm.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of LeadService interface.
 * Provides business logic for lead management operations.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
@Service
@Transactional
public class LeadServiceImpl implements LeadService {
    
    @Autowired
    private LeadRepository leadRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Override
    public LeadDTO createLead(Lead lead) {
        // Validate customer exists
        if (lead.getCustomer() == null || lead.getCustomer().getId() == null) {
            throw new BusinessException("Customer is required for lead creation");
        }
        
        Customer customer = customerRepository.findById(lead.getCustomer().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + lead.getCustomer().getId()));
        
        lead.setCustomer(customer);
        
        // Set default status if not provided
        if (lead.getLeadStatus() == null) {
            lead.setLeadStatus(Lead.LeadStatus.NEW);
        }
        
        Lead savedLead = leadRepository.save(lead);
        return new LeadDTO(savedLead);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> getAllLeads() {
        return leadRepository.findAll().stream()
                .map(LeadDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<LeadDTO> getLeadById(Long id) {
        return leadRepository.findById(id)
                .map(LeadDTO::new);
    }
    
    @Override
    public LeadDTO updateLead(Long id, Lead leadDetails) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));
        
        // Update fields
        if (leadDetails.getTitle() != null) {
            lead.setTitle(leadDetails.getTitle());
        }
        if (leadDetails.getDescription() != null) {
            lead.setDescription(leadDetails.getDescription());
        }
        if (leadDetails.getLeadStatus() != null) {
            lead.setLeadStatus(leadDetails.getLeadStatus());
        }
        if (leadDetails.getLeadSource() != null) {
            lead.setLeadSource(leadDetails.getLeadSource());
        }
        if (leadDetails.getExpectedValue() != null) {
            lead.setExpectedValue(leadDetails.getExpectedValue());
        }
        if (leadDetails.getProbabilityPercentage() != null) {
            lead.setProbabilityPercentage(leadDetails.getProbabilityPercentage());
        }
        if (leadDetails.getExpectedCloseDate() != null) {
            lead.setExpectedCloseDate(leadDetails.getExpectedCloseDate());
        }
        if (leadDetails.getNotes() != null) {
            lead.setNotes(leadDetails.getNotes());
        }
        
        Lead updatedLead = leadRepository.save(lead);
        return new LeadDTO(updatedLead);
    }
    
    @Override
    public LeadDTO partialUpdateLead(Long id, LeadUpdateDTO updateDTO) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));
        
        // Update only fields that are provided
        if (updateDTO.getTitle() != null) {
            lead.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getDescription() != null) {
            lead.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getLeadStatus() != null) {
            lead.setLeadStatus(Lead.LeadStatus.valueOf(updateDTO.getLeadStatus()));
        }
        if (updateDTO.getLeadSource() != null) {
            lead.setLeadSource(Lead.LeadSource.valueOf(updateDTO.getLeadSource()));
        }
        if (updateDTO.getExpectedValue() != null) {
            lead.setExpectedValue(updateDTO.getExpectedValue());
        }
        if (updateDTO.getProbabilityPercentage() != null) {
            lead.setProbabilityPercentage(updateDTO.getProbabilityPercentage());
        }
        if (updateDTO.getExpectedCloseDate() != null) {
            lead.setExpectedCloseDate(updateDTO.getExpectedCloseDate());
        }
        if (updateDTO.getNotes() != null) {
            lead.setNotes(updateDTO.getNotes());
        }
        
        // Handle customer assignment if provided
        if (updateDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findById(updateDTO.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + updateDTO.getCustomerId()));
            lead.setCustomer(customer);
        }
        
        // Handle user assignment if provided
        if (updateDTO.getAssignedToId() != null) {
            User user = userRepository.findById(updateDTO.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + updateDTO.getAssignedToId()));
            lead.setAssignedTo(user);
        }
        
        Lead updatedLead = leadRepository.save(lead);
        return new LeadDTO(updatedLead);
    }
    
    @Override
    public void deleteLead(Long id) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + id));
        
        leadRepository.delete(lead);
    }
    
    @Override
    public LeadDTO assignLeadToUser(Long leadId, Long userId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + leadId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        lead.setAssignedTo(user);
        Lead updatedLead = leadRepository.save(lead);
        return new LeadDTO(updatedLead);
    }
    
    @Override
    public LeadDTO updateLeadStatus(Long leadId, Lead.LeadStatus status) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + leadId));
        
        lead.setLeadStatus(status);
        Lead updatedLead = leadRepository.save(lead);
        return new LeadDTO(updatedLead);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> searchLeads(String searchTerm) {
        return leadRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchTerm, searchTerm)
                .stream()
                .map(LeadDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> getLeadsByStatus(Lead.LeadStatus status) {
        return leadRepository.findByLeadStatus(status)
                .stream()
                .map(LeadDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> getLeadsBySource(Lead.LeadSource source) {
        return leadRepository.findByLeadSource(source)
                .stream()
                .map(LeadDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> getLeadsByAssignedUser(Long userId) {
        return leadRepository.findByAssignedToId(userId)
                .stream()
                .map(LeadDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<LeadDTO> getLeadsByCustomer(Long customerId) {
        return leadRepository.findByCustomerId(customerId)
                .stream()
                .map(LeadDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public java.util.Map<String, Object> getLeadAnalytics() {
        long totalCount = getTotalLeadCount();
        java.util.Map<Lead.LeadStatus, Long> statusCount = getLeadCountByStatus();
        
        java.util.Map<String, Object> analytics = new java.util.HashMap<>();
        analytics.put("totalLeads", totalCount);
        analytics.put("leadsByStatus", statusCount);
        
        return analytics;
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalLeadCount() {
        return leadRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public java.util.Map<Lead.LeadStatus, Long> getLeadCountByStatus() {
        return leadRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    Lead::getLeadStatus,
                    Collectors.counting()
                ));
    }
}
