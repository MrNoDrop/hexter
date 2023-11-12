import React from "react";
import { ListGroup } from "react-bootstrap";
import { connect } from "react-redux";
import "./style.scss";

const mapStateToProps = ({ state: { "my-ip-data": myIpData } }) => ({
  myIpData,
});

function ClientIpData_clientIp({ myIpData }) {
  console.log(myIpData?.["ip address"]);
  return (
    <ListGroup className="client-ip-data-client-ip">
      <ListGroup.Item>
        Your ip address is:{" "}
        {myIpData?.["ip address"] === null
          ? "not found."
          : myIpData?.["ip address"] === undefined
          ? "loading..."
          : myIpData?.["ip address"] +
            (myIpData?.["ip address version"]
              ? " (" + myIpData?.["ip address version"] + ")"
              : "")}
      </ListGroup.Item>
    </ListGroup>
  );
}

export default connect(mapStateToProps)(ClientIpData_clientIp);
