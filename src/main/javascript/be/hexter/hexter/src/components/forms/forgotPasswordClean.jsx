import { useFormik } from "formik";
import React, { useState } from "react";
import { connect } from "react-redux";

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

function ForgotPassword({ forgotValues }) {
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
  });
  return null;
}

export default connect(mapStateToProps)(ForgotPassword);
