import React from "react";
import { connect } from "react-redux";
import { Route, Routes } from "react-router-dom";
import LandingPage from "./path/landingPage";
import LoginPage from "./path/loginPage";
import RegisterPage from "./path/registerPage";
import Redirect from "../components/redirect";
import PasswordRecovery from "../components/forms/passwordRecovery";

const mapStateToProps = ({ state }) => ({
  authenticationToken: state.cookie["authentication-token"],
});

function Paths({ authenticationToken }) {
  return (
    <>
      <Routes>
        {authenticationToken && (
          <>
            <Route path="/" exact element={<LandingPage />} />
            <Route path="/:name" exact element={<Redirect path="/" />} />
          </>
        )}
        ||
        {!authenticationToken && (
          <>
            {["/", "/login"].map((path, key) => (
              <Route {...{ path, key }} exact element={<LoginPage />} />
            ))}
            <Route path="/register" exact element={<RegisterPage />} />
            <Route
              path="/forgot-password"
              exact
              element={<PasswordRecoveryPage />}
            />
            <Route path="/:name" exact element={<Redirect path="/login" />} />
          </>
        )}
      </Routes>
    </>
  );
}

export default connect(mapStateToProps)(Paths);
