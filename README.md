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

