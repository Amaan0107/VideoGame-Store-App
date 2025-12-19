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

### 📃 Order
- Retrieves the current user’s shopping cart 
- Creates a new record in the orders table
- Creates a record in order_line_items for each cart item
- Clears the shopping cart after the order is created

---

- ## 🧪 Testing & Validation
- Automated API validation using Postman collections
- All tests pass successfully (0 failures)
<img width="1363" height="890" alt="Screenshot 2025-12-18 110805" src="https://github.com/user-attachments/assets/1b8b7bad-ea98-40ab-98dd-19eaaf000911" />
<img width="882" height="872" alt="Screenshot 2025-12-19 075646" src="https://github.com/user-attachments/assets/cc557ec9-db1b-4e7b-a9e3-62c59acda471" />


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
<img width="1919" height="872" alt="Screenshot 2025-12-19 020404" src="https://github.com/user-attachments/assets/632e0ad0-8b0d-4069-92dd-ffa7b10eb250" />
<img width="1915" height="875" alt="Screenshot 2025-12-19 020417" src="https://github.com/user-attachments/assets/4e51d385-c725-4c4e-8c61-6df5100fce24" />
<img width="1919" height="865" alt="Screenshot 2025-12-19 020448" src="https://github.com/user-attachments/assets/6b40c317-cc0c-4f69-a3e4-dfc6867c607b" />
<img width="1919" height="869" alt="Screenshot 2025-12-19 020459" src="https://github.com/user-attachments/assets/a6d76c46-d979-4290-ac62-942c3f50bcf0" />


