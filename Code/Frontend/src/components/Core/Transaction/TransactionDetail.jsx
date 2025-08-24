import Navbar from "../../Common/Navbar";
import { FaUserCircle } from "react-icons/fa";
import { FaArrowLeft } from "react-icons/fa6";
import { useLocation, useNavigate } from "react-router-dom";
import { getCookie } from "../../../services/cookieService";
import { useEffect, useState } from "react";
import { getWithToken } from "../../../services/operations/axiosGetRequest";

function TransactionDetail() {
  const location = useLocation();
  const navigate = useNavigate();
  const [fullName, setFullName] = useState("");

  const data = location.state;

  const thisWalletId = getCookie("walletId");
  var isCredit = false;
  if (data.receiverWalletId === thisWalletId) isCredit = true;
  const isProcessing = data.status === "PENDING";
  const isFailed = data.status === "FAILED";

  const getTitle = () => {
    if (isProcessing) return "Transaction Processing";
    if (isFailed) return "Transaction Failed";
    return isCredit ? "Amount Credited" : "Amount Debited";
  };

  const formattedDate = new Date(data.transactionInitTime).toLocaleString(
    "en-IN",
    {
      day: "2-digit",
      month: "short",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    }
  );

  useEffect(() => {
    const fetchOtherUsersDetails = async () => {
      var walletId = data.receiverWalletId;
      if (isCredit) walletId = data.senderWalletId;
      const otherUserDetails = await getWithToken(
        `api/auth/get/userdetails/from/walletid/${walletId}`
      );
      setFullName(otherUserDetails.data.name);
    };
    if (data.remarks != "Added Money To Wallet") fetchOtherUsersDetails();
    else setFullName("Default Wallet");
  }, []);

  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute bg-gray-300 rounded-lg shadow-lg z-10 flex flex-col">
        <Navbar />
        <div className="bg-white p-4 rounded-xl shadow-md ml-2 mr-2 mb-2 flex-grow overflow-y-auto">
          <div className="-translate-x-[50%] flex justify-center items-center ml-4">
            <FaArrowLeft
              className="text-2xl cursor-pointer text-gray-600"
              onClick={() => {
                navigate("/transactions");
              }}
            />
          </div>
          <div className="max-w-md mx-auto bg-white shadow-md rounded-2xl p-6 text-center space-y-4">
            <div className="flex justify-center items-center">
              {data.status === "PENDING" ? (
                <img
                  src="/Transaction_Processing_status.png"
                  alt="Processing"
                  className="w-32 h-32 jump-animation-processing"
                />
              ) : data.status === "COMPLETED" ? (
                <img
                  src="/Transaction_Success_status.png"
                  alt="Success"
                  className="w-32 h-32 jump-animation-success"
                />
              ) : (
                <img
                  src="/Transaction_Fail_status.png"
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
                  {isCredit ? `+ $ ${data.amount}` : `-$ ${data.amount}`}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">
                  {isCredit ? "From" : "To"}
                </span>
                <span className="font-semibold">{fullName}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Wallet ID</span>
                <span className="font-semibold">
                  {isCredit ? data.senderWalletId : data.receiverWalletId}
                </span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Date</span>
                <span className="font-semibold">{formattedDate}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-600">Transaction ID</span>
                <span className="font-semibold">{data.id}</span>
              </div>
              {data.remarks && (
                <div className="flex justify-between">
                  <span className="text-gray-600">Remarks</span>
                  <span className="font-semibold">{data.remarks}</span>
                </div>
              )}
              {data.status === "COMPLETED" && data.duration && (
                <div className="flex justify-between">
                  <span className="text-gray-600">Completed in</span>
                  <span className="font-semibold">{data.duration}</span>
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
export default TransactionDetail;
