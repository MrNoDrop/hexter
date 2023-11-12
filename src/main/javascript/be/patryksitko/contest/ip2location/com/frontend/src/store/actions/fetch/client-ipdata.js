import { STORE_CLIENT_IP_DATA } from "../types";

const clientIpDataAction = (clientIpData) => {
  return {
    type: STORE_CLIENT_IP_DATA,
    payload: {
      "my-ip-data": { ...clientIpData },
    },
  };
};

export default clientIpDataAction;
