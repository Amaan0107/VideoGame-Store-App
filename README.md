# Video Game Store API (Spring Boot, Java, MySQL) 🎮

This project is a Video-Game-Store REST API for an e-commerce platform built with Java, Spring Boot, and MySQL. I worked within an existing codebase to debug, extend, and harden backend functionality, focusing on correctness, security, and test coverage.

---

## 🚀 Key Features

### 🔐 Authentication & Authorization
- JWT-based authentication for login/registration
- Role-based access control
- Admin-only protections for write operations

---

### 🗂️ Category Management
- Full CRUD support for product categories
- Admin-only create/update/delete operations
  
---

### 🛍️ Product Management & Bug Fixes
- Fixed incorrect product search/filter behavior
- Fixed a bug where product updates created duplicate rows
- Admin-only product creation, updates, and deletion

---

### 🛒 Shopping Cart (Database-Persisted)
- Shopping cart for authenticated users that persists in the database
- Add-to-cart behavior:
  - Inserts a new cart item if the product isn’t already in the cart
  - Increments quantity if the product is already present
- Clear cart support to remove all items for the current user
- Quantity update support via a PUT-style endpoint

---

### 👤 User Profile
- Authenticated users can view and update their profile
- Profile operations are scoped to the logged-in user

---

- ## 🧪 Testing & Validation
- Automated API validation using Postman collections
- All tests pass successfully (0 failures)
<img width="1363" height="890" alt="Screenshot 2025-12-18 110805" src="https://github.com/user-attachments/assets/1b8b7bad-ea98-40ab-98dd-19eaaf000911" />
<img width="1358" height="871" alt="Screenshot 2025-12-18 110749" src="https://github.com/user-attachments/assets/9a501c21-d4d1-4038-b210-5e4e50c3da7e" />

