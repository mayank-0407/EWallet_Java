import { useState } from "react";
import OtpInput from "react-otp-input";
import { Link, useNavigate } from "react-router-dom";
import { BiArrowBack } from "react-icons/bi";
import { RxCountdownTimer } from "react-icons/rx";
import { Toaster, toast } from "react-hot-toast";
function VerifyLogin() {
  const [otp, setOtp] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  if (!signupData) {
    navigate("/signup");
  }

  const handleVerifyAndSignup = (e) => {
    e.preventDefault();
    const {
      accountType,
      firstName,
      lastName,
      email,
      password,
      confirmPassword,
    } = signupData;

    navigate("/setupwalletpin"); // Redirect to a welcome page or dashboard
  };

  const handleResendOtp = () => {
    toast.success("Email verified and user signed up!");
  };

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
                Verify Email
              </h1>
              <p className="text-[1.125rem] leading-[1.625rem] my-4 text-richblack-100">
                A verification code has been sent to you.
                <br /> Enter the code below.
              </p>
              <form onSubmit={handleVerifyAndSignup}>
                <OtpInput
                  value={otp}
                  onChange={setOtp}
                  numInputs={6}
                  renderInput={(props) => (
                    <input
                      {...props}
                      placeholder="-"
                      style={{
                        boxShadow:
                          "inset 0px -1px 0px rgba(255, 255, 255, 0.18)",
                      }}
                      className="w-[48px] lg:w-[60px] border-0 bg-gray-500 rounded-[0.5rem] text-richblack-5 aspect-square text-center focus:border-0 focus:outline-2 focus:outline-yellow-50"
                    />
                  )}
                  containerStyle={{
                    justifyContent: "space-between",
                    gap: "0 6px",
                  }}
                />
                <button
                  type="submit"
                  className="w-full bg-yellow-50 py-[12px] px-[12px] rounded-[8px] mt-6 font-medium text-richblack-900"
                >
                  Verify Email
                </button>
              </form>
              <div className="mt-6 flex items-center justify-between">
                <Link to="/signup">
                  <p className="text-richblack-5 flex items-center gap-x-2">
                    <BiArrowBack /> Back To Signup
                  </p>
                </Link>
                <button
                  className="flex items-center text-blue-600 gap-x-2"
                  onClick={handleResendOtp}
                >
                  <RxCountdownTimer />
                  Resend it
                </button>
              </div>
            </div>
          )}
          <Toaster />
        </div>
      </div>
    </div>
  );
}
export default VerifyLogin;
