🎬 KitFlik – Distributed Movie Booking Platform

A production-grade distributed backend system built using Spring Boot, Spring Cloud, Spring WebFlux, Redis, JWT Security, Resilience4j, and MySQL.
KitFlik provides a fully modular microservices architecture for handling movies, bookings, user accounts, admin operations, and notifications with high performance and scalability.

📌 Overview

KitFlik is composed of multiple independently deployable microservices communicating via a reactive API Gateway.
The system supports user authentication, movie listing, ticket booking, notifications, and admin management with secure, fault-tolerant, load-balanced routing.

🧩 Microservices
🔐 Auth Gateway

Reactive API Gateway using Spring WebFlux and LoadBalancer.
Handles token generation, validation, routing, and exposes authentication endpoints.

Key Responsibilities

Generate Access & Refresh Tokens

Validate JWT via custom filters

Non-blocking routing using WebFlux

Load-balanced WebClient communication

Authentication endpoints: login, register, forgot password, reset password, logout, refresh token

👤 User Service

Manages the complete user lifecycle.

Capabilities

User registration

Credential verification

Password reset support

Provides user details to Gateway and Admin Service

Triggers notification emails

🎞️ Movie Management Service

Handles movies and booking modules in a single service.

Movie Module

Fetch all movies

Search by type

Search by name

Redis caching for improved performance

Booking Module

Book movie

Update booking

Remove booking

Confirm booking

Fetch confirmed bookings

🛡 Admin Service

Dedicated admin operations with its own authentication flow.

Capabilities

Admin login and token generation

Validate admin JWT

Fetch user details

Delete user by ID

Access confirmed bookings from Movie Management Service

🔔 Notification Service

Handles all email-related operations.

Capabilities

Registration emails

OTP for password resets

Booking confirmation mails

Scheduled Friday email reminders

🌐 Service Registry (Eureka)

Centralized service discovery for all microservices with dynamic load-balanced routing.

🛠 Architecture
                  ![WhatsApp Image 2025-11-18 at 23 41 11_312b4047](https://github.com/user-attachments/assets/512b4f5d-1190-47aa-af44-37e5c0d6081f)




             
⚙️ Tech Stack

Language — Java 21
Frameworks — Spring Boot, Spring Cloud
Gateway — Spring WebFlux, Spring Cloud LoadBalancer
Communication — Reactive WebClient
Security — JWT Authentication
Database — MySQL with Spring Data JPA
Caching — Redis
Scheduling — Spring Scheduler
Resilience — Resilience4j
Service Discovery — Eureka
Build — Maven
Other — Lombok, ModelMapper

🔄 Communication Flow

Gateway ↔ UserService (authentication, verification)

Gateway ↔ NotificationService (registration and OTP emails)

MovieManagementService ↔ NotificationService (booking updates)

AdminService ↔ UserService (user operations)

AdminService ↔ MovieManagementService (confirmed bookings)

All via WebClient + LoadBalancer

🛡 Fault Tolerance

Resilience4j is integrated into all inter-service communication for:

Circuit Breaking

Retry

Rate Limiting

Time Limiting

Fallback responses

🧪 Testing Summary

All services tested through Gateway

Redis caching validated

Email service operational

Admin authorization validated

API response latency: 33ms – 433ms

🚀 Run Order

Service Registry

Auth Gateway

User Service

Movie Management Service

Admin Service

Notification Service


👤 Author

Bharat Marwah
Java Backend Developer | System Architect

🔗 LinkedIn: https://www.linkedin.com/in/bharat-marwah-323056319/
