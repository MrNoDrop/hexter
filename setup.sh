#!/bin/bash

#################################################################################
# Hexter Project Setup Script
# 
# System-agnostic setup script for development environment
# Detects OS and installs all prerequisites
#
# Usage:
#   ./setup.sh                            # Interactive setup with prompts
#   ./setup.sh --auto                     # Automatic setup (non-interactive)
#   ./setup.sh --skip-db                  # Setup without database configuration
#   ./setup.sh --help                     # Show help
#
# Supported Systems:
#   - Linux (Ubuntu, Debian, Fedora, CentOS, Arch)
#   - macOS (Intel and Apple Silicon)
#   - Windows (Git Bash, WSL, or native with manual steps)
#
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
AUTO_MODE=false
SKIP_DB=false
SHOW_HELP=false

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --auto)
            AUTO_MODE=true
            shift
            ;;
        --skip-db)
            SKIP_DB=true
            shift
            ;;
        --help|-h)
            SHOW_HELP=true
            shift
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            echo "Use --help for usage information"
            exit 1
            ;;
    esac
done

# Show help
if [ "$SHOW_HELP" = true ]; then
    cat << 'EOF'
╔════════════════════════════════════════════════════════════════════════════╗
║                  Hexter Project Development Setup                          ║
╚════════════════════════════════════════════════════════════════════════════╝

USAGE:
    ./setup.sh [OPTIONS]

OPTIONS:
    --auto              Automatic setup (non-interactive, installs all)
    --skip-db           Skip database configuration
    --help              Show this help message

DESCRIPTION:
    This script sets up your development environment for Hexter by:
    1. Detecting your operating system
    2. Installing Java 17 (if needed)
    3. Installing Maven (if needed)
    4. Installing Node.js and npm (if needed)
    5. Installing MySQL Server (production setup, optional)
    6. Building the project
    7. Installing npm dependencies

SYSTEM REQUIREMENTS:
    - Linux, macOS, or Windows with Git Bash/WSL
    - ~2GB free disk space
    - Internet connection

SUPPORTED PACKAGE MANAGERS:
    - Linux: apt (Ubuntu/Debian), dnf (Fedora/CentOS), pacman (Arch)
    - macOS: brew (Homebrew)
    - Windows: Manual installation or WSL

EOF
    exit 0
fi

#################################################################################
# Utility Functions
#################################################################################

detect_os() {
    case "$(uname -s)" in
        Linux*)
            echo "Linux"
            ;;
        Darwin*)
            echo "macOS"
            ;;
        MINGW*|MSYS*|CYGWIN*)
            echo "Windows"
            ;;
        *)
            echo "Unknown"
            ;;
    esac
}

detect_linux_distro() {
    if [ -f /etc/os-release ]; then
        . /etc/os-release
        echo "$ID"
    else
        echo "unknown"
    fi
}

detect_package_manager() {
    if command -v apt &> /dev/null; then
        echo "apt"
    elif command -v dnf &> /dev/null; then
        echo "dnf"
    elif command -v yum &> /dev/null; then
        echo "yum"
    elif command -v pacman &> /dev/null; then
        echo "pacman"
    elif command -v brew &> /dev/null; then
        echo "brew"
    else
        echo "none"
    fi
}

command_exists() {
    command -v "$1" &> /dev/null
}

print_header() {
    echo ""
    echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║ $1$(printf ' %.0s' {1..$(( 57 - ${#1} ))})║${NC}"
    echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
    echo ""
}

print_step() {
    echo -e "${YELLOW}[*] $1${NC}"
}

print_success() {
    echo -e "${GREEN}[✓] $1${NC}"
}

print_error() {
    echo -e "${RED}[✗] $1${NC}"
}

print_info() {
    echo -e "${CYAN}[i] $1${NC}"
}

confirm() {
    if [ "$AUTO_MODE" = true ]; then
        return 0
    fi
    
    local prompt="$1"
    local response
    
    while true; do
        echo -ne "${YELLOW}${prompt} (y/n): ${NC}"
        read -r response
        case "$response" in
            [Yy]*)
                return 0
                ;;
            [Nn]*)
                return 1
                ;;
            *)
                echo "Please answer yes or no."
                ;;
        esac
    done
}

#################################################################################
# Installation Functions
#################################################################################

install_java_linux() {
    local pkg_manager=$1
    local distro=$2
    
    print_step "Installing Java 17 on Linux ($distro)..."
    
    case $pkg_manager in
        apt)
            sudo apt update
            sudo apt install -y openjdk-17-jdk
            ;;
        dnf|yum)
            sudo "$pkg_manager" install -y java-17-openjdk-devel
            ;;
        pacman)
            sudo pacman -Sy jdk17-openjdk
            ;;
    esac
    
    if command_exists java; then
        print_success "Java 17 installed"
    else
        print_error "Failed to install Java 17"
        exit 1
    fi
}

install_java_macos() {
    print_step "Installing Java 17 on macOS..."
    
    if ! command_exists brew; then
        print_error "Homebrew not found. Please install from https://brew.sh"
        exit 1
    fi
    
    brew install openjdk@17
    
    if command_exists java; then
        print_success "Java 17 installed via Homebrew"
    else
        print_error "Failed to install Java 17"
        exit 1
    fi
}

install_java_windows() {
    print_error "Windows detected. Please install Java 17 manually:"
    echo "  1. Download from: https://adoptium.net/temurin"
    echo "  2. Or use: choco install openjdk17 (if using Chocolatey)"
    echo "  3. Set JAVA_HOME environment variable to your Java installation path"
    exit 1
}

install_maven_linux() {
    local pkg_manager=$1
    
    print_step "Installing Maven on Linux..."
    
    case $pkg_manager in
        apt)
            sudo apt update
            sudo apt install -y maven
            ;;
        dnf|yum)
            sudo "$pkg_manager" install -y maven
            ;;
        pacman)
            sudo pacman -Sy maven
            ;;
    esac
    
    if command_exists mvn; then
        print_success "Maven installed"
    fi
}

install_maven_macos() {
    print_step "Installing Maven on macOS..."
    
    if ! command_exists brew; then
        print_error "Homebrew not found"
        exit 1
    fi
    
    brew install maven
    
    if command_exists mvn; then
        print_success "Maven installed via Homebrew"
    fi
}

install_nodejs_linux() {
    local pkg_manager=$1
    
    print_step "Installing Node.js and npm on Linux..."
    
    case $pkg_manager in
        apt)
            sudo apt update
            sudo apt install -y nodejs npm
            ;;
        dnf|yum)
            sudo "$pkg_manager" install -y nodejs npm
            ;;
        pacman)
            sudo pacman -Sy nodejs npm
            ;;
    esac
    
    if command_exists node; then
        print_success "Node.js installed ($(node --version))"
    fi
}

install_nodejs_macos() {
    print_step "Installing Node.js on macOS..."
    
    if ! command_exists brew; then
        print_error "Homebrew not found"
        exit 1
    fi
    
    brew install node
    
    if command_exists node; then
        print_success "Node.js installed ($(node --version))"
    fi
}

install_mysql_linux() {
    local pkg_manager=$1
    
    print_step "Installing MySQL Server on Linux..."
    
    case $pkg_manager in
        apt)
            sudo apt update
            sudo apt install -y mysql-server
            sudo systemctl start mysql
            sudo systemctl enable mysql
            ;;
        dnf|yum)
            sudo "$pkg_manager" install -y mysql-server
            sudo systemctl start mysqld
            sudo systemctl enable mysqld
            ;;
        pacman)
            sudo pacman -Sy mysql
            sudo systemctl start mysqld
            sudo systemctl enable mysqld
            ;;
    esac
    
    if command_exists mysql; then
        print_success "MySQL Server installed"
    fi
}

install_mysql_macos() {
    print_step "Installing MySQL Server on macOS..."
    
    if ! command_exists brew; then
        print_error "Homebrew not found"
        exit 1
    fi
    
    brew install mysql
    
    if command_exists mysql; then
        print_success "MySQL Server installed"
        print_info "Start MySQL with: brew services start mysql"
    fi
}

setup_mysql_database() {
    if ! command_exists mysql; then
        print_error "MySQL not found. Please install MySQL Server first."
        return 1
    fi
    
    print_step "Setting up MySQL database..."
    
    # Check if MySQL is running
    if ! mysqladmin ping -u root &> /dev/null; then
        print_error "MySQL is not running. Please start MySQL and try again."
        print_info "Start with: sudo systemctl start mysql (Linux) or brew services start mysql (macOS)"
        return 1
    fi
    
    # Create hexter database and user
    mysql -u root -e "CREATE DATABASE IF NOT EXISTS hexter;" || true
    mysql -u root -e "CREATE USER IF NOT EXISTS 'hexter_user'@'localhost' IDENTIFIED BY 'hexter_password';" || true
    mysql -u root -e "GRANT ALL PRIVILEGES ON hexter.* TO 'hexter_user'@'localhost';" || true
    mysql -u root -e "FLUSH PRIVILEGES;" || true
    
    print_success "MySQL database 'hexter' created with user 'hexter_user'"
    print_info "Update credentials in application.properties if needed"
}

#################################################################################
# Setup Process
#################################################################################

main() {
    print_header "Hexter Project Development Setup"
    
    OS=$(detect_os)
    DISTRO=$(detect_linux_distro)
    PKG_MANAGER=$(detect_package_manager)
    
    echo -e "${CYAN}System Information:${NC}"
    echo "  OS: $OS"
    [ "$OS" = "Linux" ] && echo "  Distribution: $DISTRO"
    echo "  Package Manager: $PKG_MANAGER"
    echo "  Project Root: $PROJECT_ROOT"
    echo ""
    
    # Check prerequisites
    print_step "Checking prerequisites..."
    
    JAVA_OK=false
    MAVEN_OK=false
    NODE_OK=false
    MYSQL_OK=false
    
    if command_exists java; then
        JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "\K[^"]*')
        print_success "Java found: $JAVA_VERSION"
        JAVA_OK=true
    else
        print_info "Java 17 not found"
    fi
    
    if command_exists mvn; then
        MAVEN_VERSION=$(mvn --version 2>&1 | head -1)
        print_success "$MAVEN_VERSION"
        MAVEN_OK=true
    else
        print_info "Maven not found"
    fi
    
    if command_exists node; then
        print_success "Node.js found: $(node --version)"
        NODE_OK=true
    else
        print_info "Node.js not found"
    fi
    
    if command_exists mysql; then
        print_success "MySQL found"
        MYSQL_OK=true
    else
        print_info "MySQL not found"
    fi
    
    echo ""
    
    # Install missing components
    if [ "$JAVA_OK" = false ]; then
        if confirm "Install Java 17?"; then
            case $OS in
                Linux)
                    install_java_linux "$PKG_MANAGER" "$DISTRO"
                    ;;
                macOS)
                    install_java_macos
                    ;;
                Windows)
                    install_java_windows
                    ;;
            esac
        else
            print_error "Java 17 is required. Exiting."
            exit 1
        fi
    fi
    
    if [ "$MAVEN_OK" = false ]; then
        if confirm "Install Maven?"; then
            case $OS in
                Linux)
                    install_maven_linux "$PKG_MANAGER"
                    ;;
                macOS)
                    install_maven_macos
                    ;;
                *)
                    print_error "Manual Maven installation required"
                    exit 1
                    ;;
            esac
        else
            print_error "Maven is required. Exiting."
            exit 1
        fi
    fi
    
    if [ "$NODE_OK" = false ]; then
        if confirm "Install Node.js and npm?"; then
            case $OS in
                Linux)
                    install_nodejs_linux "$PKG_MANAGER"
                    ;;
                macOS)
                    install_nodejs_macos
                    ;;
                *)
                    print_error "Manual Node.js installation required"
                    exit 1
                    ;;
            esac
        else
            print_error "Node.js is required. Exiting."
            exit 1
        fi
    fi
    
    if [ "$SKIP_DB" = false ] && [ "$MYSQL_OK" = false ]; then
        if confirm "Install MySQL Server?"; then
            case $OS in
                Linux)
                    install_mysql_linux "$PKG_MANAGER"
                    ;;
                macOS)
                    install_mysql_macos
                    ;;
                *)
                    print_info "Manual MySQL installation required"
                    ;;
            esac
        else
            print_info "Skipping MySQL installation"
        fi
    fi
    
    echo ""
    
    # Set up Java environment
    print_step "Setting up Java environment..."
    
    if [ -z "$JAVA_HOME" ]; then
        if [ "$OS" = "macOS" ]; then
            # macOS with Homebrew
            if [ -d "/Library/Java/JavaVirtualMachines" ]; then
                JAVA_HOME=$(/usr/libexec/java_home)
            elif [ -d "/opt/homebrew/opt/openjdk" ]; then
                JAVA_HOME="/opt/homebrew/opt/openjdk"
            elif [ -d "/usr/local/opt/openjdk" ]; then
                JAVA_HOME="/usr/local/opt/openjdk"
            fi
        elif [ "$OS" = "Linux" ]; then
            # Linux
            JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
        fi
        
        if [ -n "$JAVA_HOME" ]; then
            export JAVA_HOME
            echo "export JAVA_HOME='$JAVA_HOME'" >> ~/.bashrc.hexter 2>/dev/null || true
            echo "export JAVA_HOME='$JAVA_HOME'" >> ~/.zshrc.hexter 2>/dev/null || true
            print_success "JAVA_HOME set to: $JAVA_HOME"
        fi
    else
        print_success "JAVA_HOME already set: $JAVA_HOME"
    fi
    
    echo ""
    
    # Setup database
    if [ "$SKIP_DB" = false ] && [ "$MYSQL_OK" = true ]; then
        if confirm "Configure MySQL database?"; then
            setup_mysql_database || true
        fi
    fi
    
    echo ""
    
    # Build backend
    print_step "Building backend (Maven)..."
    cd "$PROJECT_ROOT"
    export JAVA_HOME
    
    if mvn clean install -q; then
        print_success "Backend built successfully"
    else
        print_error "Backend build failed. Check error messages above."
        exit 1
    fi
    
    echo ""
    
    # Setup frontend
    print_step "Setting up frontend (npm)..."
    FRONTEND_DIR="$PROJECT_ROOT/src/main/javascript/be/hexter/hexter"
    
    if [ -d "$FRONTEND_DIR" ]; then
        cd "$FRONTEND_DIR"
        if npm install > /dev/null 2>&1; then
            print_success "Frontend dependencies installed"
        else
            print_error "Frontend setup failed"
            exit 1
        fi
    else
        print_info "Frontend directory not found, skipping npm setup"
    fi
    
    echo ""
    print_header "Setup Complete!"
    
    echo -e "${GREEN}Your development environment is ready!${NC}"
    echo ""
    echo "Next steps:"
    echo "  1. Update database credentials in src/main/resources/application.properties"
    echo "  2. Start the backend: cd $PROJECT_ROOT && mvn spring-boot:run"
    echo "  3. Start the frontend: cd $FRONTEND_DIR && npm start"
    echo "  4. Access the application at http://localhost:3000"
    echo ""
    echo "Documentation:"
    echo "  - Backend: see README.md"
    echo "  - Testing: see TEST_GUIDE.md"
    echo "  - Installation: see DEB_INSTALLATION.md"
    echo ""
}

# Run main function
main "$@"
