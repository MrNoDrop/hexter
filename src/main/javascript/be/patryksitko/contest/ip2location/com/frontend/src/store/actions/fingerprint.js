//update tick
import { STORE_FINGERPRINT } from "./types";

const storeFingerprintAction = (fingerprint) => ({
  type: STORE_FINGERPRINT,
  payload: {
    fingerprint,
  },
});

export default storeFingerprintAction;
