import Navbar from "../../Common/Navbar";
import React, { useEffect, useState } from "react";
import axios from "axios";
import toast from "react-hot-toast";
import { postWithToken } from "../../../services/operations/axiosPostRequest";
import { getCookie } from "../../../services/cookieService";
import { getWithToken } from "../../../services/operations/axiosGetRequest";
import { useNavigate } from "react-router-dom";

function BusinessDetails() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    businessName: "",
    industry: "",
    address: "",
    city: "",
    state: "",
    zipCode: "",
  });

  const [businessDetailsAlreadyUpdated, setBusinessDetailsAlreadyUpdated] =
    useState(false);

  useEffect(() => {
    const fetchBusinessDetails = async () => {
      const myBusinessId = getCookie("businessId");
      try {
        const response = await getWithToken(
          `api/auth/business/${myBusinessId}`
        );
        // console.log(response);
        if (response.status === 200) {
          setFormData(response.data);
          setBusinessDetailsAlreadyUpdated(true);
        } else {
          setBusinessDetailsAlreadyUpdated(false);
        }
      } catch (error) {
        setBusinessDetailsAlreadyUpdated(false);
        console.error("Failed to fetch business details:", error);
      }
    };

    const isMerchant = getCookie("isMerchant");
    if (!isMerchant) navigate("/");

    fetchBusinessDetails();
  }, []);

  const handleOnChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleOnSubmit = async (e) => {
    e.preventDefault();
    // console.log(formData);
    const phoneNumber = getCookie("phoneNumber");
    // console.log(phoneNumber);

    try {
      const response = await postWithToken(
        `api/auth/business/${phoneNumber}`,
        formData
      );
      // console.log(response);
      if (response.status === 201) {
        toast.success("Business Details Updated!!");
        navigate("/");
      } else {
        toast.error("Unable to Update Business Details");
      }
    } catch (e) {
      toast.error("Unknown Error Occured");
    }
  };

  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute bg-gray-300 rounded-lg shadow-lg z-10 flex flex-col">
        <Navbar />
        <div className="bg-white p-4 rounded-xl shadow-md ml-2 mr-2 mb-2 flex-grow overflow-y-auto">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold">Business Details</h2>
          </div>
          <div className="w-[100%] flex flex-col items-center">
            <form
              className="bg-white p-8 rounded-xl shadow-md w-full max-w-md"
              onSubmit={handleOnSubmit}
            >
              <div className="flex flex-row justify-end items-center mb-2">
                <span className="text-gray-900 mr-2">Business Name</span>
                <input
                  type="text"
                  name="businessName"
                  value={formData.businessName}
                  onChange={handleOnChange}
                  disabled={businessDetailsAlreadyUpdated}
                  className="mt-1 block w-2/3 border border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div className="flex flex-row justify-end items-center mb-4">
                <span className="text-gray-900 mr-2">Industry</span>
                <input
                  type="text"
                  name="industry"
                  value={formData.industry}
                  onChange={handleOnChange}
                  disabled={businessDetailsAlreadyUpdated}
                  className="mt-1 block w-2/3 border border-gray-300 rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div className="flex flex-row justify-end items-center mb-4">
                <span className="text-gray-900 mr-2">Address</span>
                <input
                  type="text"
                  name="address"
                  value={formData.address}
                  onChange={handleOnChange}
                  disabled={businessDetailsAlreadyUpdated}
                  className="mt-1 block w-2/3 border border-gray-300 rounded-md p-2"
                />
              </div>
              <div className="flex flex-row justify-end items-center mb-4">
                <span className="text-gray-900 mr-2">City</span>
                <input
                  type="text"
                  name="city"
                  value={formData.city}
                  onChange={handleOnChange}
                  disabled={businessDetailsAlreadyUpdated}
                  className="mt-1 block w-2/3 border  border-gray-300 rounded-md p-2"
                />
              </div>
              <div className="flex flex-row justify-end items-center mb-4">
                <span className="text-gray-900 mr-2">State</span>
                <input
                  type="text"
                  name="state"
                  value={formData.state}
                  onChange={handleOnChange}
                  disabled={businessDetailsAlreadyUpdated}
                  className="mt-1 block w-2/3 border  border-gray-300 rounded-md p-2"
                />
              </div>
              <div className="flex flex-row justify-end items-center mb-4">
                <span className="text-gray-900 mr-2">Zip Code</span>
                <input
                  type="number"
                  name="zipCode"
                  value={formData.zipCode}
                  onChange={handleOnChange}
                  disabled={businessDetailsAlreadyUpdated}
                  className="mt-1 block w-2/3 border border-gray-300 rounded-md p-2"
                />
              </div>
              {!businessDetailsAlreadyUpdated && (
                <button
                  className="w-full bg-blue-500 text-white py-2 rounded-md hover:bg-blue-600 transition"
                  type="submit"
                >
                  Update Business Details
                </button>
              )}
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default BusinessDetails;
