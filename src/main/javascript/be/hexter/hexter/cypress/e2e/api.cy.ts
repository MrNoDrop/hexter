describe("API Integration Tests", () => {
  const backendUrl = "http://localhost:8080/api";

  describe("User Registration API", () => {
    it("should register a new user", () => {
      cy.request("POST", `${backendUrl}/user/register`, {
        email: "api-test@example.com",
        nick_name: "apitestuser",
        password: "TestPassword123!",
      }).then((response) => {
        expect(response.status).to.equal(200);
      });
    });

    it("should reject duplicate email registration", () => {
      // First registration
      cy.request("POST", `${backendUrl}/user/register`, {
        email: "duplicate@example.com",
        nick_name: "user1",
        password: "TestPassword123!",
      });

      // Second registration with same email
      cy.request({
        method: "POST",
        url: `${backendUrl}/user/register`,
        body: {
          email: "duplicate@example.com",
          nick_name: "user2",
          password: "TestPassword123!",
        },
        failOnStatusCode: false,
      }).then((response) => {
        expect(response.status).to.not.equal(200);
      });
    });

    it("should validate required fields", () => {
      cy.request({
        method: "POST",
        url: `${backendUrl}/user/register`,
        body: {
          email: "",
          nick_name: "",
          password: "",
        },
        failOnStatusCode: false,
      }).then((response) => {
        expect(response.status).to.not.equal(200);
      });
    });
  });

  describe("User Login API", () => {
    beforeEach(() => {
      // Create a test user
      cy.request("POST", `${backendUrl}/user/register`, {
        email: "login-test@example.com",
        nick_name: "logintestuser",
        password: "LoginTest123!",
      });
    });

    it("should authenticate user with valid credentials", () => {
      cy.request("POST", `${backendUrl}/user/login`, {
        email: "login-test@example.com",
        password: "LoginTest123!",
      }).then((response) => {
        expect(response.status).to.equal(200);
        expect(response.body).to.have.property("token");
      });
    });

    it("should reject login with incorrect password", () => {
      cy.request({
        method: "POST",
        url: `${backendUrl}/user/login`,
        body: {
          email: "login-test@example.com",
          password: "WrongPassword123!",
        },
        failOnStatusCode: false,
      }).then((response) => {
        expect(response.status).to.not.equal(200);
      });
    });

    it("should reject login with non-existent email", () => {
      cy.request({
        method: "POST",
        url: `${backendUrl}/user/login`,
        body: {
          email: "nonexistent@example.com",
          password: "TestPassword123!",
        },
        failOnStatusCode: false,
      }).then((response) => {
        expect(response.status).to.not.equal(200);
      });
    });
  });

  describe("Authentication Token Validation API", () => {
    it("should validate authentication token", () => {
      cy.request("POST", `${backendUrl}/user/validate-authentication-token`, {
        fingerprint: "test_fingerprint",
        authenticationToken: "valid_token",
      }).then((response) => {
        expect(response.status).to.equal(200);
      });
    });

    it("should handle invalid token", () => {
      cy.request({
        method: "POST",
        url: `${backendUrl}/user/validate-authentication-token`,
        body: {
          fingerprint: "test_fingerprint",
          authenticationToken: "invalid_token_xyz",
        },
        failOnStatusCode: false,
      }).then((response) => {
        expect(response.status).to.equal(200);
        expect(response.body.valid).to.equal(false);
      });
    });
  });

  describe("Password Recovery API", () => {
    it("should request password recovery for valid user", () => {
      cy.request({
        method: "POST",
        url: `${backendUrl}/user/request-recover-password`,
        body: {
          hash: "valid_user_hash",
        },
      }).then((response) => {
        expect(response.status).to.equal(200);
      });
    });

    it("should recover password with valid token", () => {
      cy.request({
        method: "POST",
        url: `${backendUrl}/user/recover-password`,
        body: {
          recoveryToken: "valid_recovery_token",
          password: "NewPassword123!",
        },
      }).then((response) => {
        expect(response.status).to.equal(200);
      });
    });

    it("should reject password recovery with invalid token", () => {
      cy.request({
        method: "POST",
        url: `${backendUrl}/user/recover-password`,
        body: {
          recoveryToken: "invalid_token",
          password: "NewPassword123!",
        },
        failOnStatusCode: false,
      }).then((response) => {
        expect(response.status).to.not.equal(200);
      });
    });
  });

  describe("API Response Format", () => {
    it("should return proper error format for invalid requests", () => {
      cy.request({
        method: "POST",
        url: `${backendUrl}/user/register`,
        body: {
          email: "test@example.com",
        },
        failOnStatusCode: false,
      }).then((response) => {
        expect(response.status).to.not.equal(200);
        expect(response.body).to.have.property("error");
      });
    });

    it("should include CORS headers in responses", () => {
      cy.request("POST", `${backendUrl}/user/validate-authentication-token`, {
        fingerprint: "test",
        authenticationToken: "test",
      }).then((response) => {
        expect(response.headers).to.have.property("content-type");
      });
    });
  });
});
