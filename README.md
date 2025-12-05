# 🎬 KitFlik – Distributed Movie Booking Platform

A **production-grade**, highly scalable, and fault-tolerant **distributed movie booking system** built using modern Java technologies with a reactive microservices architecture.

![KitFlik Architecture](https://github.com/Bharatmarwah/KitFlik-Distributed-System/blob/main/Kitflik_systemdesugn.draw.io.png)

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

| Service                      | Core Responsibilities                                                                 | Key Technologies                              |
|------------------------------|---------------------------------------------------------------------------------------|-----------------------------------------------|
| **Auth Gateway**             | Reactive API Gateway, JWT issuance & validation, token refresh, secure routing, load balancing, rate limiting | Spring WebFlux, Spring Cloud Gateway, LoadBalancer, JJWT, Resilience4j |
| **User Service**             | Full user lifecycle management, registration, login, profile updates, secure password reset with OTP, email verification | Spring Boot, Spring Data JPA, MySQL, BCrypt |
| **Movie Management Service** | Movie catalog CRUD, advanced search, high-concurrency seat selection & booking, inventory locking, booking lifecycle, Redis caching | Spring Data Redis, JPA, MySQL, Redis, Reactive Streams |
| **Admin Service**            | Separate admin auth flow, user management (view/delete), full access to confirmed bookings, privileged operations | Custom JWT, WebClient, RBAC |
| **Notification Service**     | Asynchronous email engine, welcome mails, OTP delivery, booking confirmations, scheduled Friday reminders | Spring Mail, JavaMailSender, @Scheduled |
| **Eureka Service Registry**  | Dynamic service discovery, client-side load balancing, health checks, instance registry | Spring Cloud Netflix Eureka |
| **Recommend Service**   | AI-powered recommendations, semantic search, vector similarity, RAG-based responses | Spring AI, Pinecone, Ollama (mxbai-embed-large, phi3:3.8b, gemma2:2b) |

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

## 🩺 Observability & Monitoring (Spring Boot Actuator)

All microservices include Spring Boot Actuator with secure production-grade endpoints:

**/actuator/health** – service health & dependency checks  
**/actuator/info** – application metadata  
**/actuator/metrics** – JVM, CPU, memory, HTTP request metrics  
**/actuator/prometheus** – for Grafana/Prometheus integration  
**/actuator/loggers** – dynamic logging level updates  

**Actuator is enabled in:**

- Auth Gateway  
- User Service  
- Movie Management Service  
- Admin Service  
- Notification Service  

Each service registers health with Eureka, enabling automatic instance tracking and load-balancer awareness.

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
7. **Recommendation Service**

> Make sure MySQL, Redis, Pinecone API keys, and AI model configurations are correctly set.

---

# 🔮 Recommendation Service

The **Recommendation Service** introduces AI-driven personalization into KitFlik using vector search, embeddings, and RAG (Retrieval-Augmented Generation).

## **Core Responsibilities**

- Semantic movie search  
- Personalized recommendations  
- Vector similarity (cosine) search  
- RAG-based movie suggestions  
- Embedding generation for movie metadata  
- User-context-aware responses  

## **Key Technologies**

- **Spring AI** – Unified interface for embeddings + LLM  
- **Pinecone Vector Database** – High-speed embedding storage + similarity search  
- **Ollama Models:**  
  - **mxbai-embed-large** → Embeddings  
  - **phi3:3.8b** → Lightweight reasoning for RAG  
  - **gemma2:2b** → Natural language generation  
- **Spring WebFlux** – Reactive networking  
- **WebClient** – Gateway token-forwarding  

## **How It Works**

1. Movie metadata is converted to embeddings using **mxbai-embed-large**.  
2. Embeddings stored inside **Pinecone**.  
3. On user query:  
   - JWT forwarded through Gateway  
   - Cosine similarity search fetches top movies  
   - **phi3:3.8b** or **gemma2:2b** generates the final recommended text  
4. Response is sent back reactively.

---

## 📂 Project Structure (High-level)  Kitflik 

```
├── eureka-server/
├── auth-gateway/
├── user-service/
├── movie-management-service/
├── admin-service/
├── notification-service/
├── recommendation-service/
└── common
    (shared models,exceptions, utils)
```

## 👤 Author

**Bharat Marwah**  
Java Backend Developer | Systems Architect  

🔗 https://www.linkedin.com/in/bharat-marwah-323056319/

---

⭐ If you like this project, please give it a star!  
Contributions, issues, and feature requests are welcome!
