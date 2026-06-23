<div align="center">

# ⚡ EnergySense

**Cloud-native HVAC & IoT Energy Monitoring Platform**

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-6DB33F?style=flat-square&logo=springboot)
![Angular](https://img.shields.io/badge/Angular-18-DD0031?style=flat-square&logo=angular)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=flat-square&logo=postgresql)
![Kubernetes](https://img.shields.io/badge/Kubernetes-Minikube-326CE5?style=flat-square&logo=kubernetes)
![Terraform](https://img.shields.io/badge/Terraform-IaC-7B42BC?style=flat-square&logo=terraform)
![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-D24939?style=flat-square&logo=jenkins)
![ArgoCD](https://img.shields.io/badge/ArgoCD-GitOps-EF7B4D?style=flat-square&logo=argo)

*Portfolio project — DevOps & Cloud Engineering*

</div>

---

## 📋 Overview

EnergySense is a cloud-native platform for real-time HVAC system monitoring and IoT sensor data analysis. Built as a comprehensive DevOps portfolio project, it combines domain expertise in HVAC/refrigeration with modern cloud engineering practices.

**Key design decisions:**
- **Hexagonal architecture** (ports & adapters) for clean separation between domain logic and infrastructure
- **GitOps workflow** with Jenkins CI and ArgoCD continuous delivery
- **Infrastructure as Code** with Terraform and security scanning via Checkov (17 checks)
- **Full observability stack** with Prometheus, Grafana, and Loki

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Angular 18 Frontend                       │
│         Dark dashboard · Chart.js · JWT interceptor         │
└─────────────────────┬───────────────────────────────────────┘
                      │ REST / HTTP
┌─────────────────────▼───────────────────────────────────────┐
│              Spring Boot 4.1.0 — Hexagonal Architecture      │
│                                                              │
│  ┌──────────────┐    ┌──────────────┐    ┌───────────────┐  │
│  │   Domain     │    │  Application │    │ Infrastructure│  │
│  │   Models     │◄───│  Use Cases   │◄───│   Adapters    │  │
│  │   (pure)     │    │   (ports)    │    │  JPA / MQTT   │  │
│  └──────────────┘    └──────────────┘    └───────────────┘  │
└─────────────────────┬───────────────────────────────────────┘
                      │
        ┌─────────────┼──────────────┐
        ▼             ▼              ▼
  PostgreSQL 16    Mosquitto      MQTT Broker
                   (IoT sensors)
```

### Domain Model

```
Site ──► Sensor ──► Reading
              └──► Threshold ──► Alert
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 4.1.0, Java 21, Maven |
| Security | JWT (jjwt 0.12.6), BCrypt |
| Database | PostgreSQL 16, Spring Data JPA |
| IoT | MQTT (Mosquitto), Eclipse Paho, Spring Integration 7.x |
| Frontend | Angular 18 (standalone), Chart.js, dark theme |
| Container | Docker, Docker Compose |
| Orchestration | Kubernetes (Minikube), Helm |
| CI/CD | Jenkins, ArgoCD (GitOps) |
| IaC | Terraform + Checkov (17 security checks) |
| Observability | Prometheus, Grafana, Loki |
| API Docs | SpringDoc OpenAPI / Swagger UI |

---

## 🚀 Getting Started

### Prerequisites

- Docker & Docker Compose
- Java 21
- Node.js 20+
- Minikube (optional, for K8s)

### 1. Clone the repository

```bash
git clone git@github.com:Oussemamsehli/energysense.git
cd energysense
```

### 2. Start the backend (Docker Compose)

```bash
docker compose up -d
```

Services started:
- `postgres` → `localhost:5432`
- `mosquitto` → `localhost:1883`
- `core-api` → `localhost:8080`

### 3. Start the frontend

```bash
cd frontend
npm install
ng serve
```

Angular dashboard → `http://localhost:4200`

### 4. API Documentation

```
http://localhost:8080/swagger-ui.html
```

---

## 🔐 Authentication

```bash
# Register
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@energysense.com", "password": "secret"}'

# Login → returns JWT token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@energysense.com", "password": "secret"}'

# Use token
curl http://localhost:8080/api/sites \
  -H "Authorization: Bearer <token>"
```

---

## 📡 REST API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register user |
| POST | `/auth/login` | Login → JWT |
| GET | `/api/sites` | List all sites |
| POST | `/api/sites` | Create site |
| GET | `/api/sites/{id}/sensors` | List sensors |
| POST | `/api/sensors/{id}/readings` | Ingest reading |
| POST | `/api/sensors/{id}/threshold` | Set alert threshold |
| GET | `/api/sensors/{id}/alerts` | Get alerts |

---

## ☸️ Kubernetes Deployment

```bash
# Start Minikube
minikube start --driver=docker

# Deploy via ArgoCD (GitOps)
kubectl apply -f k8s/

# Access the API
minikube service energysense-core-api --url
# → http://192.168.49.2:30080
```

### CI/CD Pipeline

```
git push
    │
    ▼
Jenkins (CI)
    │  build · test · docker build · push to Docker Hub
    ▼
ArgoCD (CD)
    │  detects change on feature/k8s-manifests
    ▼
Minikube
    │  rolling update
    ▼
Running pod (oussemamsehli/energysense-core-api:latest)
```

---

## 📊 Observability

```bash
# Grafana dashboard
kubectl port-forward deployment/prometheus-grafana 3000:3000 -n monitoring
# → http://localhost:3000  (admin / admin123)

# Prometheus metrics
kubectl port-forward svc/prometheus-kube-prometheus-prometheus 9090:9090 -n monitoring
# → http://localhost:9090

# Loki logs
# Available as datasource in Grafana
```

Metrics exposed at `/actuator/prometheus` via Spring Boot Actuator.

---

## 🏗️ Infrastructure as Code

```bash
cd terraform/
terraform init
terraform plan
terraform apply
```

Security scanning with Checkov — **17 checks passing**.

```bash
checkov -d terraform/
```

---

## 🧪 Testing

```bash
cd core-api/
mvn test
```

Unit tests follow hexagonal architecture — no Spring context, no database.  
Fakes replace real adapters (`FakeSensorRepository`, `FakeReadingRepository`, etc.)

---

## 📁 Project Structure

```
energysense/
├── core-api/                    # Spring Boot backend
│   └── src/main/java/
│       └── com/energysense/
│           ├── domain/          # Pure domain models & logic
│           ├── application/     # Use cases & ports
│           └── infrastructure/  # Adapters (JPA, MQTT, Security)
├── frontend/                    # Angular 18 dashboard
│   └── src/app/
│       ├── dashboard/
│       ├── sensors/
│       ├── sites/
│       ├── alerts/
│       ├── sidebar/
│       ├── layout/
│       └── interceptors/
├── k8s/                         # Kubernetes manifests
├── terraform/                   # IaC
├── monitoring/                  # Prometheus/Grafana/Loki config
├── Jenkinsfile                  # CI pipeline
└── docker-compose.yml
```

---

## 🌿 Git Branching Strategy

```
main          ← stable releases (tagged)
└── develop   ← integration branch
    ├── feature/jwt-auth
    ├── feature/rest-api
    ├── feature/unit-tests
    ├── feature/dockerfile
    ├── feature/jenkins-pipeline
    ├── feature/terraform
    ├── feature/observability
    ├── feature/k8s-manifests
    └── feature/frontend
```

All feature branches merged via `--no-ff` for full traceability in the commit graph.

---

## 👤 Author

**Oussema Msehli**  
Cloud Engineering Student — ESPRIT, Tunisia  
Background in HVAC & Refrigeration Engineering

[![GitHub](https://img.shields.io/badge/GitHub-Oussemamsehli-181717?style=flat-square&logo=github)](https://github.com/Oussemamsehli)

---

<div align="center">
<sub>Built with ☕ Java, 🌿 Spring Boot, and a lot of <code>kubectl logs</code></sub>
</div>
