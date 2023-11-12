import * as yup from "yup";

yup.addMethod(yup.string, "strongPassword", strongPasswordMethod);

function strongPasswordMethod() {
  return this.test("strongPasswordTest", null, function (value) {
    const { path, createError } = this;
    switch (Boolean(value)) {
      case !/^.{8,}$/.test(value):
        return createError({
          path,
          message: "Password must contain atleast 8 characters.",
        });
      case !/^(?=.*[a-z])/.test(value):
        return createError({
          path,
          message: "Password must include lowercase letter.",
        });
      case !/^(?=.*[A-Z])/.test(value):
        return createError({
          path,
          message: "Password must include uppercase letter.",
        });
      case !/^(?=.*[0-9])/.test(value):
        return createError({ path, message: "Password must include digit." });
      case !/^(?=.*[!@#$%^&*])/.test(value):
        return createError({
          path,
          message: "Password must include special character.",
        });
      default:
        return true;
    }
  });
}
