# 🔐 Login Authentication System

A complete, secure, console-based Login Authentication System built with **Core Java**, **MySQL**, and **JDBC**.

> **Resume-ready project** demonstrating backend development, database connectivity, OOP design, and security best practices.

---

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

---

## 🏗️ Project Structure

```
Login Autentication System/
├── lib/                              # External JARs
│   ├── jbcrypt-0.4.jar
│   └── mysql-connector-j-8.x.jar
├── sql/
│   └── setup.sql                     # Database creation script
├── src/
│   └── com/auth/
│       ├── model/
│       │   └── User.java             # User POJO
│       ├── db/
│       │   └── DatabaseConnection.java  # Singleton DB connection
│       ├── util/
│       │   ├── PasswordUtil.java     # BCrypt password hashing
│       │   ├── InputValidator.java   # Input validation
│       │   └── AuthLogger.java       # File-based logging
│       ├── service/
│       │   └── AuthService.java      # Core auth logic
│       └── app/
│           └── MainApp.java          # Console UI entry point
├── logs/
│   └── auth.log                      # Auto-generated log file
├── out/                              # Compiled .class files
├── compile.bat                       # Build script (Windows)
├── run.bat                           # Run script (Windows)
└── README.md
```

---

## 📋 Prerequisites

Before running this project, ensure you have:

1. **Java JDK 17+** — [Download](https://www.oracle.com/java/technologies/downloads/)
   - Verify: `java --version` and `javac --version`
2. **MySQL 8.x** — [Download](https://dev.mysql.com/downloads/mysql/)
   - Verify: `mysql --version`

---

## 🚀 Setup & Run

### Step 1: Clone / Download the Project

```bash
cd "c:\programming\projects\Login Autentication System"
```

### Step 2: Download Required JARs

Download and place these JARs in the `lib/` folder:

| JAR | Download Link |
|-----|--------------|
| **jBCrypt 0.4** | [Maven Central](https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar) |
| **MySQL Connector/J** | [MySQL Downloads](https://dev.mysql.com/downloads/connector/j/) — Select "Platform Independent" → Download the `.jar` file |

> ⚠️ Make sure the JARs are placed directly inside the `lib/` folder.

### Step 3: Set Up the Database

```bash
# Login to MySQL
mysql -u root -p

# Run the setup script
source sql/setup.sql
```

Or run directly:
```bash
mysql -u root -p < sql/setup.sql
```

### Step 4: Update Database Credentials

Open `src/com/auth/db/DatabaseConnection.java` and update:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/auth_system";
private static final String DB_USER = "root";           // Your MySQL username
private static final String DB_PASSWORD = "password";   // Your MySQL password
```

### Step 5: Compile

```bash
compile.bat
```

### Step 6: Run

```bash
run.bat
```

---

## 🎮 Usage

```
  ╔══════════════════════════════════════════════╗
  ║     🔐  LOGIN AUTHENTICATION SYSTEM  🔐      ║
  ║     Secure • Fast • Reliable                 ║
  ╚══════════════════════════════════════════════╝

  ┌──────────────────────────────┐
  │        MAIN MENU             │
  ├──────────────────────────────┤
  │   1.  📝  Register           │
  │   2.  🔑  Login              │
  │   3.  🚪  Exit               │
  └──────────────────────────────┘
```

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

---

## 🔒 Security Implementation Details

### Password Hashing (BCrypt)
```java
// Hashing — automatic salt generation (12 rounds)
String hash = BCrypt.hashpw(password, BCrypt.gensalt(12));

// Verification — salt extracted from stored hash
boolean match = BCrypt.checkpw(password, storedHash);
```

### SQL Injection Prevention
```java
// ✅ Safe: PreparedStatement with parameterized queries
PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
stmt.setString(1, username);

// ❌ Unsafe: String concatenation (NEVER used in this project)
// Statement stmt = conn.createStatement("SELECT * FROM users WHERE username = '" + username + "'");
```

### Account Lockout
- Failed attempts counter increments on each wrong password
- After 3 failures → `account_locked = TRUE` in database
- Locked accounts cannot login until manually unlocked by admin

---

## 🗄️ Database Schema

```sql
CREATE TABLE users (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    email           VARCHAR(100) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    failed_attempts INT          DEFAULT 0,
    account_locked  BOOLEAN      DEFAULT FALSE,
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);
```

---

## 📂 Class Responsibilities

| Class | Layer | Responsibility |
|-------|-------|----------------|
| `User` | Model | POJO representing a user record |
| `DatabaseConnection` | Data | Singleton JDBC connection manager |
| `PasswordUtil` | Utility | BCrypt password hashing & verification |
| `InputValidator` | Utility | Email, password, username validation |
| `AuthLogger` | Utility | File & console logging for audit trail |
| `AuthService` | Service | Core registration & login business logic |
| `MainApp` | Application | Console-based UI and user interaction |

---

## 🛠️ Tech Stack

- **Language:** Java 17+ (Core Java — no frameworks)
- **Database:** MySQL 8.x
- **Connectivity:** JDBC (mysql-connector-j)
- **Hashing:** BCrypt (jBCrypt library)
- **Logging:** java.util.logging

---

## 📝 License

This project is open-source and available for educational purposes.

---

> Built with ❤️ as a demonstration of backend development and security fundamentals.
