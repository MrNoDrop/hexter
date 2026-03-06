# Hexter Development Environment Setup

Welcome to the Hexter project! This guide will help you set up your development environment quickly.

## Quick Start

### For Linux and macOS (Recommended)

```bash
git clone <repository-url>
cd hexter
./setup.sh
```

### For Windows

```cmd
git clone <repository-url>
cd hexter
setup.bat
```

Or if using Git Bash/WSL:

```bash
bash setup.sh
```

## Available Setup Scripts

### 1. `setup.sh` (Linux, macOS, Git Bash, WSL)

**Recommended for:** Linux, macOS, and Windows with Git Bash or WSL

This is a comprehensive, fully-featured setup script that:

- Automatically detects your OS and distribution
- Installs all required dependencies
- Configures Java environment variables
- Sets up MySQL database (optional)
- Builds the project
- Installs frontend dependencies

**Usage:**

```bash
./setup.sh                    # Interactive with prompts
./setup.sh --auto             # Automatic installation (no prompts)
./setup.sh --skip-db          # Skip database setup
./setup.sh --help             # Show detailed help
```

### 2. `setup.bat` (Windows Native)

**Recommended for:** Windows Command Prompt or PowerShell

Provides assistance in verifying prerequisites are installed.

**Usage:**

```cmd
setup.bat                     # Interactive setup
setup.bat --help              # Show help
```

## System Requirements

### Minimum Requirements

- **RAM:** 2GB
- **Disk Space:** 2GB
- **Internet Connection:** Yes (for downloads)

### Java

- **Java 17 JDK** (required)
  - Linux: `openjdk-17-jdk`
  - macOS: Homebrew or manual download
  - Windows: Download from [Adoptium](https://adoptium.net/)

### Build Tool

- **Maven 3.6+** (required)
  - Install via package manager or [maven.apache.org](https://maven.apache.org/)

### Node.js

- **Node.js 14+** and **npm 6+** (required)
  - Download from [nodejs.org](https://nodejs.org/)

### Database

- **MySQL 8.0+** (optional, for production)
  - The setup script can install this for you
  - Development uses H2 in-memory database by default

## Supported Platforms

### Linux

- ✅ Ubuntu/Debian (apt)
- ✅ Fedora/RHEL/CentOS (dnf/yum)
- ✅ Arch Linux (pacman)
- ✅ Other distributions (manual package manager)

### macOS

- ✅ Intel-based Macs
- ✅ Apple Silicon (M1, M2, etc.)
- Requires: [Homebrew](https://brew.sh/)

### Windows

- ✅ Windows 10/11 (Native Command Prompt/PowerShell)
- ✅ Windows with Git Bash
- ✅ Windows Subsystem for Linux (WSL)

## Detailed Installation Guide

### Option 1: Automatic Setup (Recommended)

#### Linux/macOS

```bash
./setup.sh --auto
```

This will:

1. Automatically detect your system
2. Install any missing Java, Maven, Node.js
3. Install MySQL (if needed)
4. Build the entire project
5. Install all dependencies

#### Windows (Native)

1. Manually install prerequisites from:
   - Java 17: https://adoptium.net/
   - Maven: https://maven.apache.org/download.cgi
   - Node.js: https://nodejs.org/
2. Add installation directories to your PATH
3. Restart your terminal
4. Run: `setup.bat`

### Option 2: Manual Setup

If you prefer to install dependencies manually:

#### 1. Install Java 17

**Linux (Ubuntu/Debian):**

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk
```

**Linux (Fedora/RHEL):**

```bash
sudo dnf install -y java-17-openjdk-devel
```

**macOS (with Homebrew):**

```bash
brew install openjdk@17
```

**Windows:**

- Download from: https://adoptium.net/
- Follow installer instructions
- Set `JAVA_HOME` environment variable

#### 2. Install Maven

**Linux (Ubuntu/Debian):**

```bash
sudo apt update
sudo apt install -y maven
```

**Linux (Fedora/RHEL):**

```bash
sudo dnf install -y maven
```

**macOS (with Homebrew):**

```bash
brew install maven
```

**Windows:**

- Download from: https://maven.apache.org/download.cgi
- Extract and set `M2_HOME` environment variable

#### 3. Install Node.js

**Linux (Ubuntu/Debian):**

```bash
sudo apt update
sudo apt install -y nodejs npm
```

**Linux (Fedora/RHEL):**

```bash
sudo dnf install -y nodejs npm
```

**macOS (with Homebrew):**

```bash
brew install node
```

**Windows:**

- Download from: https://nodejs.org/
- Follow installer instructions

#### 4. Install MySQL (Optional)

**Linux (Ubuntu/Debian):**

```bash
sudo apt update
sudo apt install -y mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
```

**Linux (Fedora/RHEL):**

```bash
sudo dnf install -y mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld
```

**macOS (with Homebrew):**

```bash
brew install mysql
brew services start mysql
```

**Windows:**

- Download from: https://dev.mysql.com/downloads/mysql/
- Follow installer instructions
- Configure Windows Firewall if needed

#### 5. Build and Setup the Project

```bash
cd hexter
mvn clean install
cd src/main/javascript/be/hexter/hexter
npm install
```

## Configuration

### Database Configuration

#### For Development (Default)

Development uses H2 in-memory database. No configuration needed.

#### For Production with MySQL

1. **Create the database:**

```bash
mysql -u root -p
CREATE DATABASE hexter;
CREATE USER 'hexter_user'@'localhost' IDENTIFIED BY 'hexter_password';
GRANT ALL PRIVILEGES ON hexter.* TO 'hexter_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

2. **Update application configuration:**
   Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hexter?useSSL=false&serverTimezone=UTC
spring.datasource.username=hexter_user
spring.datasource.password=hexter_password
```

Or for production deployments, use environment variables:

```bash
export MYSQL_HOST=your-mysql-host
export MYSQL_PORT=3306
export MYSQL_SCHEMA=hexter
export WEB_JAVA_IP2LOCATION_DB_LOGIN=hexter_user
export WEB_JAVA_IP2LOCATION_DB_PASSWORD=hexter_password
```

## Starting Development

### Start Backend

```bash
cd /path/to/hexter
mvn spring-boot:run
```

The backend will be available at: `http://localhost:8080`

### Start Frontend (in another terminal)

```bash
cd /path/to/hexter/src/main/javascript/be/hexter/hexter
npm start
```

The frontend will be available at: `http://localhost:3000`

### Run Tests

```bash
# Backend tests
mvn test

# Frontend tests
cd src/main/javascript/be/hexter/hexter
npm test

# E2E tests with Cypress
npm run cypress:open
```

## Troubleshooting

### "Java command not found"

- **Linux:** Run `sudo apt/dnf install openjdk-17-jdk`
- **macOS:** Run `brew install openjdk@17`
- **Windows:** Install from https://adoptium.net/ and add to PATH

### "Maven command not found"

- **Linux:** Run `sudo apt/dnf install maven`
- **macOS:** Run `brew install maven`
- **Windows:** Install from https://maven.apache.org/ and add to PATH

### "Node not found"

- **Linux:** Run `sudo apt/dnf install nodejs npm`
- **macOS:** Run `brew install node`
- **Windows:** Install from https://nodejs.org/

### "MySQL connection refused"

- Check if MySQL is running: `sudo systemctl status mysql` (Linux) or `brew services list` (macOS)
- Start MySQL if not running: `sudo systemctl start mysql` (Linux) or `brew services start mysql` (macOS)
- Verify credentials in `application.properties`

### "Permission denied" on setup.sh

```bash
chmod +x setup.sh
./setup.sh
```

### Build fails on Windows

- Use Git Bash instead of Command Prompt
- Or use WSL (Windows Subsystem for Linux) for full compatibility
- Run: `bash setup.sh` in Git Bash

## Additional Resources

- [Backend Documentation](README.md)
- [Testing Guide](TEST_GUIDE.md)
- [DEB Installation Guide](DEB_INSTALLATION.md)
- [Project Structure](docs/ARCHITECTURE.md) (if available)

## Environment Variables

The following environment variables can be set for advanced configuration:

```bash
# Java
export JAVA_HOME=/path/to/java-17

# Maven
export M2_HOME=/path/to/maven
export MAVEN_OPTS="-Xmx1024m"

# Database (Production)
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_SCHEMA=hexter
export WEB_JAVA_IP2LOCATION_DB_LOGIN=hexter_user
export WEB_JAVA_IP2LOCATION_DB_PASSWORD=password
```

## Getting Help

If you encounter issues:

1. Check this guide's Troubleshooting section
2. Review the build logs for errors
3. Check system prerequisites are installed: `java -version`, `mvn -version`, `node -v`, `npm -v`
4. Ensure you're using supported versions (Java 17+, Maven 3.6+, Node 14+)
5. Check existing GitHub Issues

## Contributing

Once your environment is set up, refer to:

- [CONTRIBUTING.md](CONTRIBUTING.md) (if available)
- [Development Guide](docs/DEVELOPMENT.md) (if available)

---

**Enjoy developing with Hexter!** 🚀
