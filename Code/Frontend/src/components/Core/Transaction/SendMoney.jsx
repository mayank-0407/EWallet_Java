import Navbar from "../../Common/Navbar";
import { useEffect, useState } from "react";
import { FaUserCircle } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import RecentContacts from "./RecentContacts";
import { getWithToken } from "../../../services/operations/axiosGetRequest";
import { getCookie } from "../../../services/cookieService";
import { toast } from "react-hot-toast";
import ProfileIconIntitialGenerator from "../../../utils/profileIconIntitialGenerator";

const SendMoney = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [searchResults, setSearchResults] = useState(null);
  const [error, setError] = useState("");
  const [isSearching, setIsSearching] = useState(false);
  const [recentTransactions, setRecentTransactions] = useState([]);
  const myWalletId = getCookie("walletId");
  const myPhoneNumber = getCookie("phoneNumber");
  // console.log(myWalletId, myPhoneNumber);

  const navigate = useNavigate();

  const fetchUserDetailsFromSearchTerm = async (url) => {
    try {
      const res = await getWithToken(url);
      // await new Promise((resolve) => setTimeout(resolve, 1000));
      if (res.status === 200) {
        setSearchResults({
          id: res.data.id,
          name: res.data.name,
          phoneNumber: res.data.phoneNumber,
          walletId: res.data.walletId,
          email: res.data.email,
        });
      } else {
        setSearchResults(null);
        // alert("User Not Found!");
        toast.error("User Not Found");
      }
    } catch (e) {
      setSearchResults(null);
      // alert("User Not Found!");
      toast.error("User Not Found");
    }
  };

  const handleSearch = async () => {
    setError("");
    setSearchResults(null);
    setIsSearching(true);

    if (!searchTerm.trim()) {
      setError("Please enter a phone number or wallet ID.");
      setIsSearching(false);
      return;
    }
    if (searchTerm == myWalletId || searchTerm == myPhoneNumber) {
      toast.error("Self Transfer isn't Possible");
    } else {
      if (searchTerm.length === 10) {
        await fetchUserDetailsFromSearchTerm(
          `api/auth/get/userdetails/${searchTerm}`
        );
      } else if (searchTerm.endsWith("@wallet") && searchTerm.length > 7) {
        await fetchUserDetailsFromSearchTerm(
          `api/auth/get/userdetails/from/walletid/${searchTerm}`
        );
      } else {
        toast.error("Unknown Error");
      }
    }

    setIsSearching(false);
  };
  const handleRecentContactClick = (contact) => {
    navigate(`/sendmoney/${contact.receiverWalletId}`);
  };

  const handleResultClick = (user) => {
    navigate(`/sendmoney/${user.walletId}`);
  };

  useEffect(() => {
    const getRecentTransaction = async () => {
      const senderWalletId = getCookie("walletId");
      const response = await getWithToken(
        `api/transaction/get/five/recent/transaction/by/sender/${senderWalletId}`
      );
      setRecentTransactions(response.data);
    };
    getRecentTransaction();
  }, []);

  return (
    <div className="w-[100%] h-[100%] flex justify-center items-center bg-gray-800 relative">
      <div className="lg:w-[60vw] md:w-[90vw] md:h-[90vh] sm:w-[100vw] sm:h-[100vh] lg:h-[80vh] w-[100vw] h-[100vh] absolute bg-gray-300 rounded-lg shadow-lg z-10 flex flex-col">
        <Navbar />
        <div className="bg-white p-4 rounded-xl shadow-md ml-2 mr-2 mb-2 flex-grow overflow-y-auto">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold">Send Money</h2>
          </div>
          <div className="w-[100%] flex flex-col items-center">
            <div className="mb-4 w-3/4">
              <div className="flex flex-col rounded-md shadow-sm">
                <div className="flex">
                  <input
                    type="text"
                    id="search"
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-tl-md rounded-bl-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                    placeholder="Enter phone number or wallet ID"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    onKeyDown={(e) => {
                      if (e.key === "Enter") {
                        handleSearch();
                      }
                    }}
                  />
                  <button
                    type="button"
                    className="ml-3 inline-flex items-center px-4 py-2 border border-transparent rounded-tr-md rounded-br-md shadow-sm text-sm font-medium text-white bg-[#909bff] hover:bg-[#747dce] focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    onClick={handleSearch}
                    disabled={isSearching}
                  >
                    {isSearching ? "Searching..." : "Search"}
                  </button>
                </div>
                {error && <p className="text-red-500 text-sm mt-2">{error}</p>}

                {searchResults && (
                  <button
                    onClick={() => handleResultClick(searchResults)}
                    className="mt-2 p-4 bg-white rounded-md shadow-md w-full  flex flex-row items-center hover:bg-gray-100 cursor-pointer"
                  >
                    <div className="w-1/6 flex items-center justify-center">
                      <ProfileIconIntitialGenerator
                        size={72}
                        name={searchResults.name}
                      />
                    </div>
                    <div className="w-4/5 flex flex-col items-start justify-center pl-4">
                      <p className="text-lg font-semibold">
                        {searchResults.name}
                      </p>
                      <p className="text-md text-gray-500">
                        {searchResults.phoneNumber || searchResults.walletId}
                      </p>
                      {searchResults.email && (
                        <p className="text-md text-gray-500">
                          {searchResults.email}
                        </p>
                      )}
                    </div>
                  </button>
                )}
              </div>
            </div>
            <RecentContacts
              contacts={recentTransactions}
              onContactClick={handleRecentContactClick}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default SendMoney;
