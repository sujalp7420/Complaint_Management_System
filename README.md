# 📌 Complaint Management System (CMS)

## 🚀 Overview
The **Complaint Management System (CMS)** is a Spring Boot–based backend application designed to manage complaints efficiently with role-based access control.

It allows users to:
- Register and manage accounts
- Create and track complaints
- Add comments and attachments
- Manage complaint lifecycle (Open → Closed)

---

## 🛠️ Tech Stack

- **Backend:** Spring Boot  
- **Security:** Spring Security (JDBC Authentication)  
- **Database:** MySQL  
- **ORM:** Spring Data JPA (Hibernate)  
- **Mapping:** ModelMapper  
- **Build Tool:** Maven  
- **Validation:** Jakarta Validation  

---

## 📂 Project Structure
com.cms
│
├── config # Security & configuration
├── controller # REST APIs
├── dto # Data Transfer Objects
├── entity # JPA Entities
├── exception # Custom exceptions
├── repository # Database layer
├── service # Business logic
---

## 🔐 Security

- Uses **Spring Security with JDBC Authentication**
- Login is done using **email as username**
- Role-based authorization:
  - `USER`
  - `MANAGER`
  - `ADMIN`

### 🔑 Password Encoding
- Uses **BCryptPasswordEncoder**
- Passwords are securely stored in encrypted format

---

## 🧑‍💻 Features

### 👤 User Management
- Create / Update / Delete users
- Role-based access control
- User status (ACTIVE / INACTIVE)

---

### 📢 Complaint Management
- Create complaints
- Assign complaints to users
- Track complaint status:
  - OPEN
  - IN_PROGRESS
  - RESOLVED
  - CLOSED

---

### 💬 Comments
- Add comments on complaints
- Track user activity
- Maintain timestamps

---

### 📎 Attachments
- Upload complaint-related files
- Store file path in database
- Link attachments to complaints
