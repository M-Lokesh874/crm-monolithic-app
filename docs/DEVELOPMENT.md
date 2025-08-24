# 🚀 CRM System - Development Documentation

## 📋 Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture](#architecture)
3. [Setup Instructions](#setup-instructions)
4. [Development Guidelines](#development-guidelines)
5. [API Documentation](#api-documentation)
6. [Database Schema](#database-schema)
7. [Testing Strategy](#testing-strategy)
8. [Deployment Guide](#deployment-guide)
9. [Troubleshooting](#troubleshooting)

## 🎯 Project Overview

The CRM System is a comprehensive Customer Relationship Management application built with:
- **Backend**: Spring Boot 3.x with Java 17
- **Frontend**: React 18 with Tailwind CSS
- **Database**: PostgreSQL
- **Security**: JWT + Spring Security + RBAC
- **Deployment**: Render (Backend) + Vercel (Frontend)

### Core Features
- User authentication and role-based access control
- Customer management with detailed profiles
- Sales pipeline tracking (leads, opportunities)
- Contact history and communication tracking
- Task management and follow-up scheduling
- Analytics and reporting dashboard

## 🏗️ Architecture

### System Architecture
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React Frontend│    │  Spring Boot   │    │   PostgreSQL    │
│   (Vercel)      │◄──►│   Backend      │◄──►│   Database      │
│                 │    │   (Render)      │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Backend Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
├─────────────────────────────────────────────────────────────┤
│  Controllers (REST APIs)                                   │
├─────────────────────────────────────────────────────────────┤
│                    Business Layer                           │
├─────────────────────────────────────────────────────────────┤
│  Services (Business Logic)                                 │
├─────────────────────────────────────────────────────────────┤
│                    Data Access Layer                        │
├─────────────────────────────────────────────────────────────┤
│  Repositories (Data Access)                                │
├─────────────────────────────────────────────────────────────┤
│                    Data Layer                               │
├─────────────────────────────────────────────────────────────┤
│  Entities (JPA Models)                                     │
└─────────────────────────────────────────────────────────────┘
```

### Frontend Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                    Pages                                    │
├─────────────────────────────────────────────────────────────┤
│  Dashboard, Customers, Users, etc.                         │
├─────────────────────────────────────────────────────────────┤
│                    Components                               │
├─────────────────────────────────────────────────────────────┤
│  Layout, Forms, Tables, etc.                               │
├─────────────────────────────────────────────────────────────┤
│                    Services                                 │
├─────────────────────────────────────────────────────────────┤
│  API calls, Authentication                                 │
├─────────────────────────────────────────────────────────────┤
│                    Context                                  │
├─────────────────────────────────────────────────────────────┤
│  State Management (Auth, etc.)                             │
└─────────────────────────────────────────────────────────────┘
```

## 🛠️ Setup Instructions

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 13+
- Maven 3.6+
- Git

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd crm-system/backend
   ```

2. **Database Setup**
   ```bash
   # Start PostgreSQL
   # Create database
   psql -U postgres
   CREATE DATABASE crm_db;
   
   # Run initialization script
   psql -U postgres -d crm_db -f ../../database/init.sql
   ```

3. **Configure Application**
   - Update `application.yml` with your database credentials
   - Set JWT secret and other environment variables

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

5. **Verify Setup**
   - Backend: http://localhost:8080/api
   - Swagger UI: http://localhost:8080/api/swagger-ui.html

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd ../frontend
   ```

2. **Install Dependencies**
   ```bash
   npm install
   ```

3. **Configure Environment**
   - Update API base URL if needed
   - Set environment variables

4. **Run the Application**
   ```bash
   npm start
   ```

5. **Verify Setup**
   - Frontend: http://localhost:3000
   - Login with demo credentials

## 📝 Development Guidelines

### Backend Development

#### Code Structure
```
src/main/java/com/crm/
├── controller/     # REST API endpoints
├── service/        # Business logic
├── repository/     # Data access layer
├── entity/         # JPA entities
├── dto/           # Data transfer objects
├── security/      # Security configuration
└── config/        # Application configuration
```

#### Naming Conventions
- **Classes**: PascalCase (e.g., `CustomerService`)
- **Methods**: camelCase (e.g., `getCustomerById`)
- **Variables**: camelCase (e.g., `customerName`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_COUNT`)
- **Packages**: lowercase (e.g., `com.crm.service`)

#### Best Practices
- Use DTOs for API requests/responses
- Implement proper exception handling
- Add comprehensive logging
- Write unit tests for services
- Use validation annotations
- Follow Spring Boot conventions

#### Security Guidelines
- Always validate input data
- Use parameterized queries
- Implement proper authentication
- Apply role-based access control
- Log security events
- Use HTTPS in production

### Frontend Development

#### Code Structure
```
src/
├── components/     # Reusable components
├── pages/         # Page components
├── services/      # API services
├── context/       # React context
├── hooks/         # Custom hooks
└── utils/         # Utility functions
```

#### Naming Conventions
- **Components**: PascalCase (e.g., `CustomerList`)
- **Files**: PascalCase (e.g., `CustomerForm.js`)
- **Functions**: camelCase (e.g., `handleSubmit`)
- **Variables**: camelCase (e.g., `customerData`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `API_BASE_URL`)

#### Best Practices
- Use functional components with hooks
- Implement proper error handling
- Add loading states
- Use TypeScript for type safety
- Follow React best practices
- Implement responsive design

#### UI/UX Guidelines
- Use consistent color scheme
- Implement proper spacing
- Add loading indicators
- Provide user feedback
- Ensure accessibility
- Test on multiple devices

## 📱 API Documentation

### Authentication Endpoints

#### POST /api/auth/login
**Description**: User authentication
**Request Body**:
```json
{
  "username": "string",
  "password": "string"
}
```
**Response**:
```json
{
  "token": "string",
  "tokenType": "Bearer",
  "userId": "number",
  "username": "string",
  "email": "string",
  "firstName": "string",
  "lastName": "string",
  "role": "string",
  "expiresAt": "string"
}
```

#### POST /api/auth/validate
**Description**: Validate JWT token
**Headers**: `Authorization: Bearer <token>`
**Response**: `boolean`

### Customer Endpoints

#### GET /api/customers
**Description**: Get all customers
**Headers**: `Authorization: Bearer <token>`
**Response**: `CustomerDTO[]`

#### POST /api/customers
**Description**: Create new customer
**Headers**: `Authorization: Bearer <token>`
**Request Body**: `Customer` entity
**Response**: `CustomerDTO`

#### PUT /api/customers/{id}
**Description**: Update customer
**Headers**: `Authorization: Bearer <token>`
**Request Body**: `Customer` entity
**Response**: `CustomerDTO`

#### DELETE /api/customers/{id}
**Description**: Delete customer (soft delete)
**Headers**: `Authorization: Bearer <token>`
**Response**: `200 OK`

### User Endpoints

#### GET /api/users
**Description**: Get all users (Admin only)
**Headers**: `Authorization: Bearer <token>`
**Response**: `UserDTO[]`

#### POST /api/users
**Description**: Create new user (Admin only)
**Headers**: `Authorization: Bearer <token>`
**Request Body**: `User` entity
**Response**: `UserDTO`

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Customers Table
```sql
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    website VARCHAR(255),
    customer_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
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
```

### Leads Table
```sql
CREATE TABLE leads (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    lead_source VARCHAR(20) NOT NULL,
    lead_status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    expected_value DECIMAL(15,2),
    probability_percentage INTEGER,
    expected_close_date TIMESTAMP,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    assigned_to BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);
```

## 🧪 Testing Strategy

### Backend Testing
- **Unit Tests**: Test individual services and components
- **Integration Tests**: Test API endpoints and database operations
- **Security Tests**: Test authentication and authorization
- **Performance Tests**: Test response times and throughput

### Frontend Testing
- **Component Tests**: Test individual React components
- **Integration Tests**: Test component interactions
- **E2E Tests**: Test complete user workflows
- **Accessibility Tests**: Ensure compliance with WCAG guidelines

### Test Coverage Goals
- Backend: 80%+ code coverage
- Frontend: 70%+ code coverage
- Critical paths: 100% coverage

## 🚀 Deployment Guide

### Backend Deployment (Render)

1. **Connect Repository**
   - Link your GitHub repository to Render
   - Set build command: `mvn clean package`
   - Set start command: `java -jar target/crm-system-1.0.0.jar`

2. **Environment Variables**
   ```
   SPRING_PROFILES_ACTIVE=production
   SPRING_DATASOURCE_URL=<production-db-url>
   SPRING_DATASOURCE_USERNAME=<db-username>
   SPRING_DATASOURCE_PASSWORD=<db-password>
   JWT_SECRET=<secure-jwt-secret>
   ```

3. **Database Setup**
   - Create production PostgreSQL database
   - Run migration scripts
   - Update connection strings

### Frontend Deployment (Vercel)

1. **Connect Repository**
   - Link your GitHub repository to Vercel
   - Set build command: `npm run build`
   - Set output directory: `build`

2. **Environment Variables**
   ```
   REACT_APP_API_URL=<backend-api-url>
   ```

3. **Build Configuration**
   - Ensure all dependencies are in package.json
   - Test build locally before deployment

## 🔧 Troubleshooting

### Common Issues

#### Backend Issues
1. **Database Connection Failed**
   - Check database credentials
   - Verify database is running
   - Check network connectivity

2. **JWT Token Issues**
   - Verify JWT secret is set
   - Check token expiration
   - Validate token format

3. **CORS Issues**
   - Check CORS configuration
   - Verify allowed origins
   - Test with Postman

#### Frontend Issues
1. **API Calls Failing**
   - Check API base URL
   - Verify authentication token
   - Check network tab for errors

2. **Build Failures**
   - Clear node_modules and reinstall
   - Check for syntax errors
   - Verify all dependencies

3. **Authentication Issues**
   - Clear localStorage
   - Check token expiration
   - Verify login endpoint

### Debug Tips
- Use browser developer tools
- Check application logs
- Test APIs with Postman
- Verify database connections
- Check environment variables

### Performance Optimization
- Enable database connection pooling
- Implement caching strategies
- Optimize database queries
- Use CDN for static assets
- Implement lazy loading

## 📚 Additional Resources

### Documentation
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [React Documentation](https://reactjs.org/docs/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Tailwind CSS Documentation](https://tailwindcss.com/docs)

### Tools
- [Postman](https://www.postman.com/) - API testing
- [pgAdmin](https://www.pgadmin.org/) - Database management
- [VS Code](https://code.visualstudio.com/) - Development IDE
- [Git](https://git-scm.com/) - Version control

### Best Practices
- [REST API Design](https://restfulapi.net/)
- [JWT Best Practices](https://auth0.com/blog/a-look-at-the-latest-draft-for-jwt-bcp/)
- [React Patterns](https://reactpatterns.com/)
- [Database Design](https://www.postgresql.org/docs/current/ddl.html)

---

**Happy Coding! 🎉**
