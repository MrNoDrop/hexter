import httpStatus from "http-status";
import Cookies from "js-cookie";
import { useEffect } from "react";

function useCheckAuthenticationToken(
  csrfToken,
  fingerprint,
  authenticationToken
) {
  useEffect(() => {
    (async () => {
      if (!fingerprint) {
        return;
      }
      const result = await fetch("/api/user/validate-authentication-token", {
        headers: {
          "CSRF-TOKEN": csrfToken,
          "Content-Type": "Application/json",
        },
        method: "POST",
        body: JSON.stringify({
          authenticationToken,
          fingerprint: fingerprint.visitorId,
        }),
      });
      if (
        httpStatus[await result.status] !== httpStatus.INTERNAL_SERVER_ERROR
      ) {
        const { status, body: body_ } = await result.json();
        const { isProvidedAuthenticationTokenValid } = JSON.parse(body_);
        switch (httpStatus[status]) {
          default:
          case httpStatus.ACCEPTED:
            break;
          case httpStatus.UNPROCESSABLE_ENTITY:
          case httpStatus.NOT_ACCEPTABLE:
            if (!isProvidedAuthenticationTokenValid) {
              Cookies.remove("authentication-token");
            }
            break;
        }
      }
    })();
  }, [csrfToken, fingerprint, authenticationToken]);
}

export default useCheckAuthenticationToken;
