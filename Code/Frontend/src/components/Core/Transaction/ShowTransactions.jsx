import React, { useEffect, useState } from "react";
import Navbar from "../../Common/Navbar";
import TransactionFilter from "./TransactionFilter"; // Assuming TransactionFilter is in the same directory
import {
  FaArrowDown,
  FaMoneyCheckAlt,
  FaSpotify,
  FaWallet,
} from "react-icons/fa";
import Transaction from "./Transaction";
import { getCookie } from "../../../services/cookieService";
import { getWithToken } from "../../../services/operations/axiosGetRequest";

const ShowTransactions = () => {
  const [transactions, setTransactions] = useState([]);
  const [filteredTransactions, setFilteredTransactions] =
    useState(transactions);

  const handleFilter = (filters) => {
    let filteredData = [...transactions];

    if (filters.date) {
      filteredData = filteredData.filter((transaction) =>
        transaction.date.startsWith(filters.date)
      );
    }

    if (filters.status && filters.status !== "All") {
      filteredData = filteredData.filter(
        (transaction) => transaction.status === filters.status
      );
    }

    if (filters.amount) {
      filteredData = filteredData.filter((transaction) =>
        String(transaction.amount).includes(filters.amount)
      );
    }

    if (filters.type && filters.type !== "All") {
      filteredData = filteredData.filter(
        (transaction) => transaction.type === filters.type
      );
    }

    setFilteredTransactions(filteredData);
  };

  useEffect(() => {
    const fetchTransaction = async () => {
      try {
        const walletId = getCookie("walletId");

        const response = await getWithToken(
          `api/transaction/get/all/transaction/${walletId}`
        );
        if (response.status === 200) {
          setTransactions(response.data);
          setFilteredTransactions(response.data);
        } else console.log("No transaction Found");
      } catch (error) {
        console.log("Unable to fetch transactions or No transaction");
      }
    };
    fetchTransaction();
  }, []);

  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute bg-gray-300 rounded-lg shadow-lg z-10 flex flex-col">
        <Navbar />
        <div className="bg-white p-4 rounded-xl shadow-md ml-2 mr-2 mb-2 flex-grow overflow-y-auto">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold">Transactions</h2>
            {/* <TransactionFilter onFilter={handleFilter} /> */}
          </div>
          <ul className="space-y-1">
            {filteredTransactions.map((tx) => (
              <Transaction key={tx.transaction.id} tx={tx} />
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};
export default ShowTransactions;
