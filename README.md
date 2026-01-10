# E-Commerce Application

A scalable and secure e-commerce application developed using Java, Spring Boot, Microservices, and React. The project follows modern backend design principles with a strong focus on security, performance, and maintainability.

---

## Technology Stack

### Backend Technologies
- Java  
- Spring Boot  
- Spring Security  
- JWT (JSON Web Token) Authentication  
- Hibernate / JPA  
- Spring WebFlux for reactive programming  
- RESTful APIs  
- Flyway for database versioning and migrations  

### Frontend Technologies
- React  

### Architecture and Tools
- Microservices architecture  
- API Gateway for centralized request handling  
- Relational databases  
- Centralized logging  
- Global exception and error handling  

---

## Application Features

### Authentication and Authorization
- Implemented secure authentication using JWT tokens.
- Used Spring Security to protect microservice endpoints.
- Enabled role-based authorization to restrict access to sensitive APIs.

### Product Catalog Management
- Developed product catalog services with full CRUD functionality.
- Enabled category-based product browsing.
- Designed scalable APIs for handling large product datasets.

### Cart and Order Management
- Implemented shopping cart functionality for adding and removing items.
- Designed order processing workflows including order creation and history tracking.
- Ensured data consistency across cart and order services.

### User Profile Management
- Built user services for registration and profile management.
- Allowed users to securely view and update personal information.
- Ensured user-specific data isolation across services.

### Reactive and High-Performance APIs
- Built non-blocking APIs using Spring WebFlux.
- Used Mono and Flux to improve application throughput and scalability.
- Optimized backend services for high concurrency.

### Database and Persistence
- Used Hibernate and JPA for object-relational mapping.
- Managed database schema changes using Flyway migrations.
- Ensured data integrity and consistency across microservices.

### Logging and Error Handling
- Implemented centralized logging for better monitoring and debugging.
- Designed global exception handling for consistent API error responses.
- Improved system reliability through structured error reporting.

---

## Microservices Overview

- API Gateway  
  Acts as the single entry point for all client requests. Handles routing, authentication, and security concerns.

- User Service  
  Manages user registration, authentication, and profile-related operations.

- Product Service  
  Handles product catalog management and inventory-related operations.

- Cart Service  
  Manages shopping cart operations and temporary user selections.

- Order Service  
  Responsible for order creation, processing, and order history management.



