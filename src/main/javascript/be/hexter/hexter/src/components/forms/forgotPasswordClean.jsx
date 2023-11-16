import { useFormik } from "formik";
import React, { useState } from "react";
import { connect } from "react-redux";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { goBack } from "redux-first-routing";
import * as Yup from "yup";

const validationSchema = Yup.object({
  email: Yup.string().email().required("Email is required."),
  password: Yup.string().strongPassword().required("Password is required."),
  repassword: Yup.string()
    .required("Retyping password is required.")
    .oneOf([Yup.ref("password"), null], "Passwords don't match!"),
});

const mapStateToProps = ({ state }) => ({
  forgotValues: state.form.forgot,
});

const mapDispatchToProps = (dispatch) => ({
  goBack: (path) => dispatch(goBack(path)),
});

function ForgotPassword({ forgotValues, goBack }) {
  const [initialValues] = useState({
    email: forgotValues.email,
    recoveryToken: forgotValues.recoveryToken,
    password: forgotValues.password,
    repassword: forgotValues.repassword,
  });

  const [initialErrors] = useState(forgotValues.errors);

  const formik = useFormik({
    validationSchema,
    initialValues,
    initialErrors,
    onSubmit: async () => {},
  });
  return (
    <Form>
      <Form.Group className="mb-3" controlId="formEmail">
        <Button className="go-back" onClick={goBack}>
          go back
        </Button>
        <Form.Label>Forgot your password?</Form.Label>
        <Form.Control></Form.Control>
      </Form.Group>
    </Form>
  );
}

export default connect(mapStateToProps)(ForgotPassword);
