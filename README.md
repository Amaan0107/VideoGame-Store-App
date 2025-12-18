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
![Test 1](https://github.com/user-attachments/assets/2cefd17f-96b6-4ec8-8d04-1d05f100213d)

<img width="1919" height="1078" alt="Screenshot 2025-12-18 091514" src="https://github.com/user-attachments/assets/c537df52-5147-4a1a-9715-41545f0219a5" />

