import Navbar from "../../Common/Navbar";
import React, { useEffect, useState } from "react";
import { MdPassword } from "react-icons/md";
import { getUserDetailsFromToken } from "../../../services/apis";
import { logOut } from "../../../services/cookieService";
import { postWithToken } from "../../../services/operations/axiosPostRequest";
import { useNavigate } from "react-router-dom";
import { toast } from "react-hot-toast";
// EditProfile Component
function ChangePassword() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });
  const [phoneNumber, setPhoneNumber] = useState("");
  const [error, setError] = useState("");

  const handleOnChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value }); // Update formData state based on the input change
  };

  const handleOnSubmit = async (e) => {
    e.preventDefault();

    // Check if new password and confirm password match
    if (formData.newPassword !== formData.confirmPassword) {
      toast.error("New password and confirm password do not match.");
      return;
    }
    if (formData.currentPassword === formData.newPassword) {
      toast.error("Current password and new password can't be same.");
      return;
    }
    const reqBody = {
      phoneNumber,
      currentPassword: formData.currentPassword,
      newPassword: formData.newPassword,
    };

    try {
      const response = await postWithToken("api/auth/change/password", reqBody);

      if (response.status === 200) {
        // Password changed successfully
        toast.success("Password changed successfully!");
        logOut();
        navigate("/login");
      } else {
        toast.error("Failed to change password. Please try again.");
      }
    } catch (err) {
      console.error("Error during password change:", err);
      toast.error(
        "An error occurred while changing your password. Please try again."
      );
    }
  };

  useEffect(() => {
    const fetchUserDetails = async () => {
      try {
        const res = await getUserDetailsFromToken();
        if (res.status === 200) {
          setPhoneNumber(res.data.phoneNumber);
        } else {
          throw new Error("Failed to fetch user details.");
        }
      } catch (err) {
        console.error("Error occurred while fetching user details:", err);
        // setError("Failed to load user details. Please try again.");
      }
    };

    fetchUserDetails();
  }, []);

  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute bg-gray-300 rounded-lg shadow-lg z-10 flex flex-col">
        <Navbar />
        <div className="bg-white p-4 rounded-xl shadow-md ml-2 mr-2 mb-2 flex-grow overflow-y-auto">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold">Change Password</h2>
          </div>
          <div className="w-[100%] flex flex-col items-center">
            <div>
              <MdPassword className="w-24 h-24" />
            </div>
            <form
              className="bg-white p-8 text-sm rounded-xl shadow-md w-full max-w-md"
              onSubmit={handleOnSubmit}
            >
              {/* Current Password Input */}
              <div className="flex flex-row justify-end items-center mb-2">
                <span className="text-gray-900 mr-2">Current Password</span>
                <input
                  type="password"
                  name="currentPassword"
                  value={formData.currentPassword}
                  onChange={handleOnChange}
                  className="mt-1 block w-2/3 border border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              {/* New Password Input */}
              <div className="flex flex-row justify-end items-center mb-4">
                <span className="text-gray-900 mr-2">New Password</span>
                <input
                  type="password"
                  name="newPassword"
                  value={formData.newPassword}
                  onChange={handleOnChange}
                  className="mt-1 block w-2/3 border border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              {/* Confirm New Password Input */}
              <div className="flex flex-row justify-end items-center mb-4">
                <span className="text-gray-900 mr-2">Confirm Password</span>
                <input
                  type="password"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleOnChange}
                  className="mt-1 block w-2/3 border border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              {/* Submit Button */}
              <button
                className="w-full bg-blue-500 text-white py-2 rounded-md hover:bg-blue-600 transition"
                type="submit"
              >
                Update
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ChangePassword;
