import { GO, GO_BACK, GO_FORWARD, PUSH, REPLACE } from "redux-first-routing";

// eslint-disable-next-line consistent-return
const routerMiddleware = (history) => () => (next) => (action) => {
  switch (action.type) {
    case PUSH:
      // as of from now on. React uses useNavigate hook and this push creates duplicate entries.
      // history.push(action.payload);
      break;
    case REPLACE:
      history.replace(action.payload);
      break;
    case GO:
      history.go(action.payload);
      break;
    case GO_BACK:
      history.goBack();
      break;
    case GO_FORWARD:
      history.goForward();
      break;
    default:
      return next(action);
  }
};
export default routerMiddleware;
