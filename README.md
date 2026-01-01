
# 🛒 E-Commerce Backend Application

## 📌 Project Overview

This is a **backend-only E-commerce application** built using **Java and Spring Boot**.
The project focuses on implementing **real-world ecommerce backend workflows**, clean architecture, security, and scalability while following **SOLID principles** and common **design patterns**.

The application exposes **RESTful APIs** that can be consumed by any frontend (Web / Mobile).

---

## 🛠️ Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Security (JWT Authentication)**
* **Spring Data JPA**
* **MySQL**
* **Maven**
* **Lombok**
* **Postman** (API testing)

---

## 🧱 Architecture & Design

* **Layered Architecture**

    * Controller
    * Service (interface + implementation)
    * Repository
    * DTO
    * Entity
* **DTO-based API design**
* **Centralized exception handling**
* **Role-based access control**
* **Snapshot-based order and reporting design**

### Design Patterns Used

* **Strategy Pattern** → Payment methods
* **Factory Pattern** → Payment strategy selection
* **Singleton Pattern** → Security and configuration components
* **Observer Pattern** → Order status change notifications

---

## 🔐 Authentication & Authorization

* JWT-based authentication
* Roles:

    * `ADMIN`
    * `CUSTOMER`
* Authorization enforced using:

    * Spring Security filter chain
    * Method-level security (`@PreAuthorize`)
* Proper HTTP status codes:

    * `401 Unauthorized` → unauthenticated
    * `403 Forbidden` → authenticated but not allowed

---

## 📦 Features Implemented (Mapped to Requirements)

### ✅ Product Management

* Admin CRUD operations
* Public product listing
* Filtering by:

    * Category
    * Price range
* Pagination support
* Product APIs return:

    * ⭐ Average rating
    * 🧮 Total review count

---

### ✅ Customer Management

* User registration & login
* Profile view & update
* Address book management
* Address ownership validation

---

### ✅ Cart Management

* Add products to cart
* Update quantities
* Remove items
* Per-user cart
* Stock validation

---

### ✅ Order Management

* Place order from cart
* Address snapshot stored inside order
* Order history for customers
* Inventory deduction during order placement
* Strict order lifecycle:

  ```
  PLACED → PAID → SHIPPED → DELIVERED
  ```

> Taxes and discounts are intentionally scoped as future enhancements.

---

### ✅ Payment Processing

* Multiple payment gateways supported
* Implemented using **Strategy + Factory patterns**
* Order status updated automatically based on payment outcome
* Admin cannot manually mark orders as PAID

---

### ✅ Inventory Management

* Stock reduced when order is placed
* Prevents overselling

---

### ✅ Reviews & Ratings

* Customers can rate and review products
* **Only verified buyers** can add reviews
* One review per user per product
* Update and delete own reviews
* Pagination on reviews
* Product rating stored as an aggregated snapshot

---

### ✅ Admin Features

* Admin product management
* Admin order management
* Strict order status transitions
* View all orders
* **Admin Reports**

    * Sales summary
    * Top-selling products

> Reports are derived from order data and are read-only.

---

## 📊 Admin Reports

### Sales Summary

* Total number of orders
* Total revenue
* Supports optional date range filtering
* Considers only `PAID` and `DELIVERED` orders

### Top-Selling Products

* Product name (snapshot)
* Quantity sold
* Revenue per product

---

## 🧪 Testing

* All APIs tested end-to-end using **Postman**
* Covered:

    * Happy paths
    * Negative scenarios
    * Role-based access
    * Ownership validation
    * Pagination
    * Reporting queries

---

## ⚙️ Running the Application

* Local Setup
  * Database configuration is defined using Spring configuration files
  * Maven configuration is managed using a custom maven-settings.xml
  
* Running the Application (IntelliJ)
  * A Maven Run Configuration is created in IntelliJ
  * The application is started using the following Maven command:
    > spring-boot:run -s maven-settings.xml
* This setup was used during local development and testing.

--- 

## 📌 Future Enhancements (Planned)

* Taxes calculation
* Discount / coupon system
* Advanced revenue analytics dashboards
* Microservices-based architecture

> The project is designed as a **modular monolith**, making future expansion straightforward.

---

## 🧠 Key Learnings

* Secure JWT-based authentication
* Role-based authorization
* Snapshot-based domain modeling
* Clean service-layer validation
* Proper separation of concerns
* Read-optimized reporting queries
* Practical usage of design patterns

---

## 👨‍💻 Author

**Abhishek Deshmukh**
