import { useEffect, useState } from "react";
import OtpInput from "react-otp-input";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { BiArrowBack } from "react-icons/bi";
import { RxCountdownTimer } from "react-icons/rx";
import { Toaster, toast } from "react-hot-toast";
import { getCookie } from "../../../services/cookieService";
import { postWithToken } from "../../../services/operations/axiosPostRequest";

function TransactionPinWindow() {
  const navigate = useNavigate();
  const location = useLocation();

  const [otp, setOtp] = useState("");
  const [otpMasked, setOtpMasked] = useState(["", "", "", ""]);
  const [loading, setLoading] = useState(false);
  const [senderWalletId, setSenderWalletId] = useState("");
  const { receiverWalletId, amount, remarks } = location.state || {};

  const maskDelay = 250;
  const handleNumericInput = (event) => {
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode < 48 || charCode > 57) {
      event.preventDefault();
    }
  };
  const handleOtpChange = (value) => {
    const oldLength = otpMasked.filter(Boolean).length;
    const newLength = value.length;

    if (newLength > oldLength) {
      // New digit entered
      const newCharIndex = newLength - 1;

      // Show new digit immediately
      const newMasked = [...otpMasked];
      newMasked[newCharIndex] = value[newCharIndex];
      setOtpMasked(newMasked);
      setTimeout(() => {
        setOtpMasked((prev) => {
          const updated = [...prev];
          updated[newCharIndex] = "*";
          return updated;
        });
      }, maskDelay);
    } else if (newLength < oldLength) {
      const newMasked = [...otpMasked];
      for (let i = newLength; i < otpMasked.length; i++) {
        newMasked[i] = "";
      }
      setOtpMasked(newMasked);
    }

    setOtp(value);
  };

  const initiateTheTransaction = async () => {
    try {
      const reqBody = {
        senderId: senderWalletId,
        receiverId: receiverWalletId,
        amount: amount,
        remarks,
      };
      const response = await postWithToken("api/transaction/initiate", reqBody);

      if (response.status === 200) {
        // console.log("In status of Transaction Complete", response.data);
        navigate("/processingtransaction");
      } else {
        // console.log("Unable To Initiate Transaction ", response.data);
        navigate("/");
      }
    } catch (e) {
      // console.log("Error While Processing Transaction", e);
      navigate("/");
    }
  };

  const handleVerifyAndSignup = async (e) => {
    e.preventDefault();
    const reqBody = {
      walletId: senderWalletId,
      pinHash: otp,
    };
    try {
      const response = await postWithToken(`api/wallet/pin/verify`, reqBody);
      if (response.status === 200) {
        initiateTheTransaction();
      } else if (response.status === 403) {
        toast.error("Wrong Pin Entered");
      } else toast.error("Unknown Error Occured");
    } catch (error) {
      if (error.status === 403) {
        toast.error("Wrong Pin Entered.");
      } else toast.error("Unknown Error Occured");
    }
  };

  useEffect(() => {
    const tempSenderWalletId = getCookie("walletId");
    // console.log(
    //   "Senders Wallet ID :-- ",
    //   tempSenderWalletId,
    //   "--",
    //   receiverWalletId,
    //   "--",
    //   amount
    // );
    setSenderWalletId(tempSenderWalletId);
    if (!tempSenderWalletId) {
      navigate("/login");
    }
  }, []);

  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="flex flex-col items-center justify-center lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute  bg-gray-400 rounded-lg shadow-lg z-10 ">
        <div className=" flex flex-col items-center justify-center">
          {loading ? (
            <div>
              <div className="spinner"></div>
            </div>
          ) : (
            <div className="max-w-[500px] p-4 bg-gray-300 lg:p-8 rounded-lg">
              <h1 className="text-richblack-5 font-semibold text-[1.875rem] leading-[2.375rem]">
                Verify Pin
              </h1>
              <p className="text-[1.125rem] leading-[1.625rem] my-4 text-richblack-100">
                Enter the PIN below.
              </p>
              <form onSubmit={handleVerifyAndSignup}>
                <OtpInput
                  value={otp}
                  onChange={handleOtpChange}
                  numInputs={4}
                  renderInput={(props, index) => (
                    <input
                      {...props}
                      value={otpMasked[index] || ""}
                      placeholder="-"
                      style={{
                        boxShadow:
                          "inset 0px -1px 0px rgba(255, 255, 255, 0.18)",
                      }}
                      className="w-[48px] lg:w-[60px] border-0 bg-gray-500 rounded-[0.5rem] text-richblack-5 aspect-square text-center focus:border-0 focus:outline-2 focus:outline-yellow-50"
                      onKeyPress={handleNumericInput}
                    />
                  )}
                  containerStyle={{
                    justifyContent: "space-between",
                    gap: "0 6px",
                  }}
                />
                <button
                  type="submit"
                  className="w-full bg-yellow-200 hover:bg-yellow-300 py-[12px] px-[12px] rounded-[8px] mt-6 font-medium text-richblack-900"
                >
                  Verify Pin
                </button>
              </form>
            </div>
          )}
          <Toaster />
        </div>
      </div>
    </div>
  );
}

export default TransactionPinWindow;
