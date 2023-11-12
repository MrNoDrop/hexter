import bindListeners from "./listeners";
import configuration from "./configuration";
import { createStore } from "redux";

const [reducers, enhancers] = configuration;

const store = createStore(reducers, enhancers);

bindListeners(store);

export default store;
