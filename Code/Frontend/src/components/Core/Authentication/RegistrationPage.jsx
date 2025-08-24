import { useState, useEffect } from "react";
import { ACCOUNT_TYPE } from "./../../../utils/constants";
import { AiOutlineEye, AiOutlineEyeInvisible } from "react-icons/ai";
import { toast } from "react-hot-toast";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { BASE_URL } from "../../../utils/constants";
import { getCookie } from "../../../services/cookieService";
//import { useNavigate } from "react-router-dom"
function RegistrationPage() {
  const [accountType, setAccountType] = useState("user");
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phoneNumber: "",
    password: "",
    confirmPassword: "",
    dob: "",
    accountType: "user",
    merchant: false,
  });

  // const [showPassword, setShowPassword] = useState(false);
  // const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const navigate = useNavigate();
  const [thisdate, setthisdate] = useState("");
  const { name, email, phoneNumber, password, confirmPassword } = formData;
  const handleOnChange = (e) => {
    const { thisname, value } = e.target;
    if (thisname === "dob") {
      const formattedDate = new Date(value).toLocaleDateString("en-US");
      setFormData((prevData) => ({
        ...prevData,
        [thisname]: formattedDate,
      }));
    } else {
      setFormData((prevData) => ({
        ...prevData,
        [e.target.name]: e.target.value,
      }));
    }
  };

  // Handle Form Submission
  // const handleOnSubmit = async (e) => {
  //   e.preventDefault();

  //   if (password !== confirmPassword) {
  //     toast.error("Passwords Do Not Match");
  //     return;
  //   }
  //   formData.dob = thisdate;
  //   if (formData.accountType === "merchant") formData.merchant = true;

  //   try {
  //     const response = await axios.post(
  //       BASE_URL + "api/auth/register",
  //       formData
  //     );
  //     if (response.status === 200) {
  //       navigate("/login");
  //       toast.success("Registeration Successful");
  //       // Reset;
  //       setFormData({
  //         name: "",
  //         email: "",
  //         phoneNumber: "",
  //         password: "",
  //         confirmPassword: "",
  //         dob: "",
  //       });
  //     } else {
  //       toast.error("Error while Signing Up");
  //     }
  //   } catch (e) {
  //     toast.error("Unable to Register User");
  //   }
  // };
  // const handleOnSubmit = async (e) => {
  //   e.preventDefault();

  //   const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  //   const phoneRegex = /^\d{10}$/;
  //   const strongPasswordRegex =
  //     /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{6,}$/;

  //   // Basic validations
  //   if (!formData.name.trim()) {
  //     toast.error("Full name is required");
  //     return;
  //   }

  //   if (!emailRegex.test(formData.email)) {
  //     toast.error("Invalid email format");
  //     return;
  //   }

  //   if (!phoneRegex.test(formData.phoneNumber)) {
  //     toast.error("Phone number must be 10 digits");
  //     return;
  //   }

  //   if (!strongPasswordRegex.test(formData.password)) {
  //     toast.error(
  //       "Password must be at least 6 characters and include a number"
  //     );
  //     return;
  //   }

  //   if (formData.password !== formData.confirmPassword) {
  //     toast.error("Passwords do not match");
  //     return;
  //   }

  //   if (!thisdate) {
  //     toast.error("Date of birth is required");
  //     return;
  //   }

  //   formData.dob = thisdate;
  //   if (formData.accountType === "merchant") formData.merchant = true;

  //   try {
  //     const response = await axios.post(
  //       BASE_URL + "api/auth/register",
  //       formData
  //     );
  //     if (response.status === 200) {
  //       navigate("/login");
  //       toast.success("Registration Successful");
  //       setFormData({
  //         name: "",
  //         email: "",
  //         phoneNumber: "",
  //         password: "",
  //         confirmPassword: "",
  //         dob: "",
  //         accountType: "user",
  //         merchant: false,
  //       });
  //       setthisdate("");
  //     } else {
  //       toast.error("Error while Signing Up");
  //     }
  //   } catch (e) {
  //     toast.error("Unable to Register User");
  //   }
  // };

  const handleOnSubmit = async (e) => {
    e.preventDefault();

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^\d{10}$/;
    const strongPasswordRegex =
      /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{6,}$/;

    // Basic validations
    if (!formData.name.trim()) {
      toast.error("Full name is required");
      return;
    }

    if (!emailRegex.test(formData.email)) {
      toast.error("Invalid email format");
      return;
    }

    if (!phoneRegex.test(formData.phoneNumber)) {
      toast.error("Phone number must be 10 digits");
      return;
    }

    if (!strongPasswordRegex.test(formData.password)) {
      toast.error(
        "Password must be at least 6 characters and include a number"
      );
      return;
    }

    if (formData.password !== formData.confirmPassword) {
      toast.error("Passwords do not match");
      return;
    }

    if (!thisdate) {
      toast.error("Date of birth is required");
      return;
    }

    // Validate age >= 18
    const dob = new Date(thisdate);
    const today = new Date();
    let age = today.getFullYear() - dob.getFullYear();
    const m = today.getMonth() - dob.getMonth();

    if (m < 0 || (m === 0 && today.getDate() < dob.getDate())) {
      age--;
    }

    if (age < 18) {
      toast.error("You must be at least 18 years old");
      return;
    }

    formData.dob = thisdate;
    if (formData.accountType === "merchant") formData.merchant = true;

    try {
      const response = await axios.post(
        BASE_URL + "api/auth/register",
        formData
      );
      if (response.status === 200) {
        navigate("/login");
        toast.success("Registration Successful");
        setFormData({
          name: "",
          email: "",
          phoneNumber: "",
          password: "",
          confirmPassword: "",
          dob: "",
          accountType: "user",
          merchant: false,
        });
        setthisdate("");
      } else {
        toast.error("Error while Signing Up");
      }
    } catch (e) {
      if (e.status === 400) {
        toast.error("User Already Exists!");
      } else if (e.status === 404) {
        toast.error("Not Found");
      } else {
        toast.error("Unable to Register User");
      }
    }
  };

  useEffect(() => {
    const hasToken = getCookie("token");
    if (hasToken) navigate("/");
  }, []);
  // const [accountType, setAccountType] = useState(ACCOUNT_TYPE.STUDENT)

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
            <h1 className="text-3xl">Registration</h1>
          </div>
          <div className="text-black flex w-[70%] p-4">
            {/* Form */}
            <form
              noValidate
              onSubmit={handleOnSubmit}
              className="flex w-full flex-col text-xl lg:text-lg md:text-base items-center-safe justify-evenly"
            >
              <div className="flex flex-col gap-y-2 justify-around items-start">
                <label>
                  <div className="flex lg:flex-row md:flex-row w-full sm:flex-col md:items-center lg:items-center md:justify-center lg:justify-center sm:items-start">
                    <p className="mb-1 leading-[1.375rem] w-[16vw] md:whitespace-normal lg:whitespace-normal sm:whitespace-nowrap  text-richblack-5">
                      Full Name
                    </p>
                    <input
                      required
                      type="text"
                      value={name}
                      onChange={handleOnChange}
                      name="name"
                      placeholder="Enter Full Name"
                      className="form-style w-full p-2 sm:p-1 text-white bg-gray-500 rounded-t-lg"
                    />
                  </div>
                </label>
                <label>
                  <div className="flex lg:flex-row md:flex-row w-full sm:flex-col md:items-center lg:items-center md:justify-center lg:justify-center sm:items-start">
                    <p className="mb-1 leading-[1.375rem] w-[16vw] sm:whitespace-nowrap text-richblack-5">
                      Email
                    </p>
                    <input
                      required
                      type="email"
                      name="email"
                      onChange={handleOnChange}
                      value={email}
                      placeholder="Enter email address"
                      className="form-style w-full p-2 sm:p-1 text-white bg-gray-500"
                    />
                  </div>
                </label>
                <label className="">
                  <div className="flex lg:flex-row md:flex-row lg:w-full md:w-full sm:flex-col md:items-center lg:items-center md:justify-center lg:justify-center sm:items-start">
                    <p className="mb-1 leading-[1.375rem] w-[16vw] sm:whitespace-nowrap md:-translate-x-6 text-richblack-5">
                      Phone Number
                    </p>
                    <input
                      required
                      type="tel"
                      name="phoneNumber"
                      value={phoneNumber}
                      onChange={handleOnChange}
                      minLength={10}
                      maxLength={10}
                      placeholder="Enter phone number"
                      className="form-style md:w-full p-2 sm:p-1 text-white bg-gray-500"
                    />
                  </div>
                </label>
                <label className="">
                  <div className="flex lg:flex-row md:flex-row w-full sm:flex-col md:items-center lg:items-center md:justify-center lg:justify-center sm:items-start">
                    <p className="mb-1  leading-[1.375rem] w-[16vw] sm:whitespace-nowrap text-richblack-5">
                      Create Password
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
                        className="form-style w-full p-2 sm:p-1 text-white bg-gray-500"
                      />
                      {/* <span
                      onClick={() => setShowPassword((prev) => !prev)}
                      className="relative left-3 cursor-pointer"
                    >
                      {showPassword ? (
                        <AiOutlineEyeInvisible
                          className="text-2xl text-black -translate-x-12"/>
                      ) : (
                        <AiOutlineEye className="text-2xl text-black -translate-x-12" />
                      )}
                    </span> */}
                    </div>
                  </div>
                </label>
                <label className="">
                  <div className="flex lg:flex-row md:flex-row w-full sm:flex-col md:items-center lg:items-center md:justify-center lg:justify-center sm:items-start -translate-x-0.5">
                    <p className=" leading-[1.375rem] w-[16vw] sm:whitespace-nowrap text-richblack-5">
                      Confirm Password
                    </p>
                    <div className="relative w-full flex flex-row rounded-br-lg justify-center items-center">
                      <input
                        required
                        //type={showConfirmPassword ? "text" : "password"}
                        type="password"
                        name="confirmPassword"
                        value={confirmPassword}
                        onChange={handleOnChange}
                        placeholder="Confirm Password"
                        className="form-style w-full p-2 sm:p-1 text-white bg-gray-500 appearance-none"
                      />
                      {/* <span
                      onClick={() => setShowConfirmPassword((prev) => !prev)}
                      className="relative cursor-pointer"
                    >
                      {showConfirmPassword ? (
                        <AiOutlineEyeInvisible className="text-2xl ml-2 -translate-x-12 text-black" />
                      ) : (
                        <AiOutlineEye className="text-2xl ml-2 -translate-x-12 text-black " />
                      )}
                    </span> */}
                    </div>
                  </div>
                </label>
                <label>
                  <div className="flex lg:flex-row md:flex-row w-full sm:flex-col md:items-center lg:items-center md:justify-center lg:justify-center sm:items-start">
                    <p className="mb-1 mr-1 w-[10vw] sm:whitespace-nowrap leading-[1.375rem] text-richblack-5">
                      Date of Birth
                    </p>
                    <input
                      type="date"
                      value={thisdate}
                      onChange={(e) => setthisdate(e.target.value)}
                      placeholder="MM-DD-YYYY"
                      className="p-2 sm:p-1 text-white bg-gray-500  appearance-none"
                    />
                  </div>
                </label>
                <label>
                  <div className="flex flex-row sm:flex-col lg:flex-row sm:items-start sm:justify-center lg:justify-center lg:items-center ">
                    <p className="leading-[1.375rem] lg:w-full sm:w-1/3 sm:whitespace-nowrap  lg:-translate-x-5 sm:translate-x-0.5 sm:mb-1 text-richblack-5">
                      Select Account Type
                    </p>
                    <div className="relative bg-gray-200 sm:text-lg lg:text-xl rounded flex sm:flex-row  lg:w-[100%]  sm:w-full transition-all duration-300">
                      <div
                        className={`absolute top-1  bottom-1 w-[48%] rounded bg-gray-500 transition-all duration-300 
                    ${
                      accountType === "user"
                        ? "left-1 "
                        : "lg:left-23 sm:left-22"
                    } }`}
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
                          className={`block py-2 pr-7.5 font-medium ${
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
                </label>
                <span
                  className="text-blue-600 text-base cursor-pointer translate-x-[100%]"
                  onClick={() => {
                    navigate("/login");
                  }}
                >
                  Already Registered? Login Here
                </span>

                <button
                  type="submit"
                  className="mt-2 rounded-[8px] bg-gray-200 py-[8px] px-[12px] w-full hover:bg-gray-300 shadow-lg hover:border-2 font-medium text-richblack-900"
                >
                  REGISTER
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
export default RegistrationPage;
