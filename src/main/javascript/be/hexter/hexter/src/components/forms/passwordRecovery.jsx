import React from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";

import "./passwordRecovery.scss";

function PasswordRecovery() {
  return (
    <Form className="password-recovery-form">
      <Form.Group className="mb-3" controlId="formEmail">
        <Form.Label>Forgot your password?</Form.Label>
        <Form.Control />
      </Form.Group>
    </Form>
  );
}

export default PasswordRecovery;
