# COMP3095 - Assignment 2 Status Checklist
**Group Name:** GBC_WellnessHub-[YourGroupName]
**Members:** [Name1 ID], [Name2 ID], [Name3 ID]

| Requirement | Status | Implementation Details |
| :--- | :---: | :--- |
| **1. API Gateway & Auth** | ✅ | |
| Spring Cloud Gateway | DONE | Configured on port 8082. Routes all traffic to microservices. |
| Keycloak Integration | DONE | Role-based access control (Student/Staff) enforced via JWT. |
| Centralized Swagger | DONE | Aggregated Swagger UI available at `http://localhost:8082/webjars/swagger-ui/index.html`. |
| **2. Inter-Service Comm.** | ✅ | |
| Kafka Setup | DONE | Kafka, Zookeeper, and Schema Registry running in Docker. |
| Schema Registry | DONE | Used `KafkaJsonSchemaSerializer` to enforce schema compatibility. |
| Async Events | DONE | `GoalCompletedEvent` published by Goal Service, consumed by Event/Wellness Services. |
| **3. Resilience** | ✅ | |
| Circuit Breaker | DONE | Resilience4j applied to inter-service calls (Goal -> Wellness). |
| Fallback Logic | DONE | Fallback methods return default/cached responses when Wellness service is down. |
| **4. Observability** | ✅ | |
| Prometheus | DONE | Scrapes `/actuator/prometheus` from all services. |
| Grafana | DONE | Visualizes metrics on port 3000. |
| Loki & Promtail | DONE | Aggregates logs from all Docker containers. |
| **5. Testing & Dev** | ✅ | |
| Integration Tests | DONE | `AuthenticationIT` uses TestContainers for Keycloak. |
| Docker Compose | DONE | Full stack (13 containers) launches via `docker-compose up`. |

**Notes:**
- All services are fully containerized.
- No local IntelliJ environments are required to run the solution.