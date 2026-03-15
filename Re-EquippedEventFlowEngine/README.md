**Re-Equipped EventFlowEngine 🚀**



A robust **event-driven backend engine** built using **Java 21, Spring Boot, Docker, and PostgreSQL.** 

The project demonstrates core backend engineering concepts such as **concurrency, reliability,** 

**event recovery, and fault-tolerant event processing.**

The system simulates how real event-driven systems operate internally by implementing **producer-consumer architecture, retry mechanisms, dead letter queues, and persistent event storage.**









**Features ✨**





* **Event Queue System:** Implements **Producer → Queue → Consumer** architecture.



* **Reliable Event Processing:** Events are **persisted and acknowledged** to ensure exactly-once style processing and prevent duplicate execution.



* **Recovery \& Failover:** On application restart the **Recovery Engine automatically restores unprocessed events** from file storage.



* **Dead Letter Queue (DLQ):** The system simulates how real event-driven systems operate internally by implementing p**roducer-consumer architecture, retry mechanisms, dead letter queues, and persistent event storage.**



* **Concurrent Consumers:** **Multiple consumer threads process events concurrently** to simulate high-throughput backend systems.



* **Logging \& Monitoring:** Detailed logs for **event publishing, consumption, retry attempts and recovery.**



* **Dockerized:** The entire system **runs using Docker \& Docker Compose**, ensuring consistent environments across machines.









**Architecture🏛️**



The following diagram illustrates the internal architecture of the EventFlowEngine:



!\[EventFlowEngine Architecture](docs/architecture.png)





Producer

&#x20;  **↓**

Event Queue

&#x20;  **↓**

Consumer Threads

&#x20;  ↓

Acknowledgement / Retry

&#x20;  ↓

Dead Letter Queue

&#x20;  ↓

Persistent Storage

&#x20;  ↓

Recovery Engine (on restart)





**EXPLANATION: 🔤**



**##** Events are produced through **REST APIs** and **placed into an in-memory queue.**

&#x20;

**## Multiple consumer threads** process the events concurrently. 



**##** Successful events are acknowledged and persisted to PostgreSQL, while **failed events are retried or moved to a Dead Letter Queue (DLQ).** 



**##** During application restart, **the Recovery Engine restores any unprocessed events.**









**Tech Stack 🛠️**





* **Java 21 / JDK 21 –** Latest language features and concurrency utilities.



* **Spring Boot –** Dependency injection, scheduling, and component management.



* **PostgreSQL –** Persistent storage for events.



* **Docker \& Docker Compose –** Containerized setup for reproducible environments.



* **Maven –** Build and dependency management.









**Getting Started 🚀**





Follow these steps to run the engine locally:



**1. Prerequisites**



* Java 21 installed



* Docker \& Docker Compose installed



* Maven installed









**2. Clone the Repository ©️**



git clone https://github.com/pihhuusrivastavv/Re-EquippedEventFlowEngine.git

cd Re-EquippedEventFlowEngine









**3. Set Up and Run system (Docker) 📐**



Start **PostgreSQL** and the **Spring Boot application**:



docker compose --build



This will start **PostgreSQL container and EventFlowEngine application container** with default credentials:



**##** username: postgres

**##** password: postgres

**##** database: eventflowdb









4\. **Verify Running Containers**



docker ps



**Expected containers:**



eventflow-app

eventflow-postgres





**5. REST APIs 🔚**



The EventFlowEngine exposes the following endpoints:



**POST   /events**       → create a new event  

**GET    /events**       → retrieve all events  

**GET    /events/{id}**  → retrieve event by ID  

**DELETE /events/{id}**  → delete event





**## Example Request**



**Create Event** 



POST /events



{

&#x20; "msg": "Sample Event",

&#x20; "type": "INFO"

}







**6. Verify Events 🔎**



**->** You can monitor the logs for:



* **Event publishing:** Produced Event id=X type=... message=Event-X



* **Event consumption:** Event consumed | message=Event-X



* **Event recovery:** Recovered event: X



**Example from Project:**



eventflow-app       | 2026-03-15T09:11:32.669Z  INFO 1 --- \[Re-EquippedEventFlowEngine] \[nio-8080-exec-2] c.a.R.QueueInitialization.EventQueue     : Event published | message= null queueSize= 1









**Project Workflow 🔄**



1. **EventProducer –** Creates events and persists them in the file system.



2\. **EventQueue –** Stores events temporarily for consumers.



3\. **EventConsumer –** Processes events concurrently, skipping already confirmed ones.



4\. **EventRecovery –** Recovers any unprocessed events during startup.



5\. **DeadLetterQueue –** Tracks failures for retry or debugging.









**Why This Project? 💡**



This project was built to practice **core backend engineering concepts** used in distributed systems:



* Producer–Consumer architecture



* Concurrency with multithreading



* Fault tolerance and retry mechanisms



* Event persistence and recovery



* Dead Letter Queue handling



These patterns are widely used in Kafka, RabbitMQ, and large-scale event-driven systems.









**Optional: Running without Docker 🤝**



If running locally:



**##** mvn clean install



\## mvn spring-boot:run



**Ensure PostgreSQL** is running locally.







**Contact 📬**



* Author: Pihu Srivastav 



* Email: pihusrivastav97@gmail.com



* GitHub: https://github.com/pihhuusrivastavv









**Notes 📝**



* &#x20;Compatible with JDK 21, Docker, and PostgreSQL 18+.



* &#x20;Fully containerized for development and deployment.



* &#x20;Logs provide detailed insights for debugging and performance analysis









**License 📜**



This project is intended for **educational and backend engineering practice purposes.**







