describe("User Registration Flow", () => {
  beforeEach(() => {
    cy.visit("/");
  });

  it("should display the home page", () => {
    cy.get("body").should("be.visible");
  });

  it("should navigate to registration page", () => {
    // Adjust selectors based on your actual app structure
    cy.contains("Register").click().should("exist");
  });

  it("should successfully register a new user", () => {
    cy.visit("/register");

    // Fill in registration form
    cy.get('[data-testid="email"]').type("newuser@example.com");
    cy.get('[data-testid="nickname"]').type("newusername");
    cy.get('[data-testid="password"]').type("SecurePassword123!");
    cy.get('[data-testid="confirm-password"]').type("SecurePassword123!");

    // Submit form
    cy.get('[data-testid="register-btn"]').click();

    // Verify success message or redirect
    cy.url().should("include", "/login");
  });

  it("should show validation error for invalid email", () => {
    cy.visit("/register");

    cy.get('[data-testid="email"]').type("invalidemail");
    cy.get('[data-testid="nickname"]').type("testuser");
    cy.get('[data-testid="password"]').type("password123");
    cy.get('[data-testid="register-btn"]').click();

    cy.contains("Invalid email").should("be.visible");
  });

  it("should show error for weak password", () => {
    cy.visit("/register");

    cy.get('[data-testid="email"]').type("test@example.com");
    cy.get('[data-testid="nickname"]').type("testuser");
    cy.get('[data-testid="password"]').type("123");
    cy.get('[data-testid="register-btn"]').click();

    cy.contains("Password must be").should("be.visible");
  });

  it("should show error when passwords do not match", () => {
    cy.visit("/register");

    cy.get('[data-testid="email"]').type("test@example.com");
    cy.get('[data-testid="nickname"]').type("testuser");
    cy.get('[data-testid="password"]').type("SecurePassword123!");
    cy.get('[data-testid="confirm-password"]').type("DifferentPassword123!");

    cy.get('[data-testid="register-btn"]').click();

    cy.contains("Passwords do not match").should("be.visible");
  });
});
