import { useEffect, useState } from "react";

function useFetchClientIpData(myIpData, storeClientIpDataDispatchedAction) {
  const [fetching, setFetching] = useState(false);
  useEffect(() => {
    if (!myIpData && !fetching) {
      setFetching(true);
      (async () => {
        const myIpData = JSON.parse(
          await (
            await (await fetch("/api/client/get-ip-data")).json()
          ).body
        );
        storeClientIpDataDispatchedAction(myIpData);
        setFetching(false);
      })();
    }
  }, [fetching, myIpData, storeClientIpDataDispatchedAction]);
}

export default useFetchClientIpData;
