import React from "react";
import {
  Button,
  Container,
  Form,
  Nav,
  Navbar,
  NavDropdown,
} from "react-bootstrap";
import { connect } from "react-redux";
import { useNavigate } from "react-router-dom";
import { push } from "redux-first-routing";
import "./menu.scss";

const mapDispatchToProps = (dispatch) => ({
  changePath: (path) => dispatch(push(path)),
});

function Menu({ changePath }) {
  const navigateTo = useNavigate();
  return (
    <Navbar bg="light" expand="lg">
      <Container fluid>
        <Navbar.Brand
          style={{ cursor: "pointer" }}
          onClick={(event) => {
            event.preventDefault();
            changePath("/");
            navigateTo("/");
          }}
        >
          <img src="/rsc/icons/logo.png" alt="brand logo" />
          <div className="buffer" />
          IP2Location.Lite
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav
            className="me-auto my-2 my-lg-0"
            style={{ maxHeight: "100px" }}
            navbarScroll
          >
            <Nav.Link
              onClick={(event) => {
                event.preventDefault();
                changePath("/");
                navigateTo("/");
              }}
            >
              Home
            </Nav.Link>
            <NavDropdown title="Lookup" id="navbarScrollingDropdown">
              <NavDropdown.Item
                onClick={(event) => {
                  event.preventDefault();
                  changePath("/ipV4-lookup");
                  navigateTo("/ipV4-lookup");
                }}
              >
                ipV4
              </NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item
                onClick={(event) => {
                  event.preventDefault();
                  changePath("/ipV6-lookup");
                  navigateTo("/ipV6-lookup");
                }}
              >
                ipV6
              </NavDropdown.Item>
            </NavDropdown>
            <Nav.Link
              onClick={(event) => {
                event.preventDefault();
                changePath("/proxy-checkup");
                navigateTo("/proxy-checkup");
              }}
            >
              Proxy checkup
            </Nav.Link>
          </Nav>
          <Form className="d-flex" onChange={(e) => {}}>
            <Form.Control
              type="search"
              placeholder="Search"
              className="me-2"
              aria-label="Search"
            />
            <Button variant="outline-success">Search</Button>
          </Form>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default connect(null, mapDispatchToProps)(Menu);
