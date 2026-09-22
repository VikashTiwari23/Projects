# Spring Boot Microservices - Zero se Seekho (Simple Hinglish)

---

## 1. MICROSERVICES KYA HAI?

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Microservices = Application ko CHHOTI services mein baantna  │
    │                                                              │
    │  Real Life Analogy:                                          │
    │                                                              │
    │  MONOLITH (Ek bada restaurant):                              │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Sab kuch EK jagah:                                  │    │
    │  │  Menu + Kitchen + Billing + Delivery = ONE app       │    │
    │  │  Problem: Ek cheez crash → SAB crash! ❌             │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  MICROSERVICES (Alag alag dukaane):                           │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Har cheez ALAG:                                     │    │
    │  │  🍔 Burger Shop (Order Service)                      │    │
    │  │  📦 Delivery Shop (Delivery Service)                 │    │
    │  │  💰 Billing Shop (Payment Service)                   │    │
    │  │  👤 Customer Shop (User Service)                     │    │
    │  │                                                      │    │
    │  │  Ek crash → Baaki chalte rahenge! ✅                 │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 2. MONOLITH vs MICROSERVICES

```
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  MONOLITH:                                                   │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │                                                      │    │
    │  │   ┌─────────────────────────────────────────────┐   │    │
    │  │   │              ONE BIG APPLICATION            │   │    │
    │  │   │  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐  │   │    │
    │  │   │  │User │ │Order│ │Pay  │ │Auth │ │Ship │  │   │    │
    │  │   │  └─────┘ └─────┘ └─────┘ └─────┘ └─────┘  │   │    │
    │  │   └─────────────────────────────────────────────┘   │    │
    │  │                                                      │    │
    │  │  Problem: Ek module pe change → Poora deploy!        │    │
    │  │  Scaling: Poora app scale karo (expensive!)          │    │
    │  │  Tech: Ek technology use karo                        │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  MICROSERVICES:                                              │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │                                                      │    │
    │  │   ┌─────────┐  ┌─────────┐  ┌─────────┐            │    │
    │  │   │  User   │  │  Order  │  │ Payment │            │    │
    │  │   │ Service │  │ Service │  │ Service │            │    │
    │  │   └────┬────┘  └────┬────┘  └────┬────┘            │    │
    │  │        │            │            │                   │    │
    │  │        └────────────┼────────────┘                   │    │
    │  │                     │                                 │    │
    │  │              ┌──────┴──────┐                          │    │
    │  │              │  API Gateway │                          │    │
    │  │              └─────────────┘                          │    │
    │  │                                                      │    │
    │  │  ✅ Independent deploy                                │    │
    │  │  ✅ Scale only what's needed                          │    │
    │  │  ✅ Different tech stack                              │    │
    │  │  ✅ Fault isolated                                    │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    └──────────────────────────────────────────────────────────────┘
```

---

## 3. MICROSERVICES ARCHITECTURE

```
    ┌──────────────────────────────────────────────────────────────┐
    │  MICROSERVICES ARCHITECTURE                                  │
    └──────────────────────────────────────────────────────────────┘

              ┌─────────────────────────────────────┐
              │           CLIENT (Web/Mobile)       │
              └──────────────────┬──────────────────┘
                                 │
                                 ▼
              ┌─────────────────────────────────────┐
              │         API GATEWAY                 │
              │    (Routing, Auth, Rate Limit)      │
              └──────────────────┬──────────────────┘
                                 │
        ┌────────────────────────┼────────────────────────┐
        │                        │                        │
        ▼                        ▼                        ▼
┌──────────────┐        ┌──────────────┐        ┌──────────────┐
│ USER SERVICE │        │ORDER SERVICE │        │PAYMENT SERVICE│
│   (Port 8081)│        │  (Port 8082) │        │  (Port 8083) │
│              │        │              │        │              │
│ - CRUD Users │        │- CRUD Orders │        │- Process Pay │
│ - Auth       │        │- Inventory   │        │- Refunds     │
└──────┬───────┘        └──────┬───────┘        └──────┬───────┘
       │                       │                       │
       ▼                       ▼                       ▼
┌──────────────┐        ┌──────────────┐        ┌──────────────┐
│  User DB     │        │  Order DB    │        │  Payment DB  │
│  (MySQL)     │        │  (PostgreSQL)│        │  (MySQL)     │
└──────────────┘        └──────────────┘        └──────────────┘


    Key Components:
    ┌──────────────────────────────────────────────────────────────┐
    │  1. API Gateway     → Single entry point (routing, auth)     │
    │  2. Service Registry→ Service discovery (Eureka)            │
    │  3. Load Balancer   → Distribute traffic                    │
    │  4. Config Server   → Centralized configuration             │
    │  5. Circuit Breaker → Fault tolerance                       │
    │  6. Message Broker  → Async communication (RabbitMQ, Kafka) │
    │  7. Distributed Tracing → Track requests across services    │
    └──────────────────────────────────────────────────────────────┘
```

---

## 4. KEY MICROSERVICES CONCEPTS

### 4.1 API Gateway

```
    ┌──────────────────────────────────────────────────────────────┐
    │  API GATEWAY = Single entry point for all requests           │
    │                                                              │
    │  Real Life: Receptionist in office                           │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Visitor → Receptionist → Concerned Person           │    │
    │  │  Client   → API Gateway → Correct Microservice       │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  Functions:                                                  │
    │  ✅ Routing (request ko sahi service bhejo)                  │
    │  ✅ Authentication (login check)                             │
    │  ✅ Rate Limiting (kitne requests allow)                     │
    │  ✅ Load Balancing (traffic distribute)                      │
    │  ✅ Request/Response transformation                          │
    │  ✅ SSL Termination                                          │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Spring Cloud Gateway Example
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
```

### 4.2 Service Discovery (Eureka)

```
    ┌──────────────────────────────────────────────────────────────┐
    │  SERVICE DISCOVERY = Services ek doosre ko dhoond sakein     │
    │                                                              │
    │  Problem: Har service ka URL hardcode mat karo!              │
    │                                                              │
    │  Solution: Eureka Server (Phone directory)                   │
    │                                                              │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │                                                      │    │
    │  │  Service → Register → Eureka Server                 │    │
    │  │  (Main available hoon!)                              │    │
    │  │                                                      │    │
    │  │  Client → Discover → Eureka Server                  │    │
    │  │  (Kahan milega User Service?)                        │    │
    │  │                                                      │    │
    │  │  Eureka: "http://localhost:8081"                     │    │
    │  │                                                      │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  Flow:                                                       │
    │  1. Service start hote hi Eureka mein REGISTER hota hai      │
    │  2. Client Eureka se service ka address discover karta hai   │
    │  3. Client directly service ko call karta hai                │
    │  4. Service down ho toh Eureka se remove hota hai            │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Eureka Server
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApp.class, args);
    }
}

// Eureka Client (each microservice)
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApp.class, args);
    }
}
```

### 4.3 Load Balancing

```
    ┌──────────────────────────────────────────────────────────────┐
    │  LOAD BALANCING = Traffic ko multiple instances mein baanto  │
    │                                                              │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │                                                      │    │
    │  │         100 Requests                                 │    │
    │  │              │                                       │    │
    │  │              ▼                                       │    │
    │  │       ┌─────────────┐                                │    │
    │  │       │ Load Balancer│                                │    │
    │  │       └──────┬──────┘                                │    │
    │  │              │                                       │    │
    │  │    ┌─────────┼─────────┐                             │    │
    │  │    ▼         ▼         ▼                             │    │
    │  │ ┌─────┐  ┌─────┐  ┌─────┐                          │    │
    │  │ │Inst1│  │Inst2│  │Inst3│  (3 instances)            │    │
    │  │ │ 34  │  │ 33  │  │ 33  │                           │    │
    │  │ └─────┘  └─────┘  └─────┘                          │    │
    │  │                                                      │    │
    │  │  Strategies:                                         │    │
    │  │  Round Robin: 1,2,3,1,2,3...                        │    │
    │  │  Random: Random selection                            │    │
    │  │  Least Connections: Jo kam load hai                  │    │
    │  │  Weighted: Weight ke hisaab se                      │    │
    │  └──────────────────────────────────────────────────────┘    │
    └──────────────────────────────────────────────────────────────┘
```

### 4.4 Circuit Breaker

```
    ┌──────────────────────────────────────────────────────────────┐
    │  CIRCUIT BREAKER = Fault tolerance ka pattern                │
    │                                                              │
    │  Real Life: Electrical fuse                                   │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Problem: Service down → Requests pile up → Crash!   │    │
    │  │                                                      │    │
    │  │  Circuit Breaker states:                             │    │
    │  │                                                      │    │
    │  │  CLOSED (Normal):                                    │    │
    │  │  ┌─────────────────────────────────────────────┐     │    │
    │  │  │  Requests pass through normally              │     │    │
    │  │  │  Failure count increasing...                 │     │    │
    │  │  └─────────────────────────────────────────────┘     │    │
    │  │              │ (Too many failures)                   │    │
    │  │              ▼                                       │    │
    │  │  OPEN (Broken):                                      │    │
    │  │  ┌─────────────────────────────────────────────┐     │    │
    │  │  │  All requests FAIL immediately              │     │    │
    │  │  │  No calls to failing service!               │     │    │
    │  │  │  Return fallback response                   │     │    │
    │  │  └─────────────────────────────────────────────┘     │    │
    │  │              │ (After timeout)                        │    │
    │  │              ▼                                       │    │
    │  │  HALF-OPEN (Testing):                                │    │
    │  │  ┌─────────────────────────────────────────────┐     │    │
    │  │  │  Send few test requests                     │     │    │
    │  │  │  Success → CLOSED                           │     │    │
    │  │  │  Failure → OPEN                             │     │    │
    │  │  └─────────────────────────────────────────────┘     │    │
    │  │                                                      │    │
    │  └──────────────────────────────────────────────────────┘    │
    └──────────────────────────────────────────────────────────────┘
```

```java
// Resilience4j Circuit Breaker
@Service
public class OrderService {

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUser")
    public User getUser(Long userId) {
        return restTemplate.getForObject(
            "http://user-service/api/users/" + userId, User.class);
    }

    // Fallback method (called when circuit is open)
    public User fallbackGetUser(Long userId, Throwable t) {
        return new User(userId, "Default User", "default@email.com");
    }
}
```

### 4.5 Config Server

```
    ┌──────────────────────────────────────────────────────────────┐
    │  CONFIG SERVER = Centralized configuration management        │
    │                                                              │
    │  Problem: Har service ki config alag file mein? 😫           │
    │                                                              │
    │  Solution: Sab config EK jagah (Config Server)               │
    │                                                              │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │                                                      │    │
    │  │  Config Server (Git Repository)                      │    │
    │  │  ┌─────────────────────────────────────────────┐     │    │
    │  │  │  application.yml                             │     │    │
    │  │  │  user-service.yml                            │     │    │
    │  │  │  order-service.yml                           │     │    │
    │  │  │  payment-service.yml                         │     │    │
    │  │  └─────────────────────────────────────────────┘     │    │
    │  │         │           │           │                     │    │
    │  │         ▼           ▼           ▼                     │    │
    │  │      User        Order       Payment                  │    │
    │  │      Service     Service     Service                  │    │
    │  │                                                      │    │
    │  │  ✅ Change once, applies everywhere                   │    │
    │  │  ✅ Version controlled (Git)                          │    │
    │  │  ✅ Dynamic refresh (no restart needed)               │    │
    │  └──────────────────────────────────────────────────────┘    │
    └──────────────────────────────────────────────────────────────┘
```

### 4.6 Message Broker (Async Communication)

```
    ┌──────────────────────────────────────────────────────────────┐
    │  MESSAGE BROKER = Services aapas mein baat karein ASYNC      │
    │                                                              │
    │  SYNC (REST - Tight coupling):                               │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Order Service → HTTP Call → Payment Service         │    │
    │  │  (Wait for response... wait... wait...)               │    │
    │  │  If Payment down → Order fails! ❌                   │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  ASYNC (Message Broker - Loose coupling):                    │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  Order Service → Send Message → Queue → Payment      │    │
    │  │  (Send and forget!)                                  │    │
    │  │  Payment down → Message in queue, try later ✅       │    │
    │  └──────────────────────────────────────────────────────┘    │
    │                                                              │
    │  Popular: RabbitMQ, Apache Kafka                             │
    └──────────────────────────────────────────────────────────────┘
```

```
    Message Broker Flow:
    ┌──────────────────────────────────────────────────────────────┐
    │                                                              │
    │  Producer          Queue/Broker          Consumer            │
    │  ┌─────────┐      ┌─────────────┐      ┌─────────┐         │
    │  │ Order   │─────▶│ ┌─────────┐ │─────▶│ Payment │         │
    │  │ Service │      │ │Message 1│ │      │ Service │         │
    │  │         │      │ │Message 2│ │      │         │         │
    │  │         │      │ │Message 3│ │      │         │         │
    │  └─────────┘      │ └─────────┘ │      └─────────┘         │
    │                    └─────────────┘                          │
    │                                                              │
    │  ✅ Loose coupling (services don't know each other)          │
    │  ✅ Reliable (messages don't get lost)                       │
    │  ✅ Scalable (add more consumers if needed)                  │
    │  ✅ Fault tolerant (retry on failure)                        │
    └──────────────────────────────────────────────────────────────┘
```

---

## 5. MICROSERVICES COMMUNICATION

```
    ┌──────────────────────────────────────────────────────────────┐
    │  Two Types of Communication:                                 │
    │                                                              │
    │  1. SYNCHRONOUS (Direct call - wait for response)            │
    │     - REST (HTTP)                                            │
    │     - gRPC                                                   │
    │     - GraphQL                                                │
    │                                                              │
    │  2. ASYNCHRONOUS (Send and forget)                           │
    │     - Message Queue (RabbitMQ)                               │
    │     - Event Streaming (Kafka)                                │
    │     - Pub/Sub                                                │
    │                                                              │
    │  When to use:                                                │
    │  ┌──────────────────────────────────────────────────────┐    │
    │  │  SYNC: Real-time response needed                     │    │
    │  │  Example: Get user details for order                 │    │
    │  │                                                      │    │
    │  │  ASYNC: Response not needed immediately              │    │
    │  │  Example: Send email after order placed              │    │
    │  └──────────────────────────────────────────────────────┘    │
    └──────────────────────────────────────────────────────────────┘
```

```java
// SYNCHRONOUS (REST Template / WebClient)
@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    public User getUser(Long userId) {
        return restTemplate.getForObject(
            "http://user-service/api/users/" + userId, User.class);
    }
}

// ASYNCHRONOUS (RabbitMQ)
@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void createOrder(Order order) {
        // Save order
        orderRepository.save(order);

        // Send message (don't wait for response)
        rabbitTemplate.convertAndSend(
            "order.exchange", "order.created", order);
    }
}
```

---

## 6. MICROSERVICES BEST PRACTICES

```
╔══════════════════════════════════════════════════════════════════╗
║           MICROSERVICES BEST PRACTICES                           ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                  ║
║  1. DESIGN:                                                      ║
║  ✅ Single Responsibility (ek service = ek kaam)                 ║
║  ✅ Database per service (alag DB for each)                      ║
║  ✅ Design for failure (assume services will fail)               ║
║  ✅ Loose coupling (services independent)                        ║
║  ✅ High cohesion (related functionality together)               ║
║                                                                  ║
║  2. COMMUNICATION:                                               ║
║  ✅ Use async when possible (message queue)                      ║
║  ✅ Use sync only when response needed                           ║
║  ✅ Implement circuit breaker                                    ║
║  ✅ Use API gateway for entry point                              ║
║                                                                  ║
║  3. DATA MANAGEMENT:                                             ║
║  ✅ Database per service                                         ║
║  ✅ Use Saga for distributed transactions                        ║
║  ✅ Event sourcing for audit trail                               ║
║  ✅ CQRS for read/write separation                               ║
║                                                                  ║
║  4. DEPLOYMENT:                                                  ║
║  ✅ Containerization (Docker)                                    ║
║  ✅ Orchestration (Kubernetes)                                   ║
║  ✅ CI/CD pipeline                                              ║
║  ✅ Blue-green or canary deployment                              ║
║                                                                  ║
║  5. OBSERVABILITY:                                               ║
║  ✅ Centralized logging (ELK stack)                              ║
║  ✅ Distributed tracing (Jaeger, Zipkin)                         ║
║  ✅ Metrics (Prometheus + Grafana)                               ║
║  ✅ Health checks (Actuator)                                     ║
║                                                                  ║
║  6. SECURITY:                                                    ║
║  ✅ API gateway for auth                                         ║
║  ✅ JWT tokens                                                  ║
║  ✅ Service-to-service auth                                      ║
║  ✅ Secrets management                                           ║
║                                                                  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

## 7. MICROSERVICES INTERVIEW QUESTIONS (40+)

### ⭐ BASIC

**Q1: What are microservices?**
> Small, independent services that communicate via APIs. Each service handles one business capability.

**Q2: What is the difference between monolith and microservices?**
```
Monolith: Single application, all-in-one, harder to scale
Microservices: Multiple services, independent, easier to scale
```

**Q3: What is API Gateway?**
> Single entry point for all client requests. Handles routing, auth, rate limiting.

**Q4: What is service discovery?**
> Mechanism for services to find each other dynamically (Eureka, Consul).

**Q5: What is load balancing?**
> Distributing traffic across multiple service instances.

**Q6: What is circuit breaker?**
> Pattern to handle failures gracefully. Prevents cascade failures.

**Q7: What is the difference between sync and async communication?**
```
Sync: Wait for response (REST, gRPC)
Async: Send and forget (Message Queue, Kafka)
```

**Q8: What is a message broker?**
> Intermediary for async communication (RabbitMQ, Kafka).

**Q9: What is centralized configuration?**
> Config Server for managing all service configurations in one place.

**Q10: What is the benefit of microservices?**
> Independent deployment, scalability, technology diversity, fault isolation.

---

### ⭐⭐ MIDDLE

**Q11: What is Eureka?**
> Netflix service discovery. Services register here, clients discover them.

**Q12: What is the difference between Eureka and Consul?**
```
Eureka: Netflix, Java-focused, eventual consistency
Consul: HashiCorp, multi-language, stronger consistency
```

**Q13: What is the difference between load balancer and API gateway?**
```
Load Balancer: Distributes traffic to instances
API Gateway: Entry point with routing, auth, rate limiting
API Gateway often includes load balancing
```

**Q14: What is distributed tracing?**
> Tracking requests across multiple services (Jaeger, Zipkin).

**Q15: What is the saga pattern?**
> Managing distributed transactions through a sequence of local transactions with compensation.

**Q16: What is the difference between saga and 2-phase commit?**
```
Saga: Eventually consistent, compensating transactions
2-Phase Commit: Strongly consistent, locks resources
Saga is more scalable
```

**Q17: What is event sourcing?**
> Store state changes as events (not current state). Full audit trail.

**Q18: What is CQRS?**
> Command Query Responsibility Segregation. Separate read and write models.

**Q19: What is the difference between event sourcing and CQRS?**
```
Event Sourcing: Store events instead of state
CQRS: Separate read/write models
Often used together
```

**Q20: What is Docker in microservices?**
> Containerization. Package app with dependencies. Consistent across environments.

---

### ⭐⭐⭐ ADVANCED

**Q21: What is Kubernetes?**
> Container orchestration. Manages, scales, heals containers automatically.

**Q22: What is the difference between Docker and Kubernetes?**
```
Docker: Containerization (package app)
Kubernetes: Orchestration (manage containers)
```

**Q23: What is service mesh?**
> Infrastructure layer for service-to-service communication (Istio, Linkerd).

**Q24: What is the difference between API gateway and service mesh?**
```
API Gateway: North-south traffic (client to service)
Service Mesh: East-west traffic (service to service)
```

**Q25: What is blue-green deployment?**
> Two identical environments. Switch traffic from blue to green for zero downtime.

**Q26: What is canary deployment?**
> Gradually roll out to small percentage of users before full rollout.

**Q27: What is the difference between blue-green and canary?**
```
Blue-Green: Switch all at once (two environments)
Canary: Gradual rollout (percentage-based)
```

**Q28: What is rate limiting in API gateway?**
> Limit number of requests per time period to prevent abuse.

**Q29: What is the difference between authentication and authorization in microservices?**
```
Authentication: Who are you? (JWT token)
Authorization: What can you do? (roles/permissions)
```

**Q30: What is JWT in microservices?**
> Stateless token for authentication. Passed in every request.

**Q31: What is the difference between JWT and session-based auth?**
```
JWT: Stateless, stored by client, scalable
Session: Stateful, stored by server, simpler
JWT better for microservices
```

**Q32: What is the difference between REST and gRPC in microservices?**
```
REST: JSON, HTTP, text, slower, easier
gRPC: Protocol Buffers, HTTP/2, binary, faster, complex
gRPC better for internal service communication
```

**Q33: What is the difference between RabbitMQ and Kafka?**
```
RabbitMQ: Message queue, traditional, simpler
Kafka: Event streaming, high throughput, distributed log
Kafka better for event sourcing, real-time
```

**Q34: What is the difference between queue and topic?**
```
Queue: Point-to-point (one consumer per message)
Topic: Pub/Sub (multiple consumers get message)
```

**Q35: What is dead letter queue?**
> Queue for messages that can't be processed (failed repeatedly).

**Q36: What is idempotency in microservices?**
> Same request multiple times = same result. Important for retries.

**Q37: What is the difference between consistency and availability?**
```
Consistency: All nodes see same data
Availability: System always responds
CAP theorem: Can only choose 2 of 3 (CP, AP, CA)
```

**Q38: What is CAP theorem?**
```
Consistency: All nodes see same data
Availability: Every request gets response
Partition Tolerance: System works despite network failures
Can only guarantee 2 of 3
```

**Q39: What is the best architecture for microservices?**
```
1. API Gateway (entry point)
2. Service Discovery (Eureka/Consul)
3. Config Server (centralized config)
4. Circuit Breaker (fault tolerance)
5. Message Broker (async communication)
6. Distributed Tracing (observability)
7. Containerization (Docker)
8. Orchestration (Kubernetes)
```

**Q40: What are challenges of microservices?**
```
1. Complex deployment (many services)
2. Data consistency (distributed transactions)
3. Network latency (service-to-service calls)
4. Debugging (distributed system)
5. Testing (integration testing hard)
6. Security (service-to-service auth)
7. Monitoring (many services to monitor)
8. Versioning (API compatibility)
```

---

*Last Updated: September 2026*
*Covers: Microservices, API Gateway, Eureka, Circuit Breaker, 40 Interview Questions*
