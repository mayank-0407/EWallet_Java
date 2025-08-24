import React, { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { getCookie } from "../../../services/cookieService";
import { postWithToken } from "../../../services/operations/axiosPostRequest";

const InitiateTransaction = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const { receiverWalletId, amount, remarks } = location.state || {};

  //   useEffect(() => {
  const initiateTheTransaction = async () => {
    try {
      const senderWalletId = getCookie("walletId");
      const reqBody = {
        senderId: senderWalletId,
        receiverId: receiverWalletId,
        amount: amount,
        remarks,
      };
      const response = await postWithToken("api/transaction/initiate", reqBody);

      if (response.status === 200) {
        navigate("/processingtransaction");
      } else {
        navigate("/");
      }
    } catch (e) {
      navigate("/");
    }
  };

  initiateTheTransaction();
  //   }, []);
  return <div>Initiating Transaction</div>;
};

export default InitiateTransaction;
