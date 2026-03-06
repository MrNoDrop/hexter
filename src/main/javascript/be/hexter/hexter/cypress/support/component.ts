// Cypress Component Testing Support File
import "./component.css";

// Helpful to keep tests isolated and not pollute the window object
beforeEach(() => {
  cy.fixture("example").then((data) => {
    // Data is available here
  });
});
