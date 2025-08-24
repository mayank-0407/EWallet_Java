import Navbar from "../../Common/Navbar";
import React, { useState, useEffect } from "react";
import { getUserDetailsFromToken } from "../../../services/apis";
import { postWithToken } from "../../../services/operations/axiosPostRequest";
import { toast } from "react-hot-toast";
import { useNavigate } from "react-router-dom";

function EditProfile() {
  const [formData, setFormData] = useState({
    username: "",
    fullName: "",
    email: "",
    phoneNumber: "",
    dob: "",
  });
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const [accountType, setAccountType] = useState(false);
  const handleOnChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleOnSubmit = async (e) => {
    e.preventDefault();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      toast.error("Invalid email format");
      return;
    }
    try {
      const reqBody = {
        username: formData.username,
        email: formData.email,
        name: formData.fullName,
      };
      const response = await postWithToken("api/auth/profile/update", reqBody);

      if (response.status === 200) {
        toast.success("Profile Updated Successfully");
        navigate("/");
        // location.reload();
      } else {
        toast.error("Unable to Update Profile");
        throw new Error("Unable to Update Profile: " + response.statusText);
      }
    } catch (err) {
      toast.error(
        "Something went wrong while updating the profile. Please try again."
      );
    }
  };

  useEffect(() => {
    const fetchUserDetails = async () => {
      try {
        const res = await getUserDetailsFromToken();
        if (res.status === 200) {
          const formattedDob = res.data.dob
            ? new Date(res.data.dob).toISOString().split("T")[0]
            : "";
          setAccountType(res.data.merchant);
          setFormData({
            username: res.data.username,
            fullName: res.data.name,
            email: res.data.email,
            phoneNumber: res.data.phoneNumber,
            dob: formattedDob,
          });
        } else {
          throw new Error("Failed to fetch user details.");
        }
      } catch (err) {
        console.error("Error occurred while fetching user details:", err);
        toast.error("Failed to load user details. Please try again.");
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
            <h2 className="text-lg font-semibold">Edit Profile</h2>
          </div>
          <div className="w-[100%] flex flex-col items-center">
            <form
              className="bg-white p-8 rounded-xl shadow-md w-full max-w-md"
              onSubmit={handleOnSubmit}
              noValidate
            >
              <div className="flex flex-row justify-end items-center mb-2">
                <span className="text-gray-900 mr-2">Full Name</span>
                <input
                  type="text"
                  name="fullName"
                  value={formData.fullName}
                  onChange={handleOnChange}
                  className="mt-1 block w-2/3 border border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div className="flex flex-row justify-end items-center mb-4">
                <span className="text-gray-900 mr-2">Email</span>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleOnChange}
                  className="mt-1 block w-2/3 border border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div className="flex flex-row justify-end items-center mb-4">
                <span className="text-gray-900 mr-2">Phone Number</span>
                <input
                  type="number"
                  name="phoneNumber"
                  value={formData.phoneNumber}
                  disabled
                  className="mt-1 block w-2/3 border text-gray-400 border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div className="flex flex-row justify-end items-center mb-4">
                <span className="text-gray-900 mr-2">Date of Birth</span>
                <input
                  type="date"
                  name="2/3Name"
                  value={formData.dob}
                  disabled
                  className="mt-1 block w-2/3 border text-gray-400 border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div className="flex flex-row justify-end items-center mb-4">
                <span className="text-gray-900 mr-2">Account Type</span>
                <input
                  type="text"
                  name="accountType"
                  value={accountType ? "Merchant User" : "Normal User"}
                  disabled
                  className="mt-1 block w-2/3 border text-gray-400 border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
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
export default EditProfile;
