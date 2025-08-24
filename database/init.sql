-- CRM System Database Initialization Script
-- PostgreSQL

-- Create database
CREATE DATABASE crm_db;

-- Connect to the database
\c crm_db;

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'MANAGER', 'SALES_REP')),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create customers table
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    website VARCHAR(255),
    customer_type VARCHAR(20) NOT NULL CHECK (customer_type IN ('PROSPECT', 'CUSTOMER', 'PARTNER', 'VENDOR')),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED')),
    industry VARCHAR(100),
    annual_revenue DECIMAL(15,2),
    employee_count INTEGER,
    address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    notes TEXT,
    assigned_to BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create leads table
CREATE TABLE leads (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    lead_source VARCHAR(20) NOT NULL CHECK (lead_source IN ('WEBSITE', 'REFERRAL', 'SOCIAL_MEDIA', 'EMAIL', 'PHONE', 'TRADE_SHOW', 'OTHER')),
    lead_status VARCHAR(20) NOT NULL DEFAULT 'NEW' CHECK (lead_status IN ('NEW', 'CONTACTED', 'QUALIFIED', 'PROPOSAL', 'NEGOTIATION', 'CLOSED_WON', 'CLOSED_LOST')),
    expected_value DECIMAL(15,2) CHECK (expected_value > 0),
    probability_percentage INTEGER CHECK (probability_percentage >= 0 AND probability_percentage <= 100),
    expected_close_date TIMESTAMP,
    customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    assigned_to BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

-- Create contacts table
CREATE TABLE contacts (
    id BIGSERIAL PRIMARY KEY,
    contact_type VARCHAR(20) NOT NULL CHECK (contact_type IN ('PHONE_CALL', 'EMAIL', 'MEETING', 'DEMO', 'PRESENTATION', 'FOLLOW_UP', 'OTHER')),
    subject VARCHAR(255) NOT NULL,
    description TEXT,
    contact_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    next_follow_up TIMESTAMP,
    customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

-- Create tasks table
CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    task_type VARCHAR(20) NOT NULL CHECK (task_type IN ('FOLLOW_UP', 'DEMO', 'PRESENTATION', 'PROPOSAL', 'NEGOTIATION', 'CLOSING', 'OTHER')),
    task_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (task_status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    priority VARCHAR(10) NOT NULL CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT')),
    due_date TIMESTAMP,
    completed_date TIMESTAMP,
    assigned_to BIGINT REFERENCES users(id),
    customer_id BIGINT REFERENCES customers(id),
    lead_id BIGINT REFERENCES leads(id),
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

-- Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_active ON users(is_active);

CREATE INDEX idx_customers_company_name ON customers(company_name);
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_customer_type ON customers(customer_type);
CREATE INDEX idx_customers_status ON customers(status);
CREATE INDEX idx_customers_assigned_to ON customers(assigned_to);
CREATE INDEX idx_customers_industry ON customers(industry);

CREATE INDEX idx_leads_customer_id ON leads(customer_id);
CREATE INDEX idx_leads_assigned_to ON leads(assigned_to);
CREATE INDEX idx_leads_lead_status ON leads(lead_status);
CREATE INDEX idx_leads_lead_source ON leads(lead_source);
CREATE INDEX idx_leads_expected_close_date ON leads(expected_close_date);

CREATE INDEX idx_contacts_customer_id ON contacts(customer_id);
CREATE INDEX idx_contacts_created_by ON contacts(created_by);
CREATE INDEX idx_contacts_contact_date ON contacts(contact_date);
CREATE INDEX idx_contacts_contact_type ON contacts(contact_type);

CREATE INDEX idx_tasks_assigned_to ON tasks(assigned_to);
CREATE INDEX idx_tasks_customer_id ON tasks(customer_id);
CREATE INDEX idx_tasks_lead_id ON tasks(lead_id);
CREATE INDEX idx_tasks_task_status ON tasks(task_status);
CREATE INDEX idx_tasks_priority ON tasks(priority);
CREATE INDEX idx_tasks_due_date ON tasks(due_date);

-- Create updated_at trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_customers_updated_at BEFORE UPDATE ON customers FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_leads_updated_at BEFORE UPDATE ON leads FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_contacts_updated_at BEFORE UPDATE ON contacts FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_tasks_updated_at BEFORE UPDATE ON tasks FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insert sample data
-- Sample users (passwords will be hashed by Spring Boot)
INSERT INTO users (username, email, password, first_name, last_name, role, is_active) VALUES
('admin', 'admin@crm.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Admin', 'User', 'ADMIN', true),
('manager', 'manager@crm.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Manager', 'User', 'MANAGER', true),
('salesrep', 'salesrep@crm.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Sales', 'Representative', 'SALES_REP', true);

-- Sample customers
INSERT INTO customers (company_name, contact_person, email, phone_number, website, customer_type, status, industry, annual_revenue, employee_count, address, city, state, country, postal_code, notes, assigned_to) VALUES
('TechCorp Solutions', 'John Smith', 'john@techcorp.com', '+1-555-0123', 'www.techcorp.com', 'CUSTOMER', 'ACTIVE', 'Technology', 5000000.00, 150, '123 Tech Street', 'San Francisco', 'CA', 'USA', '94105', 'Enterprise software client', 1),
('Global Industries', 'Sarah Johnson', 'sarah@global.com', '+1-555-0124', 'www.global.com', 'PROSPECT', 'ACTIVE', 'Manufacturing', 25000000.00, 500, '456 Industry Blvd', 'Chicago', 'IL', 'USA', '60601', 'Potential manufacturing client', 2),
('StartupXYZ', 'Mike Wilson', 'mike@startupxyz.com', '+1-555-0125', 'www.startupxyz.com', 'CUSTOMER', 'ACTIVE', 'Technology', 500000.00, 25, '789 Innovation Ave', 'Austin', 'TX', 'USA', '73301', 'Growing startup client', 3);

-- Sample leads
INSERT INTO leads (title, description, lead_source, lead_status, expected_value, probability_percentage, expected_close_date, customer_id, assigned_to, notes) VALUES
('Enterprise Software License', 'Large enterprise software licensing deal', 'WEBSITE', 'QUALIFIED', 100000.00, 75, '2024-03-31 00:00:00', 1, 1, 'High priority lead'),
('Manufacturing Process Optimization', 'Process optimization consulting project', 'REFERRAL', 'PROPOSAL', 250000.00, 60, '2024-04-30 00:00:00', 2, 2, 'Complex project requiring technical expertise'),
('Startup Growth Package', 'Comprehensive growth and scaling package', 'EMAIL', 'NEGOTIATION', 50000.00, 85, '2024-02-28 00:00:00', 3, 3, 'Fast-growing startup opportunity');

-- Sample contacts
INSERT INTO contacts (contact_type, subject, description, contact_date, next_follow_up, customer_id, created_by, notes) VALUES
('MEETING', 'Initial Discovery Call', 'Discussed company needs and potential solutions', '2024-01-15 10:00:00', '2024-01-22 10:00:00', 1, 1, 'Client showed strong interest'),
('DEMO', 'Product Demonstration', 'Demonstrated key features and capabilities', '2024-01-20 14:00:00', '2024-01-27 14:00:00', 2, 2, 'Demo went well, client engaged'),
('FOLLOW_UP', 'Proposal Discussion', 'Discussed proposal details and pricing', '2024-01-25 11:00:00', '2024-02-01 11:00:00', 3, 3, 'Client reviewing proposal');

-- Sample tasks
INSERT INTO tasks (title, description, task_type, task_status, priority, due_date, assigned_to, customer_id, created_by, notes) VALUES
('Follow up on proposal', 'Call client to discuss proposal feedback', 'FOLLOW_UP', 'PENDING', 'HIGH', '2024-01-30 09:00:00', 1, 1, 1, 'Urgent follow-up required'),
('Prepare technical specifications', 'Create detailed technical specifications document', 'PROPOSAL', 'IN_PROGRESS', 'MEDIUM', '2024-02-05 17:00:00', 2, 2, 2, 'Complex technical requirements'),
('Schedule demo', 'Arrange product demonstration for client team', 'DEMO', 'PENDING', 'MEDIUM', '2024-02-02 14:00:00', 3, 3, 3, 'Client requested team demo');

-- Grant permissions (adjust as needed for your environment)
GRANT ALL PRIVILEGES ON DATABASE crm_db TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;

-- Display table information
\dt

-- Display sample data
SELECT 'Users' as table_name, count(*) as record_count FROM users
UNION ALL
SELECT 'Customers', count(*) FROM customers
UNION ALL
SELECT 'Leads', count(*) FROM leads
UNION ALL
SELECT 'Contacts', count(*) FROM contacts
UNION ALL
SELECT 'Tasks', count(*) FROM tasks;
