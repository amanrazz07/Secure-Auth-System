🔐 Login Authentication System

A complete, secure, console-based Login Authentication System built with **Core Java**, **MySQL**, and **JDBC**.


## ✨ Features

| Feature | Description |
|---------|-------------|
| 🔑 User Registration | Register with username, email, and password |
| 🔓 User Login | Authenticate with username and password |
| 🔒 BCrypt Hashing | Industry-standard password hashing with automatic salting |
| 🛡️ Account Lockout | Locks account after 3 consecutive failed login attempts |
| ✅ Input Validation | Email format, password strength, username format validation |
| 🚫 SQL Injection Prevention | All database queries use PreparedStatement |
| 📝 Audit Logging | All authentication events logged to `logs/auth.log` |


### Registration
- Enter a unique username (3-50 chars, alphanumeric + underscores)
- Enter a valid email address
- Enter a strong password:
  - Minimum 8 characters
  - At least 1 uppercase letter
  - At least 1 lowercase letter
  - At least 1 digit
  - At least 1 special character

### Login
- Enter your registered username and password
- After 3 consecutive incorrect passwords, the account will be **locked**


### Account Lockout
- Failed attempts counter increments on each wrong password
- After 3 failures → `account_locked = TRUE` in database
- Locked accounts cannot login until manually unlocked by admin



## 🛠️ Tech Stack

- **Language:** Java 17+ (Core Java — no frameworks)
- **Database:** MySQL 8.x
- **Connectivity:** JDBC (mysql-connector-j)
- **Hashing:** BCrypt (jBCrypt library)
- **Logging:** java.util.logging
