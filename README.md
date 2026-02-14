# SupportAI Hub 🚀

An Enterprise-grade Smart Customer Support Automation Platform. This project implements a multi-agent AI system (LangGraph) integrated with a robust Spring Boot microservices backend and a modern React dashboard.

## 🏗️ Project Architecture
- **Backend**: Java 17, Spring Boot 3, Spring Cloud, Hibernate.
- **AI Service**: Python, FastAPI, LangChain, LangGraph, RAG.
- **Frontend**: React 18, TypeScript, Vite, Material UI (MUI).
- **Data & Infra**: PostgreSQL, Redis, Apache Kafka, Elasticsearch, Docker, Kubernetes.

## 📂 Project Structure
- `/backend-services`: Spring Boot microservices (Auth, Ticket, Knowledge).
- `/ai-service`: Python FastAPI service handling agentic workflows.
- `/frontend`: React TypeScript dashboard for agents and admins.
- `/docker`: Configuration for local infrastructure setup.

## 🚀 Getting Started
1. **Infrastructure**: Run `docker-compose up -d` to start DBs, Kafka, and Redis.
2. **Backend**: Navigate to `backend-services/`, run `./mvnw clean install`.
3. **Frontend**: Navigate to `frontend/`, run `npm install` and `npm run dev`.
4. **AI Service**: Navigate to `ai-service/`, setup venv and run `uvicorn main:app`.

## 🛠️ Roadmap
- [ ] Phase 1: Foundation & Infrastructure (Current)
- [ ] Phase 2: Core Services (Auth, Ticket)
- [ ] Phase 3: AI Agent Integration
- [ ] Phase 4: Frontend UI/UX
- [ ] Phase 5: Advanced Analytics & Deployment