import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { BiArrowBack } from "react-icons/bi";
import { Toaster, toast } from "react-hot-toast";
import OtpInput from "react-otp-input";
import { postWithToken } from "../../../services/operations/axiosPostRequest";
import { getCookie } from "../../../services/cookieService";
import Navbar from "../../Common/Navbar";

function ChangeWalletPin() {
  const [oldPin, setOldPin] = useState("");
  const [oldPinMasked, setOldPinMasked] = useState(["", "", "", ""]);

  const [pin, setPin] = useState("");
  const [pinMasked, setPinMasked] = useState(["", "", "", ""]);

  const [confirmPin, setConfirmPin] = useState("");
  const [confirmPinMasked, setConfirmPinMasked] = useState(["", "", "", ""]);

  const [pinError, setPinError] = useState("");
  const navigate = useNavigate();

  const maskDelay = 250;

  const handleOtpChange = (value, setValue, maskedValue, setMaskedValue) => {
    const oldLength = maskedValue.filter(Boolean).length;
    const newLength = value.length;

    if (newLength > oldLength) {
      const newCharIndex = newLength - 1;

      const newMasked = [...maskedValue];
      newMasked[newCharIndex] = value[newCharIndex];
      setMaskedValue(newMasked);

      setTimeout(() => {
        setMaskedValue((prev) => {
          const updated = [...prev];
          updated[newCharIndex] = "*";
          return updated;
        });
      }, maskDelay);
    } else if (newLength < oldLength) {
      const newMasked = [...maskedValue];
      for (let i = newLength; i < maskedValue.length; i++) {
        newMasked[i] = "";
      }
      setMaskedValue(newMasked);
    }

    setValue(value);
  };

  const handleSetWalletPin = async (e) => {
    e.preventDefault();

    if (oldPin.length !== 4 || pin.length !== 4 || confirmPin.length !== 4) {
      toast.error("Please enter a 4-digit PIN in all fields.");
      return;
    }

    if (pin !== confirmPin) {
      toast.error("PINs do not match.");
      return;
    }

    const walletId = getCookie("walletId");
    const reqBody = {
      walletId,
      oldPin,
      newPin: pin,
    };

    try {
      const response = await postWithToken(`api/wallet/pin/change`, reqBody);
      if (response.status === 200) {
        toast.success("New Wallet PIN set successfully!");
        navigate("/");
      } else {
        toast.error("Error: Unknown Error Occurred!");
      }
    } catch (e) {
      if (e.status === 403) toast.error("Error: Old Password didn't match!");
      else toast.error("Error: Unknown Error Occurred!");
    }
  };

  const handleNumericInput = (event) => {
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode < 48 || charCode > 57) {
      event.preventDefault();
    }
  };

  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute bg-gray-300 rounded-lg shadow-lg z-10 flex flex-col">
        <Navbar />
        <div className="bg-white p-4 rounded-xl shadow-md ml-2 mr-2 mb-2 flex-grow overflow-y-auto">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold">Change Wallet Pin</h2>
          </div>
          <div className="items-center justify-center flex flex-col ">
            <p className="text-[1.125rem] leading-[1.625rem]  my-2 text-richblack-100">
              Choose a new secure 4-digit PIN for your wallet.
            </p>
            <div className="max-w-[300px]">
              <form onSubmit={handleSetWalletPin}>
                {/* Old PIN */}
                <div className="mb-4">
                  <label
                    htmlFor="oldPin"
                    className="block text-richblack-5 text-sm font-medium mb-2"
                  >
                    Old Wallet PIN
                  </label>
                  <OtpInput
                    value={oldPin}
                    onChange={(value) =>
                      handleOtpChange(
                        value,
                        setOldPin,
                        oldPinMasked,
                        setOldPinMasked
                      )
                    }
                    numInputs={4}
                    renderInput={(props, index) => (
                      <input
                        {...props}
                        value={oldPinMasked[index] || ""}
                        placeholder="-"
                        style={{
                          boxShadow:
                            "inset 0px -1px 0px rgba(255, 255, 255, 0.18)",
                        }}
                        className="w-[48px] lg:w-[60px] border-0 bg-gray-400 rounded-[0.5rem] text-richblack-5 aspect-square text-center focus:border-0 focus:outline-2 focus:outline-yellow-50"
                        onKeyPress={handleNumericInput}
                      />
                    )}
                    containerStyle={{
                      justifyContent: "space-between",
                      gap: "0 6px",
                    }}
                  />
                </div>

                {/* New PIN */}
                <div className="mb-4">
                  <label
                    htmlFor="pin"
                    className="block text-richblack-5 text-sm font-medium mb-2"
                  >
                    New Wallet PIN
                  </label>
                  <OtpInput
                    value={pin}
                    onChange={(value) =>
                      handleOtpChange(value, setPin, pinMasked, setPinMasked)
                    }
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
                        className="w-[48px] lg:w-[60px] border-0 bg-gray-400 rounded-[0.5rem] text-richblack-5 aspect-square text-center focus:border-0 focus:outline-2 focus:outline-yellow-50"
                        onKeyPress={handleNumericInput}
                      />
                    )}
                    containerStyle={{
                      justifyContent: "space-between",
                      gap: "0 6px",
                    }}
                  />
                </div>

                {/* Confirm PIN */}
                <div className="mb-4">
                  <label
                    htmlFor="confirmPin"
                    className="block text-richblack-5 text-sm font-medium mb-2"
                  >
                    Confirm Wallet PIN
                  </label>
                  <OtpInput
                    value={confirmPin}
                    onChange={(value) =>
                      handleOtpChange(
                        value,
                        setConfirmPin,
                        confirmPinMasked,
                        setConfirmPinMasked
                      )
                    }
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
                        className="w-[48px] lg:w-[60px] border-0 bg-gray-400 rounded-[0.5rem] text-richblack-5 aspect-square text-center focus:border-0 focus:outline-2 focus:outline-yellow-50"
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
                  className="w-full bg-blue-500 text-white py-2 rounded-md hover:bg-blue-600 transition"
                >
                  Update Wallet PIN
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ChangeWalletPin;
