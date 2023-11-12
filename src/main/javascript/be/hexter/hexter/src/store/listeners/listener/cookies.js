import storeWindowCookies from "../../actions/window/cookies";

const cookiesListener = ({ dispatch }) => {
  const cookies = {};
  document.cookie
    .split("; ")
    .map((cookie) => {
      const keyValuePair = cookie.split(/=(.*)/s);
      keyValuePair.pop();
      return keyValuePair;
    })
    .forEach(([key, value]) => (cookies[key] = value));
  dispatch(storeWindowCookies(cookies));
};

export default cookiesListener;
