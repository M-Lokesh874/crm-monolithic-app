# 🏢 CRM System - Customer Relationship Management

A comprehensive CRM system built with Spring Boot backend and React frontend, featuring customer management, sales pipeline tracking, and performance analytics.

## 🏗️ System Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   React Frontend│    │  Spring Boot   │    │   PostgreSQL    │
│   (Vercel)      │◄──►│   Backend      │◄──►│   Database      │
│                 │    │   (Render)      │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🎯 Core Features

- **Customer Management**: Add, edit, view customer profiles
- **Contact History**: Track interactions and communications
- **Sales Pipeline**: Manage leads through stages (Lead → Qualified → Proposal → Closed)
- **Task Management**: Schedule follow-ups and reminders
- **Basic Reporting**: Customer acquisition, sales conversion rates
- **Role-Based Access Control**: Secure user management

## 🛠️ Technology Stack

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Security**: Spring Security + JWT + RBAC
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Deployment**: Render

### Frontend
- **Library**: React 18
- **State Management**: React Context + Hooks
- **UI Framework**: Tailwind CSS
- **HTTP Client**: Axios
- **Deployment**: Vercel

## 📁 Project Structure

```
crm-system/
├── backend/                 # Spring Boot application
│   ├── src/main/java/
│   │   └── com/crm/
│   │       ├── controller/  # REST controllers
│   │       ├── service/     # Business logic
│   │       ├── repository/  # Data access
│   │       ├── entity/      # JPA entities
│   │       ├── dto/         # Data transfer objects
│   │       ├── security/    # Security configuration
│   │       └── config/      # Application config
│   ├── src/main/resources/
│   └── pom.xml
├── frontend/                # React application
│   ├── src/
│   │   ├── components/      # Reusable components
│   │   ├── pages/          # Page components
│   │   ├── services/       # API services
│   │   ├── context/        # React context
│   │   ├── hooks/          # Custom hooks
│   │   └── utils/          # Utility functions
│   ├── public/
│   └── package.json
├── docs/                    # Documentation
├── database/                # Database scripts
└── README.md
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 13+
- Maven 3.6+
- Git

### Backend Setup
```bash
cd backend
mvn spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm start
```

## 📊 Database Schema

### Core Tables
- `users` - User accounts and roles
- `customers` - Customer information
- `contacts` - Contact history
- `leads` - Sales leads
- `tasks` - Follow-up tasks
- `opportunities` - Sales opportunities

## 🔐 Security Features

- JWT-based authentication
- Role-based access control (Admin, Manager, Sales Rep)
- Password encryption
- API endpoint protection
- CORS configuration

## 📱 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Customers
- `GET /api/customers` - List customers
- `POST /api/customers` - Create customer
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

### Leads
- `GET /api/leads` - List leads
- `POST /api/leads` - Create lead
- `PUT /api/leads/{id}/status` - Update lead status

## 🎨 UI/UX Guidelines

- **Color Scheme**: Professional blues and grays
- **Typography**: Clean, readable fonts
- **Layout**: Responsive grid system
- **Components**: Reusable, consistent design
- **Navigation**: Intuitive sidebar navigation

## 📝 Development Guidelines

### Backend
- Follow Spring Boot best practices
- Use DTOs for API requests/responses
- Implement proper exception handling
- Add comprehensive logging
- Write unit tests for services

### Frontend
- Use functional components with hooks
- Implement proper error handling
- Add loading states
- Use TypeScript for type safety
- Follow React best practices

## 🧪 Testing Strategy

- **Backend**: Unit tests with JUnit, integration tests
- **Frontend**: Component testing with React Testing Library
- **API**: Postman collections for manual testing
- **E2E**: Basic user flow testing

## 🚀 Deployment

### Backend (Render)
- Connect GitHub repository
- Set environment variables
- Auto-deploy on push to main

### Frontend (Vercel)
- Connect GitHub repository
- Set build commands
- Auto-deploy on push to main

## 📚 Documentation

- API documentation with Swagger
- Database schema documentation
- User manual
- Developer setup guide
- Deployment guide

## 🤝 Contributing

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create pull request

## 📄 License

This project is for assessment purposes only.

---

**Built with ❤️ using Spring Boot + React**
