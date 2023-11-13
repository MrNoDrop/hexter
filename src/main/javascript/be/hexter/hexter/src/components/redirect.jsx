import React, { useEffect } from "react";
import { connect } from "react-redux";
import { push } from "redux-first-routing";
import { useNavigate } from "react-router-dom";

const mapStateToProps = ({ state }) => ({
  updateTick: state.updateTick,
});
const mapDispatchToProps = (dispatch) => ({
  changePath: (path) => dispatch(push(path)),
});

function Redirect({ updateTick, path, changePath }) {
  const navigateTo = useNavigate();
  useEffect(() => {
    changePath(path);
  }, [updateTick, changePath, path]);
  navigateTo(path);

  return <>redirecting..</>;
}

export default connect(mapStateToProps, mapDispatchToProps)(Redirect);
