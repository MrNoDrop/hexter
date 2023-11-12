import { startListener } from "redux-first-routing";
import { browserHistory } from "../../configuration";

const historyListener = (store) => {
  startListener(browserHistory, store);
};

export default historyListener;
