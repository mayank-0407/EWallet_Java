import React, { useState, useEffect } from "react";
import axios from "axios";
import { FaArrowRight, FaCopy, FaEye, FaEyeSlash } from "react-icons/fa";
import formatBalance from "../../utils/formatBalance";
import { getWithToken } from "../../services/operations/axiosGetRequest";
import { getUserDetailsFromToken } from "../../services/apis";
import { setCookie } from "../../services/cookieService";
import { toast } from "react-hot-toast";
const BalanceCard = () => {
  const [balance, setBalance] = useState(0.0);
  const [walletId, setWalletId] = useState("");
  const [visible, setVisible] = useState(false);
  useEffect(() => {
    const fetchBalance = async () => {
      try {
        const Data = await getUserDetailsFromToken();
        setCookie("userId", Data.data.id);
        setCookie("walletId", Data.data.walletId);
        setCookie("userFullName", Data.data.name);
        setCookie("phoneNumber", Data.data.phoneNumber);
        if (Data.data.businessDetails != null)
          setCookie("businessId", Data.data.businessDetails.id);
        else setCookie("businessId", null);
        setCookie("isMerchant", Data.data.merchant);
        setWalletId(Data.data.walletId);

        const res = await getWithToken(`api/wallet/balance/${Data.data.id}`);

        if (res.status === 200) {
          setBalance(res.data);
        } else {
          throw new Error("No balance data found");
        }
      } catch (err) {
        console.error("Error occurred: ", err);
      }
    };

    fetchBalance();
  }, []);
  const toggleVisibility = () => {
    setVisible(!visible);
  };
  const copyToClipboard = () => {
    navigator.clipboard
      .writeText(walletId)
      .then(() => {
        toast.success("Copied to clipboard ");
      })
      .catch((error) => {
        toast.error("Failed to copy text");
      });
  };
  return (
    <div className="bg-[#1e4d93] text-white flex flex-col rounded-l-2xl rounded-r-md p-6 shadow-lg mb-2 mt-0 ml-2 mr-1 w-3/5">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-lg font-semibold">Current Balance</h2>
        <button
          onClick={toggleVisibility}
          className="text-lg text-white px-3 py-1 rounded hover:text-xl transition cursor-pointer"
        >
          {visible ? <FaEye /> : <FaEyeSlash />}
        </button>
      </div>
      <div className="text-3xl font-bold">
        {visible ? formatBalance(balance) : "********"}
      </div>
      <div className="text-base mt-4  py-1 flex flex-row justify-start items-center">
        <div>Wallet ID: {walletId}</div>
        <div className="ml-2">
          <FaCopy className="hover:text-lg" onClick={() => copyToClipboard()} />
        </div>
      </div>
    </div>
  );
};
export default BalanceCard;
