import Navbar from "../../Common/Navbar";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getWithToken } from "../../../services/operations/axiosGetRequest";
import ProfileIconIntitialGenerator from "../../../utils/profileIconIntitialGenerator";
import { toast } from "react-hot-toast";
const SendMoneyDemo = () => {
  const [showRemarks, setShowRemarks] = useState(false);
  const [amount, setAmount] = useState("");
  const [remarks, setRemarks] = useState("");
  const navigate = useNavigate(); // Assuming you might want to navigate after sending

  const [userData, setUserData] = useState({
    email: "",
    id: null,
    merchant: null,
    name: "",
    phoneNumber: "",
    walletId: "",
  });

  const { walletId } = useParams();

  const handleRemarksClick = () => {
    setShowRemarks(true);
  };

  const handleAmountChange = (event) => {
    setAmount(event.target.value);
  };

  const handleRemarksChange = (event) => {
    setRemarks(event.target.value);
  };

  const handleSendMoney = () => {
    if (amount <= 0) alert("Enter the Amount greater than 0");
    if (amount) {
      // In a real application, you would make an API call here
      // Example of navigation after sending money
      // navigate('/transaction-successful');
      if (userData.merchant) {
        if (amount > 10000) {
          toast.error(
            "Transaction Amount can not be greater than your Account Limit, which is 10,000$ as this is Merchant Account"
          );
          return;
        }
      } else {
        if (amount > 5000) {
          toast.error(
            "Transaction Amount can not be greater than your Account Limit, which is 5,000$"
          );
          return;
        }
      }
      navigate("/transactionpinwindow", {
        state: {
          receiverWalletId: userData.walletId,
          amount: amount,
          remarks,
        },
      });
    } else {
      toast.error("Please enter the amount to send.");
    }
  };

  useEffect(() => {
    const fetchThisUserDetails = async () => {
      const res = await getWithToken(
        `api/auth/get/userdetails/from/walletid/${walletId}`
      );
      if (res.status == 200) {
        setUserData({
          email: res.data.email,
          id: res.data.id,
          merchant: res.data.merchant,
          name: res.data.name,
          phoneNumber: res.data.phoneNumber,
          walletId: res.data.walletId,
        });
      } else {
        console.log("Error fetching the userData");
      }
    };
    fetchThisUserDetails();
  }, []);

  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute bg-gray-300 rounded-lg shadow-lg z-10 flex flex-col">
        <Navbar />
        <div className="bg-white p-4 rounded-xl shadow-md ml-2 mr-2 mb-2 flex-grow overflow-y-auto">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold">Send Money</h2>
          </div>
          <div className="flex flex-col items-center mb-4">
            <div className="w-20 h-20 overflow-hidden mb-2">
              <ProfileIconIntitialGenerator name={userData.name} size={72} />
            </div>
            <p className="font-bold text-2xl text-gray-800">
              Pay to {userData.name}
            </p>
            {/*<p className="text-sm text-gray-600">Email: john.doe@example.com</p>
            <p className="text-sm text-gray-600">Wallet ID: 1234567890</p>
            <p className="text-sm text-gray-600">Phone: +91 9876543210</p> */}
            <div className="flex flex-row">
              <div className="flex flex-col items-start justify-center">
                <div>Email</div>
                <div>Wallet ID</div>
                <div>Phone No.</div>
              </div>
              <div className="flex flex-col items-end justify-center">
                <div>{userData.email}</div>
                <div>{userData.walletId}</div>
                <div>{userData.phoneNumber}</div>
              </div>
            </div>
          </div>
          {!showRemarks && (
            <button
              onClick={handleRemarksClick}
              className="mb-4 text-sm text-blue-500 hover:underline focus:outline-none"
            >
              Add Remarks
            </button>
          )}
          {showRemarks && (
            <div className="mb-4">
              <label
                htmlFor="remarks"
                className="block text-gray-700 text-sm font-bold mb-2"
              >
                Remarks
              </label>
              <textarea
                id="remarks"
                className="shadow appearance-none border rounded w-1/3 h-10 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                placeholder="Enter remarks"
                value={remarks}
                onChange={handleRemarksChange}
              />
            </div>
          )}
          <div className="mb-4">
            <label
              htmlFor="amount"
              className="block text-gray-700 text-sm font-bold mb-2"
            >
              Amount
            </label>
            <input
              type="number"
              id="amount"
              className="shadow appearance-none border rounded w-1/3 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              placeholder="Enter amount"
              value={amount}
              onChange={handleAmountChange}
              style={{ MozAppearance: "textfield" }}
              onFocus={(e) => (e.target.type = "number")}
              onBlur={(e) => (e.target.type = "number")}
              onKeyDown={(e) => {
                if (e.key === "-" || e.key === "e") {
                  e.preventDefault();
                }
              }}
              min="0"
            />
          </div>
          <button
            onClick={handleSendMoney}
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            disabled={!amount}
          >
            Send Money
          </button>
        </div>
      </div>
    </div>
  );
};

export default SendMoneyDemo;
