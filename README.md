# 🛠️ Kanban Application Monorepo

[![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Node.js](https://img.shields.io/badge/Node.js-22-339933?logo=nodedotjs)](https://nodejs.org/)
[![Express](https://img.shields.io/badge/Express-4.21-000000?logo=express)](https://expressjs.com/)
[![React](https://img.shields.io/badge/React-19-blue?logo=react)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-7.0-646CFF?logo=vite)](https://vitejs.dev/)

This is a task/project management tool based on the Kanban method, where work is visualized as cards that move across columns representing stages of a workflow.

---

## 📖 Table of Contents
- [Architecture Overview](#-architecture-overview)
- [Project Structure](#-project-structure)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Core Kanban Concept](#-core-kanban-concept)
- [Minimal Kanban Features (MVP)](#-minimal-kanban-features-mvp)
- [Advanced Features (Optional)](#-advanced-features-optional)
- [Development Guidelines](#-development-guidelines)

---

## 🚀 Architecture Overview

This project follows a **Monorepo** architecture pattern, centralizing the frontend application, multiple backend server implementations, and shared packages into a single repository.

**Why?**
- **Consistency:** Shared API contracts between frontend and backend minimize integration issues.
- **Flexibility:** Supports multiple backend implementations (Spring Boot, Express) within the same ecosystem.
- **Efficiency:** Shared tooling and configurations across all sub-projects.

---

## 📁 Project Structure

```text
kanban-mono-repo/
├── apps/
│   ├── client/           # React 19 + Vite frontend
│   └── servers/
│       ├── express/      # Node.js 24 + Express 5.2.1 server
│       └── spring-boot/  # Java 25 + Spring Boot 4.0.1 server
├── packages/
│   └── api-contract/     # OpenAPI specifications and shared definitions
├── tools/                # Shared scripts and configuration tools
└── README.md
```

---

## 💻 Tech Stack

### 🧩 Frontend
- **React 19**
- **Vite 7.0**
- **Drag & drop:**
  - **`@dnd-kit/core`** (modern, recommended)
- **State management:**
  - **`RTK 2.0 and Redux 5.0`** (modern, recommended)

### 🧱 Backend (Spring Boot)
- **Java 25**
- **Spring Boot 4.0.1**
- **JPA / Hibernate**
- **PostgreSQL**
- **Auth: JWT & Social Media Login (OAuth2)**
- **`WebSocket / STOMP` (optional) for real-time updates**

### ⚡ Backend (Express - Optional)
- **Node.js 24**
- **Express 5.2.1**
- **Sequelize**
- **PostgreSQL**
- **Auth: JWT & Social Media Login (OAuth2)**

### 📁 Basic Data Model
```text
User
Board
Column
Card
```
Example relations:
- **Board → many Columns**
- **Column → many Cards**
- **Card → assigned to User**

---

## 🛠️ Getting Started

### 📋 Prerequisites
- **Node.js** (v24+)
- **Java** (v25)
- **PostgreSQL**

### 🚀 Installation & Running

#### 💻 Frontend (Client)
```bash
cd apps/client
npm install
npm run dev
```

#### ⚡ Backend (Express)
```bash
cd apps/servers/express
npm install
npm start
```

#### 🧱 Backend (Spring Boot)
```bash
cd apps/servers/spring-boot
./mvnw spring-boot:run
```

---

## 📄 API Documentation
The API is documented using OpenAPI specifications. You can find the detailed documentation and the `open-api.yaml` file in the [api-contract](./packages/api-contract) package.

---

## 🧠 Core Kanban Concept
Think of a board with columns and cards.

### 1️⃣ Board
A board represents a project (e.g. “Personal Tasks”, “Sprint 12”).

### 2️⃣ Columns (Workflow Stages)
Columns → represent stages of work

Typical examples:
- **Backlog**
- **To Do**
- **In Progress**
- **Review**
- **Done**

### 3️⃣ Cards (Tasks)
Each card represents a task and usually contains:
- **Title**
- **Description**
- **Status (based on column)**
- **Assignee**
- **Priority**
- **Due date**
- **Labels/tags**

Cards move left → right as work progresses.

---

## 📊 Minimal Kanban Features (MVP)

### ✅ Create board
Boards (e.g., “Personal”, “Project A”)

### ✅ Create columns
Columns per board (configurable)

### ✅ Create cards
Cards (CRUD)

### ✅ Drag card between columns
Drag & drop between columns

### ✅ Persist order (important!)
Persistent storage (DB)

### ✅ Login / Auth
- JWT based authentication
- Social media registration and login (OAuth2)

---

## 🚀 Advanced Features (Optional)

- **WIP limits per column**
- **Swimlanes**
- **Activity log**
- **Real-time collaboration**
- **Analytics (cycle time, lead time)**
- **Permissions (admin/member/viewer)**

---

## 🛠️ Development Guidelines

To ensure consistency and efficiency while working in this monorepo:

### 🔍 Code Scanning
When using code analysis or scanning tools:
- **Ignore scan for ignorable files or folders:** Exclude build artifacts, logs, and temporary files.
- **Ignore `node_modules`:** Always exclude `node_modules` from scans to improve performance and avoid noise.

---

