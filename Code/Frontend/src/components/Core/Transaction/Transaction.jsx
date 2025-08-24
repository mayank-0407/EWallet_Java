import {
  FaArrowDown,
  FaMoneyCheckAlt,
  FaSpotify,
  FaWallet,
} from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import TransactionDetail from "./TransactionDetail";
import { getCookie } from "../../../services/cookieService";
import { useState, useEffect } from "react";
import { getWithToken } from "../../../services/operations/axiosGetRequest";
import ProfileIconIntitialGenerator from "../../../utils/profileIconIntitialGenerator";

function Transaction({ tx }) {
  const navigate = useNavigate();
  const currentWalletId = getCookie("walletId");
  const [fullName, setFullName] = useState("");

  const type =
    tx.transaction.receiverWalletId === currentWalletId ? "credit" : "debit";

  const formattedDate = new Date(
    tx.transaction.transactionInitTime
  ).toLocaleString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });

  useEffect(() => {
    const fetchOtherUsersDetails = async () => {
      if (type === "credit") {
        setFullName(tx.senderName);
      } else {
        setFullName(tx.receiverName);
      }
    };
    if (tx.transaction.remarks === "Added Money To Wallet")
      setFullName("Default Wallet");
    else fetchOtherUsersDetails();
  }, []);

  return (
    <li
      key={tx.transaction.id}
      className="flex items-center justify-between p-2 shadow-md rounded-lg cursor-pointer"
      onClick={() =>
        navigate("/transactiondetail", {
          state: { ...tx.transaction, source: "child-component" },
        })
      }
    >
      <div className="flex items-center space-x-3">
        <div className="bg-gray-100 rounded-full">
          <ProfileIconIntitialGenerator size={32} name={fullName} />
        </div>
        <div className="flex flex-col items-start justify-center">
          <span className="text-gray-800 font-bold">{fullName}</span>
          {/* <span className="text-gray-800 font-bold">{tx.receiverWalletId}</span> */}
          <p className="text-[12px]">{formattedDate}</p>
          {tx.transaction.remarks != "" ? (
            <p className="text-[12px] rounded bg-amber-400 p-1 ">
              {tx.transaction.remarks}
            </p>
          ) : (
            <p></p>
          )}
        </div>
      </div>
      <div className="flex flex-col items-end justify-center">
        <span
          className={`font-medium ${
            type === "debit" ? "text-red-500" : "text-green-500"
          }`}
        >
          {tx.transaction.status === "REFUNDED" || tx.transaction.status === "INSUFFICIENT_BALANCE" 
            ? ""
            : type === "debit"
            ? "-"
            : "+"}
          ${tx.transaction.amount}
        </span>
        <p
          className={`rounded-xl p-1 text-sm ${
            tx.transaction.status === "COMPLETED"
              ? "text-green-400 bg-green-200"
              : tx.transaction.status === "FAILED"
              ? "text-red-400 bg-red-200"
              : tx.transaction.status === "PENDING" ||
                tx.transaction.status === "PROCESSING"
              ? "text-yellow-400 bg-yellow-200"
              // : tx.transaction.status === "REFUNDED"
              // ? "text-green-400 bg-green-200"
              : "text-red-400 bg-red-200"
          }`}
        >
          {tx.transaction.status === "COMPLETED"
            ? "Completed"
            : tx.transaction.status === "PROCESSING" ||
              tx.transaction.status === "PENDING"
            ? "Processing"
            : tx.transaction.status === "REFUNDED"
            ? "Refunded"
            : tx.transaction.status === "INSUFFICIENT_BALANCE"
            ? "Insufficient Balance"
            : "Failed"}
        </p>
      </div>
    </li>
  );
}
export default Transaction;
