import ReactDOM from "react-dom";

export function getElementRect(ref) {
  if (!ref || !ref.current) {
    return undefined;
  } else {
    return ReactDOM.findDOMNode(ref.current).getBoundingClientRect();
  }
}

export default getElementRect;
