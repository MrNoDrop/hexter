import React from "react";

// Mock component for testing
const LoginForm = ({ onSubmit }) => {
  const [email, setEmail] = React.useState("");
  const [password, setPassword] = React.useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ email, password });
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="email">Email</label>
        <input
          id="email"
          data-testid="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>
      <div>
        <label htmlFor="password">Password</label>
        <input
          id="password"
          data-testid="password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>
      <button data-testid="login-btn" type="submit">
        Login
      </button>
    </form>
  );
};

describe("LoginForm Component", () => {
  it("should render login form", () => {
    cy.mount(<LoginForm onSubmit={cy.stub()} />);

    cy.get('[data-testid="email"]').should("be.visible");
    cy.get('[data-testid="password"]').should("be.visible");
    cy.get('[data-testid="login-btn"]').should("be.visible");
  });

  it("should update input values", () => {
    cy.mount(<LoginForm onSubmit={cy.stub()} />);

    cy.get('[data-testid="email"]').type("test@example.com");
    cy.get('[data-testid="email"]').should(
      "have.value",
      "test@example.com"
    );

    cy.get('[data-testid="password"]').type("password123");
    cy.get('[data-testid="password"]').should("have.value", "password123");
  });

  it("should call onSubmit with form data", () => {
    const onSubmit = cy.stub();
    cy.mount(<LoginForm onSubmit={onSubmit} />);

    cy.get('[data-testid="email"]').type("test@example.com");
    cy.get('[data-testid="password"]').type("password123");
    cy.get('[data-testid="login-btn"]').click();

    cy.wrap(onSubmit).should(
      "be.calledWith",
      Cypress.sinon.match({
        email: "test@example.com",
        password: "password123",
      })
    );
  });

  it("should handle empty form submission", () => {
    const onSubmit = cy.stub();
    cy.mount(<LoginForm onSubmit={onSubmit} />);

    cy.get('[data-testid="login-btn"]').click();

    cy.wrap(onSubmit).should(
      "be.calledWith",
      Cypress.sinon.match({
        email: "",
        password: "",
      })
    );
  });
});
