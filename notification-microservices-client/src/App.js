import "./App.css";
import Header from "./components/Header";
import MessageList from "./components/MessageList";
import EmailList from "./components/EmailList";
import {Routes, Route} from "react-router-dom";
import { HomeContent } from "./components/HomeContent";
import React from "react";

function App() {

  return (
    <>
      <Header />
      <Routes>
          <Route path="" element={<HomeContent/>}></Route>
          <Route exact path="/messages" element={<MessageList/>}></Route>
          <Route exact path="/emails" element={<EmailList/>}></Route>
      </Routes>
    </>
  );
}

export default App;
