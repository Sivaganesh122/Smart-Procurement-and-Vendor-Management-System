# Smart Procurement & Vendor Management System

A comprehensive enterprise procurement and vendor management system built with **Spring Boot 3**, **Spring Security 6**, **Thymeleaf**, and **MySQL**.

---

## 🌟 Key Features

* **Vendor Management**: Register and maintain supplier directories with contact details, address, ratings, and status (`ACTIVE`, `INACTIVE`, `PENDING_APPROVAL`). Export supplier reports to PDF and Excel via JasperReports.
* **Requisition Workflow**: Create procurement requests with detailed item specifications, quantities, and unit pricing. Role-based access ensures requests are tracked from initiation to approval.
* **Purchase Orders**: Generate POs linked directly to verified suppliers with line items and total order valuation. Auto-creates approval tasks for management review.
* **Approval Center**: Unified review hub for managers and administrators to inspect requisitions and purchase orders, supply feedback/rejection comments, and record decision audit trails.
* **Real-time Metrics Dashboard**: Centralized dashboard displaying live statistics for vendors, requisitions, purchase orders, and pending approval tasks.
* **Role-Based Access Control (RBAC)**:
  * **ADMIN**: Full system access, vendor management, requisition review, purchase order lifecycle, and approval center.
  * **PROCUREMENT_MANAGER**: Supplier management, purchase order generation, and approval decisions.
  * **EMPLOYEE**: Requisition creation and tracking.

---

## 🛠️ Tech Stack

* **Backend**: Java 17+, Spring Boot 3.3.3, Spring Data JPA, Spring Security
* **Frontend**: Thymeleaf, Responsive Modern CSS
* **Database**: MySQL 8.x
* **Reporting**: JasperReports (PDF & Excel generation)
* **Build Tool**: Apache Maven

---

## 🚀 Getting Started

### 1. Prerequisites
* Java Development Kit (JDK 17 or higher)
* MySQL Server (running on `localhost:3306`)
* Apache Maven

### 2. Database Configuration
Create the database in MySQL:
```sql
CREATE DATABASE procurement_db;
```

Update your database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/procurement_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run the Application
Open a terminal in the project directory:
```bash
mvn clean spring-boot:run
```

### 4. Access the Application
* **Web UI**: [http://localhost:8083/login](http://localhost:8083/login)
* **Swagger API Docs**: [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)

---

## 👥 Default Demo Credentials

| Role | Username | Password |
|---|---|---|
| **Admin** | `admin` | `admin123` |
| **Manager** | `manager` | `manager123` |
| **Employee** | `employee` | `employee123` |
