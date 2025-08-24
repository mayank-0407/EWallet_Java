import React, { useEffect, useState } from "react";
import {
  FaArrowDown,
  FaMoneyCheckAlt,
  FaSpotify,
  FaWallet,
} from "react-icons/fa";
import Transaction from "../Core/Transaction/Transaction";
import { getCookie } from "../../services/cookieService";
import { getWithToken } from "../../services/operations/axiosGetRequest";

const DashTransactions = () => {
  const [transactions, setTransactions] = useState([]);
  useEffect(() => {
    const fetchTransaction = async () => {
      try {
        const walletId = getCookie("walletId");

        const response = await getWithToken(
          `api/transaction/get/five/recent/transactions/${walletId}`
        );
        if (response.status === 200) setTransactions(response.data);
        else console.log("No transaction Found");
      } catch (error) {
        // console.log("Unable to fetch transactions or No transaction");
        setTransactions([]);
      }
    };
    fetchTransaction();

    // const interval = setInterval(fetchTransaction, 5000);

    // return () => clearInterval(interval);
  }, []);

  return (
    <div className="bg-white p-4 rounded-xl min-h-[42vh] shadow-md ml-2 mr-2">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-lg font-semibold">Transactions</h2>
        {transactions.length != 0 && (
          <a
            href="/transactions"
            className="text-sm text-blue-600 hover:underline"
          >
            View All
          </a>
        )}
      </div>
      <ul className="space-y-1 ">
        {/* {transactions.map((tx) => (
          <Transaction key={tx.id} tx={tx} />
        ))} */}
        {(transactions.length != 0 &&
          transactions.map((tx) => (
            <Transaction key={tx.transaction.id} tx={tx} />
          ))) || (
          <>
            <div className="flex justify-center items-center">
              <p>No Transactions Yet</p>
            </div>
          </>
        )}
      </ul>
    </div>
  );
};
export default DashTransactions;
