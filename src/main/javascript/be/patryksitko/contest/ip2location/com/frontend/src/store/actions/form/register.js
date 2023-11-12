//update tick
import { STORE_FORM_REGISTER } from "../types";

const registerFormAction = (
  currentState,
  register = {
    firstname: "",
    lastname: "",
    email: "",
    password: "",
    repassword: "",
    errors: {},
  }
) => ({
  type: STORE_FORM_REGISTER,
  payload: {
    form: {
      ...currentState.form,
      register: { ...currentState.form.register, ...register },
    },
  },
});

export default registerFormAction;
