import { applyMiddleware } from "redux";
import { createBrowserHistory } from "redux-first-routing";
import thunk from "redux-thunk";
import routerMiddleware from "./middleware/redux-first-routing/routerMiddleware";
import reducers from "./reducers";

export const browserHistory = (() => createBrowserHistory())();

export default (() => {
  const middlewares = applyMiddleware(routerMiddleware(browserHistory), thunk);
  return [reducers, middlewares];
})();
