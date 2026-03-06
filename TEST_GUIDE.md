# Hexter Testing Guide

## Overview

This document provides comprehensive instructions for running tests in the Hexter project, which includes both backend (Java/Spring Boot) and frontend (React) tests.

## Backend Testing (Java/Spring Boot)

### Prerequisites

- Java 17 JDK
- Maven 3.6+
- MySQL Server (for production integration tests)
- H2 Database (included for development/test)

### Test Structure

```
src/test/java/be/hexter/hexter/
├── ApplicationTests.java                    # Basic context loading test
├── UserIntegrationTest.java                 # Integration tests with real DB
├── controller/
│   └── UserControllerTest.java              # REST API unit tests
└── service/implementation/
    ├── UserServiceImplementationTest.java   # User service unit tests
    ├── RecoveryServiceImplementationTest.java
    └── AuthenticationTokenServiceImplementationTest.java
```

### Running Backend Tests

#### Run All Tests

```bash
cd /home/patryk/Documenten/hexter
export JAVA_HOME=/home/patryk/.jdk/jdk-17.0.16
mvn clean test
```

#### Run Specific Test Class

```bash
mvn test -Dtest=UserServiceImplementationTest
```

#### Run Tests with Coverage Report

```bash
mvn clean test jacoco:report
# Coverage report will be generated at: target/site/jacoco/index.html
```

#### Run Tests with Detailed Output

```bash
mvn test -X
```

#### Skip Tests during Build

```bash
mvn clean install -DskipTests
```

### Test Coverage

The project uses **JaCoCo** for code coverage analysis. Coverage reports include:

- Line coverage
- Branch coverage
- Method coverage
- Complexity metrics

Generated reports are located in: `target/site/jacoco/index.html`

### Test Coverage Goals

- **Target:** 80-90% overall coverage
- **Critical paths:** 95%+ (authentication, registration, recovery)
- **Utilities:** 50%+ (optional, nice-to-have)

### Backend Test Categories

#### 1. Unit Tests

Test individual service methods in isolation using Mockito for dependency mocking.

**Example:** `UserServiceImplementationTest.java`

- Tests for `registerUser()` with valid/invalid inputs
- Tests for `authenticateUser()` with various scenarios
- Tests for token validation
- Tests for recovery token handling

**Run unit tests only:**

```bash
mvn test -Dtest=*ServiceImplementationTest
```

#### 2. Integration Tests

Test full workflows with real database connections.

**Example:** `UserIntegrationTest.java`

- End-to-end registration workflow
- Login after registration
- API endpoint integration

**Run integration tests only:**

```bash
mvn test -Dtest=*IntegrationTest
```

#### 3. Controller/REST Tests

Test REST endpoints using MockMvc.

**Example:** `UserControllerTest.java`

- POST /api/user/register
- POST /api/user/login
- POST /api/user/validate-authentication-token
- POST /api/user/request-recover-password
- POST /api/user/recover-password

**Run controller tests only:**

```bash
mvn test -Dtest=*ControllerTest
```

### Test Reports

After running tests, view results:

```bash
# Text report
mvn test | tail -50

# JaCoCo coverage report (HTML)
mvn jacoco:report
open target/site/jacoco/index.html  # macOS
firefox target/site/jacoco/index.html  # Linux
```

## Frontend Testing (React + Cypress)

### Prerequisites

- Node.js 16+ (verify: `node --version`)
- npm 8+ (verify: `npm --version`)
- Port 3000 available (for React dev server)
- Port 8080 available (for backend API server)

### Test Structure

```
src/main/javascript/be/hexter/hexter/
├── cypress.config.ts                          # Cypress configuration
├── cypress/
│   ├── support/
│   │   ├── e2e.ts                            # E2E support file
│   │   └── component.ts                      # Component test support
│   ├── e2e/
│   │   ├── registration.cy.ts                # User registration tests
│   │   ├── login.cy.ts                       # User login tests
│   │   ├── password-recovery.cy.ts           # Password recovery tests
│   │   └── api.cy.ts                         # Backend API integration tests
│   └── component/
│       └── LoginForm.cy.tsx                  # React component tests
└── package.json                              # Scripts configuration
```

### Installing Frontend Test Dependencies

```bash
cd /home/patryk/Documenten/hexter/src/main/javascript/be/hexter/hexter

# Install Cypress (already done, but shown here for reference)
npm install --save-dev cypress

# Install other test dependencies if needed
npm install --save-dev @testing-library/react @testing-library/jest-dom
```

### Running Frontend Tests

#### 1. Open Cypress Test Runner (Interactive Mode)

```bash
cd src/main/javascript/be/hexter/hexter
npm run test:e2e:open

# This will open the Cypress Test Runner where you can:
# - See all available tests
# - Run tests one by one
# - Watch tests execute in real-time
# - Debug failing tests
```

#### 2. Run All E2E Tests (Headless Mode)

```bash
cd src/main/javascript/be/hexter/hexter
npm run test:e2e

# Runs all tests in: cypress/e2e/*.cy.ts
```

#### 3. Run Specific E2E Test File

```bash
cd src/main/javascript/be/hexter/hexter

# Run registration tests
npx cypress run --spec "cypress/e2e/registration.cy.ts"

# Run login tests
npx cypress run --spec "cypress/e2e/login.cy.ts"

# Run password recovery tests
npx cypress run --spec "cypress/e2e/password-recovery.cy.ts"

# Run API tests
npx cypress run --spec "cypress/e2e/api.cy.ts"
```

#### 4. Run Component Tests

```bash
cd src/main/javascript/be/hexter/hexter

# Open component test runner
npm run test:component:open

# Run component tests in headless mode
npm run test:component
```

#### 5. Run Unit Tests with Jest

```bash
cd src/main/javascript/be/hexter/hexter

# Run Jest tests (in interactive watch mode)
npm test

# Run Jest tests once
npm test -- --watchAll=false

# Run Jest with coverage
npm test -- --coverage --watchAll=false
```

#### 6. Run All Frontend Tests Together

```bash
cd src/main/javascript/be/hexter/hexter
npm run test:all
```

### Frontend Test Setup Requirements

Before running frontend tests, ensure:

1. **Backend API Server is Running**

   ```bash
   # In a separate terminal
   cd /home/patryk/Documenten/hexter
   export JAVA_HOME=/home/patryk/.jdk/jdk-17.0.16
   java -jar target/hexter-0.0.1-SNAPSHOT.jar

   # Verify: curl http://localhost:8080/api/user/validate-authentication-token
   ```

2. **Database is Configured**
   - For dev/test: Uses H2 in-memory database (automatic)
   - For integration: Update application.properties with MySQL credentials

3. **Frontend Dev Server (Optional, for open tests)**

   ```bash
   # In another terminal
   cd src/main/javascript/be/hexter/hexter
   npm start

   # Accessible at: http://localhost:3000
   ```

### Frontend Test Examples

#### Registration Tests (registration.cy.ts)

- ✅ Display home page
- ✅ Navigate to registration page
- ✅ Successfully register new user
- ✅ Validate email format
- ✅ Validate password strength
- ✅ Verify password matches confirmation

#### Login Tests (login.cy.ts)

- ✅ Display login page
- ✅ Successful login with valid credentials
- ✅ Error handling for invalid email
- ✅ Error handling for incorrect password
- ✅ Required field validation
- ✅ Forgot password link
- ✅ Register link
- ✅ Token persistence in localStorage
- ✅ Token validation on protected pages

#### Password Recovery Tests (password-recovery.cy.ts)

- ✅ Display forgot password page
- ✅ Request password reset
- ✅ Invalid user hash handling
- ✅ Reset password with valid token
- ✅ Invalid/expired token handling
- ✅ Password match validation
- ✅ Password strength requirements

#### API Integration Tests (api.cy.ts)

- ✅ User registration API
- ✅ User login API
- ✅ Authentication token validation
- ✅ Password recovery requests
- ✅ Proper error formats
- ✅ CORS header validation

## Continuous Integration (CI)

### Running All Tests (Backend + Frontend)

```bash
#!/bin/bash
# Run all tests in sequence

echo "Building project..."
cd /home/patryk/Documenten/hexter
export JAVA_HOME=/home/patryk/.jdk/jdk-17.0.16
mvn clean install

echo "Running backend tests..."
mvn test

echo "Generating coverage report..."
mvn jacoco:report

echo "Running frontend tests..."
cd src/main/javascript/be/hexter/hexter
npm install
npm run test:e2e -- --headless --no-interactive

echo "All tests completed!"
```

### CI/CD Pipeline Script

Create `run-all-tests.sh`:

```bash
#!/bin/bash
set -e

PROJECT_ROOT="/home/patryk/Documenten/hexter"
JAVA_HOME="/home/patryk/.jdk/jdk-17.0.16"
FRONTEND_DIR="$PROJECT_ROOT/src/main/javascript/be/hexter/hexter"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Starting Hexter Test Suite${NC}"

# Backend Tests
echo -e "\n${YELLOW}=== Backend Tests ===${NC}"
cd "$PROJECT_ROOT"
export JAVA_HOME
mvn clean test || { echo -e "${RED}Backend tests failed${NC}"; exit 1; }

echo -e "${GREEN}Backend tests passed${NC}"

# Coverage Report
echo -e "\n${YELLOW}=== Generating Coverage Report ===${NC}"
mvn jacoco:report
echo -e "${GREEN}Coverage report generated: target/site/jacoco/index.html${NC}"

# Frontend Tests
echo -e "\n${YELLOW}=== Frontend Tests ===${NC}"
cd "$FRONTEND_DIR"
npm install || { echo -e "${RED}npm install failed${NC}"; exit 1; }
npm run test:e2e || { echo -e "${RED}Frontend E2E tests failed${NC}"; exit 1; }

echo -e "${GREEN}Frontend tests passed${NC}"

echo -e "\n${GREEN}All tests completed successfully!${NC}"
```

Make it executable and run:

```bash
chmod +x run-all-tests.sh
./run-all-tests.sh
```

## Test Naming Conventions

### Backend Tests

- **Unit Tests:** `*ServiceImplementationTest.java`, `*ControllerTest.java`
- **Integration Tests:** `*IntegrationTest.java`
- **Test Methods:** `testDescriptionOfWhatIsBeingTested()`
  - Example: `testRegisterUserSuccess()`, `testLoginWithInvalidEmail()`

### Frontend Tests

- **E2E Tests:** `*.cy.ts` in `cypress/e2e/`
- **Component Tests:** `*.cy.tsx` in `cypress/component/`
- **Test Suites:** `describe("Feature Name", () => { ... })`
- **Test Cases:** `it("should do X", () => { ... })`

## Debugging Tests

### Backend Test Debugging

1. **View detailed test output:**

   ```bash
   mvn test -X
   ```

2. **Run single test with debugging:**

   ```bash
   mvn test -Dtest=UserServiceImplementationTest#testRegisterUserSuccess
   ```

3. **Enable logging in tests:**
   Add to `application-test.properties`:
   ```properties
   logging.level.be.hexter=DEBUG
   logging.level.org.springframework=DEBUG
   ```

### Frontend Test Debugging

1. **Open Cypress Interactive Mode:**

   ```bash
   npm run test:e2e:open
   ```

   - Inspect elements in real-time
   - Step through tests
   - Check network requests

2. **View test video recordings:**
   - Videos are saved in `cypress/videos/`
   - Enable in `cypress.config.ts`: `video: true`

3. **Enable debug logs:**

   ```bash
   DEBUG=cypress:* npm run test:e2e
   ```

4. **Use browser DevTools in Cypress:**
   - Open Inspector in Cypress Runner
   - Execute commands in DevTools Console
   - Inspect element state

## Test Reports and Metrics

### Backend Coverage Report

```bash
# Generate JaCoCo coverage report
mvn jacoco:report

# View HTML report
open target/site/jacoco/index.html  # macOS
xdg-open target/site/jacoco/index.html  # Linux
```

### Frontend Coverage Report

```bash
npm test -- --coverage --watchAll=false

# View HTML report (if configured)
open coverage/lcov-report/index.html
```

## Known Issues and Solutions

### Issue: Cypress Hangs on Installation

**Solution:**

```bash
npm install cypress --no-save 2>&1 &
# Let it run in background, check progress periodically
```

### Issue: "Port 3000 already in use"

**Solution:**

```bash
# Find and kill process using port 3000
lsof -ti :3000 | xargs kill -9

# Or use different port
PORT=3001 npm start
```

### Issue: "Backend API not responding"

**Solution:**

```bash
# Verify backend is running
curl http://localhost:8080/api/user/validate-authentication-token -H "Content-Type: application/json" -d '{"fingerprint":"test","authenticationToken":"test"}'

# Check logs
tail -f /var/log/hexter/hexter.log
```

### Issue: Test Timeouts

**Solution:**

```bash
# Increase timeout in cypress.config.ts
export default defineConfig({
  e2e: {
    requestTimeout: 10000,
    responseTimeout: 10000,
    defaultCommandTimeout: 5000,
  },
});
```

## Best Practices

1. **Keep tests isolated** - Each test should be independent
2. **Use descriptive names** - Test names should explain what they verify
3. **Follow AAA pattern** - Arrange, Act, Assert
4. **Mock external dependencies** - Use Mockito for Java, cy.stub() for frontend
5. **Review coverage reports** - Aim for 80%+ coverage
6. **Run tests frequently** - Before each commit
7. **Maintain test data** - Use fixtures for consistent test data
8. **Document complex tests** - Add comments explaining test logic

## Resources

- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)
- [JaCoCo Documentation](https://www.eclemma.org/jacoco/)
- [Cypress Documentation](https://docs.cypress.io/)
- [Jest Documentation](https://jestjs.io/)
- [Mockito Guide](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

---

**Last Updated:** February 2026
**Test Coverage Target:** 80-90%
**Maintainers:** Hexter Development Team
