// import { useState } from 'react'
import "./App.css";
import RegistrationPage from "./components/Core/Authentication/RegistrationPage";
import LoginPage from "./components/Core/Authentication/LoginPage";
import Dashboard from "./components/Dashboard/Dashboard";
import VerifyLogin from "./components/Core/Authentication/VerifyLogin";
import ShowTransactions from "./components/Core/Transaction/ShowTransactions";
import SendMoney from "./components/Core/Transaction/SendMoney";
import AddMoneyToWallet from "./components/Core/Transaction/AddMoneyToWallet";
import SendMoneyDemo from "./components/Core/Transaction/SendMoneyDemo";
import TransactionPinWindow from "./components/Core/Transaction/TransactionPinWindow";
import SetupWalletPin from "./components/Core/Profile/SetupWalletPin";
import TransactionDetail from "./components/Core/Transaction/TransactionDetail";
import ProcessingTransaction from "./components/Core/Transaction/ProcessingTransaction";
import AddMoneyToWalletTransaction from "./components/Core/Transaction/AddMoneyToWalletTransaction";
import EditProfile from "./components/Core/Profile/EditProfile";
import ChangePassword from "./components/Core//Profile/ChangePassword";
import BusinessDetails from "./components/Core/Profile/BusinessDetails";
import Faq from "./components/Common/FAQ";
import ChangeWalletPin from "./components/Core/Profile/ChangeWalletPin";
// React Router
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Layout from "./Layout";
import InitiateTransaction from "./components/Core/Transaction/InitiateTransaction";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/signup" element={<RegistrationPage />} />
        <Route path="/login" element={<LoginPage />} />

        <Route element={<Layout />}>
          <Route path="/" element={<Dashboard />} />
          <Route path="/verifylogin" element={<VerifyLogin />} />
          <Route path="/changewalletpin" element={<ChangeWalletPin />} />
          <Route path="/transactions" element={<ShowTransactions />} />
          <Route path="/sendmoney" element={<SendMoney />} />
          <Route path="/addmoneytowallet" element={<AddMoneyToWallet />} />
          {/* <Route path="/sendmoneydemo" element={<SendMoneyDemo />} /> */}
          <Route path="/sendmoney/:walletId" element={<SendMoneyDemo />} />
          <Route
            path="/transactionpinwindow"
            element={<TransactionPinWindow />}
          />
          <Route path="/setupwalletpin" element={<SetupWalletPin />} />
          <Route path="/transactiondetail" element={<TransactionDetail />} />
          <Route path="/faq" element={<Faq />} />
          <Route
            path="/processingtransaction"
            element={<ProcessingTransaction />}
          />
          <Route
            path="/initiate/transaction"
            element={<InitiateTransaction />}
          />
          <Route
            path="/addmoneytoWallettransaction"
            element={<AddMoneyToWalletTransaction />}
          />
          <Route path="/editprofile" element={<EditProfile />} />
          <Route path="/changepassword" element={<ChangePassword />} />
          <Route path="/businessdetails" element={<BusinessDetails />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
export default App;
