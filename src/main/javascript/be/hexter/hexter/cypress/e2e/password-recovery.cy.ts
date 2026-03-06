describe("Password Recovery Flow", () => {
  beforeEach(() => {
    cy.visit("/forgot-password");
  });

  it("should display the forgot password page", () => {
    cy.contains("Reset Password").should("be.visible");
  });

  it("should request password reset for valid user hash", () => {
    cy.get('[data-testid="user-hash"]').type("valid_user_hash_123");

    cy.get('[data-testid="request-reset-btn"]').click();

    cy.contains("Reset link sent").should("be.visible");
  });

  it("should show error for invalid user hash", () => {
    cy.get('[data-testid="user-hash"]').type("invalid_hash");

    cy.get('[data-testid="request-reset-btn"]').click();

    cy.contains("User not found").should("be.visible");
  });

  it("should show error when hash field is empty", () => {
    cy.get('[data-testid="request-reset-btn"]').click();

    cy.contains("User hash is required").should("be.visible");
  });

  it("should successfully reset password with valid recovery token", () => {
    // Navigate to recovery page with token
    cy.visit("/reset-password?token=valid_recovery_token_123");

    cy.get('[data-testid="new-password"]').type("NewSecurePassword123!");
    cy.get('[data-testid="confirm-password"]').type("NewSecurePassword123!");

    cy.get('[data-testid="reset-password-btn"]').click();

    cy.contains("Password reset successfully").should("be.visible");
    cy.url().should("include", "/login");
  });

  it("should show error for invalid recovery token", () => {
    cy.visit("/reset-password?token=invalid_token");

    cy.contains("Invalid or expired reset link").should("be.visible");
  });

  it("should show error when passwords do not match", () => {
    cy.visit("/reset-password?token=valid_recovery_token_123");

    cy.get('[data-testid="new-password"]').type("NewSecurePassword123!");
    cy.get('[data-testid="confirm-password"]').type("DifferentPassword123!");

    cy.get('[data-testid="reset-password-btn"]').click();

    cy.contains("Passwords do not match").should("be.visible");
  });

  it("should validate minimum password requirements", () => {
    cy.visit("/reset-password?token=valid_recovery_token_123");

    cy.get('[data-testid="new-password"]').type("weak");
    cy.get('[data-testid="confirm-password"]').type("weak");

    cy.get('[data-testid="reset-password-btn"]').click();

    cy.contains("Password must be at least").should("be.visible");
  });

  it("should provide link back to login page", () => {
    cy.contains("Back to Login").click();
    cy.url().should("include", "/login");
  });

  it("should provide link back to register page", () => {
    cy.contains("Create new account").click();
    cy.url().should("include", "/register");
  });

  it("should disable reset button while request is in progress", () => {
    cy.visit("/reset-password?token=valid_recovery_token_123");

    cy.get('[data-testid="new-password"]').type("NewSecurePassword123!");
    cy.get('[data-testid="confirm-password"]').type("NewSecurePassword123!");

    cy.get('[data-testid="reset-password-btn"]').click();

    cy.get('[data-testid="reset-password-btn"]').should("be.disabled");
  });
});
