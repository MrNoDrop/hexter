//update tick
import { UPDATE_TICK } from "./types";

const updateTickAction = (count) => ({
  type: UPDATE_TICK,
  payload: {
    updateTick: count,
  },
});

export default updateTickAction;
