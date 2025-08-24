import Navbar from "../../Common/Navbar";
import { FaHome, FaUserCircle } from "react-icons/fa";
import { FaArrowLeft } from "react-icons/fa6";
import { useLocation, useNavigate } from "react-router-dom";
import { getUserDetailsFromToken } from "../../../services/apis";
import { getWithToken } from "../../../services/operations/axiosGetRequest";
import { useEffect, useState } from "react";

function ProcessingTransaction() {
  // const location = useLocation();
  const navigate = useNavigate();

  const [initialTransactions, setTransaction] = useState({
    transactionId: 1,
    receiver: "",
    type: "debit",
    amount: 12.33,
    transactionInitTime: "",
    status: "Success",
    remarks: "",
    icon: "spotify",
    duration: "1s",
    walletId: "default@wallet",
  });
  const isCredit = initialTransactions.type === "credit";
  const isProcessing = initialTransactions.status === "Processing";
  const isFailed = initialTransactions.status === "Failed";
  const getTitle = () => {
    if (isProcessing) return "Transaction Processing";
    if (isFailed) return "Transaction Failed";
    return isCredit ? "Amount Credited" : "Amount Debited";
  };

  useEffect(() => {
    const getTransactionData = async () => {
      try {
        const Data = await getUserDetailsFromToken();
        const thiswalletId = Data.data.walletId;

        const res = await getWithToken(
          `api/transaction/get/one/recent/transaction/by/sender/${thiswalletId}`
        );
        const receiverNameDetailsResponse = await getWithToken(
          `api/auth/get/userdetails/from/walletid/${res.data.receiverWalletId}`
        );

        const transactionInitTime = res.data.transactionInitTime;
        const transactionCompleteTime = res.data.transactionCompletedTime;

        // Check if both times are available and parse them to Date objects
        if (transactionInitTime && transactionCompleteTime) {
          const initTime = new Date(transactionInitTime);
          const completeTime = new Date(transactionCompleteTime);

          const initDateFormatted = initTime.toLocaleDateString("en-CA");
          // Calculate the difference in seconds
          const timeDifferenceInSeconds = Math.floor(
            (completeTime - initTime) / 1000
          );
          setTransaction((prev) => ({
            ...prev,
            transactionInitTime: initDateFormatted,
            duration: timeDifferenceInSeconds + "s",
          }));
        }
        setTransaction((prevTransaction) => ({
          ...prevTransaction,
          transactionId: res.data.id || prevTransaction.transactionId,
          receiver:
            receiverNameDetailsResponse.data.name || prevTransaction.receiver,
          type: prevTransaction.type,
          amount: res.data.amount || prevTransaction.amount,
          transactionInitTime: prevTransaction.transactionInitTime,
          status:
            res.data.status === "COMPLETED"
              ? "Success"
              : res.data.status === "PROCESSING"
              ? "Processing"
              : "Failed" || prevTransaction.status,
          remarks: res.data.remarks || prevTransaction.remarks,
          icon: res.data.icon || prevTransaction.icon,
          duration: prevTransaction.duration,
          walletId: res.data.receiverWalletId || prevTransaction.walletId,
        }));
      } catch (e) {
        console.log(e);
      }
    };
    getTransactionData();
    // console.log("!! : ", initialTransactions.status);
    setTimeout(() => {
      if (initialTransactions.status === "PROCESSING") {
        getTransactionData();
      }
    }, 10000);
    // console.log("!! 1 : ", initialTransactions.status);
  }, [initialTransactions.status]);
  // We need to add the controller in this component in order to fetch the details of the transaction using transactionID.

  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute bg-gray-300 rounded-lg shadow-lg z-10 flex flex-col">
        <Navbar />
        <div className="bg-white p-4 rounded-xl shadow-md ml-2 mr-2 mb-2 flex-grow overflow-y-auto">
          <div className="-translate-x-[50%] flex justify-center items-center ml-4">
            <FaHome
              className="text-2xl cursor-pointer text-gray-600"
              onClick={() => {
                navigate("/");
              }}
            />
          </div>
          <div className="max-w-md mx-auto bg-white shadow-md rounded-2xl p-6 text-center space-y-4">
            <div className="flex justify-center items-center">
              {initialTransactions.status === "Processing" ? (
                <img
                  src="src\assets\Transaction_Processing_status.png"
                  alt="Processing"
                  className="w-32 h-32 jump-animation-processing"
                />
              ) : initialTransactions.status === "Success" ? (
                <img
                  src="src\assets\Transaction_Success_status.png"
                  alt="Success"
                  className="w-32 h-32 jump-animation-success"
                />
              ) : (
                <img
                  src="src\assets\Transaction_Fail_status.png"
                  alt="Fail"
                  className="w-32 h-32 jump-animation-failed"
                />
              )}
            </div>
            <h2 className="text-xl font-bold">{getTitle()}</h2>
            <div className="text-left space-y-2">
              <div className="flex justify-between">
                <span className="text-gray-600">Amount</span>
                <span
                  className={`font-semibold ${
                    isCredit ? "text-green-600" : "text-red-600"
                  }`}
                >
                  {isCredit
                    ? `+ $ ${initialTransactions.amount}`
                    : `-$ ${initialTransactions.amount}`}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">
                  {isCredit ? "From" : "To"}
                </span>
                <span className="font-semibold">
                  {initialTransactions.receiver}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Wallet ID</span>
                <span className="font-semibold">
                  {initialTransactions.walletId}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Date</span>
                <span className="font-semibold">
                  {initialTransactions.transactionInitTime}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Transaction ID</span>
                <span className="font-semibold">
                  {initialTransactions.transactionId}
                </span>
              </div>
              {initialTransactions.remarks && (
                <div className="flex justify-between">
                  <span className="text-gray-600">Remarks</span>
                  <span className="font-semibold">
                    {initialTransactions.remarks}
                  </span>
                </div>
              )}
              {initialTransactions.status === "Success" &&
                initialTransactions.duration && (
                  <div className="flex justify-between">
                    <span className="text-gray-600">Completed in</span>
                    <span className="font-semibold">
                      {initialTransactions.duration}
                    </span>
                  </div>
                )}
            </div>
            {/* {!isProcessing && (
              <button className="w-full bg-gray-100 hover:bg-gray-200 text-gray-700 py-2 px-4 rounded-lg font-medium">
                Done
              </button>
            )} */}
          </div>
        </div>
      </div>
    </div>
  );
}
export default ProcessingTransaction;
