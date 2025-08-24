package com.crm.service.impl;

import com.crm.dto.CustomerDTO;
import com.crm.entity.Customer;
import com.crm.entity.User;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.CustomerRepository;
import com.crm.repository.UserRepository;
import com.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of CustomerService interface.
 * Provides business logic for customer management operations.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public CustomerDTO createCustomer(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return new CustomerDTO(savedCustomer);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerDTO> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(CustomerDTO::new);
    }
    
    @Override
    public CustomerDTO updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        
        // Update fields
        if (customerDetails.getCompanyName() != null) {
            customer.setCompanyName(customerDetails.getCompanyName());
        }
        if (customerDetails.getContactPerson() != null) {
            customer.setContactPerson(customerDetails.getContactPerson());
        }
        if (customerDetails.getEmail() != null) {
            customer.setEmail(customerDetails.getEmail());
        }
        if (customerDetails.getPhoneNumber() != null) {
            customer.setPhoneNumber(customerDetails.getPhoneNumber());
        }
        if (customerDetails.getCustomerType() != null) {
            customer.setCustomerType(customerDetails.getCustomerType());
        }
        if (customerDetails.getStatus() != null) {
            customer.setStatus(customerDetails.getStatus());
        }
        if (customerDetails.getIndustry() != null) {
            customer.setIndustry(customerDetails.getIndustry());
        }
        if (customerDetails.getAnnualRevenue() != null) {
            customer.setAnnualRevenue(customerDetails.getAnnualRevenue());
        }
        if (customerDetails.getEmployeeCount() != null) {
            customer.setEmployeeCount(customerDetails.getEmployeeCount());
        }
        if (customerDetails.getAddress() != null) {
            customer.setAddress(customerDetails.getAddress());
        }
        if (customerDetails.getCity() != null) {
            customer.setCity(customerDetails.getCity());
        }
        if (customerDetails.getState() != null) {
            customer.setState(customerDetails.getState());
        }
        if (customerDetails.getCountry() != null) {
            customer.setCountry(customerDetails.getCountry());
        }
        if (customerDetails.getPostalCode() != null) {
            customer.setPostalCode(customerDetails.getPostalCode());
        }
        if (customerDetails.getNotes() != null) {
            customer.setNotes(customerDetails.getNotes());
        }
        
        Customer updatedCustomer = customerRepository.save(customer);
        return new CustomerDTO(updatedCustomer);
    }
    
    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        
        customerRepository.delete(customer);
    }
    
    @Override
    public CustomerDTO assignCustomerToUser(Long customerId, Long userId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        customer.setAssignedTo(user);
        Customer updatedCustomer = customerRepository.save(customer);
        return new CustomerDTO(updatedCustomer);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> searchCustomers(String searchTerm) {
        return customerRepository.searchCustomers(searchTerm).stream()
                .map(CustomerDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByType(Customer.CustomerType customerType) {
        return customerRepository.findByCustomerType(customerType).stream()
                .map(CustomerDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByAssignedUser(Long userId) {
        return customerRepository.findByAssignedToId(userId).stream()
                .map(CustomerDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getCustomersByStatus(Customer.CustomerStatus status) {
        return customerRepository.findByStatus(status).stream()
                .map(CustomerDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCustomerAnalytics() {
        long totalCount = getTotalCustomerCount();
        Map<Customer.CustomerStatus, Long> statusCount = getCustomerCountByStatus();
        
        Map<String, Object> analytics = Map.of(
            "totalCustomers", totalCount,
            "customersByStatus", statusCount
        );
        
        return analytics;
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalCustomerCount() {
        return customerRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<Customer.CustomerStatus, Long> getCustomerCountByStatus() {
        return customerRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                    Customer::getStatus,
                    Collectors.counting()
                ));
    }
}
