import { render, screen } from "@testing-library/react";
import App from "./App";

test("renders landing page text", () => {
  render(<App />);
  const linkElement = screen.getByText(/landing page/i);
  expect(linkElement).toBeInTheDocument();
});
