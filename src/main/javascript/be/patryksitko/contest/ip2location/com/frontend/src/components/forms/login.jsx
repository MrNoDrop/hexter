import { useFormik } from "formik";
import httpStatus from "http-status";
import Cookies from "js-cookie";
import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { connect } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { push } from "redux-first-routing";
import * as Yup from "yup";
import updateLoginFormValues from "../../store/actions/form/login";
import "./login.scss";

function parseErrors(errors) {
  const parsedErrors = {};
  errors.forEach((error) => {
    const startOfErrorMessage = error.indexOf(":") + 1;
    const errorMessage = error.substring(startOfErrorMessage, error.length);
    const errorMessageType = error.substring(0, startOfErrorMessage);
    switch (errorMessageType) {
      default:
        break;
      case "[EMAIL]:":
        parsedErrors.email = errorMessage;
        break;
      case "[PASSWORD]:":
        parsedErrors.password = errorMessage;
    }
  });
  return parsedErrors;
}

const validationSchema = Yup.object({
  email: Yup.string().email().required("Email is required."),
  password: Yup.string().strongPassword().required("Password is required."),
});

const mapStateToProps = ({ state }) => ({
  currentState: state,
  csrfToken: state.csrfToken,
  loginValues: state.form.login,
  fingerprint: state.fingerprint,
});

const mapDispatchToProps = (dispatch) => ({
  changePath: (path) => dispatch(push(path)),
  updateLoginFormValues: (currentState, loginValues) =>
    dispatch(updateLoginFormValues(currentState, loginValues)),
});

function LoginForm({
  changePath,
  currentState,
  loginValues,
  csrfToken,
  updateLoginFormValues,
  fingerprint,
}) {
  const navigateTo = useNavigate();
  const [initialValues] = useState({
    email: loginValues.email,
    password: loginValues.password,
  });
  const [initialErrors] = useState(loginValues.errors);
  const formik = useFormik({
    validationSchema,
    initialValues,
    initialErrors,
    onSubmit: async ({ email, password }, { setSubmitting, setErrors }) => {
      try {
        const result = await fetch("/api/user/login", {
          headers: {
            "CSRF-TOKEN": csrfToken,
            "Content-Type": "Application/json",
          },
          credentials: "include",
          method: "POST",
          body: JSON.stringify({
            email,
            password,
            fingerprint: fingerprint.visitorId,
          }),
        });
        const { status, responseType, errors, body } = await result.json();
        if (responseType === "ERROR") {
          switch (httpStatus[status]) {
            default:
              break;
            case httpStatus.NOT_FOUND:
            case httpStatus.CONFLICT:
              setErrors({ ...formik.errors, ...parseErrors(errors) });
              break;
          }
        } else if (responseType === "SUCCESS") {
          Cookies.set(
            "authentication-token",
            JSON.parse(body).authenticationToken
          );
        }
      } catch (e) {
      } finally {
        setSubmitting(false);
      }
    },
  });

  return (
    <Form className="login-form" onSubmit={formik.handleSubmit}>
      <Form.Group className="mb-3" controlId="formEmail">
        <Form.Label className="email">Email address</Form.Label>
        <Form.Control
          disabled={formik.isSubmitting}
          name="email"
          type="email"
          placeholder="Enter email"
          onChange={(event) => {
            updateLoginFormValues(currentState, {
              email: event.target.value,
              password: loginValues.password,
              errors: loginValues.errors,
            });
            formik.handleChange(event);
          }}
          onBlur={formik.handleBlur}
          value={formik.values.email}
          isValid={
            (formik.touched.email || formik.values.email !== "") &&
            ((formik.dirty && !initialErrors.email) ||
              (!formik.errors.email && formik.values.email !== ""))
          }
          isInvalid={
            (!formik.dirty && initialErrors.email) ||
            ((formik.touched.email || initialErrors.email) &&
              formik.errors.email)
          }
          feedback={
            !formik.dirty && initialErrors.email
              ? initialErrors.email
              : formik.errors.email
          }
        />
        {(!formik.errors.email && (
          <Form.Text className="text-muted">
            I'll never share your email with anyone else.
          </Form.Text>
        )) || (
          <Form.Control.Feedback type="invalid">
            {!formik.dirty && initialErrors.email
              ? initialErrors.email
              : formik.errors.email}
          </Form.Control.Feedback>
        )}
      </Form.Group>

      <Form.Group className="mb-3" controlId="formPassword">
        <Form.Label className="password">Password</Form.Label>
        <Form.Control
          disabled={formik.isSubmitting}
          name="password"
          type="password"
          placeholder="Password"
          onChange={(event) => {
            updateLoginFormValues(currentState, {
              email: loginValues.email,
              password: event.target.value,
              errors: loginValues.errors,
            });
            formik.handleChange(event);
          }}
          onBlur={formik.handleBlur}
          value={formik.values.password}
          isValid={!formik.errors.password && formik.values.password !== ""}
          isInvalid={formik.touched.password && formik.errors.password}
          feedback={formik.errors.password}
        />
        <div visible={formik.errors.password} className="feedback-invalid">
          {formik.errors.password}
        </div>
      </Form.Group>
      <Button
        variant="primary"
        type="submit"
        className="login"
        disabled={!formik.isValid || formik.isSubmitting}
      >
        Login
      </Button>
      <Link to="/forgot-password" className="forgot-password">
        Forgot password?
      </Link>
      <Button
        variant="secondary"
        type="submit"
        className="register"
        onClick={(action) => {
          action.preventDefault();
          changePath("/register");
          navigateTo("/register");
        }}
      >
        Register
      </Button>
    </Form>
  );
}

export default connect(mapStateToProps, mapDispatchToProps)(LoginForm);
