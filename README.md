# Bank Transfer Scheduling API

A Spring Boot REST API for managing scheduled bank transactions, calculating transfer fees according to business rules, and providing full CRUD operations with validation, DTOs, Swagger documentation, and global exception handling.

---

## 📌 Features

* Create, read, update and delete bank transfer schedules
* Automatic fee calculation following specific business rules
* DTO-based input/output
* Validation with Jakarta Bean Validation
* Global exception handler
* Swagger/OpenAPI documentation
* Unit tests for business logic and exception handling

---

## 🚀 Requirements

* Java 17+
* Maven 3.8+
* IntelliJ IDEA (recommended)

---

## ▶️ How to Run the Application

### **1. Clone the repository**

```bash
git clone <your-repository-url>
cd crud_project
```

### **2. Build the project**

```bash
./mvnw clean package
```

### **3. Run the application**

```bash
./mvnw spring-boot:run
```

The server will start on:

```
http://localhost:8080
```

---

## 📖 API Documentation (Swagger)

Once the app is running, open:

```
http://localhost:8080/swagger-ui/index.html
```

This provides interactive documentation for all endpoints.

---

## 📌 Endpoints

### **Create Transaction**

`POST /api/transactions`

### **List All Transactions**

`GET /api/transactions`

### **Get Transaction by ID**

`GET /api/transactions/{id}`

### **Update Transaction**

`PUT /api/transactions/{id}`

### **Delete Transaction**

`DELETE /api/transactions/{id}`

---

## 📦 Example cURL Requests

### **Create a Transaction**

```bash
curl -X POST http://localhost:8080/api/transactions \
 -H "Content-Type: application/json" \
 -d '{
   "originAccount":"PT5000",
   "destinationAccount":"PT6000",
   "amount":1500,
   "scheduledDate":"2025-11-20"
 }'
```

### **Get All Transactions**

```bash
curl http://localhost:8080/api/transactions
```

### **Get Transaction by ID**

```bash
curl http://localhost:8080/api/transactions/1
```

### **Update Transaction**

```bash
curl -X PUT http://localhost:8080/api/transactions/1 \
 -H "Content-Type: application/json" \
 -d '{
   "originAccount":"PT999",
   "destinationAccount":"PT222",
   "amount":2000,
   "scheduledDate":"2025-11-20"
 }'
```

### **Delete Transaction**

```bash
curl -X DELETE http://localhost:8080/api/transactions/1
```

---

## ⚙️ Fee Calculation Rules

### **Tax A — Amount 0€ to 1000€**

* Scheduled for **today**
* **Fee = 3% + 3€**

### **Tax B — Amount 1001€ to 2000€**

* Scheduled **1 to 10 days** from today
* **Fee = 9%**

### **Tax C — Amount above 2000€**

* **11–20 days** → 8.2%
* **21–30 days** → 6.9%
* **31–40 days** → 4.7%
* **More than 40 days** → 1.7%

---

## 🧪 Running Tests

```bash
./mvnw test
```

The test suite includes:

* Tax calculation tests
* Global exception handler tests
* Controller-level validation tests

---

## 📁 Project Structure

```
src/main/java
 └── natixis/crud_project/transfer
     ├── controllers
     ├── dto
     ├── exceptions
     ├── models
     ├── repositories
     ├── services
     ├── utils
     └── config
```

---

This project was created for a job interview technical challenge.
