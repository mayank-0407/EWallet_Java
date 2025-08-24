import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { BiArrowBack } from "react-icons/bi";
import { Toaster, toast } from "react-hot-toast";
import OtpInput from "react-otp-input";
import { postWithToken } from "../../../services/operations/axiosPostRequest";
import { getWithToken } from "../../../services/operations/axiosGetRequest";
import { getUserDetailsFromToken } from "../../../services/apis";

function SetupWalletPin() {
  const [pin, setPin] = useState("");
  const [confirmPin, setConfirmPin] = useState("");
  const [pinMasked, setPinMasked] = useState(["", "", "", ""]);
  const [confirmPinMasked, setConfirmPinMasked] = useState(["", "", "", ""]);
  const [pinError, setPinError] = useState("");
  const [pinExists, setPinExists] = useState(false);
  const [myWalletId, setMyWalletId] = useState("");
  const navigate = useNavigate();

  const maskDelay = 250;

  const handleSetWalletPin = async (e) => {
    e.preventDefault();

    if (pin.length !== 4 || confirmPin.length !== 4) {
      toast.error("Please enter a 4-digit PIN in both fields.");
      return;
    }

    if (pin !== confirmPin) {
      toast.error("PINs do not match.");
      return;
    }

    const reqBody = {
      walletId: myWalletId,
      pinHash: pin,
    };

    try {
      const response = await postWithToken("api/wallet/pin/set", reqBody);
      if (response.status === 200) {
        toast.success("Wallet PIN set successfully!");
        navigate("/");
      } else {
        toast.error("Unable to update Pin!");
      }
    } catch (e) {
      toast.error("Error Occurred While setting up the pin!");
    }
  };

  const handleNumericInput = (event) => {
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode < 48 || charCode > 57) {
      event.preventDefault();
    }
  };

  // Masking logic for new pin
  const handlePinChange = (value) => {
    const newCharIndex = value.length - 1;
    setPin(value);

    const newMasked = [...pinMasked];
    newMasked[newCharIndex] = value[newCharIndex];
    setPinMasked(newMasked);

    setTimeout(() => {
      setPinMasked((prev) => {
        const updated = [...prev];
        if (value[newCharIndex] === pin[newCharIndex]) {
          updated[newCharIndex] = "*";
        }
        return updated;
      });
    }, maskDelay);
  };

  // Masking logic for confirm pin
  const handleConfirmPinChange = (value) => {
    const newCharIndex = value.length - 1;
    setConfirmPin(value);

    const newMasked = [...confirmPinMasked];
    newMasked[newCharIndex] = value[newCharIndex];
    setConfirmPinMasked(newMasked);

    setTimeout(() => {
      setConfirmPinMasked((prev) => {
        const updated = [...prev];
        if (value[newCharIndex] === confirmPin[newCharIndex]) {
          updated[newCharIndex] = "*";
        }
        return updated;
      });
    }, maskDelay);
  };

  useEffect(() => {
    const fetchPinDetails = async () => {
      try {
        const Data = await getUserDetailsFromToken();
        setMyWalletId(Data.data.walletId);

        const res = await getWithToken(
          `api/wallet/pin/exists/${Data.data.walletId}`
        );
        if (res.status === 200) {
          setPinExists(true);
          navigate("/");
        } else {
          setPinExists(false);
        }
      } catch (err) {
        setPinExists(false);
        console.error("Error occurred: ", err);
      }
    };

    fetchPinDetails();
    if (pinExists) {
      navigate("/");
    }
  }, []);

  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="flex flex-col items-center justify-center lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute bg-gray-400 rounded-lg shadow-lg z-10 ">
        <div className=" flex flex-col items-center justify-center">
          <div className="max-w-[500px] p-4 bg-gray-300 lg:p-8 rounded-lg">
            <h1 className="text-richblack-5 font-semibold text-[1.875rem] leading-[2.375rem]">
              Set Your Wallet PIN
            </h1>
            <p className="text-[1.125rem] leading-[1.625rem] my-4 text-richblack-100">
              Choose a secure 4-digit PIN for your wallet.
            </p>
            <form onSubmit={handleSetWalletPin}>
              <div className="mb-4">
                <label
                  htmlFor="pin"
                  className="block text-richblack-5 text-sm font-medium mb-2"
                >
                  New Wallet PIN
                </label>
                <OtpInput
                  value={pin}
                  onChange={handlePinChange}
                  numInputs={4}
                  renderInput={(props, index) => (
                    <input
                      {...props}
                      value={pinMasked[index] || ""}
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
              </div>
              <div className="mb-4">
                <label
                  htmlFor="confirmPin"
                  className="block text-richblack-5 text-sm font-medium mb-2"
                >
                  Confirm Wallet PIN
                </label>
                <OtpInput
                  value={confirmPin}
                  onChange={handleConfirmPinChange}
                  numInputs={4}
                  renderInput={(props, index) => (
                    <input
                      {...props}
                      value={confirmPinMasked[index] || ""}
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
              </div>
              {pinError && (
                <p className="text-red-500 text-sm mb-4">{pinError}</p>
              )}
              <button
                type="submit"
                className="w-full bg-yellow-200 hover:bg-yellow-400 py-[12px] px-[12px] rounded-[8px] mt-2 font-medium text-richblack-900"
              >
                Set Wallet PIN
              </button>
            </form>
          </div>
          <Toaster />
        </div>
      </div>
    </div>
  );
}

export default SetupWalletPin;
