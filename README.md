# 🎬 KitFlik – Distributed Movie Booking Platform

A **production-grade**, highly scalable, and fault-tolerant **distributed movie booking system** built using modern Java technologies with a reactive microservices architecture.

![KitFlik Architecture](https://github.com/user-attachments/assets/512b4f5d-1190-47aa-af44-37e5c0d6081f)

## 📌 Overview

KitFlik is a fully modular, reactive microservices-based movie ticket booking platform that supports:

- User registration & authentication
- Movie browsing & search
- Secure ticket booking
- Admin dashboard
- Real-time email notifications
- High availability & fault tolerance

All services communicate asynchronously via a **reactive API Gateway** using **Spring WebFlux**, ensuring non-blocking, high-performance operations.

## Microservices Architecture

| Service                    | Core Responsibilities                                                                 | Key Technologies                              |
|----------------------------|---------------------------------------------------------------------------------------|-----------------------------------------------|
| **Auth Gateway**           | Reactive API Gateway, JWT issuance & validation, token refresh, secure routing, load balancing, rate limiting | Spring WebFlux, Spring Cloud Gateway, LoadBalancer, JJWT, Resilience4j |
| **User Service**           | Full user lifecycle management, registration, login, profile updates, secure password reset with OTP, email verification | Spring Boot, Spring Data JPA, MySQL, BCrypt |
| **Movie Management Service** | Movie catalog CRUD, advanced search, high-concurrency seat selection & booking, inventory locking, booking lifecycle, Redis caching | Spring Data Redis, JPA, MySQL, Redis, Reactive Streams |
| **Admin Service**          | Separate admin auth flow, user management (view/delete), full access to confirmed bookings, privileged operations | Custom JWT, WebClient, RBAC |
| **Notification Service**  | Asynchronous email engine, welcome mails, OTP delivery, booking confirmations, scheduled Friday reminders | Spring Mail, JavaMailSender, Thymeleaf, @Scheduled |
| **Eureka Service Registry**| Dynamic service discovery, client-side load balancing, health checks, instance registry | Spring Cloud Netflix Eureka                   |
 
## ⚙️ Tech Stack

- **Language**: Java 21
- **Core Framework**: Spring Boot 3.x
- **Reactive Stack**: Spring WebFlux, Project Reactor
- **API Gateway**: Spring Cloud Gateway + WebFlux
- **Inter-service Communication**: Reactive WebClient + Spring Cloud LoadBalancer
- **Security**: JWT (Access + Refresh Tokens)
- **Database**: MySQL + Spring Data JPA
- **Caching**: Redis
- **Service Discovery**: Netflix Eureka
- **Resilience**: Resilience4j (Circuit Breaker, Retry, Rate Limiter, Time Limiter)
- **Scheduling**: Spring @Scheduled
- **Build Tool**: Maven
- **Utilities**: Lombok, ModelMapper

## 🛡 Fault Tolerance & Resilience

All inter-service calls are protected with **Resilience4j**:

- Circuit Breaker
- Retry Mechanism
- Rate Limiting
- Time Limiter
- Fallback responses

Ensures the system remains operational even during partial failures.

## 🧪 Testing & Performance

- All APIs tested end-to-end via Gateway
- Redis caching validated (movie listings)
- Email delivery confirmed
- Admin role-based access enforced
- **Average API latency**: 33ms – 433ms (depending on load & caching)

## 🚀 Run Order (Local Development)

1. **Eureka Server**
2. **Auth Gateway**
3. **User Service**
4. **Movie Management Service**
5. **Admin Service**
6. **Notification Service**

> Make sure MySQL, Redis, and Mail server (or MailHog for testing) are running.

## 📂 Project Structure (High-level)
kitflik/
├── eureka-server/
├── auth-gateway/
├── user-service/
├── movie-management-service/
├── admin-service/
├── notification-service/
└── common/                 (shared models, exceptions, utils)
text

## 👤 Author

**Bharat Marwah**  
Java Backend Developer | Systems Architect

🔗 [LinkedIn](https://www.linkedin.com/in/bharat-marwah-323056319/)  
💼 Open for full-time opportunities in Backend/Java/Microservices

---

⭐ If you like this project, please give it a star!  
Contributions, issues, and feature requests are welcome!

---
