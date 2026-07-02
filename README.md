# Spring Boot 3 Account Management System with JWT Security

> **Educational Project**: This project was developed to help students understand the basics of Spring Security, JWT authentication, and role-based access control in Spring Boot 3.

A comprehensive Account Management System (AMS) demonstrating modern Spring Boot 3 security practices with JWT (JSON Web Token) authentication, role-based authorization, and RESTful API implementation.

## 🎯 Project Overview

This is a full-stack backend application that demonstrates:

- **JWT-based Authentication**: Secure user authentication using JSON Web Tokens
- **Role-Based Access Control (RBAC)**: Multiple user roles with different access levels
- **Account Management**: Complete CRUD operations for user accounts
- **Transaction Processing**: Deposit, withdrawal, and purchase transactions
- **Audit Trail**: JPA Auditing for tracking data changes
- **API Documentation**: Swagger/OpenAPI integration

## ✨ Features

### Authentication & Authorization
- ✅ User registration with role assignment
- ✅ JWT-based authentication
- ✅ Refresh token mechanism
- ✅ Secure logout functionality
- ✅ Password encryption using BCrypt
- ✅ Role-based endpoint protection

### Account Management
- ✅ Create, read, update, and delete accounts
- ✅ Account balance tracking
- ✅ Account-user relationship management

### Transaction Management
- ✅ Deposit funds to accounts
- ✅ Withdraw funds from accounts
- ✅ Purchase products with transaction validation
- ✅ Transaction history tracking
- ✅ Deposit history management
- ✅ Movement history logging
- ✅ PDF generation for transactions

### User Management
- ✅ User profile management
- ✅ Password change functionality
- ✅ Role-based user listing
- ✅ Customer management

### Additional Features
- ✅ JPA Auditing (created/modified timestamps)
- ✅ Swagger/OpenAPI documentation
- ✅ RESTful API design
- ✅ Exception handling
- ✅ Input validation

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17+ | Programming Language |
| **Spring Boot** | 3.1.4 | Framework |
| **Spring Security** | 6.x | Security Framework |
| **Spring Data JPA** | - | Data Persistence |
| **JWT (jjwt)** | 0.11.5 | Token Management |
| **MySQL** | - | Database |
| **Lombok** | - | Boilerplate Reduction |
| **Swagger/OpenAPI** | 2.1.0 | API Documentation |
| **iTextPDF** | 5.5.13.3 | PDF Generation |
| **Maven** | 3+ | Build Tool |

## 📋 Prerequisites

Before running this project, ensure you have the following installed:

- **JDK 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+** (or compatible database)
- **Git** (for cloning the repository)
- An IDE like IntelliJ IDEA, Eclipse, or VS Code (recommended)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/engr-muhammad-mansoor/User-Account-Management-System.git
cd User-Account-Management-System
```

### 2. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE account_management;
```

Or update the database name in `application.yml` to match your existing database.

### 3. Configure Application Properties

Update `src/main/resources/application.yml` with your database credentials:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/account_management
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Or run the `SecurityApplication.java` class directly from your IDE.

### 6. Access the Application

- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs

## 🔐 User Roles

The system supports the following roles with different access levels:

| Role | Description | Access Level |
|------|-------------|--------------|
| **ADMIN** | System Administrator | Full access to all endpoints |
| **SYSTEM_USER** | System User | Access to system-user and customer endpoints |
| **WORKER** | Worker | Limited access (can be configured) |
| **SIMPLE_EMPLOYEE** | Employee | Limited access (can be configured) |
| **CUSTOMER** | Customer | Basic access (can be configured) |

### Endpoint Access Rules

- **Public Endpoints**: `/api/v1/auth/**`, `/swagger-ui/**`
- **Admin Only**: `/api/admin/**`
- **System Users**: `/api/system-users/**` (ADMIN + SYSTEM_USER)
- **Authenticated Users**: All other endpoints require valid JWT token

## 📚 API Endpoints

### Authentication Endpoints (`/api/v1/auth`)

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/register` | Register a new user | Public |
| POST | `/authenticate` | Login and get JWT token | Public |
| POST | `/refresh-token` | Refresh access token | Public |
| POST | `/logout` | Logout user | Authenticated |

### User Management Endpoints (`/api`)

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| PATCH | `/system-users/change-password` | Change user password | Authenticated |
| GET | `/admin/get-all-users/` | Get all users | Admin |
| GET | `/admin/get-all-customers/` | Get all customers | Admin |
| GET | `/system-users/find-user/{id}` | Find user by ID | System User |

### Account Management Endpoints (`/api`)

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/admin/get-all-accounts/` | Get all accounts | Admin |
| GET | `/system-users/find-account/{id}` | Find account by ID | System User |
| POST | `/system-users/add-account/` | Create new account | System User |
| PUT | `/system-users/update-account/{id}` | Update account | System User |
| DELETE | `/system-users/delete-account/{id}` | Delete account | System User |

### Transaction Endpoints (`/api`)

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/system-user/deposit` | Deposit funds | System User |
| POST | `/system-user/withdraw` | Withdraw funds | System User |
| POST | `/system-user/purchase` | Purchase product | System User |
| GET | `/system-user/generate-pdf-transaction` | Generate transaction PDF | System User |
| GET | `/system-user/generate-pdf-purchase` | Generate purchase PDF | System User |

### History Endpoints

- Deposit History endpoints
- Movement History endpoints

> **Note**: Check Swagger UI for complete API documentation with request/response schemas.

## 📝 Example API Usage

### 1. Register a User

```http
POST http://localhost:8080/api/v1/auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "role": "ADMIN"
}
```

### 2. Authenticate (Login)

```http
POST http://localhost:8080/api/v1/auth/authenticate
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3. Access Protected Endpoint

```http
GET http://localhost:8080/api/admin/get-all-users/
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 🔧 Configuration

### JWT Configuration

JWT settings can be configured in `application.yml`:

```yaml
application:
  security:
    jwt:
      secret-key: <your-secret-key>
      expiration: 86400000  # 24 hours in milliseconds
      refresh-token:
        expiration: 604800000  # 7 days in milliseconds
```

### Database Configuration

The application uses JPA with Hibernate:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # Options: none, validate, update, create, create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

## 📁 Project Structure

```
spring-boot-3-jwt-security-main/
├── src/
│   ├── main/
│   │   ├── java/com/example/ams/
│   │   │   ├── auth/              # Authentication logic
│   │   │   ├── config/            # Security configuration
│   │   │   ├── data/              # Business entities
│   │   │   │   ├── accounts_data/
│   │   │   │   ├── deposit_history_data/
│   │   │   │   ├── movement_history/
│   │   │   │   └── transaction_data/
│   │   │   ├── token/             # JWT token management
│   │   │   ├── user/              # User entity and management
│   │   │   └── auditing/          # JPA auditing
│   │   └── resources/
│   │       └── application.yml    # Configuration
│   └── test/                      # Unit tests
├── http/                          # HTTP test files
├── pom.xml                        # Maven dependencies
└── README.md                      # This file
```

## 🔒 Security Features Explained

### 1. JWT Authentication Flow

1. User registers/logs in → Receives access token and refresh token
2. Access token is included in `Authorization: Bearer <token>` header
3. JwtAuthenticationFilter validates token on each request
4. Token expires after configured time (default: 24 hours)
5. Refresh token used to obtain new access token

### 2. Security Filter Chain

- **CSRF**: Disabled for stateless JWT approach
- **Session Management**: Stateless (STATELESS)
- **Authentication Provider**: Custom implementation using UserDetailsService
- **JWT Filter**: Validates JWT token before authentication

### 3. Password Security

- Passwords are encrypted using BCrypt
- Spring Security handles password encoding automatically
- Never store plain-text passwords

## 🧪 Testing

Test files are available in the `http/` directory:

- `http-test.http` - Basic authentication tests
- `change-password.http` - Password change tests
- `jpa-auditing.http` - Auditing feature tests

You can use these files with REST Client extensions in VS Code or IntelliJ IDEA.

## 📖 Learning Objectives

This project helps students understand:

1. **Spring Security Basics**
   - Authentication vs Authorization
   - Security Filter Chain
   - UserDetailsService implementation

2. **JWT Implementation**
   - Token generation and validation
   - Refresh token mechanism
   - Stateless authentication

3. **Role-Based Access Control**
   - Role definitions
   - Method-level security
   - Endpoint protection

4. **Spring Boot Best Practices**
   - Dependency injection
   - Service layer pattern
   - DTO pattern
   - Exception handling

5. **JPA & Database**
   - Entity relationships
   - JPA Auditing
   - Repository pattern

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify MySQL is running
   - Check database credentials in `application.yml`
   - Ensure database exists

2. **Port Already in Use**
   - Change port in `application.yml`: `server.port: 8081`

3. **JWT Token Invalid**
   - Check token expiration
   - Verify secret key matches
   - Ensure token is included in Authorization header

4. **Access Denied**
   - Verify user has required role
   - Check endpoint access rules in `SecurityConfiguration`

## 🤝 Contributing

This is an educational project. Feel free to:
- Fork the repository
- Submit issues
- Create pull requests with improvements
- Share with other students learning Spring Security

## 📄 License

This project is open source and available for educational purposes.

## 👨‍💻 Author

Developed as an educational project to help students understand Spring Boot Security concepts.

## 🙏 Acknowledgments

- Spring Boot Security documentation
- JWT.io for JWT standard
- Spring community for excellent frameworks

---

**Happy Learning! 🚀**

If you found this project helpful, please give it a ⭐ on GitHub!
