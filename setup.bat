@REM #############################################################################
@REM Hexter Project Setup Script for Windows
@REM
@REM System-agnostic setup script for development environment
@REM Detects Windows version and helps install prerequisites
@REM
@REM Usage:
@REM    setup.bat                     - Interactive setup with prompts
@REM    setup.bat --auto              - Automatic setup (non-interactive)
@REM    setup.bat --skip-db            - Setup without database configuration
@REM    setup.bat --help               - Show help
@REM
@REM Prerequisites (manual installation on Windows):
@REM    - Git Bash, WSL, or native Windows (recommended: Git Bash)
@REM    - Java 17 JDK (https://adoptium.net/)
@REM    - Maven (https://maven.apache.org/)
@REM    - Node.js (https://nodejs.org/)
@REM    - MySQL Server (https://dev.mysql.com/downloads/mysql/) - optional
@REM
@REM #############################################################################

@echo off
setlocal enabledelayedexpansion

set "RED=[91m"
set "GREEN=[92m"
set "YELLOW=[93m"
set "BLUE=[94m"
set "CYAN=[96m"
set "NC=[0m"

set "AUTO_MODE=false"
set "SKIP_DB=false"
set "SHOW_HELP=false"

REM Parse command line arguments
:parse_args
if "%~1"=="" goto start_setup
if "%~1"=="--auto" (
    set "AUTO_MODE=true"
    shift
    goto parse_args
)
if "%~1"=="--skip-db" (
    set "SKIP_DB=true"
    shift
    goto parse_args
)
if "%~1"=="--help" (
    set "SHOW_HELP=true"
    shift
    goto parse_args
)

:start_setup
if "%SHOW_HELP%"=="true" goto show_help

cls
echo.
echo %BLUE%^╔════════════════════════════════════════════════════════════════════════════^╗%NC%
echo %BLUE%^║                  Hexter Project Development Setup                          ^║%NC%
echo %BLUE%^║                        (Windows)                                           ^║%NC%
echo %BLUE%^╚════════════════════════════════════════════════════════════════════════════^╝%NC%
echo.

set "PROJECT_ROOT=%CD%"
echo %CYAN%System Information:%NC%
echo   OS: Windows
echo   Project Root: %PROJECT_ROOT%
echo.

REM Check prerequisites
echo %YELLOW%[*] Checking prerequisites...%NC%
echo.

set "JAVA_OK=false"
set "MAVEN_OK=false"
set "NODE_OK=false"

where java >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    for /f "tokens=*" %%i in ('java -version 2^>^&1 ^| findstr /R "version"') do (
        echo %GREEN%[^✓] Java found: %%i%NC%
    )
    set "JAVA_OK=true"
) else (
    echo %CYAN%[i] Java 17 not found%NC%
)

where mvn >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo %GREEN%[^✓] Maven found%NC%
    set "MAVEN_OK=true"
) else (
    echo %CYAN%[i] Maven not found%NC%
)

where node >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    for /f "tokens=*" %%i in ('node --version') do (
        echo %GREEN%[^✓] Node.js found: %%i%NC%
    )
    set "NODE_OK=true"
) else (
    echo %CYAN%[i] Node.js not found%NC%
)

where mysql >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo %GREEN%[^✓] MySQL found%NC%
) else (
    echo %CYAN%[i] MySQL not found%NC%
)

echo.

if "%JAVA_OK%"=="false" (
    echo %RED%[^✗] Java 17 is required but not found.%NC%
    echo.
    echo Please install Java 17 from: https://adoptium.net/
    echo After installation, add Java to your PATH and restart your terminal.
    echo.
    pause
    exit /b 1
)

if "%MAVEN_OK%"=="false" (
    echo %RED%[^✗] Maven is required but not found.%NC%
    echo.
    echo Please install Maven from: https://maven.apache.org/download.cgi
    echo After installation, add Maven to your PATH and restart your terminal.
    echo.
    pause
    exit /b 1
)

if "%NODE_OK%"=="false" (
    echo %RED%[^✗] Node.js is required but not found.%NC%
    echo.
    echo Please install Node.js from: https://nodejs.org/
    echo After installation, restart your terminal.
    echo.
    pause
    exit /b 1
)

echo %YELLOW%[*] Building backend (Maven)...%NC%
cd /d "%PROJECT_ROOT%"
call mvn clean install -q
if %ERRORLEVEL% NEQ 0 (
    echo %RED%[^✗] Backend build failed%NC%
    pause
    exit /b 1
)
echo %GREEN%[^✓] Backend built successfully%NC%
echo.

if exist "src\main\javascript\be\hexter\hexter" (
    echo %YELLOW%[*] Setting up frontend (npm)...%NC%
    cd /d "%PROJECT_ROOT%\src\main\javascript\be\hexter\hexter"
    call npm install >nul 2>&1
    if %ERRORLEVEL% NEQ 0 (
        echo %RED%[^✗] Frontend setup failed%NC%
        pause
        exit /b 1
    )
    echo %GREEN%[^✓] Frontend dependencies installed%NC%
) else (
    echo %CYAN%[i] Frontend directory not found, skipping npm setup%NC%
)

echo.
echo %BLUE%^╔════════════════════════════════════════════════════════════════════════════^╗%NC%
echo %BLUE%^║                       Setup Complete!                                      ^║%NC%
echo %BLUE%^╚════════════════════════════════════════════════════════════════════════════^╝%NC%
echo.
echo %GREEN%Your development environment is ready!%NC%
echo.
echo Next steps:
echo   1. Update database credentials in src\main\resources\application.properties
echo   2. Start the backend: mvn spring-boot:run
echo   3. Start the frontend: cd src\main\javascript\be\hexter\hexter ^&^& npm start
echo   4. Access the application at http://localhost:3000
echo.
echo Documentation:
echo   - Backend: see README.md
echo   - Testing: see TEST_GUIDE.md
echo   - Installation: see DEB_INSTALLATION.md
echo.
pause
exit /b 0

:show_help
cls
echo.
echo %BLUE%╔════════════════════════════════════════════════════════════════════════════╗%NC%
echo %BLUE%║                  Hexter Project Development Setup                          │%NC%
echo %BLUE%╚════════════════════════════════════════════════════════════════════════════╝%NC%
echo.
echo USAGE:
echo     setup.bat [OPTIONS]
echo.
echo OPTIONS:
echo     --auto              Automatic setup (non-interactive, installs all)
echo     --skip-db           Skip database configuration
echo     --help              Show this help message
echo.
echo DESCRIPTION:
echo     This script helps set up your development environment for Hexter.
echo.
echo SYSTEM REQUIREMENTS:
echo     - Windows 7 or later
echo     - ~2GB free disk space
echo     - Internet connection
echo.
echo PREREQUISITES (install manually):
echo     1. Java 17 JDK: https://adoptium.net/
echo     2. Maven: https://maven.apache.org/download.cgi
echo     3. Node.js: https://nodejs.org/
echo     4. MySQL Server (optional): https://dev.mysql.com/downloads/mysql/
echo.
echo INSTALLATION INSTRUCTIONS:
echo     1. Download and install each prerequisite
echo     2. Add installation directories to Windows PATH
echo     3. Open a new Command Prompt or Git Bash
echo     4. Navigate to the project directory
echo     5. Run this script: setup.bat
echo.
echo For Git Bash on Windows:
echo     - This script should work in Git Bash without modifications
echo     - Use: bash setup.sh (recommended for better experience)
echo.
echo For Windows Subsystem for Linux (WSL):
echo     - Use: bash setup.sh (native Linux script, fully featured)
echo.
echo For native Command Prompt/PowerShell:
echo     - Use: setup.bat (this script)
echo     - Some features may need manual configuration
echo.
pause
exit /b 0
