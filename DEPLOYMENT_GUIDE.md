# 🚀 **TechCRM Deployment Guide**

## 📋 **Overview**

This guide provides step-by-step instructions for deploying TechCRM to production environments. The system consists of a Spring Boot backend and a React frontend, both designed for cloud deployment.

---

## 🏗️ **Architecture Overview**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │   Database      │
│   (React)       │◄──►│  (Spring Boot)  │◄──►│  (PostgreSQL)   │
│   [Vercel]      │    │   [Render]      │    │   [Render]      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 🔧 **Backend Deployment (Render)**

### **Prerequisites**
- GitHub repository with backend code
- Render account (free tier available)
- PostgreSQL database (Render provides this)

### **Step 1: Prepare Backend for Deployment**

#### **1.1 Update application.yml for Production**
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: ${DATABASE_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: false
        show_sql: false

server:
  port: ${PORT:8080}
  servlet:
    context-path: /api

# JWT Configuration
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400000 # 24 hours

# CORS Configuration
cors:
  allowed-origins: ${FRONTEND_URL:https://your-app.vercel.app}
```

#### **1.2 Create Dockerfile**
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### **1.3 Create .dockerignore**
```
target/
!target/*.jar
.git
.gitignore
README.md
```

### **Step 2: Deploy to Render**

#### **2.1 Create New Web Service**
1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click "New +" → "Web Service"
3. Connect your GitHub repository

#### **2.2 Configure Service Settings**
```
Name: techcrm-backend
Environment: Java
Build Command: mvn clean package -DskipTests
Start Command: java -jar target/crm-0.0.1-SNAPSHOT.jar
```

#### **2.3 Set Environment Variables**
```
DATABASE_URL=postgresql://username:password@host:port/database
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
JWT_SECRET=your_super_secret_jwt_key_here
FRONTEND_URL=https://your-frontend-app.vercel.app
```

#### **2.4 Deploy**
1. Click "Create Web Service"
2. Wait for build to complete
3. Note the service URL (e.g., `https://techcrm-backend.onrender.com`)

---

## 🌐 **Frontend Deployment (Vercel)**

### **Prerequisites**
- GitHub repository with frontend code
- Vercel account (free tier available)
- Backend API URL from Render

### **Step 1: Prepare Frontend for Deployment**

#### **1.1 Create Environment Variables**
Create `.env.production` file:
```env
REACT_APP_API_URL=https://your-backend-url.onrender.com/api
REACT_APP_ENVIRONMENT=production
```

#### **1.2 Update API Configuration**
Ensure your `apiService.js` uses the environment variable:
```javascript
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
```

#### **1.3 Build Optimization**
Update `package.json` scripts:
```json
{
  "scripts": {
    "build": "GENERATE_SOURCEMAP=false react-scripts build",
    "build:prod": "GENERATE_SOURCEMAP=false react-scripts build"
  }
}
```

### **Step 2: Deploy to Vercel**

#### **2.1 Connect Repository**
1. Go to [Vercel Dashboard](https://vercel.com/dashboard)
2. Click "New Project"
3. Import your GitHub repository

#### **2.2 Configure Build Settings**
```
Framework Preset: Create React App
Build Command: npm run build
Output Directory: build
Install Command: npm install
```

#### **2.3 Set Environment Variables**
```
REACT_APP_API_URL=https://your-backend-url.onrender.com/api
REACT_APP_ENVIRONMENT=production
```

#### **2.4 Deploy**
1. Click "Deploy"
2. Wait for build to complete
3. Note your app URL (e.g., `https://techcrm.vercel.app`)

---

## 🗄️ **Database Setup (Render PostgreSQL)**

### **Step 1: Create Database**
1. In Render Dashboard, go to "New +" → "PostgreSQL"
2. Choose plan (free tier available)
3. Set database name: `techcrm_db`
4. Note connection details

### **Step 2: Initialize Database**
The application will automatically create tables on first run with `ddl-auto: update`.

### **Step 3: Verify Connection**
Check backend logs to ensure database connection is successful.

---

## 🔒 **Security Configuration**

### **JWT Secret Generation**
Generate a strong JWT secret:
```bash
# Using OpenSSL
openssl rand -base64 64

# Or using Node.js
node -e "console.log(require('crypto').randomBytes(64).toString('base64'))"
```

### **Environment Variables Security**
- Never commit secrets to version control
- Use environment variables for all sensitive data
- Rotate secrets regularly

### **CORS Configuration**
Ensure your backend CORS settings match your frontend domain:
```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "https://your-app.vercel.app",
            "http://localhost:3000" // for development
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

## 📊 **Monitoring & Health Checks**

### **Health Check Endpoint**
Your backend includes a health check endpoint:
```
GET /api/health
```

### **Render Health Checks**
Configure in Render dashboard:
```
Health Check Path: /api/health
```

### **Vercel Analytics**
Enable Vercel Analytics for frontend monitoring:
```bash
npm install @vercel/analytics
```

---

## 🚀 **Post-Deployment Verification**

### **Backend Verification**
1. **Health Check**: `GET https://your-backend.onrender.com/api/health`
2. **API Documentation**: `GET https://your-backend.onrender.com/api/swagger-ui.html`
3. **Database Connection**: Check backend logs

### **Frontend Verification**
1. **App Loading**: Visit your Vercel app URL
2. **API Connection**: Test login functionality
3. **Theme Switching**: Verify dark/light mode works
4. **Responsive Design**: Test on different screen sizes

### **Integration Testing**
1. **User Registration**: Create a new user account
2. **User Login**: Authenticate with JWT
3. **Data Creation**: Add sample customers/leads
4. **Role-Based Access**: Test different user roles

---

## 🔧 **Troubleshooting**

### **Common Backend Issues**

#### **Database Connection Failed**
```
Error: Could not create connection to database server
```
**Solution**: Check DATABASE_URL format and credentials

#### **Port Binding Issues**
```
Error: Web server failed to start. Port 8080 was already in use
```
**Solution**: Use `${PORT}` environment variable (Render sets this automatically)

#### **JWT Token Issues**
```
Error: JWT signature does not match locally computed signature
```
**Solution**: Ensure JWT_SECRET is consistent across deployments

### **Common Frontend Issues**

#### **API Calls Failing**
```
Error: Failed to fetch from API
```
**Solution**: Check REACT_APP_API_URL and CORS configuration

#### **Build Failures**
```
Error: Build failed during deployment
```
**Solution**: Test build locally with `npm run build`

---

## 📈 **Performance Optimization**

### **Backend Optimization**
1. **Database Indexing**: Ensure proper indexes on frequently queried fields
2. **Connection Pooling**: Configure optimal database connection pool size
3. **Caching**: Implement Redis for session and data caching

### **Frontend Optimization**
1. **Code Splitting**: Implement React.lazy() for route-based splitting
2. **Bundle Analysis**: Use `npm run build --analyze` to identify large packages
3. **Image Optimization**: Compress and optimize images

---

## 🔄 **Continuous Deployment**

### **Automatic Deployments**
Both Render and Vercel support automatic deployments:
- **Render**: Deploys on every push to main branch
- **Vercel**: Deploys on every push to main branch

### **Deployment Workflow**
```
1. Develop locally
2. Test with `npm test` and `mvn test`
3. Commit and push to GitHub
4. Automatic deployment to staging/production
5. Verify deployment health
```

---

## 📞 **Support & Maintenance**

### **Monitoring Tools**
- **Render**: Built-in logging and metrics
- **Vercel**: Performance analytics and error tracking
- **Application**: Custom logging with SLF4J

### **Backup Strategy**
- **Database**: Render PostgreSQL automatic backups
- **Code**: GitHub repository backup
- **Configuration**: Environment variables documentation

### **Update Process**
1. **Backend Updates**: Push to GitHub → Automatic Render deployment
2. **Frontend Updates**: Push to GitHub → Automatic Vercel deployment
3. **Database Updates**: Automatic with `ddl-auto: update`

---

## 🎯 **Next Steps**

### **Immediate Actions**
1. ✅ Deploy backend to Render
2. ✅ Deploy frontend to Vercel
3. ✅ Configure environment variables
4. ✅ Test all functionality
5. ✅ Document deployment URLs

### **Future Enhancements**
1. **Custom Domain**: Configure custom domain names
2. **SSL Certificates**: Ensure HTTPS everywhere
3. **CDN**: Implement content delivery network
4. **Monitoring**: Add advanced monitoring and alerting

---

## 📚 **Additional Resources**

- [Render Documentation](https://render.com/docs)
- [Vercel Documentation](https://vercel.com/docs)
- [Spring Boot Deployment](https://spring.io/guides/gs/spring-boot/)
- [React Deployment](https://create-react-app.dev/docs/deployment/)

---

*For technical support during deployment, refer to the application logs and this guide.*
