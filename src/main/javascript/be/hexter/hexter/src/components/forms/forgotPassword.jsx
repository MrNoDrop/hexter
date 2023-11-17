import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import httpStatus from "http-status";
import { connect } from "react-redux";
import Form from "react-bootstrap/Form";
import { useFormik } from "formik";
import { Link, useNavigate } from "react-router-dom";
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
  const [recoveryToken, setRecoveryToken] = useState(undefined);
  const navigateTo = useNavigate();
  const [initialValues] = useState({
    email: forgotValues.email,
  });
  const [succesfullySubmitted, setSuccessfullySubmitted] = useState(false);
  const [initialErrors] = useState(forgotValues.errors);
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
                errors: forgotValues.errors,
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
        <Form className="password-recovery-form-part2">
          <Form.Group className="mb-3" controlId="formRecovery">
            <Form.Label>Recover your account.</Form.Label>
            <Form.Control
              placeholder="recovery token here"
              value={recoveryToken}
              style={{ textAlign: "center" }}
              onChange={(event) => {
                //continue from here.
              }}
            />
            <Form.Label>new password:</Form.Label>
            <Form.Control placeholder="password" />
            <Form.Label>match new password:</Form.Label>
            <Form.Control placeholder="retype password" />
            <Button
              style={{
                marginLeft: ".1vmin",
                marginTop: "0.3vmin",
                width: "15vmin",
              }}
              onClick={(e) => {
                changePath("/recover-password");
                navigateTo("/recover-password");
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
