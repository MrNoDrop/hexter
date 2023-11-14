//update tick
import { STORE_FORM_FORGOT } from "../types";

const forgotFormAction = (
  currentState,
  forgot = {
    email: "",
  }
) => ({
  type: STORE_FORM_FORGOT,
  payload: {
    form: {
      ...currentState.form,
      forgot: { ...currentState.form.forgot, ...forgot },
    },
  },
});

export default forgotFormAction;
