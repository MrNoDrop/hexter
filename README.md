# Hexter - The Social Media Platform

A full-stack JavaEE implementation of user authentication with registration, login, and password recovery functionality. Built with Spring Boot, React, and MySQL.

**Project Scope:** A competitor to mainstream social media platforms with a focus on secure user authentication and data management.

---

## 📋 Table of Contents

1. [Project Overview](#-project-overview)
2. [System Requirements](#-system-requirements)
3. [Prerequisites Installation](#-prerequisites-installation)
4. [Quick Start Guide](#-quick-start-guide)
5. [Backend Setup](#-backend-setup)
6. [Frontend Setup](#-frontend-setup)
7. [Running the Application](#-running-the-application)
8. [Building the Application](#-building-the-application)
9. [Running Tests](#-running-tests)
10. [Building Debian Packages](#-building-debian-packages)
11. [Configuration Guide](#-configuration-guide)
12. [Troubleshooting](#-troubleshooting)

---

## 📱 Project Overview

**Hexter** is a full-stack web application with the following components:

- **Backend:** Spring Boot 2.7.18 REST API with Spring Security
- **Frontend:** React application (TypeScript/TSX)
- **Database:** MySQL 8.0
- **Package Distribution:** Debian (.deb) packages

### Key Features

- ✅ User registration with email validation
- ✅ Secure login with JWT authentication tokens
- ✅ Password recovery via email
- ✅ Fingerprint-based device authentication
- ✅ Email notifications (Gmail integration)
- ✅ Comprehensive test coverage (unit, integration, E2E)

---

## 💻 System Requirements

### Minimum Requirements

| Component      | Version | Notes                                |
| -------------- | ------- | ------------------------------------ |
| **Java**       | 17+     | JDK required (not JRE)               |
| **Node.js**    | 16+     | For frontend development             |
| **npm**        | 8+      | Comes with Node.js                   |
| **Maven**      | 3.6+    | Build tool (included via mvnw)       |
| **MySQL**      | 8.0+    | Database server                      |
| **RAM**        | 2 GB    | For running backend + frontend       |
| **Disk Space** | 5 GB    | For dependencies and build artifacts |

### Supported Operating Systems

- ✅ Linux (Ubuntu 20.04+, Debian 10+)
- ✅ macOS (10.15+)
- ✅ Windows 10+ (with WSL2 recommended)

---

## 🔧 Prerequisites Installation

### 1. Install Java 17

#### On Linux (Ubuntu/Debian):

```bash
# Using SDKMAN (recommended)
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17.0.8-temurin
sdk default java 17.0.8-temurin

# Or using apt
sudo apt update
sudo apt install openjdk-17-jdk
```

#### On macOS:

```bash
# Using Homebrew
brew tap homebrew/cask-versions
brew install java17

# Or manually download from
# https://adoptium.net/temurin/releases/?version=17
```

#### Verify Java Installation:

```bash
java -version
# Expected output: openjdk version "17.x.x" or similar
```

### 2. Install Node.js and npm

#### Using NVM (Node Version Manager - Recommended):

```bash
# Install NVM
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

# Reload shell
source ~/.bashrc

# Install Node 20 LTS
nvm install 20
nvm use 20

# Verify installation
node --version   # Should show v20.x.x
npm --version    # Should show 10.x.x
```

#### Using Homebrew (macOS):

```bash
brew install node
```

### 3. Install MySQL Server

#### On Linux (Ubuntu/Debian):

```bash
sudo apt update
sudo apt install mysql-server

# Start MySQL service
sudo systemctl start mysql
sudo systemctl enable mysql

# Secure your installation
sudo mysql_secure_installation

# Verify installation
mysql --version
```

#### On macOS:

```bash
brew install mysql

# Start MySQL service
brew services start mysql

# Connect to verify
mysql -u root
```

#### Initial MySQL Setup:

```bash
# Connect to MySQL as root
mysql -u root -p

# Create hexter database
CREATE DATABASE hexter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Create application user (recommended for security)
CREATE USER 'hexter_user'@'localhost' IDENTIFIED BY 'secure_password_here';
GRANT ALL PRIVILEGES ON hexter.* TO 'hexter_user'@'localhost';
FLUSH PRIVILEGES;

# Exit MySQL
EXIT;
```

### 4. Install Git

```bash
# On Ubuntu/Debian
sudo apt install git

# On macOS
brew install git

# Verify
git --version
```

---

## ⚡ Quick Start Guide

For experienced developers, here's the quick version:

```bash
# 1. Clone the repository
git clone https://github.com/yourusername/hexter.git
cd hexter

# 2. Set environment variables
export JAVA_HOME=/path/to/java17
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_SCHEMA=hexter
export WEB_JAVA_IP2LOCATION_DB_LOGIN=hexter_user
export WEB_JAVA_IP2LOCATION_DB_PASSWORD=secure_password_here

# 3. Build backend
./mvnw clean install -DskipTests

# 4. Install frontend dependencies
cd src/main/javascript/be/hexter/hexter
npm install

# 5. Start backend (from project root)
cd /path/to/hexter
./mvnw spring-boot:run

# 6. Start frontend (in another terminal)
cd src/main/javascript/be/hexter/hexter
npm start

# 7. Access application at http://localhost:3000
```

---

## 📦 Backend Setup

### Step 1: Clone the Repository

```bash
# Clone from GitHub
git clone https://github.com/yourusername/hexter.git
cd hexter

# Or clone from a different source
git clone git@github.com:yourusername/hexter.git
cd hexter
```

### Step 2: Set Java Home (if not already set)

```bash
# Check current JAVA_HOME
echo $JAVA_HOME

# If not set, find your Java installation
which java
readlink -f $(which java)  # Shows full path

# Set JAVA_HOME permanently (add to ~/.bashrc or ~/.zshrc)
export JAVA_HOME=/home/patryk/.jdk/jdk-17.0.16
export PATH=$JAVA_HOME/bin:$PATH

# Reload shell
source ~/.bashrc
```

### Step 3: Verify Maven Wrapper

The project includes Maven Wrapper (`mvnw`), so you don't need to install Maven separately:

```bash
# Make executable
chmod +x mvnw

# Verify Maven wrapper works
./mvnw --version
# Expected: Apache Maven 3.8.x
```

### Step 4: Configure Database Connection

Edit or create `.properties` file with credentials:

```bash
# Create a local .properties file (not tracked by git)
cat > .properties << EOF
# MySQL Configuration
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_SCHEMA=hexter

# Database credentials
WEB_JAVA_IP2LOCATION_DB_LOGIN=hexter_user
WEB_JAVA_IP2LOCATION_DB_PASSWORD=secure_password_here
EOF
```

Or set environment variables:

```bash
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_SCHEMA=hexter
export WEB_JAVA_IP2LOCATION_DB_LOGIN=hexter_user
export WEB_JAVA_IP2LOCATION_DB_PASSWORD=secure_password_here
```

### Step 5: Build with Maven

```bash
# Clean and build (skipping tests for first build)
./mvnw clean install -DskipTests

# Expected output:
# [INFO] BUILD SUCCESS
# [INFO] Total time: X.XXs
```

This will:

- ✅ Download all dependencies
- ✅ Compile Java source code
- ✅ Build frontend (Node packages)
- ✅ Package into JAR/WAR files
- ✅ Generate build artifacts in `target/` directory

### Step 6: Build Output

After successful build, check:

```bash
# Verify JAR was created
ls -lh target/hexter-*.jar

# Expected: hexter-0.0.1-SNAPSHOT.jar (approximately 100-150 MB with dependencies)

# Verify frontend was built
ls -la src/main/javascript/be/hexter/hexter/build/

# Check build logs
tail -50 target/maven-build.log
```

---

## ⚛️ Frontend Setup

### Step 1: Navigate to Frontend Directory

```bash
cd src/main/javascript/be/hexter/hexter
```

### Step 2: Verify Node.js and npm

```bash
node --version   # Should be 16+
npm --version    # Should be 8+
```

### Step 3: Install Dependencies

```bash
# Install all npm packages
npm install

# Expected: should take 1-2 minutes
# Creates node_modules/ directory (~500MB)
```

### Step 4: Verify Frontend Build (Optional)

```bash
# Build production version
npm run build

# Check build output
ls -la build/
# Expected: static/ directory with optimized assets
```

### Step 5: Frontend Package Contents

Your `package.json` includes:

| Package          | Purpose             |
| ---------------- | ------------------- |
| react            | UI framework        |
| react-dom        | DOM rendering       |
| react-router-dom | Client-side routing |
| axios            | HTTP client         |
| typescript       | Type safety         |
| cypress          | E2E testing         |
| jest             | Unit testing        |

---

## 🚀 Running the Application

### Option 1: Full-Stack Development Mode

#### Terminal 1 - Start Backend:

```bash
cd /path/to/hexter

# Set Java home
export JAVA_HOME=/path/to/java17

# Start Spring Boot application
./mvnw spring-boot:run

# Expected output:
# Started Application in X.XXX seconds (JVM running for X.XXs)
# Tomcat started on port(s): 8080 (http)
```

The backend will be available at: **http://localhost:8080**

#### Terminal 2 - Start Frontend (React Dev Server):

```bash
cd /path/to/hexter/src/main/javascript/be/hexter/hexter

# Start React development server
npm start

# Expected output:
# webpack compiled successfully
# Compiled successfully!
# Local:            http://localhost:3000
# On Your Network:  http://xxx.xxx.xxx.xxx:3000
```

The frontend will be available at: **http://localhost:3000**

### Option 2: Production Build (JAR)

```bash
# From project root
./mvnw clean package -DskipTests

# Run the built JAR
java -jar target/hexter-0.0.1-SNAPSHOT.jar

# Or with custom parameters
java -jar target/hexter-0.0.1-SNAPSHOT.jar \
  --server.port=8080 \
  --spring.datasource.url=jdbc:mysql://localhost:3306/hexter
```

### Option 3: Docker Container (if Dockerfile exists)

```bash
# Build Docker image
docker build -t hexter:latest .

# Run container
docker run -p 8080:8080 \
  -e MYSQL_HOST=host.docker.internal \
  -e WEB_JAVA_IP2LOCATION_DB_LOGIN=root \
  -e WEB_JAVA_IP2LOCATION_DB_PASSWORD=password \
  hexter:latest
```

### Testing the Application

Once both servers are running:

```bash
# Test backend API
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "email":"test@example.com",
    "username":"testuser",
    "password":"SecurePass123!"
  }'

# Test frontend
open http://localhost:3000  # macOS
xdg-open http://localhost:3000  # Linux
start http://localhost:3000  # Windows

# Expected: Registration page loads successfully
```

---

## 🔨 Building the Application

### Clean Build (Removes Previous Build Artifacts)

```bash
cd /path/to/hexter

./mvnw clean install

# This will:
# 1. Remove target/ directory
# 2. Remove node_modules/ (via Maven plugin)
# 3. Reinstall all dependencies
# 4. Recompile all code
# 5. Build JAR and WAR files
```

### Incremental Build (Faster, Skipping Tests)

```bash
./mvnw install -DskipTests

# Faster for development, skips test execution
```

### Frontend-Only Build

```bash
cd src/main/javascript/be/hexter/hexter
npm run build

# Generates optimized frontend assets
```

### Backend-Only Build

```bash
./mvnw install -DskipTests -Dorg.slf4j.simpleLogger.defaultLogLevel=warn

# Quiet build, only essential output
```

### Build Profiles

```bash
# Development profile (default)
./mvnw install -Pdev

# Production profile
./mvnw install -Pprod

# Custom profile
./mvnw install -Pcustom-profile
```

### Build Artifacts

After successful build, check:

```bash
# Main application JAR
ls -lh target/hexter-*.jar

# Exploded WAR directory (if configured)
ls -la target/hexter/

# Frontend build output
ls -la target/classes/static/

# Maven reports
ls -la target/site/
```

---

## 🧪 Running Tests

### Backend Tests (Unit, Integration)

```bash
cd /path/to/hexter

# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=UserServiceTest

# Run tests with coverage report
./mvnw test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Frontend Tests (Unit, Component)

```bash
cd src/main/javascript/be/hexter/hexter

# Run Jest unit tests
npm test

# Run with coverage
npm run test:coverage

# Watch mode (re-runs on change)
npm run test:watch
```

### E2E Tests (Cypress)

```bash
cd src/main/javascript/be/hexter/hexter

# Open Cypress Test Runner (interactive)
npm run test:e2e

# Run headless (CI mode)
npm run cypress:run

# Run specific test file
npx cypress run --spec "cypress/e2e/login.cy.ts"
```

### Test Coverage Reports

```bash
# Java/Backend coverage
./mvnw jacoco:report
open target/site/jacoco/index.html

# JavaScript/Frontend coverage
cd src/main/javascript/be/hexter/hexter
npm run test:coverage
open coverage/lcov-report/index.html
```

### Running All Tests

```bash
# Backend + Frontend tests
./mvnw clean install
cd src/main/javascript/be/hexter/hexter && npm test

# Total coverage across project
# Backend: target/site/jacoco/index.html
# Frontend: coverage/lcov-report/index.html
```

---

## 📦 Building Debian Packages

The project includes an automated Debian (.deb) package builder with semantic versioning.

### Prerequisites for .deb Building

```bash
# Install required tools
sudo apt install build-essential debhelper fakeroot

# Make build script executable
chmod +x build-deb.sh
```

### Building .deb Package

#### Default (Auto Patch Bump: 0.0.1 → 0.0.2)

```bash
cd /path/to/hexter

./build-deb.sh

# Expected output:
# ✓ [1/6] Building Java application...
# ✓ [2/6] Building frontend assets...
# ✓ [3/6] Creating Debian directory structure...
# ✓ [4/6] Building .deb package...
# ✓ [5/6] Updating version file...
# ✓ [6/6] Package ready for distribution
#
# Version: 0.0.2
# Location: /path/to/hexter/hexter_0.0.2_all.deb
# Size: 40 MB
```

#### Version Bump Modes

```bash
# Bump minor version (0.0.2 → 0.1.0)
./build-deb.sh --bump-minor

# Bump major version (0.1.0 → 1.0.0)
./build-deb.sh --bump-major

# Set explicit version (→ 2.5.3)
./build-deb.sh --version 2.5.3

# No version change (use current version)
./build-deb.sh --no-bump
```

### Installing .deb Package

```bash
# Install the generated package
sudo dpkg -i hexter_0.0.2_all.deb

# Install dependencies (if any missing)
sudo apt-get install -f

# Verify installation
dpkg -l | grep hexter
sudo systemctl status hexter

# Start service (if enabled)
sudo systemctl start hexter
sudo systemctl enable hexter
```

### Uninstalling .deb Package

```bash
sudo dpkg -r hexter

# Or
sudo apt remove hexter
```

### .deb Package Contents

The generated .deb includes:

```
/usr/local/hexter/
├── hexter-0.0.2-SNAPSHOT.jar
├── hexter.service
└── start.sh

/etc/systemd/system/
└── hexter.service

/usr/bin/
└── hexter (symlink)
```

---

## ⚙️ Configuration Guide

### Application Properties (Backend)

Edit `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/
server.compression.enabled=true

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hexter?serverTimezone=UTC
spring.datasource.username=hexter_user
spring.datasource.password=secure_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Application Profile
spring.profiles.active=dev

# Logging
logging.level.root=INFO
logging.level.be.hexter.hexter=DEBUG

# Email Configuration (Gmail example)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

### Environment Variables

```bash
# Database
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_SCHEMA=hexter

# Credentials
export WEB_JAVA_IP2LOCATION_DB_LOGIN=hexter_user
export WEB_JAVA_IP2LOCATION_DB_PASSWORD=secure_password_here

# Java
export JAVA_HOME=/path/to/java17
export JAVA_OPTS="-Xmx2g -Xms1g"
```

### profile Files

Create profile-specific configuration:

```bash
# Development profile
src/main/resources/application-dev.properties

# Production profile
src/main/resources/application-prod.properties

# Testing profile
src/main/resources/application-test.properties
```

### Frontend Configuration (.env)

```bash
cd src/main/javascript/be/hexter/hexter

# Create .env file
cat > .env << EOF
REACT_APP_API_URL=http://localhost:8080
REACT_APP_DEBUG=true
REACT_APP_VERSION=0.0.1
EOF
```

---

## 🔍 Troubleshooting

### Java/Maven Issues

#### Problem: `Java version mismatch`

```bash
# Solution: Verify Java version
java -version

# Set correct JAVA_HOME
export JAVA_HOME=/path/to/java17
./mvnw clean install
```

#### Problem: `Maven: command not found`

```bash
# Solution: Make wrapper executable
chmod +x mvnw

# Or use full path
./mvnw --version
```

#### Problem: `BUILD FAILURE: Missing dependencies`

```bash
# Solution: Clean Maven cache
./mvnw clean
rm -rf ~/.m2/repository/
./mvnw install -DskipTests
```

### Database Issues

#### Problem: `Connection refused: localhost:3306`

```bash
# Solution: Start MySQL server
sudo systemctl start mysql

# Verify MySQL is running
sudo systemctl status mysql

# Check port
netstat -tuln | grep 3306
```

#### Problem: `Access denied for user`

```bash
# Solution: Verify credentials
mysql -h localhost -u hexter_user -p

# Reset password if needed
mysql -u root -p
ALTER USER 'hexter_user'@'localhost' IDENTIFIED BY 'new_password';
FLUSH PRIVILEGES;
EXIT;
```

#### Problem: `Database doesn't exist`

```bash
# Solution: Create database
mysql -u root -p
CREATE DATABASE hexter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON hexter.* TO 'hexter_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### Frontend Issues

#### Problem: `npm: command not found`

```bash
# Solution: Install Node.js
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
nvm install 20
nvm use 20
```

#### Problem: `npm install fails`

```bash
# Solution: Clear npm cache
npm cache clean --force
rm -rf node_modules package-lock.json
npm install
```

#### Problem: `Port 3000 already in use`

```bash
# Find and kill process
lsof -i :3000  # Find process ID
kill -9 <PID>

# Or use different port
PORT=3001 npm start
```

### Port Conflicts

#### Problem: `Port 8080 already in use`

```bash
# Find process using port
lsof -i :8080
netstat -tuln | grep 8080

# Kill the process
kill -9 <PID>

# Or use different port
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Build Issues

#### Problem: `Out of memory during build`

```bash
# Solution: Increase Java heap size
export MAVEN_OPTS="-Xmx2g -Xms1g"
./mvnw clean install

# Or edit .mvn/maven.config
echo "-Xmx2g -Xms1g" >> .mvn/maven.config
```

#### Problem: `Long build times`

```bash
# Solution: Skip tests during build
./mvnw install -DskipTests

# Run tests separately
./mvnw test

# Parallel build (faster)
./mvnw clean install -T 1C  # 1 thread per core
```

### Verification Checklist

After troubleshooting, verify:

```bash
# 1. Java is correct version
java -version | grep "17"

# 2. MySQL is running and accessible
mysql -u hexter_user -p -e "SELECT 1;"

# 3. Node.js/npm are correct versions
node --version | grep "v20"
npm --version | grep "10"

# 4. Git repository is initialized
git status

# 5. All files are present
ls -la | grep pom.xml
ls -la src/main/java
ls -la src/main/javascript/be/hexter/hexter

# 6. Ports are available
netstat -tuln | grep -E "3000|8080"
```

---

## 📝 Development Workflow

### Typical Development Session

```bash
# 1. Start in first terminal
cd /path/to/hexter
export JAVA_HOME=/path/to/java17
./mvnw spring-boot:run

# 2. Start in second terminal
cd /path/to/hexter/src/main/javascript/be/hexter/hexter
npm start

# 3. Edit code and test
# - Edit Java files → auto-reload via Spring Boot DevTools
# - Edit React files → auto-reload via React Dev Server

# 4. Run tests when making changes
./mvnw test -Dtest=UserServiceTest
npm test -- --watch
npm run test:e2e
```

### Git Workflow

```bash
# Create feature branch
git checkout -b feature/user-profile

# Make commits
git add .
git commit -m "feat: add user profile page"

# Push to remote
git push origin feature/user-profile

# Create pull request on GitHub
```

### Releasing a Version

```bash
# 1. Update version in pom.xml
# 2. Update CHANGELOG.md
# 3. Build .deb package
./build-deb.sh --version 1.0.0

# 4. Tag release
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0

# 5. Deploy
sudo dpkg -i hexter_1.0.0_all.deb
sudo systemctl restart hexter
```

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [Cypress Documentation](https://docs.cypress.io)

---

## 📄 License

[Your License Here - e.g., MIT, Apache 2.0, GPL]

## 👤 Author

**Patryk Sitko**

- GitHub: [@patryksitko](https://github.com/mrnodrop)
- Email: patryk.sitko.algemeen@gmail.com

---

**Last Updated:** March 6, 2026
**Hexter Version:** 0.0.1-SNAPSHOT
**Java Version:** 17+
**Node Version:** 20+
**MySQL Version:** 8.0+
