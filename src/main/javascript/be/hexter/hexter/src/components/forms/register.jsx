import { useFormik } from "formik";
import httpStatus from "http-status";
import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { connect } from "react-redux";
import { Link } from "react-router-dom";
import { goBack } from "redux-first-routing";
import * as Yup from "yup";
import setRegisterFormValues from "../../store/actions/form/register";
import "./register.scss";

const validationSchema = Yup.object({
  firstname: Yup.string()
    .matches(/^[a-zA-Z]+$/, "Must only contain letters.")
    .required("Firstname is required."),
  lastname: Yup.string()
    .matches(/^[a-zA-Z]+$/, "Must only contain letters.")
    .required("Lastname is required."),
  email: Yup.string().email().required("Email is required."),
  password: Yup.string().strongPassword().required("Password is required."),
  repassword: Yup.string()
    .required("Retyping password is required.")
    .oneOf([Yup.ref("password"), null], "Passwords don't match!"),
});
const mapStateToProps = ({ state }) => ({
  currentState: state,
  csrfToken: state.csrfToken,
  registerValues: state.form.register,
});

const mapDispatchToProps = (dispatch) => ({
  goBack: (path) => dispatch(goBack(path)),
  updateRegisterFormValues: (currentState, registerFormValues) =>
    dispatch(setRegisterFormValues(currentState, registerFormValues)),
});

const ReturnToLoginQuestion = ({ formik, initialErrors }) => {
  const formikErrorsIncludeRequiredErrorToShow = formik.errors.email?.includes(
    `The email "${formik.values.email}" is already registered.`
  );
  const formikIsNotDirtyButErrorPersisted =
    !formik.dirty &&
    initialErrors.email?.includes(
      `The email "${formik.values.email}" is already registered.`
    );
  const show =
    formikErrorsIncludeRequiredErrorToShow || formikIsNotDirtyButErrorPersisted;
  return (
    <div hidden={!show} className="action-suggestion">
      Do you want to return to the{" "}
      <Link className="return-to-login-page" to="/login">
        login
      </Link>{" "}
      page?
    </div>
  );
};

function LoginForm({
  goBack,
  currentState,
  csrfToken,
  registerValues,
  updateRegisterFormValues,
}) {
  const [initialValues] = useState({
    firstname: registerValues.firstname,
    lastname: registerValues.lastname,
    email: registerValues.email,
    password: registerValues.password,
    repassword: registerValues.repassword,
  });
  const [initialErrors] = useState(
    (() =>
      registerValues.errors.email?.includes(registerValues.email)
        ? registerValues.errors
        : (() => {
            delete registerValues.errors.email;
            return registerValues.errors;
          })())()
  );
  const formik = useFormik({
    validationSchema,
    initialValues,
    initialErrors,
    onSubmit: async (
      { firstname, lastname, email, password },
      { setSubmitting, setErrors }
    ) => {
      try {
        const result = await fetch("/api/user/register", {
          headers: {
            "CSRF-TOKEN": csrfToken,
            "Content-Type": "Application/json",
          },
          credentials: "include",
          method: "PUT",
          body: JSON.stringify({
            firstname,
            lastname,
            credential: { email, password },
          }),
        });
        const { status, responseType, errors } = await result.json();
        if (httpStatus[status] === httpStatus.CONFLICT) {
          if (responseType === "ERROR") {
            const parsedErrors = {};
            errors.forEach((error) => {
              const startOfErrorMessage = error.indexOf(":") + 1;
              const errorMessage = error.substring(
                startOfErrorMessage,
                error.length
              );
              const errorMessageType = error.substring(0, startOfErrorMessage);
              switch (errorMessageType) {
                default:
                  break;
                case "[EMAIL]:":
                  parsedErrors.email = errorMessage;
                  break;
              }
            });
            setErrors(parsedErrors);
            updateRegisterFormValues(currentState, {
              firstname: registerValues.firstname,
              lastname: registerValues.lastname,
              email: registerValues.email,
              password: registerValues.password,
              repassword: registerValues.repassword,
              errors: { ...registerValues.errors, ...parsedErrors },
              undirtyFormAllowed: responseType === "ERROR",
            });
          }
        }
      } catch (e) {
      } finally {
        setSubmitting(false);
      }
    },
  });
  return (
    <div className="register-form-wrapper">
      <Form className="register-form" onSubmit={formik.handleSubmit}>
        <Form.Group
          className="mb-3 given-names-wrapper firstname"
          controlId="formFirstname"
        >
          <Form.Label className="given-names">First name</Form.Label>
          <Form.Control
            disabled={formik.isSubmitting}
            name="firstname"
            className="given-names"
            type="text"
            placeholder="Enter first name"
            onChange={(event) => {
              updateRegisterFormValues(currentState, {
                firstname: event.target.value,
                lastname: registerValues.lastname,
                email: registerValues.email,
                password: registerValues.password,
                repassword: registerValues.repassword,
                errors: registerValues.errors,
              });
              formik.handleChange(event);
            }}
            onBlur={formik.handleBlur}
            value={formik.values.firstname}
            isValid={!formik.errors.firstname && formik.values.firstname !== ""}
            isInvalid={formik.touched.firstname && formik.errors.firstname}
            feedback={formik.errors.firstname}
          />
          <Form.Control.Feedback type="invalid">
            {formik.errors.firstname}
          </Form.Control.Feedback>
        </Form.Group>
        <Form.Group
          className="mb-3 given-names-wrapper lastname"
          controlId="formLastname"
        >
          <Form.Label className="given-names">Last name</Form.Label>
          <Form.Control
            disabled={formik.isSubmitting}
            name="lastname"
            className="given-names"
            type="text"
            placeholder="Enter last name"
            onChange={(event) => {
              updateRegisterFormValues(currentState, {
                firstname: registerValues.firstname,
                lastname: event.target.value,
                email: registerValues.email,
                password: registerValues.password,
                repassword: registerValues.repassword,
                errors: registerValues.errors,
              });
              formik.handleChange(event);
            }}
            onBlur={formik.handleBlur}
            value={formik.values.lastname}
            isValid={!formik.errors.lastname && formik.values.lastname !== ""}
            isInvalid={formik.touched.lastname && formik.errors.lastname}
            feedback={formik.errors.lastname}
          />
          <Form.Control.Feedback type="invalid">
            {formik.errors.lastname}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group className="mb-3" controlId="formEmail">
          <Form.Label className="email">Email address</Form.Label>
          <ReturnToLoginQuestion {...{ formik, initialErrors }} />
          <Form.Control
            disabled={formik.isSubmitting}
            name="email"
            type="email"
            placeholder="Enter email"
            onChange={(event) => {
              updateRegisterFormValues(currentState, {
                firstname: registerValues.firstname,
                lastname: registerValues.lastname,
                email: event.target.value,
                password: registerValues.password,
                repassword: registerValues.repassword,
                errors: registerValues.errors,
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
          <Form.Control.Feedback type="invalid">
            {!formik.dirty && initialErrors.email
              ? initialErrors.email
              : formik.errors.email}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group
          className="mb-3 form-group-password"
          controlId="formPassword"
        >
          <Button className="go-back" onClick={goBack}>
            go back
          </Button>
          <Form.Label className="password">Password</Form.Label>
          <Form.Control
            disabled={formik.isSubmitting}
            name="password"
            type="password"
            placeholder="Password"
            onChange={(event) => {
              updateRegisterFormValues(currentState, {
                firstname: registerValues.firstname,
                lastname: registerValues.lastname,
                email: registerValues.email,
                password: event.target.value,
                repassword: registerValues.repassword,
                errors: registerValues.errors,
              });
              formik.handleChange(event);
            }}
            onBlur={formik.handleBlur}
            value={formik.values.password}
            isValid={!formik.errors.password && formik.values.password !== ""}
            isInvalid={formik.touched.password && formik.errors.password}
            feedback={formik.errors.password}
          />
        </Form.Group>
        <div
          visible={formik.errors.password ? true : false}
          className="feedback-invalid"
        >
          {formik.errors.password}
        </div>

        <Form.Group className="mb-3" controlId="formRetypePassword">
          <Form.Label className="password-label">Retype Password</Form.Label>
          <Form.Control
            disabled={formik.isSubmitting}
            name="repassword"
            type="password"
            placeholder="Retype Password"
            onChange={(event) => {
              updateRegisterFormValues(currentState, {
                firstname: registerValues.firstname,
                lastname: registerValues.lastname,
                email: registerValues.email,
                password: registerValues.password,
                repassword: event.target.value,
                errors: registerValues.errors,
              });
              formik.handleChange(event);
            }}
            onBlur={formik.handleBlur}
            value={formik.values.repassword}
            isValid={
              !formik.errors.repassword && formik.values.repassword !== ""
            }
            isInvalid={formik.touched.repassword && formik.errors.repassword}
            feedback={formik.errors.repassword}
          />
        </Form.Group>
        <div className="feedback-invalid">{formik.errors.repassword}</div>
        <Button
          variant="primary"
          type="submit"
          className="register"
          disabled={
            !(
              formik.isValid &&
              (formik.dirty || registerValues.undirtyFormAllowed)
            ) || formik.isSubmitting
          }
        >
          Register
        </Button>
      </Form>
    </div>
  );
}

export default connect(mapStateToProps, mapDispatchToProps)(LoginForm);
