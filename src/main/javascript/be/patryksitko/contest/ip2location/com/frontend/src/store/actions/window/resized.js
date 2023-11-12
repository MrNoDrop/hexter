import { WINDOW_RESIZED } from "../types";

const windowResizedAction = (currentState, inner, outer) => ({
  type: WINDOW_RESIZED,
  payload: { window: { ...currentState.window, inner, outer } },
});
export default windowResizedAction;
