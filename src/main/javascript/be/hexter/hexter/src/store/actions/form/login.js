//update tick
import { STORE_FORM_LOGIN } from "../types";

const loginFormAction = (
  currentState,
  login = {
    email: "",
    password: "",
  }
) => ({
  type: STORE_FORM_LOGIN,
  payload: {
    form: {
      ...currentState.form,
      login: { ...currentState.form.login, ...login },
    },
  },
});

export default loginFormAction;
