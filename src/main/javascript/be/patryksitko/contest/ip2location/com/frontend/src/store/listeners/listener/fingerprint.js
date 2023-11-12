import FingerprintJS from "@fingerprintjs/fingerprintjs";
import storeFingerprint from "../../actions/fingerprint";

const fingerprintListener = async ({ dispatch }) => {
  const fp = await FingerprintJS.load();

  // The FingerprintJS agent is ready.
  // Get a visitor identifier when you'd like to.
  const fingerprint = await fp.get();

  // This is the visitor identifier:
  dispatch(storeFingerprint(fingerprint));
};

export default fingerprintListener;
