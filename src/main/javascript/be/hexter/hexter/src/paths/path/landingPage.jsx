import React from "react";
import { connect } from "react-redux";
import useCheckAuthenticationToken from "../../hooks/useCheckAuthenticationToken";

const mapStateToProps = ({ state }) => ({
  csrfToken: state.cookie.csrfToken,
  fingerprint: state.fingerprint,
  authenticationToken: state.cookie["authentication-token"],
});

const mapDispatchToProps = (dispatch) => ({});

function LandingPage({ csrfToken, fingerprint, authenticationToken }) {
  useCheckAuthenticationToken(csrfToken, fingerprint, authenticationToken);
  return <>landing page</>;
}

export default connect(mapStateToProps, mapDispatchToProps)(LandingPage);
