# Hexter .deb Package Installation Guide

## Package Information

- **Package Name:** hexter
- **Version:** 0.0.1
- **Architecture:** all (architecture-independent, requires Java 17)
- **Size:** ~40 MB
- **Maintainer:** Patryk Sitko

## Prerequisites

Before installing the .deb package, ensure your system has:

1. **Java 17 Runtime Environment** (one of):
   - `openjdk-17-jre-headless`
   - `java-17-openjdk-headless`

Install Java 17:
```bash
sudo apt update
sudo apt install openjdk-17-jre-headless
```

2. **MySQL Server** (for production use)
```bash
sudo apt install mysql-server
```

## Installation

### Option 1: Using dpkg

```bash
sudo dpkg -i hexter_0.0.1_all.deb
```

### Option 2: Using apt

```bash
sudo apt install ./hexter_0.0.1_all.deb
```

The apt method is preferred as it handles dependencies automatically.

## What Gets Installed

The .deb package installs the following:

```
/usr/share/hexter/hexter-0.0.1-SNAPSHOT.jar  # Application JAR
/etc/hexter/application.properties             # Configuration file
/etc/systemd/system/hexter.service             # Systemd service definition
```

## Service Management

### Start the service
```bash
sudo systemctl start hexter
```

### Stop the service
```bash
sudo systemctl stop hexter
```

### Enable for automatic startup at boot
```bash
sudo systemctl enable hexter
```

### Disable automatic startup
```bash
sudo systemctl disable hexter
```

### Check service status
```bash
sudo systemctl status hexter
```

### View logs in real-time
```bash
sudo journalctl -u hexter -f
```

### View recent logs
```bash
sudo journalctl -u hexter -n 50
```

## Configuration

The application configuration is stored in `/etc/hexter/application.properties`

### For Development (H2 In-Memory Database):
```properties
spring.profiles.active=dev
```

### For Production (MySQL):

Edit `/etc/hexter/application.properties` and configure:

```properties
spring.profiles.active=prod
spring.datasource.url=jdbc:mysql://localhost:3306/hexter
spring.datasource.username=hexter_user
spring.datasource.password=your_secure_password
```

Create the MySQL database:
```bash
mysql -u root -p
mysql> CREATE DATABASE hexter;
mysql> CREATE USER 'hexter_user'@'localhost' IDENTIFIED BY 'your_secure_password';
mysql> GRANT ALL PRIVILEGES ON hexter.* TO 'hexter_user'@'localhost';
mysql> FLUSH PRIVILEGES;
```

Save the configuration and restart the service:
```bash
sudo systemctl restart hexter
```

## Accessing the Application

After starting the service, the application will be available at:

- **Backend API:** `http://localhost:8080/api`
- **Frontend:** `http://localhost:3000` (if deployed separately)

### Available Endpoints:
- `POST /api/user/register` - Register new user
- `POST /api/user/login` - User login
- `POST /api/user/validate-authentication-token` - Validate token
- `POST /api/user/request-recover-password` - Request password recovery
- `POST /api/user/recover-password` - Reset password

## Uninstallation

```bash
sudo apt remove hexter
```

or

```bash
sudo dpkg -r hexter
```

This will:
- Stop the service
- Remove the application files
- Keep configuration files in `/etc/hexter/` for reference

To completely remove configuration files as well:
```bash
sudo apt purge hexter
```

## Troubleshooting

### Service fails to start

Check the logs:
```bash
sudo journalctl -u hexter -n 50
```

Common issues:
- **Java not found:** Ensure Java 17 is installed
- **Port 8080 in use:** Change in `/etc/hexter/application.properties` (server.port=XXXX)
- **Database connection error:** Verify MySQL is running and credentials are correct

### High memory usage

For production systems with limited memory, add JVM options to `/etc/systemd/system/hexter.service`:

Edit the service file:
```bash
sudo systemctl edit hexter
```

Add to the `[Service]` section:
```
Environment="JAVA_OPTS=-Xmx512m -Xms256m"
ExecStart=/usr/bin/java $JAVA_OPTS -jar /usr/share/hexter/hexter-0.0.1-SNAPSHOT.jar
```

Reload and restart:
```bash
sudo systemctl daemon-reload
sudo systemctl restart hexter
```

## Version Information

- **Spring Boot:** 2.7.18
- **Spring Framework:** 5.3.31
- **Java Version:** 17 LTS
- **Frontend:** React 18.2.0
- **Database:** H2 (dev) / MySQL 8.0 (prod)

## Support

For issues and updates, please refer to the project repository.

---

**Installation Date:** $(date)
**Installed By:** Hexter PackageManager v0.0.1
