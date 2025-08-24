package com.crm.config;

import com.crm.entity.*;
import com.crm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Data initializer component for populating the database with initial data.
 * 
 * This component implements CommandLineRunner to execute data initialization
 * logic when the Spring Boot application starts. It creates default users
 * with different roles to ensure the system has basic functionality from
 * the start.
 * 
 * The initializer creates:
 * <ul>
 *   <li>Administrator user with full system access</li>
 *   <li>Manager user with elevated access to team resources</li>
 *   <li>Sales representative user with customer management access</li>
 * </ul>
 * 
 * This component is essential for:
 * <ul>
 *   <li>First-time system setup</li>
 *   <li>Development and testing environments</li>
 *   <li>Demo and evaluation purposes</li>
 *   <li>Ensuring system usability from startup</li>
 * </ul>
 * 
 * Note: In production environments, this component should be disabled
 * or modified to prevent automatic user creation.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 * @see org.springframework.boot.CommandLineRunner
 * @see com.crm.entity.User
 * @see com.crm.repository.UserRepository
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    /**
     * Repository for user data operations.
     * Used to save initial users to the database.
     */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Repository for customer data operations.
     * Used to save sample customers to the database.
     */
    @Autowired
    private CustomerRepository customerRepository;
    
    /**
     * Repository for lead data operations.
     * Used to save sample leads to the database.
     */
    @Autowired
    private LeadRepository leadRepository;
    
    /**
     * Repository for task data operations.
     * Used to save sample tasks to the database.
     */
    @Autowired
    private TaskRepository taskRepository;
    
    /**
     * Repository for user settings data operations.
     * Used to save default user settings to the database.
     */
    @Autowired
    private UserSettingsRepository userSettingsRepository;
    
    /**
     * Password encoder for secure password storage.
     * Used to hash passwords before storing them in the database.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Executes data initialization logic when the application starts.
     * 
     * This method is automatically called by Spring Boot after the application
     * context is fully initialized. It performs the following operations:
     * <ol>
     *   <li>Checks if default users already exist</li>
     *   <li>Creates administrator user if not present</li>
     *   <li>Creates manager user if not present</li>
     *   <li>Creates sales representative user if not present</li>
     *   <li>Logs the initialization results</li>
     * </ol>
     * 
     * The method uses a safe approach that only creates users if they
     * don't already exist, preventing duplicate user creation on
     * subsequent application restarts.
     * 
     * @param args Command line arguments passed to the application
     * @throws Exception if initialization fails
     */
    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
        initializeCustomers();
        initializeLeads();
        initializeTasks();
        initializeUserSettings();
    }
    
    /**
     * Initializes default users in the system.
     * 
     * This method creates the essential user accounts needed for the
     * CRM system to function properly. Each user is created with:
     * <ul>
     *   <li>Unique username and email</li>
     *   <li>Encrypted password</li>
     *   <li>Appropriate role assignment</li>
     *   <li>Active status</li>
     * </ul>
     * 
     * The method checks for existing users before creation to avoid
     * conflicts and duplicate entries.
     */
    private void initializeUsers() {
        // Create ADMIN user if not exists
        if (!userRepository.existsByUsername("admin")) {
            User adminUser = new User(
                "admin",
                "admin@crm.com",
                passwordEncoder.encode("admin123"),
                "System",
                "Administrator",
                User.UserRole.ADMIN
            );
            adminUser.setActive(true);
            userRepository.save(adminUser);
            System.out.println("✅ Admin user created successfully");
        } else {
            System.out.println("ℹ️ Admin user already exists");
        }
        
        // Create MANAGER user if not exists
        if (!userRepository.existsByUsername("manager")) {
            User managerUser = new User(
                "manager",
                "manager@crm.com",
                passwordEncoder.encode("manager123"),
                "John",
                "Manager",
                User.UserRole.MANAGER
            );
            managerUser.setActive(true);
            userRepository.save(managerUser);
            System.out.println("✅ Manager user created successfully");
        } else {
            System.out.println("ℹ️ Manager user already exists");
        }
        
        // Create SALES_REP user if not exists
        if (!userRepository.existsByUsername("salesrep")) {
            User salesRepUser = new User(
                "salesrep",
                "salesrep@crm.com",
                passwordEncoder.encode("sales123"),
                "Jane",
                "SalesRep",
                User.UserRole.SALES_REP
            );
            salesRepUser.setActive(true);
            userRepository.save(salesRepUser);
            System.out.println("✅ Sales Representative user created successfully");
        } else {
            System.out.println("ℹ️ Sales Representative user already exists");
        }
        
        System.out.println("🚀 Data initialization completed successfully!");
    }
    
    /**
     * Initializes sample customers in the system.
     * 
     * This method creates realistic customer profiles across different
     * industries and company sizes to demonstrate the CRM capabilities.
     */
    private void initializeCustomers() {
        if (customerRepository.count() > 0) {
            System.out.println("ℹ️ Customers already exist, skipping initialization");
            return;
        }
        
        List<Customer> customers = Arrays.asList(
            // Enterprise Customers
            createCustomer("TechCorp Solutions", "David Wilson", "david.wilson@techcorp.com", 
                "+1-555-0101", "Technology", "Enterprise", new BigDecimal("25000000"), 
                "www.techcorp.com", "123 Tech Street, Silicon Valley, CA 94025", 
                "Major enterprise client, interested in AI solutions"),

            createCustomer("Global Manufacturing Inc.", "Lisa Thompson", "lisa.thompson@globalmfg.com", 
                "+1-555-0102", "Manufacturing", "Enterprise", new BigDecimal("15000000"), 
                "www.globalmfg.com", "456 Industrial Blvd, Detroit, MI 48201", 
                "Looking for supply chain optimization"),

            // Mid-Market Customers
            createCustomer("Healthcare Plus", "Dr. Robert Martinez", "robert.martinez@healthcareplus.com", 
                "+1-555-0103", "Healthcare", "Mid-Market", new BigDecimal("8000000"), 
                "www.healthcareplus.com", "789 Medical Center Dr, Boston, MA 02115", 
                "Healthcare provider seeking patient management system"),

            createCustomer("Green Energy Solutions", "Jennifer Lee", "jennifer.lee@greenenergy.com", 
                "+1-555-0104", "Energy", "Mid-Market", new BigDecimal("12000000"), 
                "www.greenenergy.com", "321 Renewable Way, Austin, TX 73301", 
                "Solar energy company expanding operations"),

            // Small Business Customers
            createCustomer("Local Restaurant Chain", "Carlos Rodriguez", "carlos@localrestaurant.com", 
                "+1-555-0105", "Food & Beverage", "Small Business", new BigDecimal("2500000"), 
                "www.localrestaurant.com", "654 Food Court Ave, Miami, FL 33101", 
                "Chain of 15 restaurants, need POS integration"),

            createCustomer("Creative Design Studio", "Amanda Foster", "amanda@creativedesign.com", 
                "+1-555-0106", "Creative Services", "Small Business", new BigDecimal("1800000"), 
                "www.creativedesign.com", "987 Art District St, Portland, OR 97201", 
                "Design agency needing project management tools")
        );

        customerRepository.saveAll(customers);
        System.out.println("✅ " + customers.size() + " sample customers created successfully");
    }
    
    /**
     * Creates a customer with the specified details.
     * 
     * @param companyName The company name
     * @param contactPerson The contact person name
     * @param email The contact email
     * @param phone The contact phone number
     * @param industry The company industry
     * @param size The company size category
     * @param annualRevenue The annual revenue
     * @param website The company website
     * @param address The company address
     * @param notes Additional notes about the customer
     * @return The created Customer entity
     */
    private Customer createCustomer(String companyName, String contactPerson, String email, 
                                  String phone, String industry, String size, 
                                  BigDecimal annualRevenue, String website, String address, String notes) {
        Customer customer = new Customer();
        customer.setCompanyName(companyName);
        customer.setContactPerson(contactPerson);
        customer.setEmail(email);
        customer.setPhoneNumber(phone);
        customer.setIndustry(industry);
        customer.setCustomerType(Customer.CustomerType.CUSTOMER);
        customer.setStatus(Customer.CustomerStatus.ACTIVE);
        customer.setAnnualRevenue(annualRevenue.doubleValue());
        customer.setWebsite(website);
        customer.setAddress(address);
        customer.setNotes(notes);
        return customer;
    }
    
    /**
     * Initializes sample leads in the system.
     * 
     * This method creates realistic lead profiles with different
     * priorities, sources, and statuses to demonstrate lead management.
     */
    private void initializeLeads() {
        if (leadRepository.count() > 0) {
            System.out.println("ℹ️ Leads already exist, skipping initialization");
            return;
        }
        
        List<Lead> leads = Arrays.asList(
            // Hot Leads
            createLead("Innovation Tech Startup", "AI startup with Series A funding, looking for CRM solution",
                Lead.LeadSource.WEBSITE, Lead.LeadStatus.QUALIFIED, new BigDecimal("500000"), "salesrep"),

            createLead("Financial Services Group", "Regional bank expanding to digital banking",
                Lead.LeadSource.REFERRAL, Lead.LeadStatus.QUALIFIED, new BigDecimal("750000"), "salesrep"),

            // Warm Leads
            createLead("E-commerce Retailer", "Online retailer with 100K+ customers",
                Lead.LeadSource.SOCIAL_MEDIA, Lead.LeadStatus.CONTACTED, new BigDecimal("300000"), "salesrep"),

            createLead("Educational Institute", "University seeking student management system",
                Lead.LeadSource.TRADE_SHOW, Lead.LeadStatus.CONTACTED, new BigDecimal("400000"), "salesrep"),

            // Cold Leads
            createLead("Real Estate Development", "Property development company with 50+ projects",
                Lead.LeadSource.PHONE, Lead.LeadStatus.NEW, new BigDecimal("200000"), "salesrep"),

            createLead("Logistics Company", "Transportation company with 200+ vehicles",
                Lead.LeadSource.EMAIL, Lead.LeadStatus.NEW, new BigDecimal("250000"), "salesrep")
        );

        leadRepository.saveAll(leads);
        System.out.println("✅ " + leads.size() + " sample leads created successfully");
    }
    
    /**
     * Creates a lead with the specified details.
     * 
     * @param companyName The company name
     * @param notes Additional notes about the lead
     * @param source The lead source
     * @param status The lead status
     * @param estimatedValue The estimated deal value
     * @param assignedTo The username of the assigned sales rep
     * @return The created Lead entity
     */
    private Lead createLead(String companyName, String notes, Lead.LeadSource source, 
                           Lead.LeadStatus status, BigDecimal estimatedValue, String assignedTo) {
        Lead lead = new Lead();
        lead.setTitle(companyName + " - Lead");
        lead.setDescription("Lead for " + companyName);
        lead.setLeadSource(source);
        lead.setLeadStatus(status);
        lead.setExpectedValue(estimatedValue);
        lead.setNotes(notes);
        
        // Set assignedTo to the appropriate user if username exists
        if (assignedTo != null && !assignedTo.isEmpty()) {
            User assignedUser = userRepository.findByUsername(assignedTo).orElse(null);
            if (assignedUser != null) {
                lead.setAssignedTo(assignedUser);
            }
        }
        
        return lead;
    }
    
    /**
     * Initializes sample tasks in the system.
     * 
     * This method creates realistic task profiles with different
     * priorities, types, and due dates to demonstrate task management.
     */
    private void initializeTasks() {
        if (taskRepository.count() > 0) {
            System.out.println("ℹ️ Tasks already exist, skipping initialization");
            return;
        }
        
        List<Task> tasks = Arrays.asList(
            // High Priority Tasks
            createTask("Follow up with Innovation Tech Startup", 
                "Schedule demo for AI startup team, discuss AI integration features",
                Task.TaskType.FOLLOW_UP, Task.TaskPriority.HIGH, Task.TaskStatus.IN_PROGRESS,
                LocalDate.now().plusDays(1), "salesrep", 2.0,
                "Client has Series A funding, high potential"),

            createTask("Prepare proposal for Financial Services Group",
                "Create comprehensive proposal for digital banking solution",
                Task.TaskType.PROPOSAL, Task.TaskPriority.HIGH, Task.TaskStatus.PENDING,
                LocalDate.now().plusDays(2), "salesrep", 4.0,
                "Include compliance and security features"),

            // Medium Priority Tasks
            createTask("Demo for E-commerce Retailer",
                "Showcase inventory management and customer analytics",
                Task.TaskType.PRESENTATION, Task.TaskPriority.MEDIUM, Task.TaskStatus.PENDING,
                LocalDate.now().plusDays(3), "salesrep", 1.5,
                "Focus on scalability and integration"),

            createTask("Research Educational Institute requirements",
                "Analyze needs for student management system",
                Task.TaskType.OTHER, Task.TaskPriority.MEDIUM, Task.TaskStatus.IN_PROGRESS,
                LocalDate.now().plusDays(4), "salesrep", 3.0,
                "Check compliance with educational standards"),

            // Low Priority Tasks
            createTask("Cold call Real Estate Development",
                "Initial contact to introduce our solutions",
                Task.TaskType.COLD_CALL, Task.TaskPriority.LOW, Task.TaskStatus.PENDING,
                LocalDate.now().plusDays(7), "salesrep", 1.0,
                "Research company projects first"),

            createTask("Email campaign follow-up for Logistics Company",
                "Send personalized follow-up email with case studies",
                Task.TaskType.FOLLOW_UP, Task.TaskPriority.LOW, Task.TaskStatus.PENDING,
                LocalDate.now().plusDays(5), "salesrep", 1.0,
                "Include logistics industry success stories")
        );

        taskRepository.saveAll(tasks);
        System.out.println("✅ " + tasks.size() + " sample tasks created successfully");
    }
    
    /**
     * Creates a task with the specified details.
     * 
     * @param title The task title
     * @param description The task description
     * @param taskType The type of task
     * @param priority The task priority
     * @param status The task status
     * @param dueDate The due date for the task
     * @param assignedTo The username of the assigned user
     * @param estimatedHours The estimated hours to complete
     * @param notes Additional notes about the task
     * @return The created Task entity
     */
    private Task createTask(String title, String description, Task.TaskType taskType,
                           Task.TaskPriority priority, Task.TaskStatus status,
                           LocalDate dueDate, String assignedTo, Double estimatedHours, String notes) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setTaskType(taskType);
        task.setPriority(priority);
        task.setStatus(status);
        task.setDueDate(dueDate);
        // Set assignedTo to the appropriate user if username exists
        if (assignedTo != null && !assignedTo.isEmpty()) {
            User assignedUser = userRepository.findByUsername(assignedTo).orElse(null);
            if (assignedUser != null) {
                task.setAssignedTo(assignedUser);
            }
        }
        task.setEstimatedHours(estimatedHours);
        task.setNotes(notes);
        
        // Set createdBy to the admin user (required field)
        User adminUser = userRepository.findByUsername("admin").orElse(null);
        if (adminUser != null) {
            task.setCreatedBy(adminUser);
        }
        
        return task;
    }
    
    /**
     * Initializes default user settings for all users.
     * 
     * This method creates default settings for each user including
     * notification preferences, display settings, and security options.
     */
    private void initializeUserSettings() {
        if (userSettingsRepository.count() > 0) {
            System.out.println("ℹ️ User settings already exist, skipping initialization");
            return;
        }
        
        List<User> users = userRepository.findAll();
        int settingsCreated = 0;
        
        for (User user : users) {
            if (!userSettingsRepository.existsByUserId(user.getId())) {
                UserSettings settings = new UserSettings();
                settings.setUser(user);
                
                // Set notification settings directly
                settings.setEmailNotifications(true);
                settings.setPushNotifications(true);
                settings.setSmsNotifications(false);
                settings.setMarketingNotifications(false);
                settings.setSystemNotifications(true);
                settings.setSecurityNotifications(true);
                
                // Set display settings directly
                settings.setTheme("light");
                settings.setLanguage("en");
                settings.setTimezone("UTC");
                settings.setCompactMode(false);
                settings.setShowAvatars(true);
                settings.setShowStatus(true);
                
                // Set security settings directly
                settings.setTwoFactorEnabled(false);
                settings.setSessionTimeout(30);
                settings.setRequirePasswordChange(false);
                settings.setLoginNotifications(true);
                
                // Set privacy settings directly
                settings.setProfileVisibility("team");
                settings.setActivityVisibility("team");
                settings.setDataSharing(false);
                
                userSettingsRepository.save(settings);
                settingsCreated++;
            }
        }
        
        if (settingsCreated > 0) {
            System.out.println("✅ " + settingsCreated + " user settings created successfully");
        } else {
            System.out.println("ℹ️ All users already have settings");
        }
    }
}
