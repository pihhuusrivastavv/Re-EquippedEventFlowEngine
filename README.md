# 🚀 Re-Equipped EventFlowEngine

## 📖 Overview

Re-Equipped EventFlowEngine is a **fault-tolerant, event-driven backend system** built using **Java 21, Spring Boot, Docker, and PostgreSQL**.
It is engineered to simulate **production-grade event processing pipelines**, emphasizing **concurrency, reliability, failure handling, and system resilience**—core principles of modern distributed backend systems.

The system mirrors real-world architectures by implementing **asynchronous processing, retry orchestration, dead-letter handling, persistent storage, and recovery mechanisms**, ensuring robustness under failure scenarios.

---

## ⚙️ Key Features

* **Event-Driven Architecture** → Producer → Queue → Consumer pipeline
* **Concurrent Processing** using multi-threaded consumer workers
* **Reliable Execution** with acknowledgement and retry strategies
* **Dead Letter Queue (DLQ)** for isolating failed events
* **Crash Recovery Mechanism** for restoring unprocessed events
* **Persistent Storage** with PostgreSQL integration
* **Containerized Deployment** using Docker & Docker Compose
* **Detailed Logging & Monitoring** for full event lifecycle visibility

---

## 🏗️ Architecture

![Architecture Diagram](docs/architecture.png)

**Flow:**
Producer → Event Queue → Consumer Workers → Acknowledgement / Retry → DLQ → Database → Recovery Engine

---

## 🔄 Processing Workflow

* Events are ingested via **REST APIs** and placed into an **in-memory queue**
* Multiple **consumer threads process events concurrently**, enabling high throughput
* Successfully processed events are **acknowledged and persisted to PostgreSQL**
* Failed events undergo **controlled retries**, then are redirected to the **Dead Letter Queue (DLQ)**
* On restart, the **Recovery Engine reloads and reprocesses unacknowledged events**, ensuring continuity

---

## 🛠️ Tech Stack

**Java 21** • **Spring Boot** • **PostgreSQL** • **Docker** • **Maven**

---

## 🚀 Getting Started

### Run with Docker

```bash
docker compose --build
```

### Verify Containers

```bash
docker ps
```

**Default Database Configuration:**

* username: `postgres`
* password: `postgres`
* database: `eventflowdb`

---

## 🔌 REST API Endpoints

* `POST /events` → Publish a new event
* `GET /events` → Retrieve all events
* `GET /events/{id}` → Retrieve event by ID
* `DELETE /events/{id}` → Delete event

### Sample Request

```json
{
  "msg": "Sample Event",
  "type": "INFO"
}
```

---

## 📊 Sample Execution (API + Output)

**POST /events → Successfully processed and persisted**

```json
{
  "id": 10,
  "status": "processed"
}
```

---

## 🔎 Observability & Sample Execution

The system provides **granular logs** capturing the complete lifecycle of events, including publishing, processing, retries, persistence, and shutdown behavior.

### Sample Runtime Logs

```
INFO  EventConsumer        : Confirmed Event 10  
INFO  EventStore           : Event stored to Database  
INFO  EventConsumer        : Event processed successfully: 10  

INFO  EventQueue           : Queue shutdown signal received  
INFO  EventProducer        : Queue shutdown initiated  
INFO  EventConsumer        : Consumer shutdown observed  

INFO  DispatcherServlet    : Initialized  
INFO  EventQueue           : Event published | queueSize=1  

PostgreSQL: checkpoint complete
```

**Highlights:**

* Successful event processing & persistence
* Graceful shutdown handling
* Queue lifecycle management
* Database interaction (Hibernate + PostgreSQL)

---

## 🧠 Core Concepts Demonstrated

Event-Driven Systems • Concurrency & Multithreading • Fault Tolerance •
Retry Mechanisms • Dead Letter Queue (DLQ) • Data Persistence • System Resilience

---

## 💡 Design Focus

Built to reflect backend patterns used in systems like **Kafka and RabbitMQ**, emphasizing **high-throughput processing, failure isolation, and data consistency**.

---

## 🔁 Run Without Docker

```bash
mvn clean install
mvn spring-boot:run
```

---

## 📬 Contact

**Author:** Pihu Srivastav
**GitHub:** https://github.com/pihhuusrivastavv

---
