import updateTick from "../../actions/updateTick";

const updateTickListener = ({ getState, dispatch }) => {
  const {
    state: { updateTick: updateTickCount },
  } = getState();
  if (updateTickCount === 10) {
    dispatch(updateTick(0));
  } else {
    dispatch(updateTick(updateTickCount + 1));
  }
};

export default updateTickListener;
