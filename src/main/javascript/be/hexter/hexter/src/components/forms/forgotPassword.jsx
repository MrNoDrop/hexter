import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import httpStatus from "http-status";
import { connect } from "react-redux";
import Form from "react-bootstrap/Form";
import { useFormik } from "formik";
import { Link, useNavigate, useParams } from "react-router-dom";
import * as Yup from "yup";
import updateForgotFormValues from "../../store/actions/form/forgot";
import { goBack } from "redux-first-routing";
import { push } from "redux-first-routing";
import "./forgotPassword.scss";

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
    }
  });
  return parsedErrors;
}

const validationSchema = Yup.object({
  email: Yup.string().email().required("Email is required."),
});
const recoveryValidationSchema = Yup.object({
  password: Yup.string().strongPassword().required("Password is required."),
  repassword: Yup.string()
    .required("Retyping password is required.")
    .oneOf([Yup.ref("password"), null], "Passwords don't match!"),
});

const mapStateToProps = ({ state }) => ({
  currentState: state,
  csrfToken: state.csrfToken,
  forgotValues: state.form.forgot,
});

const mapDispatchToProps = (dispatch) => ({
  changePath: (path) => dispatch(push(path)),
  goBack: (path) => dispatch(goBack(path)),
  updateForgotFormValues: (currentState, forgotValues) =>
    dispatch(updateForgotFormValues(currentState, forgotValues)),
});
const GoToRegistryQuestion = ({ formik, initialErrors }) => {
  const formikErrorsIncludeRequiredErrorToShow = formik.errors.email?.includes(
    `This email is unregistered.`
  );
  const formikIsNotDirtyButErrorPersisted =
    !formik.dirty &&
    initialErrors.email?.includes(`This email is unregistered.`);
  const show =
    formikErrorsIncludeRequiredErrorToShow || formikIsNotDirtyButErrorPersisted;
  return (
    <div hidden={!show} className="action-suggestion">
      Do you want to go to the{" "}
      <Link className="go-to-registry-page" to="/register">
        register
      </Link>{" "}
      page?
    </div>
  );
};
function ForgotPassword({
  changePath,
  goBack,
  currentState,
  csrfToken,
  forgotValues,
  updateForgotFormValues,
}) {
  const navigateTo = useNavigate();
  const [initialValues] = useState({
    email: forgotValues.email,
  });
  const [recoveryInitialValues] = useState({
    recoveryToken: forgotValues.recoveryToken,
    password: forgotValues.password,
    repassword: forgotValues.repassword,
  });
  const [succesfullySubmitted, setSuccessfullySubmitted] = useState(false);

  const [initialErrors] = useState(forgotValues.errors1);
  const [recoveryInitialErrors] = useState(forgotValues.errors2);

  const formik = useFormik({
    validationSchema,
    initialValues,
    initialErrors,
    onSubmit: async ({ email }, { setSubmitting, setErrors }) => {
      try {
        const result = await fetch("/api/user/request-recover-password", {
          headers: {
            "CSRF-TOKEN": csrfToken,
            "Content-Type": "Application/json",
          },
          credentials: "include",
          method: "POST",
          body: JSON.stringify({
            email,
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
        }
        if (responseType == "SUCCESS") {
          setSuccessfullySubmitted(true);
        }
        console.log(responseType);
      } catch (e) {
      } finally {
        setSubmitting(false);
      }
    },
  });
  const recoveryFormik = useFormik({
    validationSchema: recoveryValidationSchema,
    initialValues: recoveryInitialValues,
    initialErrors: recoveryInitialErrors,
    onSubmit: () => alert("hello"),
  });
  return (
    <>
      <Form className="password-recovery-form" onSubmit={formik.handleSubmit}>
        <Form.Group className="mb-3" controlId="formEmail">
          <Button className="go-back" onClick={goBack}>
            go back
          </Button>
          <Form.Label>Forgot your password?</Form.Label>
          <Form.Control
            disabled={formik.isSubmitting || succesfullySubmitted}
            name="email"
            type="email"
            placeholder="Enter email"
            onChange={(event) => {
              updateForgotFormValues(currentState, {
                email: event.target.value,
                recoveryToken: forgotValues.recoveryToken,
                password: forgotValues.password,
                repassword: forgotValues.repassword,
                errors: forgotValues.errors1,
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
              <GoToRegistryQuestion {...{ formik, initialErrors }} />
            </Form.Control.Feedback>
          )}
        </Form.Group>
        <Button
          style={{ width: "20vmin" }}
          variant="primary"
          type="submit"
          className="login"
          disabled={
            !formik.isValid || formik.isSubmitting || succesfullySubmitted
          }
        >
          {formik.isSubmitting
            ? "Submitting"
            : succesfullySubmitted
            ? "Submitted"
            : "Submit"}
        </Button>
      </Form>
      {succesfullySubmitted && (
        <Form
          className="password-recovery-form-part2"
          onSubmit={recoveryFormik.handleSubmit}
        >
          <Form.Group className="mb-3" controlId="formRecovery">
            <Form.Label>Recover your account.</Form.Label>
            <Form.Control
              placeholder="recovery token here"
              style={{ textAlign: "center" }}
              name="recoveryToken"
              onChange={(event) => {
                if (event.target.value.length <= 8) {
                  updateForgotFormValues(currentState, {
                    email: forgotValues.email,
                    recoveryToken: event.target.value,
                    password: forgotValues.password,
                    repassword: forgotValues.repassword,
                    errors: forgotValues.errors2,
                  });
                  recoveryFormik.handleChange(event);
                }
              }}
              onBlur={recoveryFormik.handleBlur}
              value={recoveryFormik.values.recoveryToken}
            />
            <Form.Label>new password:</Form.Label>
            <Form.Control
              disabled={
                recoveryFormik.isSubmitting ||
                recoveryFormik.values.recoveryToken.length != 8
              }
              name="password"
              type="password"
              placeholder="Password"
              onChange={(event) => {
                updateForgotFormValues(currentState, {
                  email: forgotValues.email,
                  recoveryToken: forgotValues.recoveryToken,
                  password: event.target.value,
                  repassword: forgotValues.repassword,
                  errors: forgotValues.errors2,
                });
                recoveryFormik.handleChange(event);
              }}
              onBlur={recoveryFormik.handleBlur}
              value={recoveryFormik.values.password}
              isValid={
                !recoveryFormik.errors.password &&
                recoveryFormik.values.password !== ""
              }
              isInvalid={
                recoveryFormik.touched.password &&
                recoveryFormik.errors.password
              }
              feedback={recoveryFormik.errors.password}
            />
            <div
              visible={recoveryFormik.errors.password ? true : false}
              className="feedback-invalid"
            >
              {recoveryFormik.errors.password}
            </div>
            <Form.Label>match new password:</Form.Label>
            <Form.Control
              disabled={
                recoveryFormik.isSubmitting ||
                recoveryFormik.values.recoveryToken.length != 8
              }
              name="repassword"
              type="password"
              placeholder="Retype Password"
              onChange={(event) => {
                updateForgotFormValues(currentState, {
                  email: forgotValues.email,
                  recoveryToken: forgotValues.recoveryToken,
                  password: forgotValues.password,
                  repassword: event.target.value,
                  errors: forgotValues.errors2,
                });
                recoveryFormik.handleChange(event);
              }}
              onBlur={recoveryFormik.handleBlur}
              value={recoveryFormik.values.repassword}
              isValid={
                !recoveryFormik.errors.repassword &&
                recoveryFormik.values.repassword !== ""
              }
              isInvalid={
                recoveryFormik.touched.repassword &&
                recoveryFormik.errors.repassword
              }
              feedback={recoveryFormik.errors.repassword}
            />
            <div className="feedback-invalid">
              {recoveryFormik.errors.repassword}
            </div>
            <Button
              variant="primary"
              type="submit"
              disabled={
                recoveryFormik.values.recoveryToken.length != 8 ||
                recoveryFormik.values.password !=
                  recoveryFormik.values.repassword
              }
              style={{
                marginLeft: ".1vmin",
                marginTop: "0.9vmin",
                marginBottom: "-5.5vmin",
                width: "15vmin",
              }}
            >
              Recover
            </Button>
          </Form.Group>
        </Form>
      )}
    </>
  );
}

export default connect(mapStateToProps, mapDispatchToProps)(ForgotPassword);
