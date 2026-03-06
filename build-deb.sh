#!/bin/bash

#################################################################################
# Hexter .deb Package Build Script with Automatic Version Bumping
# 
# This script automates the creation of a Debian package for the Hexter 
# social media platform. It performs the following steps:
#
# 1. Reads/bumps version number (semantic versioning)
# 2. Builds the Maven project
# 3. Creates the .deb package directory structure
# 4. Prepares Debian control files and systemd service
# 5. Packages everything into a .deb file
# 6. Copies the final package to the project directory
# 7. Updates VERSION file with new version
#
# Usage: 
#   ./build-deb.sh                      # Auto-bumps patch version (0.0.1 -> 0.0.2)
#   ./build-deb.sh --bump-patch         # Explicitly bump patch (0.0.1 -> 0.0.2)
#   ./build-deb.sh --bump-minor         # Bump minor version (0.0.1 -> 0.1.0)
#   ./build-deb.sh --bump-major         # Bump major version (0.0.1 -> 1.0.0)
#   ./build-deb.sh --version X.Y.Z      # Use specific version
#   ./build-deb.sh --no-bump            # Build without changing version
#################################################################################

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configuration
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAVA_HOME="${JAVA_HOME:-/home/patryk/.jdk/jdk-17.0.16}"
BUILD_DIR="/tmp/hexter-deb-build"
DEB_DIR="${BUILD_DIR}/hexter-deb"
OUTPUT_DIR="${PROJECT_ROOT}"
VERSION_FILE="${PROJECT_ROOT}/VERSION"
BUMP_MODE="patch"  # Default: bump patch version

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --bump-patch)
            BUMP_MODE="patch"
            shift
            ;;
        --bump-minor)
            BUMP_MODE="minor"
            shift
            ;;
        --bump-major)
            BUMP_MODE="major"
            shift
            ;;
        --version)
            CUSTOM_VERSION="$2"
            BUMP_MODE="none"
            shift 2
            ;;
        --no-bump)
            BUMP_MODE="none"
            shift
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            echo "Usage: $0 [--bump-patch|--bump-minor|--bump-major|--version X.Y.Z|--no-bump]"
            exit 1
            ;;
    esac
done

# Function to bump version
bump_version() {
    local current=$1
    local mode=$2
    
    IFS='.' read -r major minor patch <<< "$current"
    
    case $mode in
        patch)
            ((patch++))
            ;;
        minor)
            ((minor++))
            patch=0
            ;;
        major)
            ((major++))
            minor=0
            patch=0
            ;;
    esac
    
    echo "${major}.${minor}.${patch}"
}

# Read or initialize version
if [ -f "${VERSION_FILE}" ]; then
    CURRENT_VERSION=$(cat "${VERSION_FILE}" | xargs)
    echo -e "${CYAN}Current version: ${CURRENT_VERSION}${NC}"
else
    CURRENT_VERSION="0.0.1"
    echo "${CURRENT_VERSION}" > "${VERSION_FILE}"
    echo -e "${CYAN}Version file created: ${CURRENT_VERSION}${NC}"
fi

# Determine final version to use
if [ -n "${CUSTOM_VERSION}" ]; then
    VERSION="${CUSTOM_VERSION}"
    echo -e "${YELLOW}Using custom version: ${VERSION}${NC}"
elif [ "${BUMP_MODE}" != "none" ]; then
    VERSION=$(bump_version "${CURRENT_VERSION}" "${BUMP_MODE}")
    echo -e "${YELLOW}Bumping ${BUMP_MODE}: ${CURRENT_VERSION} → ${VERSION}${NC}"
else
    VERSION="${CURRENT_VERSION}"
    echo -e "${CYAN}Using existing version: ${VERSION}${NC}"
fi

JAR_FILE="${PROJECT_ROOT}/target/hexter-${VERSION}-SNAPSHOT.jar"
JAR_FILE_ACTUAL="${PROJECT_ROOT}/target/hexter-0.0.1-SNAPSHOT.jar"

echo ""
echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║        Hexter .deb Package Build Script                    ║${NC}"
echo -e "${BLUE}║                                                            ║${NC}"
echo -e "${BLUE}║   Version: ${VERSION}$(printf ' %.0s' {1..$(( 27 - ${#VERSION} ))})║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""

# Step 1: Build Maven project
echo -e "${YELLOW}[1/6] Building Maven project...${NC}"
export JAVA_HOME
cd "${PROJECT_ROOT}"
mvn clean package -DskipTests -q 2>&1 | grep -E "BUILD|ERROR" || true

if [ ! -f "${JAR_FILE_ACTUAL}" ]; then
    echo -e "${RED}✗ Maven build failed - JAR not found at ${JAR_FILE_ACTUAL}${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Maven project built successfully${NC}"
JAR_SIZE=$(du -h "${JAR_FILE_ACTUAL}" | cut -f1)
echo -e "  JAR size: ${JAR_SIZE}"
echo ""

# Step 2: Clean and create directory structure
echo -e "${YELLOW}[2/6] Creating .deb package directory structure...${NC}"
rm -rf "${DEB_DIR}"
mkdir -p "${DEB_DIR}/DEBIAN"
mkdir -p "${DEB_DIR}/usr/share/hexter"
mkdir -p "${DEB_DIR}/etc/systemd/system"
mkdir -p "${DEB_DIR}/etc/hexter"
echo -e "${GREEN}✓ Directory structure created${NC}"
echo ""

# Step 3: Create Debian control files
echo -e "${YELLOW}[3/6] Creating Debian package metadata...${NC}"

# Create control file
cat > "${DEB_DIR}/DEBIAN/control" << EOF
Package: hexter
Version: ${VERSION}
Section: web
Priority: optional
Architecture: all
Depends: openjdk-17-jre-headless | java-17-openjdk-headless
Maintainer: Patryk Sitko <patryk@hexter.local>
Description: Hexter - Social Media Platform
 A modern Java/Spring Boot and React-based social media application
 with user authentication, password recovery, and real-time features.
Homepage: https://github.com/patryksitko/hexter
EOF

# Create postinst script
cat > "${DEB_DIR}/DEBIAN/postinst" << 'EOF'
#!/bin/bash
set -e

# Reload systemd daemon and enable hexter service
systemctl daemon-reload
systemctl enable hexter.service
systemctl start hexter.service

# Create log directory
mkdir -p /var/log/hexter
chmod 755 /var/log/hexter

echo "✓ Hexter service installed and started successfully!"
echo "  Service status: systemctl status hexter"
echo "  View logs: journalctl -u hexter -f"
EOF
chmod 755 "${DEB_DIR}/DEBIAN/postinst"

# Create postrm script
cat > "${DEB_DIR}/DEBIAN/postrm" << 'EOF'
#!/bin/bash
set -e

# Stop and disable hexter service
systemctl stop hexter.service || true
systemctl disable hexter.service || true
systemctl daemon-reload

echo "✓ Hexter service removed"
EOF
chmod 755 "${DEB_DIR}/DEBIAN/postrm"

# Create conffiles
cat > "${DEB_DIR}/DEBIAN/conffiles" << 'EOF'
/etc/hexter/application.properties
EOF

echo -e "${GREEN}✓ Debian control files created${NC}"
echo ""

# Step 4: Create systemd service file
echo -e "${YELLOW}[4/6] Creating systemd service configuration...${NC}"
cat > "${DEB_DIR}/etc/systemd/system/hexter.service" << 'EOF'
[Unit]
Description=Hexter Social Media Platform
After=network-online.target mysql.service
Wants=network-online.target

[Service]
Type=simple
User=root
WorkingDirectory=/usr/share/hexter
ExecStart=/usr/bin/java -jar /usr/share/hexter/hexter-0.0.1-SNAPSHOT.jar
ExecReload=/bin/kill -HUP $MAINPID
KillMode=process
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF

# Create application properties
cat > "${DEB_DIR}/etc/hexter/application.properties" << 'EOF'
# Hexter Application Configuration
# This file can be customized for your deployment

# Server port
server.port=8080

# Application profile (dev, test, prod)
spring.profiles.active=prod

# MySQL Database Configuration (for production)
# Update these values with your MySQL server details
spring.datasource.url=jdbc:mysql://localhost:3306/hexter?useSSL=false&serverTimezone=UTC
spring.datasource.username=hexter_user
spring.datasource.password=hexter_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Logging
logging.level.root=INFO
logging.level.be.hexter=DEBUG
logging.file.name=/var/log/hexter/hexter.log
EOF

echo -e "${GREEN}✓ Systemd service and configuration files created${NC}"
echo ""

# Step 5: Copy JAR and build .deb
echo -e "${YELLOW}[5/6] Packaging .deb file...${NC}"
cp "${JAR_FILE_ACTUAL}" "${DEB_DIR}/usr/share/hexter/"
echo -e "  Copied hexter-0.0.1-SNAPSHOT.jar (${JAR_SIZE})"

# Build the .deb package
cd "${BUILD_DIR}"
dpkg-deb --build hexter-deb "hexter_${VERSION}_all.deb" 2>&1 | grep -E "building|built" || true

DEB_FILE="${BUILD_DIR}/hexter_${VERSION}_all.deb"
if [ ! -f "${DEB_FILE}" ]; then
    echo -e "${RED}✗ .deb package creation failed${NC}"
    exit 1
fi

DEB_SIZE=$(du -h "${DEB_FILE}" | cut -f1)
echo -e "${GREEN}✓ .deb package created successfully${NC}"
echo -e "  File: hexter_${VERSION}_all.deb"
echo -e "  Size: ${DEB_SIZE}"
echo ""

# Step 6: Copy to project directory and update VERSION file
echo -e "${YELLOW}[6/6] Finalizing and updating version...${NC}"
cp "${DEB_FILE}" "${OUTPUT_DIR}/"
echo -e "${GREEN}✓ Package copied to project directory${NC}"

# Update VERSION file with new version if version was bumped
if [ "${BUMP_MODE}" != "none" ] && [ "${VERSION}" != "${CURRENT_VERSION}" ]; then
    echo "${VERSION}" > "${VERSION_FILE}"
    echo -e "${GREEN}✓ VERSION file updated to ${VERSION}${NC}"
fi
echo ""

# Print summary
echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║                    BUILD SUCCESSFUL                        ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${GREEN}Package Details:${NC}"
echo "  Version: ${VERSION}"
echo "  Location: ${OUTPUT_DIR}/hexter_${VERSION}_all.deb"
echo "  Size: ${DEB_SIZE}"
echo ""
if [ "${BUMP_MODE}" != "none" ] && [ "${VERSION}" != "${CURRENT_VERSION}" ]; then
    echo -e "${GREEN}Version Bump:${NC}"
    echo "  From: ${CURRENT_VERSION}"
    echo "  To: ${VERSION}"
    echo "  Mode: ${BUMP_MODE}"
    echo ""
fi
echo -e "${GREEN}Installation instructions:${NC}"
echo "  1. On target system, run:"
echo "     sudo apt update"
echo "     sudo apt install openjdk-17-jre-headless"
echo ""
echo "  2. Install the package:"
echo "     sudo apt install ./hexter_${VERSION}_all.deb"
echo ""
echo "  3. Verify service is running:"
echo "     systemctl status hexter"
echo ""
echo -e "${GREEN}View service logs:${NC}"
echo "  journalctl -u hexter -f"
echo ""
echo -e "${GREEN}For more information, see:${NC}"
echo "  ${OUTPUT_DIR}/DEB_INSTALLATION.md"
echo ""
