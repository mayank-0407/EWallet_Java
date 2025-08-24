import { useEffect, useState, React } from "react";
import Navbar from "../Common/Navbar";
import BalanceCard from "./BalanceCard";
import MoneyButtonCard from "./MoneyButtonCard";
import DashTransactions from "./DashTransactions";
import { isLogin } from "../../services/apis";

const Dashboard = () => {
  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute  bg-gray-300 rounded-lg shadow-lg z-10 flex flex-col">
        <Navbar />
        <div className="flex flex-row">
          <BalanceCard />
          <MoneyButtonCard />
        </div>
        <div className="overflow-y-auto">
          <DashTransactions />
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
