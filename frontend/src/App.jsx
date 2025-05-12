import React from "react";
import { Route, Routes } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import Home from "./pages/Home";
import Login from "./pages/Login";
import EmailVerify from "./pages/EmailVerify";
import ResetPassword from "./pages/ResetPassword";

const App = () => {
  return (
    <div>
      <ToastContainer />
      <Routes>
        <Route path="/" element={<Home></Home>} />
        <Route path="/login" element={<Login></Login>} />
        <Route path="/email-verify" element={<EmailVerify></EmailVerify>} />
        <Route
          path="/reset-password"
          element={<ResetPassword></ResetPassword>}
        ></Route>
      </Routes>
    </div>
  );
};

export default App;
