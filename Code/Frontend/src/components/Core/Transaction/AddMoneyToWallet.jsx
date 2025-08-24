import Navbar from "../../Common/Navbar";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getUserDetailsFromToken, isLogin } from "../../../services/apis";
import { postWithToken } from "../../../services/operations/axiosPostRequest";
import { toast } from "react-hot-toast";
const AddMoneyToWallet = () => {
  const [amount, setAmount] = useState("");
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    userId: 0,
    phoneNumber: "",
    walletId: "",
  });
  const handleAmountChange = (event) => {
    setAmount(event.target.value);
  };
  const navigate = useNavigate();

  const handleSubmit = async () => {
    if (amount <= 0) {
      toast.error("Enter Amount Greater than 0");
      return;
    }
    if (amount > 10000) {
      toast.error(
        "Add money Amount Limit is $10000, so please enter amount less than equal to that!"
      );
      return;
    }
    const reqBody = {
      userId: formData.userId,
      balance: amount,
    };
    const res = await postWithToken("api/wallet/add/money", reqBody);
    if (res.data) {
      navigate("/addmoneytoWallettransaction");
      toast.success("Money Added to Wallet");
    } else {
      toast.error("Error Adding Money");
    }
  };

  useEffect(() => {
    const checkIfLoggedIn = async () => {
      const loggedIn = await isLogin();
      if (!loggedIn) navigate("/login");
    };

    checkIfLoggedIn();
  }, []);

  useEffect(() => {
    const fetchThisUserDetails = async () => {
      try {
        const Data = await getUserDetailsFromToken();
        const { name, email, id, phoneNumber, walletId } = Data.data;
        setFormData({
          name,
          email,
          userId: id,
          phoneNumber,
          walletId,
        });
      } catch (err) {
        console.error("Error occurred: ", err);
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
            <h2 className="text-lg font-semibold">Add Money To Wallet</h2>
          </div>
          <div className="flex flex-col items-center mb-4">
            <div className="w-20 h-20 rounded-full  mb-2">
              <img
                src="src\assets\E-Wallet Logo PNG.png" // Replace with actual URL
                alt="Wallet"
                className="w-full h-full object-contain"
              />
            </div>
            <p className="font-semibold text-2xl text-gray-800">
              {formData.name}
            </p>
            <div className="flex flex-row">
              <div className="flex flex-col items-start justify-end pr-2">
                <div>Email</div>
                <div>Wallet ID</div>
              </div>
              <div className="flex flex-col items-start justify-start pl-2">
                <div>{formData.email}</div>
                <div>{formData.walletId}</div>
              </div>
            </div>
            <div>
              <div></div>
              <div></div>
            </div>
          </div>
          <div className="mb-4">
            <label
              htmlFor="amount"
              className="block text-gray-700 text-lg font-bold mb-2"
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
              onKeyDown={(e) => {
                if (e.key === "-" || e.key === "e") {
                  e.preventDefault();
                }
              }}
              min="0"
            />
          </div>
          <button
            onClick={() => {
              handleSubmit();
            }}
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            // disabled={!amount}
          >
            Add Money
          </button>
        </div>
      </div>
    </div>
  );
};
export default AddMoneyToWallet;
