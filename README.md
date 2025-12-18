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

---

## 🧠 What I Learned
This project strengthened my ability to work inside an existing Spring Boot application, debug real issues, and extend functionality safely. I gained hands-on experience with JWT auth, role-based authorization, database-backed features, and validating behavior with automated API tests.

---

## 🔍 Interesting Code: Fixing Duplicate Products on Update

One of the most interesting issues I worked on was resolving a bug that caused updating a product (such as a video game) to create duplicate records in the database instead of modifying the existing entry. 

<img width="1198" height="388" alt="Screenshot 2025-12-18 111635" src="https://github.com/user-attachments/assets/aca788cd-effa-4523-831f-c79eedf2c3b8" />

This fix was exciting to me because it required understanding both the controller and DAO behavior, and it reinforced how small backend mistakes can lead to serious data issues if not caught.

---

## Pics of the application
<img width="1919" height="1018" alt="Screenshot 2025-12-18 095419" src="https://github.com/user-attachments/assets/6a28a664-8b04-4329-af63-d51717dbaa7c" />
<img width="1917" height="1015" alt="Screenshot 2025-12-18 095440" src="https://github.com/user-attachments/assets/efcac6f0-9512-4441-9340-62d524c2430b" />
<img width="1919" height="1014" alt="Screenshot 2025-12-18 095527" src="https://github.com/user-attachments/assets/330ac784-d081-489c-88e6-741b1524f6bc" />

