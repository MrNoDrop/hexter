//update tick
import { STORE_COOKIES } from "../types";

const windowCookiesAction = (cookies = {}) => ({
  type: STORE_COOKIES,
  payload: {
    cookie: { ...cookies },
  },
});

export default windowCookiesAction;
