import React, { useEffect, useState } from "react";
import { ListGroup } from "react-bootstrap";
import "./ipData.scss";

function IpData() {
  const [data, setData] = useState({});
  useEffect(() => {
    fetch("/api/client/get-ip-data")
      .then((res) => res.json())
      .then((data) => setData(JSON.parse(data.body)));
  }, []);
  return (
    <div>
      {data && (
        <>
          <input type="text" />
          <ListGroup className="ip-data">
            <ListGroup.Item>ip address: {data["ip address"]}</ListGroup.Item>
            <ListGroup.Item>
              address type: {data["address type"]}
            </ListGroup.Item>
            <ListGroup.Item>
              coordinates:
              <ListGroup.Item>
                latitude: {data.coordinates?.latitude}
              </ListGroup.Item>
              <ListGroup.Item>
                longitude: {data.coordinates?.longitude}
              </ListGroup.Item>
              <ListGroup.Item>
                elevation: {data.coordinates?.elevation}
              </ListGroup.Item>
            </ListGroup.Item>
            <ListGroup.Item>area code: {data["area code"]}</ListGroup.Item>
            <ListGroup.Item>category: {data.category}</ListGroup.Item>
            <ListGroup.Item>city: {data.city}</ListGroup.Item>
            <ListGroup.Item>
              country long: {data["country long"]}
            </ListGroup.Item>
          </ListGroup>
          <ListGroup className="ip-data">
            <ListGroup.Item>
              country short: {data["country short"]}
            </ListGroup.Item>
            <ListGroup.Item>domain: {data.domain}</ListGroup.Item>
            <ListGroup.Item>IDD code: {data["IDD code"]}</ListGroup.Item>
            <ListGroup.Item>ISP: {data.ISP}</ListGroup.Item>
            <ListGroup.Item>MCC: {data.MCC}</ListGroup.Item>
            <ListGroup.Item>MNC: {data.MNC}</ListGroup.Item>
            <ListGroup.Item>mobile band: {data["mobile band"]}</ListGroup.Item>
          </ListGroup>
          <ListGroup className="ip-data">
            <ListGroup.Item>
              network speed: {data["network speed"]}
            </ListGroup.Item>
            <ListGroup.Item>region: {data.region}</ListGroup.Item>
            <ListGroup.Item>status: {data.status}</ListGroup.Item>
            <ListGroup.Item>time zone: {data["time zone"]}</ListGroup.Item>
            <ListGroup.Item>usage type: {data["usage type"]}</ListGroup.Item>
            <ListGroup.Item>
              weather station code: {data["weather station code"]}
            </ListGroup.Item>
            <ListGroup.Item>
              weather station name: {data["weather station name"]}
            </ListGroup.Item>
            <ListGroup.Item>zip code: {data["zip code"]}</ListGroup.Item>
            <ListGroup.Item>version: {data.version}</ListGroup.Item>
          </ListGroup>
        </>
      )}
    </div>
  );
}

export default IpData;
