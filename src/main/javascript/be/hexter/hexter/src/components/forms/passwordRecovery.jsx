import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import { connect } from "react-redux";
import Form from "react-bootstrap/Form";
import { useFormik } from "formik";
import * as Yup from "yup";
import updateForgotFormValues from "../../store/actions/form/forgot";
import "./passwordRecovery.scss";

const validationSchema = Yup.object({
  email: Yup.string().email().required("Email is required."),
});
const mapStateToProps = ({ state }) => ({
  currentState: state,
  csrfToken: state.csrfToken,
  forgotValues: state.form.forgot,
  fingerprint: state.fingerprint,
});

const mapDispatchToProps = (dispatch) => ({
  updateForgotFormValues: (currentState, forgotValues) =>
    dispatch(updateForgotFormValues(currentState, forgotValues)),
});

function PasswordRecovery({
  currentState,
  csrfToken,
  forgotValues,
  fingerprint,
  updateForgotFormValues,
}) {
  // const navigateTo = useNavigate();
  const [initialValues] = useState({
    email: forgotValues.email,
  });
  const [initialErrors] = useState(forgotValues.errors);
  const formik = useFormik({
    validationSchema,
    initialValues,
    initialErrors,
    onSubmit: () => {},
  });
  return (
    <Form className="password-recovery-form">
      <Form.Group className="mb-3" controlId="formEmail">
        <Form.Label>Forgot your password?</Form.Label>
        <Form.Control
          disabled={formik.isSubmitting}
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
          </Form.Control.Feedback>
        )}
      </Form.Group>
      <Button
        variant="primary"
        type="submit"
        className="login"
        disabled={!formik.isValid || formik.isSubmitting}
      >
        Submit
      </Button>
    </Form>
  );
}

export default connect(mapStateToProps, mapDispatchToProps)(PasswordRecovery);
