import { useEffect, useState } from "react";
import { AiOutlineEye, AiOutlineEyeInvisible } from "react-icons/ai";
// import { useDispatch } from "react-redux"
// import { Link, useNavigate } from "react-router-dom"
import { useNavigate } from "react-router-dom";
// import { login } from "../../../services/operations/authAPI"
import axios from "axios";
import { getCookie, setAuthentication } from "../../../services/cookieService";
import { BASE_URL } from "../../../utils/constants";
import { toast } from "react-hot-toast";
import { getUserDetailsFromToken } from "../../../services/apis";
import { getWithToken } from "../../../services/operations/axiosGetRequest";
import { FaSearch } from "react-icons/fa";

function LoginForm() {
  const navigate = useNavigate();
  //   const dispatch = useDispatch()
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const { username, password } = formData;
  // const [error, setError] = useState("");
  // const [pinExists, setPinExists] = useState(0);
  const [countdown, setCountdown] = useState(10);
  const [isDisabled, setIsDisabled] = useState(false);
  const handleOnChange = (e) => {
    setFormData((prevData) => ({
      ...prevData,
      [e.target.name]: e.target.value,
    }));
  };

  const fetchPinDetails = async () => {
    try {
      const Data = await getUserDetailsFromToken();

      const res = await getWithToken(
        `api/wallet/pin/exists/${Data.data.walletId}`
      );
      if (res.status === 200) {
        // console.log("1 : ",res.data);
        return res.data;
      } else if (res.status === 202) {
        // console.log("2 : ",res.data);
        return res.data;
      } else {
        return false;
      }
    } catch (err) {
      // console.log("3 : ",err);
      return false;
    }
  };
  const handleOnSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(BASE_URL + "api/auth/login", formData);
      if (response.status === 200) {
        setAuthentication(response.data);
        const pinAlreadyExists = await fetchPinDetails();
        // console.log("MAIN : ", pinAlreadyExists);
        if (pinAlreadyExists) {
          navigate("/");
          // console.log(pinAlreadyExists);
        } else navigate("/setupwalletpin");
        toast.success("Login Successful");
      } else if (response.status === 404) {
        toast.error("UserName Doesn't Exists");
      } else {
        toast.error("An error occurred. Please try again");
      }
    } catch (err) {
      if (err.response.status === 403) {
        toast.error("Incorrect username or password");
        // Disable button
        setIsDisabled(true);
        // Re-enable after 10 seconds
        setCountdown(10);
        setTimeout(() => {
          setIsDisabled(false);
        }, 10000);
        return;
      } else if (err.response.status === 400) {
        toast.error("Bad request. Please check your input");
      } else if (err.response.status === 404) {
        toast.error("UserName Doesn't Exists");
      } else {
        toast.error("An error occurred. Please try again later.");
      }
    }
  };
  useEffect(() => {
    let timer;
    if (isDisabled) {
      // Start countdown interval
      timer = setInterval(() => {
        setCountdown((prev) => {
          if (prev === 1) {
            clearInterval(timer);
            setIsDisabled(false);
            return 10; // reset for next time
          }
          return prev - 1;
        });
      }, 1000);
    }
    return () => clearInterval(timer);
  }, [isDisabled]);

  useEffect(() => {
    const hasToken = getCookie("token");
    if (hasToken) navigate("/");
  }, []);

  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute  bg-gray-400 rounded-lg shadow-lg z-10 ">
        <div className="flex lg:flex-row sm:flex-col md:flex-row flex-col w-full h-full md:justify-center items-center">
          <div className="flex flex-col md:w-[30%] w-[100%] md:h-full h-[30%] bg-gray-200 p-4  justify-center items-center">
            <img
              src="/E_Wallet_Logo_PNG.png"
              alt=""
              className="w-16 h-16 mr-2 sm:mr-4 "
            />
            <h1 className="text-lg">E-Wallet System</h1>
            <h1 className="text-3xl">Login</h1>
          </div>
          <div className="text-black flex w-[70%] p-4">
            {/* Form */}
            <form
              onSubmit={handleOnSubmit}
              className="flex w-full flex-col text-xl items-center-safe justify-evenly"
            >
              <div className="flex flex-col gap-y-2 justify-around items-start">
                <label className="">
                  <div className="relative w-full flex flex-row items-center -translate-x-0.5 justify-center">
                    <p className="mb-1 leading-[1.375rem] w-[16vw] text-richblack-5">
                      Phone Number
                    </p>
                    <input
                      required
                      type="tel"
                      name="username"
                      value={username}
                      onChange={handleOnChange}
                      minLength={10}
                      maxLength={10}
                      placeholder="Enter Phone Number"
                      className="form-style w-full p-2 text-white rounded-t-lg bg-gray-500"
                    />
                  </div>
                </label>
                <label>
                  <div className="flex flex-row w-full items-center justify-center">
                    <p className="mb-1  leading-[1.375rem] w-[16vw] text-richblack-5">
                      Password
                    </p>
                    <div className="relative w-full flex flex-row items-center -translate-x-0.5 justify-center">
                      <input
                        required
                        //type={showPassword ? "text" : "password"}
                        type="password"
                        name="password"
                        value={password}
                        onChange={handleOnChange}
                        placeholder="Enter Password"
                        className="form-style w-full p-2 text-white rounded-b-lg bg-gray-500"
                      />
                    </div>
                  </div>
                </label>
                {/* <label>
                  <div className="flex flex-row justify-center items-center ">
                    <p className="leading-[1.375rem] -translate-x-5 text-richblack-5">
                      Select Account Type
                    </p>
                    <div className="relative bg-gray-200 rounded -translate-x-4 flex  w-[16vw] transition-all duration-300">
                      <div
                        className={`absolute top-1  bottom-1 w-1/2 rounded bg-gray-500 transition-all duration-300 
                    ${accountType === "user" ? "left-1" : "left-30"} }`}
                      ></div>

                      <label
                        className="w-1/2 text-center z-10 cursor-pointer select-none"
                        onClick={() => {
                          setAccountType("user");
                          handleOnChange({
                            target: {
                              name: "accountType",
                              value: "user",
                            },
                          });
                        }}
                      >
                        <input
                          type="radio"
                          name="accountType"
                          value="user"
                          checked={accountType === "user"}
                          className="hidden"
                          onChange={handleOnChange}
                        />
                        <span
                          className={`block py-2  font-medium ${
                            accountType === "user"
                              ? "text-white"
                              : "text-gray-700"
                          }`}
                        >
                          Normal
                        </span>
                      </label>

                      <label
                        className="w-1/2 text-center z-10 cursor-pointer select-none"
                        onClick={() => {
                          setAccountType("mechant");
                          handleOnChange({
                            target: {
                              name: "accountType",
                              value: "merchant",
                            },
                          });
                        }}
                      >
                        <input
                          type="radio"
                          name="accountType"
                          value="merchant"
                          checked={accountType === "merchant"}
                          className="hidden"
                          onChange={handleOnChange}
                        />
                        <span
                          className={`block py-2 font-medium ${
                            accountType === "merchant"
                              ? "text-white"
                              : "text-gray-700"
                          }`}
                        >
                          Merchant
                        </span>
                      </label>
                    </div>
                  </div>
                </label> */}
                <span
                  className="text-blue-600 text-base cursor-pointer translate-x-[100%]"
                  onClick={() => {
                    navigate("/signup");
                  }}
                >
                  Not Registered Yet? Register Here
                </span>
                <button
                  type="submit"
                  className={`mt-2 rounded-[8px] bg-gray-200 py-[8px] px-[12px] w-full hover:bg-gray-300 shadow-lg hover:border-2 font-medium text-richblack-900 
                     ${
                       isDisabled
                         ? "cursor-not-allowed opacity-50"
                         : "hover:bg-gray-300 hover:border-2"
                     }`}
                >
                  {isDisabled ? `Try Again in ${countdown} seconds` : "LOGIN"}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LoginForm;
