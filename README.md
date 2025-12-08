# Expense Report Management System

---

## Database Setup

Create a MySQL database:

```sql
CREATE DATABASE expense_db;
```

Update `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/expense_db
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

On Windows machine change following:
```
spring.datasource.url=jdbc:mysql://host.docker.internal:3306/expense_db
```

---

## Running the Application

1. Start the backend:
```
mvn clean install
mvn spring-boot:run
```

Server runs on:

```
http://localhost:8080
```
2. Open frontend/login.html manually in your browser.
   There is no build step- the UI is fully static and communicates with the backend using fetch().

---

## Insecure Mode Vulnerabilities

The `insecure` branch intentionally contains multiple vulnerabilities.

---

### Stored XSS

User input is rendered directly in the UI without sanitization.

**Payload example:**

```html
<img src=x onerror=alert('stored-xss')>
```

Open:

```
view-report.html
```

Alert will execute.

---

### Reflected XSS

Unescaped input is returned directly in the response.

**Example:**

```
GET /insecure/echo?text=<img src=x onerror=alert('reflected')>
```

Triggers immediately.

---

### SQL Injection

The insecure login endpoint concatenates raw SQL.

**Exploit:**

```json
POST /auth/login-insecure
{
  "username": "admin' OR '1'='1",
  "password": "test"
}
```

Bypasses authentication.

---

### Sensitive Data Exposure

Passwords and raw request data are logged in plaintext.

**Example log entry:**

```
event=login_raw, meta=username=maks, password=123
```

---

### Broken Access Control

Any authenticated user can approve or reject reports.

```
POST /reports/{id}/action/{userId}
```

No role checks performed.

---

### Missing Input Validation

No validation on:

* report fields
* credentials
* numeric values
* description/title content

Allows malformed/malicious payloads.

---

### Missing Security Headers

Security headers intentionally disabled:

```
Content-Security-Policy
X-Frame-Options
X-Content-Type-Options
```

---

### Logging of Sensitive Data

The application logs raw credentials during register/login operations.

Example insecure logging call:

```
loggingService.log("login_raw", null, "username=maks, password=123");
```

Stored in DB as-is.

## Authentication API

### Register

**POST** `/auth/register`

```json
{
  "username": "maks",
  "password": "123",
  "role": "employee"
}
```

### Login

**POST** `/auth/login`

```json
{
  "username": "maks",
  "password": "123"
}
```

---

## Expense Reports API

### Create Report

**POST** `/reports/create/{userId}`

```json
{
  "title": "Fuel expenses",
  "items": [
    {
      "category": "fuel",
      "description": "Gasoline",
      "amount": 40
    }
  ]
}
```

### Get Report

**GET** `/reports/{id}`

### Get Pending Reports

**GET** `/reports/pending`

### Approve / Reject

**POST** `/reports/{id}/action/{managerId}`

```json
{
  "action": "approve",
  "comment": "Looks good"
}
```

---

## Logging

All user actions (login, register, report creation, approval, rejection) are logged into the `logs` table.

---

## Branch Structure

* **main** – clean, functional version
* **insecure** – intentionally vulnerable version
* **secure** – fixed version

---

## Author

Maksym Pylypenko
National College of Ireland

