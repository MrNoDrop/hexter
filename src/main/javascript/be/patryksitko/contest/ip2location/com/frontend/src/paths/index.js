import React from "react";
import { connect } from "react-redux";
import { Route, Routes } from "react-router-dom";
import Mention from "../components/mention";
import Menu from "../components/menu";
import IpV4Lookup from "./path/ipV4Lookup";
import LandingPage from "./path/landingPage";
import LoginPage from "./path/loginPage";
import RegisterPage from "./path/registerPage";

const mapStateToProps = ({ state }) => ({
  authenticationToken: state.cookie["authentication-token"],
});

function Paths({ authenticationToken }) {
  return (
    <>
      {authenticationToken && <Menu />}
      <Routes>
        {(authenticationToken && (
          <>
            <Route path="/" exact element={<LandingPage />} />
            <Route path="/ipV4-lookup" exact element={<IpV4Lookup />} />
          </>
        )) || (
          <>
            {["/", "/login"].map((path, key) => (
              <Route {...{ path, key }} exact element={<LoginPage />} />
            ))}
            <Route path="/register" exact element={<RegisterPage />} />
          </>
        )}
      </Routes>
      <Mention />
    </>
  );
}

export default connect(mapStateToProps)(Paths);
