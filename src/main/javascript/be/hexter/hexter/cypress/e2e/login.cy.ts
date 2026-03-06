describe("User Login Flow", () => {
  beforeEach(() => {
    cy.visit("/login");
  });

  it("should display the login page", () => {
    cy.contains("Login").should("be.visible");
  });

  it("should successfully login with valid credentials", () => {
    cy.get('[data-testid="email"]').type("test@example.com");
    cy.get('[data-testid="password"]').type("TestPassword123!");

    cy.get('[data-testid="login-btn"]').click();

    // Verify redirect to dashboard or home
    cy.url().should("include", "/dashboard");
    cy.contains("Welcome").should("be.visible");
  });

  it("should show error for invalid email", () => {
    cy.get('[data-testid="email"]').type("nonexistent@example.com");
    cy.get('[data-testid="password"]').type("TestPassword123!");

    cy.get('[data-testid="login-btn"]').click();

    cy.contains("Invalid credentials").should("be.visible");
  });

  it("should show error for incorrect password", () => {
    cy.get('[data-testid="email"]').type("test@example.com");
    cy.get('[data-testid="password"]').type("WrongPassword123!");

    cy.get('[data-testid="login-btn"]').click();

    cy.contains("Invalid credentials").should("be.visible");
  });

  it("should show error when fields are empty", () => {
    cy.get('[data-testid="login-btn"]').click();

    cy.contains("Email is required").should("be.visible");
    cy.contains("Password is required").should("be.visible");
  });

  it("should have a forgot password link", () => {
    cy.contains("Forgot password?").should("be.visible").click();
    cy.url().should("include", "/forgot-password");
  });

  it("should have a register link", () => {
    cy.contains("Create account").should("be.visible").click();
    cy.url().should("include", "/register");
  });

  it("should validate email format", () => {
    cy.get('[data-testid="email"]').type("invalidemail");
    cy.get('[data-testid="password"]').type("TestPassword123!");

    cy.get('[data-testid="login-btn"]').click();

    cy.contains("Invalid email").should("be.visible");
  });

  it("should disable login button while request is in progress", () => {
    cy.get('[data-testid="email"]').type("test@example.com");
    cy.get('[data-testid="password"]').type("TestPassword123!");

    cy.get('[data-testid="login-btn"]').click();

    cy.get('[data-testid="login-btn"]').should("be.disabled");
  });

  it("should persist auth token in localStorage after login", () => {
    cy.get('[data-testid="email"]').type("test@example.com");
    cy.get('[data-testid="password"]').type("TestPassword123!");

    cy.get('[data-testid="login-btn"]').click();

    cy.window().then((win) => {
      expect(win.localStorage.getItem("authToken")).to.exist;
    });
  });

  it("should validate token on protected pages", () => {
    cy.visit("/dashboard");
    
    cy.window().then((win) => {
      const token = win.localStorage.getItem("authToken");
      if (!token) {
        cy.url().should("include", "/login");
      }
    });
  });
});
