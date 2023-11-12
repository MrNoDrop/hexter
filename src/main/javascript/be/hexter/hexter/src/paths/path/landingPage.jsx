import React from "react";
import { connect } from "react-redux";
import {
  ClientDataset1,
  ClientDataset2,
  ClientIp,
  ClientLocationMapping,
  storeClientIpDataAction,
} from "../../components/client-ip-data";
import useCheckAuthenticationToken from "../../hooks/useCheckAuthenticationToken";
import useFetchClientIpData from "../../hooks/useFetchClientIpData";

const mapStateToProps = ({ state }) => ({
  myIpData: state["my-ip-data"],
  csrfToken: state.cookie.csrfToken,
  fingerprint: state.fingerprint,
  authenticationToken: state.cookie["authentication-token"],
});

const mapDispatchToProps = (dispatch) => ({
  storeClientIpData: (myIpData) => dispatch(storeClientIpDataAction(myIpData)),
});

function LandingPage({
  myIpData,
  csrfToken,
  fingerprint,
  authenticationToken,
  storeClientIpData,
}) {
  useCheckAuthenticationToken(csrfToken, fingerprint, authenticationToken);
  useFetchClientIpData(myIpData, storeClientIpData);
  return (
    <>
      <ClientIp />
      <ClientDataset1 />
      <ClientLocationMapping />
      <ClientDataset2 />
    </>
  );
}

export default connect(mapStateToProps, mapDispatchToProps)(LandingPage);
